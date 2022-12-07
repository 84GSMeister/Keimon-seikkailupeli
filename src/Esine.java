import java.util.Random;
import java.util.ArrayList;

public abstract class Esine extends KenttäKohde {

    Random r = new Random();
    String sopiiKäytettäväksi;

    boolean poista = false;
    boolean poistoon(){
        return poista;
    }

    boolean kenttäkäyttö = false;
    boolean onkoKenttäkäyttöön() {
        return kenttäkäyttö;
    }

    boolean erikoiskäyttö = false;
    boolean onkoErikoiskäyttö() {
        return erikoiskäyttö;
    }

    boolean yhdistettävä = false;
    boolean onkoYhdistettävä() {
        return yhdistettävä;
    }
    ArrayList<String> kelvollisetYhdistettävät = new ArrayList<String>();

    String käytä(){
        poista = true;
        return "mitään ei tapahtunut";
    }

    String annaNimi(){
        return nimi;
    }

    Esine() {

    }
}
