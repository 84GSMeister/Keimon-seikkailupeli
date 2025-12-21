package keimo.seikkailupeli.objektit.kenttäkohteet.esine;

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
        return super.käytä();
    }
}
