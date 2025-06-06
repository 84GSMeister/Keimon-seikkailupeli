package keimo.kenttäkohteet.esine;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public final class Huume extends Ruoka {

    public Huume(int sijX, int sijY){
        super(sijX, sijY);
        super.nimi = "Huume";
        super.tiedostonNimi = "huume.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.käyttö = true;
        super.heal = 30;
        super.katsomisTeksti = "Tää menee ykkösel alas";
        super.asetaTiedot();
    }

    @Override
    public String käytä() {
        super.käytä();
        super.poista = true;
        return "Never stop the madness! (sait 30 elämäpistettä)";
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Huume";
            case "genetiivi":    return "Huumeen";
            case "esiivi":       return "Huumeena";
            case "partitiivi":   return "Huumetta";
            case "translatiivi": return "Huumeeksi";
            case "inessiivi":    return "Huumeessa";
            case "elatiivi":     return "Huumeesta";
            case "illatiivi":    return "Huumeeseen";
            case "adessiivi":    return "Huumeella";
            case "ablatiivi":    return "Huumeelta";
            case "allatiivi":    return "Huumeelle";
            default:             return "Huume";
        }
    }
}
