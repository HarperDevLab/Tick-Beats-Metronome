package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sound.sampled.*;
import java.io.File;
import java.nio.file.Paths;


/**
 * This Class is used to determine which sounds are played and play them
 */
@Slf4j
@Singleton
public class TickSoundManager {

    @Inject
    TickBeatsMetronomeConfig config;

    @Inject
    UserSoundManager userSoundManager;

    @Inject
    OverlayMessage overlayMessage;

    @Inject
    private TickSoundCache tickSoundCache;


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
     * Plays the configured sound for the given tick (1–9) using the DEFAULT set (Beat 1).
     */
    private void playBeat1(int tickCount)
    {
        TickSoundOption soundOption;
        int volume;

        switch (tickCount)
        {
            case 1:  soundOption = config.beat1Tick1Sound(); volume = config.beat1Tick1Volume(); break;
            case 2:  soundOption = config.beat1Tick2Sound(); volume = config.beat1Tick2Volume(); break;
            case 3:  soundOption = config.beat1Tick3Sound(); volume = config.beat1Tick3Volume(); break;
            case 4:  soundOption = config.beat1Tick4Sound(); volume = config.beat1Tick4Volume(); break;
            case 5:  soundOption = config.beat1Tick5Sound(); volume = config.beat1Tick5Volume(); break;
            case 6:  soundOption = config.beat1Tick6Sound(); volume = config.beat1Tick6Volume(); break;
            case 7:  soundOption = config.beat1Tick7Sound(); volume = config.beat1Tick7Volume(); break;
            case 8:  soundOption = config.beat1Tick8Sound(); volume = config.beat1Tick8Volume(); break;
            case 9:  soundOption = config.beat1Tick9Sound(); volume = config.beat1Tick9Volume(); break;
            default: soundOption = TickSoundOption.OFF; volume = 100; break;
        }

        if (soundOption != TickSoundOption.OFF)
        {
            playSound(soundOption.getResourceName(), volume);
        }
    }

    /**
     * Plays the configured sound for the given tick (1–9) using the ALTERNATE set (Beat 2).
     */
    private void playBeat2(int tickCount)
    {
        TickSoundOption soundOption;
        int volume;

        switch (tickCount)
        {
            case 1:  soundOption = config.beat2Tick1Sound(); volume = config.beat2Tick1Volume(); break;
            case 2:  soundOption = config.beat2Tick2Sound(); volume = config.beat2Tick2Volume(); break;
            case 3:  soundOption = config.beat2Tick3Sound(); volume = config.beat2Tick3Volume(); break;
            case 4:  soundOption = config.beat2Tick4Sound(); volume = config.beat2Tick4Volume(); break;
            case 5:  soundOption = config.beat2Tick5Sound(); volume = config.beat2Tick5Volume(); break;
            case 6:  soundOption = config.beat2Tick6Sound(); volume = config.beat2Tick6Volume(); break;
            case 7:  soundOption = config.beat2Tick7Sound(); volume = config.beat2Tick7Volume(); break;
            case 8:  soundOption = config.beat2Tick8Sound(); volume = config.beat2Tick8Volume(); break;
            case 9:  soundOption = config.beat2Tick9Sound(); volume = config.beat2Tick9Volume(); break;
            default: soundOption = TickSoundOption.OFF; volume = 100; break;
        }

        if (soundOption != TickSoundOption.OFF)
        {
            playSound(soundOption.getResourceName(), volume);
        }
    }

    /**
     * Plays the configured sound for the given tick (1–9) using the THIRD set (Beat 3).
     */
    private void playBeat3(int tickCount)
    {
        TickSoundOption soundOption;
        int volume;

        switch (tickCount)
        {
            case 1:  soundOption = config.beat3Tick1Sound(); volume = config.beat3Tick1Volume(); break;
            case 2:  soundOption = config.beat3Tick2Sound(); volume = config.beat3Tick2Volume(); break;
            case 3:  soundOption = config.beat3Tick3Sound(); volume = config.beat3Tick3Volume(); break;
            case 4:  soundOption = config.beat3Tick4Sound(); volume = config.beat3Tick4Volume(); break;
            case 5:  soundOption = config.beat3Tick5Sound(); volume = config.beat3Tick5Volume(); break;
            case 6:  soundOption = config.beat3Tick6Sound(); volume = config.beat3Tick6Volume(); break;
            case 7:  soundOption = config.beat3Tick7Sound(); volume = config.beat3Tick7Volume(); break;
            case 8:  soundOption = config.beat3Tick8Sound(); volume = config.beat3Tick8Volume(); break;
            case 9:  soundOption = config.beat3Tick9Sound(); volume = config.beat3Tick9Volume(); break;
            default: soundOption = TickSoundOption.OFF; volume = 100; break;
        }

        if (soundOption != TickSoundOption.OFF)
        {
            playSound(soundOption.getResourceName(), volume);
        }
    }

    /**
     * Plays a sound by its resource name at the specified volume.
     *
     * Retrieves the TickSound from cache, validates the audio data,
     * and plays it using Java's Clip system. Also displays helpful
     * error messages for common issues with user-provided sound files.
     *
     * @param resourceName The resource name or key (e.g., "snare.wav" or "1" for user sounds)
     * @param volume Volume to play at (0–150) Over 100 for boosted volume if you want to emphasize a sound
     */
    public void playSound(String resourceName, int volume) {
        // Attempt to retrieve the sound from the cache
        TickSound sound = tickSoundCache.getSound(resourceName);

        //////////////////////////////////////////////
        //////////// Sound Error Handling ////////////
        //////////////////////////////////////////////

        // If the sound wasn't found in the cache
        if (sound == null) {

            // If the name looks like a user sound (digits only, like "1", "2", etc.)
            if (resourceName.matches("\\d+")) {
                // Suggest to the user where to place valid user sound files
                String soundsFolder = Paths.get(
                        RuneLite.RUNELITE_DIR.getAbsolutePath(),
                        "tick-beats", "sounds"
                ).toString();

                overlayMessage.show(
                        "User Sound " + resourceName + " Not Found. Add 16-bit .wav files to:",
                        soundsFolder
                );
            } else {
                // Otherwise it's likely a built-in sound and should have been cached

                // Uncomment the overlayMessage below for a visual error for failed built-in sounds,
                // I ended up commenting it out as I thought it may be annoying to a user, as it doesn't provide very useful info
                // overlayMessage.show("Something went wrong playing sound: " + resourceName, "Try restarting Tick Beats");
            }

            // Log the failure for debugging purposes
            log.debug("Sound {} not found in cache", resourceName);
            return;
        }

        // Try to get the full path for the user sound (for displaying in error messages)
        String soundFilePath = "";
        File userSoundFile = userSoundManager.getUserSoundMap().get(resourceName);
        if (userSoundFile != null) {
            soundFilePath = userSoundFile.getAbsolutePath();
        }

        // Check for file size exceeding 1MB (limit for user sounds)
        if (sound.getData().length >= 1024 * 1024) {
            overlayMessage.show(
                    "User Sound " + resourceName + " is too large (must be under 1MB)",
                    soundFilePath
            );
            return;
        }

        // Check that the audio is 16-bit (other formats may cause playback issues)
        int sampleSize = sound.getFormat().getSampleSizeInBits();
        if (sampleSize != 16) {
            overlayMessage.show(
                    "User Sound " + resourceName + " is " + sampleSize + "-bit. Must be 16-bit.",
                    soundFilePath
            );
            return;
        }

        //////////////////////////////////////////////
        //////////// Try to Play the Sound ///////////
        //////////////////////////////////////////////

        try {
            // Create a Clip instance from the system
            Clip clip = AudioSystem.getClip();

            // Load raw audio data into the clip
            clip.open(sound.getFormat(), sound.getData(), 0, sound.getData().length);

            // Apply the user-configured volume setting
            setClipVolume(clip, volume);

            // Ensure the clip is closed automatically after playing
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

            // Begin playback (non-blocking)
            clip.start();
        }
        catch (Exception e) {
            // Show error overlay if something went wrong during playback
            overlayMessage.show("Unable to play sound file", soundFilePath);

            // Log the full exception message for debugging
            log.debug("Failed to play sound {}: {}", resourceName, e.getMessage());
        }
    }

    /**
     * Set the clips volume
     * @param clip the audio clip to adjust the volume for
     * @param volume the volume percentage to set the clip at, ex. 100. supports over 100 for boosted audio
     */
    private void setClipVolume(Clip clip, float volume)
    {
        // Check if the clip supports master gain control (volume)
        // if not return
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
        {
            return;
        }

        FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        double dB;

        // Avoid log10(0) (log10 shouldn't be 0 or negative) and handle 0 volume
        if (volume <= 0)
        {
            dB = volumeControl.getMinimum();
        }
        else
        {
            // Convert percentage to a volume ratio (0.0–1.0)
            float volumePercent = volume / 100f;

            // Convert to decibels using a log scale
            dB = Math.log10(volumePercent) * 20.0;
        }

        // Clamp volume to the supported dB range
        float minDb = volumeControl.getMinimum();
        float maxDb = volumeControl.getMaximum();

        if (dB < minDb)
        {
            dB = minDb;
        }
        else if (dB > maxDb)
        {
            dB = maxDb;
        }

        // Apply the decibel volume to the clip
        volumeControl.setValue((float) dB);
    }
}
