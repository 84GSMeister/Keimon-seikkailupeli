package keimo.Kenttäkohteet;

import keimo.Ruudut.Lisäruudut.PullonPalautusRuutu;
import keimo.Utility.KäännettäväKuvake;

import javax.swing.ImageIcon;

public final class Pulloautomaatti extends Kiintopiste {
    
    @Override
    public void näytäDialogi(Esine e) {
        PullonPalautusRuutu.luoPullonPalautusIkkuna();
    }

    @Override
    public String vuorovaikuta(Esine e) {
        PullonPalautusRuutu.luoPullonPalautusIkkuna();
        return katsomisTeksti;
    }

    public PulloautomaatinKuvake tila = PulloautomaatinKuvake.IDLE;

    public enum PulloautomaatinKuvake {
        IDLE,
        AKTIIVINEN,
        VIRHE
    }
    
    public void valitseKuvake(PulloautomaatinKuvake paKuvake) {
        switch (paKuvake) {
            case IDLE:
                super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/pullonpalautus_idle.png");
            break;
            case AKTIIVINEN:
                super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/pullonpalautus_active.png");
            break;
            case VIRHE:
                super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/pullonpalautus_virhe.png");
            break;
        }
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
    }
    
    public Pulloautomaatti (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Pulloautomaatti";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/pullonpalautus_idle.png");
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
        super.tiedostonNimi = "pullonpalautus_idle.png";
        super.katsomisTeksti = "Tänne voi palauttaa tölkit";
        super.erillisDialogi = true;
        super.asetaTiedot();
    }
}
