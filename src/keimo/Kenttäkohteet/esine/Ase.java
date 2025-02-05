package keimo.kenttäkohteet.esine;

public abstract class Ase extends Esine {

    protected int vahinko = 0;
    public int annaVahinko() {
        return vahinko;
    }

    protected int hyökkäysAika = 0;
    public int annaHyökkäysAika() {
        return hyökkäysAika;
    }

    protected int hyökkäysViive = 0;
    public int annaHyökkäysViive() {
        return hyökkäysViive;
    }

    public Ase(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }
}
