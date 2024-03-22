package keimo.NPCt;

public class Entity {

    public int sijX;
    public int sijY;
    protected int alkuSijX;
    protected int alkuSijY;
    
    public SuuntaVasenOikea suuntaVasenOikea = SuuntaVasenOikea.OIKEA;
    public enum SuuntaVasenOikea {
        VASEN,
        OIKEA;
    }

    public SuuntaDiagonaali suuntaDiagonaali = SuuntaDiagonaali.OIKEA;
    public enum SuuntaDiagonaali {
        VASEN,
        OIKEA,
        ALAS,
        YLÖS,
        YLÄVASEN,
        ALAVASEN,
        YLÄOIKEA,
        ALAOIKEA;
    }
}
