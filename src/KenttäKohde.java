import javax.swing.Icon;

public abstract class KenttäKohde {
    
    protected String nimi;
    protected Icon kuvake;
    protected boolean tavoiteSuoritettu = false;

    boolean vaatiiPäivityksen = true;

    String katso() {
        System.out.println("Vakiometodi");
        return "vakiometodi";
    }

    String kokeileEsinettä(Esine e) {
        System.out.println("Mitään ei tapahtunut!");
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
