package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.audio.AudioPlayer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages playback of both built-in and user-supplied sound files using RuneLite's AudioPlayer.
 */
@Slf4j
@Singleton
public class AudioClipManager
{
    @Inject
    private AudioPlayer audioPlayer;

    @Inject
    private UserSoundManager userSoundManager;

    // Stores user sounds keyed by their ID ("1", "2", etc.)
    private final Map<String, File> userSoundFiles = new HashMap<>();

    /**
     * Loads all User audio files.
     * Should be called once during plugin startup.
     */
    public void loadAllAudioFiles()
    {
        // Load user sounds
        userSoundManager.loadUserSounds();
        List<File> files = userSoundManager.getUserSoundFiles();

        int i = 0;
        for (File file : files)
        {
            i++;
            String fileId = String.valueOf(i);
            userSoundFiles.put(fileId, file);
            log.info("Registered user sound [{}]: {}", fileId, file.getName());
        }
    }


    /**
     * Plays a sound using its key (from TickSoundOption or user file ID).
     *
     * @param key TickSoundOption name or user file ID (e.g., "tick-hihat.wav" or "1")
     */
    public void play(String key)
    {
        //get the key and normalize it to lowercase for better matching
        //I've changed things a bit since implementing, I'm not certain this is still necessary
        String normalizedKey = key.toLowerCase();

        // Check UserSoundFiles Map to make sure our key doesn't match a user added sound first
        File userFile = userSoundFiles.get(normalizedKey);
        if (userFile != null)
        {
            try
            {
                audioPlayer.play(userFile, 1.0f);
            }
            catch (Exception e)
            {
                log.error("Failed to play user sound '{}': {}", normalizedKey, e.getMessage());
            }
            return;
        }

        // Otherwise, try to play a built-in resource
        String resourcePath = "/com/TickBeatsMetronome/" + normalizedKey;
        try
        {
            audioPlayer.play(getClass(), resourcePath, 1.0f);
        }
        catch (Exception e)
        {
            log.error("Failed to play built-in sound '{}': {}", normalizedKey, e.getMessage());
        }
    }
}
