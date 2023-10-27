package Projet.Common;

public class Note {
    private int channel;
    private NoteEnum note;
    private int volume;
    private int octave;
    private float duration;
    private float time;

    public Note(int channel, int octave, NoteEnum note, int volume, float duration, float time) {
        this.channel = channel;
        this.note = note;
        this.octave = octave;
        this.volume = volume;
        this.duration = duration;
        this.time = time;
    }

    public NoteEnum getNote() {
        return this.note;
    }

    public int getVolume() {
        return this.volume;
    }

    public int getOctave() {
        return this.octave;
    }

    public float getFrequency() {
        return this.note.getFrequency(this.octave);
    }

    public float getDuration() {
        return this.duration;
    }

    public float getTime() {
        return this.time;
    }

    public int getChannel() {
        return this.channel;
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

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
