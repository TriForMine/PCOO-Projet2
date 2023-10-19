package Projet.Common;

public enum NoteEnum {
    Do(0),
    DoSharp(1),
    Re(2),
    ReSharp(3),
    Mi(4),
    Fa(5),
    FaSharp(6),
    Sol(7),
    SolSharp(8),
    La(9),
    LaSharp(10),
    Si(11);

    private final int value;
    private int volume;

    private NoteEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static NoteEnum fromValue(int value) {
        return switch (value) {
            case 0 -> Do;
            case 1 -> DoSharp;
            case 2 -> Re;
            case 3 -> ReSharp;
            case 4 -> Mi;
            case 5 -> Fa;
            case 6 -> FaSharp;
            case 7 -> Sol;
            case 8 -> SolSharp;
            case 9 -> La;
            case 10 -> LaSharp;
            case 11 -> Si;
            default -> null;
        };
    }

    public float getOctave4Frequency() {
        return switch (this) {
            case Si -> 493.883f;
            case LaSharp -> 466.164f;
            case La -> 440.000f;
            case SolSharp -> 415.305f;
            case Sol -> 391.995f;
            case FaSharp -> 369.994f;
            case Fa -> 349.228f;
            case Mi -> 329.628f;
            case ReSharp -> 311.127f;
            case Re -> 293.665f;
            case DoSharp -> 277.183f;
            case Do -> 261.626f;
        };
    }

    public float getFrequency(int octave) {
        return this.getOctave4Frequency() * (float) Math.pow(2, octave - 4f);
    }

    @Override
    public String toString() {
        return switch (this) {
            case Si -> "Si";
            case LaSharp -> "La#";
            case La -> "La";
            case SolSharp -> "Sol#";
            case Sol -> "Sol";
            case FaSharp -> "Fa#";
            case Fa -> "Fa";
            case Mi -> "Mi";
            case ReSharp -> "Re#";
            case Re -> "Re";
            case DoSharp -> "Do#";
            case Do -> "Do";
        };
    }
}