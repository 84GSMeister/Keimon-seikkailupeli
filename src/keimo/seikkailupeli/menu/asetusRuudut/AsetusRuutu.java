package keimo.seikkailupeli.menu.asetusRuudut;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;

public class AsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 5;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);

    private static Teksti asetus1Teksti = new Teksti("Grafiikka", Color.white, 400, 48);
    private static Teksti asetus2Teksti = new Teksti("Ääni", Color.white, 400, 48);
    private static Teksti asetus3Teksti = new Teksti("Peli", Color.white,400, 48);
    private static Teksti asetus4Teksti = new Teksti("Ohjaimet", Color.white,400, 48);
    
    public static boolean pelissä = false;

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
            case "enter" -> {
                valitse(valinta);
            }
            case "esc" -> {
                peruuta();
            }
        }
        Äänet.toistaSFX("Valinta");
    }

    static void valitse(int valinta) {

        switch (valinta) {
            case 0: // Grafiikka
                KeimoEngine.valitseAktiivinenRuutu("asetusruutu_grafiikka");
                break;
            case 1: // Ääni
                KeimoEngine.valitseAktiivinenRuutu("asetusruutu_ääni");
                break;
            case 2: // Peli
                KeimoEngine.valitseAktiivinenRuutu("asetusruutu_peli");
                break;
            case 3: // Peli
                KeimoEngine.valitseAktiivinenRuutu("asetusruutu_ohjaimet");
                break;
            case 4: // Takaisin
                hyväksy();
                peruuta();
                break;
            default:
                break;
        }
    }

    private static void hyväksy() {

    }

    private static void peruuta() {
        if (pelissä) {
            Peli.syötteenTila = SyötteenTila.TOIMINTO;
            KeimoEngine.valitseAktiivinenRuutu("peliruutu");
            Peli.pause = true;
        }
        else KeimoEngine.valitseAktiivinenRuutu("valikkoruutu");
    }

    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        otsikkoLabel.renderöi(shader, window);

        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = -1f/7.5f + (float)((2-i) + (i == asetustenMäärä-1 ? 0 : 1)) * (1f/5f);
            Komponentti.renderöiKomponentti(shader, annaOsoitinKuvake(i), window, 1f/10f, 1f/10f, 1, -0.6f, offsetY, 0);
        }

        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = -1f/7.5f + ((2-i) + (i == asetustenMäärä-1 ? 0 : 1)) * (1f/5f);
            Komponentti.renderöiKomponentti(shader, annaAsetusTekstuuri(i), window, 1f/2f, 1f/15f, 1, 0, offsetY, 0);
        }
    }

    private static Renderöitävä annaAsetusTekstuuri(int indeksi) {
        switch (indeksi) {
            case 0: return asetus1Teksti;
            case 1: return asetus2Teksti;
            case 2: return asetus3Teksti;
            case 3: return asetus4Teksti;
            case 4: return hyväksyTekstuuri;
            default: return hyväksyTekstuuri;
        }
    }

    private static Renderöitävä annaOsoitinKuvake(int valikkoElementti) {
        if (valikkoElementti == valinta) return osoitinKuvake;
        else return tyhjäTekstuuri;
    }
}
