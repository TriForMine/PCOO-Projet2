package Projet.Converter;

import Projet.Midi.MidiProcessor;
import Projet.Wav.WavSynthesizer;


// The class that handles converting from Midi to Wav
public final class MidiToWavConverter {
    private MidiToWavConverter() {
        throw new IllegalStateException();
    }

    public static void convert(String midiPath, String wavPath) {
        WavSynthesizer.createFile(wavPath, MidiProcessor.processFile(midiPath));
    }
}
