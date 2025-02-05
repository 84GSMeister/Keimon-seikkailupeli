package keimo.kenttäkohteet.kiintopiste;

import keimo.Ruudut.Lisäruudut.PullonPalautusRuutu;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;

import javax.swing.ImageIcon;

public final class Pulloautomaatti extends Kiintopiste {

    private static Tekstuuri idleTekstuuri, aktiivinenTekstuuri, virheTekstuuri;

    public static void luoTekstuurit() {
        idleTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pullonpalautus_idle.png");
        aktiivinenTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pullonpalautus_active.png");
        virheTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/pullonpalautus_virhe.png");
    }
    
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
    
    public void valitseTila(PulloautomaatinKuvake paKuvake) {
        this.tila = paKuvake;
        switch (paKuvake) {
            case IDLE:
                super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/pullonpalautus_idle.png");
                super.tekstuuri = idleTekstuuri;
            break;
            case AKTIIVINEN:
                super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/pullonpalautus_active.png");
                super.tekstuuri = aktiivinenTekstuuri;
            break;
            case VIRHE:
                super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/pullonpalautus_virhe.png");
                super.tekstuuri = virheTekstuuri;
            break;
        }
    }
    
    public Pulloautomaatti (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Pulloautomaatti";
        super.tiedostonNimi = "pullonpalautus_idle.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Tänne voi palauttaa tölkit";
        super.erillisDialogi = true;
        super.ignooraaEsineValintaDialogissa = true;
        super.ohitaDialogiTesktiboksi = true;
        super.asetaTiedot();
    }
}
