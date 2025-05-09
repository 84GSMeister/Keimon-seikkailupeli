package keimo.kenttäkohteet.kiintopiste;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.util.ArrayList;

public final class Ämpärikone extends Kiintopiste {

    public Ämpärikone(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Ämpärikone";
        super.tiedostonNimi = "ämpärikone.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Täältä saa ilmaisia ämpäreitä";
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Tässä todistamme jälleen kauniin kielemme kukkasia.
        switch (sijamuoto) {
            case "nominatiivi":  return "Ämpärikone";
            case "genetiivi":    return "Ämpärikoneen";
            case "essiivi":      return "Ämpärikoneena";
            case "partitiivi":   return "Ämpärikonetta";
            case "translatiivi": return "Ämpärikoneeksi";
            case "inessiivi":    return "Ämpärikoneessa";
            case "elatiivi":     return "Ämpärikoneesta";
            case "illatiivi":    return "Ämpärikoneeseen";
            case "adessiivi":    return "Ämpärikoneella";
            case "ablatiivi":    return "Ämpärikoneelta";
            case "allatiivi":    return "Ämpärikoneelle";
            default:             return "Ämpärikone";
        }
    }
}
