import java.util.ArrayList;

public class Vihollinen extends NPC{
    
    protected int vahinko = 0;
    protected ArrayList<String> tehoavatAseet = new ArrayList<String>();

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
