package keimo.kenttäkohteet.kerättävä;

import keimo.kenttäkohteet.KenttäKohde;

public abstract class Kerättävä extends KenttäKohde {

    public Kerättävä(int sijX, int sijY) {
        super(sijX, sijY);
    }
    
    public abstract void kerää();
}
