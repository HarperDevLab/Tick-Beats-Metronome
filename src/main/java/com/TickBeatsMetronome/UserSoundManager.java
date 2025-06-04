package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

import javax.inject.Singleton;
import java.io.File;
import java.util.*;

/**
 * Handles finding and managing user-supplied sound files from the
 * RuneLite plugin configuration directory.
 * This class creates a list audio files which can be accessed via getUserSoundFiles()
 * The actual loading of the files into memory happens in AudioClipManager
 * Expected location of sound files, it should create this folder on startup:
 *   ~/.runelite/tick-beats/sounds/*
 * All user-supplied files must be `.wav` format.
 */
@Slf4j
@Singleton
public class UserSoundManager
{
    // Path to the directory where users can drop .wav files
    private static final File SOUND_DIRECTORY = new File(RuneLite.RUNELITE_DIR, "tick-beats/sounds");

    // list of sound files
    private List<File> userSoundFiles = new ArrayList<>();




    /**
     * Loads all .wav files from the user's sound directory.
     * This should be called once during plugin startup.
     */
    public void loadUserSounds()
    {
        // Clear any previously loaded sounds (safe for reloads)
        userSoundFiles.clear();

        // Ensure directory exists
        if (!SOUND_DIRECTORY.exists())
        {
            boolean created = SOUND_DIRECTORY.mkdirs();
            if (!created)
            {
                log.info("Could not create user sound directory: {}", SOUND_DIRECTORY.getAbsolutePath());
                return;
            }
        }

        // List all .wav files in the sound directory
        File[] files = SOUND_DIRECTORY.listFiles();
        if (files == null)
        {
            log.info("Could not read sound directory contents.");
            return;
        }

        for (File file : files)
        {
            // Only accept regular files that end in .wav (case-insensitive)
            if (file.isFile() && file.getName().toLowerCase().endsWith(".wav"))
            {
                userSoundFiles.add(file);
                log.info("Discovered user sound file: {}", file.getName());
            }
        }

        log.info("Total user sound files loaded: {}", userSoundFiles.size());
    }




    /**
     * Returns a collection of all user sound files.
     * Used for loading them into AudioClipManager.
     */
    public List<File> getUserSoundFiles()
    {
        userSoundFiles.sort(Comparator.comparing(File::getName));
        return userSoundFiles;
    }
}