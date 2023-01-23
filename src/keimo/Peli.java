package keimo;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.text.DecimalFormat;
import java.awt.event.*;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;

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
    static String häviönSyy = "";
    static int aloitusHp = 10;
    public static int huoneidenMäärä = 0;

    int esineitäKentällä = 0;
    Random r = new Random();
    Pelaaja p = new Pelaaja();
    Scanner sc = new Scanner(System.in);
    boolean peliLäpäisty = false;
    boolean peliKäynnissä = true;
    boolean peliHävitty = false;
    int valinta;
    int valinta2;
    Esine valittuEsine;
    int toiminto;
    int valX;
    int valY;
    int esineValInt = 0;
    DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
    boolean voiWarpata = false;
    
    

    GrafiikanPäivitysSäie gThread = new GrafiikanPäivitysSäie();
    ÄänentoistamisSäie sThread = new ÄänentoistamisSäie();
    AikaSäie tThread = new AikaSäie();

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
                PääIkkuna.hudTeksti.setText("Sait uuden esineen: " + pelikenttä[x][y].annaNimi());
                pelikenttä[x][y] = null;
            }
            else {
                PääIkkuna.hudTeksti.setText("Ei voida poimia! Tavaraluettelo täynnä! Kokeile pudottaa jokin esine tyhjään ruutuun");
            }
        }
        else if (pelikenttä[x][y] instanceof Ämpärikone) {
            annaÄmpäri();
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
                PääIkkuna.hudTeksti.setText(p.esineet[esineVal].annaNimi() + " pudotettiin tavaraluettelosta ruutuun " + x + ", " + y);
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
            PääIkkuna.hudTeksti.setText("" + p.esineet[valinta].annaNimi() + " ei sovi yhdistettäväksi!");
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
            if (p.esineet[val1].kelvollisetYhdistettävät.contains(p.esineet[val2].annaNimi())) {
                return true;
            }
            else {
                PääIkkuna.hudTeksti.setText("Näitä esineitä ei voi yhdistää keskenään");
                return false;
            }
        }
    }
    /**
     * Yhdistää kaksi esinettä: poistaa esineet ja antaa tilalle yhdistelmän tuloksen.
     * Aseta vanhoille tavaraluettelon paikoille null ja luo uusi esine
     * @.pre {
     * @param a instanceof Esine &&
     * @param b instanceof Esine
     * }
     * @.post p.esineet[a] instanceof Esine && p.esineet[b] == null
     */
    void yhdistäEsineet(int a, int b) {
        if ((p.esineet[a].annaNimi() == "Kaasupullo" && p.esineet[b].annaNimi() == "Tyhjä kaasusytytin") || (p.esineet[a].annaNimi() == "Tyhjä kaasusytytin" && p.esineet[b].annaNimi() == "Kaasupullo")) {
            p.esineet[a] = new Kaasusytytin("toimiva");
            p.esineet[b] = null;
        }
        PääIkkuna.hudTeksti.setText("Yhdistys onnistui! " + "Sait uuden esineen: " + p.esineet[a].annaNimi());
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

    void annaÄmpäri() {
        if (p.annaEsineidenMäärä() < p.annaTavaraluettelonKoko()) {
            for (int i = 0; i < p.esineet.length; i++) {
                if (p.esineet[i] == null) {
                    p.esineet[i] = new Vesiämpäri();
                    break;
                }
            }
            PääIkkuna.hudTeksti.setText("Sait uuden ämpärin");
        }
        else {
            PääIkkuna.hudTeksti.setText("Ei voida poimia! Tavaraluettelo täynnä! Kokeile pudottaa jokin esine tyhjään ruutuun");
        }
    }

    void käyttö(int esine) {
        voiWarpata = false;
        valittuEsine = p.esineet[esine];
        if (pelikenttä[p.sijX][p.sijY] instanceof Ämpärikone) {
            annaÄmpäri();
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

            if (pelikenttä[valX][valY] instanceof Kiintopiste) {
                if (valittuEsine.sopiiKäytettäväksi.contains(pelikenttä[valX][valY].annaNimi())) {
                    
                    valittuEsine =  p.esineet[esine];
                    if (valittuEsine instanceof Makkara) {
                        if (pelikenttä[valX][valY] instanceof Nuotio) {
                            Nuotio nuotio = (Nuotio)pelikenttä[valX][valY];
                            if (nuotio.onSytytetty()) {
                                Makkara makkara = (Makkara)valittuEsine;
                                makkara.paista();
                            }
                            else {
                                PääIkkuna.hudTeksti.setText("Makkaraa ei voi paistaa ennen kuin nuotio on sytytetty.");
                            }
                        }
                        else {
                            PääIkkuna.hudTeksti.setText(valittuEsine.annaNimiSijamuodossa("allatiivi") + " saattaa olla käyttöä toisessa kohteessa.");
                        }
                    }
                    else {
                        PääIkkuna.hudTeksti.setText(valittuEsine.käytä());
                        PääIkkuna.hudTeksti.setText(pelikenttä[valX][valY].kokeileEsinettä(valittuEsine));
                        Kiintopiste kp = (Kiintopiste)pelikenttä[valX][valY];
                        if (valittuEsine.sopiiKäytettäväksi.contains(kp.annaNimi()) && kp.vuorovaikutusToimii()) {
                            valittuEsine.poista = true;
                        }
                    }
                }
                else {
                    PääIkkuna.hudTeksti.setText(valittuEsine.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + pelikenttä[valX][valY].annaNimiSijamuodossa("illatiivi"));
                }
            }
            else if (pelikenttä[valX][valY] instanceof NPC) {
                if (pelikenttä[valX][valY] instanceof Vihollinen) {
                    Vihollinen vihollinen = (Vihollinen)pelikenttä[valX][valY];
                    if (vihollinen.tehoavatAseet.contains(valittuEsine.annaNimi())) {
                        PääIkkuna.hudTeksti.setText(valittuEsine.käytä());
                        vihollinen.kukista();
                    }
                    else {
                        PääIkkuna.hudTeksti.setText(valittuEsine.annaNimi() + " ei tehonnut " + vihollinen.annaNimiSijamuodossa("illatiivi"));
                    }
                }
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
        if (valittuEsine.poistoon()) {
            p.esineet[esine] = null;
        }
        PääIkkuna.vaatiiKentänPäivityksen = true;
    }

    void suoritaKohtaaminen() {
        if (pelikenttä[p.sijX][p.sijY] instanceof NPC) {
            NPCKohtaaminen();
        }
        else if (pelikenttä[p.sijX][p.sijY] instanceof ReunaWarppi) {
            voiWarpata = true;
        }
    }

    void NPCKohtaaminen() {
        if (pelikenttä[p.sijX][p.sijY] instanceof Vihollinen) {
            vihollisKohtaaminen();
        }
    }

    void vihollisKohtaaminen() {
        if (pelikenttä[p.sijX][p.sijY] instanceof Vihollinen) {
            Vihollinen vihu = (Vihollinen)pelikenttä[p.sijX][p.sijY];
            if (vihu.onkoKukistettu() || ((p.esineet[esineValInt] instanceof Kilpi) && vihu.kilpiTehoaa)) {
                
            }
            else {
                p.vahingoita(vihu.vahinko);
                PääIkkuna.hudTeksti.setText("Osuit viholliseen! Menetit " + vihu.vahinko +" elämäpisteen.");
            }
        }
    }

    // Tarkistetaan, onko pelin tavoitteet suoritettu.
    // Jos on, peli päättyy ja ikkuna sulkeutuu.
    boolean tarkistaPelinTila(){
        PääIkkuna.tavoiteTeksti1.setText("Tavoite 1: Sytytä nuotio - suoritettu: " + (tarkistaTavoiteNuotio() ? "Kyllä" : "Ei"));
        PääIkkuna.tavoiteTeksti2.setText("Tavoite 2: Avaa kirstu - suoritettu: " + (tarkistaTavoiteKirstu() ? "Kyllä" : "Ei"));
        PääIkkuna.tavoiteTeksti3.setText("Tavoite 3: Syö - suoritettu: " + (tarkistaTavoiteRuoka() ? "Kyllä" : "Ei"));
        if (p.syödytRuoat >= 4) {
            häviönSyy = "Söit liikaa ja sinulle tuli paha olo.";
            peliHävitty = true;
        }

        if (p.hp <= 0) {
            häviönSyy = "Sait selkääsi!";
            peliHävitty = true;
        }

        if (tarkistaTavoiteNuotio() && tarkistaTavoiteKirstu() && tarkistaTavoiteRuoka()) {
            return true;
        }
        else {
            return false;
        }
    }
    boolean tarkistaTavoiteNuotio() {
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
    boolean tarkistaTavoiteKirstu(){
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
    boolean tarkistaTavoiteRuoka(){
        boolean result = false;
        if (p.kylläinen) {
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
                                        pelaajaSiirtyi = p.kokeileLiikkumista("vasen");
                                        //pelaajaSiirtyi = true;
                                    }
                                    voiWarpata = false;
                                }
                                else {
                                    pelaajaSiirtyi = p.kokeileLiikkumista("vasen");
                                }
                            }
                            else {
                                pelaajaSiirtyi = p.kokeileLiikkumista("vasen");
                                //pelaajaSiirtyi = true;
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
                                        pelaajaSiirtyi = p.kokeileLiikkumista("oikea");
                                        //pelaajaSiirtyi = true;
                                    }
                                    voiWarpata = false;
                                }
                                else {
                                    pelaajaSiirtyi = p.kokeileLiikkumista("oikea");
                                }
                            }
                            else {
                                pelaajaSiirtyi = p.kokeileLiikkumista("oikea");
                                //pelaajaSiirtyi = true;
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
                                        pelaajaSiirtyi = p.kokeileLiikkumista("alas");
                                        //pelaajaSiirtyi = true;
                                    }
                                    voiWarpata = false;
                                }
                                else {
                                    pelaajaSiirtyi = p.kokeileLiikkumista("alas");
                                }
                            }
                            else {
                                pelaajaSiirtyi = p.kokeileLiikkumista("alas");
                                //pelaajaSiirtyi = true;
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
                                        pelaajaSiirtyi = p.kokeileLiikkumista("ylös");
                                        //pelaajaSiirtyi = true;
                                    }
                                    voiWarpata = false;
                                }
                                else {
                                    pelaajaSiirtyi = p.kokeileLiikkumista("ylös");
                                }
                            }
                            else {
                                pelaajaSiirtyi = p.kokeileLiikkumista("ylös");
                                //pelaajaSiirtyi = true;
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
                                        yhdistäEsineet(yhdistettäväTavarapaikka, esineValInt);
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
                        PääIkkuna.ylätekstiSij.setText("Pelaaja siirrettiin sijaintiin (" + p.sijX + ", " + p.sijY + ")");
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
                                PääIkkuna.hudTeksti.setText("Valittu esinepaikka " + esineValInt + ": " + p.esineet[esineValInt].annaNimi());
                            }
                        }
                    }
                    String kohdeteksti = "";
                    String maastoTeksti = "";
                    if (pelikenttä[p.sijX][p.sijY] == null) {
                        //PääIkkuna.ylätekstiKohde.setText("Kohteessa ei ole mitään");
                        kohdeteksti = "Kohteessa ei ole mitään";
                    }
                    else {
                        //PääIkkuna.ylätekstiKohde.setText("Kohteessa on " + pelikenttä[p.sijX][p.sijY].annaNimi());
                        kohdeteksti = "Kohteessa on " + pelikenttä[p.sijX][p.sijY].annaNimi();
                    }
                    if (maastokenttä[p.sijX][p.sijY] == null) {
                        maastoTeksti = "Maasto: normaali";
                    }
                    else {
                        maastoTeksti = "Maasto: " + maastokenttä[p.sijX][p.sijY].annaNimi();
                    }
                    PääIkkuna.ylätekstiKohde.setText("" + kohdeteksti + ", " + maastoTeksti);
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

    void suljePeli(int sulkuTapa) {
        peliKäynnissä = false;
        for (int i = 0; i < p.esineet.length; i++) {
            p.esineet[i] = null;
        }
        switch (sulkuTapa) {
            case 0:
                CustomViestiIkkunat.Loppuonnittelu.showDialog(GrafiikanPäivitysSäie.AjastimenPäivittäjä.kulunutAika);
                PääIkkuna.ikkuna.dispose();
                break;
            case 1:
                int valinta = CustomViestiIkkunat.LoppuHäviö.showDialog(häviönSyy);
                switch (valinta) {
                    case JOptionPane.OK_OPTION:
                        peliKäynnissä = true;    
                        PääIkkuna.uusiIkkuna = true;
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        suljePeli(2);
                        break;
                }
                PääIkkuna.ikkuna.dispose();
                break;
            default:
                PääIkkuna.ikkuna.dispose();
                break;
        }
        ÄänentoistamisSäie.musiikkiSoitin.stop();
        gThread.stop();
        gThread.päivitysTiheys.stop();
        gThread.sekuntiKello.stop();
        //gThread.peliKentänPäivittäjä.cancel(true);
        //gThread.ajastimenPäivittäjä.cancel(true);
        sThread.stop();
        tThread.stop();
        PeliKenttäMetodit.päivitysTiheys.stop();
    }

    boolean lataaHuone(int huoneenId) {
        try{
            Huone huone = huoneKartta.get(huoneenId);
            pelikenttä = huone.annaHuoneenKenttäSisältö();
            maastokenttä = huone.annaHuoneenMaastoSisältö();
            //Main.pelikenttä = pelikenttä;
            //Main.maastokenttä = maastokenttä;
            if (huone.annaTausta() != null) {
                GrafiikanPäivitysSäie.uusiTausta = new ImageIcon(huone.annaTausta());
            }
            PääIkkuna.hudTeksti.setText("Ladattiin huone " + huone.annaNimi() + " (ID: " + huone.annaId() + ")");
            PääIkkuna.uudelleenpiirräKaikki = true;
            if (huoneKartta.get(huoneenId).näytäAlkuDialogi) {
                pause = true;
                CustomViestiIkkunat.HuoneenVaihtoDialogi.showDialog("" + huoneKartta.get(huoneenId).alkuDialogi);
                pause = false;
            }
            PääIkkuna.alueInfoLabel.setText(huone.annaAlue());
            return true;
        }
        catch (NullPointerException e) {
            pause = true;
            JOptionPane.showMessageDialog(null, "Yritettiin warpata huoneeseen " + huoneenId + ", jota ei ole olemassa.", "Huonetta ei löytynyt.", JOptionPane.ERROR_MESSAGE);
            pause = false;
            return false;
        }
    }

    static void luoHuone(int huoneenId, String huoneenNimi, Image huoneenTausta, String huoneenAlue, ArrayList<KenttäKohde> huoneenKenttäSisältö, ArrayList<Maasto> huoneenMaastoSisältö, boolean näytäAlkuDialogi, String alkuDialogi) {

        Huone huone = new Huone(huoneenId, kentänKoko, huoneenNimi, huoneenTausta, huoneenAlue, huoneenKenttäSisältö, huoneenMaastoSisältö, näytäAlkuDialogi, alkuDialogi);
        huoneKartta.put(huoneenId, huone);
    }

    /**
         * Pelin päämetodi, jota ajetaan uudelleen niin kauan, kun peli on käynnissä.
         * Suoritusten jälkeen peli palaa aina takaisin "alkuvalikkoon".
         */

    void pelinKulku() {

        if (huoneVaihdettava) {
            lataaHuone(uusiHuone);
            huoneVaihdettava = false;
            PääIkkuna.vaatiiKentänPäivityksen = true;
        }

        //if (Main.korjaaObjektiDesync) {
            //pelikenttä = Main.pelikenttä;
            //maastokenttä = Main.maastokenttä;
        //    Main.korjaaObjektiDesync = false;
        //}
        
        if (peliHävitty) {
            suljePeli(1);
        }

        if (PääIkkuna.uusiIkkuna) {
            suljePeli(2);
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

    void taustaToimet() {
        if (PääIkkuna.uusiIkkuna) {
            suljePeli(2);
            PääIkkuna.uusiIkkuna = false;
            uusiHuone = 0;
            huoneVaihdettava = true;
            uusiPeli();
        }
    }

    Timer ajastin = new Timer(10, e -> {
        if (peliKäynnissä && !pause) {
            pelinKulku();
        }
        taustaToimet();
    });

    public static void uusiPeli() {
        
        kentänKoko = TarkistettavatArvot.uusiKentänKoko;
        kentänYläraja = kentänAlaraja + kentänKoko - 1;
        pause = true;
        new Peli();
    }

    public Peli() {

        //pelikenttä = Main.pelikenttä;
        //maastokenttä = Main.maastokenttä;
        
        PääIkkuna.luoPääikkuna();
        PääIkkuna.ikkuna.addKeyListener(new Näppäinkomennot());
        PääIkkuna.vaatiiPäivityksen = true;
        PääIkkuna.vaatiiKentänPäivityksen = true;
        ValikkoRuutu.valikkoPaneli.addKeyListener(new MenuKontrollit());
        ValikkoRuutu.valikkoPaneli.requestFocus();
        
        AikaSäie.kulunutAika = 0;
        GrafiikanPäivitysSäie.AjastimenPäivittäjä.kulunutAika = 0;
        Peli.peliAloitettu = false;

        luoHuone(0, "Puisto_keski", Toolkit.getDefaultToolkit().createImage("tiedostot/kuvat/taustat/tausta_puisto_keski.png"), "Puisto", HuoneLista.huone0Kenttä, HuoneLista.huone0Maasto, false, "");
        luoHuone(1, "Puisto_ylä", Toolkit.getDefaultToolkit().createImage("tiedostot/kuvat/taustat/tausta_puisto_keski.png"), "Puisto", HuoneLista.huone1Kenttä, HuoneLista.huone1Maasto, false, "");
        luoHuone(2, "Puisto_ala", Toolkit.getDefaultToolkit().createImage("tiedostot/kuvat/taustat/tausta_puisto_keski.png"), "Puisto", HuoneLista.huone2Kenttä, HuoneLista.huone2Maasto, true, "Keimon koti ei ole tässä suunnassa.");
        luoHuone(3, "Puisto_oikea", Toolkit.getDefaultToolkit().createImage("tiedostot/kuvat/taustat/tausta_puisto_keski.png"), "Puisto", HuoneLista.huone3Kenttä, HuoneLista.huone3Maasto, false, "");
        luoHuone(4, "Puisto_vasen", Toolkit.getDefaultToolkit().createImage("tiedostot/kuvat/taustat/tausta_puisto_keski.png"), "Puisto", HuoneLista.huone4Kenttä, HuoneLista.huone4Maasto, false, "");
        luoHuone(5, "Asuintalot_parkkipaikka", Toolkit.getDefaultToolkit().createImage("tiedostot/kuvat/taustat/tausta_asuintalot_parkkipaikka.png"), "Asuintalot", HuoneLista.huone5Kenttä, HuoneLista.huone5Maasto, false, "");
        PääIkkuna.luoAlkuIkkuna(0, 0, new ImageIcon("tiedostot/kuvat/pelaaja.png"));
        PääIkkuna.päivitäIkkuna();

        ajastin.start();
        //if (!gThread.säieKäynnissä) {
            gThread.start();
        //}
        sThread.start();
        //tThread.start();
        PeliKenttäMetodit.päivitysTiheys.start();

    }

}
