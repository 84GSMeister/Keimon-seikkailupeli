package keimo;

import keimo.Maastot.*;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.entityt.*;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.keimoEngine.toiminnot.Vuorovaikutukset;
import keimo.keimoEngine.äänet.Musat;
import keimo.keimoEngine.äänet.Äänet;
import keimo.kenttäkohteet.*;
import keimo.kenttäkohteet.esine.Esine;
import keimo.kenttäkohteet.kenttäNPC.NPC_KenttäKohde;
import keimo.kenttäkohteet.kerättävä.Kerättävä;
import keimo.kenttäkohteet.warp.Oviruutu;
import keimo.kenttäkohteet.warp.Warp;
import keimo.Pelaaja.*;
import keimo.HuoneEditori.*;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;

public class Peli {
    
    /**
     * Puutteet sovelluksessa:
     *  -joitain luokkia ei kommentoitu
     *  -esineiden käytä() -metodeissa bugeja ja puuttuvia toteutuksia
     */
    public static KeimoEngine engine;
    public static boolean pause = true;
    public static boolean pauseDialogi = false;
    protected static double pauseAlkuAika = 0;
    protected static double pauseLoppuAika = 0;
    public static boolean ajastinPysäytetty = false;
    public static boolean valintaDialogi = false;
    public static boolean peliAloitettu = false;
    public static boolean asetuksetAuki = false;
    public static int kentänKoko = 10;
    public static int kentänAlaraja = 0;
    public static int kentänYläraja = kentänAlaraja + kentänKoko - 1;
    public static KenttäKohde[][] pelikenttä = new KenttäKohde[kentänKoko][kentänKoko];
    public static Maasto[][] maastokenttä = new Maasto[kentänKoko][kentänKoko];

    public static boolean huoneVaihdettava = true;
    public static int uusiHuone = 0;
    public static HashMap<Integer, Huone> huoneKartta = new HashMap<Integer, Huone>();
    public static Huone huone;
    static String häviönSyy = "";
    static int aloitusHp = 10;
    public static boolean latausValmis = false;

    int esineitäKentällä = 0;
    public static Pelaaja p = new Pelaaja();
    public static Object huoneenLatausLukko = new Object();
    public static Object grafiikanLatausLukko = new Object();
    public static boolean peliLäpäisty = false;
    public static boolean peliKäynnissä = true;
    static boolean peliHävitty = false;
    public static Esine valittuEsine;
    public static int esineValInt = 0;
    public static int yhdistettäväTavarapaikka = -1;
    public static boolean yhdistäminenKäynnissä = false;
    static boolean voiWarpata = false;
    static boolean estäEpäturvallisetWarpit = true;
    public static boolean voiWarpataVasen = false;
    public static boolean voiWarpataOikea = false;
    public static boolean voiWarpataAlas = false;
    public static boolean voiWarpataYlös = false;
    
    public static KenttäKohde[][] annaObjektiKenttä() {
        return pelikenttä;
    }

    public static Maasto[][] annaMaastoKenttä() {
        return maastokenttä;
    }

    public static List<Entity> entityLista = Collections.synchronizedList(new ArrayList<>());

    public enum Ruudut {
		PELIRUUTU,
		TARINARUUTU,
		VALIKKORUUTU,
        ASETUSRUUTU,
        ASETUSRUUTU_GRAFIIKKA,
        ASETUSRUUTU_ÄÄNI,
        ASETUSRUUTU_PELI,
        ASETUSRUUTU_ÄÄNITESTI,
        KEHITTÄJÄRUUTU,
		LOPPURUUTU,
        MINIPELIRUUTU,
        VIRHERUUTU;
	}
	public static Ruudut aktiivinenRuutu;

    public enum SyötteenTila {
        PELI,
        DIALOGI,
        TOIMINTO;
    }
    public static SyötteenTila syötteenTila = SyötteenTila.PELI;

    public enum ToimintoIkkunanTyyppi {
        PULLONPALAUTUS,
        VALINTADIALOGI,
        ÄMPÄRIJONO,
        KARTTA,
        OHJEET,
        MINIPELI;
    }
    public static ToimintoIkkunanTyyppi toimintoIkkuna;

    /**
     * Poimii esineen kentältä tavaraluetteloon.
     * Lisää kentältä valitun esineen tavaraluetteloon ensimmäiseen vapaaseen paikkaan.
     * Poistaa kentällä olevan esineen (muuttaa null-arvoksi).
     * Jos tavaraluettelo on täynnä, tulostaa virheviestin eikä poimi esinettä.
     * @.pre {
     * @param x < kentanKoko && x >= 0
     * @param y < kentanKoko && y >= 0
     * }
     * @.post true
     */

    public static void poimi(int x, int y) {
        if (Pelaaja.annaEsineidenMäärä() < Pelaaja.annaTavaraluettelonKoko()) {
            for (int i = 0; i < Pelaaja.esineet.length; i++) {
                if (Pelaaja.esineet[i] == null) {
                    Pelaaja.esineet[i] = (Esine)pelikenttä[x][y];
                    Äänet.toistaSFX("Kerää");
                    break;
                }
            }
            valittuEsine = Pelaaja.esineet[esineValInt];
            pelikenttä[x][y] = null;
            TavoiteLista.tarkistaTavoiteEsine(valittuEsine);
        }
    }

    /**
     * Pudottaa valitun esineen tavaraluettelosta kentälle pelaajan sijaintiin.
     * Muuttaa valitun esineen tavaraluettelossa null-arvoksi.
     * Jos valittu kohde kentällä ei ole tyhjä (null), tulostaa virheviestin eikä pudota esinettä.
     * @.pre {
     * @param x < p.esineet.length && x >= 0
     * @param y < p.esineet.length && y >= 0
     * @param esineVal < p.esineet.length && esineVal >= 0
     * }
     * @.post true
     */

    public static void pudota(int x, int y, int esineVal) {
        if (Pelaaja.esineet[esineVal] != null) {
            if (pelikenttä[x][y] == null) {
                pelikenttä[x][y] = Pelaaja.esineet[esineVal];
                Pelaaja.esineet[esineVal] = null;
                valittuEsine = null;
                Äänet.toistaSFX("Pudota");
            }
        }
        if (yhdistäminenKäynnissä) {
            yhdistäminenKäynnissä = false;
            yhdistettäväTavarapaikka = -1;
        }
    }

    public static void painaE(int x, int y) {
        if (pelikenttä[x][y] instanceof Esine) {
            poimi(x, y);
        }
        else {
            vuorovaikutus(pelikenttä[x][y], esineValInt);
        }
    }

    /*
     * Tarkista onko esine yhdistettävä
     */
    public static boolean tarkistaYhdistettävyys(int valinta) {
        if (Pelaaja.esineet[valinta] == null) {
            return false;
        }
        else if (!Pelaaja.esineet[valinta].onkoYhdistettävä()) {
            return false;
        }
        else {
            return true;
        }
    }

    /*
     * Tarkistaa sisältääkö valitun esineen lista yhdistyksen kohteena olevan esineen
     * Tarkistaa ettei valinta ole tyhjä (null)
     */
    public static boolean kokeileYhdistämistä(int val1, int val2) {
        if (Pelaaja.esineet[val2] == null) {
            return false;
        }
        else {
            if (Pelaaja.esineet[val1].kelvollisetYhdistettävät.contains(Pelaaja.esineet[val2].annaNimi()) && Pelaaja.esineet[val1].onkoYhdistettävä() && Pelaaja.esineet[val2].onkoYhdistettävä()) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    //Järjestää tavaraluettelon aakkosjärjestykseen
    static void järjestäUudelleen() {
        ArrayList<Esine> esineLista = new ArrayList<Esine>();
        for (int i = 0; i < Pelaaja.esineet.length; i++) {
            if (Pelaaja.esineet[i] != null) {
                esineLista.add(Pelaaja.esineet[i]);
            }
        }
        if (esineLista.size() < 1) {
            Dialogit.avaaDialogi("", "Ei järjestettävää.", "Järjestäminen");
        }
        else {
            Collections.sort(esineLista, new Comparator<Esine>() {
                public int compare(Esine e1, Esine e2) {
                    return e1.annaNimi().compareTo(e2.annaNimi());
                }
            });
            for (int i = 0; i < Pelaaja.esineet.length; i++) {
                Pelaaja.esineet[i] = null;
            }
            for (int i = 0; i < esineLista.size(); i++) {
                Pelaaja.esineet[i] = esineLista.get(i);
            }
            valittuEsine = Pelaaja.esineet[esineValInt];
            Dialogit.avaaDialogi("tiedostot/kuvat/hud/järjestäminen.png", "Tavaraluettelo järjestettiin.", "Järjestäminen");
        }
    }

    public static void käyttö(int esine) {
        valittuEsine = Pelaaja.esineet[esine];
        KenttäKohde k = pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
        käytäEsinettä(k, esine);
    }

    static void käytäEsinettä(KenttäKohde k, int esine) {
        Vuorovaikutukset.käytäEsinettä(k, valittuEsine);
    }

    static void vuorovaikutus(KenttäKohde k, int esine) {
        Vuorovaikutukset.vuorovaikuta(pelikenttä[Pelaaja.sijX][Pelaaja.sijY], valittuEsine);
    }

    public static void katsoEsinettä() {
        Vuorovaikutukset.katsoEsinettä(valittuEsine);
    }

    public static void katsoKenttää() {
        KenttäKohde k = pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
        Vuorovaikutukset.katsoKenttää(k);
    }

    static void suoritaReunanTarkistus() {
        if (huone != null) {
            if (Pelaaja.hitbox.getMinX() <= 7 && huone.annaReunaWarppiTiedot(KenttäKohde.Suunta.VASEN)) {
                voiWarpataVasen = true;
            }
            else {
                voiWarpataVasen = false;
            }
            if (Pelaaja.hitbox.getMaxX() >= kentänKoko * Pelaaja.pelaajanKokoPx - 7 && huone.annaReunaWarppiTiedot(KenttäKohde.Suunta.OIKEA)) {
                voiWarpataOikea = true;
            }
            else {
                voiWarpataOikea = false;
            }
            if (Pelaaja.hitbox.getMaxY() >= kentänKoko * Pelaaja.pelaajanKokoPx - 7 && huone.annaReunaWarppiTiedot(KenttäKohde.Suunta.ALAS)) {
                voiWarpataAlas = true;
            }
            else {
                voiWarpataAlas = false;
            }
            if (Pelaaja.hitbox.getMinY() <= 7 && huone.annaReunaWarppiTiedot(KenttäKohde.Suunta.YLÖS)) {
                voiWarpataYlös = true;
            }
            else {
                voiWarpataYlös = false;
            }
        }
    }

    /**
     * Tarkista, minkä objektin kohdalla pelaaja on ja suorita siihen yhdistetty kohtaaminen automaattisesti.
     * Tämä mahdollistaa esim. keräämisen tai vahingon saamisen ilman vuorovaikutusta.
     */
    static void suoritaKohtaaminen() {
        if (Pelaaja.sijX >= 0 && Pelaaja.sijX < pelikenttä.length && Pelaaja.sijY >= 0 && Pelaaja.sijY < pelikenttä.length) {
            if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof NPC_KenttäKohde) {
                NPCKohtaaminen();
            }
            else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Oviruutu) {
                voiWarpata = true;
            }
            else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Kerättävä) {
                Kerättävä k = (Kerättävä)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
                k.kerää();
                pelikenttä[Pelaaja.sijX][Pelaaja.sijY] = null;
            }
        }
    }

    static void NPCKohtaaminen() {

    }

    /** Tarkistetaan, onko pelin tavoitteet suoritettu.
     * Jos on, peli päättyy ja ikkuna sulkeutuu.
     */
    static boolean tarkistaPelinTila() {
        
        try {
            int suoritetutPääTavoitteet = TavoiteLista.tarkistaSuoritetutPääTavoitteet();
            
            if (suoritetutPääTavoitteet == TavoiteLista.pääTavoitteet.size()) {
                siirryLoppuRuutuun(0);
            }
            
            if (Pelaaja.syödytRuoat >= 4) {
                häviönSyy = "Söit liikaa ja sinulle tuli paha olo.";
                Pelaaja.keimonState = KeimonState.KUOLLUT;
                peliHävitty = true;
                TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.YLENSYÖNTI;
                siirryLoppuRuutuun(1);
            }

            if (Pelaaja.hp <= 0) {
                häviönSyy = "Sait selkääsi!";
                Pelaaja.keimonState = KeimonState.KUOLLUT;
                peliHävitty = true;
                siirryLoppuRuutuun(1);
            }
            return false;
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Virhe tavoitelistan tarkistuksessa");
            e.printStackTrace();
            return false;
        }
    }

    public static void pausetaPeli(boolean pauseta) {
        if (pauseta) {
            pauseAlkuAika = System.nanoTime();
        }
        else {
            pauseLoppuAika = System.nanoTime();
            aikaReferenssi += (pauseLoppuAika - pauseAlkuAika);
            asetuksetAuki = false;
        }
        pause = pauseta;
        ajastinPysäytetty = pauseta;
    }

    public static void siirryTarinaRuutuun(String tarina) {
        if (tarina.startsWith("koti")) TavoiteLista.suoritaPääTavoite(0);
        else if (tarina.startsWith("baari")) TavoiteLista.suoritaPääTavoite(4);
        else if (tarina.startsWith("metsä")) TavoiteLista.suoritaPääTavoite(6);
        else if (tarina.startsWith("temppeli")) TavoiteLista.suoritaPääTavoite(7);
        else if (tarina.startsWith("boss")) TavoiteLista.suoritaPääTavoite(8);
        Peli.pause = true;
        KeimoEngine.lataaTarinaRuutu(tarina);
    }

    static void siirryLoppuRuutuun(int sulkuTapa) {
        peliKäynnissä = false;
        pause = true;
        for (int i = 0; i < Pelaaja.esineet.length; i++) {
            Pelaaja.esineet[i] = null;
        }
        switch (sulkuTapa) {
            case 0:
                TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.NORMAALI_VOITTO;
                break;
            case 1:
                break;
            default:
                break;
        }
        KeimoEngine.lataaLoppuRuutu(TarkistettavatArvot.pelinLoppuSyy);
    }

    public static void suljePeli() {
        peliKäynnissä = false;
        pause = true;
        boolean guit = false;
        for (int i = 0; i < Pelaaja.esineet.length; i++) {
            Pelaaja.esineet[i] = null;
        }
        Musat.suljeMusa();
        globaaliTickit = 0;
        if (guit) {
            System.exit(0);
        }
    }

    /**
     * Tarkista, että warpin kohteessa olevassa ruudussa ei ole estettä (EsteTile-objekti maastokentässä).
     * @param huoneenId
     * @param kohteenX
     * @param kohteenY
     * @param näytäHuomautus
     */

    static void tarkistaWarpinTurvallisuus(int huoneenId, int kohteenX, int kohteenY, boolean näytäHuomautus) {
        try {
            if (huoneKartta != null) {
                if (huoneKartta.get(huoneenId) != null) {
                    if (huoneKartta.get(huoneenId).annaHuoneenMaastoSisältö()[kohteenX][kohteenY] != null) {
                        if ((huoneKartta.get(huoneenId).annaHuoneenMaastoSisältö()[kohteenX][kohteenY].estääköLiikkumisen(null)) && estäEpäturvallisetWarpit) {
                            if (näytäHuomautus) {
                                Pelaaja.pakotaPelaajanPysäytys();
                                Dialogit.avaaDialogi("", "Warpin kohteessa on este tai kohde on kentän ulkopuolella.", "Warppaaminen epäonnistui");
                            }
                        }
                        else {
                            kokeileHuoneenLatausta(huoneenId, kohteenX, kohteenY);
                        }
                    }
                    else if (huoneKartta.get(huoneenId).annaHuoneenKenttäSisältö()[kohteenX][kohteenY] != null) {
                        if (huoneKartta.get(huoneenId).annaHuoneenKenttäSisältö()[kohteenX][kohteenY] instanceof VisuaalinenObjekti) {
                            VisuaalinenObjekti vo = (VisuaalinenObjekti)huoneKartta.get(huoneenId).annaHuoneenKenttäSisältö()[kohteenX][kohteenY];
                            if (vo.onkoEste()) {
                                if (näytäHuomautus) {
                                    Pelaaja.pakotaPelaajanPysäytys();
                                }
                            }
                            else {
                                kokeileHuoneenLatausta(huoneenId, kohteenX, kohteenY);
                            }
                        }
                        else {
                            kokeileHuoneenLatausta(huoneenId, kohteenX, kohteenY);
                        }
                    }
                    else {
                        kokeileHuoneenLatausta(huoneenId, kohteenX, kohteenY);
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException ioobe) {
            ioobe.printStackTrace();
        }
    }

    public static boolean muutaKentänKokoa(int uusiKoko) {
        if (kentänKoko != uusiKoko) {
            kentänKoko = uusiKoko;
            kentänAlaraja = 0;
            kentänYläraja = kentänKoko - 1;
            pelikenttä = new KenttäKohde[kentänKoko][kentänKoko];
            maastokenttä = new Maasto[kentänKoko][kentänKoko];
            return true;
        }
        else return false;
    }

    private static void kokeileHuoneenLatausta(int huoneenId, int kohteenX, int kohteenY) {
        synchronized(huoneenLatausLukko) {
            engine.lataaHuone(huoneenId, kohteenX, kohteenY, false);
        }
    }

    /**
     * Luo uusi huone ja lisää se huonekarttaan (HashMappiin) ID:n perusteella.
     * @param huoneenId ID huonekartassa, jonka avulla peli voi ladata huoneen (pakollinen)
     * @param huoneenNimi huoneen nimi ei tarvitse olla uniikki (ei pakollinen)
     * @param huoneenTaustanPolku tiedostopolku merkkijonona, jos huoneella on tausta (ei pakollinen)
     * @param huoneenAlue tämä näkyy yläreunassa (ei pakollinen)
     * @param huoneenKenttäSisältö kaikki huoneen objektit Arraylist-listana (voi olla null)
     * @param huoneenMaastoSisältö kaikki huoneen maastotilet Arraylist-listana (voi olla null)
     * @param huoneenNPCSisältö kaikki huoneen NPC:t Arraylist-listana (voi olla null)
     * @param näytäAlkuDialogi legacy, aseta false
     * @param alkuDialogi legacy
     */

    public static void luoHuone(int huoneenId, int huoneenKoko, String huoneenNimi, String huoneenTaustanPolku, String huoneenAlue, ArrayList<KenttäKohde> huoneenKenttäSisältö, ArrayList<Maasto> huoneenMaastoSisältö, ArrayList<Entity> huoneenNPCSisältö, String musa, String alkuDialogi, String vaaditunTavoitteenTunniste) {
        Huone huone = new Huone(huoneenId, huoneenKoko, huoneenNimi, huoneenTaustanPolku, huoneenAlue, huoneenKenttäSisältö, huoneenMaastoSisältö, huoneenNPCSisältö, musa, alkuDialogi, vaaditunTavoitteenTunniste);
        huoneKartta.put(huoneenId, huone);
    }

    public static int warppiViive = 0;
    public static int dialoginAvausViive = 0;
    /**
     * Warppiviiveen tarkoitus on estää liian tiheät reunan yli warppaukset, jotta reunaan ei jää jumiin jatkuvaan warppilooppiin.
     * Warppiviivettä vähennetään yhdellä joka framessa.
     * Vakiowarppiviive on 20 framea
     */
    
    public static void vähennäKäyttöViivettä() {
        if (warppiViive > 0) {
            warppiViive--;
        }
        if (dialoginAvausViive > 0) {
            dialoginAvausViive--;
        }
    }

    /**
     * Simuloi pelaajan liike-tickejä.
     * Tarkista jokaisen liikkeen jälkeen, onko pelaaja warpin kohdalla (reunawarp tai Warp-objekti).
     * Jos on, tarkista voiko kohteeseen warpata.
     */
    public static void pelaajanLiike() {
        try {
            suoritaReunanTarkistus();
            suoritaKohtaaminen();
            if (warppiViive == 0) {
                if (voiWarpataVasen && (keimo.keimoEngine.io.NäppäinKomennot.vasenPainettu)) {
                    int kohdeHuoneenKoko = huoneKartta.get(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.VASEN)).annaKoko();
                    tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.VASEN), kohdeHuoneenKoko-1, Pelaaja.sijY, false);
                }
                else if (voiWarpataOikea && (keimo.keimoEngine.io.NäppäinKomennot.oikeaPainettu)) {
                    tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.OIKEA), kentänAlaraja, Pelaaja.sijY, false);
                }
                else if (voiWarpataAlas && (keimo.keimoEngine.io.NäppäinKomennot.alasPainettu)) {
                    tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.ALAS), Pelaaja.sijX, kentänAlaraja, false);
                }
                else if (voiWarpataYlös && (keimo.keimoEngine.io.NäppäinKomennot.ylösPainettu)) {
                    int kohdeHuoneenKoko = huoneKartta.get(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.YLÖS)).annaKoko();
                    tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.YLÖS), Pelaaja.sijX, kohdeHuoneenKoko-1, false);
                }
                else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Warp) {
                    Warp warp = (Warp)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
                    if (
                        warp.annaSuunta() == KenttäKohde.Suunta.VASEN && (keimo.keimoEngine.io.NäppäinKomennot.vasenPainettu) ||
                        warp.annaSuunta() == KenttäKohde.Suunta.OIKEA && (keimo.keimoEngine.io.NäppäinKomennot.oikeaPainettu) ||
                        warp.annaSuunta() == KenttäKohde.Suunta.ALAS && (keimo.keimoEngine.io.NäppäinKomennot.alasPainettu) ||
                        warp.annaSuunta() == KenttäKohde.Suunta.YLÖS && (keimo.keimoEngine.io.NäppäinKomennot.ylösPainettu)
                    ) {
                        warp.ennenWarppia();
                        tarkistaWarpinTurvallisuus(warp.annaKohdeHuone(), warp.annaKohdeRuutuX(), warp.annaKohdeRuutuY(), true);
                        warp.warpinJälkeen();
                    }
                }
            }
            Pelaaja.pelaajaLiikkuu = false;
        }
        catch (NullPointerException npe) {
            System.out.println("Pelaajan liike epäonnistui.");
            npe.printStackTrace();
        }
        catch (IndexOutOfBoundsException ioobe) {
            System.out.println("Pelaajan liike epäonnistui.");
            ioobe.printStackTrace();
        }
    }

    /**
     * Tarkista pelin tilan muutokset kuten huoneen lataus, tarinaruutuun siirtyminen ja häviö
     */

    public static void pelinKulku() {

        if (huoneVaihdettava) {
            synchronized(Peli.huoneenLatausLukko) {
                System.out.println("Pelisäie ottaa lukon");
                engine.lataaHuone(uusiHuone, Pelaaja.sijX, Pelaaja.sijY, true);
                boolean turvallinenRuutuLöydetty = false;
                if (Peli.huone != null && (Pelaaja.sijX >= huone.annaKoko() || Pelaaja.sijY >= huone.annaKoko())) {
                    Dialogit.avaaDialogi("", "Pelaajan nykyinen sijainti on uuden huoneen ulkopuolella. Pelaaja siirretään ensimmäiseen turvalliseen ruutuun.", "Pelaaja uuden huoneen ulkopuolella");
                    for (int i = 0; i < huone.annaKoko(); i++) {
                        if (!turvallinenRuutuLöydetty) {
                            for (int j = 0; j < huone.annaKoko(); j++) {
                                if (huone.annaHuoneenMaastoSisältö()[j][i] != null) {
                                    if (!(huone.annaHuoneenMaastoSisältö()[j][i].estääköLiikkumisen(null))) {
                                        Pelaaja.teleport(j, i);
                                        turvallinenRuutuLöydetty = true;
                                        break;
                                    }
                                }
                                else if (huone.annaHuoneenKenttäSisältö()[j][i] != null) {
                                    if (huone.annaHuoneenKenttäSisältö()[j][i] instanceof VisuaalinenObjekti) {
                                        VisuaalinenObjekti vo = (VisuaalinenObjekti)huone.annaHuoneenKenttäSisältö()[j][i];
                                        if (!vo.onkoEste()) {
                                            Pelaaja.teleport(j, i);
                                            turvallinenRuutuLöydetty = true;
                                            break;
                                        } 
                                    }
                                    else {
                                        Pelaaja.teleport(j, i);
                                        turvallinenRuutuLöydetty = true;
                                        break;
                                    }
                                }
                                else {
                                    Pelaaja.teleport(j, i);
                                    turvallinenRuutuLöydetty = true;
                                    break;
                                }
                            }
                        }
                        else {
                            break;
                        }
                    }
                }
                huoneVaihdettava = false;
            }
            System.out.println("Pelisäie vapauttaa lukon");
        }

        tarkistaPelinTila();
        if (peliLäpäisty) {
            siirryLoppuRuutuun(0);
            peliKäynnissä = false;
        }
    }

    public static long globaaliTickit = 0;
    public static long aikaReferenssi = System.nanoTime();

    static boolean uusiKäynnistysYritys = false;

    public static void uusiPeli() {
        try {
            kentänKoko = TarkistettavatArvot.uusiKentänKoko;
            kentänYläraja = kentänAlaraja + kentänKoko - 1;
            pause = true;
            peliKäynnissä = false;
            TarkistettavatArvot.nollaa();
            luoPeli();
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Aloitusikkunaa ei voitu ladata. Peli on todennäköisesti yritetty käynnistää väärillä java-argumenteilla.\nTämä virhe voi tulla, jos peli on yritetty käynnistää jar-tiedostosta suoraan.\n\nVersio 0.8.2:sta eteenpäin mukana tulee exe-tiedosto, joka käynnistää pelin automaattisesti oikeilla argumenteilla.", "Virhe ladatessa peliä", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void luoPeli() {
        latausValmis = false;
        aikaReferenssi = System.nanoTime();
        peliAloitettu = false;
        peliLäpäisty = false;
        pauseDialogi = false;
        valintaDialogi = false;
        ajastinPysäytetty = false;
        uusiHuone = 0;
        huoneVaihdettava = true;

        engine = new KeimoEngine();
        engine.start();
        while (!KeimoEngine.glKäynnistetty) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        esineValInt = 0;
        valittuEsine = null;
        p = new Pelaaja();
        TarkistettavatArvot.pelinLoppuSyy = null;
    }

    public static void nollaaPeli() {
        aikaReferenssi = System.nanoTime();
        peliAloitettu = false;
        peliLäpäisty = false;
        pauseDialogi = false;
        valintaDialogi = false;
        ajastinPysäytetty = false;
        uusiHuone = 0;
        huoneVaihdettava = true;
        esineValInt = 0;
        valittuEsine = null;
        syötteenTila = SyötteenTila.PELI;
        p = new Pelaaja();
        HuoneLista.lataaReferenssiHuonekartta();
        Pelaaja.teleporttaaSpawniin();
        TarkistettavatArvot.pelinLoppuSyy = null;
        TarkistettavatArvot.nollaa();
        TavoiteLista.nollaaTavoiteLista();
        Musat.suljeMusa();
    }
}