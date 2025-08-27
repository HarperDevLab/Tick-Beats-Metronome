package com.TickBeatsMetronome;

import lombok.Getter;

import javax.inject.Inject;
import javax.sound.sampled.AudioFormat;
import java.util.List;

/**
 * Represents a full music track composed of multiple 600ms beat segments.
 * Tracks are internally structured as a flat list of beats,
 * where every 4 beats form a bar (assuming a 4/4 time signature).
 */
public class MusicTrack
{
    /** The MusicTrackOption for this music track */
    @Getter
    private final MusicTrackOption musicTrackOption;

    /** A nice looking display name to use for displaying the track name */
    @Getter
    private final String displayName;

    /** Flat list of MusicBeat objects that make up the full track */
    @Getter
    private final List<MusicBeat> beats;

    /** Audio format (e.g., 44.1 kHz, 16-bit PCM, stereo) */
    @Getter
    private final AudioFormat format;

    /** Constant: number of beats in a single bar (e.g., 4 for 4/4 time) */
    public static final int BEATS_PER_BAR = 4;

    /**
     * Constructs a new MusicTrack object from the parsed beat list and audio format.
     *
     * @param musicTrackOption  The MusicTrackOption enum for this track
     * @param displayName A user-friendly name to use when displaying this tracks name to the user
     * @param beats  A list of MusicBeat segments extracted from the audio
     * @param format The shared audio format for all beats in the track
     */
    public MusicTrack(MusicTrackOption musicTrackOption, String displayName, List<MusicBeat> beats, AudioFormat format)
    {
        this.musicTrackOption = musicTrackOption;
        this.displayName = displayName;
        this.beats = beats;
        this.format = format;
    }

    /** @return The total number of full bars in the track. */
    public int getNumberBarsInTrack()
    {
        return (int) Math.floor((double) beats.size() / BEATS_PER_BAR);
    }

    /**
     * Retrieves a specific beat based on bar and beat number (both 1-based).
     *
     * @param bar  The 1-based bar number
     * @param beat The 1-based beat number within the bar (must be between 1 and BEATS_PER_BAR)
     * @return The MusicBeat object, or null if the bar/beat index is invalid or out of range
     */
    public MusicBeat getBeat(int bar, int beat)
    {
        // Reject invalid beat numbers (e.g., 0 or > 4)
        if (beat < 1 || beat > BEATS_PER_BAR)
            return null;

        // Convert 1-based bar/beat to a 0-based flat index
        int index = (bar - 1) * BEATS_PER_BAR + (beat - 1);

        // Validate index is within bounds
        if (index >= 0 && index < beats.size())
            return beats.get(index);

        return null;
    }
}