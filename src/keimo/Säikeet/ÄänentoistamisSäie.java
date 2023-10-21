package keimo.Säikeet;

import keimo.*;
import keimo.Utility.Sound;

import java.io.File;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ÄänentoistamisSäie extends Thread {
    
    public static MediaPlayer ääniToistin;
    public static MediaPlayer musiikkiSoitin;
    public static AudioClip musiikkiSoitin2;
    public static Sound musiikkiSoitin3;
    private static int musaValinta = 0;
    private static Duration musaLoopKohta;
    private static Random r = new Random();
    protected static Vector<MediaPlayer> ääniJono = new Vector<>();
    public static List<String> musaLista;

    public static void nollaa() {
        suljeMusiikki();
        ääniJono.clear();
    }

    void luoMusaKartta() {
        musaLista = Stream.of(new File("tiedostot/musat").listFiles())
            .filter(file -> !file.isDirectory() && (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")))
            .map(File::getName).sorted()
            .collect(Collectors.toList());
    }

    double valitseMusanLoopKohta() {
        switch (PelinAsetukset.musiikkiValinta) {
            case 0: musaLoopKohta = new Duration(0); break;
            case 1: musaLoopKohta = new Duration(0); break;
            case 2: musaLoopKohta = new Duration(0); break;
            case 3: musaLoopKohta = new Duration(2667); break;
            case 4: musaLoopKohta = new Duration(28800); break;
            case 5: musaLoopKohta = new Duration(0); break;
            case 6: musaLoopKohta = new Duration(0); break;
            case 7: musaLoopKohta = new Duration(19200); break;
        }
        return musaLoopKohta.toMillis();
    }

    void toistaMusiikkiJFXMediaPlayer() {
        try {
            nollaa();
            musaValinta = PelinAsetukset.musiikkiValinta;
            musiikkiSoitin = new MediaPlayer(new Media(new File("tiedostot/musat/" + musaLista.get(musaValinta)).toURI().toString()));
            if (valitseMusanLoopKohta() > 0) {
                // musiikkiSoitin.setOnEndOfMedia(new Runnable() {
                //     @Override
                //     public void run() {
                //         double alkuaika = System.nanoTime();
                //         musiikkiSoitin.seek(musaLoopKohta);
                //         musiikkiSoitin.play();
                //         double loppuAika = System.nanoTime();
                //         System.out.println("loopin viive: " + (loppuAika - alkuaika)/1_000_000d + " ms");
                //     }
                // });
            }
            musiikkiSoitin.setCycleCount(MediaPlayer.INDEFINITE);
            musiikkiSoitin.setVolume(PelinAsetukset.musaVolyymi);
            if (PelinAsetukset.musiikkiPäällä) {
                musiikkiSoitin.play();
                //ääniJono.add(musiikkiSoitin);
            }
        }
        catch (NullPointerException e) {
            System.out.println("Musiikkia ei voitu toistaa");
            e.printStackTrace();
        }
    }

    void toistaMusiikkiJFXAudioClip() {
        try {
            suljeMusiikki();
            musaValinta = PelinAsetukset.musiikkiValinta;
            musiikkiSoitin2 = new AudioClip(new File("tiedostot/musat/" + musaLista.get(musaValinta)).toURI().toString());
            musiikkiSoitin2.setVolume(PelinAsetukset.musaVolyymi);
            musiikkiSoitin2.setCycleCount(AudioClip.INDEFINITE);
            if (PelinAsetukset.musiikkiPäällä) {
                musiikkiSoitin2.play();
            }
        }
        catch (NullPointerException e) {
            System.out.println("Musiikkia ei voitu toistaa");
            e.printStackTrace();
        }
    }

    void toistaMusiikkiJavaxSound() {
        musiikkiSoitin3 = new Sound(new File("tiedostot/musat/" + musaLista.get(musaValinta)).toURI().toString(), true);
        musiikkiSoitin3.play();
    }

    public static void asetaMusanVolyymi(double volyymi) {
        if (musiikkiSoitin != null) {
            musiikkiSoitin.setVolume(volyymi);
            //System.out.println("Musan volyymi: " + musiikkiSoitin.getVolume());
        }
        if (musiikkiSoitin2 != null) {
            musiikkiSoitin2.setVolume(volyymi);
            if (musiikkiSoitin2.isPlaying()) {
                musiikkiSoitin2.stop();
                musiikkiSoitin2.play();
            }
            //System.out.println("Musan volyymi: " + musiikkiSoitin2.getVolume());
        }
    }

    public static void asetaMusatoistimenSijainti(double ms) {
        if (musiikkiSoitin != null) {
            musiikkiSoitin.seek(new Duration(ms));
            //System.out.println("Musiikkisoitin: Siirrytään kohtaan " + ms + " ms");
        }
    }

    public static void suljeMusiikki() {
        if (musiikkiSoitin != null) {
            musiikkiSoitin.stop();
        }
        if (musiikkiSoitin2 != null) {
            musiikkiSoitin2.stop();
        }
    }

    public static void toistaSFX(String ääni) {

        switch (ääni) {

            case "pelaaja_damage":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/pelaaja_damage.mp3").toURI().toString()));
            break;
            case "pikkuvihu_damage":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/pikkuvihu_damage.mp3").toURI().toString()));
            break;
            case "woof":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/woof.wav").toURI().toString()));
            break;
            case "oven_avaus":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/risitas.wav").toURI().toString()));
            break;
            case "oven_sulkeminen":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/ovi_kiinni.wav").toURI().toString()));
            break;
            case "ammus":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/ammus.wav").toURI().toString()));
            break;
            case "frans_cs":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/frans_cs.mp3").toURI().toString()));
            break;
            case "nappi":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/nappi.wav").toURI().toString()));
            break;
            case "portti":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/portti.wav").toURI().toString()));
            break;

            case "tölkki":
                List<String> tölkkiÄäniLista = Stream.of(new File("tiedostot/äänet/tölkki").listFiles())
                .filter(file -> !file.isDirectory() && (file.getName().endsWith(".mp3")))
                .map(File::getName).sorted()
                .collect(Collectors.toList());
                int valitseÄäni = r.nextInt(tölkkiÄäniLista.size());
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/tölkki/tölkki" + valitseÄäni + ".mp3").toURI().toString()));
            break;
            case "Vesiämpäri":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/vihollinen_ämpäröinti.mp3").toURI().toString()));
            break;
            case "Pesäpallomaila":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/vihollinen_mukilointi.mp3").toURI().toString()));
            break;
            case "Pikkuvihu_damage":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/Pikkuvihu_damage.wav").toURI().toString()));
            break;
            case "Pahavihu_damage":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/Pahavihu_damage.wav").toURI().toString()));
            break;
            case "Asevihu_damage":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/Asevihu_damage.wav").toURI().toString()));
            break;
            case "Pomo_damage":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/Boss_damage.wav").toURI().toString()));
            break;
            case "Boss_death":
                ääniToistin = new MediaPlayer(new Media(new File("tiedostot/äänet/Boss_death.wav").toURI().toString()));
            break;
            default:
            break;
        }
        if (ääniToistin != null) {
            ääniToistin.setVolume(PelinAsetukset.ääniVolyymi);
            //ääniToistin.play();
            if (ääniJono.size() < 10) {
                ääniJono.add(ääniToistin);
            }
        }
    }

    public static void toistaÄäniJono() {
        try {
            //System.out.println("ääniä jonossa: " + ääniJono.size());
            for (MediaPlayer mp : ääniJono) {
                mp.setVolume(PelinAsetukset.ääniVolyymi);
                mp.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        ääniJono.remove(mp);
                    }
                });
                mp.play();
            }
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin äänentoisto jonosta peruttiin (konkurrenssi-issue)");
        }
    }

    @Override
    public void run() {
        JFXPanel fxPanel = new JFXPanel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
                luoMusaKartta();
                if (valitseMusanLoopKohta() > 0) {
                    toistaMusiikkiJFXMediaPlayer();
                }
                else {
                    //toistaMusiikkiJFXAudioClip();
                    toistaMusiikkiJFXMediaPlayer();
                    //toistaMusiikkiJavaxSound();
                }
            }
        });
        // while (true) {
        //     double alkuaika = System.nanoTime();
        //     if (musiikkiSoitin != null) {
        //         if (musiikkiSoitin.getCurrentTime().lessThan(musiikkiSoitin.getStopTime())) {
        //             //System.out.println(musiikkiSoitin.getCurrentTime() + " of " + musiikkiSoitin.getStopTime());
        //         }
        //         else {
        //             musiikkiSoitin.seek(musaLoopKohta);
        //             double loppuAika = System.nanoTime();
        //             System.out.println("loopin viive: " + (loppuAika - alkuaika)/1_000_000d + " ms");
        //             // try {
        //             //     Thread.sleep(10);
        //             // }
        //             // catch (InterruptedException ie) {
        //             //     ie.printStackTrace();
        //             // }
        //             break;
        //         }
        //     }
        // }
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
