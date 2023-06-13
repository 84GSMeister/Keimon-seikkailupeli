package keimo.Kenttäkohteet;

import keimo.PääIkkuna;

import javax.swing.ImageIcon;

public class Kauppa extends Kiintopiste {

    @Override
    public void näytäDialogi(Esine e) {
        PääIkkuna.avaaPitkäDialogiRuutu("kyläkauppa");
    }
    
    public Kauppa (boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kauppa";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppias.png");
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppias_dialogi.png");
        super.katsomisTeksti = "Kauppaan voi palauttaa tölkit";
        super.asetaTiedot();
    }
}
