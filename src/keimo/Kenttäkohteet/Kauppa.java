package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;
import java.text.DecimalFormat;

import keimo.Pelaaja;
import keimo.TarkistettavatArvot;

public class Kauppa extends Kiintopiste {

    public String kokeileEsinettä(Esine e) {
        float saatavaRaha = 0.15f * Pelaaja.kuparit;
        DecimalFormat df = new DecimalFormat("##.##");
        if (saatavaRaha >= 20) {
            Pelaaja.annaEsine(annaSeteli());
            Pelaaja.kuparit = 0;
            return "Palautit tölkit kauppaan ja sait " + saatavaRaha + "€.";
        }
        else {
            return "Sinulla on " + Pelaaja.kuparit + " tölkkiä. Niistä saa " + df.format(saatavaRaha) + "€. Kerää lisää tölkkejä, jotta saat 20€.";
        }
    }

    public static Esine annaSeteli() {
        return new Seteli(false, 0, 0);
    }

    public String katso() {
        return "Kauppaan voi palauttaa tölkit";
    }
    
    public Kauppa (boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kauppa";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kaupparuutu.png");
        super.asetaTiedot();
    }
}
