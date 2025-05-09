package keimo.kenttäkohteet.kiintopiste;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.util.ArrayList;

public final class Pulloautomaatti extends Kiintopiste {

    private static Tekstuuri idleTekstuuri, aktiivinenTekstuuri, virheTekstuuri;

    public Pulloautomaatti (int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Pulloautomaatti";
        super.tiedostonNimi = "pullonpalautus_idle.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Tänne voi palauttaa tölkit";
        super.asetaTiedot();
    }

    public static void luoTekstuurit() {
        idleTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pullonpalautus_idle.png");
        aktiivinenTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pullonpalautus_active.png");
        virheTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pullonpalautus_virhe.png");
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
