package com.TickBeatsMetronome;

import javax.sound.sampled.AudioFormat;

/**
 * TickSound
 *
 * Represents a single cached tick sound in memory.
 * Stores both:
 * - The AudioFormat (sample rate, channels, encoding, etc.)
 * - The raw PCM audio data (decoded bytes)
 */
public class TickSound
{
    private final AudioFormat format;
    private final byte[] data;

    public TickSound(AudioFormat format, byte[] data)
    {
        this.format = format;
        this.data = data;
    }

    /**
     * Get the audio format describing the PCM data.
     */
    public AudioFormat getFormat()
    {
        return format;
    }

    /**
     * Get the raw PCM audio data for this sound.
     */
    public byte[] getData()
    {
        return data;
    }
}