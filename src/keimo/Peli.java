package keimo;

import keimo.Kenttäkohteet.*;
import keimo.Kenttäkohteet.Käännettävä.Suunta;
import keimo.Maastot.*;
import keimo.NPCt.*;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.Pelaaja.*;
import keimo.PelinAsetukset.AjoitusMuoto;
import keimo.Ruudut.*;
import keimo.Ruudut.Lisäruudut.PullonPalautusIkkuna;
import keimo.Ruudut.Lisäruudut.ValintaDialogiIkkuna;
import keimo.Säikeet.*;
import keimo.Ikkunat.*;
import keimo.HuoneEditori.*;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Peli {
    
    /**
     * Puutteet sovelluksessa:
     *  -joitain luokkia ei kommentoitu
     *  -esineiden käytä() -metodeissa bugeja ja puuttuvia toteutuksia
     */
    
    public static boolean pause = true;
    public static boolean ajastinPysäytetty = false;
    public static boolean valintaDialogi = false;
    public static boolean peliAloitettu = false;
    public static int kentänKoko = 10;
    public static int kentänAlaraja = 0;
    public static int kentänYläraja = kentänAlaraja + kentänKoko - 1;
    public static KenttäKohde[][] pelikenttä = new KenttäKohde[kentänKoko][kentänKoko];
    public static Maasto[][] maastokenttä= new Maasto[kentänKoko][kentänKoko];

    public static boolean huoneVaihdettava = true;
    public static int uusiHuone = 0;
    public static HashMap<Integer, Huone> huoneKartta = new HashMap<Integer, Huone>();
    public static Huone huone;
    static String häviönSyy = "";
    static int aloitusHp = 10;

    int esineitäKentällä = 0;
    public static Pelaaja p = new Pelaaja();
    Scanner sc = new Scanner(System.in);
    public static boolean peliLäpäisty = false;
    public static boolean peliKäynnissä = true;
    static boolean peliHävitty = false;
    int valinta;
    int valinta2;
    public static Esine valittuEsine;
    int toiminto;
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

    static GrafiikanPäivitysSäie grafiikkaThread = new GrafiikanPäivitysSäie();
    static AikaSäie aikaThread = new AikaSäie();
    public static ÄänentoistamisSäie ääniThread = new ÄänentoistamisSäie();
    static GrafiikkaAikaSäieNopea gaThread = new GrafiikkaAikaSäieNopea();
    static TekstiAjastinSäie tekstiaikaThread = new TekstiAjastinSäie();

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

    static void poimi(int x, int y){
        if (pelikenttä[x][y] instanceof Esine){
            if (Pelaaja.annaEsineidenMäärä() < Pelaaja.annaTavaraluettelonKoko()) {
                for (int i = 0; i < Pelaaja.esineet.length; i++) {
                    if (Pelaaja.esineet[i] == null) {
                        Pelaaja.esineet[i] = (Esine)pelikenttä[x][y];
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
        else if (pelikenttä[x][y] instanceof Ämpärikone || pelikenttä[x][y] instanceof KauppaRuutu || pelikenttä[x][y] instanceof Pulloautomaatti || pelikenttä[x][y] instanceof Silta || pelikenttä[x][y] instanceof Hämärähemmo) {
            pelikenttä[x][y].näytäDialogi(valittuEsine);
        }
        else if (pelikenttä[x][y] instanceof Kiintopiste || pelikenttä[x][y] instanceof NPC_KenttäKohde) {
            vuorovaikutus(esineValInt);
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
            PääIkkuna.avaaDialogi(null, "Tavaraluettelo järjestettiin.", "Järjestäminen");
            PääIkkuna.hudTeksti.setText("Tavaraluettelo järjestettiin");
        }
    }

    static void käyttö(int esine) {
        voiWarpata = false;
        valittuEsine = Pelaaja.esineet[esine];
        if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Warp) {
            Warp warp = (Warp)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
            voiWarpata = true;
            tarkistaWarpinTurvallisuus(warp.annaKohdeHuone(), warp.annaKohdeRuutuX(), warp.annaKohdeRuutuY(), true);
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
                else if (valittuEsine instanceof Kuparilager || valittuEsine instanceof Seteli) {
                    PääIkkuna.hudTeksti.setText(valittuEsine.käytä());
                }
                if (valittuEsine.poistoon()){
                    Pelaaja.esineet[esine] = null;
                    valittuEsine = null;
                }
            }
            else if (valittuEsine.onkoKenttäkäyttöön()) {
                if (Pelaaja.vihollisenKohdalla && Pelaaja.vihollinenKohdalla != null) {
                    Vihollinen vihollinen = Pelaaja.vihollinenKohdalla;
                    if (!vihollinen.onkoKukistettu()) {
                        if (vihollinen.tehoavatAseet.contains(valittuEsine.annaNimi())) {
                            PääIkkuna.hudTeksti.setText(valittuEsine.käytä());
                            vihollinen.kukista(valittuEsine.annaNimi());
                            TarkistettavatArvot.lisääTappoLaskuriin(valittuEsine.annaNimi());
                            ÄänentoistamisSäie.toistaSFX(valittuEsine.annaNimi());
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
                else {
                    PääIkkuna.hudTeksti.setText(valittuEsine.katso());
                }
            }
            else {
                PääIkkuna.hudTeksti.setText(valittuEsine.katso());
            }
        }
        if (valittuEsine != null) {
            if (valittuEsine.poistoon()) {
                Pelaaja.esineet[esine] = null;
            }
        }
    }

    static void vuorovaikutus(int esine) {
        valittuEsine = Pelaaja.esineet[esine];
        if (Pelaaja.esineet[esine] == null) {
            if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] == null) {
                PääIkkuna.hudTeksti.setText("Ei valittua esinettä.");
            }
            else {
                PääIkkuna.hudTeksti.setText(pelikenttä[Pelaaja.sijX][Pelaaja.sijY].katso());
                if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Kiintopiste || pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof NPC_KenttäKohde) {
                    KenttäKohde kk = (KenttäKohde)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
                    PääIkkuna.avaaDialogi(kk.annaDialogiKuvake(), kk.kokeileEsinettä(null), kk.annaNimi());
                    valittuEsine = Pelaaja.esineet[esineValInt];
                    valittuEsine = kk.suoritaMuutoksetEsineelle(valittuEsine);
                    Pelaaja.esineet[esineValInt] = valittuEsine;
                    TavoiteLista.tarkistaTavoiteKiintopiste(kk);
                }
            }
        }
        else if (valittuEsine.onkoKenttäkäyttöön()) {
            if (Pelaaja.vihollisenKohdalla && Pelaaja.vihollinenKohdalla != null) {
                Vihollinen vihollinen = Pelaaja.vihollinenKohdalla;
                if (!vihollinen.onkoKukistettu()) {
                    if (vihollinen.tehoavatAseet.contains(valittuEsine.annaNimi())) {
                        PääIkkuna.hudTeksti.setText(valittuEsine.käytä());
                        vihollinen.kukista(valittuEsine.annaNimi());
                        TarkistettavatArvot.lisääTappoLaskuriin(valittuEsine.annaNimi());
                        ÄänentoistamisSäie.toistaSFX(valittuEsine.annaNimi());
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
            else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Kiintopiste || pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof NPC_KenttäKohde) {
                KenttäKohde kk = (KenttäKohde)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
                PääIkkuna.avaaDialogi(kk.annaDialogiKuvake(), kk.kokeileEsinettä(valittuEsine), kk.annaNimi());
                valittuEsine = Pelaaja.esineet[esineValInt];
                valittuEsine = kk.suoritaMuutoksetEsineelle(valittuEsine);
                Pelaaja.esineet[esineValInt] = valittuEsine;
                TavoiteLista.tarkistaTavoiteKiintopiste(kk);
            }
            else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] == null) {
                PääIkkuna.hudTeksti.setText("Kohteessa ei ole mitään mihin käyttää esinettä");
            }
            else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Esine) {
                PääIkkuna.hudTeksti.setText("Ei voi käyttää kentällä lojuviin esineisiin. Kokeile etsiä muita kiintopisteitä.");
            }
            else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Warp) {
                PääIkkuna.hudTeksti.setText("Ei voi käyttää oviruutuihin.");
            }
            else {
                PääIkkuna.hudTeksti.setText("Käyttö ei onnistunut. Häire sovelluksessa? Ilmoitathan kehittäjille!");
            }
        }
        else {
            PääIkkuna.hudTeksti.setText(valittuEsine.annaNimiSijamuodossa("adessiivi") + " ei ole erikoiskäyttöä.");
            if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Kiintopiste || pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof NPC_KenttäKohde) {
                KenttäKohde kk = (KenttäKohde)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
                PääIkkuna.avaaDialogi(kk.annaDialogiKuvake(), kk.kokeileEsinettä(null), kk.annaNimi());
                valittuEsine = kk.suoritaMuutoksetEsineelle(valittuEsine);
                Pelaaja.esineet[esineValInt] = valittuEsine;
                TavoiteLista.tarkistaTavoiteKiintopiste(kk);
            }
        }
        if (valittuEsine != null) {
            if (valittuEsine.poistoon()) {
                Pelaaja.esineet[esine] = null;
            }
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

    static void suoritaKohtaaminen() {
        if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof NPC_KenttäKohde) {
            NPCKohtaaminen();
        }
        else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Oviruutu) {
            voiWarpata = true;
        }
    }

    static void NPCKohtaaminen() {

    }

    /** Tarkistetaan, onko pelin tavoitteet suoritettu.
     * Jos on, peli päättyy ja ikkuna sulkeutuu.
     */
    static boolean tarkistaPelinTila(){
        
        try {
            int suoritetutPääTavoitteet = TavoiteLista.tarkistaSuoritetutPääTavoitteet();
            
            if (suoritetutPääTavoitteet == TavoiteLista.pääTavoitteet.size()) {
                siirryLoppuRuutuun(0);
            }
            
            if (Pelaaja.syödytRuoat >= 4) {
                häviönSyy = "Söit liikaa ja sinulle tuli paha olo.";
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

                if (!pause && !valintaDialogi) {
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
                        
                        case KeyEvent.VK_P:
                            pausetaPeli(true);
                            PääIkkuna.hudTeksti.setText("Pause");
                            break;

                        case KeyEvent.VK_F:
                            vuorovaikutus(esineValInt);
                            break;

                        case KeyEvent.VK_F2:
                            PääIkkuna.vaatiiPäivityksen = true;
                            PääIkkuna.uudelleenpiirräKaikki = true;
                            PääIkkuna.hudTeksti.setText("Ruudunpäivitys pakotettiin");
                            break;

                        case KeyEvent.VK_F3:
                            PääIkkuna.uudelleenpiirräKenttä = true;
                            PääIkkuna.hudTeksti.setText("Kentänpäivitys pakotettiin");
                            break;

                        case KeyEvent.VK_F4:
                            PääIkkuna.uudelleenpiirräObjektit = true;
                            PääIkkuna.hudTeksti.setText("Objektien päivitys pakotettiin");
                            break;
                        case KeyEvent.VK_F8:
                            System.out.println("Testinäppäin");    
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
                                PääIkkuna.näytöt.setFullScreenWindow(PääIkkuna.ikkuna);
                                PääIkkuna.fullscreen = true;
                            }
                            else {
                                PääIkkuna.yläPalkki.setVisible(true);
                                PääIkkuna.näytöt.setFullScreenWindow(null);
                                PääIkkuna.fullscreen = false;
                            }
                        break;

                        default:
                            System.out.println("Näppäimellä "+ e.getKeyCode() + " ei ole toimintoa.");
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
                else if (pause && !valintaDialogi) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_P:
                            if (PeliRuutu.pausePaneli.isVisible()) {
                                pausetaPeli(false);
                            }
                            PääIkkuna.hudTeksti.setText("Peli jatkuu.");
                        break;
                        case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                            if (PeliRuutu.vuoropuhePaneli.isVisible() && !PääIkkuna.äläSuljeNuolilla) {
                                PääIkkuna.suljeDialogi();
                                p.aloitaLiike(Suunta.VASEN);
                            }
                        break;
                        case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                            if (PeliRuutu.vuoropuhePaneli.isVisible() && !PääIkkuna.äläSuljeNuolilla) {
                                PääIkkuna.suljeDialogi();
                                p.aloitaLiike(Suunta.OIKEA);
                            }
                        break;
                        case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                            if (PeliRuutu.vuoropuhePaneli.isVisible() && !PääIkkuna.äläSuljeNuolilla) {
                                PääIkkuna.suljeDialogi();
                                p.aloitaLiike(Suunta.ALAS);
                            }
                        break;
                        case KeyEvent.VK_UP, KeyEvent.VK_W:
                            if (PeliRuutu.vuoropuhePaneli.isVisible() && !PääIkkuna.äläSuljeNuolilla) {
                                PääIkkuna.suljeDialogi();
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
                                System.out.println(PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.PAKKAUS);
                                if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.PAKKAUS && e.getKeyCode() == KeyEvent.VK_SPACE) {
                                    PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                    System.out.println(PullonPalautusIkkuna.jatkoSyöteAnnettu);
                                }
                                else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.MUOTO && e.getKeyCode() == KeyEvent.VK_X) {
                                    PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                }
                                else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.KÄSI && e.getKeyCode() == KeyEvent.VK_C) {
                                    PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                }
                                else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.MERKKI && e.getKeyCode() == KeyEvent.VK_Z) {
                                    PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                }
                            }
                        break;
                        case VALINTADIALOGI:
                            switch (e.getKeyCode()) {
                                case KeyEvent.VK_SPACE, KeyEvent.VK_ENTER: ValintaDialogiIkkuna.hyväksyValinta();
                                case KeyEvent.VK_W, KeyEvent.VK_UP: ValintaDialogiIkkuna.pienennäValintaa(); break;
                                case KeyEvent.VK_S, KeyEvent.VK_DOWN: ValintaDialogiIkkuna.kasvataValintaa(); break;
                            }
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

    static void pausetaPeli(boolean pauseta) {
        if (pauseta) {
            TekstinPäivitys.pauseAlkuAika = System.nanoTime();
        }
        else {
            TekstinPäivitys.pauseLoppuAika = System.nanoTime();
            aikaReferenssi += (TekstinPäivitys.pauseLoppuAika - TekstinPäivitys.pauseAlkuAika);
        }
        pause = pauseta;
        ajastinPysäytetty = pauseta;
        PeliRuutu.pausePaneli.setVisible(pauseta);
    }

    static void siirryTarinaRuutuun(String tarina) {
        TavoiteLista.suoritaPääTavoite(0);
        Peli.pause = true;
        PääIkkuna.lataaTarinaRuutu("koti");
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
        //PääIkkuna.ikkuna.remove(PääIkkuna.kortit);
        PääIkkuna.ikkuna.removeKeyListener(nk);
        ValikkoRuutu.alkuvalikonKorttiLayout.first(ValikkoRuutu.kortit);
        ÄänentoistamisSäie.suljeMusiikki();
        grafiikkaThread.stop();
        ääniThread.stop();
        aikaThread.stop();
        tekstiaikaThread.stop();
        gaThread.stop();
        TekstinPäivitys.suljeAjastimet();
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
        if (huoneKartta != null) {
            if (huoneKartta.get(huoneenId) != null) {
                if ((huoneKartta.get(huoneenId).annaHuoneenMaastoSisältö()[kohteenX][kohteenY] instanceof EsteTile) && estäEpäturvallisetWarpit) {
                    if (näytäHuomautus) {
                        JOptionPane.showMessageDialog(null, "Warpin kohteessa on este tai kohde on kentän ulkopuolella.\n\nWarppaamisen epäturvallisiin kohteisiin voi sallia editorissa.", "Warppaaminen epäonnistui", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else if (huoneKartta.get(huoneenId).annaHuoneenKenttäSisältö()[kohteenX][kohteenY] != null) {
                    if (huoneKartta.get(huoneenId).annaHuoneenKenttäSisältö()[kohteenX][kohteenY] instanceof VisuaalinenObjekti) {
                        VisuaalinenObjekti vo = (VisuaalinenObjekti)huoneKartta.get(huoneenId).annaHuoneenKenttäSisältö()[kohteenX][kohteenY];
                        if (vo.onkoEste()) {
                            if (näytäHuomautus) {
                                JOptionPane.showMessageDialog(null, "Warpin kohteessa on este tai kohde on kentän ulkopuolella.\n\nWarppaamisen epäturvallisiin kohteisiin voi sallia editorissa.", "Warppaaminen epäonnistui", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        else {
                            if (lataaHuone(huoneenId)) {
                                Pelaaja.teleport(kohteenX, kohteenY);
                            }
                        }
                    }
                    else {
                        if (lataaHuone(huoneenId)) {
                            Pelaaja.teleport(kohteenX, kohteenY);
                        }
                    }
                }
                else {
                    if (lataaHuone(huoneenId)) {
                        Pelaaja.teleport(kohteenX, kohteenY);
                    }
                }
            }
        }
    }

    /**
     * "Siirry valittuun huoneeseen" eli
     * Lataa pelin nykyiseksi kentäksi huonekartasta (HashMapista) valittu Huone-objekti
     * @param huoneenId ladattavan huoneen ID huonekartassa
     * @return onnistuiko huoneen lataaminen (löytyikö valitulla ID:llä huone)
     */

    public static boolean lataaHuone(int huoneenId) {
        try{
            if (!huoneKartta.get(huoneenId).annaTavoiteVaatimus() || TavoiteLista.tavoiteLista.get(huoneKartta.get(huoneenId).annaVaaditunTavoitteenTunniste())) {
                huone = huoneKartta.get(huoneenId);
                pelikenttä = huone.annaHuoneenKenttäSisältö();
                maastokenttä = huone.annaHuoneenMaastoSisältö();
                for (Maasto[] mm : maastokenttä) {
                    for (Maasto m : mm) {
                        if (m != null) {
                            m.päivitäKuvanAsento();
                        }
                    }
                }
                npcLista.clear();
                for (NPC[] nn : huone.annaHuoneenNPCSisältö()) {
                    for (NPC n : nn) {
                        if (n != null) {
                            npcLista.add(n);
                            if (!n.onLadattuPelissä) {
                                n.teleport(n.annaAlkuSijX(), n.annaAlkuSijY());
                                n.onLadattuPelissä = true;
                            }
                        }
                    }
                }
                if (huone.annaTausta() != null) {
                    GrafiikanPäivitysSäie.uusiTausta = huone.annaTausta();
                }
                voiWarpataVasen = false;
                voiWarpataOikea = false;
                voiWarpataAlas = false;
                voiWarpataYlös = false;
                warppiViive = 20;
                PääIkkuna.hudTeksti.setText("Ladattiin huone " + huone.annaNimi() + " (ID: " + huone.annaId() + ")");
                PääIkkuna.uudelleenpiirräObjektit = true;
                if (huoneKartta.get(huoneenId).annaTarinaRuudunLataus()) {
                    pause = true;
                    siirryTarinaRuutuun(huoneKartta.get(huoneenId).annaTarinaRuudunTunniste());
                    huoneKartta.get(huoneenId).tarinaRuudunTunniste = null;
                    //CustomViestiIkkunat.HuoneenVaihtoDialogi.showDialog("" + huoneKartta.get(huoneenId).tarinaRuudunTunniste);
                }
                
                if (PääIkkuna.ikkunanKokoMuutettuEnnenHuoneenLatuasta) {
                    PeliKenttäMetodit.teleporttaaViholliset = true;
                    PääIkkuna.ikkunanKokoMuutettuEnnenHuoneenLatuasta = false;
                }
                return true;
            }
            else {
                PääIkkuna.avaaDialogi(null, "Huoneeseen warppaaminen vaatii tavoitteen: " + huoneKartta.get(huoneenId).annaVaaditunTavoitteenTunniste(), "Huone lukittu");
                return false;
            }
            
        }
        catch (NullPointerException e) {
            pause = true;
            JOptionPane.showMessageDialog(null, "Yritettiin warpata huoneeseen " + huoneenId + ", jota ei ole olemassa.", "Huonetta ei löytynyt.", JOptionPane.ERROR_MESSAGE);
            pause = false;
            return false;
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

    public static void luoHuone(int huoneenId, String huoneenNimi, String huoneenTaustanPolku, String huoneenAlue, ArrayList<KenttäKohde> huoneenKenttäSisältö, ArrayList<Maasto> huoneenMaastoSisältö, ArrayList<NPC> huoneenNPCSisältö, String alkuDialogi, String vaaditunTavoitteenTunniste) {

        Huone huone = new Huone(huoneenId, kentänKoko, huoneenNimi, huoneenTaustanPolku, huoneenAlue, huoneenKenttäSisältö, huoneenMaastoSisältö, huoneenNPCSisältö, alkuDialogi, vaaditunTavoitteenTunniste);
        huoneKartta.put(huoneenId, huone);
    }

    static int warppiViive = 0;
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

    public static void pelaajanLiike() {
        if (Pelaaja.liikutaPelaajaa()) {
            suoritaReunanTarkistus();
            suoritaKohtaaminen();
            päivitäPelaajanSijaintiTiedot();
            PääIkkuna.vaatiiPäivityksen = true;
            PääIkkuna.pelaajaSiirtyi = true;
            if (warppiViive == 0) {
                if (voiWarpataVasen && Näppäinkomennot.vasenPainettu) {
                    tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.VASEN), 9, Pelaaja.sijY, false);
                }
                else if (voiWarpataOikea && Näppäinkomennot.oikeaPainettu) {
                    tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.OIKEA), 0, Pelaaja.sijY, false);
                }
                else if (voiWarpataAlas && Näppäinkomennot.alasPainettu) {
                    tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.ALAS), Pelaaja.sijX, 0, false);
                }
                else if (voiWarpataYlös && Näppäinkomennot.ylösPainettu) {
                    tarkistaWarpinTurvallisuus(huone.annaReunaWarpinKohdeId(KenttäKohde.Suunta.YLÖS), Pelaaja.sijX, 9, false);
                }
                else if (pelikenttä[Pelaaja.sijX][Pelaaja.sijY] instanceof Warp) {
                    Warp warp = (Warp)pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
                    if (
                        warp.annaSuunta() == KenttäKohde.Suunta.VASEN && Näppäinkomennot.vasenPainettu ||
                        warp.annaSuunta() == KenttäKohde.Suunta.OIKEA && Näppäinkomennot.oikeaPainettu ||
                        warp.annaSuunta() == KenttäKohde.Suunta.ALAS && Näppäinkomennot.alasPainettu ||
                        warp.annaSuunta() == KenttäKohde.Suunta.YLÖS && Näppäinkomennot.ylösPainettu
                    ) {
                        // uusiHuone = warp.annaKohdeHuone();
                        // huoneVaihdettava = true;
                        // p.teleport(warp.annaKohdeRuutuX(), warp.annaKohdeRuutuY());
                        tarkistaWarpinTurvallisuus(warp.annaKohdeHuone(), warp.annaKohdeRuutuX(), warp.annaKohdeRuutuY(), true);
                        warp.warpinJälkeen();
                    }
                }
            }
        }
    }

    /**
     * Päivitä pelaajan sijainti ikkunan yläpalkkiin tietoihin, jos sijainti on asetettu näkyviin.
     * Pelaajan sijainti pelikentällä + pelaajan hitboxin rajat.
     */

    static void päivitäPelaajanSijaintiTiedot() {
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

    /**
     * Pelin päämetodi, jota ajetaan uudelleen niin kauan, kun peli on käynnissä.
     * Suoritusten jälkeen peli palaa aina takaisin "alkuvalikkoon".
     */

    public static void pelinKulku() {

        if (huoneVaihdettava) {
            lataaHuone(uusiHuone);
            huoneVaihdettava = false;
        }

        if (PääIkkuna.uusiIkkuna) {
            PääIkkuna.pääPaneeli.removeAll();
            suljePeli();
            PääIkkuna.uusiIkkuna = false;
            uusiHuone = 0;
            huoneVaihdettava = true;
            uusiPeli();
        }

        //peliLäpäisty = tarkistaPelinTila();
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

    public static void uusiPeli() {
        
        try {
            kentänKoko = TarkistettavatArvot.uusiKentänKoko;
            kentänYläraja = kentänAlaraja + kentänKoko - 1;
            pause = true;
            peliKäynnissä = true;
            TarkistettavatArvot.nollaa();
            new Peli();
            //throw new Exception();
        }
        catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString();
            int virheValinta = CustomViestiIkkunat.SuljeVirheenJälkeen.showDialog(sStackTrace);
            if (virheValinta == 1) {
                System.exit(0);
            }
            else {
                uusiPeli();
            }
            System.exit(0);
        }
    }

    public Peli() {
        
        AikaSäie.kulunutAika = 0;
        TekstinPäivitys.kulunutAika = 0;
        aikaReferenssi = System.nanoTime();
        peliAloitettu = false;
        peliLäpäisty = false;
        TavoiteLista.luoPääTavoiteLista();
        TavoiteLista.luoTavoiteLista();

        huoneKartta = HuoneLista.luoVakioHuoneKarttaTiedostosta();
        esineValInt = 0;
        valittuEsine = null;
        p = new Pelaaja();
        uusiHuone = 0;
        huoneVaihdettava = true;

        if (PääIkkuna.ikkuna != null && PääIkkuna.pääPaneeli != null) {
            PääIkkuna.ikkuna.remove(PääIkkuna.pääPaneeli);
        }
        PääIkkuna.luoPääikkuna();
        PääIkkuna.ikkuna.addKeyListener(nk);
        PääIkkuna.vaatiiPäivityksen = true;
        PeliRuutu.luoPeliRuudunGUI();
        PeliRuutu.alustaPeliRuutu();
        PääIkkuna.lataaRuutu("tarinaruutu");
        TarinaRuutu.tarinaPaneli.requestFocus();

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        grafiikkaThread = new GrafiikanPäivitysSäie();
        grafiikkaThread.setName("Grafiikkasäie");
        aikaThread = new AikaSäie();
        aikaThread.setName("Aikasäie");
        ääniThread = new ÄänentoistamisSäie();
        ääniThread.setName("Äänisäie");
        tekstiaikaThread = new TekstiAjastinSäie();
        tekstiaikaThread.setName("Tekstiajastinsäie");
        gaThread = new GrafiikkaAikaSäieNopea();
        gaThread.setName("Grafiikka-aikasäie nopea");

        if (PelinAsetukset.ajoitus == AjoitusMuoto.ERITTÄIN_NOPEA) {
            gaThread.start();
        }
        else {
            grafiikkaThread.start();
            aikaThread.start();
        }
        if (PelinAsetukset.ajoitus == AjoitusMuoto.TARKKA) {
            IsoNumeroSäie.luoIsoNumeroSäie();
        }
        ääniThread.start();
        tekstiaikaThread.start();
        TekstinPäivitys.aloitaAjastimet();
        System.out.print((char)0x4A);
        System.out.print((char)0x6F);
        System.out.print((char)0x6E);
        System.out.print((char)0x74);
        System.out.print((char)0x74);
        System.out.println((char)0x75);
    }
}