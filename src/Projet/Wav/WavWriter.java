package Projet.Wav;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public final class WavWriter implements  IWavWriter {
    public static final AudioFormat AudioFormat = new AudioFormat(
            44100,
            8,
            1,
            true,
            true
    );

    @Override
    public void writeWavFile(String path, byte[] data) {
        AudioInputStream ais = new AudioInputStream(
                new ByteArrayInputStream(data),
                AudioFormat,
                data.length / AudioFormat.getFrameSize()
        );
        File file = new File(path);
        try {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
