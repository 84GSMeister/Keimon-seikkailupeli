package keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste;

import java.util.ArrayList;

public final class Pulloautomaatti extends Kiintopiste {

    public Pulloautomaatti (int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Pulloautomaatti";
        super.tiedostonNimi = "pullonpalautus_idle.png";
        super.katsomisTeksti = "Tänne voi palauttaa tölkit";
        super.asetaTiedot();
    }

    public PulloautomaatinKuvake tila = PulloautomaatinKuvake.IDLE;

    public enum PulloautomaatinKuvake {
        IDLE,
        AKTIIVINEN,
        VIRHE
    }
    
    public void valitseTila(PulloautomaatinKuvake paKuvake) {
        this.tila = paKuvake;
    }
}
