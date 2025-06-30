package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

import javax.inject.Singleton;
import java.io.File;
import java.util.*;

/**
 * Handles finding and managing user-supplied sound files from the
 * RuneLite plugin configuration directory.
 * This class creates a map of audio files which can be accessed via getUserSoundMap()
 *   ~/.runelite/tick-beats/sounds/*
 * Is the expected location of sound files, it should create this folder on startup:
 * All user-supplied files must be `.wav` format.
 */
@Slf4j
@Singleton
public class UserSoundManager
{
    // Path to the directory where users can drop .wav files
    private static final File SOUND_DIRECTORY = new File(RuneLite.RUNELITE_DIR, "tick-beats/sounds");

    // Stores user sound files keyed by their ID ("1", "2", etc.)
    private final Map<String, File> userSoundMap = new HashMap<>();

    // list of sound files
    private List<File> userFiles = new ArrayList<>();



    /**
     * Loads all .wav files from the user's sound directory.
     * This should be called once during plugin startup.
     */
    public void loadUserSounds()
    {
        // Clear any previously loaded sounds (safe for reloads)
        userFiles.clear();
        userSoundMap.clear();

        // Ensure directory exists
        if (!SOUND_DIRECTORY.exists())
        {
            boolean created = SOUND_DIRECTORY.mkdirs();
            if (!created)
            {
                log.debug("Could not create user sound directory: {}", SOUND_DIRECTORY.getAbsolutePath());
                return;
            }
        }

        // List all .wav files in the sound directory
        File[] files = SOUND_DIRECTORY.listFiles();
        if (files == null)
        {
            log.debug("Could not read sound directory contents.");
            return;
        }

        for (File file : files)
        {
            // Only accept regular files that end in .wav (case-insensitive)
            if (file.isFile() && file.getName().toLowerCase().endsWith(".wav"))
            {
                userFiles.add(file);
                log.debug("Discovered user sound file: {}", file.getName());
            }
        }

        //sort the files by filename
        userFiles.sort(Comparator.comparing(File::getName));

        log.debug("Total user sound files loaded: {}", userFiles.size());

        //create the userSoundMap attaching an id to each sound file for access
        int i = 0;
        for (File file : userFiles)
        {
            i++;
            String fileId = String.valueOf(i);
            userSoundMap.put(fileId, file);
            log.debug("Registered user sound [{}]: {}", fileId, file.getName());
        }
    }



    /**
     * Returns a collection of all user sound files.
     */

    public Map<String, File> getUserSoundMap()
    {
        return userSoundMap;
    }

}