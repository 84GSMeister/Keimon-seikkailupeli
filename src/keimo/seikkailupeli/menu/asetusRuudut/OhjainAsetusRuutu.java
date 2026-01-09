package keimo.seikkailupeli.menu.asetusRuudut;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.PelinAsetukset.OhjainKuvakkeet;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;

public class OhjainAsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 2;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static Teksti infoTeksti = new Teksti("info", Color.white, 2000, 300);
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti infoTekstiLabel = new MenuKomponentti(1, 0.25f, 0, -0.75f, infoTeksti);

    private static Teksti asetusOhjainkuvakkeetTeksti = new Teksti("Ohjainkuvakkeet", Color.white, 600, 48);

    private static Teksti tilaOhjainkuvakkeetTeksti = new Teksti("ABYX (Xbox)", Color.white, 800, 48);

    private static String[] ohjainKuvakeValinnat = {"ABYX (Xbox)", "BAXY (Nintendo)", "X○▲□ (Playstation)"};
    private static int ohjainKuvakeValinta = 0;
    private static String valittuOhjainKuvake = "ABYX (Xbox)";

    private static String infoTekstiOhjainkuvakkeet = "Valitse ohjainkuvakkeet\n" + 
    "Vaikuttaa ainoastaan visuaalisiin kuvakkeisiin.\n" +
    "Näppäinten uudelleenmääritys tulossa myöhemmin.";

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
            case 0: // Ohjainkuvakkeet
                if (kasvata) {
                    if (ohjainKuvakeValinta < ohjainKuvakeValinnat.length-1) ohjainKuvakeValinta++;
                }
                else {
                    if (ohjainKuvakeValinta > 0) ohjainKuvakeValinta--;
                }
                valittuOhjainKuvake = ohjainKuvakeValinnat[ohjainKuvakeValinta];
            break;
            case 1: // Hyväksy
                
            break;
            default:
            break;
        }
    }

    static void päivitäAsetukset() {
        switch (valittuOhjainKuvake) {
            case "ABYX (Xbox)": PelinAsetukset.ohjainKuvakkeet = OhjainKuvakkeet.XBOX; break;
            case "BAXY (Nintendo)": PelinAsetukset.ohjainKuvakkeet = OhjainKuvakkeet.NINTENDO; break;
            case "X○▲□ (Playstation)": PelinAsetukset.ohjainKuvakkeet = OhjainKuvakkeet.PLAYSTATION; break;
        }
    }

    static void hyväksy(int valinta) {
        if (valinta == 1) {
            KeimoEngine.valitseAktiivinenRuutu("asetusruutu");
        }
    }

    static void peruuta() {
        KeimoEngine.valitseAktiivinenRuutu("asetusruutu");
    }

    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        
        tilaOhjainkuvakkeetTeksti.päivitäTeksti(valittuOhjainKuvake);

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
            case 0: return asetusOhjainkuvakkeetTeksti;
            default: return hyväksyTekstuuri;
        }
    }

    private static Renderöitävä annaOsoitinKuvake(int valikkoElementti) {
        if (valikkoElementti == valinta) return osoitinKuvake;
        else return tyhjäTekstuuri;
    }

    private static Renderöitävä annaTilaTeksti(int indeksi) {
        switch (indeksi) {
            case 0: return tilaOhjainkuvakkeetTeksti;
            default: return tyhjäTekstuuri;
        }
    }

    private static void annaInfoTeksti(int indeksi) {
        switch (indeksi) {
            case 0: infoTeksti.päivitäTeksti(infoTekstiOhjainkuvakkeet, 0, 58); break;
            default: infoTeksti.päivitäTeksti(""); break;
        }
    }
}
