package keimo.Kenttäkohteet;

import keimo.Säikeet.ÄänentoistamisSäie;

public abstract class Ruoka extends Esine {
    
    int heal;

    public int annaParannusMäärä() {
        return heal;
    }

    @Override
    public String käytä() {
        ÄänentoistamisSäie.toistaSFX("Käytä");
        return super.käytä();
    }
    
    public Ruoka(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }
}
