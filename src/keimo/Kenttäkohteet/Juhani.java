package keimo.Kenttäkohteet;

import keimo.Pelaaja;
import keimo.PääIkkuna;
import keimo.TarkistettavatArvot;
import keimo.Pelaaja.KeimonState;

import javax.swing.ImageIcon;

public final class Juhani extends NPC_KenttäKohde {

    @Override
    public String kokeileEsinettä(Esine e) {
        if (e instanceof Pesäpallomaila) {
            TarkistettavatArvot.pelinLoppuSyy = TarkistettavatArvot.PelinLopetukset.KUOLEMA_JUHANI;
            Pelaaja.hp = 0;
            Pelaaja.keimonState = KeimonState.KUOLLUT;
            return "...";
        }
        else if (Pelaaja.raha >= 20) {
            if (Pelaaja.annaEsineidenMäärä() < Pelaaja.annaTavaraluettelonKoko()) {
                Pelaaja.annaEsine(new Huume(true, 0, 0));
                Pelaaja.raha -= 20;
                return "<html><p>-Njuu. Kyllä tää fiitti nyt siltä näyttää, että sinä annat minulle yhden massin, ja minä annan sinulle yhden huumeen.</p></html>";
            }
            else {
                return "<html><p>(Tavaraluettelo on täynnä!)</p></html>";
            }
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
            case "nominatiivi": return "Juhani";
            case "genetiivi": return "Juhanin";
            case "esiivi": return "Juhanina";
            case "partitiivi": return "Juhania";
            case "translatiivi": return "Juhaniksi";
            case "inessiivi": return "Juhanissa";
            case "elatiivi": return "Juhanista";
            case "illatiivi": return "Juhaniin";
            case "adessiivi": return "Juhanilla";
            case "ablatiivi": return "Juhanilta";
            case "allatiivi": return "Juhanille";
            default: return "Juhani";
        }
    }
    
    public Juhani(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Juhani";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/juhani.gif");
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/juhani_dialogi.png");
        super.tiedostonNimi = "juhani.gif";
        super.katsomisTeksti = "Osta Juhanilta kahel kybäl yksi huume pois.";
        super.asetaTiedot();
    }
}
