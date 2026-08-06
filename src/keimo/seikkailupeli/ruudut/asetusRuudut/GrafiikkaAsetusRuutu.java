package keimo.seikkailupeli.ruudut.asetusRuudut;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenRenderöinti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.keimoengine.ikkuna.GLFW_Ikkuna;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.kenttä.Maailma;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu.AsetusRuudut;
import keimo.seikkailupeli.äänet.Äänet;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GrafiikkaAsetusRuutu {
    private static int valinta = 0;
    private static int scrollaus = 0;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static Teksti infoTeksti = new Teksti("info", Väri.white, 2000, 300);
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -0.85f, 0, osoitinKuvake, 10, 0, 0);
    private static MenuKomponentti infoTekstiLabel = new MenuKomponentti(1, 0.25f, 0, -0.75f, infoTeksti);

    private static Renderöitävä[] asetusTekstit = new Renderöitävä[] {
        new Teksti("Kokonäyttö (F11)", Väri.white, 600, 48),
        new Teksti("Resoluutio", Väri.white, 600, 48),
        new Teksti("Näyttö", Väri.white, 600, 48),
        new Teksti("Näköetäisyys", Väri.white, 600, 48),
        new Teksti("Kirkkaus", Väri.white, 600, 48),
        new Teksti("Pystysynkronointi", Väri.white, 600, 48),
        new Teksti("Vapaa kamera", Väri.white, 600, 48),
        new Teksti("Grafiikkatesti", Väri.white, 600, 48),
        hyväksyTekstuuri,
    };

    private static Teksti[] tilaTekstit = new Teksti[] {
        new Teksti("Ei", Väri.white, 600, 48),
        new Teksti("Natiivi", Väri.white, 800, 48),
        new Teksti("0", Väri.white, 600, 48),
        new Teksti("1x", Väri.white, 600, 48),
        new Teksti("100%", Väri.white, 600, 48),
        new Teksti("Ei", Väri.white, 600, 48),
        new Teksti("Kyllä", Väri.white, 600, 48),
        new Teksti("", Väri.white, 600, 48),
        new Teksti("", Väri.white, 600, 48),
    };

    private static int asetustenMäärä = asetusTekstit.length;
    private static ArrayList<String> resoluutiot = KeimoEngine.window.annaResoluutiot();
    private static boolean kokonäyttö = false, vsync = false;
    private static int resoluutioValInt = resoluutiot.size()-1;
    private static int näyttöjenMäärä = ((GLFW_Ikkuna)KeimoEngine.window).annaNäytöt().size();
    private static int valittuNäyttö = 0;
    private static String valittuResoluutio = "1920x1080";
    private static int resoluutioX, resoluutioY;
    private static float zoom = 1f;
    private static float kirkkaus = 1f;
    private static boolean vapaaKamera = true;
    private static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
    private static boolean päivitäVsync = false;

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
    "Suurempi arvo loitontaa näkymää ja kasvattaa piirrettävien laattojen määrää.\n" +
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
    private static String infoTekstiVapaaKamera = "Vapaa kamera\n" +
    "Sallii kameran liikkeen kentän ulkopuolelle.\n" +
    "Kamera liikkuu aina pelaajan sijainnin mukaan eikä pysähdy huoneen reunaan.\n" +
    "Oletus: Kyllä";

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
            case 6: // Vapaa kamera
                vapaaKamera = !vapaaKamera;
            break;
            case 7: // Hyväksy
                
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
            Kamera.zoomKerroin = zoom;
            PelinAsetukset.zoom = zoom;
            Kamera.päivitäZoom = true;
            Maailma.fade = 1f - kirkkaus;
            PelinAsetukset.vapaaKamera = vapaaKamera;
            päivitäVsync = true;
        }
        catch (Exception e) {
            System.out.println("Asetusten tallentaminen epäonnistui.");
            e.printStackTrace();
        }
    }

    static void hyväksy(int valinta) {
        if (valinta == 7) {
            AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.GRAFIIKKATESTI_VALIKKO;
        }
        if (valinta == asetustenMäärä -1) {
            AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.ASETUSRUUTU;
            valinta = 0;
        }
    }

    static void peruuta() {
        AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.ASETUSRUUTU;
        valinta = 0;
    }

    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        
        tilaTekstit[0].päivitäTeksti(KeimoEngine.window.isFullscreen() ? "Kyllä" : "Ei");
        tilaTekstit[1].päivitäTeksti(valittuResoluutio);
        tilaTekstit[2].päivitäTeksti("" + valittuNäyttö);
        tilaTekstit[3].päivitäTeksti("" + kaksiDesimaalia.format(zoom));
        tilaTekstit[4].päivitäTeksti("" + kaksiDesimaalia.format(kirkkaus));
        tilaTekstit[5].päivitäTeksti(KeimoEngine.window.isVsync() ? "Kyllä" : "Ei");
        tilaTekstit[6].päivitäTeksti(vapaaKamera ? "Kyllä" : "Ei");

        if (valinta > 4) scrollaus = valinta - 4;
        else scrollaus = 0;

        osoitinLabel.muutaOffsetY(1f/3f - (float)((valinta-scrollaus) - (valinta == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f));
        osoitinLabel.renderöiPyörivä(shader, window);

        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i-scrollaus) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, asetusTekstit[i], window, 1f/2.5f, 1f/15f, 1, 1f/2.5f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i-scrollaus) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, tilaTekstit[i], window, 1f/2.5f, 1f/15f, 1, 1f/2.5f +1f/4f, offsetY, 0);
        }

        otsikkoLabel.renderöi(shader, window);

        annaInfoTeksti(valinta);
        infoTekstiLabel.renderöi(shader, window);

        if (päivitäVsync) {
            KeimoEngine.window.setVSync(vsync);
            päivitäVsync = false;
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
            case 6: infoTeksti.päivitäTeksti(infoTekstiVapaaKamera, 2); break;
            default: infoTeksti.päivitäTeksti(""); break;
        }
    }
}
