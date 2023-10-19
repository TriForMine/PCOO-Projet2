package Projet;

import Projet.Midi.MidiProcessor;
import Projet.Wav.WavSynthesizer;

public class Main {
    public static void main(String[] args) {
        WavSynthesizer.createFile("fichier1.wav", MidiProcessor.processFile("fichier1.mid"));
        WavSynthesizer.createFile("fichier2.wav", MidiProcessor.processFile("fichier2.mid"));
        WavSynthesizer.createFile("fichier3.wav", MidiProcessor.processFile("fichier3.mid"));
    }
}