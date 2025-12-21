package keimo.seikkailupeli.objektit;

import keimo.editori.utility.KäännettäväKuvake.KääntöValinta;
import keimo.editori.utility.KäännettäväKuvake.PeilausValinta;
import keimo.Utility.collision.Neliö;
import keimo.Utility.collision.Piste;

import java.util.ArrayList;

public abstract class PeliObjekti implements Käännettävä {

    protected String nimi = "";
    protected int alkuSijX;
    protected int alkuSijY;
    protected int sijX;
    protected int sijY;
    protected boolean lisäOminaisuuksia = false;
    protected ArrayList<String> lisäOminaisuudet;
    public Neliö hitbox;
    protected Suunta suunta = Suunta.YLÖS;
    protected String tiedostonNimi;
    protected int kääntöAsteet = 0;
    public boolean xPeilaus = false;
    protected boolean yPeilaus = false;
    protected String katsomisTeksti = "vakioteksti";

    /**
     * Objektin tilen X-koordinaatti
     * @return X-sijainti (Tile)
     */
    public int annaSijX() {
        return sijX;
    }
    /**
     * Objektin tilen Y-koordinaatti
     * @return Y-sijainti (Tile)
     */
    public int annaSijY() {
        return sijY;
    }
    /**
     * Objektin sijainti pelikentällä pikseleinä.
     * Ei objektin sijainti näytöllä vaan scrollattavalla pelikentällä.
     * @return Objektin sijaintia vastaava piste (java.awt.Point)
     */
    public Piste annaSijaintiKentällä() {
        Piste sijainti = new Piste(sijX * 64, sijY * 64);
        return sijainti;
    }

    public boolean onkoLisäOminaisuuksia() {
        return lisäOminaisuuksia;
    }

    public ArrayList<String> annaLisäOminaisuudet() {
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

    public int annaKääntöAsteet() {
        return kääntöAsteet;
    }

    public boolean annaXPeilaus() {
        return xPeilaus;
    }

    public boolean annaYPeilaus() {
        return yPeilaus;
    }

    public Suunta annaSuunta() {
        return suunta;
    }

    public String katso() {
        return katsomisTeksti;
    }

    public String annaNimi() {
        return nimi;
    }

    public abstract String annaNimiSijamuodossa(String sijamuoto);

    public String haeDialogiTeksti(String teksti) {
        return katso();
    }

    public String annaKuvanTiedostoNimi() {
        return tiedostonNimi;
    }

    public abstract void päivitäLisäOminaisuudet();

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
        päivitäLisäOminaisuudet();
    }

    public void asetaSuunta(Suunta suunta) {
        this.suunta = suunta;
    }
}
