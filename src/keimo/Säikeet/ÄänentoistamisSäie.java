package keimo.Säikeet;

import keimo.*;

import java.io.File;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ÄänentoistamisSäie extends Thread {
    
    public static MediaPlayer ääniToistin;
    public static MediaPlayer musiikkiSoitin;
    public static AudioClip musiikkiSoitin2;
    private static int musaValinta = 0;
    private static Duration musaLoopKohta;
    private static Random r = new Random();
    public static List<String> musaLista;

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
            suljeMusiikki();
            musaValinta = PelinAsetukset.musiikkiValinta;
            musiikkiSoitin = new MediaPlayer(new Media(new File("tiedostot/musat/" + musaLista.get(musaValinta)).toURI().toString()));
            musiikkiSoitin.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    musiikkiSoitin.seek(musaLoopKohta);
                    musiikkiSoitin.play();
                }
            });
            musiikkiSoitin.setCycleCount(Integer.MAX_VALUE);
            musiikkiSoitin.setVolume(PelinAsetukset.musaVolyymi);
            if (PelinAsetukset.musiikkiPäällä) {
                musiikkiSoitin.play();
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
            musiikkiSoitin2.setCycleCount(Integer.MAX_VALUE);
            if (PelinAsetukset.musiikkiPäällä) {
                musiikkiSoitin2.play();
            }
        }
        catch (NullPointerException e) {
            System.out.println("Musiikkia ei voitu toistaa");
            e.printStackTrace();
        }
    }

    public static void asetaMusanVolyymi(double volyymi) {
        if (musiikkiSoitin != null) {
            musiikkiSoitin.setVolume(volyymi);
            System.out.println("Musan volyymi: " + musiikkiSoitin.getVolume());
        }
        if (musiikkiSoitin2 != null) {
            musiikkiSoitin2.setVolume(volyymi);
            if (musiikkiSoitin2.isPlaying()) {
                musiikkiSoitin2.stop();
                musiikkiSoitin2.play();
            }
            System.out.println("Musan volyymi: " + musiikkiSoitin2.getVolume());
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
            default:
            break;
        }
        if (ääniToistin != null) {
            ääniToistin.setVolume(PelinAsetukset.ääniVolyymi);
            ääniToistin.play();
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
                }
            }
        });
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
