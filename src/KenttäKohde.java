import javax.swing.Icon;

public abstract class KenttäKohde {
    
    protected String nimi;
    protected Icon kuvake;
    protected boolean tavoiteSuoritettu = false;

    boolean vaatiiPäivityksen = true;

    String katsomisTeksti = "vakioteksti";
    String katso() {
        return katsomisTeksti;
    }

    String kokeileEsinettä(Esine e) {
        return "Mitään ei tapahtunut!";
    }

    String annaNimi() {
        return nimi;
    }

    String annaNimiSijamuodossa(String sijamuoto) {
        return nimi;
    }

    Icon annaKuvake() {
        return kuvake;
    }
}
