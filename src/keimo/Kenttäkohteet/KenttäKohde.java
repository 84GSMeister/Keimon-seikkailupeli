package keimo.Kenttäkohteet;

import keimo.PääIkkuna;

import javax.swing.Icon;
import java.awt.image.*;

public abstract class KenttäKohde implements Käännettävä {
    
    boolean määritettySijainti = false;
    int sijX;
    int sijY;
    public boolean lisäOminaisuuksia = false;
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

	public String annaLisäOminaisuudetYhtenäMjonona() {
        String mjono = "";
		if (annalisäOminaisuudet() != null) {
			for (String s : annalisäOminaisuudet()) {
				mjono += s + ",";
			}
			mjono = mjono.substring(0, mjono.length()-1);
		}
        return mjono;
    }

    Suunta suunta;
    
    BufferedImage käännettäväKuvake;
    
    protected String nimi;
    protected Icon kuvake;
    protected Icon dialogiKuvake;
    public boolean tavoiteSuoritettu = false;

    boolean vaatiiPäivityksen = true;

    String katsomisTeksti = "vakioteksti";
    public String katso() {
        return katsomisTeksti;
    }

    public String kokeileEsinettä(Esine e) {
        return "Mitään ei tapahtunut!";
    }

    public Esine suoritaMuutoksetEsineelle(Esine e) {
        if (e != null) {
            PääIkkuna.hudTeksti.setText(e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + this.annaNimiSijamuodossa("illatiivi"));
        }
        return e;
    }

    public String annaNimi() {
        return nimi;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        return "<sijamuotojen määrittely puuttuu kohteelta " + this.nimi + ">";
    }

    public Icon annaKuvake() {
        return kuvake;
    }

    public Icon annaDialogiKuvake() {
        if (dialogiKuvake == null) {
            return kuvake;
        }
        else {
            return dialogiKuvake;
        }
    }

    public void näytäDialogi(Esine e) {
        PääIkkuna.avaaDialogi(kuvake, katsomisTeksti, nimi);
    }

    String tiedot = "";
    void asetaTiedot() {
        tiedot = "";
        tiedot += "Nimi: " + this.annaNimi() + "\n";
        //tiedot += "Satunnainen sijainti: " + (!this.määritettySijainti ? "Kyllä" : "Ei") + "\n";
		

        if (this instanceof Esine) {
            tiedot += "Tyyppi: Esine" + "\n";

            Esine esine = (Esine)this;
            tiedot += "Kulutustavaraa: " + (esine.onkoKäyttö() ? "Kyllä" : "Ei") + "\n";
			if (esine.kenttäkäyttö) {
                tiedot += "Sopii käytettäväksi: ";
                for (String s : esine.sopiiKäytettäväksi) {
                    tiedot += s + ", ";
                }
				tiedot = tiedot.substring(0, tiedot.length()-2);
                tiedot += "\n";
            }
            if (esine.yhdistettävä) {
                tiedot += "Sopii yhdistettäväksi: ";
                for (String s : esine.kelvollisetYhdistettävät) {
                    tiedot += s + ", ";
                }
				tiedot = tiedot.substring(0, tiedot.length()-2);
                tiedot += "\n";
            }
        }
        else if (this instanceof Kiintopiste){
            tiedot += "Tyyppi: Kiintopiste" + "\n";
        }

        else if (this instanceof NPC_KenttäKohde) {
            tiedot += "Tyyppi: Kenttä-NPC" + "\n";
        }

        else if (this instanceof Warp) {
            tiedot += "Tyyppi: Warp" + "\n";
        }
        else if (this instanceof VisuaalinenObjekti) {
            tiedot += "Tyyppi: Visuaalinen objekti" + "\n";
            VisuaalinenObjekti vo = (VisuaalinenObjekti)this;
            tiedot += "kuva: " + vo.annaKuvanTiedostoNimi() + "\n";
            tiedot += "kääntö: " + vo.kääntöAsteet + "\n";
            tiedot += "x-peilaus: " + (vo.xPeilaus ? "kyllä" : "ei") + "\n";
            tiedot += "y-peilaus: " + (vo.yPeilaus ? "kyllä" : "ei") + "\n";
        }
    }
    
    public String annaTiedot() {
        return tiedot;
    }

    public KenttäKohde(boolean määritettySijainti, int sijX, int sijY) {
        this.määritettySijainti = määritettySijainti;
        this.sijX = sijX;
        this.sijY = sijY;
    }
}
