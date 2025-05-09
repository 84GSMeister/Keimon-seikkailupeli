package keimo.keimoEngine.äänet;

import keimo.Pelaaja;
import keimo.PelinAsetukset;
import keimo.keimoEngine.assets.Assets;

import java.awt.Point;
import java.io.File;
import javax.sound.sampled.Clip;

import javafx.scene.media.AudioClip;

public class Äänet {
    public static AudioClip ääniToistin;
    protected static Clip ääniClip;

    public static void asetaSFXVolyymi(double volyymi) {
        if (ääniToistin != null) {
            ääniToistin.setVolume(volyymi);
        }
        PelinAsetukset.ääniVolyymi = volyymi;
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
        try {
            //if (ääni.startsWith("tölkki")) ääni += r.nextInt(tölkkiÄäniLista.size());
            ääniToistin = new AudioClip(Assets.annaÄäni(ääni).toURI().toString());
            ääniToistin.setVolume(volume * PelinAsetukset.ääniVolyymi);
            ääniToistin.setBalance(pan);
            ääniToistin.play();
        }
        catch (NullPointerException npe) {
            System.out.println("Äänitiedostoa \"" + ääni + "\" ei löytynyt");
            npe.printStackTrace();
        }
    }
}
