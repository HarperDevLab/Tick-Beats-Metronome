package com.TickBeatsMetronome;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AudioClipManager
{

    @Inject
    public AudioClipManager()
    {
        loadAllAudioFiles();
    }

    /**
     * Inner class to hold raw sound data and format.
     * We'll use this to quickly spin up new Clips on demand without reloading the file each time.
     */
    private static class SoundData
    {
        final AudioFormat format;  // Describes the audio (sample rate, channels, encoding)
        final byte[] bytes;        // Raw PCM audio data

        SoundData(AudioFormat format, byte[] bytes)
        {
            this.format = format;
            this.bytes = bytes;
        }
    }

    // Stores sound clips by name
    private final Map<String, SoundData> sounds = new HashMap<>();


    public void loadAllAudioFiles(){
        // Load all the audio files listed in the TickSoundOption enum into memory
        for (TickSoundOption sound : TickSoundOption.values())
        {
            load(sound.name(), "/com/TickBeatsMetronome/" + sound.getFileName());
        }
    }

    /**
     * Loads a sound file from resources into memory for future playback.
     * @param name A human-friendly key name (e.g., "tick-snare")
     * @param resourcePath The full resource path to the .wav file
     */
    private void load(String name, String resourcePath)
    {
        // Normalize the name for consistent internal access
        name = name.toLowerCase().replace('_', '-');

        try
        {
            // Attempt to load the .wav file from the resources directory
            URL url = getClass().getResource(resourcePath);
            if (url == null)
            {
                log.warn("Could not find sound: {}", resourcePath);
                return;
            }

            // Read the audio stream from the file
            AudioInputStream originalIn = AudioSystem.getAudioInputStream(url);
            AudioFormat format = originalIn.getFormat();

            // Ensure the format is compatible with Java's Clip (PCM_SIGNED is required)
            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED)
            {
                format = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED, // Force PCM
                        format.getSampleRate(),          // Keep sample rate (e.g. 44100)
                        16,                              // Force 16-bit audio
                        format.getChannels(),            // Mono or stereo
                        format.getChannels() * 2,        // Frame size in bytes
                        format.getSampleRate(),          // Frame rate = sample rate
                        false                            // Little-endian
                );

                // Convert the stream to the new format
                originalIn = AudioSystem.getAudioInputStream(format, originalIn);
            }

            // Load the entire audio stream into memory
            byte[] data = originalIn.readAllBytes();

            // Store it in our map for fast reuse
            sounds.put(name, new SoundData(format, data));

            log.info("Loaded sound: {}", name);
            log.info("from resource path: {}", resourcePath);
        }
        catch (UnsupportedAudioFileException | IOException e)
        {
            log.error("Unsupported or unreadable sound file: {}", resourcePath, e);
        }
    }

    /**
     * Plays the sound by creating a new Clip instance.
     * This allows the same sound to be played multiple times in quick succession or simultaneously.
     * @param name The key used in `load()`, case- and underscore-insensitive
     */
    public void play(String name)
    {
        // Normalize the name the same way we did in load()
        String normalizedName = name.toLowerCase().replace('_', '-');

        // Retrieve the sound data
        SoundData data = sounds.get(normalizedName);
        if (data == null)
        {
            // Could optionally log, but this is silent to avoid spam on missing config
            return;
        }

        try
        {
            // Create a new Clip instance each time
            Clip audioClip = AudioSystem.getClip();

            // Open the clip using the preloaded audio data and format
            audioClip.open(data.format, data.bytes, 0, data.bytes.length);

            // Start playing the sound
            audioClip.start();

            // Automatically free the resources once the sound finishes
            audioClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP)
                {
                    audioClip.close(); // Close to try to avoid resource leaks
                }
            });
        }
        catch (LineUnavailableException e)
        {
            log.error("Unable to play sound: {}", normalizedName, e);
        }
    }
}