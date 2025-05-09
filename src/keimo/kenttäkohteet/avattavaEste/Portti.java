package keimo.kenttäkohteet.avattavaEste;

import keimo.keimoEngine.grafiikat.Animaatio;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.äänet.Äänet;

import java.util.ArrayList;

public class Portti extends AvattavaEste {

    private boolean päivitäKuvake = true;

    public Portti(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Portti";
        super.tiedostonNimi = "portti.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "portti";
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Portti";
            case "genetiivi":    return "Portin";
            case "essiivi":      return "Porttina";
            case "partitiivi":   return "Porttia";
            case "translatiivi": return "Portiksi";
            case "inessiivi":    return "Portissa";
            case "elatiivi":     return "Portista";
            case "illatiivi":    return "Porttiin";
            case "adessiivi":    return "Portilla";
            case "ablatiivi":    return "Portilta";
            case "allatiivi":    return "Portille";
            default:             return "Portti";
        }
    }

    @Override
    protected void avaa(boolean avaus) {
        super.avaa(avaus);
        if (avaus) {
            if (päivitäKuvake) {
                super.tekstuuri = new Animaatio(30, "tiedostot/kuvat/kenttäkohteet/portti_auki.gif", 1);
                Äänet.toistaSFX("portti", this.annaSijaintiKentällä());
                päivitäKuvake = false;
            }
        }
        else {
            super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/portti.png");
            päivitäKuvake = true;
        }
    }
}
