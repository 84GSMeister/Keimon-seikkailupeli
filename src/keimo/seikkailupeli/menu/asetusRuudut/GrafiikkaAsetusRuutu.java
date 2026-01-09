package keimo.seikkailupeli.menu.asetusRuudut;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.keimoengine.ikkuna.GLFW_Ikkuna;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.kenttä.Maailma;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GrafiikkaAsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 7;
    private static int scrollaus = 0;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static Teksti infoTeksti = new Teksti("info", Color.white, 2000, 300);
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti infoTekstiLabel = new MenuKomponentti(1, 0.25f, 0, -0.75f, infoTeksti);

    private static Teksti asetusKokonäyttöTeksti = new Teksti("Kokonäyttö (F11)", Color.white, 600, 48);
    private static Teksti asetusResoluutioTeksti = new Teksti("Resoluutio", Color.white, 600, 48);
    private static Teksti asetusNäyttöTeksti = new Teksti("Näyttö", Color.white, 600, 48);
    private static Teksti asetusZoomTeksti = new Teksti("Näköetäisyys", Color.white, 600, 48);
    private static Teksti asetusKirkkausTeksti = new Teksti("Kirkkaus", Color.white, 600, 48);
    private static Teksti asetusVsyncTeksti = new Teksti("Pystysynkronointi", Color.white, 600, 48);

    private static Teksti tilaKokonäyttöTeksti = new Teksti("Ei", Color.white, 600, 48);
    private static Teksti tilaResoluutioTeksti = new Teksti("Natiivi", Color.white, 800, 48);
    private static Teksti tilaNäyttöTeksti = new Teksti("0", Color.white, 800, 48);
    private static Teksti tilaZoomTeksti = new Teksti("1x", Color.white, 600, 48);
    private static Teksti tilaKirkkausTeksti = new Teksti("100%", Color.white, 600, 48);
    private static Teksti tilaVsyncTeksti = new Teksti("Ei", Color.white, 600, 48);

    private static ArrayList<String> resoluutiot = KeimoEngine.window.annaResoluutiot();
    private static boolean kokonäyttö = false, vsync = false;
    private static int resoluutioValInt = resoluutiot.size()-1;
    private static int näyttöjenMäärä = ((GLFW_Ikkuna)KeimoEngine.window).annaNäytöt().size();
    private static int valittuNäyttö = 0;
    private static String valittuResoluutio = "1920x1080";
    private static int resoluutioX, resoluutioY;
    private static float zoom = 1f;
    private static float kirkkaus = 1f;
    private static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

    private static String infoTekstiKokonäyttö = "Valitse kokonäyttö- tai ikkunoitu tila\n";
    private static String infoTekstiResoluutio = "Valitse resoluutio\n" + 
    "Vaikuttaa vain kokonäyttötilassa.\n" +
    "Ikkunoidussa tilassa resoluutio on vapaasti säädettävissä ikkunan koon perusteella.";
    private static String infoTekstiNäyttö = "Valitse näyttö\n" + 
    "Jos tietokoneeseen on kytketty useita näyttöjä.\n" +
    "Vaikuttaa vain kokonäyttötilassa.\n" +
    "Oletuksena käytetään järjestelmän päänäyttöä.";
    private static String infoTekstiNäköetäisyys = "Näköetäisyys\n" +
    "Vaikuttaa zoomaustasoon sekä piirtoetäisyyteen.\n" +
    "Suurempi arvo zoomaa ulos ja kasvattaa piirrettävien laattojen määrää.\n" +
    "Pienempi arvo parantaa suorituskykyä.\n" +
    "Oletus: 1";
    private static String infoTekstiKirkkaus = "Kirkkaus\n" +
    "Tummentaa kaikkia varjostimella piirrettäviä objekteja.\n" +
    "0: Täysin pimeä\n" +
    "1: Täysin kirkas\n" +
    "Oletus: 1";
    private static String infoTekstiVsync = "Pystysynkronointi (V-sync)\n" +
    "Lukitsee pelin päivitysnopeuden näytön virkistystaajuuteen.\n" +
    "Voi aiheuttaa lisäviivettä syötteeseen.\n" +
    "Oletus: Ei";

    public static void painaNäppäintä(String näppäin) {
        switch (näppäin) {
            case "ylös" -> {
                valinta--;
                if (valinta < 0) {
                    valinta = asetustenMäärä-1;
                }
            }
            case "alas" -> {
                valinta++;
                if (valinta > asetustenMäärä-1) {
                    valinta = 0;
                }
            }
            case "vasen" -> {
                säädäAsetusta(valinta, false);
                päivitäAsetukset();
            }
            case "oikea" -> {
                säädäAsetusta(valinta, true);
                päivitäAsetukset();
            }
            case "enter" -> {
                hyväksy(valinta);
            }
            case "esc" -> {
                peruuta();
            }
        }
        Äänet.toistaSFX("Valinta");
    }

    static void säädäAsetusta(int valinta, boolean kasvata) {
        switch (valinta) {
            case 0: // Kokonäyttö
                kokonäyttö = KeimoEngine.window.isFullscreen();
                kokonäyttö = !kokonäyttö;
            break;
            case 1: // Resoluutio
                if (kasvata) {
                    if (resoluutioValInt < resoluutiot.size()-1) resoluutioValInt++;
                }
                else {
                    if (resoluutioValInt > 0) resoluutioValInt--;
                }
                valittuResoluutio = resoluutiot.get(resoluutioValInt);
            break;
            case 2: // Näyttö
                if (kasvata) {
                    if (valittuNäyttö < näyttöjenMäärä-1) valittuNäyttö++;
                }
                else {
                    if (valittuNäyttö > 0) valittuNäyttö--;
                }
            break;
            case 3: // Zoom
                if (kasvata) {
                    if (zoom < 4.95f) zoom += 0.1f;
                }
                else {
                    if (zoom > 0.55f) zoom -= 0.1f;
                }
            break;
            case 4: // Kirkkaus
                if (kasvata) {
                    if (kirkkaus < 0.975f) kirkkaus += 0.05f;
                    if (kirkkaus > 1f) kirkkaus = 1f;
                }
                else {
                    if (kirkkaus > 0.025f) kirkkaus -= 0.05f;
                    if (kirkkaus < 0) kirkkaus = 0;
                }
            break;
            case 5: // Pystysynkronointi
                vsync = KeimoEngine.window.isVsync();
                vsync = !vsync;
            break;
            case 6: // Hyväksy
                
            break;
            default:
            break;
        }
    }

    static void päivitäAsetukset() {
        try {
            String[] resoluutioSplit = valittuResoluutio.split("x");
            resoluutioX = Integer.parseInt(resoluutioSplit[0]);
            resoluutioY = Integer.parseInt(resoluutioSplit[1]);
            if (kokonäyttö) {
                KeimoEngine.window.setSize(resoluutioX, resoluutioY);
                KeimoEngine.window.setFullscreen(kokonäyttö, true);
            }
            else if (valinta == 0) KeimoEngine.window.setFullscreen(kokonäyttö, true);
            if (valinta == 2) {
                KeimoEngine.window.setMonitor(valittuNäyttö);
                if (kokonäyttö) {
                    KeimoEngine.window.setSize(resoluutioX, resoluutioY);
                    KeimoEngine.window.setFullscreen(kokonäyttö, true);
                }
            }
            KeimoEngine.window.setVSync(vsync);
            Kamera.zoomKerroin = zoom;
            PelinAsetukset.zoom = zoom;
            Kamera.päivitäZoom = true;
            Maailma.fade = 1f - kirkkaus;
        }
        catch (Exception e) {
            System.out.println("Asetusten tallentaminen epäonnistui.");
            e.printStackTrace();
        }
    }

    static void hyväksy(int valinta) {
        if (valinta == 6) {
            KeimoEngine.valitseAktiivinenRuutu("asetusruutu");
        }
    }

    static void peruuta() {
        KeimoEngine.valitseAktiivinenRuutu("asetusruutu");
    }

    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        
        tilaKokonäyttöTeksti.päivitäTeksti(KeimoEngine.window.isFullscreen() ? "Kyllä" : "Ei");
        tilaResoluutioTeksti.päivitäTeksti(valittuResoluutio);
        tilaNäyttöTeksti.päivitäTeksti("" + valittuNäyttö);
        tilaZoomTeksti.päivitäTeksti("" + kaksiDesimaalia.format(zoom));
        tilaKirkkausTeksti.päivitäTeksti("" + kaksiDesimaalia.format(kirkkaus));
        tilaVsyncTeksti.päivitäTeksti(KeimoEngine.window.isVsync() ? "Kyllä" : "Ei");

        if (valinta > 4) scrollaus = valinta - 4;
        else scrollaus = 0;

        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i-scrollaus) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponentti(shader, annaOsoitinKuvake(i), window, 1f/12f, 1f/12f, 1, -1f/12f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i-scrollaus) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponentti(shader, annaAsetusTekstuuri(i), window, 1f/2.5f, 1f/15f, 1, 1f/2.5f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i-scrollaus) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponentti(shader, annaTilaTeksti(i), window, 1f/2.5f, 1f/15f, 1, 1f/2.5f +1f/4f, offsetY, 0);
        }

        otsikkoLabel.renderöi(shader, window);

        annaInfoTeksti(valinta);
        infoTekstiLabel.renderöi(shader, window);
    }

    private static Renderöitävä annaAsetusTekstuuri(int indeksi) {
        switch (indeksi) {
            case 0: return asetusKokonäyttöTeksti;
            case 1: return asetusResoluutioTeksti;
            case 2: return asetusNäyttöTeksti;
            case 3: return asetusZoomTeksti;
            case 4: return asetusKirkkausTeksti;
            case 5: return asetusVsyncTeksti;
            case 6: return hyväksyTekstuuri;
            default: return hyväksyTekstuuri;
        }
    }

    private static Renderöitävä annaOsoitinKuvake(int valikkoElementti) {
        if (valikkoElementti == valinta) return osoitinKuvake;
        else return tyhjäTekstuuri;
    }

    private static Renderöitävä annaTilaTeksti(int indeksi) {
        switch (indeksi) {
            case 0: return tilaKokonäyttöTeksti;
            case 1: return tilaResoluutioTeksti;
            case 2: return tilaNäyttöTeksti;
            case 3: return tilaZoomTeksti;
            case 4: return tilaKirkkausTeksti;
            case 5: return tilaVsyncTeksti;
            case 6: return tyhjäTekstuuri;
            default: return tyhjäTekstuuri;
        }
    }

    private static void annaInfoTeksti(int indeksi) {
        switch (indeksi) {
            case 0: infoTeksti.päivitäTeksti(infoTekstiKokonäyttö, 0, 58); break;
            case 1: infoTeksti.päivitäTeksti(infoTekstiResoluutio, 2); break;
            case 2: infoTeksti.päivitäTeksti(infoTekstiNäyttö, 2); break;
            case 3: infoTeksti.päivitäTeksti(infoTekstiNäköetäisyys, 2); break;
            case 4: infoTeksti.päivitäTeksti(infoTekstiKirkkaus); break;
            case 5: infoTeksti.päivitäTeksti(infoTekstiVsync, 2); break;
            default: infoTeksti.päivitäTeksti(""); break;
        }
    }
}
