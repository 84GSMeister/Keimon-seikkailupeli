package keimo.keimoEngine.menu.asetusRuudut;

import keimo.PelinAsetukset;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.ikkuna.Kamera;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.kenttä.Maailma;
import keimo.keimoEngine.äänet.Äänet;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.joml.Matrix4f;

public class GrafiikkaAsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 6;
    private static Shader asetusRuutuShader = new Shader("staattinen");
    private static Tekstuuri otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_asetukset.png");
    private static Animaatio osoitinKuvake = new Animaatio(30, "tiedostot/kuvat/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");

    private static Teksti asetusKokonäyttöTeksti = new Teksti("Kokonäyttö (F11)", Color.white, 200, 16);
    private static Teksti asetusResoluutioTeksti = new Teksti("Resoluutio", Color.white, 200, 16);
    private static Teksti asetusZoomTeksti = new Teksti("Näköetäisyys", Color.white, 200, 16);
    private static Teksti asetusKirkkausTeksti = new Teksti("Kirkkaus", Color.white, 200, 16);
    private static Teksti asetusVsyncTeksti = new Teksti("Pystysynkronointi", Color.white, 200, 16);
    private static Tekstuuri hyväksyTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/asetukset_takaisin.png");

    private static Teksti tilaKokonäyttöTeksti = new Teksti("Ei", Color.white, 200, 16);
    private static Teksti tilaResoluutioTeksti = new Teksti("Natiivi", Color.white, 300, 16);
    private static Teksti tilaZoomTeksti = new Teksti("1x", Color.white, 200, 16);
    private static Teksti tilaKirkkausTeksti = new Teksti("100%", Color.white, 200, 16);
    private static Teksti tilaVsyncTeksti = new Teksti("Ei", Color.white, 200, 16);

    //private static String[] resoluutiot = {"1920x1080", "1366x768", "1280x720", "800x600", "640x480"};
    private static ArrayList<String> resoluutiot = Window.annaResoluutiot();
    private static boolean kokonäyttö = false, vsync = false;
    private static int resoluutioValInt = resoluutiot.size()-1;
    private static String valittuResoluutio = "1920x1080";
    private static int resoluutioX, resoluutioY;
    private static float zoom = 1f;
    private static float kirkkaus = 1f;
    private static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

    private static Teksti infoTeksti = new Teksti("info", Color.white, 700, 100);
    private static String infoTekstiKokonäyttö = "Valitse kokonäyttö- tai ikkunoitu tila\n";
    private static String infoTekstiResoluutio = "Valitse resoluutio\n" + 
    "Vaikuttaa vain kokonäyttötilassa\n" +
    "Ikkunoidussa tilassa resoluutio on vapaasti säädettävissä ikkunan koon perusteella.";
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
                }
                else {
                    if (kirkkaus > 0.025f) kirkkaus -= 0.05f;
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

    public static void render(Window window) {
        asetusRuutuShader.bind();
        float scaleXOtsikko = 1;
        if (window.getWidth() != 0 && window.getWidth() != 0) {
            scaleXOtsikko = window.getWidth()/ (window.getWidth()*2/window.getHeight());
        }
        float scaleYOtsikko = window.getHeight()/16;
        float scaleXValinnat = window.getWidth()/4;
        float scaleYValinnat = window.getHeight()/30;
        float scaleXOsoitin = window.getHeight()/20;
        float scaleYOsoitin = scaleYValinnat;
        float scaleXInfo = scaleXOtsikko;
        float scaleYInfo = window.getHeight()/8;
        float keskitysX = window.getWidth()/4;
        float offsetYValinta = window.getHeight()/15;

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(0, scaleYOtsikko*6f, 0);
        matOtsikko.scale(scaleXOtsikko, scaleYOtsikko, 0);
        asetusRuutuShader.setUniform("projection", matOtsikko);
        otsikkoTekstuuri.bind(0);
        Assets.getModel().render();

        for (int i = 0; i < asetustenMäärä; i++) {
            Matrix4f matOsoitin = new Matrix4f();
            window.getView().scale(1, matOsoitin);
            if (i == asetustenMäärä-1) matOsoitin.translate(-scaleXOsoitin - keskitysX*1.5f, scaleYOtsikko*4f -1.2f*i*offsetYValinta, 0);
            else matOsoitin.translate(-scaleXOsoitin - keskitysX*1.5f, scaleYOtsikko*4f -i*offsetYValinta, 0);
            matOsoitin.scale(scaleXOsoitin, scaleYOsoitin, 0);
            asetusRuutuShader.setUniform("projection", matOsoitin);
            if (valinta == i) osoitinKuvake.bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();
        }

        for (int i = 0; i < asetustenMäärä; i++) {
            Matrix4f matValinta = new Matrix4f();
            window.getView().scale(1, matValinta);
            if (i == asetustenMäärä-1) matValinta.translate(scaleXValinnat - keskitysX*1.5f, scaleYOtsikko*4f -1.2f*i*offsetYValinta, 0);
            else matValinta.translate(scaleXValinnat - keskitysX*1.5f, scaleYOtsikko*4f -i*offsetYValinta, 0);
            matValinta.scale(scaleXValinnat, scaleYValinnat, 0);
            asetusRuutuShader.setUniform("projection", matValinta);
            switch (i) {
                case 0: asetusKokonäyttöTeksti.bind(0); break;
                case 1: asetusResoluutioTeksti.bind(0); break;
                case 2: asetusZoomTeksti.bind(0); break;
                case 3: asetusKirkkausTeksti.bind(0); break;
                case 4: asetusVsyncTeksti.bind(0); break;
                case 5: hyväksyTekstuuri.bind(0); break;
            }
            Assets.getModel().render();
        }

        tilaKokonäyttöTeksti.päivitäTeksti(KeimoEngine.window.isFullscreen() ? "Kyllä" : "Ei");
        tilaResoluutioTeksti.päivitäTeksti(valittuResoluutio);
        tilaZoomTeksti.päivitäTeksti("" + kaksiDesimaalia.format(zoom));
        tilaKirkkausTeksti.päivitäTeksti("" + kaksiDesimaalia.format(kirkkaus));
        tilaVsyncTeksti.päivitäTeksti(KeimoEngine.window.isVsync() ? "Kyllä" : "Ei");
        for (int i = 0; i < asetustenMäärä; i++) {
            Matrix4f matStatus = new Matrix4f();
            window.getView().scale(1, matStatus);
            matStatus.translate(scaleXValinnat + keskitysX, scaleYOtsikko*4f -i*offsetYValinta, 0);
            matStatus.scale(scaleXValinnat, scaleYValinnat, 0);
            asetusRuutuShader.setUniform("projection", matStatus);
            switch (i) {
                case 0: tilaKokonäyttöTeksti.bind(0); break;
                case 1: tilaResoluutioTeksti.bind(0); break;
                case 2: tilaZoomTeksti.bind(0); break;
                case 3: tilaKirkkausTeksti.bind(0); break;
                case 4: tilaVsyncTeksti.bind(0); break;
                case 5: tyhjäTekstuuri.bind(0); break;
            }
            Assets.getModel().render();
        }

        Matrix4f matInfoTeksti = new Matrix4f();
        window.getView().scale(1, matInfoTeksti);
        matInfoTeksti.translate(0, -window.getHeight()/2+scaleYInfo, 0);
        matInfoTeksti.scale(scaleXInfo, scaleYInfo, 0);
        asetusRuutuShader.setUniform("projection", matInfoTeksti);
        switch (valinta) {
            case 0: infoTeksti.päivitäTeksti(infoTekstiKokonäyttö, false, 58); break;
            case 1: infoTeksti.päivitäTeksti(infoTekstiResoluutio); break;
            case 2: infoTeksti.päivitäTeksti(infoTekstiNäköetäisyys); break;
            case 3: infoTeksti.päivitäTeksti(infoTekstiKirkkaus); break;
            case 4: infoTeksti.päivitäTeksti(infoTekstiVsync); break;
            default: infoTeksti.päivitäTeksti(""); break;
        }
        infoTeksti.bind(0);
        Assets.getModel().render();
    } 
}
