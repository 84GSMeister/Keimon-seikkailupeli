package keimo.kenttäkohteet.triggeri;

import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import javax.swing.ImageIcon;

public class Nappi extends Triggeri {

    @Override
    public void triggeröi() {
        if (!super.onkoTriggeröity()) {
            super.triggeröi();
            super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/nappi_painettu.png");
            super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/nappi_painettu.png");
            ÄänentoistamisSäie.toistaSFX("nappi");
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        // Nappi n = new Nappi(false, 0, 0);
        // System.out.println("Ei mennyt ihan " + n.annaNimiSijamuodossa("illatiivi") + ".");
        // ^ Ei mennyt ihan Nappiin.
        switch (sijamuoto) {
            case "nominatiivi":  return "Nappi";
            case "genetiivi":    return "Napin";
            case "esiivi":       return "Nappina";
            case "partitiivi":   return "Nappia";
            case "translatiivi": return "Napiksi";
            case "inessiivi":    return "Napissa";
            case "elatiivi":     return "Napista";
            case "illatiivi":    return "Nappiin";
            case "adessiivi":    return "Napilla";
            case "ablatiivi":    return "Napilta";
            case "allatiivi":    return "Napille";
            default:             return "Makkara";
        }
    }
    
    public Nappi(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Nappi";
        super.tiedostonNimi = "nappi.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Mitähän tästä tapahtuu?";
        super.vaadittuEsine = null;
        super.asetaTiedot();
    }
}
