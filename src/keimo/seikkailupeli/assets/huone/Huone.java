package keimo.seikkailupeli.assets.huone;

import java.util.ArrayList;
import java.util.Random;

import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.objektit.Käännettävä.Suunta;
import keimo.seikkailupeli.objektit.entityt.*;
import keimo.seikkailupeli.objektit.entityt.npc.NPC;
import keimo.seikkailupeli.objektit.kenttäkohteet.*;
import keimo.seikkailupeli.objektit.maastot.*;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class Huone {
    
    private int id;
    private String nimi;
    private int huoneenKoko;
    private KenttäKohde[][] huoneenKenttäSisältö;
    private Maasto[][] huoneenMaastoSisältö;
    private Entity[][] huoneenNPCSisältö;
    private String taustanPolku;
    private String alue;
    int esineitäKentällä = 0;
    int maastoaKentällä = 0;
    int npcitäKentällä = 0;
    private String tarinaRuudunTunniste = null;
    protected String vaaditunTavoitteenTunniste = null;
    private boolean warpVasen = false;
    private boolean warpOikea = false;
    private boolean warpAlas = false;
    private boolean warpYlös = false;
    private int warpVasenHuoneId = 0;
    private int warpOikeaHuoneId = 0;
    private int warpAlasHuoneId = 0;
    private int warpYlösHuoneId = 0;
    private String musa;

    static Random r = new Random();

    public int annaId() {
        return id;
    }

    public String annaNimi() {
        return nimi;
    }

    public int annaKoko() {
        return huoneenKoko;
    }

    public String annaTaustanPolku() {
        return taustanPolku;
    }

    public String annaAlue() {
        return alue;
    }

    public boolean annaTarinaRuudunLataus() {
        if (tarinaRuudunTunniste == null || tarinaRuudunTunniste == "" || tarinaRuudunTunniste.equals("")) {
            return false;
        }
        else {
            return true;
        }
    }
    public boolean annaTavoiteVaatimus() {
        if (vaaditunTavoitteenTunniste == null || vaaditunTavoitteenTunniste == "" || vaaditunTavoitteenTunniste.equals("")) {
            return false;
        }
        else {
            return true;
        }
    }

    public String annaTarinaRuudunTunniste() {
        return tarinaRuudunTunniste;
    }

    public String annaVaaditunTavoitteenTunniste() {
        return vaaditunTavoitteenTunniste;
    }
    
    public KenttäKohde[][] annaHuoneenKenttäSisältö() {
        return huoneenKenttäSisältö;
    }

    public Maasto[][] annaHuoneenMaastoSisältö() {
        return huoneenMaastoSisältö;
    }

    public Entity[][] annaHuoneenNPCSisältö() {
        return huoneenNPCSisältö;
    }

    public String annaHuoneenMusa() {
        return musa;
    }

    public void päivitäNimiJaAlue(String nimi, String alue) {
        this.nimi = nimi;
        this.alue = alue;
    }

    public void päivitäTausta(String taustaString) {
        this.taustanPolku = taustaString;
    }

    public void päivitäMusa(String musaString) {
        this.musa = musaString;
    }

    public void päivitäHuoneenKenttäSisältö(KenttäKohde[][] k) {
        this.huoneenKenttäSisältö = k;
    }

    public void päivitäHuoneenMaastoSisältö(Maasto[][] m) {
        this.huoneenMaastoSisältö = m;
    }

    public void päivitäHuoneenNPCSisältö(NPC[][] n) {
        this.huoneenNPCSisältö = n;
    }

    public void päivitäAlkudialogi(String huoneenAlkuDialogiTeksti) {
        this.tarinaRuudunTunniste = huoneenAlkuDialogiTeksti;
        if (huoneenAlkuDialogiTeksti == null || huoneenAlkuDialogiTeksti == "") {
            this.tarinaRuudunTunniste = null;
        }
    }

    public void päivitäReunawarppienTiedot(boolean warpVasen, int warpVasenHuoneId, boolean warpOikea, int warpOikeaHuoneId, boolean warpAlas, int warpAlasHuoneId, boolean warpYlös, int warpYlösHuoneId) {
        this.warpVasen = warpVasen;
        this.warpVasenHuoneId = warpVasenHuoneId;
        this.warpOikea = warpOikea;
        this.warpOikeaHuoneId = warpOikeaHuoneId;
        this.warpAlas = warpAlas;
        this.warpAlasHuoneId = warpAlasHuoneId;
        this.warpYlös = warpYlös;
        this.warpYlösHuoneId = warpYlösHuoneId;
    }

    public boolean annaReunaWarppiTiedot(Suunta suunta) {
        switch (suunta) {
            case VASEN:
                return warpVasen;
            case OIKEA:
                return warpOikea;
            case YLÖS:
                return warpYlös;
            case ALAS:
                return warpAlas;
            default:
                return false;
        }
    }

    public int annaReunaWarpinKohdeId(Suunta suunta) {
        switch (suunta) {
            case VASEN:
                return warpVasenHuoneId;
            case OIKEA:
                return warpOikeaHuoneId;
            case YLÖS:
                return warpYlösHuoneId;
            case ALAS:
                return warpAlasHuoneId;
            default:
                return 0;
        }
    }

    /**
     * Arpoo satunnaisesti pelikentän x- ja y-koordinaatit.
     * Lisää arvottuun kohtaan syötteenä saadun KenttäKohde-tyyppisen olion
     * eli jonkin Esine-luokan tai Kiintopiste-luokan alaluokan olioista.
     * @param k KenttäKohde-objekti, jonka sijainti arvotaan
     * @.pre {k instanceof KenttäKohde}
     * @.post pelikenttä[randX][randY] != null
     */

    void sijoitaSatunnaiseenRuutuun(KenttäKohde k) {
        int randX = r.nextInt(Peli.kentänKoko);
        int randY = r.nextInt(Peli.kentänKoko);
        if (huoneenKenttäSisältö[randX][randY] == null) {
            huoneenKenttäSisältö[randX][randY] = k;
            esineitäKentällä++;
        }
        else {
            if (esineitäKentällä < Peli.kentänKoko * Peli.kentänKoko) {
                sijoitaSatunnaiseenRuutuun(k);
            }
        }
    }

    /**
     * Asettaa objektin pelikentälle x- ja y-koordinaatteihin.
     * Lisää valittuun kohtaan syötteenä saadun KenttäKohde-tyyppisen olion
     * eli jonkin Esine-luokan tai Kiintopiste-luokan alaluokan olioista.
     * @param k KenttäKohde-objekti, joka asetetaan kohteeseen
     * @param sijX x-koordinaatti
     * @param sijY y-koordinaatti
     * @.pre {k instanceof KenttäKohde && sijX >= 0 && sijX <= 9 && sijY >= 0 && sijY <= 9}
     * @.post pelikenttä[sijX][sijY] != null
     */

    void sijoitaMäärättyynRuutuun(int sijX, int sijY, KenttäKohde k) {
        if (sijX < huoneenKenttäSisältö.length && sijY < huoneenKenttäSisältö.length) {
            if (huoneenKenttäSisältö[sijX][sijY] == null) {
                huoneenKenttäSisältö[sijX][sijY] = k;
                esineitäKentällä++;
            }
            else {
                String viesti = "Ei voi sijoittaa " + k.annaNimiSijamuodossa("partitiivi") + " ruutuun (" + sijX + ", " + sijY + ") huoneessa " + this.id + ", sillä ruudussa on " + huoneenKenttäSisältö[sijX][sijY].annaNimiSijamuodossa("nominatiivi");
                tinyfd_messageBox("Virheellinen sijainti", viesti, "ok", "error", false);
            }
        }
        else {
            String viesti = "Ei voi sijoittaa " + k.annaNimiSijamuodossa("partitiivi") + " ruutuun (" + sijX + ", " + sijY + ") huoneessa " + this.id + ", sillä kentän koko on " + huoneenKenttäSisältö.length;
            viesti += "\n\nTarkista, että default.kst -tiedosto on yhteensopiva nykyisen pelin version kanssa, ja että sitä ei ole muokattu muuten, kuin pelinsisäisellä editorilla.";
            tinyfd_messageBox("Virheellinen sijainti", viesti, "ok", "error", false);
        }
    }

    void sijoitaMäärättyynRuutuun(int sijX, int sijY, Maasto m) {
        if (sijX < huoneenMaastoSisältö.length && sijY < huoneenMaastoSisältö.length) {
            if (huoneenMaastoSisältö[sijX][sijY] == null) {
                huoneenMaastoSisältö[sijX][sijY] = m;
                maastoaKentällä++;
            }
            else {
                String viesti = "Ei voi sijoittaa " + m.annaNimiSijamuodossa("partitiivi") + " (kuva: " + m.annaKuvanTiedostoNimi() + ") ruutuun (" + " ruutuun (" + sijX + ", " + sijY + ") huoneessa " + this.id + ", sillä ruudussa on " + huoneenMaastoSisältö[sijX][sijY].annaKuvanTiedostoNimi();
                tinyfd_messageBox("Virheellinen sijainti", viesti, "ok", "error", false);
            }
        }
        else {
            String kuvanNimi = "";
            if (m.annaLisäOminaisuudet() != null) {
                for (String s : m.annaLisäOminaisuudet()) {
                    if (s.startsWith("kuva=")) {
                        kuvanNimi = s.substring(5);
                    }
                }
            }
            String viesti = "Ei voi sijoittaa " + m.annaNimiSijamuodossa("partitiivi") + " (kuva: " + kuvanNimi + ") ruutuun (" + sijX + ", " + sijY + ") huoneessa " + this.id + ", sillä kentän koko on " + huoneenMaastoSisältö.length;
            viesti += "\n\nTarkista, että default.kst -tiedosto on yhteensopiva nykyisen pelin version kanssa, ja että sitä ei ole muokattu muuten, kuin pelinsisäisellä editorilla.";
            tinyfd_messageBox("Virheellinen sijainti", viesti, "ok", "error", false);
        }
    }

    void sijoitaMäärättyynRuutuun(int sijX, int sijY, Entity n) {
        // if (huoneenNPCSisältö[sijX][sijY] == null) {
        //     huoneenNPCSisältö[sijX][sijY] = n;
        //     npcitäKentällä++;
        // }
        // else {
        //     String viesti = "Ei voi sijoittaa " + n.annaNimi()+ " ruutuun (" + sijX + ", " + sijY + ") huoneessa " + this.id + ", sillä kentän koko on " + huoneenNPCSisältö.length;
        //     viesti += "\n\nTarkista, että default.kst -tiedosto on yhteensopiva nykyisen pelin version kanssa, ja että sitä ei ole muokattu muuten, kuin pelinsisäisellä editorilla.";
        //     tinyfd_messageBox("Virheellinen sijainti", viesti, "ok", "error", false);
        // }
        if (sijX < huoneenNPCSisältö.length && sijY < huoneenNPCSisältö.length) {
            if (huoneenNPCSisältö[sijX][sijY] == null) {
                huoneenNPCSisältö[sijX][sijY] = n;
                esineitäKentällä++;
            }
            else {
                String viesti = "Ei voi sijoittaa " + n.annaNimiSijamuodossa("partitiivi") + " ruutuun (" + sijX + ", " + sijY + ") huoneessa " + this.id + ", sillä ruudussa on " + huoneenNPCSisältö[sijX][sijY].annaNimiSijamuodossa("nominatiivi");
                tinyfd_messageBox("Virheellinen sijainti", viesti, "ok", "error", false);
            }
        }
        else {
            String viesti = "Ei voi sijoittaa " + n.annaNimiSijamuodossa("partitiivi") + " ruutuun (" + sijX + ", " + sijY + ") huoneessa " + this.id + ", sillä kentän koko on " + huoneenNPCSisältö.length;
            viesti += "\n\nTarkista, että default.kst -tiedosto on yhteensopiva nykyisen pelin version kanssa, ja että sitä ei ole muokattu muuten, kuin pelinsisäisellä editorilla.";
            tinyfd_messageBox("Virheellinen sijainti", viesti, "ok", "error", false);
        }
    }

    public Huone(int luontiId, int luontiKoko, String luontiNimi, String luontiTaustanPolku, String luontiAlue, ArrayList<KenttäKohde> luontiKenttäSisältö, ArrayList<Maasto> luontiMaastoSisältö, ArrayList<Entity> luontiNPCSisältö, String musa, String tarinaRuudunTunniste, String vaaditunTavoitteenTunniste) {
        switch (luontiKoko) {
            default:
                this.id = luontiId;
                this.nimi = luontiNimi;
                this.huoneenKoko = luontiKoko;
                this.huoneenKenttäSisältö = new KenttäKohde[luontiKoko][luontiKoko];
                this.huoneenMaastoSisältö = new Maasto[luontiKoko][luontiKoko];
                this.huoneenNPCSisältö = new Entity[luontiKoko][luontiKoko];
                this.taustanPolku = luontiTaustanPolku;
                this.tarinaRuudunTunniste = tarinaRuudunTunniste;
                this.vaaditunTavoitteenTunniste = vaaditunTavoitteenTunniste;
                this.alue = luontiAlue;
                this.musa = musa;
        
                try {
                    for (int i = 0; i < huoneenKenttäSisältö.length; i++) {
                        for (int j = 0; j < huoneenKenttäSisältö.length; j++) {
                            this.huoneenKenttäSisältö[j][i] = null;
                            this.huoneenMaastoSisältö[j][i] = null;
                        }
                    }
                    for (KenttäKohde k : luontiKenttäSisältö) {
                        if (k != null) {
                            sijoitaMäärättyynRuutuun(k.annaSijX(), k.annaSijY(), k);
                        }
                    }
                    for (Maasto m : luontiMaastoSisältö) {
                        if (m != null) {
                            sijoitaMäärättyynRuutuun(m.annaSijX(), m.annaSijY(), m);
                        }
                    }
                    for (Entity n : luontiNPCSisältö) {
                        if (n != null) {
                            sijoitaMäärättyynRuutuun(n.annaAlkuSijX(), n.annaAlkuSijY(), n);
                        }
                    }
                }
                catch (NullPointerException e) {
                    if (luontiKenttäSisältö == null) {
                        System.out.println("Kenttäkohteita ei voitu ladata tiedostosta huoneeseen " + id + ".");
                    }
                    else if (luontiMaastoSisältö == null) {
                        System.out.println("Maastoa ei voitu ladata tiedostosta huoneeseen " + id + ".");
                    }
                    else if (luontiNPCSisältö == null) {
                        System.out.println("NPC:itä ei voitu ladata tiedostosta huoneeseen " + id + ".");
                    }
                    else {
                        System.out.println("Joitain elementtejä ei voitu ladata tiedostosta huoneeseen " + id + ".");
                        e.printStackTrace();
                    }
                }
            break;
        }
        
    }

}
