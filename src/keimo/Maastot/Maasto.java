package keimo.Maastot;
import javax.swing.Icon;

public abstract class Maasto {
    
    boolean määritettySijainti = false;
    int sijX;
    int sijY;
    boolean lisäOminaisuuksia = false;
    String[] lisäOminaisuudet;

    public boolean onkoMääritettySijainti() {
        return määritettySijainti;
    }

    public int annaSijX() {
        return sijX;
    }

    public int annaSijY() {
        return sijY;
    }

    public boolean onkolisäOminaisuuksia() {
        return lisäOminaisuuksia;
    }

    public String[] annalisäOminaisuudet() {
        return lisäOminaisuudet;
    }

    protected String nimi;
    protected Icon kuvake;
    protected boolean estääLiikkumisen = false;

    public boolean estääköLiikkumisen() {
        return estääLiikkumisen;
    }
    
    public String annaNimi() {
        return nimi;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        return nimi;
    }

    public Icon annaKuvake() {
        return kuvake;
    }

    String tiedot = "";
    void asetaTiedot() {
        tiedot += "Nimi: " + this.annaNimi() + "\n";
        tiedot += "Satunnainen sijainti: " + (!this.määritettySijainti ? "Kyllä" : "Ei" + "\n");
    }
    
    public String annaTiedot() {
        return tiedot;
    }
}
