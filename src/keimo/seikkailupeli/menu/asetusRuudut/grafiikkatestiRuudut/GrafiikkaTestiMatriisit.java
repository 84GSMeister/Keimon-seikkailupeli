package keimo.seikkailupeli.menu.asetusRuudut.grafiikkatestiRuudut;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.menu.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.AsetusRuutu.AsetusRuudut;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;

import org.joml.Matrix4f;

public class GrafiikkaTestiMatriisit {
    private static int valinta = 0;
    private static int asetustenMäärä = 7;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -0.85f, 0, osoitinKuvake, 10, 0, 0);
    private static Matrix4f testiMatriisi;
    private static String matriisiStringInfo;

    private static Renderöitävä[] asetusTekstit = new Renderöitävä[] {
        new Teksti("Skaala", Color.white, 600, 48),
        new Teksti("Sij X", Color.white, 600, 48),
        new Teksti("Sij Y", Color.white, 600, 48),
        new Teksti("Rot X", Color.white, 600, 48),
        new Teksti("Rot Y", Color.white, 600, 48),
        new Teksti("Rot Z", Color.white, 600, 48),
        hyväksyTekstuuri,
    };

    private static Teksti[] tilaTekstit = new Teksti[] {
        new Teksti("0", Color.white, 800, 192),
        new Teksti("", Color.white, 600, 48),
        new Teksti("", Color.white, 600, 48),
        new Teksti("", Color.white, 600, 48),
        new Teksti("", Color.white, 600, 48),
        new Teksti("", Color.white, 600, 48),
        new Teksti("", Color.white, 600, 48),
    };

    public static void alusta() {
        testiMatriisi = new Matrix4f().translate(0, -0.5f, 0).scale(0.5f);
        matriisiStringInfo = matriisiString();
    }

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
            case 0: // Skaala
                if (kasvata) {
                    testiMatriisi.scale(0.5f);
                }
                else {
                    testiMatriisi.scale(2f);
                }
            break;
            case 1: // Sijainti X
                if (kasvata) {
                    testiMatriisi.translate(0.1f, 0, 0);
                }
                else {
                    testiMatriisi.translate(-0.1f, 0, 0);
                }
            break;
            case 2: // Sijainti Y
                if (kasvata) {
                    testiMatriisi.translate(0, 0.1f, 0);
                }
                else {
                    testiMatriisi.translate(0, -0.1f, 0);
                }
            break;
            case 3: // Rotaatio X
                if (kasvata) {
                    testiMatriisi.rotate((float)Math.toRadians(10), 1, 0, 0);
                }
                else {
                    testiMatriisi.rotate((float)-Math.toRadians(10), 1, 0, 0);
                }
            break;
            case 4: // Rotaatio Y
                if (kasvata) {
                    testiMatriisi.rotate((float)Math.toRadians(10), 0, 1, 0);
                }
                else {
                    testiMatriisi.rotate((float)-Math.toRadians(10), 0, 1, 0);
                }
            break;
            case 5: // Rotaatio Z
                if (kasvata) {
                    testiMatriisi.rotate((float)Math.toRadians(10), 0, 0, 1);
                }
                else {
                    testiMatriisi.rotate((float)-Math.toRadians(10), 0, 0, 1);
                }
            break;
            default:
            break;
        }
    }

    static void päivitäAsetukset() {
        matriisiStringInfo = matriisiString();
    }

    private static String matriisiString() {
        String matriisiString = "";
        for (int i = 0; i < 4; i++) {
            matriisiString += "[";
            for (int j = 0; j < 4; j++) {
                matriisiString += testiMatriisi.get(i, j) + ",";
            }
            matriisiString = matriisiString.substring(0, matriisiString.length()-1);
            matriisiString += "]\n";
        }
        return matriisiString;
    }

    static void hyväksy(int valinta) {
        if (valinta == asetustenMäärä -1) {
            AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.GRAFIIKKATESTI_VALIKKO;
            valinta = 0;
        }
    }

    static void peruuta() {
        AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.GRAFIIKKATESTI_VALIKKO;
        valinta = 0;
    }

    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        
        tilaTekstit[0].päivitäTeksti(matriisiStringInfo);

        otsikkoLabel.renderöi(shader, window);

        osoitinLabel.muutaOffsetY(1f/3f - (float)((valinta) - (valinta == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f));
        osoitinLabel.renderöiPyörivä(shader, window);

        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponenttiJaSkaalaa(shader, asetusTekstit[i], window, 1f/2.5f, 1f/15f, 1, 1f/2.5f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < 1; i++) {
            float offsetY = 1f/3f - (float)((i+2) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponenttiJaSkaalaa(shader, tilaTekstit[i], window, 1f/2.5f, 1f/4f, 1, 1f/2.5f +1f/4f, offsetY, 0);
        }
        
        renderöiValittuTekstuuri(shader, window);
    }

    private static void renderöiValittuTekstuuri(Shader shader, Ikkuna window) {
        shader.asetaSijainti(testiMatriisi);
        Assets.annaTekstuuri("kuparilager").bind(0);
        Assets.getModel().render();
    }
}
