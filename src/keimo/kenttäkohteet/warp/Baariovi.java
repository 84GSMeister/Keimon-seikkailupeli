package keimo.kenttäkohteet.warp;

import keimo.Pelaaja;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.kenttäkohteet.esine.Olutlasi;

import java.util.ArrayList;

public class Baariovi extends Warp {

    Olutlasi olutlasi = new Olutlasi(0, 0);

    public Baariovi(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        this.nimi = "Baariovi";
        super.tiedostonNimi = "baariovi.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Baariovi";
            case "genetiivi":    return "Baarioven";
            case "essiivi":      return "Baariovena";
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
}
