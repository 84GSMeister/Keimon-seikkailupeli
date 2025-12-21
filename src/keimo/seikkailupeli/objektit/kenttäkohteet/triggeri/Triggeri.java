package keimo.seikkailupeli.objektit.kenttäkohteet.triggeri;

import keimo.seikkailupeli.objektit.entityt.npc.Vihollinen;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Esine;

public abstract class Triggeri extends KenttäKohde {
    
    private boolean triggeröity = false;
    protected Esine vaadittuEsine;
    protected Vihollinen vaadittuVihollinen;

    public Triggeri(int sijX, int sijY) {
        super(sijX, sijY);
        this.triggeröity = false;
    }

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
}
