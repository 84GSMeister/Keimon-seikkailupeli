package keimo.seikkailupeli.menu.asetusRuudut;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.keimoengine.ikkuna.Window;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.kenttä.Maailma;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GrafiikkaAsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 6;
    private static Tekstuuri otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_asetukset.png");
    private static Animaatio osoitinKuvake = new Animaatio("tiedostot/kuvat/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");
    private static Teksti infoTeksti = new Teksti("info", Color.white, 2000, 300);
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti infoTekstiLabel = new MenuKomponentti(1, 0.25f, 0, -0.75f, infoTeksti);

    private static Teksti asetusKokonäyttöTeksti = new Teksti("Kokonäyttö (F11)", Color.white, 600, 48);
    private static Teksti asetusResoluutioTeksti = new Teksti("Resoluutio", Color.white, 600, 48);
    private static Teksti asetusZoomTeksti = new Teksti("Näköetäisyys", Color.white, 600, 48);
    private static Teksti asetusKirkkausTeksti = new Teksti("Kirkkaus", Color.white, 600, 48);
    private static Teksti asetusVsyncTeksti = new Teksti("Pystysynkronointi", Color.white, 600, 48);
    private static Tekstuuri hyväksyTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/asetukset_takaisin.png");

    private static Teksti tilaKokonäyttöTeksti = new Teksti("Ei", Color.white, 600, 48);
    private static Teksti tilaResoluutioTeksti = new Teksti("Natiivi", Color.white, 800, 48);
    private static Teksti tilaZoomTeksti = new Teksti("1x", Color.white, 600, 48);
    private static Teksti tilaKirkkausTeksti = new Teksti("100%", Color.white, 600, 48);
    private static Teksti tilaVsyncTeksti = new Teksti("Ei", Color.white, 600, 48);

    private static ArrayList<String> resoluutiot = Window.annaResoluutiot();
    private static boolean kokonäyttö = false, vsync = false;
    private static int resoluutioValInt = resoluutiot.size()-1;
    private static String valittuResoluutio = "1920x1080";
    private static int resoluutioX, resoluutioY;
    private static float zoom = 1f;
    private static float kirkkaus = 1f;
    private static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

    private static String infoTekstiKokonäyttö = "Valitse kokonäyttö- tai ikkunoitu tila\n";
    private static String infoTekstiResoluutio = "Valitse resoluutio\n" + 
    "Vaikuttaa vain kokonäyttötilassa\n" +
    "Ikkunoidussa tilassa resoluutio on vapaasti\n " +
    "säädettävissä ikkunan koon perusteella.";
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
            case 2: // Zoom
                if (kasvata) {
                    if (zoom < 4.95f) zoom += 0.1f;
                }
                else {
                    if (zoom > 0.55f) zoom -= 0.1f;
                }
            break;
            case 3: // Kirkkaus
                if (kasvata) {
                    if (kirkkaus < 0.975f) kirkkaus += 0.05f;
                    if (kirkkaus > 1f) kirkkaus = 1f;
                }
                else {
                    if (kirkkaus > 0.025f) kirkkaus -= 0.05f;
                    if (kirkkaus < 0) kirkkaus = 0;
                }
            break;
            case 4: // Pystysynkronointi
                vsync = KeimoEngine.window.isVsync();
                vsync = !vsync;
            break;
            case 5: // Hyväksy
                
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
        if (valinta == 5) {
            KeimoEngine.valitseAktiivinenRuutu("asetusruutu");
        }
    }

    public static void render(Shader shader, Window window) {
        shader.bind();
        
        tilaKokonäyttöTeksti.päivitäTeksti(KeimoEngine.window.isFullscreen() ? "Kyllä" : "Ei");
        tilaResoluutioTeksti.päivitäTeksti(valittuResoluutio);
        tilaZoomTeksti.päivitäTeksti("" + kaksiDesimaalia.format(zoom));
        tilaKirkkausTeksti.päivitäTeksti("" + kaksiDesimaalia.format(kirkkaus));
        tilaVsyncTeksti.päivitäTeksti(KeimoEngine.window.isVsync() ? "Kyllä" : "Ei");

        otsikkoLabel.renderöi(shader, window);
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponentti(shader, annaOsoitinKuvake(i), window, 1f/12f, 1f/12f, 1, -1f/12f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponentti(shader, annaAsetusTekstuuri(i), window, 1f/2.5f, 1f/15f, 1, 1f/2.5f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponentti(shader, annaTilaTeksti(i), window, 1f/2.5f, 1f/15f, 1, 1f/2.5f +1f/4f, offsetY, 0);
        }
        annaInfoTeksti(valinta);
        infoTekstiLabel.renderöi(shader, window);
    }

    private static Renderöitävä annaAsetusTekstuuri(int indeksi) {
        switch (indeksi) {
            case 0: return asetusKokonäyttöTeksti;
            case 1: return asetusResoluutioTeksti;
            case 2: return asetusZoomTeksti;
            case 3: return asetusKirkkausTeksti;
            case 4: return asetusVsyncTeksti;
            case 5: return hyväksyTekstuuri;
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
            case 2: return tilaZoomTeksti;
            case 3: return tilaKirkkausTeksti;
            case 4: return tilaVsyncTeksti;
            case 5: return tyhjäTekstuuri;
            default: return tyhjäTekstuuri;
        }
    }

    private static void annaInfoTeksti(int indeksi) {
        switch (indeksi) {
            case 0: infoTeksti.päivitäTeksti(infoTekstiKokonäyttö, 0, 58); break;
            case 1: infoTeksti.päivitäTeksti(infoTekstiResoluutio); break;
            case 2: infoTeksti.päivitäTeksti(infoTekstiNäköetäisyys); break;
            case 3: infoTeksti.päivitäTeksti(infoTekstiKirkkaus); break;
            case 4: infoTeksti.päivitäTeksti(infoTekstiVsync); break;
            default: infoTeksti.päivitäTeksti(""); break;
        }
    }
}
