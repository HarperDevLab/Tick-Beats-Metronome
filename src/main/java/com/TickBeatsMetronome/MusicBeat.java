package com.TickBeatsMetronome;

public class MusicBeat
{
    private final int barIndex;
    private final int beatIndex;
    private final byte[] audioData;

    public MusicBeat(int barIndex, int beatIndex, byte[] audioData)
    {
        this.barIndex = barIndex;
        this.beatIndex = beatIndex;
        this.audioData = audioData;
    }

    public int getBarIndex() { return barIndex; }
    public int getBeatIndex() { return beatIndex; }
    public byte[] getAudioData() { return audioData; }

    @Override
    public String toString()
    {
        return "MusicBeat{bar=" + barIndex + ", beat=" + beatIndex + "}";
    }
}