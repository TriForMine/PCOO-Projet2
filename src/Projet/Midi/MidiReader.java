package Projet.Midi;

import Projet.Interfaces.IMidiReader;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import java.io.File;
import java.io.IOException;

public final class MidiReader implements IMidiReader {
    public Sequence getMidiSequence(String path) throws InvalidMidiDataException, IOException {
        return MidiSystem.getSequence(
                new File(path)
        );
    }
}
