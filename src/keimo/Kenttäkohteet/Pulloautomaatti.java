package keimo.Kenttäkohteet;

import keimo.Ruudut.Lisäruudut.PullonPalautusRuutu;

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
    }
    
    public Pulloautomaatti (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Pulloautomaatti";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/pullonpalautus_idle.png");
        super.tiedostonNimi = "pullonpalautus_idle.png";
        super.katsomisTeksti = "Tänne voi palauttaa tölkit";
        super.erillisDialogi = true;
        super.ignooraaEsineValintaDialogissa = true;
        super.ohitaDialogiTesktiboksi = true;
        super.asetaTiedot();
    }
}
