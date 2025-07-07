package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
public class MusicTrackLoader
{
    @Inject
    UserMusicManager userMusicManager;

    private static final float BEAT_DURATION_SECONDS = 0.6f;

    /**
     * Loads a music track either from embedded plugin resources or from user-supplied files.
     * The track is split into 600ms segments.
     *
     * @param trackName The name or ID of the track to load.
     *                  - For built-in tracks, this is the WAV filename (e.g., "sea_shanty_2.wav").
     *                  - For user music, this is a stringified number key (e.g., "1", "2", "3", etc.).
     * @return A MusicTrack object containing the parsed beats and audio format, or null if the track is missing.
     */
    public MusicTrack loadFromResource(String trackName)
    {
        // Try to get an AudioInputStream for either a user track or embedded resource
        try (AudioInputStream stream = getAudioStream(trackName))
        {
            // If the track couldn't be loaded (not found, unreadable, etc.), skip loading.
            if (stream == null)
            {
                log.debug("Track '{}' could not be loaded (stream was null). Skipping load.", trackName);
                return null;
            }

            // Get audio format info (e.g., sample rate, bit depth, channels)
            AudioFormat format = stream.getFormat();

            // Calculate audio segment (beat) size in bytes based on duration and format
            int frameSize = format.getFrameSize();                  // e.g., 4 bytes for 16-bit stereo
            float frameRate = format.getFrameRate();                // e.g., 44100 Hz
            int framesPerBeat = (int)(frameRate * BEAT_DURATION_SECONDS);  // e.g., 44100 * 0.6 = 26460 frames per beat
            int bytesPerBeat = framesPerBeat * frameSize;           // e.g., 26460 * 4 = 105840 bytes per beat

            // Read the entire audio file into memory
            byte[] fullAudio = stream.readAllBytes();
            int totalBeats = fullAudio.length / bytesPerBeat;       // Determine how many full 600ms beats exist

            // List to store beat objects for this track
            List<MusicBeat> beatList = new ArrayList<>();

            // Iterate through each full beat segment and extract audio data for it
            for (int i = 0; i < totalBeats; i++)
            {
                int start = i * bytesPerBeat;
                int end = Math.min(start + bytesPerBeat, fullAudio.length); // Handle potential edge cases at end of track
                byte[] beatData = new byte[end - start];

                // Copy just the data for this beat
                System.arraycopy(fullAudio, start, beatData, 0, beatData.length);

                // Calculate the musical bar and beat position (1-based indexing)
                int barNumber = (i / MusicTrack.BEATS_PER_BAR) + 1;
                int beatNumber = (i % MusicTrack.BEATS_PER_BAR) + 1;

                // Add the beat to our list
                beatList.add(new MusicBeat(barNumber, beatNumber, beatData));
            }

            // Remove any leftover partial bar at the end (e.g., trailing silence)
            beatList = trimIncompleteBar(beatList);

            // Package everything into a MusicTrack object
            return new MusicTrack(trackName, beatList, format);
        }
        catch (Exception e)
        {
            // Wrap any loading/parsing exceptions with more context
            throw new RuntimeException("Failed to load track: " + trackName, e);
        }
    }


    /**
     * Gets an AudioInputStream from user music directory or embedded resource.
     * determines if the track is an embedded resource or a user file, then gets its audioInputStream
     * @param trackName used to determine which track to create the audio stream for,
     *                  will be a file name for embedded tracks and a string int for user tracks
     * @return returns the AudioInputStream for the track
     */
    public AudioInputStream getAudioStream(String trackName)
    {
        //attempt to get a user file based on the track name (might be better to just check if the track name is an int string)
        File userFile = userMusicManager.getUserMusicMap().get(trackName);
        try
        {
            //if it's a user file that's been selected by the user return  the audioInputStream for that
            if (userFile != null && userFile.exists())
            {
                return AudioSystem.getAudioInputStream(userFile);
            }


            //if it's an already built in song, return that
            InputStream resourceStream = MusicTrack.class.getResourceAsStream("/com/TickBeatsMetronome/Music/" + trackName);
            if (resourceStream != null)
            {
                return AudioSystem.getAudioInputStream(resourceStream);
            }


            //if the track is neither a user track nor an audio track, it's likely that the user selected
            //a user track that doesn't exist, let's output audio that tells them how to properly add
            //their own music tracks
            resourceStream = MusicTrack.class.getResourceAsStream("/com/TickBeatsMetronome/music_error_message.wav");

            return AudioSystem.getAudioInputStream(resourceStream);


        }
        catch (Exception e)
        {
            log.error("Failed to load audio stream for '{}': {}", trackName, e.getMessage());
            return null;
        }
    }

    /**
     * Removes trailing incomplete bar from the beat list if it exists.
     */
    private List<MusicBeat> trimIncompleteBar(List<MusicBeat> beats)
    {
        int totalBeats = beats.size();
        int remainder = totalBeats % MusicTrack.BEATS_PER_BAR;

        if (remainder == 0)
        {
            return beats;
        }

        for (int i = 0; i < remainder; i++)
        {
            beats.remove(beats.size() - 1);
        }

        return beats;
    }

}