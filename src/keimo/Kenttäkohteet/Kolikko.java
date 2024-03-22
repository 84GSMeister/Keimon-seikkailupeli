package keimo.Kenttäkohteet;

import javax.swing.ImageIcon;

import keimo.Pelaaja;
import keimo.Säikeet.ÄänentoistamisSäie;

public final class Kolikko extends Kerättävä {
    
    public void kerää() {
        Pelaaja.raha += 1;
        ÄänentoistamisSäie.toistaSFX(super.nimi);
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Kolikko";
            case "genetiivi": return "Kolikon";
            case "esiivi": return "Kolikkona";
            case "partitiivi": return "Kolikkoa";
            case "translatiivi": return "Kolikoksi";
            case "inessiivi": return "Kolikossa";
            case "elatiivi": return "Kolikosta";
            case "illatiivi": return "Kolikkoon";
            case "adessiivi": return "Kolikolla";
            case "ablatiivi": return "Kolikolta";
            case "allatiivi": return "Kolikolle";
            default: return "Kolikko";
        }
    }

    public Kolikko(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kolikko";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kolikko.png");
        super.asetaTiedot();
    }
}
