package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

import javax.inject.Singleton;
import java.io.File;
import java.util.*;

@Slf4j
@Singleton
public class UserMusicManager
{
    // Path to the directory where users can drop .wav files
    private static final File MUSIC_DIRECTORY = new File(RuneLite.RUNELITE_DIR, "tick-beats/music");

    // Stores user sound files keyed by their ID ("1", "2", etc.)
    private final Map<String, File> userMusicMap = new HashMap<>();

    // List of music files
    private List<File> userFiles = new ArrayList<>();

    /**
     * Loads all .wav Files from the user's sound directory. for access with "getUserMusicMap()"
     * Note: it doesn't add all the music data itself to memory, just File objects which contains things like the file names
     * This should be called once during plugin startup.
     */
    public void loadUserMusic()
    {
        // Clear any previously loaded music files
        userFiles.clear();
        userMusicMap.clear();

        // Ensure directory exists
        if (!MUSIC_DIRECTORY.exists())
        {
            // If the directory doesn't exist, create it
            boolean created = MUSIC_DIRECTORY.mkdirs();
            if (!created)
            {
                log.debug("Could not create user music directory: {}", MUSIC_DIRECTORY.getAbsolutePath());
                return;
            }
        }

        // List all .wav files in the sound directory
        File[] files = MUSIC_DIRECTORY.listFiles();
        if (files == null)
        {
            log.debug("Could not read music directory contents, or no files exist.");
            return;
        }

        for (File file : files)
        {
            // Only accept regular files that end in .wav
            if (file.isFile() && file.getName().toLowerCase().endsWith(".wav"))
            {
                userFiles.add(file);
                log.debug("Discovered user music file: {}", file.getName());
            }
        }

        // Sort the files by filename
        userFiles.sort(Comparator.comparing(File::getName));

        log.debug("Total user music files loaded: {}", userFiles.size());

        // Create the userMusicMap attaching an id to each sound file
        // this will be used to access user sound files based on a number alone so we don't need to work with file names
        int i = 0;
        for (File file : userFiles)
        {
            // Increment first to make the ids 1 based instead of 0 based
            i++;
            String fileId = String.valueOf(i);
            userMusicMap.put(fileId, file);
            log.debug("Registered user music [{}]: {}", fileId, file.getName());
        }
    }

    /**
     * Returns a collection of user sound files.
     * the files will be accessed with string integers matching the user track number
     * so a key of "1" will return the file for User Track 1, a key of "2" will give the File for User Track 2 and so on
     */
    public Map<String, File> getUserMusicMap()
    {
        return userMusicMap;
    }
}