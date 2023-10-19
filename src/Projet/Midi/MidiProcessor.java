package Projet.Midi;

import Projet.Common.Note;
import Projet.Common.NoteEnum;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

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
        float lastNoteTime = 0;

        Track[] tracks = sequence.getTracks();
        for (Track track : tracks) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                float time = getTime(sequence, event);

                if (event.getMessage() instanceof ShortMessage shortMessage) {
                    if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
                        lastNoteTime = time;
                    } else if (shortMessage.getCommand() == ShortMessage.NOTE_OFF) {
                        float duration = time - lastNoteTime;

                        Note note = processNoteOff(shortMessage, time, duration);
                        notes.add(note);
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

    private static float getTime(Sequence sequence, MidiEvent event) {
        int tempo = getTempo(sequence);

        long resolution = sequence.getResolution();
        return ((float) event.getTick()) * 60 / (4 * tempo * resolution);
    }
}
