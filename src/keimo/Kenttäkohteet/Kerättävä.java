package keimo.Kenttäkohteet;

import keimo.Säikeet.ÄänentoistamisSäie;

public abstract class Kerättävä extends KenttäKohde {
    
    public void kerää() {
        ÄänentoistamisSäie.toistaSFX("Kerättävä");
    }

    public Kerättävä(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }
}
