package keimo.Kenttäkohteet;

import keimo.PääIkkuna;
import keimo.Ruudut.PeliRuutu;
import keimo.Utility.Käännettävä;

import java.awt.Point;
import java.awt.image.*;
import java.util.Random;
import javax.swing.Icon;

public abstract class KenttäKohde implements Käännettävä {
    
    boolean määritettySijainti = false;
    int sijX;
    int sijY;
    public boolean lisäOminaisuuksia = false;
    String[] lisäOminaisuudet;

    public static String[] kenttäkohdeLista = {"Avain", "Hiili", "Huume", "Juhani", "Jumal Velho", "Jumal Yoda", "Kaasupullo", "Kaasusytytin", "Kauppahylly", "Kauppaovi", "Kaupparuutu", "Kauppias", "Kilpi", "Kirstu", "Koriste-esine", "Kuparilager", "Makkara", "Nappi", "Nuotio", "Painelaatta (pahavihu)", "Painelaatta (pikkuvihu)", "Paperi", "Pesäpallomaila", "Pontikka-ainekset", "Portti", "Pulloautomaatti", "Puuovi", "Oviruutu", "Seteli", "Silta", "Suklaalevy", "Sänky", "Vesiämpäri", "Ämpärikone"};

    protected int objektinId = 0;
    private static int seuraavaObjektinId = 0;

    public static void nollaaObjektiId() {
        seuraavaObjektinId = 0;
    }

    public boolean onkoMääritettySijainti() {
        return määritettySijainti;
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
    public Point annaSijaintiKentällä() {
        Point sijainti = new Point(sijX * PeliRuutu.esineenKokoPx, sijY * PeliRuutu.esineenKokoPx);
        return sijainti;
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

    public abstract String annaNimiSijamuodossa(String sijamuoto);

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

    public static KenttäKohde luoObjektiTiedoilla(String objektinNimi, boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {

        KenttäKohde luotavaObjekti;

        switch (objektinNimi) {
            case "Avain":
                luotavaObjekti = new Avain(määritettySijainti, sijX, sijY);
                break;

            case "Hiili":
                luotavaObjekti = new Hiili(määritettySijainti, sijX, sijY);
                break;

            case "Huume":
                luotavaObjekti = new Huume(määritettySijainti, sijX, sijY);
                break;

            case "Jallupullo":
                luotavaObjekti = new Jallupullo(määritettySijainti, sijX, sijY);
                break;

            case "Juhani":
                luotavaObjekti = new Juhani(määritettySijainti, sijX, sijY);
                break;

            case "Jumal Velho":
                luotavaObjekti = new JumalVelho(määritettySijainti, sijX, sijY);
                break;

            case "Jumal Yoda":
                luotavaObjekti = new JumalYoda(määritettySijainti, sijX, sijY);
                break;

            case "Kaasupullo":
                luotavaObjekti = new Kaasupullo(määritettySijainti, sijX, sijY);
                break;

            case "Kaasusytytin":
                luotavaObjekti = new Kaasusytytin(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Kauppahylly":
                luotavaObjekti = new KauppaHylly(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Kauppaovi":
                luotavaObjekti = new Kauppaovi(sijX, sijY, ominaisuusLista);
                break;

            case "Kaupparuutu":
                luotavaObjekti = new KauppaRuutu(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Kauppias":
                luotavaObjekti = new Kauppias(määritettySijainti, sijX, sijY);
                break;

            case "Kilpi":
                luotavaObjekti = new Kilpi(määritettySijainti, sijX, sijY);
                break;

            case "Kirstu":
                luotavaObjekti = new Kirstu(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Kolikko":
                luotavaObjekti = new Kolikko(määritettySijainti, sijX, sijY);
                break;

            case "Koriste-esine":
                luotavaObjekti = new VisuaalinenObjekti(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Kuparilager":
                luotavaObjekti = new Kuparilager(määritettySijainti, sijX, sijY);
                break;

            case "Makkara":
                luotavaObjekti = new Makkara(määritettySijainti, sijX, sijY);
                break;

            case "Nappi":
                luotavaObjekti = new Nappi(määritettySijainti, sijX, sijY);
                break;

            case "Nuotio":
                luotavaObjekti = new Nuotio(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Painelaatta (pahavihu)":
                luotavaObjekti = new PainelaattaPahavihu(määritettySijainti, sijX, sijY);
                break;

            case "Painelaatta (pikkuvihu)":
                luotavaObjekti = new PainelaattaPikkuvihu(määritettySijainti, sijX, sijY);
                break;

            case "Paperi":
                luotavaObjekti = new Paperi(määritettySijainti, sijX, sijY);
                break;

            case "Penkki":
                luotavaObjekti = new Puistonpenkki(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Pesäpallomaila":
                luotavaObjekti = new Pesäpallomaila(määritettySijainti, sijX, sijY);
                break;

            case "Pontikka-ainekset":
                luotavaObjekti = new Ponuainekset(määritettySijainti, sijX, sijY);
                break;

            case "Portti":
                luotavaObjekti = new Portti(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Pulloautomaatti":
                luotavaObjekti = new Pulloautomaatti(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Puuovi":
                luotavaObjekti = new PuuOvi(sijX, sijY, ominaisuusLista);
                break;

            case "Oviruutu":
                luotavaObjekti = new Oviruutu(sijX, sijY, ominaisuusLista);
                break;

            case "Seteli":
                luotavaObjekti = new Seteli(määritettySijainti, sijX, sijY);
                break;

            case "Silta":
                luotavaObjekti = new Silta(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Suklaalevy":
                luotavaObjekti = new Suklaalevy(määritettySijainti, sijX, sijY);
                break;

            case "Sänky":
                luotavaObjekti = new Sänky(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Vesiämpäri":
                luotavaObjekti = new Vesiämpäri(määritettySijainti, sijX, sijY);
                break;

            case "Ämpärikone":
                luotavaObjekti = new Ämpärikone(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            default:
                luotavaObjekti = null;
                break;
        }
        return luotavaObjekti;
    }

    public static KenttäKohde luoRandomKenttäKohde(int sijX, int sijY) {
        Random r = new Random();
        return luoObjektiTiedoilla(kenttäkohdeLista[r.nextInt(kenttäkohdeLista.length)], true, sijX, sijY, null);
    }

    String tiedot = "";
    void asetaTiedot() {
        tiedot = "";
        tiedot += "Objektin ID: " + this.objektinId + "\n";
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
            tiedot += "Estää liikkumisen: " + (vo.onkoEste() ? "kyllä" : "ei") + "\n";
            tiedot += "Kuva: " + vo.annaKuvanTiedostoNimi() + "\n";
            tiedot += "Kääntö: " + vo.annaKääntöAsteet() + "\n";
            tiedot += "X-peilaus: " + (vo.annaXPeilaus() ? "kyllä" : "ei") + "\n";
            tiedot += "Y-peilaus: " + (vo.annaYPeilaus() ? "kyllä" : "ei") + "\n";
        }
        else if (this instanceof AvattavaEste) {
            tiedot += "Tyyppi: Avattava este" + "\n";
            AvattavaEste ae = (AvattavaEste)this;
            tiedot += "Vaaditut triggerit: ";
            for (Point p : ae.annaVaaditutTriggerit()) {
                tiedot += p.x + "," + p.y + "; ";
            }
            tiedot += "\n";
        }
        else if (this instanceof Triggeri) {
            tiedot += "Tyyppi: Triggeri" + "\n";
        }
        else if (this instanceof Kerättävä) {
            tiedot += "Tyyppi: Kerättävä" + "\n";
        }
    }
    
    public String annaTiedot() {
        return tiedot;
    }

    public KenttäKohde(boolean määritettySijainti, int sijX, int sijY) {
        this.määritettySijainti = määritettySijainti;
        this.sijX = sijX;
        this.sijY = sijY;
        this.objektinId = seuraavaObjektinId;
        seuraavaObjektinId++;
    }
}
