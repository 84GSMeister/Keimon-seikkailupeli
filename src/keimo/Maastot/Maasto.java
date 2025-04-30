package keimo.Maastot;

import keimo.HuoneEditori.HuoneEditoriIkkuna;
import keimo.Utility.KäännettäväKuvake;
import keimo.Utility.Käännettävä.Suunta;
import keimo.Utility.KäännettäväKuvake.KääntöValinta;
import keimo.Utility.KäännettäväKuvake.PeilausValinta;
import keimo.keimoEngine.grafiikat.objekti3d.Transform3D;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public abstract class Maasto {
    
    boolean määritettySijainti = false;
    int sijX;
    int sijY;
    boolean lisäOminaisuuksia = false;
    String[] lisäOminaisuudet;
    int kääntöAsteet = 0;
    boolean xPeilaus = false;
    boolean yPeilaus = false;
    public Rectangle hitbox;
    public Transform3D transform = new Transform3D();

    protected String nimi;
    protected String katsomisTeksti;
    protected String tiedostonNimi;
    protected String tekstuuri;
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

     /**
     * Maaston tilen X-koordinaatti
     * @return X-sijainti (Tile)
     */
    public int annaSijX() {
        return sijX;
    }
    /**
     * Maaston tilen Y-koordinaatti
     * @return Y-sijainti (Tile)
     */
    public int annaSijY() {
        return sijY;
    }

    public boolean onkolisäOminaisuuksia() {
        return lisäOminaisuuksia;
    }

    public String[] annaLisäOminaisuudet() {
        return lisäOminaisuudet;
    }

    public String annaLisäOminaisuudetYhtenäMjonona() {
        String mjono = "";
		if (annaLisäOminaisuudet() != null) {
			for (String s : annaLisäOminaisuudet()) {
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
                case VASEN: return estääLiikkumisenVasen;
                case OIKEA: return estääLiikkumisenOikea;
                case ALAS: return estääLiikkumisenAlas;
                case YLÖS: return estääLiikkumisenYlös;
                case null, default: return false;
            }
        }
    }
    
    public String annaNimi() {
        return nimi;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        return katsomisTeksti;
    }

    public String annaTekstuuri() {
        return tekstuuri;
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
        // if (this instanceof YksisuuntainenTile) {
        //     YksisuuntainenTile yTile = (YksisuuntainenTile)this;
        //     yTile.päivitäEsteenSuunta();
        // }
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
        // if (this instanceof YksisuuntainenTile) {
        //     YksisuuntainenTile yTile = (YksisuuntainenTile)this;
        //     yTile.päivitäEsteenSuunta();
        // }
        päivitäLisäOminaisuudet();
    }

    public static Maasto luoMaastoTiedoilla(String maastonNimi, boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {

        Maasto luotavaMaasto;

        switch (maastonNimi) {

            case "Tile":
                luotavaMaasto = new Tile(sijX, sijY, ominaisuusLista);
                break;

            case "EsteTile":
                luotavaMaasto = new Tile(sijX, sijY, ominaisuusLista);
                break;
            
            case "Yksisuuntainen Tile":
                luotavaMaasto = new Tile(sijX, sijY, ominaisuusLista);
                break;

            default:
                luotavaMaasto = null;
                break;
        }

        return luotavaMaasto;
    }

    public static Maasto luoRandomMaasto(int sijX, int sijY) {
        Random r = new Random();
        Object[] lista = HuoneEditoriIkkuna.listaaKuvat("tiedostot/kuvat/maasto").toArray();
        Object kuvaTiedosto = lista[r.nextInt(lista.length)];
        String[] ominaisuusLista = {"kuva=" + kuvaTiedosto,"kääntö=0","x-peilaus=ei","y-peilaus=ei"};
        return luoMaastoTiedoilla("Tile", true, sijX, sijY, ominaisuusLista);
    }

    String tiedot = "";
    void asetaTiedot() {
        tiedot = "";
        tiedot += "Nimi: " + this.annaNimi() + "\n";

        List<Suunta> esteSuunnat = new ArrayList<>();
        if (this.estääköLiikkumisen(Suunta.VASEN)) esteSuunnat.add(Suunta.VASEN);
        if (this.estääköLiikkumisen(Suunta.OIKEA)) esteSuunnat.add(Suunta.OIKEA);
        if (this.estääköLiikkumisen(Suunta.YLÖS)) esteSuunnat.add(Suunta.YLÖS);
        if (this.estääköLiikkumisen(Suunta.ALAS)) esteSuunnat.add(Suunta.ALAS);
        tiedot += "Estää liikkumisen: " + esteSuunnat.toString() + "\n";

        tiedot += "Kuva: " + this.tiedostonNimi;
    }
    
    public String annaTiedot() {
        return tiedot;
    }

    protected void luoSkaalattuKuvake() {
         
    }
}
