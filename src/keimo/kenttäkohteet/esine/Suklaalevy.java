package keimo.kenttäkohteet.esine;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public final class Suklaalevy extends Ruoka {

    public Suklaalevy(int sijX, int sijY){
        super(sijX, sijY);
        super.nimi = "Suklaalevy";
        super.tiedostonNimi = "suklaalevy.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Voisin syödä tämän.";
        super.heal = 2;
        super.hinta = 2.49;
        super.käyttö = true;
        super.asetaTiedot();
    }

    @Override
    public String käytä() {
        super.käytä();
        this.poista = true;
        return "Se maistui hyvältä. Sait " + this.heal + " elämäpistettä.";
    }
    
    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Suklaalevy";
            case "genetiivi":    return "Suklaalevyn";
            case "esiivi":       return "Suklaalevynä";
            case "partitiivi":   return "Suklaalevyä";
            case "translatiivi": return "Suklaalevyksi";
            case "inessiivi":    return "Suklaalevyssä";
            case "elatiivi":     return "Suklaalevystä";
            case "illatiivi":    return "Suklaalevyyn";
            case "adessiivi":    return "Suklaalevyllä";
            case "ablatiivi":    return "Suklaalevyltä";
            case "allatiivi":    return "Suklaalevylle";
            default:             return "Suklaalevy";
        }
    }
}
