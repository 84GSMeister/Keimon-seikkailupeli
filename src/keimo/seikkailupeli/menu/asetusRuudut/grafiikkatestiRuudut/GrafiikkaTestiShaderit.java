package keimo.seikkailupeli.menu.asetusRuudut.grafiikkatestiRuudut;

import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.menu.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.AsetusRuutu.AsetusRuudut;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.joml.Matrix4f;

public class GrafiikkaTestiShaderit {
    private static int valinta = 0;
    private static int asetustenMäärä = 2;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -0.85f, 0, osoitinKuvake, 10, 0, 0);

    private static Renderöitävä[] asetusTekstit = new Renderöitävä[] {
        new Teksti("Shader", Color.white, 600, 48),
        hyväksyTekstuuri,
    };

    private static Teksti[] tilaTekstit = new Teksti[] {
        new Teksti("0", Color.white, 800, 48),
        new Teksti("", Color.white, 600, 48),
    };

    private static int shadereita = Integer.MAX_VALUE;
    private static int shaderValinta = 0;

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
            case 0: // Shader
                if (kasvata) {
                    if (shaderValinta < shadereita-1) shaderValinta++;
                }
                else {
                    if (shaderValinta > 0) shaderValinta--;
                }
            break;
            case 1: // Hyväksy
                
            break;
            default:
            break;
        }
    }

    static void päivitäAsetukset() {

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
        
        tilaTekstit[0].päivitäTeksti("" + shaderValinta);

        otsikkoLabel.renderöi(shader, window);

        osoitinLabel.muutaOffsetY(1f/3f - (float)((valinta) - (valinta == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f));
        osoitinLabel.renderöiPyörivä(shader, window);

        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponenttiJaSkaalaa(shader, asetusTekstit[i], window, 1f/2.5f, 1f/15f, 1, 1f/2.5f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponenttiJaSkaalaa(shader, tilaTekstit[i], window, 1f/2.5f, 1f/15f, 1, 1f/2.5f +1f/4f, offsetY, 0);
        }
        
        renderöiValittuShader();
    }

    private static void renderöiValittuShader() {
        Matrix4f tekstuurinSijainti = new Matrix4f();
        tekstuurinSijainti.translate(0, -0.5f, 0);
        tekstuurinSijainti.scale(0.5f, 0.5f, 0);
        glUseProgram(shaderValinta);
        glUniformMatrix4fv(glGetUniformLocation(shaderValinta, "projection"), false, tekstuurinSijainti.get(new float[16]));
        Assets.annaTekstuuri("kuparilager").bind(0);
        Assets.getModel().render();
    }
}
