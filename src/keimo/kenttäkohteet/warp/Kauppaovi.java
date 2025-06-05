package keimo.kenttäkohteet.warp;

import keimo.Pelaaja;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.esine.Esine;

import java.util.ArrayList;

public class Kauppaovi extends Warp {

    public Kauppaovi(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        this.nimi = "Kauppaovi";
        super.tiedostonNimi = "kauppaovi.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
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

    @Override
    public void warpinJälkeen() {
        String saatujenTuotteidenNimet = "";
        String pudotettujenTuotteidenNimet = "";
        int tyhjätPaikat = 0;
        for (Esine esine : Pelaaja.esineet) {
            if (esine == null) {
                tyhjätPaikat++;
            }
        }
        for (int i = 0; i < tyhjätPaikat; i++) {
            if (Pelaaja.ostosKori.size() > i) {
                Pelaaja.annaEsine((Esine)KenttäKohde.luoObjektiTiedoilla(Pelaaja.ostosKori.get(i).annaNimi(), 0, 0, null));
                saatujenTuotteidenNimet += Pelaaja.ostosKori.get(i).annaNimi() + ", ";
            }
            else {
                break;
            }
        }
        for (int i = tyhjätPaikat; i < Pelaaja.ostosKori.size(); i++) {
            pudotettujenTuotteidenNimet += Pelaaja.ostosKori.get(i).annaNimi() + ", ";
        }
        if (saatujenTuotteidenNimet.length() > 0 && pudotettujenTuotteidenNimet.length() > 0) {
            saatujenTuotteidenNimet = saatujenTuotteidenNimet.substring(0, saatujenTuotteidenNimet.length()-2);
            pudotettujenTuotteidenNimet = pudotettujenTuotteidenNimet.substring(0, pudotettujenTuotteidenNimet.length()-2);
            Dialogit.avaaDialogi("", "Juoksit onnistuneesti kaupasta: " + saatujenTuotteidenNimet +  ",\n\n mutta sinulta putosi: " + pudotettujenTuotteidenNimet, "Juoksukalja (reppu täynnä!)");
        }
        else if (saatujenTuotteidenNimet.length() > 0) {
            saatujenTuotteidenNimet = saatujenTuotteidenNimet.substring(0, saatujenTuotteidenNimet.length()-2);
            Dialogit.avaaDialogi("", "Juoksit onnistuneesti kaupasta: " + saatujenTuotteidenNimet, "Juoksukalja");
        }
        else if (pudotettujenTuotteidenNimet.length() > 0) {
            if (pudotettujenTuotteidenNimet.split(",").length > 2) {
                pudotettujenTuotteidenNimet = pudotettujenTuotteidenNimet.substring(0, pudotettujenTuotteidenNimet.length()-2);
                Dialogit.avaaDialogi("", "Yritit varastaa kaupasta: " + pudotettujenTuotteidenNimet + ",\n\nmutta ne putosivat, sillä reppusi on täynnä.", "Juoksukalja (reppu täynnä!)");
            }
            else {
                pudotettujenTuotteidenNimet = pudotettujenTuotteidenNimet.substring(0, pudotettujenTuotteidenNimet.length()-2);
                Dialogit.avaaDialogi("", "Yritit varastaa kaupasta: " + pudotettujenTuotteidenNimet + ",\n\nmutta se putosi, sillä reppusi on täynnä.", "Juoksukalja (reppu täynnä!)");
            }
        }
        Pelaaja.tyhjennäOstoskori();
    }
}
