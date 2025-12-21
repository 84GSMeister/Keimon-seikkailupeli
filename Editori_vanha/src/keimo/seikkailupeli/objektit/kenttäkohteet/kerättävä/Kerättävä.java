package keimo.seikkailupeli.objektit.kenttäkohteet.kerättävä;

import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;

public abstract class Kerättävä extends KenttäKohde {

    public Kerättävä(int sijX, int sijY) {
        super(sijX, sijY);
    }
    
    public abstract void kerää();
}
