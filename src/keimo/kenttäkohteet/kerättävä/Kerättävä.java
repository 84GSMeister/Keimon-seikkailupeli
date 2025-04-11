package keimo.kenttäkohteet.kerättävä;

import keimo.kenttäkohteet.KenttäKohde;

public abstract class Kerättävä extends KenttäKohde {
    
    public abstract void kerää();

    public Kerättävä(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }
}
