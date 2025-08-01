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
            case 3: playBeat3(tickCount); break;
            default: playBeat1(tickCount); break;
        }
    }

    /**
     * Plays the configured sound for the given tick using the DEFAULT set
     */
    private void playBeat1(int tickCount)
    {
        TickSoundOption soundOption;

        switch (tickCount)
        {
            case 1: soundOption = config.beat1Tick1Sound(); break;
            case 2: soundOption = config.beat1Tick2Sound(); break;
            case 3: soundOption = config.beat1Tick3Sound(); break;
            case 4: soundOption = config.beat1Tick4Sound(); break;
            case 5: soundOption = config.beat1Tick5Sound(); break;
            case 6: soundOption = config.beat1Tick6Sound(); break;
            case 7: soundOption = config.beat1Tick7Sound(); break;
            case 8: soundOption = config.beat1Tick8Sound(); break;
            case 9: soundOption = config.beat1Tick9Sound(); break;
            default: soundOption = TickSoundOption.OFF; break;
        }

        if (soundOption != TickSoundOption.OFF)
        {
            play(soundOption.getFileName());
        }
    }

    /**
     * Plays the configured sound for the given tick (1–10) using the ALTERNATE set
     */
    private void playBeat2(int tickCount)
    {
        TickSoundOption soundOption;

        switch (tickCount)
        {
            case 1: soundOption = config.beat2Tick1Sound(); break;
            case 2: soundOption = config.beat2Tick2Sound(); break;
            case 3: soundOption = config.beat2Tick3Sound(); break;
            case 4: soundOption = config.beat2Tick4Sound(); break;
            case 5: soundOption = config.beat2Tick5Sound(); break;
            case 6: soundOption = config.beat2Tick6Sound(); break;
            case 7: soundOption = config.beat2Tick7Sound(); break;
            case 8: soundOption = config.beat2Tick8Sound(); break;
            case 9: soundOption = config.beat2Tick9Sound(); break;
            default: soundOption = TickSoundOption.OFF; break;
        }

        if (soundOption != TickSoundOption.OFF)
        {
            play(soundOption.getFileName());
        }
    }

    /**
     * Plays the configured sound for the given tick (1–10) using the third set
     */
    private void playBeat3(int tickCount)
    {
        TickSoundOption soundOption;

        switch (tickCount)
        {
            case 1: soundOption = config.beat3Tick1Sound(); break;
            case 2: soundOption = config.beat3Tick2Sound(); break;
            case 3: soundOption = config.beat3Tick3Sound(); break;
            case 4: soundOption = config.beat3Tick4Sound(); break;
            case 5: soundOption = config.beat3Tick5Sound(); break;
            case 6: soundOption = config.beat3Tick6Sound(); break;
            case 7: soundOption = config.beat3Tick7Sound(); break;
            case 8: soundOption = config.beat3Tick8Sound(); break;
            case 9: soundOption = config.beat3Tick9Sound(); break;
            default: soundOption = TickSoundOption.OFF; break;
        }

        if (soundOption != TickSoundOption.OFF)
        {
            play(soundOption.getFileName());
        }
    }




    /**
     * Plays a sound using its key (from TickSoundOption or user file ID).
     *
     * @param fileIdentifier TickSoundOption name or user file ID (e.g., "tick-hihat.wav" or "1") found in TickSoundOptions
     */
    public void play(String fileIdentifier)
    {


        //get the key and normalize it to lowercase for better matching
        //I've changed things a bit since implementing, I'm not certain this is still necessary
        String normalizedKey = fileIdentifier.toLowerCase();

        Map<String, File> userSoundMap = userSoundManager.getUserSoundMap();

        // Check UserSoundFiles Map to see if our Key matches a user added sound first
        File userFile = userSoundMap.get(normalizedKey);
        if (userFile != null)
        {
            try
            {
                audioPlayer.play(userFile, 1.0f);
            }
            catch (Exception e)
            {
                log.debug("Failed to play user sound '{}': {}", normalizedKey, e.getMessage());
            }
            return;
        }

        // Otherwise, try to play a built-in resource
        String resourcePath = "/com/TickBeatsMetronome/Sounds/" + normalizedKey;
        try
        {
            audioPlayer.play(getClass(), resourcePath, 1.0f);
        }
        catch (Exception e)
        {
            log.debug("Failed to play sound '{}': {}", normalizedKey, e.getMessage());
        }
    }

}
