import java.util.Random;
import java.util.ArrayList;

public abstract class Esine extends KenttäKohde {

    Random r = new Random();

    boolean poista = false;
    boolean poistoon(){
        return poista;
    }

    boolean kenttäkäyttö = false;
    boolean onkoKenttäkäyttöön() {
        return kenttäkäyttö;
    }

    boolean käyttö = false;
    boolean onkoKäyttö() {
        return käyttö;
    }

    boolean yhdistettävä = false;
    boolean onkoYhdistettävä() {
        return yhdistettävä;
    }
    ArrayList<String> kelvollisetYhdistettävät = new ArrayList<String>();
    ArrayList<String> sopiiKäytettäväksi = new ArrayList<String>();

    String käyttöTeksti = "Mitään ei tapahtunut.";
    String käytä(){
        poista = true;
        return käyttöTeksti;
    }

    String annaNimi(){
        return nimi;
    }

    void asetaTiedot() {
        tiedot += "Nimi: " + this.annaNimi() + "\n";
        tiedot += "Satunnainen sijainti: " + (!this.määritettySijainti ? "Kyllä" : "Ei");
        tiedot += "\n";
        tiedot += "Sopii käytettäväksi: ";
        for (String s : this.sopiiKäytettäväksi) {
            tiedot += s + ", ";
        }
        tiedot += "\n";
    }

    Esine() {
        
    }
}
