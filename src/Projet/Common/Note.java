package Projet.Common;

import Projet.Wav.WavSynthesizer;

public class Note {
    private final int channel;
    private final NoteEnum note;
    private int volume;
    private final int octave;
    private double duration;
    private final double time;

    public Note(int channel, int octave, NoteEnum note, int volume, double duration, double time) {
        this.channel = channel;
        this.note = note;
        this.octave = octave;
        this.volume = volume;
        this.duration = duration;
        this.time = time;
    }

    public int getVolume() {
        return this.volume;
    }


    public float getFrequency() {
        return this.note.getFrequency(this.octave);
    }

    public double getDuration() {
        return this.duration;
    }

    public double getTime() {
        return this.time;
    }

    public int getChannel() {
        return this.channel;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Calculates the ADSR envelope for the audio data based on the channel and duration.
     *
     * @return the ADSR envelope as an array of floating-point values
     */
    public float[] getEnvelope() {
        float[] envelope = new float[(int) Math.round(this.getDuration() * WavSynthesizer.SAMPLE_RATE)];

        int secondsToSamples = (int) (this.getDuration() * WavSynthesizer.SAMPLE_RATE);

        int attackTime;
        int decayTime;
        int releaseTime;
        float sustainLevel;

        switch (this.getChannel()) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 11: // Melody
                attackTime = secondsToSamples / 4;
                decayTime = secondsToSamples / 2;
                releaseTime = secondsToSamples / 4;
                sustainLevel = 0.7f;
                break;
            case 9:  // Percussion
                attackTime = 0;
                decayTime = secondsToSamples / 2;
                releaseTime = secondsToSamples / 2;
                sustainLevel = 0.0f;
                break;
            default:
                attackTime = secondsToSamples;
                decayTime = 2 * secondsToSamples;
                releaseTime = secondsToSamples;
                sustainLevel = 0.8f;
                break;
        }

        // ADSR Envelope
        for (int i = 0; i < envelope.length; i++) {
            if(i < attackTime)
                envelope[i] = (float) i / attackTime; // Attack
            else if(i < attackTime + decayTime)
                envelope[i] = ((sustainLevel - 1.0f) / decayTime * (i - attackTime)) + 1.0f; // Decay
            else if(i < envelope.length - releaseTime)
                envelope[i] = sustainLevel; // Sustain
            else
                envelope[i] = ((0.0f - sustainLevel) / releaseTime * (i - (envelope.length - releaseTime))) + sustainLevel; // Release
        }

        return envelope;
    }

    public int[] getWave() {
        switch (this.getChannel()) {
            case 0, 1, 3, 4, 5, 6 -> {
                return WavSynthesizer.generateSquareWave(this.getFrequency(), this.getDuration());
            }
            case 2 -> {
                return WavSynthesizer.generateSineWave(this.getFrequency(), this.getDuration());
            }
            case 7, 8, 11, 12 -> {
                return WavSynthesizer.generateSawtoothWave(this.getFrequency(), this.getDuration());
            }
            case 9 -> {
                return WavSynthesizer.generateWhiteNoise(this.getDuration());
            }
            case 10 -> {
                return WavSynthesizer.generateTriangleWave(this.getFrequency(), this.getDuration());
            }
            default -> {
                System.err.println("Invalid channel: " + this.getChannel());
                return new int[0];
            }
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Note{channel=%d, note=%s, volume=%d, octave=%d, duration=%.3f, time=%.3f}",
                channel, note, volume, octave, duration, time
        );
    }
}
