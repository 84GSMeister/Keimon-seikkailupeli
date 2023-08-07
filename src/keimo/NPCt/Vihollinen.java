package keimo.NPCt;

import keimo.PeliKenttäMetodit.PathFindingExample.Point;

//import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public abstract class Vihollinen extends NPC {
    
    public int vahinko = 0;
    public boolean tekeeVahinkoa = true;
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
        SEURAA_REITTIÄ,
        STAATTINEN;
    }
    
    public static int liikkeenPituus = 60;
    
    public int liikuVielä = liikkeenPituus;
    public Suunta[] liikeSuuntaLoopNeliöMyötäpäivään = {Suunta.YLÖS, Suunta.OIKEA, Suunta.ALAS, Suunta.VASEN};
    public Suunta[] liikeSuuntaLoopNeliöVastapäivään = {Suunta.YLÖS, Suunta.VASEN, Suunta.ALAS, Suunta.OIKEA};
    public Suunta[] liikeSuuntaLoopVasenOikea = {Suunta.VASEN, Suunta.OIKEA};
    public Suunta[] liikeSuuntaLoopYlösAlas = {Suunta.YLÖS, Suunta.ALAS};
    public int liikeLoopinVaihe = 0;
    public LiikeTapa liikeTapa = LiikeTapa.LOOP_NELIÖ_VASTAPÄIVÄÄN;

    public List<Point> reitti;
    public void tulostaReitinKoordinaatit() {
        for (int i = 0; i < reitti.size(); i++) {
            System.out.println("Piste " + i + ": " + reitti.get(i).x + ", " + reitti.get(i).y);
        }
    }
    
    protected boolean kukistettu = false;
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
