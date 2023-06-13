package keimo.Kenttäkohteet;

import keimo.Ikkunat.PullonPalautusIkkuna;

import javax.swing.ImageIcon;

public class Pulloautomaatti extends Kiintopiste {
    
    @Override
    public void näytäDialogi(Esine e) {
        PullonPalautusIkkuna.luoPullonPalautusIkkuna();
    }

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
    }
    
    public Pulloautomaatti (boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Pulloautomaatti";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/pullonpalautus_idle.png");
        super.katsomisTeksti = "Tänne voi palauttaa tölkit";
        super.asetaTiedot();
    }
}
