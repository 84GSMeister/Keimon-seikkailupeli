package keimo.seikkailupeli.objektit;

import java.util.ArrayList;

public abstract class SuunnallinenObjekti extends PeliObjekti implements Suunnallinen {

    protected Suunta suunta = Suunta.YLÖS;
    protected SuuntaVasenOikea suuntaVasenOikea = SuuntaVasenOikea.OIKEA;

    public SuunnallinenObjekti(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        return this.nimi;
    }

    public Suunta annaSuunta() {
        return suunta;
    }
    
    public void asetaSuunta(Suunta suunta) {
        this.suunta = suunta;
    }
}
