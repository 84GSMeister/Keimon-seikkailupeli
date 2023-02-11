package keimo.Kenttäkohteet;
public class Ruoka extends Esine {
    int heal;

    public int annaParannusMäärä() {
        return heal;
    }
    
    public Ruoka(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }
}
