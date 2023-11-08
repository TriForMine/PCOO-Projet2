package Projet.Interfaces;

import java.io.IOException;

public interface IWavWriter {
    void writeWavFile(String path, byte[] data) throws IOException;
}
