package keimo.seikkailupeli.objektit.kenttäkohteet.warp;

import java.util.ArrayList;

public class Salaovi extends Warp {

    public Salaovi(int x, int y, ArrayList<String> ominaisuusLista) {
        super(x, y, ominaisuusLista);
        this.nimi = "Salaovi";
        super.tiedostonNimi = "salaovi.png";
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
