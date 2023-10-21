package keimo.Kenttäkohteet;

import keimo.PääIkkuna;

public abstract class NPC_KenttäKohde extends KenttäKohde {
    
    protected int hp = 10;

    protected boolean customDialogi = false;
    protected String valittuDialogi = "";

    @Override
    public void näytäDialogi(Esine e) {
        PääIkkuna.avaaDialogi(dialogiKuvake, valittuDialogi, nimi);
    }

    public String annaDialogi () {
        return valittuDialogi;
    }

    public void asetaDialogi(String dialogi) {
        this.customDialogi = true;
        this.valittuDialogi = dialogi;
    }
    
    public NPC_KenttäKohde(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }

    public NPC_KenttäKohde(boolean määritettySijainti, int sijX, int sijY, String customDialogi) {
        super(määritettySijainti, sijX, sijY);
        asetaDialogi(customDialogi);
    }
}
