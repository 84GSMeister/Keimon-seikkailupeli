package keimo.keimoEngine.äänet;

import keimo.PelinAsetukset;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.media.AudioClip;

public class MidiToistin {

    public static AudioClip ääniToistin;
    protected static Vector<AudioClip> ääniJono = new Vector<>();
    public static List<String> musaLista;
    protected static Clip musaClip;
    protected static Clip ääniClip;
    protected static String nytSoi;

    private static AudioInputStream resampledInputStream;
    public static HashMap<String, File> musaTiedostot = new HashMap<>();
    public static HashMap<String, File> ääniTiedostot = new HashMap<>();

    private static int valitsePeliMusanLoopKohta(String musa, int sampleRate) {
        int loopKohta = 0;
        double loopKohtaMs = 0;
        switch (musa) {
            case "keimo_overworld.ogg":        loopKohtaMs = 48_000; break;
            case "keimo_puisto.ogg":           loopKohtaMs = 60_000; break;
            case "keimo_sad_tarina.ogg":       loopKohtaMs = 14_769; break;
            case "keimo_taistelu_boss_v2.ogg": loopKohtaMs = 1_600; break;
            case "keimo_valikko.ogg":          loopKohtaMs = 6_400; break;
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

    public static void toistaResamplattavaÄäni(float sampleRate) {
        toistaResamplattavaÄäni(sampleRate, ääniTiedostot.get("woof"), false);
    }

    static HashMap<Integer, Clip> woofÄänet = new HashMap<>();
    static int seuraavaWoofIndeksi = 0;
    public static void toistaResamplattavaÄäni(float sampleRate, File ääniTiedosto, boolean jatkuvaToisto) {
        try {
            if (woofÄänet.get(seuraavaWoofIndeksi) != null) {
                woofÄänet.get(seuraavaWoofIndeksi).close();
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

            if (woofÄänet.get(seuraavaWoofIndeksi) == null) {
                Clip clip = AudioSystem.getClip();
                woofÄänet.put(seuraavaWoofIndeksi, clip);
            }
            woofÄänet.get(seuraavaWoofIndeksi).open(resampledInputStream);
            if (jatkuvaToisto) {
                int loopStart = valitsePeliMusanLoopKohta(ääniTiedosto.getName(), 44100);
                int loopEnd = woofÄänet.get(seuraavaWoofIndeksi).getFrameLength()-1;
                woofÄänet.get(seuraavaWoofIndeksi).setLoopPoints(loopStart, loopEnd);
                woofÄänet.get(seuraavaWoofIndeksi).loop(Clip.LOOP_CONTINUOUSLY);
            }
            FloatControl gainControl = (FloatControl) woofÄänet.get(seuraavaWoofIndeksi).getControl(FloatControl.Type.MASTER_GAIN);
            float gain = (float)(Math.pow(PelinAsetukset.ääniVolyymi, (1f/9f))*80 -80);
            gainControl.setValue(gain);
            woofÄänet.get(seuraavaWoofIndeksi).start();

            seuraavaWoofIndeksi++;
            seuraavaWoofIndeksi %= 10;
        }
        catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public static void suljeMusat() {
        for (int i = 0; i < woofÄänet.size(); i++) {
            if (woofÄänet.get(i) != null) {
                woofÄänet.get(i).close();
            }
        }
    }

    private static AudioFormat getOutFormat(AudioFormat inFormat, float sampleRate) {
        Encoding enc = inFormat.getEncoding();
        int ch = inFormat.getChannels();
        float rate = inFormat.getSampleRate();
        boolean isBigEndian = inFormat.isBigEndian();
        return new AudioFormat(enc, sampleRate, 16, ch, ch * 2, rate, isBigEndian);
    }

    public static void resampleWoof(int sampleRate) {
        try {
            float targetRate = sampleRate;
            AudioInputStream sourceStream = AudioSystem.getAudioInputStream(new File("tiedostot/äänet/woof.wav"));
            AudioFormat sourceFormat = sourceStream.getFormat();
            AudioFormat targetFormat = new AudioFormat(
                sourceStream.getFormat().getEncoding(),
                targetRate,
                sourceFormat.getSampleSizeInBits(),
                sourceFormat.getChannels(),
                sourceFormat.getFrameSize(),
                targetRate,
                sourceFormat.isBigEndian()
            );
            resampledInputStream = AudioSystem.getAudioInputStream(targetFormat, sourceStream);
            ääniClip.close();
            ääniClip.open(resampledInputStream);
            FloatControl sampleRateControl = (FloatControl) ääniClip.getControl(FloatControl.Type.SAMPLE_RATE);
            float targetSampleRate = targetRate;
            sampleRateControl.setValue(targetSampleRate);
            ääniClip.start();
        }
        catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }
}
