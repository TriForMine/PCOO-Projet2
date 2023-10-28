package Projet.Interfaces;

import javax.sound.midi.Sequence;

public interface IMidiReader {
    Sequence getMidiSequence(String path);
}
