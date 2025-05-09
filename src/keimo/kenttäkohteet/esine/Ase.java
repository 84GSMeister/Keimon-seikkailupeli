package keimo.kenttäkohteet.esine;

public abstract class Ase extends Esine {

    protected int vahinko = 0;
    protected int hyökkäysAika = 0;
    protected int hyökkäysViive = 0;

    public Ase(int sijX, int sijY) {
        super(sijX, sijY);
    }

    public int annaVahinko() {
        return vahinko;
    }

    public int annaHyökkäysAika() {
        return hyökkäysAika;
    }

    public int annaHyökkäysViive() {
        return hyökkäysViive;
    }
}
