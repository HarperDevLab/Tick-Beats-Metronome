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

    /**
     * Loads a music track either from embedded plugin resources or from user-supplied files.
     * The track is split into 600ms segments.
     *
     * @param musicTrackOption The music track option of the track to load.
     *                  - For built-in tracks, this includes the WAV resourceName (e.g., "sea_shanty_2.wav").
     *                  - For user music, this includes a stringified number key resourceName (e.g., "1", "2", "3", etc.).
     * @return A MusicTrack object containing the parsed beats and audio format, or null if the track is missing.
     */
    public MusicTrack loadFromResource(MusicTrackOption musicTrackOption)
    {
        String resourceName = musicTrackOption.getResourceName();

        // Try to get an AudioInputStream for either a user track or downloaded track
        try (AudioInputStream stream = getAudioStream(musicTrackOption))
        {
            // If the track couldn't be loaded (not found, unreadable, etc.), skip loading.
            if (stream == null)
            {
                log.debug("Track '{}' could not be loaded (stream was null). Skipping load.", resourceName);
                return null;
            }

            // Get audio format info (e.g., sample rate, bit depth, channels)
            AudioFormat format = stream.getFormat();

            // Make sure .wav file is 16-bit
            int sampleSize = format.getSampleSizeInBits();
            if(sampleSize != 16){

                // If the music track is a user track
                if (musicTrackOption.isUserMusic())
                {
                    File userFile = userMusicManager.getUserMusicMap().get(resourceName);
                    // the userFile shouldn't be null, but just in case wouldn't want to somehow get a null pointer exception on displaying an error message
                    if (userFile == null)
                    {
                        return null;
                    }

                    // If it's a user track show the user where the file that is the wrong sample size is located
                    String trackLocation = userFile.getAbsolutePath();
                    overlayMessage.show( "User Music Track " + resourceName + " is a " + sampleSize + "-bit .wav file but must be 16-bit",
                            trackLocation);

                }else{
                    //if the user modifies a downloaded .wav file, this could fire on what's supposed to be a built-in track
                    overlayMessage.show(  resourceName + " isn't 16-bit, Files in the tick-beats/downloads folder aren't meant to be modified",
                             "Delete: " + resourceName + " from the hi and lo folder to redownload");
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

            // Get a nice looking track name to include with our MusicTrack object
            String displayName = getDisplayName(musicTrackOption);

            // Package everything into a MusicTrack object
            return new MusicTrack(musicTrackOption, displayName, beatList, format);
        }
        catch (Exception e)
        {
            log.debug("Failed to load track '{}': {}", resourceName, e.getMessage(), e);
            overlayMessage.show("Error loading track:", resourceName);
            return null;
        }
    }

    /**
     * Gets an AudioInputStream from user music directory or downloaded resource.
     * determines if the track is a downloaded track or a user file, then gets its audioInputStream
     * @param musicTrackOption which track to create the audio stream for,
     *                  - For downloaded tracks, this includes a WAV resourceName (e.g., "sea_shanty_2.wav").
     *                  - For user music, this includes a stringified number key resourceName (e.g., "1", "2", "3", etc.).
     * @return returns the AudioInputStream for the track
     */
    public AudioInputStream getAudioStream(MusicTrackOption musicTrackOption)
    {
        String resourceName = musicTrackOption.getResourceName();

        try
        {
            // Check if this is a user added track
            if (musicTrackOption.isUserMusic())
            {
                // Not really needed, but make the variable name more accurate
                String trackNumber = resourceName;

                // Get the user file based on its track id number
                File userFile = userMusicManager.getUserMusicMap().get(trackNumber);
                if (userFile != null && userFile.exists())
                {
                    return AudioSystem.getAudioInputStream(userFile);
                }
                String titleMessage ="User Music Track " + trackNumber + " Not Found. Save 16-bit .wav files to and restart plugin:";
                String tickBeatsMusicFolder = Paths.get(RuneLite.RUNELITE_DIR.getAbsolutePath(), "tick-beats", "music").toString();
                overlayMessage.show(titleMessage, tickBeatsMusicFolder);

            } else {

                // Not really needed, but make the variable name more accurate
                String fileName = resourceName;

                // If it's not a user track, first check high-quality folder if the user has use high quality music checked
                Path hiPath = Paths.get(RuneLite.RUNELITE_DIR.getAbsolutePath(), "tick-beats", "downloads", "hi", fileName);
                if (Files.exists(hiPath) && config.useHighQualityMusic())
                {
                    return AudioSystem.getAudioInputStream(hiPath.toFile());
                }

                // Now check low-quality folder
                Path loPath = Paths.get(RuneLite.RUNELITE_DIR.getAbsolutePath(), "tick-beats", "downloads", "lo", fileName);
                if (Files.exists(loPath))
                {
                    return AudioSystem.getAudioInputStream(loPath.toFile());
                }

                // If the file isn't found in either folder, display the track hasn't been downloaded yet message
                overlayMessage.show("This Track Hasn't Been Downloaded Yet", "Try a track higher up in the list");

            }

            return null;
        }
        catch (Exception e)
        {
            log.debug("Failed to load audio stream for '{}': {}", resourceName, e.getMessage());
            return null;
        }
    }

    /**
     * Removes any trailing beats that do not form a complete musical bar.
     *
     * @param beats
     *     The full list of MusicBeat objects representing the track’s beats.
     *     The list may contain extra beats at the end that do not form a full bar.
     *
     * @return
     *     A possibly shortened list with the incomplete final bar removed.
     */
    private List<MusicBeat> trimIncompleteBar(List<MusicBeat> beats)
    {
        int totalBeats = beats.size();
        int remainder = totalBeats % MusicTrack.BEATS_PER_BAR;

        // If total beats is an exact multiple of beats-per-bar, return unchanged
        if (remainder == 0)
        {
            return beats;
        }

        // Remove the leftover beats so that only full bars remain
        for (int i = 0; i < remainder; i++)
        {
            beats.remove(beats.size() - 1);
        }

        return beats;
    }

    /**
     * Generates a human-friendly display name for a music track option.
     *
     * For user tracks, this method:
     * <ul>
     *   <li>Retrieves the raw filename from the user music directory.</li>
     *   <li>Removes the .wav extension if present.</li>
     *   <li>Replaces underscores and dashes with spaces.</li>
     *   <li>Capitalizes the first letter of each word while lowercasing the rest.</li>
     * </ul>
     * For built-in tracks, the method simply returns the display name provided by the MusicTrackOption.
     *
     * @param musicTrackOption the track option to generate a display name for.
     *
     * @return A beautified, human-readable display name for the given track option.
     */
    public String getDisplayName(MusicTrackOption musicTrackOption) {

        String displayName;

        // If it's a user track, get the tracks file name and make it better looking
        if(musicTrackOption.isUserMusic()){

            // Get the file object from the userMusicMap for the user music track
            File userFile = userMusicManager.getUserMusicMap().get(musicTrackOption.getResourceName());

            // the userFile shouldn't be null, but just in case
            if (userFile == null)
            {
                log.debug("User track {} not found in userMusicMap", musicTrackOption.getResourceName());
                return musicTrackOption.getDisplayName();
            }

            // Get the files name by pulling it from the userMusicMap
            String fileName = userFile.getName();

            // Remove the .wav extension
            if (fileName.toLowerCase().endsWith(".wav")) {
                fileName = fileName.substring(0, fileName.length() - 4);
            }

            // Replace underscores and dashes with spaces
            fileName = fileName.replace('_', ' ')
                    .replace('-', ' ');

            // Split into words and capitalize first letter of each
            StringBuilder result = new StringBuilder();
            for (String word : fileName.split(" ")) {
                if (!word.isEmpty()) {
                    result.append(Character.toUpperCase(word.charAt(0)));
                    if (word.length() > 1) {
                        result.append(word.substring(1).toLowerCase());
                    }
                    result.append(" ");
                }
            }

            // Get our string and remove any leading or trailing whitespace
            displayName = result.toString().trim();

        }else{
            // The displayName for built-in tracks already looks nice, so just use that
            displayName = musicTrackOption.getDisplayName();
        }

        return displayName;
    }
}