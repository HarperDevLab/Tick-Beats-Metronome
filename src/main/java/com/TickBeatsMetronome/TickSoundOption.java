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
    CLAP_SNARE_VINYL("clap-snare-vinyl.wav");


    private final String fileName;

    TickSoundOption(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }

    @Override
    public String toString()
    {
        return name().toLowerCase().replace('_', ' ');
    }
}

