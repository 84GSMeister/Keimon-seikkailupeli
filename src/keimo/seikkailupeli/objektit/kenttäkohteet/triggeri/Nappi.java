package keimo.seikkailupeli.objektit.kenttäkohteet.triggeri;

import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.äänet.Äänet;

public class Nappi extends Triggeri {

    public Nappi(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Nappi";
        super.tiedostonNimi = "nappi.png";
        super.tekstuuri = Assets.annaTekstuuri("nappi");
        super.katsomisTeksti = "Mitähän tästä tapahtuu?";
        super.vaadittuEsine = null;
        super.asetaTiedot();
    }

    @Override
    public void triggeröi() {
        if (!super.onkoTriggeröity()) {
            super.triggeröi();
            super.tekstuuri = Assets.annaTekstuuri("nappi_painettu");
            Äänet.toistaSFX("nappi");
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
            case "essiivi":      return "Nappina";
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
}
