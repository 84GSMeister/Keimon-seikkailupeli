package keimo.kenttäkohteet.warp;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PääIkkuna;
import keimo.Utility.KäännettäväKuvake;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.esine.Esine;

import javax.swing.ImageIcon;

public class Kauppaovi extends Warp {
    
    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kauppaovi";
            case "genetiivi":    return "Kauppaoven";
            case "esiivi":       return "Kauppaovena";
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
        this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppaovi.png");
        switch (suunta) {
            case YLÖS:
                this.suunta = Suunta.YLÖS;
                this.kuvake = new KäännettäväKuvake(kuvake, 0);
                break;
            case ALAS:
                this.suunta = Suunta.ALAS;
                this.kuvake = new KäännettäväKuvake(kuvake, 180);
                break;
            case VASEN:
                this.suunta = Suunta.VASEN;
                this.kuvake = new KäännettäväKuvake(kuvake, 270);
                break;
            case OIKEA:
                this.suunta = Suunta.OIKEA;
                this.kuvake = new KäännettäväKuvake(kuvake, 90);
                break;
            default:
                this.suunta = Suunta.YLÖS;
                this.kuvake = new KäännettäväKuvake(kuvake, 0);
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
                //Pelaaja.annaEsine(Pelaaja.ostosKori.get(i));
                Pelaaja.annaEsine((Esine)KenttäKohde.luoObjektiTiedoilla(Pelaaja.ostosKori.get(i).annaNimi(), true, 0, 0, null));
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
            if (Peli.legacy) PääIkkuna.avaaDialogi(null, "<html><p>Juoksit onnistuneesti kaupasta: " + saatujenTuotteidenNimet +  ",<br><br> mutta sinulta putosi: " + pudotettujenTuotteidenNimet + "</p></html>", "Juoksukalja (reppu täynnä!)", true, null);
            else Dialogit.avaaDialogi("", "<html><p>Juoksit onnistuneesti kaupasta: " + saatujenTuotteidenNimet +  ",<br><br> mutta sinulta putosi: " + pudotettujenTuotteidenNimet + "</p></html>", "Juoksukalja (reppu täynnä!)");
        }
        else if (saatujenTuotteidenNimet.length() > 0) {
            saatujenTuotteidenNimet = saatujenTuotteidenNimet.substring(0, saatujenTuotteidenNimet.length()-2);
            if (Peli.legacy) PääIkkuna.avaaDialogi(null, "<html><p>Juoksit onnistuneesti kaupasta: " + saatujenTuotteidenNimet + "</p></html>", "Juoksukalja", true, null);
            else Dialogit.avaaDialogi("", "<html><p>Juoksit onnistuneesti kaupasta: " + saatujenTuotteidenNimet + "</p></html>", "Juoksukalja");
        }
        else if (pudotettujenTuotteidenNimet.length() > 0) {
            if (pudotettujenTuotteidenNimet.split(",").length > 2) {
                pudotettujenTuotteidenNimet = pudotettujenTuotteidenNimet.substring(0, pudotettujenTuotteidenNimet.length()-2);
                if (Peli.legacy) PääIkkuna.avaaDialogi(null, "<html><p>Yritit varastaa kaupasta: " + pudotettujenTuotteidenNimet + ",<br><br>mutta ne putosivat, sillä reppusi on täynnä.</p></html>", "Juoksukalja (reppu täynnä!)", true, null);
                else Dialogit.avaaDialogi("", "<html><p>Yritit varastaa kaupasta: " + pudotettujenTuotteidenNimet + ",<br><br>mutta ne putosivat, sillä reppusi on täynnä.</p></html>", "Juoksukalja (reppu täynnä!)");
            }
            else {
                pudotettujenTuotteidenNimet = pudotettujenTuotteidenNimet.substring(0, pudotettujenTuotteidenNimet.length()-2);
                if (Peli.legacy) PääIkkuna.avaaDialogi(null, "<html><p>Yritit varastaa kaupasta: " + pudotettujenTuotteidenNimet + ",<br><br>mutta se putosi, sillä reppusi on täynnä.</p></html>", "Juoksukalja (reppu täynnä!)", true, null);
                else Dialogit.avaaDialogi("", "<html><p>Yritit varastaa kaupasta: " + pudotettujenTuotteidenNimet + ",<br><br>mutta se putosi, sillä reppusi on täynnä.</p></html>", "Juoksukalja (reppu täynnä!)");
            }
        }
        Pelaaja.tyhjennäOstoskori();
    }

    public Kauppaovi(int sijX, int sijY, String[] ominaisuusLista) {
        super(true, sijX, sijY);
        this.nimi = "Kauppaovi";

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

        super.tiedostonNimi = "kauppaovi.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
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
