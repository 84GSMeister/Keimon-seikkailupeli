package keimo.keimoEngine.äänet;

import keimo.PelinAsetukset;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
import javax.sound.midi.Transmitter;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MidiToistin {

    public static List<String> musaLista;
    private static int maxÄäntenMäärä = 5;
    private static Sequencer sequencer;
    private static AudioInputStream resampledInputStream;

    private static int valitsePeliMusanLoopKohta(String musa, int sampleRate) {
        int loopKohta = 0;
        double loopKohtaMs = 0;
        switch (musa) {
            case "keimo_overworld.ogg":        loopKohtaMs = 48_000; break;
            case "keimo_puisto.ogg":           loopKohtaMs = 60_000; break;
            case "keimo_sad_tarina.ogg":       loopKohtaMs = 14_769; break;
            case "keimo_taistelu_boss_v2.ogg": loopKohtaMs = 1_600; break;
            case "keimo_valikko.mp3":          loopKohtaMs = 6_400; break;
            case "keimo_metsä.ogg":            loopKohtaMs = 8_350; break;
            case "keimo_baari.ogg":            loopKohtaMs = 6_857; break;
            case "keimo_koti.ogg":             loopKohtaMs = 7_680; break;
            case "keimo_temppeli.ogg":         loopKohtaMs = 17_455; break;
            case "keimo_kauppa.ogg":           loopKohtaMs = 16_700; break;
            case "keimo_kuu.ogg":              loopKohtaMs = 27_429; break;
            case "0_udo_haukkuu_90s.ogg":      loopKohtaMs = 0; break;
            case "1_udo_haukkuu_diiduu.ogg":   loopKohtaMs = 0; break;
            case "2_udo_haukkuu_kylie.ogg":    loopKohtaMs = 0; break;
            case "3_udo_haukkuu_mario2.ogg":   loopKohtaMs = 5_333; break;
            case "4_udo_haukkuu_nyän.ogg":     loopKohtaMs = 28_800; break;
            case "5_udo_haukkuu_smw.ogg":      loopKohtaMs = 0; break;
            case "6_udo_haukkuu_rick.ogg":     loopKohtaMs = 1_316; break;
            case "7_udo_haukkuu_wide.ogg":     loopKohtaMs = 24_000; break;
            case null, default:                loopKohtaMs = 0; break;
        }
        loopKohta = (int)((loopKohtaMs/1000d) * sampleRate);
        return loopKohta;
    }

    private static int valitseWoofMusanLoopKohta(String musa, float tempokerroin) {
        int loopKohtaMidiTick = 0;
        switch (musa) {
            //case "keimo_overworld.ogg":        loopKohtaMidiTick = 48_000; break;
            case "keimo_puisto.ogg":           loopKohtaMidiTick = 10_560; break;
            case "keimo_sad_tarina.ogg":       loopKohtaMidiTick = 15_360; break;
            case "keimo_taistelu_boss_v2.ogg": loopKohtaMidiTick = 1_920; break;
            //case "keimo_valikko.mp3":          loopKohtaMidiTick = 7_680; break;
            //case "keimo_metsä.ogg":            loopKohtaMidiTick = 8_350; break;
            //case "keimo_baari.ogg":            loopKohtaMidiTick = 6_857; break;
            //case "keimo_koti.ogg":             loopKohtaMidiTick = 7_680; break;
            //case "keimo_temppeli.ogg":         loopKohtaMidiTick = 17_455; break;
            //case "keimo_kauppa.ogg":           loopKohtaMidiTick = 16_700; break;
            case "keimo_kuu.ogg":              loopKohtaMidiTick = 30_720; break;
            case null, default:                loopKohtaMidiTick = 0; break;
        }
        return loopKohtaMidiTick;
    }

    static HashMap<Integer, Clip> ääniClipit = new HashMap<>();
    static int seuraavaÄäniIndeksi = 0;
    public static void toistaResamplattavaÄäni(float sampleRate, File ääniTiedosto, boolean musa, boolean toistaWoof) {
        try {
            if (toistaWoof) toistaMidiWoof(ääniTiedosto, sampleRate);

            if (ääniClipit.get(seuraavaÄäniIndeksi) != null) {
                ääniClipit.get(seuraavaÄäniIndeksi).close();
            }

            AudioInputStream sourceStream;
            if (ääniTiedosto.getName().endsWith(".wav")) {
                sourceStream = AudioSystem.getAudioInputStream(ääniTiedosto);
            }
            else if (ääniTiedosto.getName().endsWith(".ogg")) {
                sourceStream = Dekoodaus.decodeOgg(ääniTiedosto.getPath());
            }
            else if (ääniTiedosto.getName().endsWith(".mp3")) {
                sourceStream = Dekoodaus.decodeMP3(ääniTiedosto.getPath());
            }
            else {
                throw new UnsupportedAudioFileException();
            }
            AudioFormat sourceFormat = sourceStream.getFormat();
            AudioFormat targetFormat = getOutFormat(sourceFormat, sampleRate);
            resampledInputStream = new AudioInputStream(sourceStream, targetFormat, AudioSystem.NOT_SPECIFIED);

            if (ääniClipit.get(seuraavaÄäniIndeksi) == null) {
                Clip clip = AudioSystem.getClip();
                ääniClipit.put(seuraavaÄäniIndeksi, clip);
            }
            ääniClipit.get(seuraavaÄäniIndeksi).open(resampledInputStream);
            if (musa) {
                int loopStart = valitsePeliMusanLoopKohta(ääniTiedosto.getName(), 44100);
                int loopEnd = ääniClipit.get(seuraavaÄäniIndeksi).getFrameLength()-1;
                ääniClipit.get(seuraavaÄäniIndeksi).setLoopPoints(loopStart, loopEnd);
                ääniClipit.get(seuraavaÄäniIndeksi).loop(Clip.LOOP_CONTINUOUSLY);
            }
            FloatControl gainControl = (FloatControl) ääniClipit.get(seuraavaÄäniIndeksi).getControl(FloatControl.Type.MASTER_GAIN);
            float gain = 0;
            if (musa) gain = (float)(Math.pow(PelinAsetukset.musaVolyymi, (1f/9f))*80 -80);
            else gain = (float)(Math.pow(PelinAsetukset.ääniVolyymi, (1f/9f))*80 -80);
            gainControl.setValue(gain);
            ääniClipit.get(seuraavaÄäniIndeksi).start();

            seuraavaÄäniIndeksi++;
            seuraavaÄäniIndeksi %= maxÄäntenMäärä;
        }
        catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public static void toistaMidiWoof(File ääniTiedosto, float sampleRate) {
        try {
            float tempoKerroin = sampleRate/44100;
            sequencer = MidiSystem.getSequencer(false);
            sequencer.setTempoFactor(tempoKerroin);
            Sequence sequence;
            switch (ääniTiedosto.getName()) {
                case "keimo_puisto.ogg" -> {
                    sequence = MidiSystem.getSequence(new File("tiedostot/äänet/midi/keimo_puisto_woof.mid"));
                }
                case "keimo_sad_tarina.ogg" -> {
                    sequence = MidiSystem.getSequence(new File("tiedostot/äänet/midi/keimo_tarina_woof.mid"));
                }
                case "keimo_taistelu_boss_v2.ogg" -> {
                    sequence = MidiSystem.getSequence(new File("tiedostot/äänet/midi/keimo_boss_woof.mid"));
                }
                case "keimo_kuu.ogg" -> {
                    sequence = MidiSystem.getSequence(new File("tiedostot/äänet/midi/keimo_kuu_woof.mid"));
                }
                case null, default -> {
                    sequence = MidiSystem.getSequence(new File("tiedostot/äänet/midi/testimidi.mid"));
                }
            }
            Soundbank soundfont = MidiSystem.getSoundbank(new File("tiedostot/äänet/midi/Woof_Soundfont.sf2"));
            Synthesizer synthesizer = MidiSystem.getSynthesizer();

            sequencer.open();
            synthesizer.open();
            Receiver receiver = synthesizer.getReceiver();
            int value_14bits = (int)(PelinAsetukset.ääniVolyymi * 16383);
            value_14bits = Math.max(Math.min(value_14bits, 16383), 0);
            byte[] volumeData = new byte[] {0x7F, 0x7F, 0x04, 0x01, (byte)(value_14bits & 0x7f), (byte)(value_14bits >> 7)};
            SysexMessage volumeMessage = new SysexMessage(0xF0, volumeData, volumeData.length);
            receiver.send(volumeMessage, -1);

            Transmitter transmitter = sequencer.getTransmitter();
            transmitter.setReceiver(receiver);
            synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
            synthesizer.loadAllInstruments(soundfont);
            synthesizer.open();

            sequencer.setSequence(sequence);
            sequencer.setLoopStartPoint(valitseWoofMusanLoopKohta(ääniTiedosto.getName(), tempoKerroin));
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();

        }
        catch (InvalidMidiDataException | MidiUnavailableException | IOException | IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void suljeMusat() {
        for (int i = 0; i < ääniClipit.size(); i++) {
            if (ääniClipit.get(i) != null) {
                ääniClipit.get(i).close();
            }
        }
        if (sequencer != null) {
            sequencer.stop();
        }
    }

    private static AudioFormat getOutFormat(AudioFormat inFormat, float sampleRate) {
        Encoding enc = inFormat.getEncoding();
        int ch = inFormat.getChannels();
        float rate = inFormat.getSampleRate();
        boolean isBigEndian = inFormat.isBigEndian();
        return new AudioFormat(enc, sampleRate, 16, ch, ch * 2, rate, isBigEndian);
    }
}
