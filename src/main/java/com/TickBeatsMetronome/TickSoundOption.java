package com.TickBeatsMetronome;

public enum TickSoundOption
{
    OFF(""),
    TICK_HIHAT("tick-hihat.wav"),
    TICK_HIHAT_PLAIN("tick-hihat-plain.wav"),
    TICK_KICK("tick-kick.wav"),
    TICK_METRO("tick-metro.wav"),
    TICK_PERC_TAMBO("tick-perc-tambo.wav"),
    TICK_SNARE("tick-snare.wav"),
    TICK_CAN("tick-can.wav"),
    TICK_HIHAT_ANALOG("tick-hihat-analog.wav"),
    TICK_HIHAT_DIGITAL("tick-hihat-digital.wav"),
    TICK_HIHAT_ELECTRO("tick-hihat-electro.wav"),
    KICK_ELECTRONIC("kick-electronic.wav"),
    KICK_GRITTY("kick-gritty.wav"),
    KICK_HOLLOW("kick-hollow.wav"),
    KICK_LONG_BIG("kick-long-big.wav"),
    KICK_NEWWAVE("kick-newwave.wav"),
    KICK_SNAPBACK("kick-slapback.wav"),
    KICK_SNARE_ELECTRONIC("kick-clap-electronic.wav"),
    KICK_THUMP("kick-thump.wav"),
    KICK_TIGHT("kick-tight.wav"),
    KICK_TIGHT_CLASSIC("kick-tight-classic.wav"),
    KICK_TIGHT_HIGH("kick-tight-high.wav"),
    KICK_TIGHT_SHORT("kick-tight-short.wav"),
    KICK_ZAPPER("kick-zapper.wav"),
    CLAP_HIGH("clap-high.wav"),
    CLAP_LOW("clap-low.wav"),
    CLAP_TAPE("clap-tape.wav"),
    CLAP_SNARE_SMASHER("clap-snare-smasher.wav"),
    CLAP_SNARE_TAPE("clap-snare-tape.wav"),
    CLAP_SNARE_VINYL("clap-snare-vinyl.wav"),

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

