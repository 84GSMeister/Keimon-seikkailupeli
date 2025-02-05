package keimo.kenttäkohteet.kenttäNPC;

import keimo.Pelaaja;
import keimo.PääIkkuna;
import keimo.TarkistettavatArvot;
import keimo.Pelaaja.KeimonState;
import keimo.keimoEngine.grafiikat.Animaatio;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Huume;
import keimo.kenttäkohteet.esine.Pesäpallomaila;
import keimo.kenttäkohteet.esine.Esine;

import javax.swing.ImageIcon;

public final class Juhani extends NPC_KenttäKohde {

    @Override
    public String kokeileEsinettä(Esine e) {
        if (e instanceof Pesäpallomaila) {
            kuolemaJuhani();
            return "...";
        }
        else if (Pelaaja.raha >= 20) {
            return annaHuume();
        }
        else {
            return katsomisTeksti;
        }
    }

    @Override
    public void näytäDialogi(Esine e) {
        if (super.onkoCustomDialogi()) {
            super.näytäDialogi(e);
        }
        else {
            PääIkkuna.avaaDialogi(this.annaDialogiKuvake(), this.kokeileEsinettä(e), this.annaNimi());
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Tätä ei tehdä siksi, että kaikkia näitä tarvitsisi pelissä. Tätä tehdään taiteen itsensä vuoksi.
        switch (sijamuoto) {
            case "nominatiivi":  return "Juhani";
            case "genetiivi":    return "Juhanin";
            case "esiivi":       return "Juhanina";
            case "partitiivi":   return "Juhania";
            case "translatiivi": return "Juhaniksi";
            case "inessiivi":    return "Juhanissa";
            case "elatiivi":     return "Juhanista";
            case "illatiivi":    return "Juhaniin";
            case "adessiivi":    return "Juhanilla";
            case "ablatiivi":    return "Juhanilta";
            case "allatiivi":    return "Juhanille";
            default:             return "Juhani";
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "huume": return "-Njuu. Kyllä tää fiitti nyt siltä näyttää, että sinä annat minulle yhden massin, ja minä annan sinulle yhden huumeen.";
            case "invatäynnä": return "(Tavaraluettelo on täynnä!)";
            case null, default: return katso();
        }
    }

    public String annaHuume() {
        if (Pelaaja.annaEsineidenMäärä() < Pelaaja.annaTavaraluettelonKoko()) {
            Pelaaja.annaEsine(new Huume(true, 0, 0));
            Pelaaja.raha -= 20;
            return haeDialogiTeksti("huume");
        }
        else {
            return haeDialogiTeksti("invatäynnä");
        }
    }

    public void kuolemaJuhani() {
        TarkistettavatArvot.pelinLoppuSyy = TarkistettavatArvot.PelinLopetukset.KUOLEMA_JUHANI;
        Pelaaja.hp = 0;
        Pelaaja.keimonState = KeimonState.KUOLLUT;
    }
    
    public Juhani(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Juhani";
        super.tiedostonNimi = "juhani.gif";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Animaatio(15, "tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/juhani_dialogi.png");
        super.dialogiTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/juhani_dialogi.png");
        super.katsomisTeksti = "Osta Juhanilta kahel kybäl yksi huume pois.";
        super.asetaTiedot();
    }
}
