package keimo.kenttäkohteet.warp;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.util.ArrayList;

public class Oviruutu extends Warp {

    public Oviruutu(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        this.nimi = "Oviruutu";
        super.tiedostonNimi = "reunawarppi.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);        
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Oviruutu";
            case "genetiivi":    return "Oviruudun";
            case "essiivi":      return "Oviruutuna";
            case "partitiivi":   return "Oviruutua";
            case "translatiivi": return "Oviruuduksi";
            case "inessiivi":    return "Oviruudussa";
            case "elatiivi":     return "Oviruudusta";
            case "illatiivi":    return "Oviruutuun";
            case "adessiivi":    return "Oviruudulla";
            case "ablatiivi":    return "Oviruudulta";
            case "allatiivi":    return "Oviruudulle";
            default:             return "Oviruutu";
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
