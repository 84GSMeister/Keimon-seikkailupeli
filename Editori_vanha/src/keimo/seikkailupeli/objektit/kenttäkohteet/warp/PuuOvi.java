package keimo.seikkailupeli.objektit.kenttäkohteet.warp;

import java.util.ArrayList;

public final class PuuOvi extends Warp {

    public PuuOvi(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        this.nimi = "Puuovi";
        super.tiedostonNimi = "puuovi.png";
        super.asetaTiedot();
    }
    
    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Puuovi";
            case "genetiivi":    return "Puuoven";
            case "essiivi":      return "Puuovena";
            case "partitiivi":   return "Puuovea";
            case "translatiivi": return "Puuoveksi";
            case "inessiivi":    return "Puuovessa";
            case "elatiivi":     return "Puuovesta";
            case "illatiivi":    return "Puuoveen";
            case "adessiivi":    return "Puuovella";
            case "ablatiivi":    return "Puuovelta";
            case "allatiivi":    return "Puuovelle";
            default:             return "Puuovi";
        }
    }
}
