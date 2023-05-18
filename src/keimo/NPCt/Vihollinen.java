package keimo.NPCt;

import java.util.ArrayList;

public abstract class Vihollinen extends NPC{
    
    public int vahinko = 0;
    public ArrayList<String> tehoavatAseet = new ArrayList<String>();
    public boolean kilpiTehoaa = true;

    public enum LiikeTapa {
        LOOP_NELIÖ_MYÖTÄPÄIVÄÄN,
        LOOP_NELIÖ_VASTAPÄIVÄÄN,
        LOOP_VASEN_OIKEA,
        LOOP_YLÖS_ALAS,
        NELIÖ_MYÖTÄPÄIVÄÄN_ESTEESEEN_ASTI,
        NELIÖ_VASTAPÄIVÄÄN_ESTEESEEN_ASTI,
        VASEN_OIKEA_ESTEESEEN_ASTI,
        YLÖS_ALAS_ESTEESEEN_ASTI,
        SEURAA_PELAAJAA,
        STAATTINEN;
    }
    
    public int liikkeenPituus = 20;
    public int liikuVielä = 20;
    public String[] liikeSuuntaLoopNeliöMyötäpäivään = {"ylös", "oikea", "alas", "vasen"};
    public String[] liikeSuuntaLoopNeliöVastapäivään = {"ylös", "vasen", "alas", "oikea"};
    public String[] liikeSuuntaLoopVasenOikea = {"vasen", "oikea"};
    public String[] liikeSuuntaLoopYlösAlas = {"ylös", "alas"};
    public int liikeLoopinVaihe = 0;
    public LiikeTapa liikeTapa = LiikeTapa.LOOP_NELIÖ_VASTAPÄIVÄÄN;

    
    boolean kukistettu = false;

    public boolean onkoKukistettu() {
        return kukistettu;
    }

    public void kukista(String kukistusTapa) {
        this.kukistettu = true;
    }

    public void päivitäLisäOminaisuudet(LiikeTapa liikeTapa) {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[1];
        this.lisäOminaisuudet[0] = "liiketapa=" + liikeTapa;
    }

    public void valitseKuvake() {
        
    }

    Vihollinen(int sijX, int sijY, String[] ominaisuusLista) {
        super(sijX, sijY);
        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("liiketapa=")) {
                    this.liikeTapa = LiikeTapa.valueOf(ominaisuus.substring(10));
                }
            }
        }
        else {
            this.liikeTapa = LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN;
        }
        this.lisäOminaisuudet = new String[1];
        this.lisäOminaisuudet[0] = "liiketapa=" + liikeTapa;
    }
}
