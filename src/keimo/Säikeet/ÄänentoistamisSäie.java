package keimo.Säikeet;

import keimo.*;
import keimo.Utility.Downloaded.JavaMP3.Sound;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import javafx.scene.media.AudioClip;

public class ÄänentoistamisSäie {
    
    public static AudioClip ääniToistin;
    private static Random r = new Random();
    protected static Vector<AudioClip> ääniJono = new Vector<>();
    public static List<String> musaLista;
    protected static Clip clip;
    protected static String nytSoi;
    private static Object äänisäikeenLukko = new Object();

    private static int valitsePeliMusanLoopKohta(String musa) {
        int loopKohta = 0;
        double loopKohtaMs = 0;
        double sampleRate = 48_000;
        switch (musa) {
            case "overworld":
                loopKohtaMs = 48_000;
            break;
            case "puisto":
                loopKohtaMs = 60_000;
            break;
            case "tarina":
                loopKohtaMs = 14_769;
            break;
            case "boss":
                loopKohtaMs = 1_600;
            break;
            case "valikko":
                loopKohtaMs = 6_400;
            break;
            case "metsä":
                loopKohtaMs = 8_350;
            break;
            case "baari":
                loopKohtaMs = 6_857;
            break;
            case "koti":
                loopKohtaMs = 7_680;
            break;
            case "temppeli":
                loopKohtaMs = 17_455;
            break;
            case "kauppa":
                loopKohtaMs = 16_700;
            break;
            case null, default:
                loopKohtaMs = 0;
            break;
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
                    if (clip != null) {
                        if (clip.getFramePosition() > 0) {
                            clip.stop();
                        }
                        else {
                            clip.stop();
                        }
                    }
                    AudioInputStream audioInputStream = null;
                    boolean toista = true;
                    String tiedostotyyppi = "";
                    String tiedostonNimi = "";
                    switch (musa) {
                        case "overworld":
                            tiedostonNimi = "tiedostot/musat/keimo/keimo_overworld.wav";
                        break;
                        case "puisto":
                            tiedostonNimi = "tiedostot/musat/keimo/keimo_puisto.wav";
                        break;
                        case "tarina":
                            tiedostonNimi = "tiedostot/musat/keimo/keimo_sad_tarina.wav";
                        break;
                        case "boss":
                            tiedostonNimi = "tiedostot/musat/keimo/keimo_taistelu_boss_v2.wav";
                        break;
                        case "valikko":
                            tiedostonNimi = "tiedostot/musat/keimo/keimo_valikko.wav";
                        break;
                        case "metsä":
                            tiedostonNimi = "tiedostot/musat/keimo/keimo_metsä.wav";
                        break;
                        case "koti":
                            tiedostonNimi = "tiedostot/musat/keimo/keimo_koti.wav";
                        break;
                        case "baari":
                            tiedostonNimi = "tiedostot/musat/keimo/keimo_baari.wav";
                        break;
                        case "kauppa":
                            tiedostonNimi = "tiedostot/musat/keimo/keimo_kauppa.wav";
                        break;
                        case "temppeli":
                            tiedostonNimi = "tiedostot/musat/keimo/keimo_temppeli.wav";
                        break;
                        case null, default:
                            toista = false;
                        break;
                    }
                    if (toista && PelinAsetukset.musiikkiPäällä) {
                        clip = AudioSystem.getClip();
                        tiedostotyyppi = tiedostonNimi.substring(tiedostonNimi.length()-3, tiedostonNimi.length());
                        switch (tiedostotyyppi) {
                            case "wav":
                                audioInputStream = AudioSystem.getAudioInputStream(new File(tiedostonNimi));    
                                clip.open(audioInputStream);
                            break;
                            case "mp3":
                                decodeMP3Data(tiedostonNimi);
                            break;
                        }
                        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        float gain = (float)(Math.pow(PelinAsetukset.musaVolyymi, (1f/9f))*80 -80);
                        gainControl.setValue(gain);
                        int loopStart = valitsePeliMusanLoopKohta(musa);
                        int loopEnd = clip.getFrameLength()-1;
                        clip.setLoopPoints(loopStart, loopEnd);
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                        clip.start();
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Musiikkia ei voitu toistaa");
                e.printStackTrace();
            }
        }
    }

    private static void fadeVaihdaMusa(Clip clip) {
        //new Thread() {
        //    public void run() {
                //synchronized(äänisäikeenLukko) {
                    double vol = PelinAsetukset.musaVolyymi;
                    for (int i = 0; i < 100; i++) {
                        try {
                            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                            float gain = (float)(Math.pow(vol, (1f/9f))*80 -80);
                            gainControl.setValue(gain);
                            vol -= 0.01;
                            Thread.sleep(20);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                //}
        //    }
        //}.start();
    }

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
            if (clip != null) {
                clip.stop();
            }
        }
    }

    public static void asetaMusanVolyymi(double volyymi) {
        synchronized(äänisäikeenLukko) {
            if (clip != null) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
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
