public class NPC extends KenttäKohde{
    
    protected int hp = 10;
    
    String kokeileEsinettä(Esine e) {
        System.out.println("Mitään ei tapahtunut!");
        return "Mitään ei tapahtunut!";
    }

    String annaNimi(){
        return nimi;
    }

    NPC() {

    }
}
