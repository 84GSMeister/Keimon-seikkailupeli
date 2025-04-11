package keimo.entityt.npc;

import keimo.TarkistettavatArvot;
import keimo.keimoEngine.liikeSimulaatiot.ReitinhakuSimulaatio.PathFindingExample.Point;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.kenttäkohteet.esine.Ase;

import java.util.ArrayList;
import java.util.List;

public abstract class Vihollinen extends NPC {
    
    public int vahinko = 0;
    public boolean tekeeVahinkoa = true;
    public ArrayList<String> tehoavatAseet = new ArrayList<String>();
    public boolean kilpiTehoaa = true;
    public String ominaisHuuto = "";
    public int liikeMoodi = 0;
    public int liikeX = 0;
    public int liikeY = 0;
    public boolean ampuu = false;
    public int ammusVahinko = 0;

    public enum LiikeTapa {
        LOOP_NELIÖ_MYÖTÄPÄIVÄÄN,
        LOOP_NELIÖ_VASTAPÄIVÄÄN,
        LOOP_VASEN_OIKEA,
        LOOP_YLÖS_ALAS,
        NELIÖ_MYÖTÄPÄIVÄÄN_ESTEESEEN_ASTI,
        NELIÖ_VASTAPÄIVÄÄN_ESTEESEEN_ASTI,
        VASEN_OIKEA_ESTEESEEN_ASTI,
        YLÖS_ALAS_ESTEESEEN_ASTI,
        VARTIJA_LIIKE,
        SEURAA_PELAAJAA,
        SEURAA_REITTIÄ,
        BOSS_LIIKE,
        YMPYRÄLIIKE_MYÖTÄPÄIVÄÄN,
        YMPYRÄLIIKE_VASTAPÄIVÄÄN,
        STAATTINEN;
    }
    
    public static int liikkeenPituus = 60;
    
    public int liikuVielä = liikkeenPituus;
    public int aikaaViimeHuudosta = 0;
    public Suunta[] liikeSuuntaLoopNeliöMyötäpäivään = {Suunta.YLÖS, Suunta.OIKEA, Suunta.ALAS, Suunta.VASEN};
    public Suunta[] liikeSuuntaLoopNeliöVastapäivään = {Suunta.YLÖS, Suunta.VASEN, Suunta.ALAS, Suunta.OIKEA};
    public Suunta[] liikeSuuntaLoopVasenOikea = {Suunta.VASEN, Suunta.OIKEA};
    public Suunta[] liikeSuuntaLoopYlösAlas = {Suunta.YLÖS, Suunta.ALAS};
    public int liikeLoopinVaihe = 0;
    public LiikeTapa liikeTapa = LiikeTapa.LOOP_NELIÖ_VASTAPÄIVÄÄN;
    protected int hurtAika = 0;

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

    protected void kukista(String kukistusTapa) {
        this.kukistettu = true;
        TarkistettavatArvot.lisääTappoLaskuriin(kukistusTapa);
    }

    public void vahingoita(Ase ase) {
        this.hp -= ase.annaVahinko();
        if (this.hp <= 0) {
            this.kukista(ase.annaNimi());
            ÄänentoistamisSäie.toistaSFX(ase.annaNimi());
        }
        else {
            this.hurtAika = 10;
            ÄänentoistamisSäie.toistaSFX(this.annaNimi() + "_damage");
        }
    }

    public void vähennäHurtAikaa() {
        if (this.hurtAika > 0) {
            this.hurtAika--;
        }
    }

    public int annaHurtAika() {
        return this.hurtAika;
    }

    public void päivitäLisäOminaisuudet(LiikeTapa liikeTapa, SuuntaVasenOikea suunta) {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[2];
        this.lisäOminaisuudet[0] = "liiketapa=" + liikeTapa;
        this.lisäOminaisuudet[1] = "suunta=" + suunta;
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
                else if (ominaisuus.startsWith("suunta=")) {
                    this.suuntaVasenOikea = SuuntaVasenOikea.valueOf(ominaisuus.substring(7));
                }
            }
        }
        else {
            this.liikeTapa = LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN;
        }
        this.lisäOminaisuudet = new String[2];
        this.lisäOminaisuudet[0] = "liiketapa=" + liikeTapa;
        this.lisäOminaisuudet[1] = "suunta=" + suuntaVasenOikea;
    }
}
