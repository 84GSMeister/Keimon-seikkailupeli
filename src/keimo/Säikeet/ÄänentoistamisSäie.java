package keimo.Säikeet;

import keimo.*;
import keimo.Utility.Downloaded.JavaMP3.Sound;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.media.AudioClip;

public class ÄänentoistamisSäie {
    
    public static AudioClip ääniToistin;
    private static Random r = new Random();
    protected static Vector<AudioClip> ääniJono = new Vector<>();
    public static List<String> musaLista;
    protected static Clip musaClip;
    protected static Clip ääniClip;
    protected static String nytSoi;
    private static Object äänisäikeenLukko = new Object();

    private static AudioInputStream woofStream;
    private static AudioInputStream resampledInputStream;
    private static HashMap<String, File> musaTiedostot = new HashMap<>();

    public static void lataaÄänet() {
        try {
            musaTiedostot.put("overworld", new File("tiedostot/musat/keimo/keimo_overworld.wav"));
            musaTiedostot.put("puisto", new File("tiedostot/musat/keimo/keimo_puisto.wav"));
            musaTiedostot.put("tarina", new File("tiedostot/musat/keimo/keimo_sad_tarina.wav"));
            musaTiedostot.put("boss", new File("tiedostot/musat/keimo/keimo_taistelu_boss_v2.wav"));
            musaTiedostot.put("valikko", new File("tiedostot/musat/keimo/keimo_valikko.wav"));
            musaTiedostot.put("metsä", new File("tiedostot/musat/keimo/keimo_metsä.wav"));
            musaTiedostot.put("koti", new File("tiedostot/musat/keimo/keimo_koti.wav"));
            musaTiedostot.put("baari", new File("tiedostot/musat/keimo/keimo_baari.wav"));
            musaTiedostot.put("kauppa", new File("tiedostot/musat/keimo/keimo_kauppa.wav"));
            musaTiedostot.put("temppeli", new File("tiedostot/musat/keimo/keimo_temppeli.wav"));

            woofStream = AudioSystem.getAudioInputStream(new File("tiedostot/äänet/woof.wav"));
            ääniClip = AudioSystem.getClip();
            musaClip = AudioSystem.getClip();
        } 
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void toistaWoof(float sampleRate) {
        try {
            AudioInputStream sourceStream = AudioSystem.getAudioInputStream(new File("tiedostot/äänet/woof.wav"));
            AudioFormat sourceFormat = sourceStream.getFormat();
            AudioFormat targetFormat = getOutFormat(sourceFormat, sampleRate);
            resampledInputStream = new AudioInputStream(sourceStream, targetFormat, AudioSystem.NOT_SPECIFIED);
            ääniClip.close();
            ääniClip.open(resampledInputStream);
            ääniClip.start();
        }
        catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
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

    private static int valitsePeliMusanLoopKohta(String musa) {
        int loopKohta = 0;
        double loopKohtaMs = 0;
        double sampleRate = 48_000;
        switch (musa) {
            case "overworld":   loopKohtaMs = 48_000; break;
            case "puisto":      loopKohtaMs = 60_000; break;
            case "tarina":      loopKohtaMs = 14_769; break;
            case "boss":        loopKohtaMs = 1_600; break;
            case "valikko":     loopKohtaMs = 6_400; break;
            case "metsä":       loopKohtaMs = 8_350; break;
            case "baari":       loopKohtaMs = 6_857; break;
            case "koti":        loopKohtaMs = 7_680; break;
            case "temppeli":    loopKohtaMs = 17_455; break;
            case "kauppa":      loopKohtaMs = 16_700; break;
            case null, default: loopKohtaMs = 0; break;
        }
        loopKohta = (int)((loopKohtaMs/1000d) * sampleRate);
        return loopKohta;
    }

    /**
     * Todo: Korjaa pelin pysähtyminen musan feidatessa.
     * Ehkä joku pakotettu latausruutu jolloin säie ehtii viimeistellä toimintansa.
     * @param musa
     */
    public static void toistaPeliMusa(String musa) {
        synchronized(äänisäikeenLukko) {
            try {
                if (nytSoi == null || !nytSoi.equals(musa)) {
                    nytSoi = musa;
                    if (musaClip != null) {
                        if (musaClip.getFramePosition() > 0) {
                            musaClip.stop();
                        }
                        else {
                            musaClip.stop();
                        }
                    }
                    AudioInputStream audioInputStream = null;
                    String tiedostotyyppi = "";
                    String tiedostonNimi = "";
                    if (PelinAsetukset.musiikkiPäällä) {
                        File musaTiedosto = musaTiedostot.get(musa);
                        tiedostonNimi = musaTiedosto.getName();
                        if (tiedostonNimi.length() > 3) {
                            tiedostotyyppi = tiedostonNimi.substring(tiedostonNimi.length()-3, tiedostonNimi.length());
                        }
                        switch (tiedostotyyppi) {
                            case "wav":
                                musaClip.close();    
                                audioInputStream = AudioSystem.getAudioInputStream(musaTiedosto);
                                musaClip.open(audioInputStream);
                            break;
                            case "mp3":
                                decodeMP3Data(tiedostonNimi);
                            break;
                        }
                        FloatControl gainControl = (FloatControl) musaClip.getControl(FloatControl.Type.MASTER_GAIN);
                        float gain = (float)(Math.pow(PelinAsetukset.musaVolyymi, (1f/9f))*80 -80);
                        gainControl.setValue(gain);
                        int loopStart = valitsePeliMusanLoopKohta(musa);
                        int loopEnd = musaClip.getFrameLength()-1;
                        musaClip.setLoopPoints(loopStart, loopEnd);
                        musaClip.loop(Clip.LOOP_CONTINUOUSLY);
                        musaClip.start();
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Musiikkia ei voitu toistaa");
                e.printStackTrace();
            }
        }
    }

    // private static void fadeVaihdaMusa(Clip clip) {
    //     //new Thread() {
    //     //    public void run() {
    //             //synchronized(äänisäikeenLukko) {
    //                 double vol = PelinAsetukset.musaVolyymi;
    //                 for (int i = 0; i < 100; i++) {
    //                     try {
    //                         FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    //                         float gain = (float)(Math.pow(vol, (1f/9f))*80 -80);
    //                         gainControl.setValue(gain);
    //                         vol -= 0.01;
    //                         Thread.sleep(20);
    //                     }
    //                     catch (Exception e) {
    //                         e.printStackTrace();
    //                     }
    //                 }
    //             //}
    //     //    }
    //     //}.start();
    // }

    private static AudioInputStream decodeMP3Data(String tiedostonNimi) {
        try {
            // Creating a Clip from the sound and play it using the plain javax.sound.sampled API
            Path path = Paths.get("", tiedostonNimi);
            Sound sound = new Sound(new BufferedInputStream(Files.newInputStream(path)));
            // We use an array to store the produced sound data (bad code style, but is okay for short sounds)
            // (We have to store the data in order to get the number of samples in it, because of the (dumb) Java sound API)
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // Read and decode the encoded sound data into the byte array output stream (blocking)
            int read = sound.decodeFullyInto(os);
            // A sample takes 2 bytes
            int samples = read / 2;
            // Java sound API stuff ...
            Clip clip = AudioSystem.getClip();
            AudioInputStream stream = new AudioInputStream(new ByteArrayInputStream(os.toByteArray()), sound.getAudioFormat(), samples);
            clip.open(stream);
            clip.start();
            System.out.println("pituus: " + clip.getMicrosecondLength());
            return stream;
        }
        catch (Exception e) {
            System.out.println("Decode failed.");
            e.printStackTrace();
            return null;
        }
    }

    public static void suljeMusa() {
        synchronized(äänisäikeenLukko) {
            nytSoi = null;
            if (musaClip != null) {
                musaClip.stop();
            }
        }
    }

    public static void asetaMusanVolyymi(double volyymi) {
        synchronized(äänisäikeenLukko) {
            if (musaClip != null) {
                FloatControl gainControl = (FloatControl) musaClip.getControl(FloatControl.Type.MASTER_GAIN);
                float gain = (float)(Math.pow(volyymi, (1f/9f))*80 -80);
                gainControl.setValue(gain);
            }
            PelinAsetukset.musaVolyymi = volyymi;
        }
    }

    public static void asetaSFXVolyymi(double volyymi) {
        if (ääniToistin != null) {
            ääniToistin.setVolume(volyymi);
        }
        PelinAsetukset.ääniVolyymi = volyymi;
    }

    static float log2(float n) {
        float result = (float)(Math.log(n)/Math.log(2));
        return result;
    }

    public static void toistaSFX(String ääni) {
        ääniToistin = new AudioClip(new File("tiedostot/äänet/selekt.wav").toURI().toString());
        double defaultVolume = ääniToistin.getVolume();
        double defaultPan = ääniToistin.getBalance();
        toistaSFX(ääni, defaultVolume, defaultPan);
    }

    public static void toistaSFX(String ääni, Point sijaintiKentällä) {
        double xEtäisyys = Pelaaja.hitbox.getCenterX() - sijaintiKentällä.getX();
        double yEtäisyys = Pelaaja.hitbox.getCenterY() - sijaintiKentällä.getY();
        double pan = -xEtäisyys/512;
        if (pan < -1) pan = -1;
        else if (pan > 1) pan = 1;
        double etäisyysKerroinV = Math.min(xEtäisyys, yEtäisyys);
        double volume = ((100 * etäisyysKerroinV) / 1024 + 100)/100;
        if (volume > 0.75) volume = 0.75;
        else if (volume < 0) volume = 0;
        toistaSFX(ääni, volume, pan);
    }

    public static void toistaSFX(String ääni, double volume, double pan) {

        switch (ääni) {

            case "pelaaja_damage":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/pelaaja_damage.mp3").toURI().toString());
            break;
            case "pikkuvihu_damage":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/pikkuvihu_damage.mp3").toURI().toString());
            break;
            case "Hyökkäys":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/hyökkäys.wav").toURI().toString());
            break;
            case "woof":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/woof.wav").toURI().toString());
            break;
            case "oven_avaus":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/risitas.wav").toURI().toString());
            break;
            case "oven_sulkeminen":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/ovi_kiinni.wav").toURI().toString());
            break;
            case "ammus":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/ammus.wav").toURI().toString());
            break;
            case "frans_cs":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/frans_cs.mp3").toURI().toString());
            break;
            case "nappi":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/nappi.wav").toURI().toString());
            break;
            case "portti":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/portti.wav").toURI().toString());
            break;
            case "tölkki":
                List<String> tölkkiÄäniLista = Stream.of(new File("tiedostot/äänet/tölkki").listFiles())
                .filter(file -> !file.isDirectory() && (file.getName().endsWith(".mp3")))
                .map(File::getName).sorted()
                .collect(Collectors.toList());
                int valitseÄäni = r.nextInt(tölkkiÄäniLista.size());
                ääniToistin = new AudioClip(new File("tiedostot/äänet/tölkki/tölkki" + valitseÄäni + ".mp3").toURI().toString());
            break;
            case "pullo":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/pullo.mp3").toURI().toString());
            break;
            case "Vesiämpäri":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/vihollinen_ämpäröinti.mp3").toURI().toString());
            break;
            case "Pesäpallomaila":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/vihollinen_mukilointi.mp3").toURI().toString());
            break;
            case "Pikkuvihu_damage":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/Pikkuvihu_damage.wav").toURI().toString());
            break;
            case "Pahavihu_damage":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/Pahavihu_damage.wav").toURI().toString());
            break;
            case "Asevihu_damage":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/Asevihu_damage.wav").toURI().toString());
            break;
            case "Pomo_damage":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/Boss_damage.wav").toURI().toString());
            break;
            case "Boss_death":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/Boss_death.wav").toURI().toString());
            break;
            case "Kolikko":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/koin.wav").toURI().toString());
            break;
            case "Kerää":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/kollekt.wav").toURI().toString());
            break;
            case "Pudota":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/pudota.wav").toURI().toString());
            break;
            case "Käytä":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/käytä.wav").toURI().toString());
            break;
            case "Valinta":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/selekt.wav").toURI().toString());
            break;
            case "Hyväksy":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/akkept.wav").toURI().toString());
            break;
            case "Kartta":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/kartta.mp3").toURI().toString());
            break;
            case "Juoman_kaato":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/juoman_kaato.mp3").toURI().toString());
            break;
            case "Kalja_kilinä":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/kalja_kilinä.mp3").toURI().toString());
            break;
            case "Tavoite_suoritettu":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/tavoite_suoritettu.wav").toURI().toString());
            break;
            case null, default:
            break;
        }
        if (ääniToistin != null) {
            ääniToistin.setVolume(volume * PelinAsetukset.ääniVolyymi);
            ääniToistin.setBalance(pan);
            ääniToistin.play();
        }
    }
}
