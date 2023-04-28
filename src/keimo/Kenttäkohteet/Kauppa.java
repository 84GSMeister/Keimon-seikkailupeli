package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

import keimo.Pelaaja;

public class Kauppa extends Kiintopiste {

    public String kokeileEsinettä(Esine e) {
        float saatavaRaha = 0.15f * Pelaaja.kuparit;
        if (saatavaRaha > 0) {
            Pelaaja.raha += saatavaRaha;
            Pelaaja.kuparit = 0;
            return "Palautit tölkit kauppaan ja sait " + saatavaRaha + "€.";
        }
        else {
            return "Sinulla ei ole yhtään tölkkiä.";
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
