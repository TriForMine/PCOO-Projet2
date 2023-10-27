package Projet.Wav;

import Projet.Common.Note;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class WavSynthesizer {
    public static final IWavWriter WavWriter = new WavWriter();
    public static final Random Random = new Random();
    public static final float SampleRate = 44100f;
    public static final int SampleSizeInBits = 8;
    public static final int MaxValue = (int) (Math.pow(2, SampleSizeInBits - 1d) - 1);
    public static final int MinValue = -MaxValue - 1;

    private WavSynthesizer() {
        throw new IllegalStateException();
    }

    public static void createFile(String path, List<Note> notes) {
        WavWriter.writeWavFile(path, convertToBytes(generateWavData(notes)));
    }

    private static int[] generateSquareWave(float frequency, float duration) {
        int[] wave = new int[(int) (duration * SampleRate)];
        for (int i = 0; i < wave.length; i++) {
            if ((4 * i % (SampleRate / frequency)) > (SampleRate / 2) / frequency) {
                wave[i] = MaxValue;
            } else {
                wave[i] = MinValue;
            }
        }
        return wave;
    }

    private static int[] generateWhiteNoise(float duration) {
        int[] wave = new int[(int) (duration * SampleRate)];
        for (int i = 0; i < wave.length; i++) {
            wave[i] = Random.nextInt(MinValue, MaxValue);
        }
        return wave;
    }

    private static int mix(int a, int b) {
        return Math.max(MinValue, Math.min(MaxValue, a + b));
    }

    private static int[] generateWavData(List<Note> notes) {
        // Each note has a duration, and the time it starts at
        // We need to find the total duration of the song
        float totalDuration = 0;
        for (Note note : notes) {
            totalDuration = Math.max(totalDuration, note.getTime() + note.getDuration());
        }

        // We generate the wave data for each note
        int[] wave = new int[(int) (totalDuration * SampleRate)];

        for (Note note : notes) {
            int[] noteWave;

            if (note.getChannel() == 9) {
                noteWave = generateWhiteNoise(note.getDuration());
            } else {
                noteWave = generateSquareWave(note.getFrequency(), note.getDuration());
            }

            int noteStart = (int) (note.getTime() * SampleRate);

            for (int i = 0; i < noteWave.length; i++) {
                wave[noteStart + i] = mix(wave[noteStart + i], noteWave[i] * note.getVolume() / 127);
            }
        }

        return wave;
    }

    private static byte[] convertToBytes(int[] wave) {
        ByteBuffer bb = ByteBuffer.allocate(wave.length * 4);
        for (int w : wave) {
            bb.putInt(w);
        }
        return bb.array();
    }
}
