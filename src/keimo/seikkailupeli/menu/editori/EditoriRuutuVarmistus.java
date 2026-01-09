package keimo.seikkailupeli.menu.editori;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;

public class EditoriRuutuVarmistus {
    private static Teksti otsikkoTeksti = new Teksti("Siirry editoriin?", Color.white, 720, 100, KeimoFontit.fontti_keimo_36, true);
    private static Teksti tekstiTexture = new Teksti("Varmistusteksti", Color.white, 3000, 900);
    private static Teksti jatkaNappiTexture = new Teksti("Jatka", Color.white, 430, 48);
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    private static Renderöitävä takaisinTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static MenuKomponentti ostikkoLabel = new MenuKomponentti(1, 1f/6f, 0, 5f/6f, otsikkoTeksti);
    private static MenuKomponentti tekstiLabel = new MenuKomponentti(1, 0.5f, 0, 0, tekstiTexture);

    private static String editorinVarmistusTeksti = "Pelinsisäinen editori on erittäin varhaisessa kehitysvaiheessa.\n" +
                                                    "Tavoitteena on kehittää uusi integroitu editori, joka toimii pelin kanssa yhdessä.\n\n" +
                                                    "Vanha editori tullaan jakelemaan erillisenä sovelluksena ainakin niin pitkään, " + 
                                                    "kun siinä on laajemmat ominaisuudet kuin pelinsisäisessä editorissa. " +
                                                    "Jos haluat käyttää vanhaa editoria, avaa se pelin tiedostoista.\n\n\n" +
                                                    "Haluatko siirtyä editoriin?";
    private static int valinta;
    private static int vaihtoehtojenMäärä = 2;

    public static void painaNäppäintä(String näppäin) {
        switch (näppäin) {
            case "ylös" -> {
                valinta--;
                if (valinta < 0) {
                    valinta = vaihtoehtojenMäärä-1;
                }
            }
            case "alas" -> {
                valinta++;
                if (valinta > vaihtoehtojenMäärä-1) {
                    valinta = 0;
                }
            }
            case "enter" -> {
                hyväksy(valinta);
            }
        }
        Äänet.toistaSFX("Valinta");
    }

    static void hyväksy(int valinta) {
        switch (valinta) {
            case 0: // Jatka
                jatka();
                break;
            case 1: // Takaisin
                takaisin();
                break;
            default:
                break;
        }
    }

    public static void jatka() {
        Äänet.toistaSFX("Valinta");
        KeimoEngine.valitseAktiivinenRuutu("editoriruutu");
    }

    public static void takaisin() {
        Äänet.toistaSFX("Valinta");
        KeimoEngine.valitseAktiivinenRuutu("valikkoruutu");
    }

    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        shader.nollaaShaderEfektit();

        ostikkoLabel.renderöi(shader, window);

        tekstiTexture.päivitäTeksti(editorinVarmistusTeksti, 2);
        tekstiLabel.renderöi(shader, window);

        for (int i = 0; i < vaihtoehtojenMäärä; i++) {
            float offsetX = i*(1f/1.5f);
            Komponentti.renderöiKomponentti(shader, annaValikkoTeksti(i), window, 0.5f, 0.1f, 1, offsetX, -0.5f, 0);
        }

        for (int i = 0; i < vaihtoehtojenMäärä; i++) {
            float offsetX = -0.6f + i*(1f/1.5f);
            Komponentti.renderöiKomponentti(shader, annaOsoitinKuvake(i), window, 0.1f, 0.1f, 1, offsetX, -0.5f, 0);
        }
    }

    private static Renderöitävä annaValikkoTeksti(int valikkoElementti) {
        switch (valikkoElementti) {
            case 0: return jatkaNappiTexture;
            case 1: return takaisinTekstuuri;
            default: return jatkaNappiTexture;
        }
    }

    private static Renderöitävä annaOsoitinKuvake(int valikkoElementti) {
        if (valikkoElementti == valinta) return osoitinKuvake;
        else return tyhjäTekstuuri;
    }
}
