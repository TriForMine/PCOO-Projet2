package Projet;

import Projet.Converter.MidiToWavConverter;

import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                MidiToWavConverter.convert("fichier1.mid", "fichier1.wav");
                MidiToWavConverter.convert("fichier2.mid", "fichier2.wav");
                MidiToWavConverter.convert("fichier3.mid", "fichier3.wav");
            } else if (args[0].equals(args[1])) {
                System.out.println("Les fichiers doivent être différents");
            } else {
                MidiToWavConverter.convert(args[0], args[1]);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier: " + e.getMessage());
        } catch (InvalidMidiDataException e) {
            System.out.println("Erreur lors de la lecture du traitement du fichier midi: " + e.getMessage());
        }
    }
}