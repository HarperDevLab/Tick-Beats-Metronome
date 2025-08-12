package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
public class MusicTrackLoader
{

    @Inject
    private TickBeatsMetronomeConfig config;

    @Inject
    UserMusicManager userMusicManager;

    @Inject
    OverlayMessage overlayMessage;




    private static final float BEAT_DURATION_SECONDS = 0.6f;

    private String musicFileName = "";

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
        // Try to get an AudioInputStream for either a user track or downloaded track
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


            //make sure .wav file is 16-bit
            int sampleSize = format.getSampleSizeInBits();
            if(sampleSize != 16){

                //if the track name is a number it's a user track
                if (trackName.matches("\\d+"))
                {
                    //if it's a user track show the user where the file that is the wrong sample size is located
                    String trackLocation = userMusicManager.getUserMusicMap().get(trackName).getAbsolutePath();
                    overlayMessage.show( "User Music Track " + trackName + " is a " + sampleSize + "-bit .wav file but must be 16-bit",
                            trackLocation);

                }else{
                    //if the user modifies a downloaded .wav file, this could fire on what's supposed to be a built-in track
                    overlayMessage.show(  trackName + " isn't 16-bit, Files in the tick-beats/downloads folder aren't meant to be modified",
                             "Delete: " + trackName + " from the hi and lo folder to redownload");
                }
                return null;
            }



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
            return new MusicTrack(musicFileName, beatList, format);
        }
        catch (Exception e)
        {
            // Wrap any loading/parsing exceptions with more context
            throw new RuntimeException("Failed to load track: " + trackName, e);
        }
    }


    /**
     * Gets an AudioInputStream from user music directory or downloaded resource.
     * determines if the track is a downloaded track or a user file, then gets its audioInputStream
     * @param trackName used to determine which track to create the audio stream for,
     *                  - For downloaded tracks, this is the WAV filename (e.g., "sea_shanty_2.wav").
     *                  - For user music, this is a stringified number key (e.g., "1", "2", "3", etc.).
     * @return returns the AudioInputStream for the track
     */
    public AudioInputStream getAudioStream(String trackName)
    {
        try
        {
            // Check if this is a user track (digit string like "1", "2", etc.)
            if (trackName.matches("\\d+"))
            {
                //Not really needed, but make the variable name more accurate
                String trackNumber = trackName;

                //get the user file based on its track id number
                File userFile = userMusicManager.getUserMusicMap().get(trackNumber);
                if (userFile != null && userFile.exists())
                {
                    //Store music file name to be used when we create the music track object
                    musicFileName = trackNumber;
                    return AudioSystem.getAudioInputStream(userFile);
                }
                String titleMessage ="User Music Track " + trackNumber + " Not Found. Save 16-bit .wav files to:";
                String tickBeatsMusicFolder = Paths.get(RuneLite.RUNELITE_DIR.getAbsolutePath(), "tick-beats", "music").toString();
                overlayMessage.show(titleMessage, tickBeatsMusicFolder);


            }
            else
            {
                // If it's not a user track, first check high-quality folder if the user has use high quality music checked
                Path hiPath = Paths.get(RuneLite.RUNELITE_DIR.getAbsolutePath(), "tick-beats", "downloads", "hi", trackName);
                if (Files.exists(hiPath) && config.useHighQualityMusic())
                {
                    //Store music file name to be used when we create the music track object
                    musicFileName = trackName;
                    return AudioSystem.getAudioInputStream(hiPath.toFile());
                }

                // now check low-quality folder
                Path loPath = Paths.get(RuneLite.RUNELITE_DIR.getAbsolutePath(), "tick-beats", "downloads", "lo", trackName);
                if (Files.exists(loPath))
                {
                    //Store music file name to be used when we create the music track object
                    musicFileName = trackName;
                    return AudioSystem.getAudioInputStream(loPath.toFile());
                }

                //if the file isn't found in either folder, display the track hasn't been downloaded yet message
                overlayMessage.show("This Track Hasn't Been Downloaded Yet", "Try a track higher up in the list");

            }

            return null;
        }
        catch (Exception e)
        {
            log.debug("Failed to load audio stream for '{}': {}", trackName, e.getMessage());
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