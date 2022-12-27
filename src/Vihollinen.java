import java.util.ArrayList;

public class Vihollinen extends NPC{
    
    protected int vahinko = 0;
    protected ArrayList<String> tehoavatAseet = new ArrayList<String>();
    protected String[] liikeSuuntaLoop = {"vasen", "vasen", "ylös", "ylös", "oikea", "oikea", "alas", "alas"};
    protected int seuraavaLiikesuunta = 0;
    protected boolean onJoLiikutettu = false;
    protected boolean kilpiTehoaa = true;

    boolean kukistettu = false;
    

    boolean onkoKukistettu() {
        return kukistettu;
    }

    void kukista() {
        this.kukistettu = true;
    }

    Vihollinen() {

    }
}
