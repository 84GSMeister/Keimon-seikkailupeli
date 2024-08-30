package keimo;

import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;
import keimo.NPCt.*;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.Utility.KeimoFontit;
import keimo.Utility.KäännettäväKuvake;
import keimo.Utility.Käännettävä.Suunta;
import keimo.Pelaaja.*;
import keimo.PelinAsetukset.AjoitusMuoto;
import keimo.Ruudut.*;
import keimo.Ruudut.Lisäruudut.PullonPalautusRuutu;
import keimo.Ruudut.Lisäruudut.ValintaDialogiRuutu;
import keimo.Ruudut.Lisäruudut.ÄmpäriJonoRuutu;
import keimo.Säikeet.*;
import keimo.Ikkunat.*;
import keimo.HuoneEditori.*;
import keimo.HuoneEditori.DialogiEditori.VuoropuheDialogit;
import keimo.HuoneEditori.TarinaEditori.TarinaDialogiLista;
import keimo.HuoneEditori.TarinaEditori.TarinaPätkä;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.State;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Peli {
    
    /**
     * Puutteet sovelluksessa:
     *  -joitain luokkia ei kommentoitu
     *  -esineiden käytä() -metodeissa bugeja ja puuttuvia toteutuksia
     */
    
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

    int esineitäKentällä = 0;
    public static Pelaaja p = new Pelaaja();
    Scanner sc = new Scanner(System.in);
    public static Object huoneenLatausLukko = new Object();
    public static Object grafiikanLatausLukko = new Object();
    public static boolean peliLäpäisty = false;
    public static boolean peliKäynnissä = true;
    static boolean peliHävitty = false;
    public static Esine valittuEsine;
    public static int esineValInt = 0;
    static Näppäinkomennot nk = new Näppäinkomennot();
    static boolean voiWarpata = false;
    static boolean estäEpäturvallisetWarpit = true;
    static boolean voiWarpataVasen = false;
    static boolean voiWarpataOikea = false;
    static boolean voiWarpataAlas = false;
    static boolean voiWarpataYlös = false;
    
    public static KenttäKohde[][] annaObjektiKenttä() {
        return pelikenttä;
    }

    public static Maasto[][] annaMaastoKenttä() {
        return maastokenttä;
    }

    protected static GrafiikanPäivitysSäie grafiikkaSäie = new GrafiikanPäivitysSäie();
    protected static PeliSäie peliSäie = new PeliSäie();
    public static ÄänentoistamisSäie ääniSäie = new ÄänentoistamisSäie();

    public static List<Entity> entityLista = Collections.synchronizedList(new ArrayList<>());

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

    static void poimi(int x, int y){
        if (pelikenttä[x][y] instanceof Esine){
            if (Pelaaja.annaEsineidenMäärä() < Pelaaja.annaTavaraluettelonKoko()) {
                for (int i = 0; i < Pelaaja.esineet.length; i++) {
                    if (Pelaaja.esineet[i] == null) {
                        Pelaaja.esineet[i] = (Esine)pelikenttä[x][y];
                        ÄänentoistamisSäie.toistaSFX("Kerää");
                        break;
                    }
                }
                valittuEsine = Pelaaja.esineet[esineValInt];
                PääIkkuna.hudTeksti.setText("Sait uuden esineen: " + pelikenttä[x][y].annaNimiSijamuodossa("nominatiivi"));
                pelikenttä[x][y] = null;
                TavoiteLista.tarkistaTavoiteEsine(valittuEsine);
            }
            else {
                PääIkkuna.hudTeksti.setText("Ei voida poimia! Tavaraluettelo täynnä! Kokeile pudottaa jokin esine tyhjään ruutuun");
            }
        }
        else if (pelikenttä[x][y] instanceof Kiintopiste || pelikenttä[x][y] instanceof NPC_KenttäKohde) {
            vuorovaikutus(esineValInt);
        }
        else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Triggeri) {
            Triggeri trg = (Triggeri)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
            if (trg.annaVaadittuVihollinen() != null) {
                PääIkkuna.avaaDialogi(null, trg.katso(), trg.annaNimi());
            }
            else if (trg.annaVaadittuEsine() != null) {
                PääIkkuna.avaaDialogi(null, "Tähän tarvitaan jokin esine.", trg.annaNimi());
            }
            else {
                trg.triggeröi();
            }
        }
        else if (pelikenttä[x][y] instanceof VisuaalinenObjekti) {
            VisuaalinenObjekti vo = (VisuaalinenObjekti)pelikenttä[x][y];
            if (vo.onkoKatsottava()) {
                PääIkkuna.avaaPitkäDialogiRuutu(vo.annaKatsomisDialogi());
            }
        }
        else if (pelikenttä[x][y] == null) {
            PääIkkuna.hudTeksti.setText("Kohteessa ei ole mitään poimittavaa");
        }
        else{
            PääIkkuna.hudTeksti.setText("Vain esineitä voi poimia! " + pelikenttä[x][y].annaNimi() + " ei ole esine.");
        }
        PääIkkuna.vaatiiPäivityksen = true;
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

    static void pudota(int x, int y, int esineVal){
        if (Pelaaja.esineet[esineVal] == null) {
            PääIkkuna.hudTeksti.setText("Ei pudotettavaa esinettä!");
        }
        else {
            if (pelikenttä[x][y] == null) {
                pelikenttä[x][y] = Pelaaja.esineet[esineVal];
                PääIkkuna.hudTeksti.setText(Pelaaja.esineet[esineVal].annaNimiSijamuodossa("nominatiivi") + " pudotettiin tavaraluettelosta ruutuun " + x + ", " + y);
                Pelaaja.esineet[esineVal] = null;
                valittuEsine = null;
                ÄänentoistamisSäie.toistaSFX("Pudota");
            }
            else {
                PääIkkuna.hudTeksti.setText("Ei voida pudottaa tähän! Kohteessa on jo jotakin.");
            }
        }
        PääIkkuna.vaatiiPäivityksen = true;
    }

    /*
     * Tarkista onko esine yhdistettävä
     */
    static boolean tarkistaYhdistettävyys(int valinta) {
        if (Pelaaja.esineet[valinta] == null) {
            PääIkkuna.hudTeksti.setText("Ei valittua esinettä");
            return false;
        }
        else if (!Pelaaja.esineet[valinta].onkoYhdistettävä()) {
            PääIkkuna.hudTeksti.setText("" + Pelaaja.esineet[valinta].annaNimiSijamuodossa("nominatiivi") + " ei sovi yhdistettäväksi!");
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
    static boolean kokeileYhdistämistä(int val1, int val2) {
        if (Pelaaja.esineet[val2] == null) {
            PääIkkuna.hudTeksti.setText("Ei voi yhdistää tyhjään tavarapaikkaan.");
            return false;
        }
        else {
            if (Pelaaja.esineet[val1].kelvollisetYhdistettävät.contains(Pelaaja.esineet[val2].annaNimi()) && Pelaaja.esineet[val1].onkoYhdistettävä() && Pelaaja.esineet[val2].onkoYhdistettävä()) {
                return true;
            }
            else {
                PääIkkuna.hudTeksti.setText("Näitä esineitä ei voi yhdistää keskenään");
                return false;
            }
        }
    }

    //Järjestää tavaraluettelon esineiden nimen perusteella (samannimiset vierekkäin)
    static void järjestäUudelleen() {
        ArrayList<Esine> esineLista = new ArrayList<Esine>();
        for (int i = 0; i < Pelaaja.esineet.length; i++) {
            if (Pelaaja.esineet[i] != null) {
                esineLista.add(Pelaaja.esineet[i]);
            }
        }
        if (esineLista.size() < 1) {
            PääIkkuna.avaaDialogi(null, "Ei järjestettävää.", "Järjestäminen");
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
            PääIkkuna.avaaDialogi(new ImageIcon("tiedostot/kuvat/hud/järjestäminen.png"), "Tavaraluettelo järjestettiin.", "Järjestäminen");
            PääIkkuna.hudTeksti.setText("Tavaraluettelo järjestettiin");
        }
    }

    static void käyttö(int esine) {
        voiWarpata = false;
        valittuEsine = Pelaaja.esineet[esine];
        if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Warp) {
            Warp warp = (Warp)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
            if (warppiViive <= 0) {
                voiWarpata = true;
                tarkistaWarpinTurvallisuus(warp.annaKohdeHuone(), warp.annaKohdeRuutuX(), warp.annaKohdeRuutuY(), true);
            }
        }
        else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof NPC_KenttäKohde) {
            vuorovaikutus(esineValInt);
        }
        else {
            käytäEsinettä(esine);
        }
        PääIkkuna.uudelleenpiirräObjektit = true;
    }

    static void käytäEsinettä(int esine) {
        if (Pelaaja.esineet[esine] == null) {
            PääIkkuna.hudTeksti.setText("Ei valittua esinettä.");
        }
        else {
            if (valittuEsine.onkoKäyttö()) {
                if (valittuEsine instanceof Ruoka) {
                    Ruoka ruoka = (Ruoka)valittuEsine;
                    PääIkkuna.avaaDialogi(ruoka.annaDialogiKuvake(), ruoka.käytä(), ruoka.annaNimi());
                    p.syöRuoka(ruoka.annaParannusMäärä());
                }
                else if (valittuEsine instanceof Seteli) {
                    PääIkkuna.avaaDialogi(valittuEsine.annaDialogiKuvake(), valittuEsine.käytä(), valittuEsine.annaNimi());
                }
                else if (valittuEsine instanceof Kuparilager || valittuEsine instanceof Seteli || valittuEsine instanceof Jallupullo) {
                    PääIkkuna.hudTeksti.setText(valittuEsine.käytä());
                }
                if (valittuEsine != null) {
                    if (valittuEsine.poistoon()){
                        Pelaaja.esineet[esine] = null;
                        valittuEsine = null;
                    }
                }
            }
            else if (valittuEsine.onkoKenttäkäyttöön()) {
                if (valittuEsine instanceof Ase) {
                    Ase ase = (Ase)valittuEsine;
                    if (Pelaaja.hyökkäysViive <= 0) {
                        Pelaaja.käytettyAse = ase;
                        Pelaaja.hyökkäysAika += ase.annaHyökkäysAika();
                        Pelaaja.hyökkäysViive += ase.annaHyökkäysViive();
                        ÄänentoistamisSäie.toistaSFX("Hyökkäys");
                    }
                }
            }
            else {
                PääIkkuna.hudTeksti.setText(valittuEsine.katso());
            }
        }
    }

    static void vuorovaikutus(int esine) {
        valittuEsine = Pelaaja.esineet[esine];

        if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] == null) {
            PääIkkuna.hudTeksti.setText("Kohteessa ei ole mitään.");
        }
        else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Kiintopiste) {
            Kiintopiste kp = (Kiintopiste)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
            if (!kp.ignooraaEsineValinta()) {
                if (Pelaaja.esineet[esine] == null) {
                    if (!kp.onkoErillisDialogi()) {
                        PääIkkuna.avaaDialogi(kp.annaKuvake(), kp.katso(), kp.annaNimi());
                    }
                    kp.vuorovaikuta(valittuEsine);
                }
                else if (valittuEsine.onkoKenttäkäyttöön()) {
                    PääIkkuna.avaaDialogi(kp.annaDialogiKuvake(), kp.kokeileEsinettä(valittuEsine), kp.annaNimi());
                    valittuEsine = Pelaaja.esineet[esineValInt];
                    valittuEsine = kp.suoritaMuutoksetEsineelle(valittuEsine);
                    Pelaaja.esineet[esineValInt] = valittuEsine;
                    TavoiteLista.tarkistaTavoiteKiintopiste(kp);
                }
                else {
                    PääIkkuna.hudTeksti.setText(valittuEsine.annaNimiSijamuodossa("adessiivi") + " ei ole erikoiskäyttöä.");
                    PääIkkuna.avaaDialogi(kp.annaDialogiKuvake(), valittuEsine.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + kp.annaNimiSijamuodossa("illatiivi") + ".", kp.annaNimi());
                }
            }
            else {
                if (kp.ohitaDialogiTesktiboksi()) {
                    kp.vuorovaikuta(valittuEsine);
                }
                else {
                    PääIkkuna.avaaDialogi(kp.annaDialogiKuvake(), kp.vuorovaikuta(valittuEsine), kp.annaNimi());
                }
            }
        }
        else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof NPC_KenttäKohde) {
            NPC_KenttäKohde kn = (NPC_KenttäKohde)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
            if (Pelaaja.esineet[esine] == null) {
                kn.näytäDialogi(valittuEsine);
            }
            else if (valittuEsine.onkoKenttäkäyttöön()) {
                PääIkkuna.avaaDialogi(kn.annaDialogiKuvake(), kn.kokeileEsinettä(valittuEsine), kn.annaNimi());
                valittuEsine = Pelaaja.esineet[esineValInt];
                //valittuEsine = kn.suoritaMuutoksetEsineelle(valittuEsine);
                Pelaaja.esineet[esineValInt] = valittuEsine;
            }
        }
        else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Triggeri) {
            Triggeri trg = (Triggeri)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
            if (trg.annaVaadittuEsine() == null) {
                trg.triggeröi();
            }
        }
        else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Esine) {
            PääIkkuna.hudTeksti.setText("Ei voi käyttää kentällä lojuviin esineisiin. Kokeile etsiä muita kiintopisteitä.");
        }
        else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Warp) {
            KenttäKohde kk = pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
            kk.näytäDialogi(valittuEsine);
        }
        else {
            PääIkkuna.hudTeksti.setText("Käyttö ei onnistunut. Häire sovelluksessa? Ilmoitathan kehittäjille!");
        }
        PääIkkuna.uudelleenpiirräObjektit = true;
    }

    static void suoritaReunanTarkistus() {
        if (huone != null) {
            if (Pelaaja.hitbox.getMinX() <= 7 && huone.annaReunaWarppiTiedot(KenttäKohde.Suunta.VASEN)) {
                voiWarpataVasen = true;
            }
            else {
                voiWarpataVasen = false;
            }
            if (Pelaaja.hitbox.getMaxX() >= kentänKoko * PeliRuutu.esineenKokoPx - 7 && huone.annaReunaWarppiTiedot(KenttäKohde.Suunta.OIKEA)) {
                voiWarpataOikea = true;
            }
            else {
                voiWarpataOikea = false;
            }
            if (Pelaaja.hitbox.getMaxY() >= kentänKoko * PeliRuutu.esineenKokoPx - 7 && huone.annaReunaWarppiTiedot(KenttäKohde.Suunta.ALAS)) {
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

    static void vaihdaKuvakkeet(int tavarapaikka, int yhdistettävä1, boolean yhdistämisOsoitin) {
        for (int i = 0; i < Pelaaja.annaTavaraluettelonKoko(); i++) {
            PeliRuutu.esineLabel[i].setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        }
        PeliRuutu.esineLabel[tavarapaikka].setBorder(BorderFactory.createLineBorder(Color.red, 3, true));
        if (yhdistämisOsoitin && (yhdistettävä1 > -1)) {
            PeliRuutu.esineLabel[yhdistettävä1].setBorder(BorderFactory.createLineBorder(Color.green, 3, true));
        }
    }

    static void katso() {
        if (!(pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof VisuaalinenObjekti)) {
            PääIkkuna.hudTeksti.setText(pelikenttä[Pelaaja.sijX][Pelaaja.sijY].katso());
            if (PeliRuutu.vuoropuhePaneli.isVisible()) {
                PääIkkuna.kelaaDialogi();
            }
            else {
                PääIkkuna.avaaDialogi(pelikenttä[Pelaaja.sijX][Pelaaja.sijY].annaDialogiKuvake(), pelikenttä[Pelaaja.sijX][Pelaaja.sijY].katso(), pelikenttä[Pelaaja.sijX][Pelaaja.sijY].annaNimi());
            }
            if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Kiintopiste) {
                Kiintopiste kp = (Kiintopiste)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
                TavoiteLista.tarkistaTavoiteKiintopiste(kp);
            }
        }
    }

    static void katsoTavaraluetteloa() {
        PääIkkuna.hudTeksti.setText(Pelaaja.esineet[esineValInt].katso());
        if (PeliRuutu.vuoropuhePaneli.isVisible()) {
            PääIkkuna.kelaaDialogi();
        }
        else {
            PääIkkuna.avaaDialogi(Pelaaja.esineet[esineValInt].annaDialogiKuvake(), Pelaaja.esineet[esineValInt].katso(), Pelaaja.esineet[esineValInt].annaNimi());
        }
    }

    private static class Näppäinkomennot implements KeyListener {

        /*
         * Määritellään mitä eri näppäinkomennot tekee ja mitä metodeja kutsutaan
         */
    
        boolean yhdistäminenKäynnissä = false;
        int yhdistettäväTavarapaikka = -1;

        static boolean vasenPainettu = false;
        static boolean oikeaPainettu = false;
        static boolean ylösPainettu = false;
        static boolean alasPainettu = false;

        @Override
            public void keyTyped(KeyEvent e) {
                
            }
    
        @Override
            public void keyPressed(KeyEvent e) {
                
                boolean pelaajaSiirtyi = false;
                boolean esinepaikkaVaihtui = false;

                if (!pause && !valintaDialogi && !pauseDialogi) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                            vasenPainettu = true;
                            p.aloitaLiike(Suunta.VASEN);
                            break;
                        case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                            oikeaPainettu = true;
                            p.aloitaLiike(Suunta.OIKEA);
                            break;
                        case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                            alasPainettu = true;
                            p.aloitaLiike(Suunta.ALAS);
                            break;
                        case KeyEvent.VK_UP, KeyEvent.VK_W:
                            ylösPainettu = true;
                            p.aloitaLiike(Suunta.YLÖS);
                            break;
                        case KeyEvent.VK_E:
                            poimi(Pelaaja.sijX, Pelaaja.sijY);
                            PääIkkuna.uudelleenpiirräObjektit = true;
                            break;
                        case KeyEvent.VK_Q:
                            pudota(Pelaaja.sijX, Pelaaja.sijY, esineValInt);
                            PääIkkuna.uudelleenpiirräObjektit = true;
                            break;
                        case KeyEvent.VK_1:
                            esineValInt = 0;
                            esinepaikkaVaihtui = true;
                            break;
                        case KeyEvent.VK_2:
                            esineValInt = 1;
                            esinepaikkaVaihtui = true;
                            break;
                        case KeyEvent.VK_3:
                            esineValInt = 2;
                            esinepaikkaVaihtui = true;
                            break;
                        case KeyEvent.VK_4:
                            esineValInt = 3;
                            esinepaikkaVaihtui = true;
                            break;
                        case KeyEvent.VK_5:
                            esineValInt = 4;
                            esinepaikkaVaihtui = true;
                            break;
                        case KeyEvent.VK_6:
                            esineValInt = 5;
                            esinepaikkaVaihtui = true;
                            break;
                        case KeyEvent.VK_SPACE:
                            käyttö(esineValInt);
                            break;
                        case KeyEvent.VK_Z:
                            
                            if (yhdistäminenKäynnissä) {
                                yhdistäminenKäynnissä = false;
                                vaihdaKuvakkeet(esineValInt, -1, false);
                                if (!(yhdistettäväTavarapaikka < 0)) {
                                    if (kokeileYhdistämistä(yhdistettäväTavarapaikka, esineValInt)) {
                                        //yhdistäEsineet(yhdistettäväTavarapaikka, esineValInt);
                                        Pelaaja.esineet[esineValInt] = Esine.yhdistä2Esinettä(Pelaaja.esineet[esineValInt], Pelaaja.esineet[yhdistettäväTavarapaikka]);
                                        Pelaaja.esineet[yhdistettäväTavarapaikka] = null;
                                        PääIkkuna.avaaDialogi(Pelaaja.esineet[esineValInt].annaKuvake(), "Yhdistäminen onnistui! " + "Sait uuden esineen: " + Pelaaja.esineet[esineValInt].annaNimiSijamuodossa("nominatiivi"), "Yhdistäminen");
                                    }
                                    else {
                                        PääIkkuna.avaaDialogi(null, "Yhdistäminen ei onnistunut.", "Yhdistäminen");
                                    }
                                } 
                            }    
                            else if (tarkistaYhdistettävyys(esineValInt)) {
                                PääIkkuna.hudTeksti.setText("Mihin haluat yhdistää? (valitse tavarapaikka näppäimillä 1-5)");
                                PeliRuutu.esineLabel[esineValInt].setBorder(BorderFactory.createLineBorder(Color.green, 3, true));
                                yhdistettäväTavarapaikka = esineValInt;
                                yhdistäminenKäynnissä = true;

                            }
                            else {
                                if (Pelaaja.esineet[esineValInt] == null) {
                                    PääIkkuna.avaaDialogi(null, "Ei valittua esinettä", "Yhdistäminen");
                                }
                                else {
                                    PääIkkuna.avaaDialogi(Pelaaja.esineet[esineValInt].annaKuvake(), Pelaaja.esineet[esineValInt].annaNimiSijamuodossa("partitiivi") + " ei voi yhdistää.", "Yhdistäminen");
                                }
                            }
                            break;

                        case KeyEvent.VK_X:
                            if (Pelaaja.esineet[esineValInt] == null) {
                                PääIkkuna.hudTeksti.setText("Ei valittua esinettä");
                            }
                            else {
                                katsoTavaraluetteloa();
                            }
                            break;
                        case KeyEvent.VK_C:
                            if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] == null) {
                                PääIkkuna.hudTeksti.setText("Kohteessa ei ole mitään.");
                            }
                            else {
                                katso();
                            }
                            break;
                        case KeyEvent.VK_R:
                            järjestäUudelleen();
                            break;
                        
                        case KeyEvent.VK_ESCAPE:
                            pausetaPeli(true);
                            PääIkkuna.hudTeksti.setText("Pause");
                            break;
                        case KeyEvent.VK_F8:
                            System.out.println("Testinäppäin");
                            System.out.println("Testin\u00E4pp\u00E4in");
                            break;
                        case KeyEvent.VK_F9:
                            if (!PääIkkuna.fullscreen) {
                                PääIkkuna.yläPalkki.setVisible(false);
                                PääIkkuna.ikkuna.setExtendedState(JFrame.MAXIMIZED_BOTH); 
                                PääIkkuna.fullscreen = true;
                            }
                            else {
                                PääIkkuna.yläPalkki.setVisible(true);
                                PääIkkuna.ikkuna.setSize(1366, 768);
                                PääIkkuna.fullscreen = false;
                            }
                        break;
                        case KeyEvent.VK_F11:
                            if (!PääIkkuna.fullscreen) {
                                PääIkkuna.yläPalkki.setVisible(false);
                                PääIkkuna.ikkuna.remove(PääIkkuna.yläPalkki);
                                PääIkkuna.näytöt.setFullScreenWindow(PääIkkuna.ikkuna);
                                PääIkkuna.fullscreen = true;
                            }
                            else {
                                PääIkkuna.yläPalkki.setVisible(true);
                                PääIkkuna.ikkuna.add(PääIkkuna.yläPalkki, BorderLayout.NORTH);
                                PääIkkuna.näytöt.setFullScreenWindow(null);
                                PääIkkuna.fullscreen = false;
                            }
                        break;

                        default:
                        break;
                    }
                    if (pelaajaSiirtyi) {
                        suoritaKohtaaminen();
                        PääIkkuna.vaatiiPäivityksen = true;
                        PääIkkuna.pelaajaSiirtyi = true;
                    }
                    else if (esinepaikkaVaihtui) {
                        vaihdaKuvakkeet(esineValInt, yhdistettäväTavarapaikka, yhdistäminenKäynnissä);
                        if (yhdistäminenKäynnissä) {
                            if (Pelaaja.esineet[esineValInt] == null) {
                                PääIkkuna.hudTeksti.setText("Valittu esinepaikka " + esineValInt + " (tyhjä paikka)");
                            }
                            else {
                                PääIkkuna.hudTeksti.setText("Paina Z yhdistääksesi tavarapaikan " + esineValInt + " esineeseen " + Pelaaja.esineet[esineValInt].annaNimi());
                            }
                        }
                        else {
                            if (Pelaaja.esineet[esineValInt] == null) {
                                PääIkkuna.hudTeksti.setText("Valittu esinepaikka " + esineValInt);
                            }
                            else {
                                PääIkkuna.hudTeksti.setText("Valittu esinepaikka " + esineValInt + ": " + Pelaaja.esineet[esineValInt].annaNimiSijamuodossa("nominatiivi"));
                            }
                            valittuEsine = Pelaaja.esineet[esineValInt];
                        }
                    }
                }
                else if (pause && PeliRuutu.pausePaneli.isVisible()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_P:
                            if (PeliRuutu.pausePaneli.isVisible()) {
                                pausetaPeli(false);
                            }
                            PääIkkuna.hudTeksti.setText("Peli jatkuu.");
                        break;
                    }
                }
                else if (pauseDialogi && !valintaDialogi) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_P:
                            if (PeliRuutu.pausePaneli.isVisible()) {
                                pausetaPeli(false);
                            }
                            PääIkkuna.hudTeksti.setText("Peli jatkuu.");
                        break;
                        case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                            if (PeliRuutu.vuoropuhePaneli.isVisible() && !PääIkkuna.äläSuljeNuolilla) {
                                PääIkkuna.edistäDialogia();
                                p.aloitaLiike(Suunta.VASEN);
                            }
                        break;
                        case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                            if (PeliRuutu.vuoropuhePaneli.isVisible() && !PääIkkuna.äläSuljeNuolilla) {
                                PääIkkuna.edistäDialogia();
                                p.aloitaLiike(Suunta.OIKEA);
                            }
                        break;
                        case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                            if (PeliRuutu.vuoropuhePaneli.isVisible() && !PääIkkuna.äläSuljeNuolilla) {
                                PääIkkuna.edistäDialogia();
                                p.aloitaLiike(Suunta.ALAS);
                            }
                        break;
                        case KeyEvent.VK_UP, KeyEvent.VK_W:
                            if (PeliRuutu.vuoropuhePaneli.isVisible() && !PääIkkuna.äläSuljeNuolilla) {
                                PääIkkuna.edistäDialogia();
                                p.aloitaLiike(Suunta.YLÖS);
                            }
                        break;
                        case KeyEvent.VK_C, KeyEvent.VK_E, KeyEvent.VK_X, KeyEvent.VK_F, KeyEvent.VK_SPACE:
                            if (PeliRuutu.vuoropuhePaneli.isVisible()) {
                                PääIkkuna.kelaaDialogi();
                            }
                            else {
                                PääIkkuna.avaaDialogi(pelikenttä[Pelaaja.sijX][Pelaaja.sijY].annaDialogiKuvake(), pelikenttä[Pelaaja.sijX][Pelaaja.sijY].katso(), pelikenttä[Pelaaja.sijX][Pelaaja.sijY].annaNimi());
                            }
                        break;
                    }
                }
                else if (valintaDialogi) {
                    switch (PeliRuutu.lisäRuutuPanelinTyyppi) {
                        case PULLONPALAUTUS:
                            if (PeliRuutu.lisäRuutuPaneli.isVisible()) {    
                                System.out.println(e.getKeyCode());
                                System.out.println(KeyEvent.VK_SPACE);
                                System.out.println(e.getKeyCode() == KeyEvent.VK_SPACE);
                                System.out.println(PullonPalautusRuutu.virheenTyyppi == PullonPalautusRuutu.VirheenTyyppi.PAKKAUS);
                                if (PullonPalautusRuutu.virheenTyyppi == PullonPalautusRuutu.VirheenTyyppi.PAKKAUS && e.getKeyCode() == KeyEvent.VK_SPACE) {
                                    PullonPalautusRuutu.jatkoSyöteAnnettu = true;
                                    System.out.println(PullonPalautusRuutu.jatkoSyöteAnnettu);
                                }
                                else if (PullonPalautusRuutu.virheenTyyppi == PullonPalautusRuutu.VirheenTyyppi.MUOTO && e.getKeyCode() == KeyEvent.VK_X) {
                                    PullonPalautusRuutu.jatkoSyöteAnnettu = true;
                                }
                                else if (PullonPalautusRuutu.virheenTyyppi == PullonPalautusRuutu.VirheenTyyppi.KÄSI && e.getKeyCode() == KeyEvent.VK_C) {
                                    PullonPalautusRuutu.jatkoSyöteAnnettu = true;
                                }
                                else if (PullonPalautusRuutu.virheenTyyppi == PullonPalautusRuutu.VirheenTyyppi.MERKKI && e.getKeyCode() == KeyEvent.VK_Z) {
                                    PullonPalautusRuutu.jatkoSyöteAnnettu = true;
                                }
                            }
                        break;
                        case VALINTADIALOGI:
                            switch (e.getKeyCode()) {
                                case KeyEvent.VK_SPACE, KeyEvent.VK_ENTER: ValintaDialogiRuutu.hyväksyValinta(); break;
                                case KeyEvent.VK_ESCAPE: ValintaDialogiRuutu.peruValinta(); break;
                                case KeyEvent.VK_W, KeyEvent.VK_UP: ValintaDialogiRuutu.pienennäValintaa(); break;
                                case KeyEvent.VK_S, KeyEvent.VK_DOWN: ValintaDialogiRuutu.kasvataValintaa(); break;
                            }
                        break;
                        case ÄMPÄRIJONO:
                            switch (e.getKeyCode()) {
                                case KeyEvent.VK_SPACE, KeyEvent.VK_ENTER: ÄmpäriJonoRuutu.poistuÄmpärijonosta(); break;
                            }
                        break;
                        default:
                        break;
                    }
                }
            }
    
        @Override
            public void keyReleased(KeyEvent e) {
                
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                        p.lopetaLiike(Suunta.VASEN);
                        vasenPainettu = false;
                        break;
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                        p.lopetaLiike(Suunta.OIKEA);
                        oikeaPainettu = false;
                        break;
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                        p.lopetaLiike(Suunta.ALAS);
                        alasPainettu = false;
                        break;
                    case KeyEvent.VK_UP, KeyEvent.VK_W:
                        p.lopetaLiike(Suunta.YLÖS);
                        ylösPainettu = false;
                        break;
                    }
            }
    }

    public static void pausetaPeli(boolean pauseta) {
        if (pauseta) {
            pauseAlkuAika = System.nanoTime();
            ValintaDialogiRuutu.luoValintaDialogiIkkuna("pause");
        }
        else {
            pauseLoppuAika = System.nanoTime();
            aikaReferenssi += (pauseLoppuAika - pauseAlkuAika);
            asetuksetAuki = false;
        }
        pause = pauseta;
        ajastinPysäytetty = pauseta;
    }

    static void siirryTarinaRuutuun(String tarina) {
        if (tarina.startsWith("koti")) {
            TavoiteLista.suoritaPääTavoite(0);
        }
        Peli.pause = true;
        PääIkkuna.lataaTarinaRuutu(tarina);
        TarinaRuutu.tarinaPaneli.requestFocus();
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
        PääIkkuna.lataaRuutu("loppuruutu");
        LoppuRuutu.valitseLoppuRuutu(TarkistettavatArvot.pelinLoppuSyy);
        LoppuRuutu.loppuruutuPaneli.requestFocus();
    }

    public static void suljePeli() {
        peliKäynnissä = false;
        pause = true;
        boolean guit = false;
        for (int i = 0; i < Pelaaja.esineet.length; i++) {
            Pelaaja.esineet[i] = null;
        }
        PääIkkuna.ikkuna.removeKeyListener(nk);
        ValikkoRuutu.alkuvalikonKorttiLayout.first(ValikkoRuutu.kortit);
        PeliSäie.peliLoopKäynnissä = false;
        GrafiikanPäivitysSäie.säieKäynnissä = false;
        ÄänentoistamisSäie.suljeMusa();
        globaaliTickit = 0;
        if (guit) {
            System.exit(0);
        }
        PääIkkuna.pääPaneeli.removeAll();
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
                    if ((huoneKartta.get(huoneenId).annaHuoneenMaastoSisältö()[kohteenX][kohteenY] instanceof EsteTile) && estäEpäturvallisetWarpit) {
                        if (näytäHuomautus) {
                            Pelaaja.pakotaPelaajanPysäytys();
                            JOptionPane.showMessageDialog(null, "Warpin kohteessa on este tai kohde on kentän ulkopuolella.\n\nWarppaamisen epäturvallisiin kohteisiin voi sallia editorissa.", "Warppaaminen epäonnistui", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    else if (huoneKartta.get(huoneenId).annaHuoneenKenttäSisältö()[kohteenX][kohteenY] != null) {
                        if (huoneKartta.get(huoneenId).annaHuoneenKenttäSisältö()[kohteenX][kohteenY] instanceof VisuaalinenObjekti) {
                            VisuaalinenObjekti vo = (VisuaalinenObjekti)huoneKartta.get(huoneenId).annaHuoneenKenttäSisältö()[kohteenX][kohteenY];
                            if (vo.onkoEste()) {
                                if (näytäHuomautus) {
                                    Pelaaja.pakotaPelaajanPysäytys();
                                    JOptionPane.showMessageDialog(null, "Warpin kohteessa on este tai kohde on kentän ulkopuolella.\n\nWarppaamisen epäturvallisiin kohteisiin voi sallia editorissa.", "Warppaaminen epäonnistui", JOptionPane.INFORMATION_MESSAGE);
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
            PääIkkuna.näytäVirheIlmoitusDialogi("Yritettiin warpata kentän ulkopuolelle kohdehuoneessa! Reunawarpit toimivat vain, jos huoneet ovat samankokoisia (ehkä tulossa korjaus). Käytä warppiobjekteja toistaiseksi.", "Virheellinen warppi");
            ioobe.printStackTrace();
        }
    }

    private static boolean muutaKentänKokoa(int uusiKoko) {
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
            if (lataaHuone(huoneenId)) {
                Pelaaja.teleport(kohteenX, kohteenY);
            }
        }
    }

    /**
     * "Siirry valittuun huoneeseen" eli
     * Lataa pelin nykyiseksi kentäksi huonekartasta (HashMapista) valittu Huone-objekti
     * @param huoneenId ladattavan huoneen ID huonekartassa
     * @param debug estä tavoitteen tarkistus ja tarinan lataus
     * @return onnistuiko huoneen lataaminen (löytyikö valitulla ID:llä huone)
     */

    public static boolean lataaHuone(int huoneenId, boolean debug) {
        try{
            if (huoneKartta.get(huoneenId) != null) {
                if ((!huoneKartta.get(huoneenId).annaTavoiteVaatimus() || TavoiteLista.tavoiteLista.get(huoneKartta.get(huoneenId).annaVaaditunTavoitteenTunniste())) || debug) {
                    huone = huoneKartta.get(huoneenId);
                    if (muutaKentänKokoa(huone.annaKoko())) {
                        PääIkkuna.uudelleenpiirräKenttä = true;
                    }
                    else {
                        PääIkkuna.uudelleenpiirräTaustat = true;
                    }
                    pelikenttä = huone.annaHuoneenKenttäSisältö();
                    maastokenttä = huone.annaHuoneenMaastoSisältö();
                    for (Maasto[] mm : maastokenttä) {
                        for (Maasto m : mm) {
                            if (m != null) {
                                m.päivitäKuvanAsento();
                            }
                        }
                    }
                    entityLista.clear();
                    for (Entity[] nn : huone.annaHuoneenNPCSisältö()) {
                        for (Entity entity : nn) {
                            if (entity != null) {
                                entityLista.add(entity);
                                if (entity instanceof NPC) {
                                    NPC npc = (NPC)entity;
                                    if (!npc.onLadattuPelissä) {
                                        npc.teleport(npc.annaAlkuSijX(), npc.annaAlkuSijY());
                                        npc.onLadattuPelissä = true;
                                    }
                                }
                            }
                        }
                    }
                    if (huone.annaTausta() != null) {
                        GrafiikanPäivitysSäie.uusiTausta = huone.annaTausta();
                        GrafiikanPäivitysSäie.uusiTaustaSkaalattu = new KäännettäväKuvake(huone.annaTausta(), 0, false, false, 990d/660d, 1, -246, -246, false);
                    }
                    voiWarpataVasen = false;
                    voiWarpataOikea = false;
                    voiWarpataAlas = false;
                    voiWarpataYlös = false;
                    warppiViive = 20;
                    PääIkkuna.hudTeksti.setText("Ladattiin huone " + huone.annaNimi() + " (ID: " + huone.annaId() + ")");
                    boolean toistaMusaTarinanJälkeen = false;
                    if (huoneKartta.get(huoneenId).annaTarinaRuudunLataus() && !debug) {
                        if (TarinaDialogiLista.tarinaKartta.containsKey(huoneKartta.get(huoneenId).annaTarinaRuudunTunniste())) {
                            pause = true;
                            toistaMusaTarinanJälkeen = true;
                            siirryTarinaRuutuun(huoneKartta.get(huoneenId).annaTarinaRuudunTunniste());
                            huoneKartta.get(huoneenId).tarinaRuudunTunniste = null;
                        }
                        else {
                            PääIkkuna.avaaDialogi(null, "Tarinapätkää " + huoneKartta.get(huoneenId).annaTarinaRuudunTunniste() + " ei löytynyt", "Virhe!", true, null);
                        }
                    }
                    if (!toistaMusaTarinanJälkeen) {
                        String musa = huone.annaHuoneenMusa();
                        ÄänentoistamisSäie.toistaPeliMusa(musa);
                    }
                    else {
                        ÄänentoistamisSäie.toistaPeliMusa(null);
                    }
                    return true;
                }
                else {
                    PääIkkuna.avaaDialogi(null, "Huoneeseen warppaaminen vaatii tavoitteen: " + huoneKartta.get(huoneenId).annaVaaditunTavoitteenTunniste(), "Huone lukittu", true, null);
                    return false;
                }
            }
            else {
                pause = true;
                JOptionPane.showMessageDialog(null, "Yritettiin warpata huoneeseen " + huoneenId + ", jota ei ole olemassa.", "Huonetta ei löytynyt.", JOptionPane.ERROR_MESSAGE);
                pause = false;
                return false;
            }
            
        }
        catch (NullPointerException e) {
            pause = true;
            JOptionPane.showMessageDialog(null, "Ongelma ladatessa huonetta " + huoneenId + ".", "Huonetta ei löytynyt.", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            pause = false;
            return false;
        }
    }

    public static boolean lataaHuone(int huoneenId) {
        return lataaHuone(huoneenId, false);
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
    static int dialoginAvausViive = 0;
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
            if (Pelaaja.liikutaPelaajaa()) {
                suoritaReunanTarkistus();
                suoritaKohtaaminen();
                PääIkkuna.vaatiiPäivityksen = true;
                PääIkkuna.pelaajaSiirtyi = true;
                if (warppiViive == 0) {
                    if (voiWarpataVasen && Näppäinkomennot.vasenPainettu) {
                        int kohdeHuoneenKoko = huoneKartta.get(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.VASEN)).annaKoko();
                        tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.VASEN), kohdeHuoneenKoko-1, Pelaaja.sijY, false);
                    }
                    else if (voiWarpataOikea && Näppäinkomennot.oikeaPainettu) {
                        tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.OIKEA), kentänAlaraja, Pelaaja.sijY, false);
                    }
                    else if (voiWarpataAlas && Näppäinkomennot.alasPainettu) {
                        tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.ALAS), Pelaaja.sijX, kentänAlaraja, false);
                    }
                    else if (voiWarpataYlös && Näppäinkomennot.ylösPainettu) {
                        int kohdeHuoneenKoko = huoneKartta.get(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.YLÖS)).annaKoko();
                        tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.YLÖS), Pelaaja.sijX, kohdeHuoneenKoko-1, false);
                    }
                    else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Warp) {
                        Warp warp = (Warp)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
                        if (
                            warp.annaSuunta() == KenttäKohde.Suunta.VASEN && Näppäinkomennot.vasenPainettu ||
                            warp.annaSuunta() == KenttäKohde.Suunta.OIKEA && Näppäinkomennot.oikeaPainettu ||
                            warp.annaSuunta() == KenttäKohde.Suunta.ALAS && Näppäinkomennot.alasPainettu ||
                            warp.annaSuunta() == KenttäKohde.Suunta.YLÖS && Näppäinkomennot.ylösPainettu
                        ) {
                            warp.ennenWarppia();
                            tarkistaWarpinTurvallisuus(warp.annaKohdeHuone(), warp.annaKohdeRuutuX(), warp.annaKohdeRuutuY(), true);
                            warp.warpinJälkeen();
                        }
                    }
                }
            }
        }
        catch (NullPointerException npe) {
            System.out.println("Pelaajan liike epäonnistui.");
            npe.printStackTrace();
        }
    }

    /**
     * Päivitä pelaajan sijainti ikkunan yläpalkkiin tietoihin, jos sijainti on asetettu näkyviin.
     * Pelaajan sijainti pelikentällä + pelaajan hitboxin rajat.
     */

    public static void päivitäPelaajanSijaintiTiedot() {
        try {
            String kohdeteksti = "";
            String kohdetekstiMaasto = "";
            String kohdetekstiNPC = "";
            if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] == null) {
                kohdeteksti = "Kohteessa ei ole mitään";
            }
            else {
                kohdeteksti = "Kohteessa on " + pelikenttä[Pelaaja.sijX][Pelaaja.sijY].annaNimiSijamuodossa("nominatiivi");
            }
            if (maastokenttä[Pelaaja.sijX][Pelaaja.sijY] == null) {
                kohdetekstiMaasto = "Maasto: tyhjä";
            }
            else {
                kohdetekstiMaasto = "Maasto: " + maastokenttä[Pelaaja.sijX][Pelaaja.sijY].annaNimiSijamuodossa("nominatiivi");
            }
            if (Pelaaja.vihollinenKohdalla != null) {
                kohdetekstiNPC = "NPC: " + Pelaaja.vihollinenKohdalla.annaNimi();
            }
            else {
                kohdetekstiNPC = "NPC: ei mitään";
            }
            PeliRuutu.ylätekstiSij.setText("X: " +  Pelaaja.hitbox.getMinX() + "-" + Pelaaja.hitbox.getMaxX() + ", Y: " + Pelaaja.hitbox.getMinY() + "-" + Pelaaja.hitbox.getMaxY());
            PeliRuutu.ylätekstiSijRuutu.setText("Tile: " + Pelaaja.sijX + ", " + Pelaaja.sijY);
            PeliRuutu.ylätekstiKohde.setText("" + kohdeteksti);
            PeliRuutu.ylätekstiKohdeMaasto.setText("" + kohdetekstiMaasto);
            PeliRuutu.ylätekstiKohdeNPC.setText("" + kohdetekstiNPC);
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            System.out.println("Pelaajan sijantitietoa ei voitu päivittää");
            aioobe.printStackTrace();
        }
    }

    /**
     * Tarkista pelin tilan muutokset kuten huoneen lataus, tarinaruutuun siirtyminen ja häviö
     */

    public static void pelinKulku() {

        if (huoneVaihdettava) {
            synchronized(Peli.huoneenLatausLukko) {
                System.out.println("Pelisäie ottaa lukon");
                lataaHuone(uusiHuone);
                boolean turvallinenRuutuLöydetty = false;
                if (Pelaaja.sijX >= huone.annaKoko() || Pelaaja.sijY >= huone.annaKoko()) {
                    JOptionPane.showMessageDialog(PääIkkuna.ikkuna, "Pelaajan nykyinen sijainti on uuden huoneen ulkopuolella. Pelaaja siirretään ensimmäiseen turvalliseen ruutuun.", "Pelaaja uuden huoneen ulkopuolella", JOptionPane.WARNING_MESSAGE);
                    for (int i = 0; i < huone.annaKoko(); i++) {
                        if (!turvallinenRuutuLöydetty) {
                            for (int j = 0; j < huone.annaKoko(); j++) {
                                if (huone.annaHuoneenMaastoSisältö()[j][i] != null) {
                                    if (!(huone.annaHuoneenMaastoSisältö()[j][i] instanceof EsteTile)) {
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
                PääIkkuna.uudelleenpiirräKaikki = true;
                huoneVaihdettava = false;
            }
            System.out.println("Pelisäie vapauttaa lukon");
        }

        if (PääIkkuna.uusiIkkuna) {
            PääIkkuna.pääPaneeli.removeAll();
            suljePeli();
            PääIkkuna.uusiIkkuna = false;
            uusiHuone = 0;
            huoneVaihdettava = true;
            uusiPeli();
        }

        tarkistaPelinTila();
        if (peliLäpäisty || PääIkkuna.uusiIkkuna) {
            siirryLoppuRuutuun(0);
            peliKäynnissä = false;
        }
    }

    public static void tarkistaUudelleenkäynnistys() {
        if (PääIkkuna.uusiIkkuna) {
            System.out.println("uusi peli");
            suljePeli();
            PääIkkuna.uusiIkkuna = false;
            uusiHuone = 0;
            huoneVaihdettava = true;
            uusiPeli();
        }
    }

    public static long globaaliTickit = 0;
    public static long aikaReferenssi = System.nanoTime();

    static boolean uusiKäynnistysYritys = false;

    public static void uusiPeli() {
        
        LatausIkkuna.palautaLatausIkkuna();
        
        Thread latausThread = new Thread() {
            public void run() {
                try {
                    kentänKoko = TarkistettavatArvot.uusiKentänKoko;
                    kentänYläraja = kentänAlaraja + kentänKoko - 1;
                    pause = true;
                    peliKäynnissä = false;
                    TarkistettavatArvot.nollaa();
                    new Peli();
                    //Keimo3D.launchKeimo3D();
                    //throw new Exception();
                }
                catch (Exception e) {
                    LatausIkkuna.suljeLatausIkkuna();
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String sStackTrace = sw.toString();
                    int virheValinta = CustomViestiIkkunat.SuljeVirheenJälkeen.näytäDialogi(sStackTrace);
                    if (virheValinta == 1) {
                        System.exit(0);
                    }
                    else {
                        LatausIkkuna.luoLatausIkkuna();
                        uusiPeli();
                    }
                }
            }
        };
        latausThread.start();
    }

    public Peli() {

        LatausIkkuna.päivitäLatausTeksti("Alustetaan peli...");
        PeliSäie.kulunutAika = 0;
        aikaReferenssi = System.nanoTime();
        peliAloitettu = false;
        peliLäpäisty = false;
        pauseDialogi = false;
        valintaDialogi = false;
        ajastinPysäytetty = false;
        uusiHuone = 0;
        huoneVaihdettava = true;
        
        LatausIkkuna.päivitäLatausTeksti("Ladataan dialogeja...");
        TavoiteLista.luoPääTavoiteLista();
        TavoiteLista.luoTavoiteLista();
        VuoropuheDialogit.luoVakioVuoropuheDialogit();
        TarinaPätkä.nollaaTarinaId();
        KeimoFontit.rekisteröiFontit();

        LatausIkkuna.päivitäLatausTeksti("Ladataan huoneita...");
        KenttäKohde.nollaaObjektiId();
        huoneKartta = HuoneLista.luoVakioHuoneKarttaTiedostosta();
        if (huoneKartta != null) {
            if (huoneKartta.get(0) != null) {
                muutaKentänKokoa(huoneKartta.get(0).annaKoko());
            }
        }
        LatausIkkuna.päivitäLatausTeksti("Ladataan pelaajaa...");
        esineValInt = 0;
        valittuEsine = null;
        p = new Pelaaja();
        if (Pelaaja.sijX < kentänKoko && Pelaaja.sijY < kentänKoko) {
            Pelaaja.teleport(Pelaaja.sijX, Pelaaja.sijY);
        }
        else {
            Pelaaja.teleport(0, 0);
        }
        TarkistettavatArvot.pelinLoppuSyy = TarkistettavatArvot.PelinLopetukset.VAKIO;

        LatausIkkuna.päivitäLatausTeksti("Ladataan grafiikkaa...");
        if (PääIkkuna.ikkuna != null && PääIkkuna.pääPaneeli != null) {
            PääIkkuna.ikkuna.remove(PääIkkuna.pääPaneeli);
        }
        PääIkkuna.luoPääikkuna();
        PääIkkuna.ikkuna.addKeyListener(nk);
        PääIkkuna.vaatiiPäivityksen = true;
        PeliRuutu.luoPeliRuudunGUI();
        ÄänentoistamisSäie.suljeMusa();
        PääIkkuna.lataaRuutu("tarinaruutu");
        TarinaRuutu.tarinaPaneli.requestFocus();

        LatausIkkuna.päivitäLatausTeksti("Käynnistetään säikeitä...");
        if (grafiikkaSäie.getState() == State.NEW || grafiikkaSäie.getState() == State.TERMINATED) {
            grafiikkaSäie = new GrafiikanPäivitysSäie();
            grafiikkaSäie.setName("Grafiikkasäie");
            if (PelinAsetukset.ajoitus != AjoitusMuoto.ERITTÄIN_NOPEA) {
                grafiikkaSäie.start();
            }
        }
        if (peliSäie.getState() == State.NEW || peliSäie.getState() == State.TERMINATED) {
            peliSäie = new PeliSäie();
            peliSäie.setName("Pelisäie");
            peliSäie.start();
        }
        
        LatausIkkuna.päivitäLatausTeksti("Valmis.");
        try {
            Thread.sleep(25);
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        LatausIkkuna.suljeLatausIkkuna();
    }
}