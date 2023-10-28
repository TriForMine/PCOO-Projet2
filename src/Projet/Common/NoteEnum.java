package Projet.Common;

public enum NoteEnum {
    DO(0),
    DO_SHARP(1),
    RE(2),
    RE_SHARP(3),
    MI(4),
    FA(5),
    FA_SHARP(6),
    SOL(7),
    SOL_SHARP(8),
    LA(9),
    LA_SHARP(10),
    SI(11);

    private final int value;

    NoteEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static NoteEnum fromValue(int value) {
        return switch (value) {
            case 0 -> DO;
            case 1 -> DO_SHARP;
            case 2 -> RE;
            case 3 -> RE_SHARP;
            case 4 -> MI;
            case 5 -> FA;
            case 6 -> FA_SHARP;
            case 7 -> SOL;
            case 8 -> SOL_SHARP;
            case 9 -> LA;
            case 10 -> LA_SHARP;
            case 11 -> SI;
            default -> null;
        };
    }

    public float getOctave4Frequency() {
        return switch (this) {
            case SI -> 493.883f;
            case LA_SHARP -> 466.164f;
            case LA -> 440.000f;
            case SOL_SHARP -> 415.305f;
            case SOL -> 391.995f;
            case FA_SHARP -> 369.994f;
            case FA -> 349.228f;
            case MI -> 329.628f;
            case RE_SHARP -> 311.127f;
            case RE -> 293.665f;
            case DO_SHARP -> 277.183f;
            case DO -> 261.626f;
        };
    }

    public float getFrequency(int octave) {
        return this.getOctave4Frequency() * (float) Math.pow(2, octave - 4f);
    }

    @Override
    public String toString() {
        return switch (this) {
            case SI -> "Si";
            case LA_SHARP -> "La#";
            case LA -> "La";
            case SOL_SHARP -> "Sol#";
            case SOL -> "Sol";
            case FA_SHARP -> "Fa#";
            case FA -> "Fa";
            case MI -> "Mi";
            case RE_SHARP -> "Re#";
            case RE -> "Re";
            case DO_SHARP -> "Do#";
            case DO -> "Do";
        };
    }
}
