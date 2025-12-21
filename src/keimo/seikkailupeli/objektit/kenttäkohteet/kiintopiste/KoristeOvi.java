package keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste;

import keimo.keimoengine.grafiikat.Tekstuuri;

import java.util.ArrayList;

public class KoristeOvi extends Kiintopiste {
    
    public KoristeOvi (int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Koristeovi";
        super.tiedostonNimi = "koristeovi.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Tästä ei pääse";
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Koristeovi";
            case "genetiivi":    return "Koristeoven";
            case "essiivi":      return "Koristeovena";
            case "partitiivi":   return "Koristeovea";
            case "translatiivi": return "Koristeoveksi";
            case "inessiivi":    return "Koristeovessa";
            case "elatiivi":     return "Koristeovesta";
            case "illatiivi":    return "Koristeoveen";
            case "adessiivi":    return "Koristeovella";
            case "ablatiivi":    return "Koristeovelta";
            case "allatiivi":    return "Koristeovelle";
            default:             return "Koristeovi";
        }
    }
}
