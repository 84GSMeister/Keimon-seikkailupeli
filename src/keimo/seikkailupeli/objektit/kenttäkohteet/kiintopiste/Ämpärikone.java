package keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste;

import keimo.seikkailupeli.assets.Assets;

import java.util.ArrayList;

public final class Ämpärikone extends Kiintopiste {

    public Ämpärikone(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Ämpärikone";
        super.tiedostonNimi = "ämpärikone.png";
        super.tekstuuri = Assets.annaTekstuuri("ämpärikone");
        super.katsomisTeksti = "Täältä saa ilmaisia ämpäreitä";
        super.asetaTiedot();
        super.päivitäLisäOminaisuudet(ominaisuusLista);
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
