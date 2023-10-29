package Projet;

import Projet.Converter.MidiToWavConverter;
import Projet.Midi.MidiProcessor;
import Projet.Wav.WavSynthesizer;

public class Main {
    public static void main(String[] args) {
        MidiToWavConverter.convert("fichier1.mid", "fichier1.wav");
        MidiToWavConverter.convert("fichier2.mid", "fichier2.wav");
        MidiToWavConverter.convert("fichier3.mid", "fichier3.wav");
        MidiToWavConverter.convert("eminem-mockingbird.mid", "eminem-mockingbird.wav");
        MidiToWavConverter.convert("tdm_music1.mid", "tdm_music1.wav");
        MidiToWavConverter.convert("Pirates of the Caribbean - He's a Pirate (3).mid", "Pirates of the Caribbean - He's a Pirate (3).wav");
    }
}
