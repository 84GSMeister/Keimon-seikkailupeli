package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

import keimo.Ikkunat.ÄmpäriJonoIkkuna;


public class Ämpärikone extends Kiintopiste {
    
    @Override
    public String kokeileEsinettä(Esine e) {
        return "Mitään ei tapahtunut.";
    }

    @Override
    public void näytäDialogi(Esine e) {
        ÄmpäriJonoIkkuna.luoÄmpäriJonoIkkuna();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Ämpärikone";
            case "genetiivi":
                return "Ämpärikoneen";
            case "esiivi":
                return "Ämpärikoneena";
            case "partitiivi":
                return "Ämpärikonetta";
            case "translatiivi":
                return "Ämpärikoneeksi";
            case "inessiivi":
                return "Ämpärikoneessa";
            case "elatiivi":
                return "Ämpärikoneesta";
            case "illatiivi":
                return "Ämpärikoneeseen";
            case "adessiivi":
                return "Ämpärikoneella";
            case "ablatiivi":
                return "Ämpärikoneelta";
            case "allatiivi":
                return "Ämpärikoneelle";
            default:
                return "Ämpärikone";
        }
    }

    public Ämpärikone(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Ämpärikone";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/ämpärikone.png");
        super.tiedostonNimi = "ämpärikone.png";
        super.katsomisTeksti = "Täältä saa ilmaisia ämpäreitä";
        super.asetaTiedot();
    }
}
