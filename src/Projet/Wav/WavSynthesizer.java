package Projet.Wav;

import Projet.Common.Note;
import Projet.Interfaces.IWavWriter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicIntegerArray;

public final class WavSynthesizer {
    public static final IWavWriter WavWriter = new WavWriter();
    public static final float SAMPLE_RATE = 65536;
    public static final int SAMPLE_SIZE_IN_BITS = 16;
    public static final int MAX_VALUE = (int) (Math.pow(2, SAMPLE_SIZE_IN_BITS - 1d) - 1);
    public static final int MIN_VALUE = -MAX_VALUE - 1;

    private WavSynthesizer() {
        throw new IllegalStateException();
    }

    public static void createFile(String path, List<Note> notes) throws IOException {
        WavWriter.writeWavFile(path, convertToBytes(generateWavData(notes)));
    }

    public static int[] generateSquareWave(float frequency, double duration) {
        int sampleRateFreqRatio = (int)(SAMPLE_RATE / frequency);
        int sampleRateHalfFreqRatio = sampleRateFreqRatio >> 1;
        int samplesCount = (int)(duration * SAMPLE_RATE);

        int[] wave = new int[samplesCount];

        for (int i = 0; i < samplesCount; i++) {
            if (((i << 2) % sampleRateFreqRatio) > sampleRateHalfFreqRatio) {
                wave[i] = MAX_VALUE;
            } else {
                wave[i] = MIN_VALUE;
            }
        }

        return wave;
    }

    public static int[] generateSawtoothWave(float frequency, double duration) {
        int sampleRateFreqRatio = (int)(SAMPLE_RATE / frequency);
        int samplesCount = (int)(duration * SAMPLE_RATE);

        int[] wave = new int[samplesCount];

        for (int i = 0; i < samplesCount; i++) {
            wave[i] = (int) (MIN_VALUE + ((i << 2) % sampleRateFreqRatio) * ((MAX_VALUE - MIN_VALUE) / (double) sampleRateFreqRatio));
        }

        return wave;
    }

    public static int[] generateTriangleWave(float frequency, double duration) {
        int sampleRateFreqRatio = (int)(SAMPLE_RATE / frequency);
        int sampleRateHalfFreqRatio = sampleRateFreqRatio >> 1;
        int samplesCount = (int)(duration * SAMPLE_RATE);

        int[] wave = new int[samplesCount];

        for (int i = 0; i < samplesCount; i++) {
            int sample = (i << 2) % sampleRateFreqRatio;
            if (sample > sampleRateHalfFreqRatio) {
                wave[i] = (int) (MAX_VALUE - (sample - sampleRateHalfFreqRatio) * ((MAX_VALUE - MIN_VALUE) / (double) sampleRateHalfFreqRatio));
            } else {
                wave[i] = (int) (MIN_VALUE + sample * ((MAX_VALUE - MIN_VALUE) / (double) sampleRateHalfFreqRatio));
            }
        }

        return wave;
    }

    public static int[] generateSineWave(float frequency, double duration) {
        int samplesCount = (int)(duration * SAMPLE_RATE);

        int[] wave = new int[samplesCount];

        for (int i = 0; i < samplesCount; i++) {
            wave[i] = (int) (Math.sin(2 * Math.PI * i * frequency / SAMPLE_RATE) * MAX_VALUE);
        }

        return wave;
    }

    public static int[] generateWhiteNoise(double duration) {
        int waveSize = (int) (duration * SAMPLE_RATE);
        return ThreadLocalRandom.current()
                .ints(waveSize, MIN_VALUE, MAX_VALUE)
                .toArray();
    }

    private static void applyEnvelope(int[] wave, float[] envelope) {
        for (int i = 0; i < wave.length; i++) {
            float envelopeValue = envelope[i];
            wave[i] = (int) (wave[i] * envelopeValue);
        }
    }

    private static int[] generateWavData(List<Note> notes) {
        double totalDuration = 0;
        for (Note note : notes) {
            totalDuration = Math.max(totalDuration, note.getTime() + note.getDuration());
        }

        int[] wave = new int[(int) (totalDuration * SAMPLE_RATE)];

        AtomicIntegerArray atomicWave = new AtomicIntegerArray(wave.length);

        notes.parallelStream().forEach(note -> {
            int[] noteWave = note.getWave();

            int noteStart = (int) (note.getTime() * SAMPLE_RATE);
            float volume = note.getVolume() / 127f;

            applyEnvelope(noteWave, note.getEnvelope());

            for (int i = 0; i < noteWave.length; i++) {
                int waveIndex = noteStart + i;
                atomicWave.getAndAdd(waveIndex, (int) (noteWave[i] * volume));
            }
        });

        for (int i = 0; i < wave.length; i++) {
            wave[i] = Math.max(MIN_VALUE, Math.min(MAX_VALUE, atomicWave.get(i)));
        }

        return wave;
    }

    private static byte[] convertToBytes(int[] wave) {
        byte[] bytes = new byte[wave.length * (SAMPLE_SIZE_IN_BITS / 8)];
        // Signed SAMPLE_SIZE_IN_BITS-bit, big-endian
        for (int i = 0; i < wave.length; i++) {
            int sample = wave[i];

            for (int j = 0; j < SAMPLE_SIZE_IN_BITS / 8; j++) {
                bytes[i * (SAMPLE_SIZE_IN_BITS / 8) + j] = (byte) (sample >> (8 * (SAMPLE_SIZE_IN_BITS / 8 - j - 1)));
            }
        }

        return bytes;
    }
}
