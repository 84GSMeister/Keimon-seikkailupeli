import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import java.awt.event.*;

public class Peli {
    
    /**
     * Puutteet sovelluksessa:
     *  -joitain luokkia ei kommentoitu
     *  -vaillinaiset tai puutuvat poikkeuksenkäsittelijät joissakin syötettä vaativissa metodeissa
     *  -esineiden käytä() -metodeissa bugeja ja puuttuvia toteutuksia
     */
    
    //final int kentänKoko = 10;
    //final int kentänAlaraja = 0;
    //final int kentänYläraja = kentänAlaraja + kentänKoko - 1;
    KenttäKohde[][] pelikenttä = Main.pelikenttä;
    int esineitäKentällä = 0;
    //Tarkastettava[][] pelikenttä = new Tarkastettava[Main.kentänKoko][Main.kentänKoko];
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
    double aika = 0f;
    DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

    GrafiikanPäivitysSäie gThread = new GrafiikanPäivitysSäie();
    ÄänentoistamisSäie sThread = new ÄänentoistamisSäie();

    /**
     * Arpoo satunnaisesti pelikentän x- ja y-koordinaatit.
     * Lisää arvottuun kohtaan syötteenä saadun Tarkastetta-tyyppisen olion
     * eli jonkin Esine-luokan tai Kiintopiste-luokan alaluokan olioista.
     * @.pre {
     * @param t instanceof Tarkastettava
     * }
     * @.post pelikenttä[randX][randY] != null
     */

    void sijoitaSatunnaiseenRuutuun(KenttäKohde t){
        int randX = r.nextInt(Main.kentänKoko);
        int randY = r.nextInt(Main.kentänKoko);
        if (pelikenttä[randX][randY] == null) {
            pelikenttä[randX][randY] = t;
            esineitäKentällä++;
        }
        else {
            if (esineitäKentällä < Main.kentänKoko * Main.kentänKoko) {
                sijoitaSatunnaiseenRuutuun(t);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

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
        else if (!p.esineet[valinta].yhdistettävä) {
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
            p.esineet[a] = new Kaasusytytin();
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

    void käytäEsinettä(int esine) {
        
        if (pelikenttä[p.sijX][p.sijY] instanceof Ämpärikone) {
            annaÄmpäri();
        }
        else {
            if (p.esineet[esine] == null) {
                PääIkkuna.hudTeksti.setText("Ei valittua esinettä.");
            }
            else {
                valittuEsine =  p.esineet[esine];
                if (valittuEsine.onkoKenttäkäyttöön()) {
                    valX = p.sijX;
                    valY = p.sijY;

                    if (pelikenttä[valX][valY] instanceof Kiintopiste) {
                        if (valittuEsine.sopiiKäytettäväksi.contains(pelikenttä[valX][valY].annaNimi())) {
                            PääIkkuna.hudTeksti.setText(valittuEsine.käytä());
                            PääIkkuna.hudTeksti.setText(pelikenttä[valX][valY].kokeileEsinettä(valittuEsine));
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
                        }
                    }
                    else if (pelikenttä[valX][valY] == null) {
                        PääIkkuna.hudTeksti.setText("Kohteessa ei ole mitään mihin käyttää esinettä");
                    }
                    else {
                        PääIkkuna.hudTeksti.setText("Ei voi käyttää kentällä lojuviin esineisiin. Kokeile etsiä muita kiintopisteitä.");
                    }
                }
                else{
                    PääIkkuna.hudTeksti.setText(valittuEsine.käytä());
                }
                if (valittuEsine instanceof Ruoka) {
                    Ruoka ruoka = (Ruoka)valittuEsine;
                    valittuEsine.käytä();
                    p.syöRuoka(ruoka.annaParannusMäärä());
                }
                if (valittuEsine.poistoon()){
                    p.esineet[esine] = null;
                }
            }
        }
        
    }

    void erikoisKäyttö(int esine) {
        if (p.esineet[esine] == null) {
            PääIkkuna.hudTeksti.setText("Ei valittua esinettä.");
        }
        else if (p.esineet[esine].erikoiskäyttö) {
            valittuEsine =  p.esineet[esine];
            if (valittuEsine instanceof Makkara) {
                if (pelikenttä[p.sijX][p.sijY] instanceof Nuotio){
                    Nuotio nuotio = (Nuotio)pelikenttä[p.sijX][p.sijY];
                    if (nuotio.onSytytetty()) {
                        Makkara makkara = (Makkara)valittuEsine;
                        makkara.paista();
                    }
                    else {
                        PääIkkuna.hudTeksti.setText("Makkaraa ei voi paistaa ennen kuin nuotio on sytytetty.");
                    }
                }
                else {
                    PääIkkuna.hudTeksti.setText("Tällä esineellä on ehkä erikoiskäyttö jossain muussa kohteessa.");
                }
            }
        }
        else {
            PääIkkuna.hudTeksti.setText("Tällä esineellä ei ole erikoiskäyttöä.");;
        }
    }

    void suoritaKohtaaminen() {
        if (pelikenttä[p.sijX][p.sijY] instanceof NPC) {
            NPCKohtaaminen();
        }
    }

    void NPCKohtaaminen() {
        if (pelikenttä[p.sijX][p.sijY] instanceof Vihollinen) {
            vihollisKohtaaminen();
        }
    }

    void vihollisKohtaaminen() {
        if (pelikenttä[p.sijX][p.sijY] instanceof PikkuVihu) {
            PikkuVihu pikkuVihu = (PikkuVihu)pelikenttä[p.sijX][p.sijY];
            if (pikkuVihu.onkoKukistettu() || (p.esineet[esineValInt] instanceof Kilpi)) {
                
            }
            else {
                p.vahingoita(pikkuVihu.vahinko);
                PääIkkuna.hudTeksti.setText("Osuit viholliseen! Menetit " + pikkuVihu.vahinko +" elämäpisteen.");
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
            Main.häviönSyy = "Söit liikaa ja sinulle tuli paha olo.";
            peliHävitty = true;
        }

        if (p.hp <= 0) {
            Main.häviönSyy = "Sait selkääsi!";
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
        for (int i = 0; i < Main.kentänKoko; i++) {
            for (int j = 0; j < Main.kentänKoko; j++) {
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

        for (int i = 0; i < Main.kentänKoko; i++) {
            for (int j = 0; j < Main.kentänKoko; j++) {
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

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                        //p.siirry("vasen");
                        p.siirry(new LiikkuminenVasemmalle());
                        pelaajaSiirtyi = true;
                        break;
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                        //p.siirry("oikea");
                        p.siirry(new LiikkuminenOikealle());
                        pelaajaSiirtyi = true;
                        break;
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                        //p.siirry("ylös");
                        p.siirry(new LiikkuminenYlös());
                        pelaajaSiirtyi = true;
                        break;
                    case KeyEvent.VK_UP, KeyEvent.VK_W:
                        //p.siirry("alas");
                        p.siirry(new LiikkuminenAlas());
                        pelaajaSiirtyi = true;
                        break;
                    case KeyEvent.VK_E:
                        poimi(p.sijX, p.sijY);
                        break;
                    case KeyEvent.VK_Q:
                        pudota(p.sijX, p.sijY, esineValInt);
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
                        käytäEsinettä(esineValInt);
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

                    case KeyEvent.VK_F:
                        erikoisKäyttö(esineValInt);
                        break;

                    case KeyEvent.VK_F2:
                        PääIkkuna.vaatiiPäivityksen = true;
                        PääIkkuna.hudTeksti.setText("Ruudunpäivitys pakotettiin");
                        break;

                    case KeyEvent.VK_F3:
                        PääIkkuna.peliKenttä.revalidate();
                        PääIkkuna.peliKenttä.repaint();
                        PääIkkuna.hudTeksti.setText("Kentänpäivitys pakotettiin");
                        break;

                    default:
                        System.out.println("Other Key Pressed");
                        break;
                }
                if (pelaajaSiirtyi) {
                    suoritaKohtaaminen();
                    PääIkkuna.yläteksti1.setText("Pelaaja siirrettiin sijaintiin (" + p.sijX + ", " + p.sijY + ")");
                    Main.pelaajanSijX = p.sijX;
                    Main.pelaajanSijY = p.sijY;
                    PääIkkuna.vaatiiPäivityksen = true;
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
                
                if (pelikenttä[p.sijX][p.sijY] == null) {
                    PääIkkuna.yläteksti2.setText("Kohteessa ei ole mitään");
                }
                else {
                    PääIkkuna.yläteksti2.setText("Kohteessa on " + pelikenttä[p.sijX][p.sijY].annaNimi());
                }
            }
    
        @Override
            public void keyReleased(KeyEvent e) {
                
            }
    }

    void suljePeli(int sulkuTapa) {
        peliKäynnissä = false;
        switch (sulkuTapa) {
            case 0:
                CustomViestiIkkunat.Loppuonnittelu.showDialog(aika);
                PääIkkuna.ikkuna.dispose();
                break;
            case 1:
                int valinta = CustomViestiIkkunat.LoppuHäviö.showDialog(Main.häviönSyy);
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
        sThread.stop();
    }

    void luoAloitusKenttä() {
        
        // Luodaan pelikentälle satunnaiseen sijaintiin esineitä ja kiintopisteitä.

        sijoitaSatunnaiseenRuutuun(new Kaasupullo());
        sijoitaSatunnaiseenRuutuun(new TyhjäKaasusytytin());
        sijoitaSatunnaiseenRuutuun(new Vesiämpäri());
        sijoitaSatunnaiseenRuutuun(new Kilpi());
        sijoitaSatunnaiseenRuutuun(new Avain());
        sijoitaSatunnaiseenRuutuun(new Paperi());
        sijoitaSatunnaiseenRuutuun(new Hiili());

        sijoitaSatunnaiseenRuutuun(new Kirstu());
        sijoitaSatunnaiseenRuutuun(new Nuotio());
        sijoitaSatunnaiseenRuutuun(new Ämpärikone());

        for (int i = 0; i < Main.suklaidenMäärä; i++) {
            sijoitaSatunnaiseenRuutuun(new Suklaalevy());
        }

        for (int i = 0; i < Main.makkaroidenMäärä; i++) {
            sijoitaSatunnaiseenRuutuun(new Makkara());
        }

        for (int i = 0; i < Main.vihujenMäärä; i++) {
            sijoitaSatunnaiseenRuutuun(new PikkuVihu());
        }

        if (esineitäKentällä >= Main.kentänKoko * Main.kentänKoko) {
            JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnatut esineet hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
        }

        /*  
        Annetaan pelaajalle aloitustavarat.

        p.esineet[0] = new Kaasupullo();
        p.esineet[1] = new TyhjäKaasusytytin();
        p.esineet[2] = new Vesiämpäri();
        p.esineet[3] = new Suklaalevy();
        */
    }

    /**
         * Pelin päämetodi, jota ajetaan uudelleen niin kauan, kun peli on käynnissä.
         * Suoritusten jälkeen peli palaa aina takaisin "alkuvalikkoon".
         */

        void pelinKulku() {

            if (peliHävitty) {
                suljePeli(1);
            }

            if (PääIkkuna.uusiIkkuna) {
                suljePeli(2);
                PääIkkuna.uusiIkkuna = false;
                Main.uusiPeli();
            }

            peliLäpäisty = tarkistaPelinTila();
            if (peliLäpäisty || PääIkkuna.uusiIkkuna) {
                suljePeli(0);
                peliKäynnissä = false;
            }
            else {
                //try{
                    //Thread.sleep(16);
                    //PääIkkuna.päivitäIkkuna(p.sijX, p.sijY, p.kuvake);
                    for (int i = 0; i < 5; i++) {
                        if (p.esineet[i] == null) {
                            PääIkkuna.esineLabel[i].setText(null);
                            PääIkkuna.esineLabel[i].setIcon(null);
                        }
                        else {
                            PääIkkuna.esineLabel[i].setIcon(p.esineet[i].annaKuvake());
                        }
                    }
                //}
                //catch (InterruptedException e) {

                //}
            }
        }
    
        Timer ajastin = new Timer(10, e -> {
            if (peliKäynnissä) {
                aika += 0.01;
                double sekunnit = aika % 60;
                int minuutit = (int)aika / 60;
                PääIkkuna.aikaTeksti.setText("Aika: " + minuutit + ":" + kaksiDesimaalia.format(sekunnit));
                pelinKulku();
            }
        });

    public Peli() {
        
        //try {
        //    Thread.sleep(500);
        //}
        //catch (InterruptedException e) {

        //}

        PääIkkuna.luoPääikkuna();
        PääIkkuna.ikkuna.addKeyListener(new Näppäinkomennot());

        luoAloitusKenttä();
        PääIkkuna.luoAlkuIkkuna(0, 0, new ImageIcon("tiedostot/kuvat/pelaaja.png"));

        ajastin.start();
        gThread.start();
        sThread.start();
        
        

        //while (peliKäynnissä) {
        //    pelinKulku();
        //}
    }

}
