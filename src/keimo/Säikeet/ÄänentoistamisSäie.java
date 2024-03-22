package keimo.Säikeet;

import keimo.*;

import java.awt.Point;
import java.io.File;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class ÄänentoistamisSäie extends Thread {
    
    public static AudioClip ääniToistin;
    public static MediaPlayer musiikkiSoitin;
    private static int musaValinta = 0;
    private static Random r = new Random();
    protected static Vector<AudioClip> ääniJono = new Vector<>();
    public static List<String> musaLista;

    public static void nollaa() {
        suljeMusiikki();
        ääniJono.clear();
    }

    public static void luoMusaKartta() {
        musaLista = Stream.of(new File("tiedostot/musat").listFiles())
            .filter(file -> !file.isDirectory() && (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")))
            .map(File::getName).sorted()
            .collect(Collectors.toList());
    }

    int valitseMusanLoopKohta() {
        int loopKohta = 0;
        double loopKohtaMs = 0;
        double sampleRate = 48_000;
        switch (PelinAsetukset.musiikkiValinta) {
            case 0: loopKohtaMs = 0; break;
            case 1: loopKohtaMs = 0; break;
            case 2: loopKohtaMs = 0; break;
            case 3: loopKohtaMs = 5_333; break;
            case 4: loopKohtaMs = 28_800; break;
            case 5: loopKohtaMs = 0; break;
            case 6: loopKohtaMs = 1_316; break;
            case 7: loopKohtaMs = 24_000; break;
        }
        loopKohta = (int)((loopKohtaMs/1000d) * sampleRate);
        System.out.println("loop-kohta: " + loopKohta);
        return loopKohta;
    }

    void toistaMusiikki() {
        musaValinta = PelinAsetukset.musiikkiValinta;
        String musaTiedosto = musaLista.get(musaValinta);
        if (musaTiedosto.endsWith(".wav")) {
            toistaMusiikkiClip(musaTiedosto);
        }
        else if (musaTiedosto.endsWith(".mp3") || musaTiedosto.endsWith(".ogg")){
            toistaMusiikkiJFXMediaPlayer(musaTiedosto);
        }
    }

    void toistaMusiikkiJFXMediaPlayer(String musaTiedosto) {
        try {
            nollaa();
            musiikkiSoitin = new MediaPlayer(new Media(new File("tiedostot/musat/" + musaTiedosto).toURI().toString()));
            musiikkiSoitin.setCycleCount(MediaPlayer.INDEFINITE);
            musiikkiSoitin.setVolume(PelinAsetukset.musaVolyymi);
            musiikkiSoitin.play();
        }
        catch (NullPointerException e) {
            System.out.println("Musiikkia ei voitu toistaa");
            e.printStackTrace();
        }
    }

    public static Clip clip;
    void toistaMusiikkiClip(String musaTiedosto) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("tiedostot/musat/" + musaTiedosto));
            nollaa();
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float gain = (float)(Math.pow(PelinAsetukset.musaVolyymi, (1f/9f))*80 -80);
            gainControl.setValue(gain);
            int loopStart = valitseMusanLoopKohta();
            int loopEnd = clip.getFrameLength()-1;
            clip.setLoopPoints(loopStart, loopEnd);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void asetaMusanVolyymi(double volyymi) {
        if (musiikkiSoitin != null) {
            musiikkiSoitin.setVolume(volyymi);
        }
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float gain = (float)(Math.pow(volyymi, (1f/9f))*80 -80);
            gainControl.setValue(gain);
        }
        PelinAsetukset.musaVolyymi = volyymi;
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

    public static void asetaMusatoistimenSijainti(double ms) {
        if (musiikkiSoitin != null) {
            musiikkiSoitin.seek(new Duration(ms));
        }
    }

    public static void suljeMusiikki() {
        if (musiikkiSoitin != null) {
            musiikkiSoitin.stop();
        }
        if (clip != null) {
            clip.stop();
        }
    }

    public static void toistaSFX(String ääni) {
        ääniToistin = new AudioClip(new File("tiedostot/äänet/pelaaja_damage.mp3").toURI().toString());
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
            case "Kerättävä":
                ääniToistin = new AudioClip(new File("tiedostot/äänet/kollekt.wav").toURI().toString());
            break;
            default:
            break;
        }
        if (ääniToistin != null) {
            ääniToistin.setVolume(volume * PelinAsetukset.ääniVolyymi);
            ääniToistin.setBalance(pan);

            // Toista suoraan
            ääniToistin.play();
            System.out.println("Toistetaan sfx: " + ääni + ", volume: " + ääniToistin.getVolume() + ", pan: " + ääniToistin.getBalance());

            // Käytä äänijonoa
            // if (ääniJono.size() < 10) {
            //     ääniJono.add(ääniToistin);
            // }
        }
    }

    public static void toistaÄäniJono() {
        try {
            //System.out.println("ääniä jonossa: " + ääniJono.size());
            for (AudioClip ac : ääniJono) {
                ac.setVolume(PelinAsetukset.ääniVolyymi);
                // ac.setOnEndOfMedia(new Runnable() {
                //     @Override
                //     public void run() {
                //         ääniJono.remove(ac);
                //     }
                // });
                ac.play();
            }
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin äänentoisto jonosta peruttiin (konkurrenssi-issue)");
        }
    }

    @Override
    public void run() {
        try {
            JFXPanel fxPanel = new JFXPanel();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initFX(fxPanel);
                    luoMusaKartta();
                    suljeMusiikki();
                    if (PelinAsetukset.musiikkiPäällä) {
                        toistaMusiikki();
                    }
                }
            });
        }
        catch (RuntimeException re) {
            re.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ei voitu ladata JavaFX:n ajonaikaisia grafiikkakirjastoja.\nMediaelementit eivät välttämättä toimi.", "Virhe ladatessa kirjastoja", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = luoJFXScene();
        fxPanel.setScene(scene);
    }

    private static Scene luoJFXScene() {
        Group root = new Group();
        Scene scene = new Scene(root, 640, 400, javafx.scene.paint.Color.ALICEBLUE);
        return scene;
    }
}
