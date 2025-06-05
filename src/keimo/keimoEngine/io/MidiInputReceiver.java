package keimo.keimoEngine.io;

import keimo.Peli;
import keimo.Peli.Ruudut;
import keimo.keimoEngine.menu.asetusRuudut.ÄäniTestiRuutu;

import java.util.Random;
import javax.sound.midi.*;

class MidiInputReceiver implements Receiver {

    private String name;
    private Random random = new Random();
    private Input input;

    public MidiInputReceiver(String name, Input input) {
        this.name = name;
        this.input = input;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        byte[] msgBytes = message.getMessage();
        if (message.getLength() >= 3) {
            int midiEvent = msgBytes[0];
            int midiNuotti = msgBytes[1];
            int voimakkuus = msgBytes[2];
            if (Peli.aktiivinenRuutu == Ruudut.ASETUSRUUTU_ÄÄNITESTI) {
                toistaMuunnettuÄäni(midiEvent, midiNuotti, voimakkuus);
            }
            else {
                toistaMidi(midiEvent, midiNuotti, voimakkuus);
            }
        }
    }

    @Override
    public void close() {
        
    }

    private void toistaMidi(int midiEvent, int midiNuotti, int voimakkuus) {
        try {
            Sequencer sequencer = input.getMidiSequencer();
            Sequence sequence = new Sequence(Sequence.PPQ, 24, 1);
            Track track1 = sequence.createTrack();

            int midiEventInt = 0;
            switch (midiEvent) {
                case -112: midiEventInt = ShortMessage.NOTE_ON; break;
                case -128: midiEventInt = ShortMessage.NOTE_OFF; break;
                default: System.out.println("unsupported midi event: " + midiEvent); break;
            }

            ShortMessage message = new ShortMessage();
            message.setMessage(midiEventInt, 0, midiNuotti, voimakkuus);
            MidiEvent event = new MidiEvent(message, 0);
            track1.add(event);
            sequencer.setSequence(sequence);
            sequencer.start();

        }
        catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private void toistaMuunnettuÄäni(int midiEvent, int midiNuotti, int voimakkuus) {
        int midiEventInt = 0;
        switch (midiEvent) {
            case -112: midiEventInt = ShortMessage.NOTE_ON; break;
            case -128: midiEventInt = ShortMessage.NOTE_OFF; break;
            default: System.out.println("unsupported midi event: " + midiEvent); break;
        }
        if (midiEventInt == ShortMessage.NOTE_ON) {
            ÄäniTestiRuutu.toistaValittuÄäni(haeMidiSyötteenSampleRate(midiNuotti));
        }
    }

    private float haeMidiSyötteenSampleRate(int midiNuotti) {
        float sampleRate = (float)(44100 * Math.pow(2d, (((double)midiNuotti-64d)/12d)));
        return sampleRate;
    }
}
