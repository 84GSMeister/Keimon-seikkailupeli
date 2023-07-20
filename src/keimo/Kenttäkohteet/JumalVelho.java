package keimo.Kenttäkohteet;

import keimo.PääIkkuna;
import keimo.TavoiteLista;
import keimo.Utility.KäännettäväKuvake;

import javax.swing.ImageIcon;

public class JumalVelho extends NPC_KenttäKohde {
    
    @Override
    public String kokeileEsinettä(Esine e) {
        this.näytäDialogi(e);
        return katsomisTeksti;
    }

    @Override
    public void näytäDialogi(Esine e) {
        TavoiteLista.suoritaTavoite("Löydä Jumal Yoda");
        if (TavoiteLista.tavoiteLista.get("Keitä booli")) {
            PääIkkuna.avaaDialogi(super.dialogiKuvake, "...", super.nimi);
        }
        else {
            PääIkkuna.avaaDialogi(super.dialogiKuvake, "Tervetuloa kaikki mun bordello dello dello dello dello dello dello delloon", super.nimi);
        }
    }

    public JumalVelho(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Jumal Velho";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/velho.png");
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/velho_dialogi.png");
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
        super.katsomisTeksti = "No se on se Jumal Velho!";
        super.asetaTiedot();
    }
}
