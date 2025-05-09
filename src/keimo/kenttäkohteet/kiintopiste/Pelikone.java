package keimo.kenttäkohteet.kiintopiste;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.util.ArrayList;

public final class Pelikone extends Kiintopiste {

    public Pelikone(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Pelikone";
        super.tiedostonNimi = "pelikone.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Mitenkäs tätä pelataan?";
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Tässä todistamme jälleen kauniin kielemme kukkasia.
        switch (sijamuoto) {
            case "nominatiivi":  return "Pelikone";
            case "genetiivi":    return "Pelikoneen";
            case "essiivi":      return "Pelikoneena";
            case "partitiivi":   return "Pelikonetta";
            case "translatiivi": return "Pelikoneeksi";
            case "inessiivi":    return "Pelikoneessa";
            case "elatiivi":     return "Pelikoneesta";
            case "illatiivi":    return "Pelikoneeseen";
            case "adessiivi":    return "Pelikoneella";
            case "ablatiivi":    return "Pelikoneelta";
            case "allatiivi":    return "Pelikoneelle";
            default:             return "Pelikone";
        }
    }
}
