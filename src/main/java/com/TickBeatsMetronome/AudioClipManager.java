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
     * Loads a .wav sound file from the plugin's resources directory and stores it in memory
     * for quick playback later. This version should correctly both in development (file system)
     * and when the plugin is deployed as a JAR via RuneLite's plugin hub.
     *
     * @param name          A name used as a key in the sound map
     * @param resourcePath  The resource path to the sound file (e.g. "/com/TickBeatsMetronome/tick-hihat.wav")
     */
    private void load(String name, String resourcePath)
    {
        // Normalize the name by converting it to lowercase and replacing underscores with dashes
        // This ensures consistency regardless of how it's referenced elsewhere in the code
        String normalizedName = name.toLowerCase().replace('_', '-');

        // Try-with-resources ensures the stream is closed automatically, even if exceptions occur
        try (var stream = getClass().getResourceAsStream(resourcePath))
        {
            // If the resource is missing or the path is incorrect, warn and skip loading
            if (stream == null)
            {
                log.warn("Could not find sound resource: {}", resourcePath);
                return;
            }

            // Read the raw audio stream from the embedded resource
            AudioInputStream originalIn = AudioSystem.getAudioInputStream(stream);

            // Get the format of the input audio file (may not yet be PCM_SIGNED)
            AudioFormat format = originalIn.getFormat();

            // If the audio isn't already in a compatible signed PCM format, convert it
            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED)
            {
                // Create a new AudioFormat with PCM_SIGNED encoding and appropriate settings
                format = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,     // Convert to signed PCM
                        format.getSampleRate(),              // Use the same sample rate
                        16,                                  // Force 16-bit samples
                        format.getChannels(),                // Keep mono/stereo channel count
                        format.getChannels() * 2,            // Frame size (2 bytes per channel)
                        format.getSampleRate(),              // Frame rate = sample rate
                        false                                // Little-endian
                );

                // Apply the format conversion to the stream
                originalIn = AudioSystem.getAudioInputStream(format, originalIn);
            }

            // Read the full audio data into memory as a byte array
            byte[] data = originalIn.readAllBytes();

            // Store the sound data and its format in the sounds map under the normalized name
            sounds.put(normalizedName, new SoundData(format, data));

            // Logging for verification
            log.info("Loaded sound: {}", normalizedName);
            log.info("From resource path: {}", resourcePath);
        }
        // Catch unsupported or corrupted audio formats and log the error
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