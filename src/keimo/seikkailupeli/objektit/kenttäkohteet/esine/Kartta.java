package keimo.seikkailupeli.objektit.kenttäkohteet.esine;

import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.gui.toimintoIkkunat.KarttaIkkuna;
import keimo.seikkailupeli.äänet.Äänet;

public class Kartta extends Esine {

    public Kartta(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Kartta";
        super.tiedostonNimi = "kartta.png";
        super.tekstuuri = Assets.annaTekstuuri("kartta");
        super.katsomisTeksti = "Parempi vilkaista karttaa, jos on eksynyt.";
        super.käyttö = true;
        super.asetaTiedot();
    }

    @Override
    public String käytä() {
        KarttaIkkuna.avaaToimintoIkkuna();
        Äänet.toistaSFX("Kartta");
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
}
