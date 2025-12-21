package keimo.seikkailupeli.objektit.kenttäkohteet.warp;

import java.util.ArrayList;

public class Kauppaovi extends Warp {

    public Kauppaovi(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        this.nimi = "Kauppaovi";
        super.tiedostonNimi = "kauppaovi.png";
        super.asetaTiedot();
    }
    
    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kauppaovi";
            case "genetiivi":    return "Kauppaoven";
            case "essiivi":      return "Kauppaovena";
            case "partitiivi":   return "Kauppaovea";
            case "translatiivi": return "Kauppaoveksi";
            case "inessiivi":    return "Kauppaovessa";
            case "elatiivi":     return "Kauppaovesta";
            case "illatiivi":    return "Kauppaoveen";
            case "adessiivi":    return "Kauppaovella";
            case "ablatiivi":    return "Kauppaovelta";
            case "allatiivi":    return "Kauppaovelle";
            default:             return "Kauppaovi";
        }
    }

    @Override
    public void asetaSuunta(Suunta suunta) {
        super.asetaSuunta(suunta);
        switch (suunta) {
            case YLÖS:
                this.suunta = Suunta.YLÖS;
                break;
            case ALAS:
                this.suunta = Suunta.ALAS;
                break;
            case VASEN:
                this.suunta = Suunta.VASEN;
                break;
            case OIKEA:
                this.suunta = Suunta.OIKEA;
                break;
            default:
                this.suunta = Suunta.YLÖS;
                break;
        }
    }
}
