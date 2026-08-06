package keimo.seikkailupeli.objektit;

import keimo.keimoengine.collision.Neliö;
import keimo.keimoengine.collision.Piste;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.objekti3d.Transform3D;
import keimo.seikkailupeli.assets.KuvaObjekti;

import java.util.ArrayList;

/**
 * PeliObjekti on yläluokka kaikille pelin esineille, maastolaatoille, entityille ja muille vuorovaikutettaville kohteille.
 * Kaikilla PeliObjekteilla on sijainti XY-kentällä sekä lista ominaisuuksista, jotka tarkentavat objektin toimintaa.
 */

public abstract class PeliObjekti implements Käännettävä {

    protected String nimi = "";
    protected int alkuSijX;
    protected int alkuSijY;
    protected int sijX;
    protected int sijY;
    protected ArrayList<String> lisäOminaisuudet = new ArrayList<>();
    public Neliö hitbox;
    protected String tiedostonNimi;
    protected KuvaObjekti tekstuuriObjekti;
    protected Renderöitävä tekstuuri;
    protected Renderöitävä dialogiTekstuuri;
    protected int kääntöAsteet = 0;
    protected boolean xPeilaus = false;
    protected boolean yPeilaus = false;
    protected String katsomisTeksti = "vakioteksti";
    protected Transform3D transform = new Transform3D();

    /**
     * Yläkonstruktori, jota kutsutaan jokaisen alaluokan luonnissa. Asettaa perusominaisuudet objektille.
     * @param sijX objektin sijainti X alussa
     * @param sijY objektin sijainti Y alussa
     * @param ominaisuusLista Jos lista on null, luodaan uusi tyhjä lista.
     */

    public PeliObjekti(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        this.sijX = sijX;
        this.sijY = sijY;
        this.lisäOminaisuudet.clear();
        
        if (ominaisuusLista != null) {
            this.lisäOminaisuudet = new ArrayList<>();
            for (String s : ominaisuusLista) {
                this.lisäOminaisuudet.add(new String(s));
            }
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kääntö=")) {
                    try {
                        this.kääntöAsteet = Integer.parseInt(ominaisuus.substring(7));
                    }
                    catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                else if (ominaisuus.startsWith("x-peilaus=")) {
                    if (ominaisuus.substring(10).startsWith("kyllä")) {
                        this.xPeilaus = true;
                    }
                    else {
                        this.xPeilaus = false;
                    }
                }
                else if (ominaisuus.startsWith("y-peilaus=")) {
                    if (ominaisuus.substring(10).startsWith("kyllä")) {
                        this.yPeilaus = true;
                    }
                    else {
                        this.yPeilaus = false;
                    }
                }
                else if (ominaisuus.startsWith("peilaus=")) {
                    if (ominaisuus.substring(8).startsWith("xy")) {
                        this.xPeilaus = true;
                        this.yPeilaus = true;
                    }
                    else if (ominaisuus.substring(8).startsWith("x")) {
                        this.xPeilaus = true;
                    }
                    else if (ominaisuus.substring(8).startsWith("y")) {
                        this.yPeilaus = true;
                    }
                }
            }
        }
        else {
            this.lisäOminaisuudet = new ArrayList<>();
        }
        päivitäLisäOminaisuudet();
    }

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

    private void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet == null) this.lisäOminaisuudet = new ArrayList<>();

        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kääntö="));
        if (kääntöAsteet != 0) this.lisäOminaisuudet.add("kääntö=" + kääntöAsteet);
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("x-peilaus="));
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("y-peilaus="));
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("peilaus="));
        if (xPeilaus & yPeilaus) this.lisäOminaisuudet.add("peilaus=xy");
        else if (xPeilaus) this.lisäOminaisuudet.add("peilaus=x");
        else if (yPeilaus) this.lisäOminaisuudet.add("peilaus=y");
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

    public KuvaObjekti annaTekstuuriObjekti() {
        return tekstuuriObjekti;
    }
}
