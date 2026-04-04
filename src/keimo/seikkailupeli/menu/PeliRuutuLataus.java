package keimo.seikkailupeli.menu;

import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;

import java.awt.Color;

public class PeliRuutuLataus {
    private static Teksti tekstiTexture = new Teksti("Ladataan maailmaa", Color.white, 800, 300, KeimoFontit.fontti_keimo_100, true);
    private static MenuKomponentti tekstiLabel = new MenuKomponentti(1f/2f, 1f/4f, 0, 0, tekstiTexture);
    private static String latausteksti = "Ladataan maailmaa";

    public static void renderöi(Shader shader, Ikkuna window) {
        shader.bind();
        shader.nollaaShaderEfektit();

        tekstiTexture.päivitäTeksti(latausteksti, 2);
        tekstiLabel.renderöi(shader, window);
    }
}
