package com.TickBeatsMetronome;

public enum MusicTrackOption {
    ALLS_FAIRY_IN_LOVE_AND_WAR("Alls Fairy In Love And War", "alls_fairy_in_love_and_war.wav"),
    AUTUMN_VOYAGE("Autumn Voyage", "autumn_voyage.wav"),
    FIRE_IN_THE_DEEP("Fire In The Deep", "fire_in_the_deep.wav"),
    FIRE_IN_THE_HOLE("Fire In The Hole !", "fire_in_the_hole.wav"),
    INFERNO("Inferno", "inferno.wav"),
    MOR_UL_REK("Mor Ul Rek !", "mor_ul_rek.wav"),
    SEA_SHANTY_2("Sea Shanty 2", "sea_shanty_2.wav"),

    // User Music
    // The Music files are given IDs based on their alphabetical order.
    USER_MUSIC_1("User Track 1", "1"),
    USER_MUSIC_2("User Track 2", "2"),
    USER_MUSIC_3("User Track 3", "3"),
    USER_MUSIC_4("User Track 4", "4"),
    USER_MUSIC_5("User Track 5", "5"),
    USER_MUSIC_6("User Track 6", "6"),
    USER_MUSIC_7("User Track 7", "7"),
    USER_MUSIC_8("User Track 8", "8"),
    USER_MUSIC_9("User Track 9", "9"),
    USER_MUSIC_10("User Track 10", "10"),
    USER_MUSIC_11("User Track 11", "11"),
    USER_MUSIC_12("User Track 12", "12"),
    USER_MUSIC_13("User Track 13", "13"),
    USER_MUSIC_14("User Track 14", "14"),
    USER_MUSIC_15("User Track 15", "15"),
    USER_MUSIC_16("User Track 16", "16"),
    USER_MUSIC_17("User Track 17", "17"),
    USER_MUSIC_18("User Track 18", "18"),
    USER_MUSIC_19("User Track 19", "19"),
    USER_MUSIC_20("User Track 20", "20"),
    USER_MUSIC_21("User Track 21", "21"),
    USER_MUSIC_22("User Track 22", "22"),
    USER_MUSIC_23("User Track 23", "23"),
    USER_MUSIC_24("User Track 24", "24"),
    USER_MUSIC_25("User Track 25", "25"),
    USER_MUSIC_26("User Track 26", "26"),
    USER_MUSIC_27("User Track 27", "27"),
    USER_MUSIC_28("User Track 28", "28"),
    USER_MUSIC_29("User Track 29", "29"),
    USER_MUSIC_30("User Track 30", "30"),
    USER_MUSIC_31("User Track 31", "31"),
    USER_MUSIC_32("User Track 32", "32"),
    USER_MUSIC_33("User Track 33", "33"),
    USER_MUSIC_34("User Track 34", "34"),
    USER_MUSIC_35("User Track 35", "35"),
    USER_MUSIC_36("User Track 36", "36"),
    USER_MUSIC_37("User Track 37", "37"),
    USER_MUSIC_38("User Track 38", "38"),
    USER_MUSIC_39("User Track 39", "39"),
    USER_MUSIC_40("User Track 40", "40"),
    USER_MUSIC_41("User Track 41", "41"),
    USER_MUSIC_42("User Track 42", "42"),
    USER_MUSIC_43("User Track 43", "43"),
    USER_MUSIC_44("User Track 44", "44"),
    USER_MUSIC_45("User Track 45", "45"),
    USER_MUSIC_46("User Track 46", "46"),
    USER_MUSIC_47("User Track 47", "47"),
    USER_MUSIC_48("User Track 48", "48"),
    USER_MUSIC_49("User Track 49", "49"),
    USER_MUSIC_50("User Track 50", "50");

    private final String displayName;
    private final String fileName;

    MusicTrackOption(String displayName, String fileName) {
        this.displayName = displayName;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }


    @Override
    public String toString() {
        return displayName;
    }
}