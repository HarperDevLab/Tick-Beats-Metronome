package com.TickBeatsMetronome;

import javax.inject.Inject;
import javax.inject.Singleton;


/*
 * This Class is used to determine which sounds are played
 * The actual audio clips themselves are handled in the AudioClipManager
 */
@Singleton
public class SoundManager {

    // This manages and plays  audio clips
    @Inject
    AudioClipManager audioClipManager;

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
            audioClipManager.play(option.getFileName());
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
            audioClipManager.play(option.getFileName());
        }
    }

}
