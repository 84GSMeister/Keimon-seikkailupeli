package keimo.kenttäkohteet;

import keimo.PääIkkuna;
import keimo.Ruudut.PeliRuutu;
import keimo.Utility.Käännettävä;
import keimo.keimoEngine.grafiikat.Kuva;
import keimo.keimoEngine.grafiikat.objekti3d.Transform3D;
import keimo.kenttäkohteet.avattavaEste.*;
import keimo.kenttäkohteet.warp.*;
import keimo.kenttäkohteet.kiintopiste.*;
import keimo.kenttäkohteet.esine.*;
import keimo.kenttäkohteet.triggeri.*;
import keimo.kenttäkohteet.kenttäNPC.*;
import keimo.kenttäkohteet.kerättävä.*;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.*;
import java.util.Random;
import javax.swing.Icon;

public abstract class KenttäKohde implements Käännettävä {
    
    boolean määritettySijainti = false;
    int sijX;
    int sijY;
    public boolean lisäOminaisuuksia = false;
    protected String[] lisäOminaisuudet;
    public Rectangle hitbox;

    public static String[] kenttäkohdeLista = {"Avain", "Hiili", "Huume", "Juhani", "Jumal Velho", "Jumal Yoda", "Kaasupullo", "Kaasusytytin", "Kauppahylly", "Kauppaovi", "Kaupparuutu", "Kauppias", "Kilpi", "Kirstu", "Koriste-esine", "Kuparilager", "Makkara", "Nappi", "Nuotio", "Painelaatta (pahavihu)", "Painelaatta (pikkuvihu)", "Paperi", "Pesäpallomaila", "Pontikka-ainekset", "Portti", "Pulloautomaatti", "Puuovi", "Oviruutu", "Seteli", "Silta", "Suklaalevy", "Sänky", "Vesiämpäri", "Ämpärikone"};

    protected int objektinId = 0;
    private static int seuraavaObjektinId = 0;
    
    public Transform3D transform = new Transform3D();

    protected boolean kolmiUlotteinen = false;
    public boolean onkoKolmiUlotteinen() {
        return kolmiUlotteinen;
    }

    protected String obj3dMallinTunniste;
    public String anna3dMallinTunniste() {
        return obj3dMallinTunniste;
    }

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

    protected boolean este = false;
    public boolean onkoEste() {
        return este;
    }

    protected int kääntöAsteet = 0;
    protected boolean xPeilaus = false;
    protected boolean yPeilaus = false;

    public int annaKääntöAsteet() {
        return kääntöAsteet;
    }

    public boolean annaXPeilaus() {
        return xPeilaus;
    }

    public boolean annaYPeilaus() {
        return yPeilaus;
    }

    Suunta suunta;
    
    BufferedImage käännettäväKuvake;
    
    protected String nimi;
    protected Icon kuvake;
    protected Icon dialogiKuvake;
    public boolean tavoiteSuoritettu = false;

    boolean vaatiiPäivityksen = true;

    protected String katsomisTeksti = "vakioteksti";
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

    public String haeDialogiTeksti(String teksti) {
        return katso();
    }

    public Icon annaKuvake() {
        return kuvake;
    }

    public String tiedostonNimi;
    public String annaKuvanTiedostoNimi() {
        return tiedostonNimi;
    }

    // protected String tekstuurinNimi;
    // public String annaTekstuurinNimi() {
    //     return tekstuurinNimi;
    // }

    protected Kuva tekstuuri;
    public Kuva annaTekstuuri() {
        return tekstuuri;
    }

    protected Kuva dialogiTekstuuri;
    public Kuva annaDialogiTekstuuri() {
        if (dialogiTekstuuri == null) return tekstuuri;
        else return dialogiTekstuuri;
    }

    public Icon annaDialogiKuvake() {
        if (dialogiKuvake == null) return kuvake;
        else return dialogiKuvake;
    }

    public void näytäDialogi(Esine e) {
        PääIkkuna.avaaDialogi(kuvake, katsomisTeksti, nimi);
    }

    public float liikeY = 0;
    public float annaLiikeY() {
        return liikeY;
    }

    protected float liikeNopeus = 4f;
    public float annaLiikeNopeus() {
        return liikeNopeus;
    }

    protected float pyörimisNopeus = 1f;
    public float annaPyörimisNopeus() {
        return pyörimisNopeus;
    }

    public static KenttäKohde luoObjektiTiedoilla(String objektinNimi, boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {

        KenttäKohde luotavaObjekti;

        switch (objektinNimi) {
            case "Avain":
                luotavaObjekti = new Avain(määritettySijainti, sijX, sijY);
                break;

            case "Baariruutu":
                luotavaObjekti = new BaariRuutu(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Baariovi":
                luotavaObjekti = new Baariovi(sijX, sijY, ominaisuusLista);
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

            case "Kalja-automaatti":
                luotavaObjekti = new KaljaAutomaatti(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Kartta":
                luotavaObjekti = new Kartta(määritettySijainti, sijX, sijY);
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

            case "Olutlasi":
                luotavaObjekti = new Olutlasi(määritettySijainti, sijX, sijY);
                break;

            case "Painelaatta":
                luotavaObjekti = new Painelaatta(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Painelaatta (pikkuvihu)":
                ominaisuusLista = new String[1];
                ominaisuusLista[0] = "vihollinen=Pikkuvihu";    
                luotavaObjekti = new Painelaatta(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;
            case "Painelaatta (pahavihu)":
                ominaisuusLista = new String[1];
                ominaisuusLista[0] = "vihollinen=Pahavihu";    
                luotavaObjekti = new Painelaatta(määritettySijainti, sijX, sijY, ominaisuusLista);
                break;

            case "Paperi":
                luotavaObjekti = new Paperi(määritettySijainti, sijX, sijY);
                break;

            case "Pasi":
                luotavaObjekti = new Pasi(määritettySijainti, sijX, sijY);
                break;

            case "Paskanmarjabooli":
                luotavaObjekti = new Paskanmarjabooli(määritettySijainti, sijX, sijY);
                break;

            case "Paskanmarjat":
                luotavaObjekti = new Paskanmarjat(määritettySijainti, sijX, sijY);
                break;

            case "Pelikone":
                luotavaObjekti = new Pelikone(määritettySijainti, sijX, sijY, ominaisuusLista);
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

            case "Sieni":
                luotavaObjekti = new Sieni(määritettySijainti, sijX, sijY);
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

            case "Tynnyri":
                luotavaObjekti = new Tynnyri(määritettySijainti, sijX, sijY);
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
    protected void asetaTiedot() {
        tiedot = "";
        tiedot += "Objektin ID: " + this.objektinId + "\n";
        tiedot += "Nimi: " + this.annaNimi() + "\n";
        //tiedot += "Satunnainen sijainti: " + (!this.määritettySijainti ? "Kyllä" : "Ei") + "\n";
        if (this.tiedostonNimi != null && this.tiedostonNimi.length() > 4) {
            //this.tekstuurinNimi = this.tiedostonNimi.substring(0, this.tiedostonNimi.length()-4);
        }

        if (this instanceof Esine) {
            tiedot += "Tyyppi: Esine";
            if (this instanceof Ruoka) {
                tiedot += ", Ruoka";
            }
            else if (this instanceof Juoma) {
                tiedot += ", Juoma";
            }
            else if (this instanceof Ase) {
                tiedot += ", Ase";
            }
            tiedot += "\n";

            Esine esine = (Esine)this;
            tiedot += "Kulutustavaraa: " + (esine.onkoKäyttö() ? "Kyllä" : "Ei") + "\n";
			if (esine.onkoKenttäkäyttöön()) {
                tiedot += "Sopii käytettäväksi: ";
                for (String s : esine.sopiiKäytettäväksi) {
                    tiedot += s + ", ";
                }
				tiedot = tiedot.substring(0, tiedot.length()-2);
                tiedot += "\n";
            }
            if (esine.onkoYhdistettävä()) {
                tiedot += "Sopii yhdistettäväksi: ";
                for (String s : esine.kelvollisetYhdistettävät) {
                    tiedot += s + ", ";
                }
				tiedot = tiedot.substring(0, tiedot.length()-2);
                tiedot += "\n";
            }
        }
        else if (this instanceof Kiintopiste) {
            tiedot += "Tyyppi: Kiintopiste";
            if (this instanceof Säiliö) {
                tiedot += ", Säiliö";
            }
            else if (this instanceof Lepopaikka) {
                tiedot += ", Lepopaikka";
            }
            tiedot += "\n";
        }

        else if (this instanceof NPC_KenttäKohde) {
            tiedot += "Tyyppi: Kenttä-NPC";
            tiedot += "\n";
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
            if (ae.annaVaaditutTriggerit() != null) {
                for (Point p : ae.annaVaaditutTriggerit()) {
                    tiedot += p.x + "," + p.y + "; ";
                }
            }
            tiedot += "\n";
        }
        else if (this instanceof Triggeri) {
            tiedot += "Tyyppi: Triggeri" + "\n";
            Triggeri trg = (Triggeri)this;
            if (trg.annaVaadittuEsine() != null) {
                tiedot += "Vaadittu esine: " + trg.annaVaadittuEsine().annaNimi() + "\n";
            }
            if (trg.annaVaadittuVihollinen() != null) {
                tiedot += "Vaadittu vihollinen: " + trg.annaVaadittuVihollinen().annaNimi() + "\n";
            }
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
        this.hitbox = new Rectangle(PeliRuutu.esineenKokoPx, PeliRuutu.esineenKokoPx);
        this.hitbox.setLocation(sijX * PeliRuutu.esineenKokoPx, sijY * PeliRuutu.esineenKokoPx);
        asetaTiedot();
    }
}
