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
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;

public class AsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 4;
    private static Tekstuuri otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_asetukset.png");
    private static Animaatio osoitinKuvake = new Animaatio("tiedostot/kuvat/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);

    private static Teksti asetus1Teksti = new Teksti("Grafiikka", Color.white, 400, 48);
    private static Teksti asetus2Teksti = new Teksti("Ääni", Color.white, 400, 48);
    private static Teksti asetus3Teksti = new Teksti("Peli", Color.white,400, 48);
    private static Tekstuuri hyväksyTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/asetukset_takaisin.png");
    
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
            case 3: // Takaisin
                hyväksy();
                if (pelissä) {
                    Peli.syötteenTila = SyötteenTila.TOIMINTO;
                    KeimoEngine.valitseAktiivinenRuutu("peliruutu");
                    Peli.pause = true;
                }
                else KeimoEngine.valitseAktiivinenRuutu("valikkoruutu");
                break;
            default:
                break;
        }
    }

    private static void hyväksy() {

    }

    public static void render(Shader shader, Window window) {
        shader.bind();
        // float keskitysX = 1f/2f;
        // float skaalaX = 1, skaalaY = 1f/8f, skaalaZ = 1;
        // float offsetX = 0, offsetY = skaalaY*6, offsetZ = 0;
        // komponentti.render(shader, otsikkoTekstuuri, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);
        otsikkoLabel.renderöi(shader, window);

        for (int i = 0; i < asetustenMäärä; i++) {
            // skaalaX = 1f/10f; skaalaY = 1f/10f; skaalaZ = 1;
            // offsetX = -skaalaX -keskitysX; offsetY = -1f/7.5f + (float)((2-i) + (i == asetustenMäärä-1 ? 0 : 1)) * (1f/5f); offsetZ = 0;
            // komponentti.render(shader, annaOsoitinKuvake(i), window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);
            float offsetY = -1f/7.5f + (float)((2-i) + (i == asetustenMäärä-1 ? 0 : 1)) * (1f/5f);
            Komponentti.renderöiKomponentti(shader, annaOsoitinKuvake(i), window, 1f/10f, 1f/10f, 1, -0.6f, offsetY, 0);
        }

        for (int i = 0; i < asetustenMäärä; i++) {
            // skaalaX = 1f/2f; skaalaY = 1f/15f; skaalaZ = 1;
            // offsetX = skaalaX -keskitysX; offsetY = -1f/7.5f + ((2-i) + (i == asetustenMäärä-1 ? 0 : 1)) * (1f/5f); offsetZ = 0;
            // komponentti.render(shader, annaAsetusTekstuuri(i), window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);
            float offsetY = -1f/7.5f + ((2-i) + (i == asetustenMäärä-1 ? 0 : 1)) * (1f/5f);
            Komponentti.renderöiKomponentti(shader, annaAsetusTekstuuri(i), window, 1f/2f, 1f/15f, 1, 0, offsetY, 0);
        }
    }

    private static Renderöitävä annaAsetusTekstuuri(int indeksi) {
        switch (indeksi) {
            case 0: return asetus1Teksti;
            case 1: return asetus2Teksti;
            case 2: return asetus3Teksti;
            case 3: return hyväksyTekstuuri;
            default: return hyväksyTekstuuri;
        }
    }

    private static Renderöitävä annaOsoitinKuvake(int valikkoElementti) {
        if (valikkoElementti == valinta) return osoitinKuvake;
        else return tyhjäTekstuuri;
    }
}
