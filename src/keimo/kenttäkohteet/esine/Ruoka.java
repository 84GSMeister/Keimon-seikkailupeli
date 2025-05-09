package keimo.kenttäkohteet.esine;

import keimo.keimoEngine.äänet.Äänet;

public abstract class Ruoka extends Esine {
    
    int heal;

    public Ruoka(int sijX, int sijY) {
        super(sijX, sijY);
    }

    public int annaParannusMäärä() {
        return heal;
    }

    @Override
    public String käytä() {
        Äänet.toistaSFX("Käytä");
        return super.käytä();
    }
}
