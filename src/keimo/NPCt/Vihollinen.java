package keimo.NPCt;

import java.util.ArrayList;

public abstract class Vihollinen extends NPC{
    
    public int vahinko = 0;
    public ArrayList<String> tehoavatAseet = new ArrayList<String>();
    public boolean kilpiTehoaa = true;

    public enum LiikeTapa {
        LOOP_NELIÖ_MYÖTÄPÄIVÄÄN,
        LOOP_NELIÖ_VASTAPÄIVÄÄN;
    }
    
    public int liikkeenPituus = 20;
    public int liikuVielä = 20;
    public String[] liikeSuuntaLoopNeliöMyötäpäivään = {"ylös", "oikea", "alas", "vasen"};
    public String[] liikeSuuntaLoopNeliöVastapäivään = {"ylös", "vasen", "alas", "oikea"};
    public int liikeLoopinVaihe = 0;
    public LiikeTapa liikeTapa = LiikeTapa.LOOP_NELIÖ_VASTAPÄIVÄÄN;

    
    boolean kukistettu = false;

    public boolean onkoKukistettu() {
        return kukistettu;
    }

    public void kukista(String kukistusTapa) {
        this.kukistettu = true;
    }

    Vihollinen(LiikeTapa liikeTapa) {
        super();
        switch (liikeTapa) {
            case LOOP_NELIÖ_MYÖTÄPÄIVÄÄN: this.liikeTapa = LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN; break;
            case LOOP_NELIÖ_VASTAPÄIVÄÄN: this.liikeTapa = LiikeTapa.LOOP_NELIÖ_VASTAPÄIVÄÄN; break;
            default: break;
        }
    }
}
