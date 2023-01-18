package keimo.Kenttäkohteet;
import java.util.Random;
import java.util.ArrayList;

public abstract class Esine extends KenttäKohde {

    Random r = new Random();

    public boolean poista = false;
    public boolean poistoon(){
        return poista;
    }

    boolean kenttäkäyttö = false;
    public boolean onkoKenttäkäyttöön() {
        return kenttäkäyttö;
    }

    boolean käyttö = false;
    public boolean onkoKäyttö() {
        return käyttö;
    }

    boolean yhdistettävä = false;
    public boolean onkoYhdistettävä() {
        return yhdistettävä;
    }

    public ArrayList<String> kelvollisetYhdistettävät = new ArrayList<String>();
    public ArrayList<String> sopiiKäytettäväksi = new ArrayList<String>();

    String käyttöTeksti = "Mitään ei tapahtunut.";
    public String käytä(){
        poista = true;
        return käyttöTeksti;
    }

    public String annaNimi(){
        return nimi;
    }
    /**
    void asetaTiedot() {
        tiedot += "Nimi: " + this.annaNimi() + "\n";
        tiedot += "Satunnainen sijainti: " + (!this.määritettySijainti ? "Kyllä" : "Ei" + "\n");
        
        tiedot += "Sopii käytettäväksi: ";
        for (String s : this.sopiiKäytettäväksi) {
            tiedot += s + ", ";
        }
        tiedot += "\n";
    }
    */

    public Esine() {
        
    }
}
