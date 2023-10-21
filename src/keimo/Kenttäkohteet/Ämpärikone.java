package keimo.Kenttäkohteet;

import keimo.Ruudut.Lisäruudut.ÄmpäriJonoRuutu;
import keimo.Utility.KäännettäväKuvake;

import javax.swing.ImageIcon;

public final class Ämpärikone extends Kiintopiste {
    
    // @Override
    // public String kokeileEsinettä(Esine e) {
    //     return "Mitään ei tapahtunut.";
    // }

    // @Override
    // public void näytäDialogi(Esine e) {
    //     ÄmpäriJonoRuutu.luoÄmpäriJonoIkkuna();
    // }

    @Override
    public String vuorovaikuta(Esine e) {
        ÄmpäriJonoRuutu.luoÄmpäriJonoIkkuna();
        return katsomisTeksti;
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Tässä todistamme jälleen kauniin kielemme kukkasia.
        switch (sijamuoto) {
            case "nominatiivi": return "Ämpärikone";
            case "genetiivi": return "Ämpärikoneen";
            case "esiivi": return "Ämpärikoneena";
            case "partitiivi": return "Ämpärikonetta";
            case "translatiivi": return "Ämpärikoneeksi";
            case "inessiivi": return "Ämpärikoneessa";
            case "elatiivi": return "Ämpärikoneesta";
            case "illatiivi": return "Ämpärikoneeseen";
            case "adessiivi": return "Ämpärikoneella";
            case "ablatiivi": return "Ämpärikoneelta";
            case "allatiivi": return "Ämpärikoneelle";
            default: return "Ämpärikone";
        }
    }

    public Ämpärikone(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Ämpärikone";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/ämpärikone.png");
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
        super.tiedostonNimi = "ämpärikone.png";
        super.katsomisTeksti = "Täältä saa ilmaisia ämpäreitä";
        super.erillisDialogi = true;
        super.ignooraaEsineValintaDialogissa = true;
        super.ohitaDialogiTesktiboksi = true;
        super.asetaTiedot();
    }
}
