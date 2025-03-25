package keimo.kenttäkohteet.kenttäNPC;

import keimo.PääIkkuna;
import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.esine.Esine;

public abstract class NPC_KenttäKohde extends KenttäKohde {
    
    protected int hp = 10;

    protected boolean customDialogi = false;
    protected String valittuDialogi = "";

    @Override
    public void näytäDialogi(Esine e) {
        PääIkkuna.avaaPitkäDialogiRuutu(valittuDialogi);
    }

    public boolean onkoCustomDialogi() {
        return customDialogi;
    }

    public String annaDialogi () {
        return valittuDialogi;
    }

    public void asetaDialogi(String dialogi) {
        if (dialogi == null) {
            this.customDialogi = false;
            this.valittuDialogi = null;
        }
        else {
            this.customDialogi = true;
            this.valittuDialogi = dialogi;
        }
    }
    
    public NPC_KenttäKohde(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }

    public NPC_KenttäKohde(boolean määritettySijainti, int sijX, int sijY, String customDialogi) {
        super(määritettySijainti, sijX, sijY);
        asetaDialogi(customDialogi);
    }
}
