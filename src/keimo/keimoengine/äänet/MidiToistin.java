package keimo.keimoengine.äänet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

public class MidiToistin {

    public static List<String> musaLista;
    private static Sequencer sequencer;

    public static void toistaMidiMusa(File ääniTiedosto, double volume) {
        toistaMidiMusa(ääniTiedosto, null, volume, 44100, true);
    }

    public static void toistaMidiMusa(File ääniTiedosto, File soundFont, double volume) {
        toistaMidiMusa(ääniTiedosto, soundFont, volume, 44100, true);
    }

    public static void toistaMidiMusa(File ääniTiedosto, File soundFont, double volume, boolean rummut) {
        toistaMidiMusa(ääniTiedosto, soundFont, volume, 44100, rummut);
    }

    public static void toistaMidiMusa(File ääniTiedosto, File soundFont, double volume, float sampleRate, boolean rummut) {
        try {
            float tempoKerroin = sampleRate/44100;
            sequencer = MidiSystem.getSequencer(false);
            sequencer.setTempoFactor(tempoKerroin);
            Sequence sequence;
            sequence = MidiSystem.getSequence(ääniTiedosto);
            Synthesizer synthesizer = MidiSystem.getSynthesizer();

            sequencer.open();
            synthesizer.open();
            Receiver receiver = synthesizer.getReceiver();
            int value_14bits = (int)(volume * 16383);
            value_14bits = Math.max(Math.min(value_14bits, 16383), 0);
            byte[] volumeData = new byte[] {0x7F, 0x7F, 0x04, 0x01, (byte)(value_14bits & 0x7f), (byte)(value_14bits >> 7)};
            SysexMessage volumeMessage = new SysexMessage(0xF0, volumeData, volumeData.length);
            receiver.send(volumeMessage, -1);

            if (!rummut) {
                if (sequence.getTracks().length >= 10) {
                    Track track10 = sequence.getTracks()[9];
                    sequence.deleteTrack(track10);
                }
            }

            if (soundFont == null) {
                Transmitter transmitter = sequencer.getTransmitter();
                transmitter.setReceiver(receiver);
                synthesizer.open();

                sequencer.setSequence(sequence);
                sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
                sequencer.start();
            }
            else {
                Soundbank soundfont = MidiSystem.getSoundbank(soundFont);

                Transmitter transmitter = sequencer.getTransmitter();
                transmitter.setReceiver(receiver);
                synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
                synthesizer.loadAllInstruments(soundfont);
                synthesizer.open();

                sequencer.setSequence(sequence);
                sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
                sequencer.start();
            }

            

        }
        catch (InvalidMidiDataException | MidiUnavailableException | IOException | IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void suljeMusat() {
        if (sequencer != null) {
            sequencer.stop();
        }
    }
}
