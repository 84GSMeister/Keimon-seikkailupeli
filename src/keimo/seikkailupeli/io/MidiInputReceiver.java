package keimo.seikkailupeli.io;

import keimo.keimoengine.io.Input;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.Ruudut;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu.AsetusRuudut;
import keimo.seikkailupeli.ruudut.asetusRuudut.äänitestiRuudut.ÄäniTestiRuutu;
import keimo.seikkailupeli.ruudut.asetusRuudut.äänitestiRuudut.ÄäniTestiWoof;

import javax.sound.midi.*;

class MidiInputReceiver implements Receiver {

    private String name;
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
            if (Peli.aktiivinenRuutu == Ruudut.ASETUSRUUTU) {
                if (AsetusRuutu.aktiivinenAsetusRuutu == AsetusRuudut.ÄÄNITESTI_PELIÄÄNET || AsetusRuutu.aktiivinenAsetusRuutu == AsetusRuudut.ÄÄNITESTI_WOOF) {
                    toistaMuunnettuÄäni(midiEvent, midiNuotti, voimakkuus);
                }
                else if (AsetusRuutu.aktiivinenAsetusRuutu == AsetusRuudut.ÄÄNITESTI_MIDI) {
                    toistaMidi(midiEvent, midiNuotti, voimakkuus);
                }
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
                default: System.out.println(name + ": unsupported midi event: " + midiEvent); break;
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
            if (Peli.aktiivinenRuutu == Ruudut.ASETUSRUUTU) {
                switch (AsetusRuutu.aktiivinenAsetusRuutu) {
                    case ÄÄNITESTI_PELIÄÄNET -> {
                        ÄäniTestiRuutu.toistaValittuÄäni(haeMidiSyötteenSampleRate(midiNuotti));
                    }
                    case ÄÄNITESTI_WOOF -> {
                        ÄäniTestiWoof.toistaValittuÄäni(haeMidiSyötteenSampleRate(midiNuotti));
                    }
                    default -> {
                        
                    }
                }
            }
        }
    }

    private float haeMidiSyötteenSampleRate(int midiNuotti) {
        float sampleRate = (float)(44100 * Math.pow(2d, (((double)midiNuotti-64d)/12d)));
        return sampleRate;
    }
}
