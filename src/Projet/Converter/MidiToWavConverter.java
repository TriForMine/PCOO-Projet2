package Projet.Converter;

import Projet.Midi.MidiProcessor;
import Projet.Wav.WavSynthesizer;

import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;


// The class that handles converting from Midi to Wav
public final class MidiToWavConverter {
    private MidiToWavConverter() {
        throw new IllegalStateException();
    }

    public static void convert(String midiPath, String wavPath) throws InvalidMidiDataException, IOException {
        System.out.println("Converting " + midiPath + " to " + wavPath);
        WavSynthesizer.createFile(wavPath, MidiProcessor.processFile(midiPath));
        System.out.println("Conversion done");
    }
}
