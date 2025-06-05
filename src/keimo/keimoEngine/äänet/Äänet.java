package keimo.keimoEngine.äänet;

import keimo.Pelaaja;
import keimo.PelinAsetukset;
import keimo.keimoEngine.assets.Assets;

import java.awt.Point;
import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Äänet {
    protected static HashMap<Integer, Clip> ääniClipit = new HashMap<>();
    private static int seuraavaÄäniIndeksi = 0;
    private static int maxÄäntenMäärä = 5;
    private static double defaultVolume = 1;
    private static double defaultPan = 0;

    public static void toistaSFX(String ääni) {
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
        try {
            toistaÄäni(ääni, volume, pan);
        }
        catch (Exception e) {
            System.out.println("Äänitiedostoa \"" + ääni + "\" ei löytynyt");
            e.printStackTrace();
        }
    }

    public static void toistaÄäni(String ääni, double volume, double pan) {
        try {
            for (Clip clip : ääniClipit.values()) {
                if (!clip.isActive()) {
                    clip.close();
                }
            }
            if (PelinAsetukset.äänetPäällä) {
                AudioInputStream audioInputStream = null;
                String tiedostotyyppi = "";
                String tiedostonNimi = "";
                String tiedostonPolku = "";
            
                File ääniTiedosto = Assets.annaÄäni(ääni);
                tiedostonNimi = ääniTiedosto.getName();
                tiedostonPolku = ääniTiedosto.getPath();
                if (tiedostonNimi.length() > 3) {
                    tiedostotyyppi = tiedostonNimi.substring(tiedostonNimi.length()-3, tiedostonNimi.length());
                }
                switch (tiedostotyyppi) {
                    case "wav" -> {
                        audioInputStream = AudioSystem.getAudioInputStream(ääniTiedosto);
                    }
                    case "mp3" -> {
                        audioInputStream = Dekoodaus.decodeMP3(tiedostonPolku);
                    }
                    case "ogg" -> {
                        audioInputStream = Dekoodaus.decodeOgg(tiedostonPolku);
                    }
                    case null, default -> {
                        System.out.println("Ei-tuettu tiedostotyyppi: " + tiedostonNimi);
                        throw new UnsupportedAudioFileException();
                    }
                }

                if (ääniClipit.get(seuraavaÄäniIndeksi) == null) {
                    Clip clip = AudioSystem.getClip();
                    ääniClipit.put(seuraavaÄäniIndeksi, clip);
                }
                ääniClipit.get(seuraavaÄäniIndeksi).open(audioInputStream);
                
                FloatControl gainControl = (FloatControl)ääniClipit.get(seuraavaÄäniIndeksi).getControl(FloatControl.Type.MASTER_GAIN);
                float gainFloat = (float)(Math.pow(PelinAsetukset.ääniVolyymi * volume, (1f/9f))*80 -80);
                gainControl.setValue(gainFloat);
                try {
                    FloatControl panControl = (FloatControl)ääniClipit.get(seuraavaÄäniIndeksi).getControl(FloatControl.Type.PAN);
                    float panFloat = (float)pan;
                    panControl.setValue(panFloat);
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Panoroinnin säätö ei onnistunut. Tiedosto saattaa olla monoääni.");
                    e.printStackTrace();
                }
                finally {
                    ääniClipit.get(seuraavaÄäniIndeksi).start();
                    seuraavaÄäniIndeksi++;
                    seuraavaÄäniIndeksi %= maxÄäntenMäärä;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Ääntä ei voitu toistaa");
            e.printStackTrace();
        }
    }

    public static void suljeToistetutÄänet() {
        for (Clip clip : ääniClipit.values()) {
            if (!clip.isActive()) {
                clip.close();
            }
        }
    }

    public static void asetaSFXVolyymi(double volyymi) {
        if (ääniClipit.get(seuraavaÄäniIndeksi) != null) {
            FloatControl gainControl = (FloatControl) ääniClipit.get(seuraavaÄäniIndeksi).getControl(FloatControl.Type.MASTER_GAIN);
            float gain = (float)(Math.pow(volyymi, (1f/9f))*80 -80);
            gainControl.setValue(gain);
        }
        PelinAsetukset.ääniVolyymi = volyymi;
    }
}
