package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * TickSoundCache stores all tick sound files in memory to avoid repeatedly reading them from disk.
 *
 * Since tick sounds are small and played frequently, it is much more efficient to load them once
 * into a map (keyed by resource name) during startup or configuration changes. This allows for
 * fast retrieval and playback with minimal I/O overhead.
 */

@Slf4j
@Singleton
public class TickSoundCache
{
    // Limit user sound size to 1 MB to avoid putting too much data in memory
    private static final int MAX_FILE_SIZE_BYTES = 1024 * 1024; // 1 MB

    // Map of cached tick sounds: TickSound (format + data).
    private final Map<String, TickSound> cachedSounds = new HashMap<>();

    @Inject
    private UserSoundManager userSoundManager;

    /**
     * Preload and cache all sounds.
     * This should be called once on plugin startup.
     */
    public void cacheTickSounds(){
        cacheAllBuiltInSounds();
        cacheAllUserSounds();
    }

    /**
     * Preload and cache all built-in sounds (from resources).
     */
    public void cacheAllBuiltInSounds()
    {
        for (TickSoundOption option : TickSoundOption.values())
        {
            if (option == TickSoundOption.OFF || option.isUserSound())
            {
                continue;
            }
            cacheBuiltInSound(option.getResourceName());
        }
    }

    /**
     * Preload and cache all user sounds (from tick-beats/sounds directory).
     * This can be called once on startup, or refreshed if user updates files.
     */
    public void cacheAllUserSounds()
    {
        // Clear old user sounds (Any key that's a stringified int which indicates a user sound)
        // in case the user removed one or the order has changed
        cachedSounds.keySet().removeIf(key -> key.matches("\\d+"));

        //make sure user sound files are up to date with any changes the user may have made
        userSoundManager.loadUserSounds();

        // Now add all the user sounds to the cache
        Map<String, File> userSounds = userSoundManager.getUserSoundMap();
        for (Map.Entry<String, File> userSound : userSounds.entrySet())
        {
            cacheUserSound(userSound.getKey(), userSound.getValue());
        }
    }

    /**
     * Cache a single built-in resource sound.
     *
     * @param resourceName WAV filename in /com/TickBeatsMetronome/Sounds/
     */
    private void cacheBuiltInSound(String resourceName)
    {
        String resourcePath = "/com/TickBeatsMetronome/Sounds/" + resourceName;

        try (InputStream inputStream = TickSoundCache.class.getResourceAsStream(resourcePath))
        {
            if (inputStream == null)
            {
                log.warn("Built-in sound not found: {}", resourcePath);
                return;
            }

            //I've read it's better to use buffered input stream when using an internal sound file
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                 AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream))
            {
                cacheFromAudioInputStream(resourceName, audioInputStream);
            }
        }
        catch (Exception e)
        {
            log.warn("Failed to cache built-in sound {}: {}", resourceName, e.getMessage());
        }
    }

    /**
     * Cache a single user-provided sound.
     *
     * @param key  Identifier (e.g. "1", "2", etc.)
     * @param file User's sound file
     */
    private void cacheUserSound(String key, File file)
    {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file))
        {
            cacheFromAudioInputStream(key, audioInputStream);
        }
        catch (Exception e)
        {
            log.debug("Failed to cache user sound {}: {}", key, e.getMessage());
        }
    }

    /**
     * Decode and store a sound from an AudioInputStream.
     * Truncates files larger than 1 MB and shows an overlay warning.
     *
     * @param soundKey         Key used for lookup (e.g., "tick-hihat.wav" or "1")
     * @param audioInputStream The decoded audio stream from a WAV file or resource
     */
    private void cacheFromAudioInputStream(String soundKey, AudioInputStream audioInputStream) throws IOException
    {
        AudioFormat audioFormat = audioInputStream.getFormat();

        // Read up to MAX_FILE_SIZE_BYTES of audio data (will truncate if larger)
        byte[] rawAudioData = audioInputStream.readNBytes(MAX_FILE_SIZE_BYTES);

        // Store the decoded PCM data and format in the cache
        TickSound tickSound = new TickSound(audioFormat, rawAudioData);

        //add the key and tickSound data to the cache map
        cachedSounds.put(soundKey, tickSound);

        log.debug("Cached sound {} ({} bytes, {} Hz)", soundKey, rawAudioData.length, audioFormat.getSampleRate());
    }

    /**
     * Retrieve a CachedSound (format + PCM data) by key.
     *
     * @param key File name or user sound number
     * @return CachedSound or null if not cached
     */
    public TickSound getSound(String key)
    {
        return cachedSounds.get(key);
    }

    /**
     * Clear ALL cached sounds (both built-in + user).
     */
    public void clearAll()
    {
        cachedSounds.clear();
        log.debug("Cleared all cached tick sounds");
    }

}