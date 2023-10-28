package Projet.Midi;

import Projet.Interfaces.IMidiReader;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import java.io.File;

public final class MidiReader implements IMidiReader {
    public Sequence getMidiSequence(String path) {
        try {
            return MidiSystem.getSequence(
                    new File(path)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
