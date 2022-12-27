import javax.swing.Icon;

public abstract class Maasto {
    
    boolean määritettySijainti = false;
    int sijX;
    int sijY;

    protected String nimi;
    protected Icon kuvake;
    protected boolean estääLiikkumisen = false;
    
    String annaNimi() {
        return nimi;
    }

    String annaNimiSijamuodossa(String sijamuoto) {
        return nimi;
    }

    Icon annaKuvake() {
        return kuvake;
    }

    String tiedot = "";
    void asetaTiedot() {
        tiedot += "Nimi: " + this.annaNimi() + "\n";
        tiedot += "Satunnainen sijainti: " + (!this.määritettySijainti ? "Kyllä" : "Ei" + "\n");
    }
    
    String annaTiedot() {
        return tiedot;
    }
}
