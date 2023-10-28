package Projet.Midi;

import Projet.Common.Note;
import Projet.Common.NoteEnum;
import Projet.Interfaces.IMidiReader;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MidiProcessor {
    public static final IMidiReader MidiReader = new MidiReader();

    private MidiProcessor() {
        throw new IllegalStateException();
    }

    public static List<Note> processFile(String path) {
        Sequence sequence = MidiReader.getMidiSequence(path);
        return processMidiSequence(sequence);
    }

    private static Note processNoteOff(ShortMessage message, float time, float duration) {
        int channel = message.getChannel();
        int key = message.getData1();
        int octave = getOctave(key);
        int noteNumber = getNoteNumber(key);
        int volume = message.getData2();

        return new Note(
                channel,
                octave,
                NoteEnum.fromValue(noteNumber),
                volume,
                duration,
                time
        );
    }

    public static List<Note> processMidiSequence(Sequence sequence) {
        List<Note> notes = new ArrayList<>();
        Map<Integer, Float> activeNotes = new HashMap<>();
        int tempo = getTempo(sequence);
        float timeFactor = 60.0f / ((tempo * sequence.getResolution()) << 2);

        Track[] tracks = sequence.getTracks();
        for (Track track : tracks) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);

                if (event.getMessage() instanceof ShortMessage shortMessage) {
                    int key = (shortMessage.getChannel() << 7) + shortMessage.getData1();
                    float time = event.getTick() * timeFactor;

                    if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
                        activeNotes.put(key, time);
                    } else if (shortMessage.getCommand() == ShortMessage.NOTE_OFF) {
                        Float noteOnTime = activeNotes.remove(key);

                        if (noteOnTime != null) {
                            Note note = processNoteOff(shortMessage, time, time - noteOnTime);
                            notes.add(note);
                        }
                    }
                }
            }
        }

        return notes;
    }

    private static int getOctave(int note) {
        return note / 12 - 1;
    }

    private static int getNoteNumber(int note) {
        return note % 12;
    }

    private static int getTempo(Sequence sequence) {
        if (sequence.getMicrosecondLength() == 0) {
            return 120;
        }

        Track[] tracks = sequence.getTracks();

        for (Track track : tracks) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);

                if (event.getMessage() instanceof MetaMessage metaMessage) {
                    byte[] msg = metaMessage.getMessage(); // Le contenu du message
                    if (((msg[1] & 0xFF) == 0x51) && (msg[2] == 0x03)) {
                        int mspq = (msg[5] & 0xFF)
                                | ((msg[4] & 0xFF) << 8)
                                | ((msg[3] & 0xFF) << 16); // Une manipulation fastidieuse
                        return Math.round(60000001f / mspq); // Le tempo est calculé
                    }
                }
            }
        }

        return 120;
    }
}
