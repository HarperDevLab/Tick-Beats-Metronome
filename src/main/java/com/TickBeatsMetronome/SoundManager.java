package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import net.runelite.client.RuneLite;
import net.runelite.client.audio.AudioPlayer;


/*
 * This Class is used to determine which sounds are played
 */
@Slf4j
@Singleton
public class SoundManager {

    @Inject
    TickBeatsMetronomeConfig config;

    @Inject
    private AudioPlayer audioPlayer;

    @Inject
    UserSoundManager userSoundManager;

    @Inject
    OverlayMessage overlayMessage;




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


        // Check if file identifier is a stringified int, if it is, we're looking for a user supplied sound
        if (fileIdentifier.matches("\\d+")){

            //get the user sound map
            Map<String, File> userSoundMap = userSoundManager.getUserSoundMap();

            // If our file identifier is greater than the size of our sound map, then no wav file exists for that sound
            // display a message and return
            if(Integer.parseInt(fileIdentifier) > userSoundMap.size()){

                String titleMessage ="No User Sound " + fileIdentifier + ". Save sounds as 16-bit .wav files to:";
                String tickBeatsSoundsFolder = Paths.get(RuneLite.RUNELITE_DIR.getAbsolutePath(), "tick-beats", "sounds").toString();

                overlayMessage.show(titleMessage, tickBeatsSoundsFolder);

                return;

            }

            // Get the sound file based on it's key
            File userFile = userSoundMap.get(normalizedKey);

            // If the sound file exists try to play it
            if (userFile.exists()) {
                try {
                    audioPlayer.play(userFile, 1.0f);
                } catch (Exception e) {
                    // If the file couldn't play for some reason, display a message to the user
                    String titleMessage = "Couldn't play: " + userFile.getAbsolutePath();
                    overlayMessage.show(titleMessage, "Make sure it's a 16-bit PCM .wav file");

                    log.debug("Failed to play user sound '{}': {}", normalizedKey, e.getMessage());
                }
                return;
            }
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
