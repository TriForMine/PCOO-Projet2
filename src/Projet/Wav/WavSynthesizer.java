package Projet.Wav;

import Projet.Common.Note;
import Projet.Interfaces.IWavWriter;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicIntegerArray;

public final class WavSynthesizer {
    public static final IWavWriter WavWriter = new WavWriter();
    public static final float SAMPLE_RATE = 44100f;
    public static final int SAMPLE_SIZE_IN_BITS = 8;
    public static final int MAX_VALUE = (int) (Math.pow(2, SAMPLE_SIZE_IN_BITS - 1d) - 1);
    public static final int MIN_VALUE = -MAX_VALUE - 1;

    private WavSynthesizer() {
        throw new IllegalStateException();
    }

    public static void createFile(String path, List<Note> notes) {
        WavWriter.writeWavFile(path, convertToBytes(generateWavData(notes)));
    }

    private static int[] generateSquareWave(float frequency, float duration) {
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

    private static int[] generateWhiteNoise(float duration) {
        int waveSize = (int) (duration * SAMPLE_RATE);
        return ThreadLocalRandom.current()
                .ints(waveSize, MIN_VALUE, MAX_VALUE)
                .toArray();
    }

    private static int[] generateWavData(List<Note> notes) {
        float totalDuration = 0;
        for (Note note : notes) {
            totalDuration = Math.max(totalDuration, note.time() + note.duration());
        }

        int[] wave = new int[(int) (totalDuration * SAMPLE_RATE)];

        AtomicIntegerArray atomicWave = new AtomicIntegerArray(wave.length);

        notes.parallelStream().forEach(note -> {
            int[] noteWave;

            if (note.channel() == 9) {
                noteWave = generateWhiteNoise(note.duration());
            } else {
                noteWave = generateSquareWave(note.getFrequency(), note.duration());
            }

            int noteStart = (int) (note.time() * SAMPLE_RATE);
            float volume = note.volume() / 127f;

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
        byte[] bytes = new byte[wave.length << 2];
        for (int i = 0; i < wave.length; i++) {
            bytes[i * 4] = (byte) (wave[i] >> 24);
            bytes[i * 4 + 1] = (byte) (wave[i] >> 16);
            bytes[i * 4 + 2] = (byte) (wave[i] >> 8);
            bytes[i * 4 + 3] = (byte) (wave[i]);
        }
        return bytes;
    }
}
