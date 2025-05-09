package keimo.entityt.npc;

import keimo.TarkistettavatArvot;
import keimo.keimoEngine.liikeSimulaatiot.ReitinhakuSimulaatio.PathFindingExample.Point;
import keimo.keimoEngine.äänet.Äänet;
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
            Äänet.toistaSFX(ase.annaNimi());
        }
        else {
            this.hurtAika = 10;
            Äänet.toistaSFX(this.annaNimi() + "_damage");
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

    public void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet != null) {
            this.lisäOminaisuuksia = true;
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("liiketapa="));
            this.lisäOminaisuudet.add("liiketapa=" + liikeTapa);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("suunta="));
            this.lisäOminaisuudet.add("suunta=" + suunta);
        }
    }

    public void valitseKuvake() {
        
    }

    static int npcId = 0;
    Vihollinen(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY);
        if (ominaisuusLista != null) {
            this.lisäOminaisuudet = new ArrayList<>();
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("liiketapa=")) {
                    this.liikeTapa = LiikeTapa.valueOf(ominaisuus.substring(10));
                }
                else if (ominaisuus.startsWith("suunta=")) {
                    String suuntaString = ominaisuus.substring(7);
                    switch (suuntaString) {
                        case "vasen", "VASEN", "Vasen": this.suuntaVasenOikea = SuuntaVasenOikea.VASEN; break;
                        case "oikea", "OIKEA", "Oikea": this.suuntaVasenOikea = SuuntaVasenOikea.OIKEA; break;
                        default: this.suuntaVasenOikea = SuuntaVasenOikea.VASEN; break;
                    }
                }
            }
            päivitäLisäOminaisuudet();
        }
    }
}
