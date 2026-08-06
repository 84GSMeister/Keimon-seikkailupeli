package keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.seikkailupeli.assets.Assets;

import java.util.ArrayList;

public final class Pulloautomaatti extends Kiintopiste {

    private Renderöitävä idleTekstuuri = Assets.annaTekstuuri("pullonpalautus_idle");
    private Renderöitävä aktiivinenTekstuuri = Assets.annaTekstuuri("pullonpalautus_idle");
    private Renderöitävä virheTekstuuri = Assets.annaTekstuuri("pullonpalautus_idle");

    public Pulloautomaatti(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Pulloautomaatti";
        super.tiedostonNimi = "pullonpalautus_idle.png";
        super.tekstuuri = Assets.annaTekstuuri("pullonpalautus_idle");
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
        switch (paKuvake) {
            case IDLE:
                super.tekstuuri = idleTekstuuri;
            break;
            case AKTIIVINEN:
                super.tekstuuri = aktiivinenTekstuuri;
            break;
            case VIRHE:
                super.tekstuuri = virheTekstuuri;
            break;
        }
    }
}
