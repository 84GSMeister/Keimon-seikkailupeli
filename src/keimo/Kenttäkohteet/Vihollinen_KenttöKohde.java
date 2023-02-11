package keimo.Kenttäkohteet;
import java.util.ArrayList;

public abstract class Vihollinen_KenttöKohde extends NPC_KenttäKohde{
    
    public int vahinko = 0;
    public ArrayList<String> tehoavatAseet = new ArrayList<String>();
    public String[] liikeSuuntaLoop = {"vasen", "vasen", "ylös", "ylös", "oikea", "oikea", "alas", "alas"};
    public int seuraavaLiikesuunta = 0;
    public boolean onJoLiikutettu = false;
    public boolean kilpiTehoaa = true;

    boolean kukistettu = false;
    

    public boolean onkoKukistettu() {
        return kukistettu;
    }

    public void kukista(String kukistusTapa) {
        this.kukistettu = true;
    }

    public Vihollinen_KenttöKohde(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }
}
