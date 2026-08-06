package keimo.seikkailupeli.ruudut.asetusRuudut;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenRenderöinti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu.AsetusRuudut;
import keimo.seikkailupeli.äänet.Äänet;

public class PeliAsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 4;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static Teksti infoTeksti = new Teksti("info", Väri.white, 2000, 300);
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -0.85f, 0, osoitinKuvake, 10, 0, 0);
    private static MenuKomponentti infoTekstiLabel = new MenuKomponentti(1, 0.25f, 0, -0.75f, infoTeksti);

    private static Renderöitävä[] asetusTekstit = new Renderöitävä[] {
        new Teksti("Vaikeusaste", Väri.white, 600, 48),
        new Teksti("Pelin nopeus", Väri.white, 600, 48),
        new Teksti("Debug-tiedot (F3)", Väri.white, 600, 48),
        hyväksyTekstuuri,
    };

    private static Teksti[] tilaTekstit = new Teksti[] {
        new Teksti("Normaali", Väri.white, 800, 48),
        new Teksti("60", Väri.white, 600, 48),
        new Teksti("Ei", Väri.white, 600, 48),
        new Teksti("", Väri.white, 600, 48),
    };

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
            case "esc" -> {
                peruuta();
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
        
        tilaTekstit[0].päivitäTeksti(valittuVaikeusaste);
        tilaTekstit[1].päivitäTeksti("" + pelinNopeus);
        tilaTekstit[2].päivitäTeksti(PelinAsetukset.debugTiedot ? "Kyllä" : "Ei");

        otsikkoLabel.renderöi(shader, window);

        osoitinLabel.muutaOffsetY(1f/3f - (float)((valinta) - (valinta == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f));
        osoitinLabel.renderöiPyörivä(shader, window);

        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, asetusTekstit[i], window, 1f/2.5f, 1f/15f, 1, 1f/2.5f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, tilaTekstit[i], window, 1f/2.5f, 1f/15f, 1, 1f/2.5f +1f/4f, offsetY, 0);
        }
        annaInfoTeksti(valinta);
        infoTekstiLabel.renderöi(shader, window);
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
