package keimo.kenttäkohteet.triggeri;

import keimo.entityt.npc.Vihollinen;
import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.esine.Esine;

public abstract class Triggeri extends KenttäKohde {
    
    private boolean triggeröity = false;
    protected Esine vaadittuEsine;
    protected Vihollinen vaadittuVihollinen;

    public boolean onkoTriggeröity() {
        return triggeröity;
    }

    public void triggeröi() {
        this.triggeröity = true;
    }

    public Esine annaVaadittuEsine() {
        return vaadittuEsine;
    }

    public Vihollinen annaVaadittuVihollinen() {
        return vaadittuVihollinen;
    }

    public Triggeri(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        this.triggeröity = false;
    }
}
