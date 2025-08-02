package com.TickBeatsMetronome;

public enum TickSoundOption
{
    OFF(""),
    TICK("tick.wav"),
    TICK_HIHAT("tick-hihat.wav"),
    TICK_KICK("tick-kick.wav"),
    TICK_METRO("tick-metro.wav"),



    KICK("kick.wav"),
    KICK_2("kick-2.wav"),
    KICK_ELECTRONIC("kick-electronic.wav"),
    KICK_ELECTRONIC_2("kick-electronic-2.wav"),
    KICK_ELECTRONIC_3("kick-electronic-3.wav"),
    KICK_ELECTRONIC_4("kick-electronic-4.wav"),
    KICK_ELECTRONIC_5("kick-electronic-5.wav"),
    KICK_ELECTRONIC_6("kick-electronic-6.wav"),
    KICK_SMALL("kick-small.wav"),


    CLAP("clap.wav"),
    CLAP_SLAP("clap-slap.wav"),

    SNARE("snare.wav"),
    SNARE_2("snare-2.wav"),
    SNARE_3("snare-3.wav"),

    CRACK_HIGH("crack-high.wav"),
    CRACK_LOW("crack-low.wav"),


    //User Sounds
    //The sound files are given id's based on their Alphabetical order, that's what we'll use to access them
    USER_SOUND_1("1"),
    USER_SOUND_2("2"),
    USER_SOUND_3("3"),
    USER_SOUND_4("4"),
    USER_SOUND_5("5"),
    USER_SOUND_6("6"),
    USER_SOUND_7("7"),
    USER_SOUND_8("8"),
    USER_SOUND_9("9"),
    USER_SOUND_10("10"),
    USER_SOUND_11("11"),
    USER_SOUND_12("12"),
    USER_SOUND_13("13"),
    USER_SOUND_14("14"),
    USER_SOUND_15("15"),
    USER_SOUND_16("16"),
    USER_SOUND_17("17"),
    USER_SOUND_18("18"),
    USER_SOUND_19("19"),
    USER_SOUND_20("20"),
    ;



    private final String resourceName;

    TickSoundOption(String resourceName)
    {
        this.resourceName = resourceName;
    }

    public String getFileName()
    {
        return resourceName;
    }

    @Override
    public String toString()
    {
        return name().toLowerCase().replace('_', ' ');
    }
}

