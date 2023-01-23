package keimo.Kenttäkohteet;
public abstract class NPC extends KenttäKohde{
    
    protected int hp = 10;
    
    public String kokeileEsinettä(Esine e) {
        System.out.println("Mitään ei tapahtunut!");
        return "Mitään ei tapahtunut!";
    }

    public String annaNimi(){
        return nimi;
    }
}
