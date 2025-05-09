package keimo.keimoEngine.menu.asetusRuudut;

import keimo.PelinAsetukset;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Animaatio;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.äänet.Äänet;

import java.awt.Color;

import org.joml.Matrix4f;

public class PeliAsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 4;
    private static Shader asetusRuutuShader = new Shader("staattinen");
    private static Tekstuuri otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_asetukset.png");
    private static Animaatio osoitinKuvake = new Animaatio(30, "tiedostot/kuvat/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");

    private static Teksti asetusVaikeusasteTeksti = new Teksti("Vaikeusaste", Color.white, 200, 16);
    private static Teksti asetusNopeusTeksti = new Teksti("Pelin nopeus", Color.white, 200, 16);
    private static Teksti asetusDebugInfoTeksti = new Teksti("Debug-tiedot (F3)", Color.white, 200, 16);
    private static Tekstuuri hyväksyTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/asetukset_takaisin.png");

    private static Teksti tilaVaikeusasteTeksti = new Teksti("Normaali", Color.white, 300, 16);
    private static Teksti tilaNopeusTeksti = new Teksti("60", Color.white, 200, 16);
    private static Teksti tilaDebugInfoTeksti = new Teksti("Ei", Color.white, 200, 16);

    private static String[] vaikeusasteet = {"Passiivinen", "Normaali", "Vaikea", "Järjetön"};
    private static int vaikeusasteValInt = 1;
    private static String valittuVaikeusaste = "Normaali";
    private static int pelinNopeus = 60;

    private static Teksti infoTeksti = new Teksti("info", Color.white, 700, 100);
    private static String infoTekstiVaikeusaste = "Pelin vaikeusaste\n" + 
    "Passiivinen: viholliset eivät tee vahinkoa\n" +
    "Normaali: viholliset tekevät normaalia vahinkoa\n" +
    "Vaikea: viholliset tekevät kaksinkertaista vahinkoa\n" +
    "Järjetön: viholliset tekevät 10-kertaista vahinkoa\n";
    private static String infoTekstiNopeus = "Pelin nopeus\n" +
    "Vaikuttaa pelin framerate- ja tickrate-nopeuteen.\n" +
    "Oletus: 60";
    private static String infoTekstiDebug = "Debug-tiedot\n" +
    "Näytä lisätietoja pelin tilasta (kehittäjiä varten).";

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
            case 0: // Vaikeusaste
                if (kasvata) {
                    if (vaikeusasteValInt < vaikeusasteet.length-1) vaikeusasteValInt++;
                }
                else {
                    if (vaikeusasteValInt > 0) vaikeusasteValInt--;
                }
                valittuVaikeusaste = vaikeusasteet[vaikeusasteValInt];
            break;
            case 1: // Pelin nopeus
                if (kasvata) {
                    if (pelinNopeus < 1000) pelinNopeus++;
                }
                else {
                    if (pelinNopeus > 1) pelinNopeus--;
                }
            break;
            case 2: // Debug-tiedot
                PelinAsetukset.debugTiedot = !PelinAsetukset.debugTiedot;
            break;
            case 3: // 

            break;
            case 4: // Hyväksy
                
            break;
            default:
            break;
        }
    }

    static void päivitäAsetukset() {
        PelinAsetukset.valitseVaikeusaste(valittuVaikeusaste);
        PelinAsetukset.pelinNopeus = pelinNopeus;
    }

    static void hyväksy(int valinta) {
        if (valinta == 3) {
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
                case 0: asetusVaikeusasteTeksti.bind(0); break;
                case 1: asetusNopeusTeksti.bind(0); break;
                case 2: asetusDebugInfoTeksti.bind(0); break;
                default: hyväksyTekstuuri.bind(0); break;
            }
            Assets.getModel().render();
        }

        for (int i = 0; i < asetustenMäärä; i++) {
            Matrix4f matStatus = new Matrix4f();
            window.getView().scale(1, matStatus);
            matStatus.translate(scaleXValinnat + keskitysX, scaleYOtsikko*4f -i*offsetYValinta, 0);
            matStatus.scale(scaleXValinnat, scaleYValinnat, 0);
            asetusRuutuShader.setUniform("projection", matStatus);
            tilaVaikeusasteTeksti.päivitäTeksti(valittuVaikeusaste);
            tilaNopeusTeksti.päivitäTeksti("" + pelinNopeus);
            tilaDebugInfoTeksti.päivitäTeksti(PelinAsetukset.debugTiedot ? "Kyllä" : "Ei");
            switch (i) {
                case 0: tilaVaikeusasteTeksti.bind(0); break;
                case 1: tilaNopeusTeksti.bind(0); break;
                case 2: tilaDebugInfoTeksti.bind(0); break;
                default: tyhjäTekstuuri.bind(0); break;
            }
            Assets.getModel().render();
        }

        Matrix4f matInfoTeksti = new Matrix4f();
        window.getView().scale(1, matInfoTeksti);
        matInfoTeksti.translate(0, -window.getHeight()/2+scaleYInfo, 0);
        matInfoTeksti.scale(scaleXInfo, scaleYInfo, 0);
        asetusRuutuShader.setUniform("projection", matInfoTeksti);
        switch (valinta) {
            case 0: infoTeksti.päivitäTeksti(infoTekstiVaikeusaste, false, 58); break;
            case 1: infoTeksti.päivitäTeksti(infoTekstiNopeus); break;
            case 2: infoTeksti.päivitäTeksti(infoTekstiDebug); break;
            default: infoTeksti.päivitäTeksti(""); break;
        }
        infoTeksti.bind(0);
        Assets.getModel().render();
    } 
}
