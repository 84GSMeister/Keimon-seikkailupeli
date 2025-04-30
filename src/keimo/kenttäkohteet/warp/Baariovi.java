package keimo.kenttäkohteet.warp;

import keimo.Pelaaja;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.kenttäkohteet.esine.Olutlasi;

public class Baariovi extends Warp {

    Olutlasi olutlasi = new Olutlasi(true, 0, 0);

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Baariovi";
            case "genetiivi":    return "Baarioven";
            case "esiivi":       return "Baariovena";
            case "partitiivi":   return "Baariovea";
            case "translatiivi": return "Baarioveksi";
            case "inessiivi":    return "Baariovessa";
            case "elatiivi":     return "Baariovesta";
            case "illatiivi":    return "Baarioveen";
            case "adessiivi":    return "Baariovella";
            case "ablatiivi":    return "Baariovelta";
            case "allatiivi":    return "Baariovelle";
            default:             return "Baariovi";
        }
    }

    @Override
    public void warpinJälkeen() {
        boolean olutTakavarikoitiin = false;
        for (int i = 0; i < Pelaaja.esineet.length; i++) {
            if (Pelaaja.esineet[i] instanceof Olutlasi) {
                Pelaaja.esineet[i] = null;
                olutTakavarikoitiin = true;
            }
        }
        if (olutTakavarikoitiin) Dialogit.avaaDialogi(olutlasi.annaDialogiTekstuuri(), "Yritit viedä olutlasin anniskelualueen ulkopuolelle, joten se takavarikoitiin.", "Huomautus: anniskelualue");
    }
    
    public Baariovi(int sijX, int sijY, String[] ominaisuusLista) {
        super(true, sijX, sijY);
        this.nimi = "Baariovi";

        this.suunta = Suunta.VASEN;

        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kohdehuone=")) {
                    this.kohdeHuone = Integer.parseInt("" + ominaisuus.substring(ominaisuus.indexOf("=") +1));
                }
                else if (ominaisuus.startsWith("kohderuutuX=")) {
                    this.kohdeRuutuX = Integer.parseInt("" + ominaisuus.substring(ominaisuus.indexOf("=") +1));
                }
                else if (ominaisuus.startsWith("kohderuutuY=")) {
                    this.kohdeRuutuY = Integer.parseInt("" + ominaisuus.substring(ominaisuus.indexOf("=") +1));
                }
                else if (ominaisuus.startsWith("suunta=")) {
                    String suuntaString = ominaisuus.substring(7);
                    switch (suuntaString) {
                        case "vasen", "VASEN", "Vasen": this.suunta = Suunta.VASEN; break;
                        case "oikea", "OIKEA", "Oikea": this.suunta = Suunta.OIKEA; break;
                        case "ylös", "YLÖS", "Ylös": this.suunta = Suunta.YLÖS; break;
                        case "alas", "ALAS", "Alas": this.suunta = Suunta.ALAS; break;
                        default: this.suunta = Suunta.YLÖS; break;
                    }
                }
            }
        }

        super.tiedostonNimi = "baariovi.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        asetaSuunta(suunta);
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kohdehuone=" + kohdeHuone;
        this.lisäOminaisuudet[1] = "kohderuutuX=" + kohdeRuutuX;
        this.lisäOminaisuudet[2] = "kohderuutuY=" + kohdeRuutuY;
        this.lisäOminaisuudet[3] = "suunta=" + annaSuunta();
        super.asetaTiedot();
    }
}
