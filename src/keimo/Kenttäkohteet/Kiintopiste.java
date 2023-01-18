package keimo.Kenttäkohteet;
import java.util.ArrayList;

public abstract class Kiintopiste extends KenttäKohde {

    boolean vuorovaikutus = false;
    public boolean vuorovaikutusToimii() {
        return vuorovaikutus;
    }

    boolean Suoritettu = false;
    ArrayList<String> käyvätEsineet = new ArrayList<String>();

    public String kokeileEsinettä(Esine e) {
        System.out.println("Mitään ei tapahtunut!");
        return "Mitään ei tapahtunut!";
    }

    public String annaNimi(){
        return nimi;
    }

    public Kiintopiste() {
        
    }
}
