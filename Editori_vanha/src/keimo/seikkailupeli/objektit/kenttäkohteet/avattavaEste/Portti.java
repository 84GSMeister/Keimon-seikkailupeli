package keimo.seikkailupeli.objektit.kenttäkohteet.avattavaEste;

import java.util.ArrayList;

public final class Portti extends AvattavaEste {

    private boolean päivitäKuvake = true;

    public Portti(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Portti";
        super.tiedostonNimi = "portti.png";
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
                päivitäKuvake = false;
            }
        }
        else {
            päivitäKuvake = true;
        }
    }
}
