package keimo.kenttäkohteet.kiintopiste;

import keimo.Pelaaja;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;

public final class Sänky extends Lepopaikka {

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
            case "esiivi":       return "Sänkynä";
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
    
    public Sänky (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Sänky";
        super.tiedostonNimi = "sänky.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Nukuttaako?";
        super.asetaTiedot();
    }
}
