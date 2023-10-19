package Projet.Wav;

import Projet.Common.Note;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

public final class WavSynthesizer {
    public static final IWavWriter WavWriter = new WavWriter();
    public static final Random Random = new Random();

    private WavSynthesizer() {
        throw new IllegalStateException();
    }

    public static void createFile(String path, List<Note> notes) {
        WavWriter.writeWavFile(path, convertToBytes(generateWavData(notes)));
    }

    private static int[] generateSquareWave(float frequency, float duration) {
        int[] wave = new int[(int) (duration * 44100)];
        for (int i = 0; i < wave.length; i++) {
            if ((4 * i % (44100f / frequency)) > 22050f / frequency) {
                wave[i] = 127;
            } else {
                wave[i] = -128;
            }
        }
        return wave;
    }

    private static int[] generateWhiteNoise(float duration) {
        int[] wave = new int[(int) (duration * 44100)];
        for (int i = 0; i < wave.length; i++) {
            wave[i] = (Random.nextInt(255) - 128);
        }
        return wave;
    }

    private static int mix(int a, int b) {
        return Math.max(-128, Math.min(127, a + b));
    }

    private static int[] generateWavData(List<Note> notes) {
        // Each note has a duration, and the time it starts at
        // We need to find the total duration of the song
        float totalDuration = 0;
        for (Note note : notes) {
            totalDuration = Math.max(totalDuration, note.getTime() + note.getDuration());
        }

        // We generate the wave data for each note
        int[] wave = new int[(int) (totalDuration * 44100)];

        for (Note note : notes) {
            int[] noteWave;

            if (note.getChannel() == 9) {
                noteWave = generateWhiteNoise(note.getDuration());
            } else {
                noteWave = generateSquareWave(note.getFrequency(), note.getDuration());
            }

            int noteStart = (int) (note.getTime() * 44100);

            for (int i = 0; i < noteWave.length; i++) {
                wave[noteStart + i] = mix(wave[noteStart + i], noteWave[i] * note.getVolume() / 127);
            }
        }

        return wave;
    }

    private static byte[] convertToBytes(int[] wave) {
        byte[] bytes = new byte[wave.length * 4];
        for (int i = 0; i < wave.length; i++) {
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(wave[i]);
            bytes[i * 4] = bb.get(0);
            bytes[i * 4 + 1] = bb.get(1);
            bytes[i * 4 + 2] = bb.get(2);
            bytes[i * 4 + 3] = bb.get(3);
        }
        return bytes;
    }
}
