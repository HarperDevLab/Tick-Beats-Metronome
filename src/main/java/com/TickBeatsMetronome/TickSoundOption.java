package com.TickBeatsMetronome;

public enum TickSoundOption
{
    OFF("Off", ""),
    TICK("Tick", "tick.wav"),
    TICK_HIHAT("Tick Hi-Hat", "tick-hihat.wav"),
    TICK_HIHAT_2("Tick Hi-Hat 2", "tick-hihat-2.wav"),

    METRO_TICK("Metro Tick", "metro-tick.wav"),
    METRO_TICK_2("Metro Tick Strong", "metro-tick-strong.wav"),
    METRO_TOCK("Metro Tock", "metro-tock.wav"),

    BASS("Bass", "bass.wav"),
    BASS_2("Bass 2", "bass-2.wav"),
    BASS_3("Bass 3", "bass-3.wav"),
    BASS_ELECTRONIC("Bass Electronic", "bass-electronic.wav"),
    BASS_ELECTRONIC_2("Bass Electronic 2", "bass-electronic-2.wav"),
    BASS_ELECTRONIC_3("Bass Electronic 3", "bass-electronic-3.wav"),
    BASS_ELECTRONIC_4("Bass Electronic 4", "bass-electronic-4.wav"),
    BASS_ELECTRONIC_5("Bass Electronic 5", "bass-electronic-5.wav"),
    BASS_ELECTRONIC_6("Bass Electronic 6", "bass-electronic-6.wav"),

    CLAP("Clap", "clap.wav"),
    CLAP_2("Clap 2", "clap-2.wav"),
    CLAP_3("Clap 3", "clap-3.wav"),
    CLAP_SLAP("Clap Slap", "clap-slap.wav"),

    SNARE("Snare", "snare.wav"),
    SNARE_2("Snare 2", "snare-2.wav"),
    SNARE_3("Snare 3", "snare-3.wav"),

    CRACK_HIGH("Crack High", "crack-high.wav"),
    CRACK_LOW("Crack Low", "crack-low.wav"),

    X2_TICK("X2 Tick", "x2-tick.wav"),
    X2_BASS("X2 Bass", "x2-bass.wav"),
    X2_CLAP("X2 Clap", "x2-clap.wav"),

    // User Sounds
    USER_SOUND_1("User Sound 1", "1"),
    USER_SOUND_2("User Sound 2", "2"),
    USER_SOUND_3("User Sound 3", "3"),
    USER_SOUND_4("User Sound 4", "4"),
    USER_SOUND_5("User Sound 5", "5"),
    USER_SOUND_6("User Sound 6", "6"),
    USER_SOUND_7("User Sound 7", "7"),
    USER_SOUND_8("User Sound 8", "8"),
    USER_SOUND_9("User Sound 9", "9"),
    USER_SOUND_10("User Sound 10", "10"),
    USER_SOUND_11("User Sound 11", "11"),
    USER_SOUND_12("User Sound 12", "12"),
    USER_SOUND_13("User Sound 13", "13"),
    USER_SOUND_14("User Sound 14", "14"),
    USER_SOUND_15("User Sound 15", "15"),
    USER_SOUND_16("User Sound 16", "16"),
    USER_SOUND_17("User Sound 17", "17"),
    USER_SOUND_18("User Sound 18", "18"),
    USER_SOUND_19("User Sound 19", "19"),
    USER_SOUND_20("User Sound 20", "20");

    private final String displayName;
    private final String resourceName;

    TickSoundOption(String displayName, String resourceName)
    {
        this.displayName = displayName;
        this.resourceName = resourceName;
    }

    public String getResourceName()
    {
        return resourceName;
    }

    public boolean isUserSound()
    {
        return resourceName.matches("\\d+");
    }

    @Override
    public String toString()
    {
        return displayName;
    }
}

