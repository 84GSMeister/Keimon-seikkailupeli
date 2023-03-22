package keimo;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import keimo.Kenttäkohteet.*;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.Maastot.*;
import keimo.NPCt.*;
import keimo.Pelaaja.KeimonState;
import keimo.Pelaaja.Suunta;

public class Peli {
    
    /**
     * Puutteet sovelluksessa:
     *  -joitain luokkia ei kommentoitu
     *  -esineiden käytä() -metodeissa bugeja ja puuttuvia toteutuksia
     */
    
    public static boolean pause = true;
    static boolean peliAloitettu = false;
    public static int kentänKoko = 10;
    public static int kentänAlaraja = 0;
    public static int kentänYläraja = kentänAlaraja + kentänKoko - 1;
    static KenttäKohde[][] pelikenttä = new KenttäKohde[kentänKoko][kentänKoko];
    static Maasto[][] maastokenttä= new Maasto[kentänKoko][kentänKoko];

    static boolean huoneVaihdettava = true;
    static int uusiHuone = 0;
    static HashMap<Integer, Huone> huoneKartta = new HashMap<Integer, Huone>();
    static Huone huone;
    static String häviönSyy = "";
    static int aloitusHp = 10;
    public static int huoneidenMäärä = 0;

    int esineitäKentällä = 0;
    Random r = new Random();
    public static Pelaaja p = new Pelaaja();
    Scanner sc = new Scanner(System.in);
    static boolean peliLäpäisty = false;
    static boolean peliKäynnissä = true;
    static boolean peliHävitty = false;
    int valinta;
    int valinta2;
    Esine valittuEsine;
    int toiminto;
    int valX;
    int valY;
    static int esineValInt = 0;
    DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
    static boolean voiWarpata = false;
    
    public static KenttäKohde[][] annaObjektiKenttä() {
        return pelikenttä;
    }

    public static Maasto[][] annaMaastoKenttä() {
        return maastokenttä;
    }

    static GrafiikanPäivitysSäie gThread = new GrafiikanPäivitysSäie();
    static ÄänentoistamisSäie sThread = new ÄänentoistamisSäie();
    static AikaSäie tThread = new AikaSäie();

    static int NPCidenMäärä;
    public static int annaNPCidenMäärä() {
        return NPCidenMäärä;
    }
    public static void lisääNPC(NPC npc) {
        NPCidenMäärä++;
        npcLista.add(npc);
    }
    public static ArrayList<NPC> npcLista = new ArrayList<NPC>();

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

    void poimi(int x, int y){
        if (pelikenttä[x][y] instanceof Esine){
            if (p.annaEsineidenMäärä() < p.annaTavaraluettelonKoko()) {
                for (int i = 0; i < p.esineet.length; i++) {
                    if (p.esineet[i] == null) {
                        p.esineet[i] = (Esine)pelikenttä[x][y];
                        break;
                    }
                }
                PääIkkuna.hudTeksti.setText("Sait uuden esineen: " + pelikenttä[x][y].annaNimiSijamuodossa("nominatiivi"));
                pelikenttä[x][y] = null;
            }
            else {
                PääIkkuna.hudTeksti.setText("Ei voida poimia! Tavaraluettelo täynnä! Kokeile pudottaa jokin esine tyhjään ruutuun");
            }
        }
        else if (pelikenttä[x][y] instanceof Ämpärikone) {
            p.annaEsine(Ämpärikone.annaÄmpäri());
            PääIkkuna.hudTeksti.setText("Sait uuden ämpärin.");
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

    void pudota(int x, int y, int esineVal){
        if (p.esineet[esineVal] == null) {
            PääIkkuna.hudTeksti.setText("Ei pudotettavaa esinettä!");
        }
        else {
            if (pelikenttä[x][y] == null) {
                pelikenttä[x][y] = p.esineet[esineVal];
                PääIkkuna.hudTeksti.setText(p.esineet[esineVal].annaNimiSijamuodossa("nominatiivi") + " pudotettiin tavaraluettelosta ruutuun " + x + ", " + y);
                p.esineet[esineVal] = null;
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
    boolean tarkistaYhdistettävyys(int valinta) {
        if (p.esineet[valinta] == null) {
            PääIkkuna.hudTeksti.setText("Ei valittua esinettä");
            return false;
        }
        else if (!p.esineet[valinta].onkoYhdistettävä()) {
            PääIkkuna.hudTeksti.setText("" + p.esineet[valinta].annaNimiSijamuodossa("nominatiivi") + " ei sovi yhdistettäväksi!");
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
    boolean kokeileYhdistämistä(int val1, int val2) {
        if (p.esineet[val2] == null) {
            PääIkkuna.hudTeksti.setText("Ei voi yhdistää tyhjään tavarapaikkaan.");
            return false;
        }
        else {
            if (p.esineet[val1].kelvollisetYhdistettävät.contains(p.esineet[val2].annaNimi()) && p.esineet[val1].onkoYhdistettävä() && p.esineet[val2].onkoYhdistettävä()) {
                return true;
            }
            else {
                PääIkkuna.hudTeksti.setText("Näitä esineitä ei voi yhdistää keskenään");
                return false;
            }
        }
    }

    //Järjestää tavaraluettelon esineiden nimen perusteella (samannimiset vierekkäin)
    void järjestäUudelleen() {
        ArrayList<Esine> esineLista = new ArrayList<Esine>();
        for (int i = 0; i < p.esineet.length; i++) {
            if (p.esineet[i] != null) {
                esineLista.add(p.esineet[i]);
            }
        }
        Collections.sort(esineLista, new Comparator<Esine>() {
            public int compare(Esine e1, Esine e2) {
                return e1.annaNimi().compareTo(e2.annaNimi());
            }
        });
        for (int i = 0; i < p.esineet.length; i++) {
            p.esineet[i] = null;
        }
        for (int i = 0; i < esineLista.size(); i++) {
            p.esineet[i] = esineLista.get(i);
        }
        PääIkkuna.hudTeksti.setText("Tavaraluettelo järjestettiin");
    }

    void käyttö(int esine) {
        voiWarpata = false;
        valittuEsine = p.esineet[esine];
        if (pelikenttä[p.sijX][p.sijY] instanceof Ämpärikone) {
            p.annaEsine(Ämpärikone.annaÄmpäri());
        }
        else if (pelikenttä[p.sijX][p.sijY] instanceof Warp) {
            Warp warp = (Warp)pelikenttä[p.sijX][p.sijY];
            voiWarpata = true;
            if (lataaHuone(warp.annaKohdeHuone())) {
                p.teleport(warp.annaKohdeRuutuX(), warp.annaKohdeRuutuY());
            }
        }
        else {
            käytäEsinettä(esine);
        }
        PääIkkuna.vaatiiKentänPäivityksen = true;
    }

    void käytäEsinettä(int esine) {
        if (p.esineet[esine] == null) {
            PääIkkuna.hudTeksti.setText("Ei valittua esinettä.");
        }
        else {
            if (p.esineet[esine].onkoKäyttö()) {
                if (valittuEsine instanceof Ruoka) {
                    Ruoka ruoka = (Ruoka)valittuEsine;
                    PääIkkuna.hudTeksti.setText(valittuEsine.käytä());
                    p.syöRuoka(ruoka.annaParannusMäärä());
                }
                if (valittuEsine.poistoon()){
                    p.esineet[esine] = null;
                }
            }
            else {
                PääIkkuna.hudTeksti.setText(p.esineet[esine].katso());
            }
        }
    }

    void erikoisKäyttö(int esine) {
        valittuEsine = p.esineet[esine];
        if (p.esineet[esine] == null) {
            if (pelikenttä[p.sijX][p.sijY] == null) {
                PääIkkuna.hudTeksti.setText("Ei valittua esinettä.");
            }
            else {
                PääIkkuna.hudTeksti.setText(pelikenttä[p.sijX][p.sijY].katso());
            }
        }
        else if (valittuEsine.onkoKenttäkäyttöön()) {
            valX = p.sijX;
            valY = p.sijY;

            if (Pelaaja.vihollisenKohdalla && Pelaaja.vihollinenKohdalla != null) {
                Vihollinen vihollinen = Pelaaja.vihollinenKohdalla;
                if (!vihollinen.onkoKukistettu()) {
                    if (vihollinen.tehoavatAseet.contains(valittuEsine.annaNimi())) {
                        PääIkkuna.hudTeksti.setText(valittuEsine.käytä());
                        vihollinen.kukista(valittuEsine.annaNimi());
                        TarkistettavatArvot.lisääTappoLaskuriin(valittuEsine.annaNimi());
                        ÄänentoistamisSäie.toistaTappoÄäni(valittuEsine.annaNimi());
                    }
                    else {
                        PääIkkuna.hudTeksti.setText(valittuEsine.annaNimi() + " ei tehonnut " + vihollinen.annaNimiSijamuodossa("illatiivi"));
                    }
                }
                else {
                    if (vihollinen.tehoavatAseet.contains(valittuEsine.annaNimi())) {
                        PääIkkuna.hudTeksti.setText(vihollinen.annaNimi() + " on jo kukistettu. Ei tarvitse lyödä lyötyä!");
                    }
                    else {
                        PääIkkuna.hudTeksti.setText(valittuEsine.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää kukistettuun " + vihollinen.annaNimiSijamuodossa("illatiivi"));
                    }
                }
            }
            else if (pelikenttä[valX][valY] instanceof Kiintopiste || pelikenttä[valX][valY] instanceof NPC_KenttäKohde) {
                KenttäKohde kk = (KenttäKohde)pelikenttä[valX][valY];
                PääIkkuna.hudTeksti.setText(kk.kokeileEsinettä(valittuEsine));
                valittuEsine = kk.suoritaMuutoksetEsineelle(valittuEsine);
                p.esineet[esineValInt] = valittuEsine;
            }
            else if (pelikenttä[valX][valY] == null) {
                PääIkkuna.hudTeksti.setText("Kohteessa ei ole mitään mihin käyttää esinettä");
            }
            else if (pelikenttä[valX][valY] instanceof Esine) {
                PääIkkuna.hudTeksti.setText("Ei voi käyttää kentällä lojuviin esineisiin. Kokeile etsiä muita kiintopisteitä.");
            }
            else if (pelikenttä[valX][valY] instanceof Warp) {
                PääIkkuna.hudTeksti.setText("Ei voi käyttää oviruutuihin.");
            }
            else {
                PääIkkuna.hudTeksti.setText("Käyttö ei onnistunut. Häire sovelluksessa? Ilmoitathan kehittäjille!");
            }
        }
        else {
            PääIkkuna.hudTeksti.setText(valittuEsine.annaNimiSijamuodossa("adessiivi") + " ei ole erikoiskäyttöä.");
        }
        if (valittuEsine != null) {
            if (valittuEsine.poistoon()) {
                p.esineet[esine] = null;
            }
        }
        PääIkkuna.vaatiiKentänPäivityksen = true;
    }

    static void suoritaKohtaaminen() {
        if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof NPC_KenttäKohde) {
            NPCKohtaaminen();
        }
        else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof ReunaWarppi) {
            voiWarpata = true;
        }
    }

    static void NPCKohtaaminen() {
        if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Vihollinen_KenttöKohde) {
            vihollisKohtaaminen();
        }
    }

    static void vihollisKohtaaminen() {
        if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Vihollinen_KenttöKohde) {
            Vihollinen_KenttöKohde vihu = (Vihollinen_KenttöKohde)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
            if (vihu.onkoKukistettu() || ((Pelaaja.esineet[esineValInt] instanceof Kilpi) && vihu.kilpiTehoaa)) {
                
            }
            else {
                if (Pelaaja.kuolemattomuusAika <= 0) {
                    Pelaaja.vahingoita(vihu.vahinko);
                    TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN;
                    System.out.println(PelinLopetukset.KUOLEMA_VIHOLLINEN);
                    Pelaaja.kuolemattomuusAika = 60;
                    PääIkkuna.hudTeksti.setText("Osuit viholliseen! Menetit " + vihu.vahinko + " elämäpisteen.");
                }
            }
        }
    }

    // Tarkistetaan, onko pelin tavoitteet suoritettu.
    // Jos on, peli päättyy ja ikkuna sulkeutuu.
    static boolean tarkistaPelinTila(){
        PääIkkuna.tavoiteTeksti1.setText("Tavoite 1: Sytytä nuotio - suoritettu: " + (tarkistaTavoiteNuotio() ? "Kyllä" : "Ei"));
        PääIkkuna.tavoiteTeksti2.setText("Tavoite 2: Avaa kirstu - suoritettu: " + (tarkistaTavoiteKirstu() ? "Kyllä" : "Ei"));
        PääIkkuna.tavoiteTeksti3.setText("Tavoite 3: Syö - suoritettu: " + (tarkistaTavoiteRuoka() ? "Kyllä" : "Ei"));
        if (Pelaaja.syödytRuoat >= 4) {
            häviönSyy = "Söit liikaa ja sinulle tuli paha olo.";
            peliHävitty = true;
            suljePeli(2);
        }

        if (Pelaaja.hp <= 0) {
            häviönSyy = "Sait selkääsi!";
            Pelaaja.keimonState = KeimonState.KUOLLUT;
            peliHävitty = true;
            suljePeli(1);
        }

        if (tarkistaTavoiteNuotio() && tarkistaTavoiteKirstu() && tarkistaTavoiteRuoka()) {
            return true;
        }
        else {
            return false;
        }
    }
    static boolean tarkistaTavoiteNuotio() {
        boolean result = false;
        for (int i = 0; i < kentänKoko; i++) {
            for (int j = 0; j < kentänKoko; j++) {
                if (pelikenttä[i][j] instanceof Nuotio) {
                    if (pelikenttä[i][j].tavoiteSuoritettu) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }
    static boolean tarkistaTavoiteKirstu(){
        boolean result = false;

        for (int i = 0; i < kentänKoko; i++) {
            for (int j = 0; j < kentänKoko; j++) {
                if (pelikenttä[i][j] instanceof Kirstu) {
                    if (pelikenttä[i][j].tavoiteSuoritettu) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }
    static boolean tarkistaTavoiteRuoka(){
        boolean result = false;
        if (Pelaaja.kylläinen) {
            result = true;
        }
        return result;
    }

    void vaihdaKuvakkeet(int tavarapaikka, int yhdistettävä1, boolean yhdistämisOsoitin) {
        for (int i = 0; i < p.annaTavaraluettelonKoko(); i++) {
            PääIkkuna.osoitinLabel[i].setIcon(null);;
        }
        PääIkkuna.osoitinLabel[tavarapaikka].setIcon(new ImageIcon("tiedostot/kuvat/osoitin.png"));
        if (yhdistämisOsoitin && (yhdistettävä1 > -1)) {
            PääIkkuna.osoitinLabel[yhdistettävä1].setIcon(new ImageIcon("tiedostot/kuvat/osoitin_valittu.png"));
        }
    }

    private class Näppäinkomennot implements KeyListener {

        /*
         * Määritellään mitä eri näppäinkomennot tekee ja mitä metodeja kutsutaan
         */
    
        boolean yhdistäminenKäynnissä = false;
        int yhdistettäväTavarapaikka = -1;

        @Override
            public void keyTyped(KeyEvent e) {
                
            }
    
        @Override
            public void keyPressed(KeyEvent e) {
                
                boolean pelaajaSiirtyi = false;
                boolean esinepaikkaVaihtui = false;

                if (!pause) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                            if (voiWarpata) {    
                                if (pelikenttä[p.sijX][p.sijY] instanceof Warp) {
                                    Warp warp = (Warp)pelikenttä[p.sijX][p.sijY];
                                    if (warp.annaSuunta() == Warp.Suunta.VASEN) {
                                        käyttö(esineValInt);
                                    }
                                    else {
                                        //pelaajaSiirtyi = p.kokeileLiikkumista("vasen");
                                        //pelaajaSiirtyi = true;
                                        p.liikeSuunta = "vasen";
                                        p.aloitaLiike(Suunta.VASEN);
                                    }
                                    voiWarpata = false;
                                }
                                else {
                                    //pelaajaSiirtyi = p.kokeileLiikkumista("vasen");
                                    p.liikeSuunta = "vasen";
                                    p.aloitaLiike(Suunta.VASEN);
                                }
                            }
                            else {
                                //pelaajaSiirtyi = p.kokeileLiikkumista("vasen");
                                //pelaajaSiirtyi = true;
                                p.liikeSuunta = "vasen";
                                p.aloitaLiike(Suunta.VASEN);
                            }
                            break;
                        case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                            if (voiWarpata) {    
                                if (pelikenttä[p.sijX][p.sijY] instanceof Warp) {
                                    Warp warp = (Warp)pelikenttä[p.sijX][p.sijY];
                                    if (warp.annaSuunta() == Warp.Suunta.OIKEA) {
                                        käyttö(esineValInt);
                                    }
                                    else {
                                        //pelaajaSiirtyi = p.kokeileLiikkumista("oikea");
                                        //pelaajaSiirtyi = true;
                                        p.liikeSuunta = "oikea";
                                        p.aloitaLiike(Suunta.OIKEA);
                                    }
                                    voiWarpata = false;
                                }
                                else {
                                    //pelaajaSiirtyi = p.kokeileLiikkumista("oikea");
                                    p.liikeSuunta = "oikea";
                                    p.aloitaLiike(Suunta.OIKEA);
                                }
                            }
                            else {
                                //pelaajaSiirtyi = p.kokeileLiikkumista("oikea");
                                //pelaajaSiirtyi = true;
                                p.liikeSuunta = "oikea";
                                p.aloitaLiike(Suunta.OIKEA);
                            }
                            break;
                        case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                            if (voiWarpata) {    
                                if (pelikenttä[p.sijX][p.sijY] instanceof Warp) {
                                    Warp warp = (Warp)pelikenttä[p.sijX][p.sijY];
                                    if (warp.annaSuunta() == Warp.Suunta.ALAS) {
                                        käyttö(esineValInt);
                                    }
                                    else {
                                        //pelaajaSiirtyi = p.kokeileLiikkumista("alas");
                                        //pelaajaSiirtyi = true;
                                        p.liikeSuunta = "alas";
                                        p.aloitaLiike(Suunta.ALAS);
                                    }
                                    voiWarpata = false;
                                }
                                else {
                                    //pelaajaSiirtyi = p.kokeileLiikkumista("alas");
                                    p.liikeSuunta = "alas";
                                    p.aloitaLiike(Suunta.ALAS);
                                }
                            }
                            else {
                                //pelaajaSiirtyi = p.kokeileLiikkumista("alas");
                                //pelaajaSiirtyi = true;
                                p.liikeSuunta = "alas";
                                p.aloitaLiike(Suunta.ALAS);
                            }
                            break;
                        case KeyEvent.VK_UP, KeyEvent.VK_W:
                            if (voiWarpata) {    
                                if (pelikenttä[p.sijX][p.sijY] instanceof Warp) {
                                    Warp warp = (Warp)pelikenttä[p.sijX][p.sijY];
                                    if (warp.annaSuunta() == Warp.Suunta.YLÖS) {
                                        käyttö(esineValInt);
                                    }
                                    else {
                                        //pelaajaSiirtyi = p.kokeileLiikkumista("ylös");
                                        //pelaajaSiirtyi = true;
                                        p.liikeSuunta = "ylös";
                                        p.aloitaLiike(Suunta.YLÖS);
                                    }
                                    voiWarpata = false;
                                }
                                else {
                                    //pelaajaSiirtyi = p.kokeileLiikkumista("ylös");
                                    p.liikeSuunta = "ylös";
                                    p.aloitaLiike(Suunta.YLÖS);
                                }
                            }
                            else {
                                //pelaajaSiirtyi = p.kokeileLiikkumista("ylös");
                                //pelaajaSiirtyi = true;
                                p.liikeSuunta = "ylös";
                                p.aloitaLiike(Suunta.YLÖS);
                            }
                            break;
                        case KeyEvent.VK_E:
                            poimi(p.sijX, p.sijY);
                            PääIkkuna.vaatiiKentänPäivityksen = true;
                            break;
                        case KeyEvent.VK_Q:
                            pudota(p.sijX, p.sijY, esineValInt);
                            PääIkkuna.vaatiiKentänPäivityksen = true;
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
                                        p.esineet[esineValInt] = Esine.yhdistä2Esinettä(p.esineet[esineValInt], p.esineet[yhdistettäväTavarapaikka]);
                                        p.esineet[yhdistettäväTavarapaikka] = null;
                                        PääIkkuna.hudTeksti.setText("Yhdistys onnistui! " + "Sait uuden esineen: " + p.esineet[esineValInt].annaNimiSijamuodossa("nominatiivi"));
                                    }
                                } 
                            }    
                            else if (tarkistaYhdistettävyys(esineValInt)) {
                                PääIkkuna.hudTeksti.setText("Mihin haluat yhdistää? (valitse tavarapaikka näppäimillä 1-5)");
                                PääIkkuna.osoitinLabel[esineValInt].setIcon(new ImageIcon("tiedostot/kuvat/osoitin_valittu.png"));
                                yhdistettäväTavarapaikka = esineValInt;
                                yhdistäminenKäynnissä = true;

                            }
                            break;

                        case KeyEvent.VK_X:
                            if (p.esineet[esineValInt] == null) {
                                PääIkkuna.hudTeksti.setText("Ei valittua esinettä");
                            }
                            else {
                                PääIkkuna.hudTeksti.setText(p.esineet[esineValInt].katso());
                            }
                            break;
                        case KeyEvent.VK_C:
                            if (pelikenttä[p.sijX][p.sijY] == null) {
                                PääIkkuna.hudTeksti.setText("Kohteessa ei ole mitään.");
                            }
                            else {
                                PääIkkuna.hudTeksti.setText(pelikenttä[p.sijX][p.sijY].katso());
                            }
                            break;
                        case KeyEvent.VK_R:
                            järjestäUudelleen();
                            break;
                        
                        case KeyEvent.VK_P:
                            pause = true;
                            PääIkkuna.hudTeksti.setText("Pause");
                            break;

                        case KeyEvent.VK_F:
                            erikoisKäyttö(esineValInt);
                            break;

                        case KeyEvent.VK_F2:
                            PääIkkuna.vaatiiPäivityksen = true;
                            PääIkkuna.uudelleenpiirräKaikki = true;
                            PääIkkuna.hudTeksti.setText("Ruudunpäivitys pakotettiin");
                            break;

                        case KeyEvent.VK_F3:
                            PääIkkuna.vaatiiKentänPäivityksen = true;
                            PääIkkuna.hudTeksti.setText("Kentänpäivitys pakotettiin");
                            break;

                        case KeyEvent.VK_F4:
                            PääIkkuna.näytäTiedot();
                            break;

                        default:
                            System.out.println("Näppäimellä "+ e.getKeyCode() + " ei ole toimintoa.");
                            break;
                    }
                    if (pelaajaSiirtyi) {
                        suoritaKohtaaminen();
                        //PääIkkuna.ylätekstiSij.setText("Pelaaja siirrettiin sijaintiin (" + p.sijX + ", " + p.sijY + ")");
                        //Main.pelaajanSijX = p.sijX;
                        //Main.pelaajanSijY = p.sijY;
                        PääIkkuna.vaatiiPäivityksen = true;
                        PääIkkuna.pelaajaSiirtyi = true;
                    }
                    else if (esinepaikkaVaihtui) {
                        vaihdaKuvakkeet(esineValInt, yhdistettäväTavarapaikka, yhdistäminenKäynnissä);
                        if (yhdistäminenKäynnissä) {
                            if (p.esineet[esineValInt] == null) {
                                PääIkkuna.hudTeksti.setText("Valittu esinepaikka " + esineValInt + " (tyhjä paikka)");
                            }
                            else {
                                PääIkkuna.hudTeksti.setText("Paina Z yhdistääksesi tavarapaikan " + esineValInt + " esineeseen " + p.esineet[esineValInt].annaNimi());
                            }
                        }
                        else {
                            if (p.esineet[esineValInt] == null) {
                                PääIkkuna.hudTeksti.setText("Valittu esinepaikka " + esineValInt);
                            }
                            else {
                                PääIkkuna.hudTeksti.setText("Valittu esinepaikka " + esineValInt + ": " + p.esineet[esineValInt].annaNimiSijamuodossa("nominatiivi"));
                            }
                            //PääIkkuna.hudTeksti.setText("Valittu esinepaikka " + (p.esineet[esineValInt] == null ? ": " + p.esineet[esineValInt].annaNimi() : ""));
                        }
                    }
                    // String kohdeteksti = "";
                    // String maastoTeksti = "";
                    // if (pelikenttä[p.sijX][p.sijY] == null) {
                    //     //PääIkkuna.ylätekstiKohde.setText("Kohteessa ei ole mitään");
                    //     kohdeteksti = "Kohteessa ei ole mitään";
                    // }
                    // else {
                    //     //PääIkkuna.ylätekstiKohde.setText("Kohteessa on " + pelikenttä[p.sijX][p.sijY].annaNimi());
                    //     kohdeteksti = "Kohteessa on " + pelikenttä[p.sijX][p.sijY].annaNimiSijamuodossa("nominatiivi");
                    // }
                    // if (maastokenttä[p.sijX][p.sijY] == null) {
                    //     maastoTeksti = "Maasto: normaali";
                    // }
                    // else {
                    //     maastoTeksti = "Maasto: " + maastokenttä[p.sijX][p.sijY].annaNimi();
                    // }
                    // PääIkkuna.ylätekstiKohde.setText("" + kohdeteksti + ", " + maastoTeksti);
                }
                else {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_P:
                            pause = false;
                            PääIkkuna.hudTeksti.setText("Peli jatkuu.");
                            break;
                    }
                }
            }
    
        @Override
            public void keyReleased(KeyEvent e) {
                
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                        p.lopetaLiike(Suunta.VASEN);
                        break;
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                        p.lopetaLiike(Suunta.OIKEA);
                        break;
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                        p.lopetaLiike(Suunta.ALAS);
                        break;
                    case KeyEvent.VK_UP, KeyEvent.VK_W:
                        p.lopetaLiike(Suunta.YLÖS);
                        break;
                    }
            }
    }

    class MenuKontrollit implements KeyListener {

        /*
         * Määritellään mitä eri näppäinkomennot tekee ja mitä metodeja kutsutaan
         */

        @Override
            public void keyTyped(KeyEvent e) {
                
            }
    
        @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                        ValikkoRuutu.painaNäppäintä("alas");
                        break;
                    case KeyEvent.VK_UP, KeyEvent.VK_W:
                        ValikkoRuutu.painaNäppäintä("ylös");
                        break;
                    case KeyEvent.VK_SPACE, KeyEvent.VK_ENTER:
                        ValikkoRuutu.painaNäppäintä("enter");
                        break;
                    default:
                        System.out.println("Näppäimellä "+ e.getKeyCode() + " ei ole toimintoa.");
                        break;
                }
            }
    
        @Override
            public void keyReleased(KeyEvent e) {
                
            }
    }

    static void suljePeli(int sulkuTapa) {
        peliKäynnissä = false;
        pause = true;
        boolean guit = false;
        for (int i = 0; i < Pelaaja.esineet.length; i++) {
            Pelaaja.esineet[i] = null;
        }
        switch (sulkuTapa) {
            case 0:
                TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.NORMAALI_VOITTO;
                PääIkkuna.crd.next(PääIkkuna.kortit);
                break;
            case 1:
                //TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA;
                PääIkkuna.crd.next(PääIkkuna.kortit);
                break;
            case 2:
                TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.YLENSYÖNTI;
                PääIkkuna.crd.next(PääIkkuna.kortit);
                break;
            default:
                PääIkkuna.ikkuna.dispose();
                break;
        }
        LoppuRuutu.valitseLoppuRuutu(TarkistettavatArvot.pelinLoppuSyy);

        ÄänentoistamisSäie.musiikkiSoitin.stop();
        gThread.stop();
        gThread.päivitysTiheys.stop();
        gThread.sekuntiKello.stop();
        //gThread.peliKentänPäivittäjä.cancel(true);
        //gThread.ajastimenPäivittäjä.cancel(true);
        sThread.stop();
        //PeliKenttäMetodit.päivitysTiheys.stop();
        //ajastin.stop();
        tThread.stop();
        globaaliAika = 0;
        if (guit) {
            System.exit(0);
        }
    }

    static boolean lataaHuone(int huoneenId) {
        try{
            huone = huoneKartta.get(huoneenId);
            pelikenttä = huone.annaHuoneenKenttäSisältö();
            maastokenttä = huone.annaHuoneenMaastoSisältö();
            npcLista.clear();
            for (NPC[] nn : huone.annaHuoneenNPCSisältö()) {
                for (NPC n : nn) {
                    if (n != null) {
                        npcLista.add(n);
                    }
                }
            }
            //Main.pelikenttä = pelikenttä;
            //Main.maastokenttä = maastokenttä;
            if (huone.annaTausta() != null) {
                GrafiikanPäivitysSäie.uusiTausta = huone.annaTausta();
            }
            PääIkkuna.hudTeksti.setText("Ladattiin huone " + huone.annaNimi() + " (ID: " + huone.annaId() + ")");
            PääIkkuna.uudelleenpiirräKaikki = true;
            if (huoneKartta.get(huoneenId).näytäAlkuDialogi) {
                pause = true;
                CustomViestiIkkunat.HuoneenVaihtoDialogi.showDialog("" + huoneKartta.get(huoneenId).alkuDialogi);
                pause = false;
            }
            return true;
        }
        catch (NullPointerException e) {
            pause = true;
            JOptionPane.showMessageDialog(null, "Yritettiin warpata huoneeseen " + huoneenId + ", jota ei ole olemassa.", "Huonetta ei löytynyt.", JOptionPane.ERROR_MESSAGE);
            pause = false;
            return false;
        }
    }

    static void luoHuone(int huoneenId, String huoneenNimi, String huoneenTaustanPolku, String huoneenAlue, ArrayList<KenttäKohde> huoneenKenttäSisältö, ArrayList<Maasto> huoneenMaastoSisältö, ArrayList<NPC> huoneenNPCSisältö, boolean näytäAlkuDialogi, String alkuDialogi) {

        Huone huone = new Huone(huoneenId, kentänKoko, huoneenNimi, huoneenTaustanPolku, huoneenAlue, huoneenKenttäSisältö, huoneenMaastoSisältö, huoneenNPCSisältö, näytäAlkuDialogi, alkuDialogi);
        huoneKartta.put(huoneenId, huone);
    }

    static void pelaajanLiike() {
        if (Pelaaja.liikutaPelaajaa()) {
            suoritaKohtaaminen();
            päivitäPelaajanSijaintiTiedot();
            PääIkkuna.vaatiiPäivityksen = true;
            PääIkkuna.pelaajaSiirtyi = true;
        }
    }

    static void päivitäPelaajanSijaintiTiedot() {
        String kohdeteksti = "";
        if (pelikenttä[p.sijX][p.sijY] == null) {
            kohdeteksti = "Kohteessa ei ole mitään";
        }
        else {
            kohdeteksti = "Kohteessa on " + pelikenttä[p.sijX][p.sijY].annaNimiSijamuodossa("nominatiivi");
        }
        PääIkkuna.ylätekstiSij.setText("Pelaaja siirrettiin sijaintiin " + Pelaaja.sijX + ", " + Pelaaja.sijY + " (" + Pelaaja.hitbox.getMinX() + "-" + Pelaaja.hitbox.getMaxX() + ", " + Pelaaja.hitbox.getMinY() + "-" + Pelaaja.hitbox.getMaxY() + ")");
        PääIkkuna.ylätekstiKohde.setText("" + kohdeteksti);
    }

    /**
         * Pelin päämetodi, jota ajetaan uudelleen niin kauan, kun peli on käynnissä.
         * Suoritusten jälkeen peli palaa aina takaisin "alkuvalikkoon".
         */

    static void pelinKulku() {

        if (huoneVaihdettava) {
            lataaHuone(uusiHuone);
            huoneVaihdettava = false;
            PääIkkuna.vaatiiKentänPäivityksen = true;
        }
        
        // if (peliHävitty) {
        //     suljePeli(1);
        // }

        if (PääIkkuna.uusiIkkuna) {
            suljePeli(3);
            PääIkkuna.uusiIkkuna = false;
            uusiHuone = 0;
            huoneVaihdettava = true;
            uusiPeli();
        }

        peliLäpäisty = tarkistaPelinTila();
        if (peliLäpäisty || PääIkkuna.uusiIkkuna) {
            suljePeli(0);
            peliKäynnissä = false;
        }
    }

    static Timer tarkistaTarviikoPeliUudelleenkäynnistää = new Timer(100, e -> {
        if (PääIkkuna.uusiIkkuna) {
            System.out.println("uusi peli");
            suljePeli(3);
            PääIkkuna.uusiIkkuna = false;
            uusiHuone = 0;
            huoneVaihdettava = true;
            uusiPeli();
        }
    });

    static long globaaliAika = 0;

    public static void uusiPeli() {
        
        kentänKoko = TarkistettavatArvot.uusiKentänKoko;
        kentänYläraja = kentänAlaraja + kentänKoko - 1;
        pause = true;
        peliKäynnissä = true;
        new Peli();
    }

    private static String format(BigDecimal x) {
        NumberFormat formatter = new DecimalFormat("0.0E0");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits((x.scale() > 0) ? x.precision() : x.scale());
        return formatter.format(x);
    }

    public Peli() {
        
        PääIkkuna.luoPääikkuna();
        PääIkkuna.ikkuna.addKeyListener(new Näppäinkomennot());
        PääIkkuna.vaatiiPäivityksen = true;
        PääIkkuna.vaatiiKentänPäivityksen = true;
        ValikkoRuutu.valikkoPaneli.addKeyListener(new MenuKontrollit());
        ValikkoRuutu.valikkoPaneli.requestFocus();
        
        AikaSäie.kulunutAika = 0;
        GrafiikanPäivitysSäie.AjastimenPäivittäjä.kulunutAika = 0;
        Peli.peliAloitettu = false;

        huoneKartta = HuoneLista.luoVakioHuoneKarttaTiedostosta();
        p = new Pelaaja();
        uusiHuone = 0;
        huoneVaihdettava = true;

        PääIkkuna.luoAlkuIkkuna(0, 0, new ImageIcon("tiedostot/kuvat/pelaaja.png"));
        PääIkkuna.päivitäIkkuna();

        Thread isoNumeroSäie = new Thread() {
            public void run() {
                System.out.println("Lasketaan päivän iso numero... ");
                BigDecimal bd = new BigDecimal(Math.pow(2.0f, 63.0f));
                int randomMäärä = r.nextInt(20);
                int randomMäärä2 = r.nextInt(20);
                for (int i = 0; i < randomMäärä; i++) {
                    bd = bd.multiply(bd).multiply(new BigDecimal(randomMäärä2));
                }
                System.out.println("Päivän iso numero: " + format(new BigDecimal("" + bd)));
            }
        };
        isoNumeroSäie.start();

        if (!tarkistaTarviikoPeliUudelleenkäynnistää.isRunning()) {
            tarkistaTarviikoPeliUudelleenkäynnistää.start();
        }
        gThread = new GrafiikanPäivitysSäie();
        sThread = new ÄänentoistamisSäie();
        tThread = new AikaSäie();
        gThread.start();
        sThread.start();
        tThread.start();
    }

}
