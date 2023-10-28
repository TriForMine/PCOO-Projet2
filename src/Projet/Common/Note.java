package Projet.Common;

public record Note(int channel, int octave, NoteEnum note, int volume, float duration, float time) {

    public float getFrequency() {
        return this.note.getFrequency(this.octave);
    }

    @Override
    public String toString() {
        return "Note{" +
                "channel=" + channel +
                ", note=" + note +
                ", volume=" + volume +
                ", octave=" + octave +
                ", duration=" + duration +
                ", time=" + time +
                '}';
    }
}
