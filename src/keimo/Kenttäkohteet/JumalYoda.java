package keimo.Kenttäkohteet;

import keimo.PääIkkuna;
import keimo.TavoiteLista;
import keimo.Utility.KäännettäväKuvake;

import javax.swing.ImageIcon;

public class JumalYoda extends NPC_KenttäKohde {

    @Override
    public String kokeileEsinettä(Esine e) {
        this.näytäDialogi(e);
        return katsomisTeksti;
    }

    @Override
    public void näytäDialogi(Esine e) {
        if (TavoiteLista.tavoiteLista.get("Löydä Jumal Yoda")) {
            super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/yoda.png");
            super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");
            if (TavoiteLista.tavoiteLista.get("Avaa takahuone")) {
                PääIkkuna.avaaDialogi(super.dialogiKuvake, "Hmm...mm...", super.nimi);
            }
            else {
                PääIkkuna.avaaPitkäDialogiRuutu("goblin_alku", "goblin");
            }
        }
        else {
            PääIkkuna.avaaDialogi(super.dialogiKuvake, "Hrmm...", "Goblin");
        }
    }

    public JumalYoda(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Jumal Yoda";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/goblin.png");
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/goblin_dialogi.png");
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
        super.katsomisTeksti = "Polku pimeälle puolelle?";
        super.asetaTiedot();
    }
}
