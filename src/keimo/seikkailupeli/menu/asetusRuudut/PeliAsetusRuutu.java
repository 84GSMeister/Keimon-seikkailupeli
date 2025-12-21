package keimo.seikkailupeli.menu.asetusRuudut;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.Animaatio;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.ikkuna.Window;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;

public class PeliAsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 4;
    private static Tekstuuri otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_asetukset.png");
    private static Animaatio osoitinKuvake = new Animaatio("tiedostot/kuvat/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");
    private static Teksti infoTeksti = new Teksti("info", Color.white, 2000, 300);
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti infoTekstiLabel = new MenuKomponentti(1, 0.25f, 0, -0.75f, infoTeksti);

    private static Teksti asetusVaikeusasteTeksti = new Teksti("Vaikeusaste", Color.white, 600, 48);
    private static Teksti asetusNopeusTeksti = new Teksti("Pelin nopeus", Color.white, 600, 48);
    private static Teksti asetusDebugInfoTeksti = new Teksti("Debug-tiedot (F3)", Color.white, 600, 48);
    private static Tekstuuri hyväksyTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/asetukset_takaisin.png");

    private static Teksti tilaVaikeusasteTeksti = new Teksti("Normaali", Color.white, 800, 48);
    private static Teksti tilaNopeusTeksti = new Teksti("60", Color.white, 600, 48);
    private static Teksti tilaDebugInfoTeksti = new Teksti("Ei", Color.white, 600, 48);

    private static String[] vaikeusasteet = {"Passiivinen", "Normaali", "Vaikea", "Järjetön"};
    private static int vaikeusasteValInt = 1;
    private static String valittuVaikeusaste = "Normaali";
    private static int pelinNopeus = 60;

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

    public static void render(Shader shader, Window window) {
        shader.bind();
        
        tilaVaikeusasteTeksti.päivitäTeksti(valittuVaikeusaste);
        tilaNopeusTeksti.päivitäTeksti("" + pelinNopeus);
        tilaDebugInfoTeksti.päivitäTeksti(PelinAsetukset.debugTiedot ? "Kyllä" : "Ei");

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
            case 0: return asetusVaikeusasteTeksti;
            case 1: return asetusNopeusTeksti;
            case 2: return asetusDebugInfoTeksti;
            default: return hyväksyTekstuuri;
        }
    }

    private static Renderöitävä annaOsoitinKuvake(int valikkoElementti) {
        if (valikkoElementti == valinta) return osoitinKuvake;
        else return tyhjäTekstuuri;
    }

    private static Renderöitävä annaTilaTeksti(int indeksi) {
        switch (indeksi) {
            case 0: return tilaVaikeusasteTeksti;
            case 1: return tilaNopeusTeksti;
            case 2: return tilaDebugInfoTeksti;
            default: return tyhjäTekstuuri;
        }
    }

    private static void annaInfoTeksti(int indeksi) {
        switch (indeksi) {
            case 0: infoTeksti.päivitäTeksti(infoTekstiVaikeusaste, 0, 58); break;
            case 1: infoTeksti.päivitäTeksti(infoTekstiNopeus); break;
            case 2: infoTeksti.päivitäTeksti(infoTekstiDebug); break;
            default: infoTeksti.päivitäTeksti(""); break;
        }
    }
}
