package keimo.seikkailupeli;

import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.keimoengine.Kello;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.PeliTiedosto;
import keimo.seikkailupeli.assets.TavoiteLista;
import keimo.seikkailupeli.assets.huone.Huone;
import keimo.seikkailupeli.assets.huone.HuoneLista;
import keimo.seikkailupeli.gui.toimintoIkkunat.YhdistämisIkkuna;
import keimo.seikkailupeli.io.SyöteYhdistetty;
import keimo.seikkailupeli.kenttä.Maailma;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.Pelaaja.*;
import keimo.seikkailupeli.objektit.entityt.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Esine;
import keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC.NPC_KenttäKohde;
import keimo.seikkailupeli.objektit.kenttäkohteet.kerättävä.Kerättävä;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.KauppaHylly;
import keimo.seikkailupeli.objektit.kenttäkohteet.warp.Oviruutu;
import keimo.seikkailupeli.objektit.kenttäkohteet.warp.Warp;
import keimo.seikkailupeli.objektit.maastot.*;
import keimo.seikkailupeli.objektit.Suunnallinen.Suunta;
import keimo.seikkailupeli.ruudut.PeliRuutu;
import keimo.seikkailupeli.toiminnot.Dialogit;
import keimo.seikkailupeli.toiminnot.Vuorovaikutukset;
import keimo.seikkailupeli.äänet.Musat;
import keimo.seikkailupeli.äänet.Äänet;
import keimo.TarkistettavatArvot;

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
    public static boolean pause = true;
    public static boolean pauseDialogi = false;
    public static boolean valintaDialogi = false;
    //public static boolean peliAloitettu = false;
    public static int kentänKoko = 10;
    public static int kentänAlaraja = 0;
    public static int kentänYläraja = kentänAlaraja + kentänKoko - 1;

    public static boolean huoneVaihdettava = true;
    public static int uusiHuone = 0;
    private static HashMap<Integer, Huone> nykyinenHuoneKartta = new HashMap<Integer, Huone>();
    public static PeliTiedosto peliTiedosto;
    public static Huone huone;
    static String häviönSyy = "";
    public static int aloitusHp = 10;
    public static boolean latausValmis = false;

    int esineitäKentällä = 0;
    public static Pelaaja p = new Pelaaja();
    public static Object huoneenLatausLukko = new Object();
    public static Object grafiikanLatausLukko = new Object();
    public static boolean peliLäpäisty = false;
    public static boolean peliKäynnissä = true;
    public static boolean vaatiiUudelleenkäynnistyksen = false;
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
    public static boolean warpVasenPainettu = false;
    public static boolean warpOikeaPainettu = false;
    public static boolean warpAlasPainettu = false;
    public static boolean warpYlösPainettu = false;
    public static int warppiViive = 0;
    public static int dialoginAvausViive = 0;
    
    public static KenttäKohde[][] annaObjektiKenttä() {
        if (huone != null) return huone.annaHuoneenKenttäSisältö();
        else return null;
    }

    public static Maasto[][] annaMaastoKenttä() {
        return huone.annaHuoneenMaastoSisältö() ;
    }

    public static HashMap<Integer, Huone> annaHuoneKartta() {
        return nykyinenHuoneKartta;
    }

    public static List<Entity> entityLista = Collections.synchronizedList(new ArrayList<>());

    public enum Ruudut {
		PELIRUUTU,
		TARINARUUTU,
		VALIKKORUUTU,
        ASETUSRUUTU,
        KEHITTÄJÄRUUTU,
		LOPPURUUTU,
        EDITORIRUUTU,
        EDITORIRUUTU_VARMISTUS,
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
        YHDISTÄMINEN,
        HUIJAUSKOODIT,
        MINIPELI_3D,
        MINIPELI_PONG,
        MINIPELI_POKERI,
        MINIPELI_TETRIS,
        MINIPELI_4,
        MINIPELI_KEIMOÄLY;
    }
    public static ToimintoIkkunanTyyppi toimintoIkkuna;

    public enum SyöteLaitteet {
        NÄPPÄIMISTÖ,
        PELIOHJAIN;
    }
    public static SyöteLaitteet viimeisinSyöteLaite = SyöteLaitteet.NÄPPÄIMISTÖ;

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
                    Pelaaja.esineet[i] = (Esine)annaObjektiKenttä()[x][y];
                    Äänet.toistaSFX("Kerää", true);
                    break;
                }
            }
            valittuEsine = Pelaaja.esineet[esineValInt];
            annaObjektiKenttä()[x][y] = null;
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
            if (annaObjektiKenttä()[x][y] == null) {
                annaObjektiKenttä()[x][y] = Pelaaja.esineet[esineVal];
                Pelaaja.esineet[esineVal] = null;
                valittuEsine = null;
                Äänet.toistaSFX("Kerää", true, -1, 1, true);
            }
        }
        if (yhdistäminenKäynnissä) {
            yhdistäminenKäynnissä = false;
            yhdistettäväTavarapaikka = -1;
        }
    }

    public static void painaE(int x, int y) {
        if (annaObjektiKenttä()[x][y] instanceof Esine) {
            poimi(x, y);
        }
        else {
            vuorovaikutus(annaObjektiKenttä()[x][y], esineValInt);
        }
    }

    public static void painaQ(int x, int y) {
        if (annaObjektiKenttä()[x][y] instanceof KauppaHylly) {
            Pelaaja.poistaOstoskorista();
        }
        else {
            pudota(x, y, esineValInt);
        }
    }

    public static void painaZ() {
        if (yhdistäminenKäynnissä) {
            if (!(yhdistettäväTavarapaikka < 0)) {
                if (kokeileYhdistämistä(yhdistettäväTavarapaikka, esineValInt)) {
                    Pelaaja.esineet[esineValInt] = Esine.yhdistä2Esinettä(Pelaaja.esineet[esineValInt], Pelaaja.esineet[yhdistettäväTavarapaikka]);
                    Pelaaja.esineet[yhdistettäväTavarapaikka] = null;
                    Dialogit.avaaDialogi(Pelaaja.esineet[esineValInt].annaDialogiTekstuuri(), "Yhdistäminen onnistui! " + "Sait uuden esineen: " + Pelaaja.esineet[esineValInt].annaNimiSijamuodossa("nominatiivi"), "Yhdistäminen");
                }
                else {
                    Dialogit.avaaDialogi("Yhdistäminen ei onnistunut.", "Yhdistäminen");
                }
            }
            yhdistäminenKäynnissä = false;
            yhdistettäväTavarapaikka = -1;
        }    
        else if (tarkistaYhdistettävyys(esineValInt)) {
            yhdistettäväTavarapaikka = 0;
            yhdistäminenKäynnissä = true;
            YhdistämisIkkuna.avaaValikko();
        }
        else {
            if (Pelaaja.esineet[esineValInt] == null) {
                Dialogit.avaaDialogi("Ei valittua esinettä", "Yhdistäminen");
            }
            else {
                Dialogit.avaaDialogi(Pelaaja.esineet[esineValInt].annaDialogiTekstuuri(), Pelaaja.esineet[esineValInt].annaNimiSijamuodossa("partitiivi") + " ei voi yhdistää.", "Yhdistäminen");
            }
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

    public static void yhdistäValittuunEsineeseen(int yhdistysValinta) {
        if (!(yhdistysValinta < 0)) {
            if (Pelaaja.esineet[yhdistysValinta] != null) {
                if (!Pelaaja.esineet[yhdistysValinta].equals(Pelaaja.esineet[esineValInt])) {
                    if (kokeileYhdistämistä(yhdistysValinta, esineValInt)) {
                        YhdistämisIkkuna.näytäOnnistunutYhdistäminen(Pelaaja.esineet[esineValInt], Pelaaja.esineet[yhdistysValinta]);
                        Pelaaja.esineet[esineValInt] = Esine.yhdistä2Esinettä(Pelaaja.esineet[esineValInt], Pelaaja.esineet[yhdistysValinta]);
                        Pelaaja.esineet[yhdistysValinta] = null;
                    }
                    else {
                        Dialogit.avaaDialogi("Yhdistäminen ei onnistunut.", "Yhdistäminen");
                    }
                    yhdistäminenKäynnissä = false;
                    yhdistettäväTavarapaikka = -1;
                }
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
            Dialogit.avaaDialogi("Ei järjestettävää.", "Järjestäminen");
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
            Dialogit.avaaDialogi(Assets.annaTekstuuri("dialogi_järjestäminen"), "Tavaraluettelo järjestettiin.", "Järjestäminen");
        }
    }

    public static void käyttö(int esine) {
        valittuEsine = Pelaaja.esineet[esine];
        if (annaObjektiKenttä() != null) {
            KenttäKohde k = annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY];
            käytäEsinettä(k, esine);
        }
    }

    static void käytäEsinettä(KenttäKohde k, int esine) {
        Vuorovaikutukset.käytäEsinettä(k, valittuEsine);
    }

    static void vuorovaikutus(KenttäKohde k, int esine) {
        Vuorovaikutukset.vuorovaikuta(annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY], valittuEsine);
    }

    public static void katsoEsinettä() {
        Vuorovaikutukset.katsoEsinettä(valittuEsine);
    }

    public static void katsoKenttää() {
        KenttäKohde k = annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY];
        Vuorovaikutukset.katsoKenttää(k);
    }

    private static void warpTarkistaKentänReunat() {
        if (huone != null) {
            if (Pelaaja.hitbox.getMinX() <= 7 && huone.annaReunaWarppiTiedot(Suunta.VASEN)) {
                voiWarpataVasen = true;
            }
            else {
                voiWarpataVasen = false;
            }
            if (Pelaaja.hitbox.getMaxX() >= kentänKoko * Pelaaja.pelaajanKokoPx - 7 && huone.annaReunaWarppiTiedot(Suunta.OIKEA)) {
                voiWarpataOikea = true;
            }
            else {
                voiWarpataOikea = false;
            }
            if (Pelaaja.hitbox.getMaxY() >= kentänKoko * Pelaaja.pelaajanKokoPx - 7 && huone.annaReunaWarppiTiedot(Suunta.ALAS)) {
                voiWarpataAlas = true;
            }
            else {
                voiWarpataAlas = false;
            }
            if (Pelaaja.hitbox.getMinY() <= 7 && huone.annaReunaWarppiTiedot(Suunta.YLÖS)) {
                voiWarpataYlös = true;
            }
            else {
                voiWarpataYlös = false;
            }
        }
    }

    private static void warpTarkistaPainallukset() {
        if (Maailma.liikuVasemmalle) {
            warpVasenPainettu = true;
        }
        else warpVasenPainettu = false;
        if (Maailma.liikuOikealle) {
            warpOikeaPainettu = true;
        }
        else warpOikeaPainettu = false;
        if (Maailma.liikuAlas) {
            warpAlasPainettu = true;
        }
        else warpAlasPainettu = false;
        if (Maailma.liikuYlös) {
            warpYlösPainettu = true;
        }
        else warpYlösPainettu = false;
    }

    private static void warppaaKohteeseen() {
        if (warppiViive == 0) {
            if (voiWarpataVasen && warpVasenPainettu) {
                int kohdeHuoneenKoko = annaHuoneKartta().get(huone.annaReunaWarpinKohdeId(Suunta.VASEN)).annaKoko();
                tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(Suunta.VASEN), kohdeHuoneenKoko-1, Pelaaja.sijY, false);
            }
            else if (voiWarpataOikea && warpOikeaPainettu) {
                tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(Suunta.OIKEA), kentänAlaraja, Pelaaja.sijY, false);
            }
            else if (voiWarpataAlas && warpAlasPainettu) {
                tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(Suunta.ALAS), Pelaaja.sijX, kentänAlaraja, false);
            }
            else if (voiWarpataYlös && warpYlösPainettu) {
                int kohdeHuoneenKoko = annaHuoneKartta().get(huone.annaReunaWarpinKohdeId(Suunta.YLÖS)).annaKoko();
                tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(Suunta.YLÖS), Pelaaja.sijX, kohdeHuoneenKoko-1, false);
            }
            else if (annaObjektiKenttä() != null && annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY] instanceof Warp) {
                Warp warp = (Warp)annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY];
                if (
                    warp.annaSuunta() == Suunta.VASEN && warpVasenPainettu ||
                    warp.annaSuunta() == Suunta.OIKEA && warpOikeaPainettu ||
                    warp.annaSuunta() == Suunta.ALAS && warpAlasPainettu ||
                    warp.annaSuunta() == Suunta.YLÖS && warpYlösPainettu
                ) {
                    warp.ennenWarppia();
                    tarkistaWarpinTurvallisuus(warp.annaKohdeHuone(), warp.annaKohdeRuutuX(), warp.annaKohdeRuutuY(), true);
                    warp.warpinJälkeen();
                }
            }
        }
    }

    /**
     * Tarkista, minkä objektin kohdalla pelaaja on ja suorita siihen yhdistetty kohtaaminen automaattisesti.
     * Tämä mahdollistaa esim. keräämisen tai vahingon saamisen ilman vuorovaikutusta.
     */
    static void suoritaKohtaaminen() {
        if (annaObjektiKenttä() != null) {
            if (Pelaaja.sijX >= 0 && Pelaaja.sijX < annaObjektiKenttä().length && Pelaaja.sijY >= 0 && Pelaaja.sijY < annaObjektiKenttä().length) {
                if (annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY] instanceof NPC_KenttäKohde) {
                    NPCKohtaaminen();
                }
                else if (annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY] instanceof Oviruutu) {
                    voiWarpata = true;
                }
                else if (annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY] instanceof Kerättävä) {
                    Kerättävä k = (Kerättävä)annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY];
                    k.kerää();
                    annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY] = null;
                }
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
                TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.YLENSYÖNTI;
                siirryLoppuRuutuun(1);
            }

            if (Pelaaja.hp <= 0) {
                häviönSyy = "Sait selkääsi!";
                Pelaaja.keimonState = KeimonState.KUOLLUT;
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
        Kello.pysäytä(pauseta);
        pause = pauseta;
    }

    public static void siirryTarinaRuutuun(String tarina) {
        if (tarina.startsWith("koti")) TavoiteLista.suoritaPääTavoite(0);
        else if (tarina.startsWith("baari")) TavoiteLista.suoritaPääTavoite(5);
        else if (tarina.startsWith("metsä")) TavoiteLista.suoritaPääTavoite(7);
        else if (tarina.startsWith("temppeli")) TavoiteLista.suoritaPääTavoite(8);
        else if (tarina.startsWith("boss")) TavoiteLista.suoritaPääTavoite(9);
        Peli.pause = true;
        Renderöinti.siirrySeuraavaanRuutuun("tarinaruutu", tarina);
    }

    /**
     * Siirry loppuruutuun
     * @param sulkuTapa 0 = Voitto, 1 = Häviö
     */

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
        Renderöinti.siirrySeuraavaanRuutuun("loppuruutu", TarkistettavatArvot.pelinLoppuSyy);
    }

    public static void suljePeli() {
        peliKäynnissä = false;
        pause = true;
        boolean guit = false;
        for (int i = 0; i < Pelaaja.esineet.length; i++) {
            Pelaaja.esineet[i] = null;
        }
        Musat.suljeMusa();
        Kello.nollaa();
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
            if (annaHuoneKartta() != null) {
                if (annaHuoneKartta().get(huoneenId) != null) {
                    if (annaHuoneKartta().get(huoneenId).annaHuoneenMaastoSisältö()[kohteenX][kohteenY] != null) {
                        if ((annaHuoneKartta().get(huoneenId).annaHuoneenMaastoSisältö()[kohteenX][kohteenY].estääköLiikkumisen(null)) && estäEpäturvallisetWarpit) {
                            if (näytäHuomautus) {
                                Pelaaja.pakotaPelaajanPysäytys();
                                Dialogit.avaaDialogi("Warpin kohteessa on este tai kohde on kentän ulkopuolella.", "Warppaaminen epäonnistui");
                            }
                        }
                        else {
                            kokeileHuoneenLatausta(huoneenId, kohteenX, kohteenY);
                        }
                    }
                    else if (annaHuoneKartta().get(huoneenId).annaHuoneenKenttäSisältö()[kohteenX][kohteenY] != null) {
                        if (annaHuoneKartta().get(huoneenId).annaHuoneenKenttäSisältö()[kohteenX][kohteenY] instanceof VisuaalinenObjekti) {
                            VisuaalinenObjekti vo = (VisuaalinenObjekti)annaHuoneKartta().get(huoneenId).annaHuoneenKenttäSisältö()[kohteenX][kohteenY];
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
            return true;
        }
        else return false;
    }

    private static void kokeileHuoneenLatausta(int huoneenId, int kohteenX, int kohteenY) {
        synchronized(huoneenLatausLukko) {
            PeliRuutu.lataaHuone(huoneenId, kohteenX, kohteenY, false);
        }
    }

    public static void nollaaPainallukset() {
        SyöteYhdistetty.nollaaPainallukset();
        Pelaaja.pakotaPelaajanPysäytys();
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
        annaHuoneKartta().put(huoneenId, huone);
    }

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
            warpTarkistaKentänReunat();
            suoritaKohtaaminen();
            warpTarkistaPainallukset();
            warppaaKohteeseen();
            Maailma.liikutaPelaajaa();
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
            synchronized(huoneenLatausLukko) {
                System.out.println("Pelisäie ottaa lukon");
                PeliRuutu.lataaHuone(uusiHuone, Pelaaja.sijX, Pelaaja.sijY, true);
                boolean turvallinenRuutuLöydetty = false;
                if (huone != null && (Pelaaja.sijX >= huone.annaKoko() || Pelaaja.sijY >= huone.annaKoko())) {
                    Dialogit.avaaDialogi("Pelaajan nykyinen sijainti on uuden huoneen ulkopuolella. Pelaaja siirretään ensimmäiseen turvalliseen ruutuun.", "Pelaaja uuden huoneen ulkopuolella");
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
        peliKäynnissä = false;
        latausValmis = false;
        peliLäpäisty = false;
        pauseDialogi = false;
        valintaDialogi = false;
        uusiHuone = 0;
        huoneVaihdettava = true;
        esineValInt = 0;
        valittuEsine = null;
        Kello.nollaa();
        p = new Pelaaja();
        TarkistettavatArvot.pelinLoppuSyy = null;
    }

    public static void nollaaPeli() {
        peliKäynnissä = false;
        peliLäpäisty = false;
        pauseDialogi = false;
        valintaDialogi = false;
        uusiHuone = 0;
        huoneVaihdettava = true;
        esineValInt = 0;
        valittuEsine = null;
        syötteenTila = SyötteenTila.PELI;
        Kello.nollaa();
        p = new Pelaaja();
        HuoneLista.lataaReferenssiHuonekartta();
        huone = null;
        Pelaaja.teleporttaaSpawniin();
        TarkistettavatArvot.pelinLoppuSyy = null;
        TarkistettavatArvot.nollaa();
        TavoiteLista.nollaaTavoiteLista();
        Musat.suljeMusa();
    }
}