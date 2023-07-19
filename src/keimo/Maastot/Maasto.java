package keimo.Maastot;

import keimo.Kenttäkohteet.Käännettävä.Suunta;
import keimo.Utility.KäännettäväKuvake;
import keimo.Utility.KäännettäväKuvake.KääntöValinta;
import keimo.Utility.KäännettäväKuvake.PeilausValinta;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class Maasto {
    
    boolean määritettySijainti = false;
    int sijX;
    int sijY;
    boolean lisäOminaisuuksia = false;
    String[] lisäOminaisuudet;
    int kääntöAsteet = 0;
    boolean xPeilaus = false;
    boolean yPeilaus = false;

    protected String nimi;
    protected String katsomisTeksti;
    protected Icon kuvake;
    protected Icon skaalattuKuvake;
    protected String tiedostonNimi;
    protected boolean estääLiikkumisen = false;
    protected boolean estääLiikkumisenVasen = false;
    protected boolean estääLiikkumisenOikea = false;
    protected boolean estääLiikkumisenAlas = false;
    protected boolean estääLiikkumisenYlös = false;

    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kuva=" + tiedostonNimi;
        this.lisäOminaisuudet[1] = "kääntö=" + kääntöAsteet;
        this.lisäOminaisuudet[2] = "x-peilaus=" + (xPeilaus ? "kyllä" : "ei");
        this.lisäOminaisuudet[3] = "y-peilaus=" + (yPeilaus ? "kyllä" : "ei");
    }

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

    public boolean estääköLiikkumisen(Suunta suunta) {
        if (this.estääLiikkumisen) {
            return true;
        }
        else {
            switch (suunta) {
                case VASEN:
                    return estääLiikkumisenVasen;
                case OIKEA:
                    return estääLiikkumisenOikea;
                case ALAS:
                    return estääLiikkumisenAlas;
                case YLÖS:
                    return estääLiikkumisenYlös;
                default:
                    return false;
            }
        }
    }
    
    public String annaNimi() {
        return nimi;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        return katsomisTeksti;
    }

    public Icon annaKuvake() {
        return kuvake;
    }

    public Icon annaSkaalattuKuvake() {
        if (skaalattuKuvake == null) {
            return kuvake;
        }
        else {
            return skaalattuKuvake;
        }
    }

    public String annaKuvanTiedostoNimi() {
        return tiedostonNimi;
    }

    public int annaKuvanKääntö() {
        return kääntöAsteet;
    }

    public boolean annaKuvanPeilausX() {
        return xPeilaus;
    }

    public boolean annaKuvanPeilausY() {
        return yPeilaus;
    }

    public void käännäKuvaa(KääntöValinta kääntö) {
        switch (kääntö) {
            case MYÖTÄPÄIVÄÄN:
                this.kääntöAsteet += 90;
                this.kääntöAsteet = kääntöAsteet % 360;
            break;
            case VASTAPÄIVÄÄN:
                this.kääntöAsteet += 270;
                this.kääntöAsteet = kääntöAsteet % 360;
            break;
        }
        päivitäKuvanAsento();
        päivitäLisäOminaisuudet();
    }

    public void peilaaKuva(PeilausValinta peilaus) {
        switch (peilaus) {
            case PEILAA_VAAKA:
                if (this.xPeilaus) {
                    this.xPeilaus = false;
                }
                else {
                    this.xPeilaus = true;
                }
            break;
            case PEILAA_PYSTY:
                if (this.yPeilaus) {
                    this.yPeilaus = false;
                }
                else {
                    this.yPeilaus = true;
                }
            break;
        }
        päivitäKuvanAsento();
        päivitäLisäOminaisuudet();
    }

    public void päivitäKuvanAsento() {
        File kuvaTiedosto = new File("tiedostot/kuvat/maasto/" + tiedostonNimi);
        if (kuvaTiedosto.isFile()) {
            if (kääntöAsteet == 0 && !xPeilaus && !yPeilaus) {
                kuvake = new ImageIcon("tiedostot/kuvat/maasto/" + tiedostonNimi);
            }
            else {
                kuvake = new ImageIcon("tiedostot/kuvat/maasto/" + tiedostonNimi);
                kuvake = new KäännettäväKuvake(kuvake, kääntöAsteet, xPeilaus, yPeilaus);
            }
        }
        else {
            kuvake = new ImageIcon("tiedostot/kuvat/virhekuva_maasto.png");
        }
        skaalattuKuvake = new KäännettäväKuvake(kuvake, kääntöAsteet, xPeilaus, yPeilaus, 96);
    }

    public static Maasto luoMaastoTiedoilla(String maastonNimi, boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {

        Maasto luotavaMaasto;

        switch (maastonNimi) {

            case "Tile":
                luotavaMaasto = new Tile(sijX, sijY, ominaisuusLista);
                break;

            case "EsteTile":
                luotavaMaasto = new EsteTile(sijX, sijY, ominaisuusLista);
                break;
            
            case "Yksisuuntainen Tile":
                luotavaMaasto = new YksisuuntainenTile(sijX, sijY, ominaisuusLista);
                break;

            default:
                luotavaMaasto = null;
                break;
        }

        return luotavaMaasto;
    }

    String tiedot = "";
    void asetaTiedot() {
        tiedot += "Nimi: " + this.annaNimi() + "\n";
        tiedot += "Satunnainen sijainti: " + (!this.määritettySijainti ? "Kyllä" : "Ei") + "\n";
        tiedot += "Estää liikkumisen: " + (this.estääköLiikkumisen(Suunta.YLÖS) ? "Kyllä" : "Ei") + "\n";
        tiedot += "Kuva: " + this.tiedostonNimi;
    }
    
    public String annaTiedot() {
        return tiedot;
    }

    protected void luoSkaalattuKuvake() {
         
    }
}
