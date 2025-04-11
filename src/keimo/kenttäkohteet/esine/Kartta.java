package keimo.kenttäkohteet.esine;

import keimo.Peli;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.gui.toimintoIkkunat.KarttaIkkuna;

import javax.swing.ImageIcon;

public class Kartta extends Esine {

    @Override
    public String käytä() {
        if (!Peli.legacy) KarttaIkkuna.avaaToimintoIkkuna();
        ÄänentoistamisSäie.toistaSFX("Kartta");
        return katso();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kartta";
            case "genetiivi":    return "Kartan";
            case "esiivi":       return "Karttana";
            case "partitiivi":   return "Karttaa";
            case "translatiivi": return "Kartaksi";
            case "inessiivi":    return "Kartassa";
            case "elatiivi":     return "Kartasta";
            case "illatiivi":    return "Karttaan";
            case "adessiivi":    return "Kartalla";
            case "ablatiivi":    return "Kartalta";
            case "allatiivi":    return "Kartalle";
            default:             return "Kartta";
        }
    }

    public Kartta(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kartta";
        super.tiedostonNimi = "kartta.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Parempi vilkaista karttaa, jos on eksynyt.";
        super.käyttö = true;
        super.asetaTiedot();
    }
}
