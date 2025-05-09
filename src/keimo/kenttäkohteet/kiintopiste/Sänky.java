package keimo.kenttäkohteet.kiintopiste;

import keimo.Pelaaja;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;

import java.util.ArrayList;

public final class Sänky extends Lepopaikka {

    public Sänky (int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Sänky";
        super.tiedostonNimi = "sänky.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Nukuttaako?";
        super.asetaTiedot();
    }

    @Override
    public String vuorovaikuta(Esine e) {
        super.hpVähennys = Math.round(Pelaaja.känninVoimakkuusFloat*1.4)-2;
        return super.vuorovaikuta(e);
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Sänky";
            case "genetiivi":    return "Sängyn";
            case "essiivi":      return "Sänkynä";
            case "partitiivi":   return "Sänkyä";
            case "translatiivi": return "Sängyksi";
            case "inessiivi":    return "Sängyssä";
            case "elatiivi":     return "Sängystä";
            case "illatiivi":    return "Sänkyyn";
            case "adessiivi":    return "Sängyllä";
            case "ablatiivi":    return "Sängyltä";
            case "allatiivi":    return "Sängylle";
            default:             return "Sänky";
        }
    }
}
