package keimo.Kenttäkohteet;

import keimo.Ruudut.Lisäruudut.ValintaDialogiIkkuna;
import keimo.Utility.KäännettäväKuvake;

import javax.swing.ImageIcon;

public class Silta extends Kiintopiste {
    
    @Override
    public void näytäDialogi(Esine e) {
        ValintaDialogiIkkuna.luoValintaDialogiIkkuna("silta");
    }
    
    public Silta (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Silta";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/asfaltti_silta.png");
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
        super.tiedostonNimi = "asfaltti_silta.png";
        super.katsomisTeksti = "Kiva näkymä";
        super.asetaTiedot();
    }
}
