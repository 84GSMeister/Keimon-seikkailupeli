package keimo.seikkailupeli.ruudut.asetusRuudut.grafiikkatestiRuudut;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenRenderöinti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu.AsetusRuudut;
import keimo.seikkailupeli.äänet.Äänet;

import static org.lwjgl.opengl.GL11.*;
import org.joml.Matrix4f;

public class GrafiikkaTestiTekstuurit {
    private static int valinta = 0;
    private static int asetustenMäärä = 2;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -0.85f, 0, osoitinKuvake, 10, 0, 0);

    private static Renderöitävä[] asetusTekstit = new Renderöitävä[] {
        new Teksti("Tekstuuri", Väri.white, 600, 48),
        hyväksyTekstuuri,
    };

    private static Teksti[] tilaTekstit = new Teksti[] {
        new Teksti("0", Väri.white, 800, 48),
        new Teksti("", Väri.white, 600, 48),
    };

    private static int tekstuureja = Renderöitävä.tekstuureja;
    private static int tekstuuriValinta = 0;

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
            case 0: // Tekstuuri
                if (kasvata) {
                    if (tekstuuriValinta < tekstuureja-1) tekstuuriValinta++;
                }
                else {
                    if (tekstuuriValinta > 0) tekstuuriValinta--;
                }
            break;
            case 1: // Hyväksy
                
            break;
            default:
            break;
        }
    }

    static void päivitäAsetukset() {
        tekstuureja = Renderöitävä.tekstuureja;
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
        
        tilaTekstit[0].päivitäTeksti("" + tekstuuriValinta);

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

        renderöiValittuTekstuuri(shader, window);
    }

    private static void renderöiValittuTekstuuri(Shader shader, Ikkuna window) {
        Matrix4f tekstuurinSijainti = new Matrix4f();
        tekstuurinSijainti.translate(0, -0.5f, 0);
        tekstuurinSijainti.scale(0.5f, 0.5f, 0);
        shader.asetaSijainti(tekstuurinSijainti);
        glBindTexture(GL_TEXTURE_2D, tekstuuriValinta);
        Assets.getModel().render();
    }
}
