package keimo.Kenttäkohteet;

public abstract class NPC_KenttäKohde extends KenttäKohde {
    
    protected int hp = 10;
    
    public NPC_KenttäKohde(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }
}
