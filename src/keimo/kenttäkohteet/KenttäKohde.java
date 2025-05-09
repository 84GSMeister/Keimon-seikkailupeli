package keimo.kenttäkohteet;

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
import java.util.ArrayList;
import java.util.Random;

public abstract class KenttäKohde implements Käännettävä {
    
    int sijX;
    int sijY;
    public boolean lisäOminaisuuksia = false;
    protected ArrayList<String> lisäOminaisuudet;
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
        Point sijainti = new Point(sijX * 64, sijY * 64);
        return sijainti;
    }

    public boolean onkolisäOminaisuuksia() {
        return lisäOminaisuuksia;
    }

    public ArrayList<String> annalisäOminaisuudet() {
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
    public boolean tavoiteSuoritettu = false;

    boolean vaatiiPäivityksen = true;

    protected String katsomisTeksti = "vakioteksti";
    public String katso() {
        return katsomisTeksti;
    }

    // public String kokeileEsinettä(Esine e) {
    //     return "Mitään ei tapahtunut!";
    // }

    public String annaNimi() {
        return nimi;
    }

    public abstract String annaNimiSijamuodossa(String sijamuoto);

    public String haeDialogiTeksti(String teksti) {
        return katso();
    }

    public String tiedostonNimi;
    public String annaKuvanTiedostoNimi() {
        return tiedostonNimi;
    }

    protected Kuva tekstuuri;
    public Kuva annaTekstuuri() {
        return tekstuuri;
    }

    protected Kuva dialogiTekstuuri;
    public Kuva annaDialogiTekstuuri() {
        if (dialogiTekstuuri == null) return tekstuuri;
        else return dialogiTekstuuri;
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

    public static KenttäKohde luoObjektiTiedoilla(String objektinNimi, int sijX, int sijY, ArrayList<String> ominaisuusLista) {

        KenttäKohde luotavaObjekti;

        switch (objektinNimi) {
            case "Avain":
                luotavaObjekti = new Avain(sijX, sijY);
                break;

            case "Baariruutu":
                luotavaObjekti = new BaariRuutu(sijX, sijY, ominaisuusLista);
                break;

            case "Baariovi":
                luotavaObjekti = new Baariovi(sijX, sijY, ominaisuusLista);
                break;

            case "Hiili":
                luotavaObjekti = new Hiili(sijX, sijY);
                break;

            case "Huume":
                luotavaObjekti = new Huume(sijX, sijY);
                break;

            case "Jallupullo":
                luotavaObjekti = new Jallupullo(sijX, sijY);
                break;

            case "Juhani":
                luotavaObjekti = new Juhani(sijX, sijY, ominaisuusLista);
                break;

            case "Jumal Velho":
                luotavaObjekti = new JumalVelho(sijX, sijY, ominaisuusLista);
                break;

            case "Jumal Yoda":
                luotavaObjekti = new JumalYoda(sijX, sijY, ominaisuusLista);
                break;

            case "Kaasupullo":
                luotavaObjekti = new Kaasupullo(sijX, sijY);
                break;

            case "Kaasusytytin":
                luotavaObjekti = new Kaasusytytin(sijX, sijY, ominaisuusLista);
                break;

            case "Kalja-automaatti":
                luotavaObjekti = new KaljaAutomaatti(sijX, sijY, ominaisuusLista);
                break;

            case "Kartta":
                luotavaObjekti = new Kartta(sijX, sijY);
                break;

            case "Kauppahylly":
                luotavaObjekti = new KauppaHylly(sijX, sijY, ominaisuusLista);
                break;

            case "Kauppaovi":
                luotavaObjekti = new Kauppaovi(sijX, sijY, ominaisuusLista);
                break;

            case "Kaupparuutu":
                luotavaObjekti = new KauppaRuutu(sijX, sijY, ominaisuusLista);
                break;

            case "Kauppias":
                luotavaObjekti = new Kauppias(sijX, sijY, ominaisuusLista);
                break;

            case "Kilpi":
                luotavaObjekti = new Kilpi(sijX, sijY);
                break;

            case "Kirstu":
                luotavaObjekti = new Kirstu(sijX, sijY, ominaisuusLista);
                break;

            case "Kolikko":
                luotavaObjekti = new Kolikko(sijX, sijY);
                break;

            case "Koriste-esine":
                luotavaObjekti = new VisuaalinenObjekti(sijX, sijY, ominaisuusLista);
                break;

            case "Kuparilager":
                luotavaObjekti = new Kuparilager(sijX, sijY);
                break;

            case "Kuuhahmo1":
                luotavaObjekti = new Kuuhahmo1(sijX, sijY, ominaisuusLista);
                break;

            case "Kuuhahmo2":
                luotavaObjekti = new Kuuhahmo2(sijX, sijY, ominaisuusLista);
                break;

            case "Kuuhahmo3":
                luotavaObjekti = new Kuuhahmo3(sijX, sijY, ominaisuusLista);
                break;

            case "KuuOlutlasi":
                luotavaObjekti = new KuuOlutlasi(sijX, sijY);
                break;

            case "Makkara":
                luotavaObjekti = new Makkara(sijX, sijY);
                break;

            case "Nappi":
                luotavaObjekti = new Nappi(sijX, sijY);
                break;

            case "Nuotio":
                luotavaObjekti = new Nuotio(sijX, sijY, ominaisuusLista);
                break;

            case "Olutlasi":
                luotavaObjekti = new Olutlasi(sijX, sijY);
                break;

            case "Painelaatta":
                luotavaObjekti = new Painelaatta(sijX, sijY, ominaisuusLista);
                break;

            case "Paperi":
                luotavaObjekti = new Paperi(sijX, sijY);
                break;

            case "Pasi":
                luotavaObjekti = new Pasi(sijX, sijY, ominaisuusLista);
                break;

            case "Paskanmarjabooli":
                luotavaObjekti = new Paskanmarjabooli(sijX, sijY);
                break;

            case "Paskanmarjat":
                luotavaObjekti = new Paskanmarjat(sijX, sijY);
                break;

            case "Pelikone":
                luotavaObjekti = new Pelikone(sijX, sijY, ominaisuusLista);
                break;

            case "Pelikone2":
                luotavaObjekti = new Pelikone2(sijX, sijY, ominaisuusLista);
                break;

            case "Penkki":
                luotavaObjekti = new Puistonpenkki(sijX, sijY, ominaisuusLista);
                break;

            case "Pesäpallomaila":
                luotavaObjekti = new Pesäpallomaila(sijX, sijY);
                break;

            case "Pontikka-ainekset":
                luotavaObjekti = new Ponuainekset(sijX, sijY);
                break;

            case "Portti":
                luotavaObjekti = new Portti(sijX, sijY, ominaisuusLista);
                break;

            case "Pulloautomaatti":
                luotavaObjekti = new Pulloautomaatti(sijX, sijY, ominaisuusLista);
                break;

            case "Puuovi":
                luotavaObjekti = new PuuOvi(sijX, sijY, ominaisuusLista);
                break;

            case "Oviruutu":
                luotavaObjekti = new Oviruutu(sijX, sijY, ominaisuusLista);
                break;

            case "Seteli":
                luotavaObjekti = new Seteli(sijX, sijY);
                break;

            case "Sieni":
                luotavaObjekti = new Sieni(sijX, sijY);
                break;

            case "Silta":
                luotavaObjekti = new Silta(sijX, sijY, ominaisuusLista);
                break;

            case "Suklaalevy":
                luotavaObjekti = new Suklaalevy(sijX, sijY);
                break;

            case "Sänky":
                luotavaObjekti = new Sänky(sijX, sijY, ominaisuusLista);
                break;

            case "Tynnyri":
                luotavaObjekti = new Tynnyri(sijX, sijY);
                break;

            case "Vesiämpäri":
                luotavaObjekti = new Vesiämpäri(sijX, sijY);
                break;

            case "Ämpärikone":
                luotavaObjekti = new Ämpärikone(sijX, sijY, ominaisuusLista);
                break;

            default:
                luotavaObjekti = null;
                break;
        }
        return luotavaObjekti;
    }

    public static KenttäKohde luoRandomKenttäKohde(int sijX, int sijY) {
        Random r = new Random();
        return luoObjektiTiedoilla(kenttäkohdeLista[r.nextInt(kenttäkohdeLista.length)], sijX, sijY, null);
    }

    String tiedot = "";
    protected void asetaTiedot() {
        tiedot = "";
        tiedot += "Objektin ID: " + this.objektinId + "\n";
        tiedot += "Nimi: " + this.annaNimi() + "\n";

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

    public KenttäKohde(int sijX, int sijY) {
        this.sijX = sijX;
        this.sijY = sijY;
        this.objektinId = seuraavaObjektinId;
        seuraavaObjektinId++;
        this.hitbox = new Rectangle(64, 64);
        this.hitbox.setLocation(sijX * 64, sijY * 64);
        asetaTiedot();
    }
}
