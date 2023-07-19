package keimo.Kenttäkohteet;

import keimo.PääIkkuna;
import keimo.TavoiteLista;
import keimo.Utility.KäännettäväKuvake;

import javax.swing.ImageIcon;

public class Hämärähemmo extends NPC_KenttäKohde {

    @Override
    public String kokeileEsinettä(Esine e) {
        this.näytäDialogi(e);
        return katsomisTeksti;
    }

    @Override
    public void näytäDialogi(Esine e) {
        if (TavoiteLista.tavoiteLista.get("Avaa takahuone")) {
            PääIkkuna.avaaDialogi(super.kuvake, "...", super.nimi);
        }
        else {
            PääIkkuna.avaaPitkäDialogiRuutu("hämärähemmo_alku", "hämärähemmo");
        }
    }

    public Hämärähemmo(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Hämärähemmo";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/hämärähemmo.png");
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
        super.katsomisTeksti = "Kulku pimeälle puolelle?";
        super.asetaTiedot();
    }
}
