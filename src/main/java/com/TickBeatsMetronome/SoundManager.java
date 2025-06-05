package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.Map;

import net.runelite.client.audio.AudioPlayer;


/*
 * This Class is used to determine which sounds are played
 */
@Slf4j
@Singleton
public class SoundManager {

    @Inject
    private AudioPlayer audioPlayer;

    @Inject
    UserSoundManager userSoundManager;

    @Inject
    TickBeatsMetronomeConfig config;


    /**
     * Determines which tick to play on which beat
     */
    public void playSound(int beatNumber, int tickCount)
    {
        switch (beatNumber)
        {
            case 1: playBeat1(tickCount); break;
            case 2: playBeat2(tickCount); break;
            default: playBeat1(tickCount); break;
        }
    }

    /**
     * Plays the configured sound for the given tick (1–8) using the DEFAULT set
     */
    private void playBeat1(int tickCount)
    {
        TickSoundOption option;

        switch (tickCount)
        {
            case 1: option = config.tick1Sound(); break;
            case 2: option = config.tick2Sound(); break;
            case 3: option = config.tick3Sound(); break;
            case 4: option = config.tick4Sound(); break;
            case 5: option = config.tick5Sound(); break;
            case 6: option = config.tick6Sound(); break;
            case 7: option = config.tick7Sound(); break;
            case 8: option = config.tick8Sound(); break;
            default: option = TickSoundOption.OFF; break;
        }

        if (option != TickSoundOption.OFF)
        {
            play(option.getFileName());
        }
    }

    /**
     * Plays the configured sound for the given tick (1–8) using the ALTERNATE set
     */
    private void playBeat2(int tickCount)
    {
        TickSoundOption option;

        switch (tickCount)
        {
            case 1: option = config.beat2Tick1Sound(); break;
            case 2: option = config.beat2Tick2Sound(); break;
            case 3: option = config.beat2Tick3Sound(); break;
            case 4: option = config.beat2Tick4Sound(); break;
            case 5: option = config.beat2Tick5Sound(); break;
            case 6: option = config.beat2Tick6Sound(); break;
            case 7: option = config.beat2Tick7Sound(); break;
            case 8: option = config.beat2Tick8Sound(); break;
            default: option = TickSoundOption.OFF; break;
        }

        if (option != TickSoundOption.OFF)
        {
            play(option.getFileName());
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

        Map<String, File> userSoundMap = userSoundManager.getUserSoundMap();

        // Check UserSoundFiles Map to make sure our key doesn't match a user added sound first
        File userFile = userSoundMap.get(normalizedKey);
        if (userFile != null)
        {
            try
            {
                audioPlayer.play(userFile, 1.0f);
            }
            catch (Exception e)
            {
                log.info("Failed to play user sound '{}': {}", normalizedKey, e.getMessage());
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
