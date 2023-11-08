package Projet.Interfaces;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import java.io.IOException;

public interface IMidiReader {
    Sequence getMidiSequence(String path) throws InvalidMidiDataException, IOException;
}
