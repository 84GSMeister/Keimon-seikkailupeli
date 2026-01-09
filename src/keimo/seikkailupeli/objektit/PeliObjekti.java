package keimo.seikkailupeli.objektit;

import keimo.keimoengine.collision.Neliö;
import keimo.keimoengine.collision.Piste;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.objekti3d.Transform3D;

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
    protected Renderöitävä tekstuuri;
    protected Renderöitävä dialogiTekstuuri;
    protected int kääntöAsteet = 0;
    public boolean xPeilaus = false;
    protected boolean yPeilaus = false;
    protected String katsomisTeksti = "vakioteksti";
    protected Transform3D transform = new Transform3D();

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

    public void päivitäLisäOminaisuudet(ArrayList<String> ominaisuusLista) {
        if (ominaisuusLista != null) {
            if (this.annaNimi() != null && this.annaNimi() != "") {
                for (int i = 0; i < ominaisuusLista.size(); i++) {
                    this.lisäOminaisuudet.add(ominaisuusLista.get(i));
                }
            }
            if (this.lisäOminaisuudet != null) {
                if (this.lisäOminaisuudet.size() == 0) {
                    this.lisäOminaisuuksia = false;
                }
                else {
                    this.lisäOminaisuuksia = true;
                    this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kääntö="));
                    if (kääntöAsteet != 0) this.lisäOminaisuudet.add("kääntö=" + kääntöAsteet);
                    this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("x-peilaus="));
                    if (xPeilaus) this.lisäOminaisuudet.add("x-peilaus=" + (xPeilaus ? "kyllä" : "ei"));
                    this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("y-peilaus="));
                    if (yPeilaus)this.lisäOminaisuudet.add("y-peilaus=" + (yPeilaus ? "kyllä" : "ei"));
                }
            }
        }
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

    public Renderöitävä annaTekstuuri() {
        return tekstuuri;
    }

    public Renderöitävä annaDialogiTekstuuri() {
        if (dialogiTekstuuri == null) return tekstuuri;
        else return dialogiTekstuuri;
    }

    public void asetaSuunta(Suunta suunta) {
        this.suunta = suunta;
    }
}
