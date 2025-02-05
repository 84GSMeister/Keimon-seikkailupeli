package keimo.kenttäkohteet.kerättävä;

import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.kenttäkohteet.KenttäKohde;

public abstract class Kerättävä extends KenttäKohde {
    
    public void kerää() {
        ÄänentoistamisSäie.toistaSFX("Kerättävä");
    }

    public Kerättävä(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }
}
