package keimo.seikkailupeli.objektit.kenttäkohteet;

import keimo.keimoengine.collision.Neliö;
import keimo.keimoengine.collision.Piste;
import keimo.keimoengine.grafiikat.objekti3d.Transform3D;
import keimo.seikkailupeli.assets.KuvaObjekti;
import keimo.seikkailupeli.objektit.PeliObjekti;
import keimo.seikkailupeli.objektit.kenttäkohteet.avattavaEste.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.kerättävä.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.triggeri.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.warp.*;

import java.util.ArrayList;
import java.util.Random;

public abstract class KenttäKohde extends PeliObjekti {
    
    public static String[] kenttäkohdeLista = {"Avain", "Baariovi", "Baariruutu", "Hiili", "Huume", "Jallupullo", "Juhani", "Jumal Velho", "Jumal Yoda", "Juomalasi", "Kaasupullo", "Kaasusytytin", "Kalja-automaatti", "Kartta", "Kauppahylly", "Kauppaovi", "Kaupparuutu", "Kauppias", "Kilpi", "Kirstu", "Kolikko", "Koriste-esine", "Koristeovi", "Kuparilager", "Kuuhahmo1", "Kuuhahmo2", "Kuuhahmo3", "Leivonta-ainekset", "Makkara", "Nappi", "Nuotio", "Oviruutu", "Painelaatta", "Paperi", "Pasi", "Paskanmarjabooli", "Paskanmarjat", "Pelikone", "Penkki", "Pesäpallomaila", "Portti", "Pulloautomaatti", "Puuovi", "Salaovi", "Seteli", "Sieni", "Silta", "Suklaalevy", "Sänky", "Tynnyri", "Vesiämpäri", "Ämpärikone"};

    protected int objektinId = 0;
    private static int seuraavaObjektinId = 0;
    public Transform3D transform = new Transform3D();
    protected boolean kolmiUlotteinen = false;
    protected String obj3dMallinTunniste;
    protected boolean este = false;
    public boolean tavoiteSuoritettu = false;
    boolean vaatiiPäivityksen = true;
    public float liikeY = 0;
    protected float liikeNopeus = 4f;
    protected float pyörimisNopeus = 1f;
    protected int animaatioFrame = 0;

    public KenttäKohde(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        this.sijX = sijX;
        this.sijY = sijY;
        this.objektinId = seuraavaObjektinId;
        seuraavaObjektinId++;
        this.hitbox = new Neliö(64, 64);
        this.hitbox.setLocation(sijX * 64, sijY * 64);
        this.tekstuuriObjekti = new KuvaObjekti(this.tekstuuri);
        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (this.lisäOminaisuudet == null) this.lisäOminaisuudet = new ArrayList<>();
                if (ominaisuus.startsWith("kääntö=")) {
                    try {
                        kääntöAsteet = Integer.parseInt(ominaisuus.substring(7));
                    }
                    catch (NumberFormatException e) {
                        System.out.println("virheellinen syöte: " + kääntöAsteet);
                        e.printStackTrace();
                        kääntöAsteet = 0;
                    }
                }
                else if (ominaisuus.startsWith("x-peilaus=")) {
                    if (ominaisuus.substring(10).startsWith("kyllä")) xPeilaus = true;
                    else xPeilaus = false;
                }
                else if (ominaisuus.startsWith("y-peilaus=")) {
                    if (ominaisuus.substring(10).startsWith("kyllä")) yPeilaus = true;
                    else yPeilaus = false;
                }
            }
            päivitäLisäOminaisuudet(ominaisuusLista);
        }
        else {
            this.lisäOminaisuuksia = false;
        }
        asetaTiedot();
    }

    public KenttäKohde(int sijX, int sijY) {
        this.sijX = sijX;
        this.sijY = sijY;
        this.objektinId = seuraavaObjektinId;
        seuraavaObjektinId++;
        this.hitbox = new Neliö(64, 64);
        this.hitbox.setLocation(sijX * 64, sijY * 64);
        asetaTiedot();
    }

    public boolean onkoKolmiUlotteinen() {
        return kolmiUlotteinen;
    }

    public String anna3dMallinTunniste() {
        return obj3dMallinTunniste;
    }

    public static void nollaaObjektiId() {
        seuraavaObjektinId = 0;
    }

    public boolean onkoEste() {
        return este;
    }

    public float annaLiikeY() {
        return liikeY;
    }

    public float annaLiikeNopeus() {
        return liikeNopeus;
    }

    public float annaPyörimisNopeus() {
        return pyörimisNopeus;
    }

    public int annaAnimaatioFrame() {
        return animaatioFrame;
    }

    public void kasvataFramea() {
        animaatioFrame++;
    }

    @Override
    public void päivitäLisäOminaisuudet(ArrayList<String> ominaisuusLista) {
        super.päivitäLisäOminaisuudet(ominaisuusLista);
    }

    public static KenttäKohde luoObjektiTiedoilla(String objektinNimi, int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        switch (objektinNimi) {
            case "Avain": return new Avain(sijX, sijY);
            case "Baariovi": return new Baariovi(sijX, sijY, ominaisuusLista);
            case "Baariruutu": return new BaariRuutu(sijX, sijY, ominaisuusLista);
            case "Hiili": return new Hiili(sijX, sijY);
            case "Huume": return new Huume(sijX, sijY);
            case "Jallupullo": return new Jallupullo(sijX, sijY);
            case "Juhani": return new Juhani(sijX, sijY, ominaisuusLista);
            case "Jumal Velho": return new JumalVelho(sijX, sijY, ominaisuusLista);
            case "Jumal Yoda": return new JumalYoda(sijX, sijY, ominaisuusLista);
            case "Juomalasi": return new Juomalasi(sijX, sijY, ominaisuusLista);
            case "Kaasupullo": return new Kaasupullo(sijX, sijY);
            case "Kaasusytytin": return new Kaasusytytin(sijX, sijY, ominaisuusLista);
            case "Kalja-automaatti": return new KaljaAutomaatti(sijX, sijY, ominaisuusLista);
            case "Kartta": return new Kartta(sijX, sijY);
            case "Kauppahylly": return new KauppaHylly(sijX, sijY, ominaisuusLista);
            case "Kauppaovi": return new Kauppaovi(sijX, sijY, ominaisuusLista);
            case "Kaupparuutu": return new KauppaRuutu(sijX, sijY, ominaisuusLista);
            case "Kauppias": return new Kauppias(sijX, sijY, ominaisuusLista);
            case "Kilpi": return new Kilpi(sijX, sijY);
            case "Kirstu": return new Kirstu(sijX, sijY, ominaisuusLista);
            case "Kolikko": return new Kolikko(sijX, sijY);
            case "Koriste-esine": return new VisuaalinenObjekti(sijX, sijY, ominaisuusLista);
            case "Koristeovi": return new KoristeOvi(sijX, sijY, ominaisuusLista);
            case "Kuparilager": return new Kuparilager(sijX, sijY);
            case "Kuuhahmo1": return new Kuuhahmo1(sijX, sijY, ominaisuusLista);
            case "Kuuhahmo2": return new Kuuhahmo2(sijX, sijY, ominaisuusLista);
            case "Kuuhahmo3": return new Kuuhahmo3(sijX, sijY, ominaisuusLista);
            case "Makkara": return new Makkara(sijX, sijY);
            case "Nappi": return new Nappi(sijX, sijY);
            case "Nuotio": return new Nuotio(sijX, sijY, ominaisuusLista);
            case "Painelaatta": return new Painelaatta(sijX, sijY, ominaisuusLista);
            case "Paperi": return new Paperi(sijX, sijY);
            case "Pasi": return new Pasi(sijX, sijY, ominaisuusLista);
            case "Paskanmarjabooli": return new Paskanmarjabooli(sijX, sijY);
            case "Paskanmarjat": return new Paskanmarjat(sijX, sijY);
            case "Pelikone": return new Pelikone(sijX, sijY, ominaisuusLista);
            case "Penkki": return new Puistonpenkki(sijX, sijY, ominaisuusLista);
            case "Pesäpallomaila": return new Pesäpallomaila(sijX, sijY);
            case "Pontikka-ainekset", "Leivonta-ainekset": return new Ponuainekset(sijX, sijY);
            case "Portti": return new Portti(sijX, sijY, ominaisuusLista);
            case "Pulloautomaatti": return new Pulloautomaatti(sijX, sijY, ominaisuusLista);
            case "Puuovi": return new PuuOvi(sijX, sijY, ominaisuusLista);
            case "Salaovi": return new Salaovi(sijX, sijY, ominaisuusLista);
            case "Oviruutu": return new Oviruutu(sijX, sijY, ominaisuusLista);
            case "Seteli": return new Seteli(sijX, sijY);
            case "Sieni": return new Sieni(sijX, sijY);
            case "Silta": return new Silta(sijX, sijY, ominaisuusLista);
            case "Suklaalevy": return new Suklaalevy(sijX, sijY);
            case "Sänky": return new Sänky(sijX, sijY, ominaisuusLista);
            case "Tynnyri": return new Tynnyri(sijX, sijY);
            case "Vesiämpäri": return new Vesiämpäri(sijX, sijY);
            case "Ämpärikone": return new Ämpärikone(sijX, sijY, ominaisuusLista);
            default: return null;
        }
    }

    public static KenttäKohde luoRandomKenttäKohde(int sijX, int sijY) {
        Random r = new Random();
        return luoObjektiTiedoilla(kenttäkohdeLista[r.nextInt(kenttäkohdeLista.length)], sijX, sijY, null);
    }

    String tiedot = "";
    protected void asetaTiedot() {
        tiedot = "";
        tiedot += "Objektin ID: " + this.objektinId + "\n";

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
            Warp warp = (Warp)this;
            tiedot += "Kohdehuone: " + warp.annaKohdeHuone() + "\n";
            tiedot += "Kohteen X-ruutu: " + warp.annaKohdeRuutuX() + "\n";
            tiedot += "Kohteen Y-ruutu: " + warp.annaKohdeRuutuY() + "\n";
        }
        else if (this instanceof VisuaalinenObjekti) {
            tiedot += "Tyyppi: Visuaalinen objekti" + "\n";
            VisuaalinenObjekti vo = (VisuaalinenObjekti)this;
            tiedot += "Estää liikkumisen: " + (vo.onkoEste() ? "kyllä" : "ei") + "\n";
            tiedot += "Kuva: " + vo.annaKuvanTiedostoNimi() + "\n";
        }
        else if (this instanceof AvattavaEste) {
            tiedot += "Tyyppi: Avattava este" + "\n";
            AvattavaEste ae = (AvattavaEste)this;
            tiedot += "Vaaditut triggerit: ";
            if (ae.annaVaaditutTriggerit() != null) {
                for (Piste p : ae.annaVaaditutTriggerit()) {
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

        tiedot += "Kääntö: " + this.annaKääntöAsteet() + "\n";
        tiedot += "X-peilaus: " + (this.annaXPeilaus() ? "kyllä" : "ei") + "\n";
        tiedot += "Y-peilaus: " + (this.annaYPeilaus() ? "kyllä" : "ei") + "\n";
    }
    
    public String annaTiedot() {
        return tiedot;
    }
}
