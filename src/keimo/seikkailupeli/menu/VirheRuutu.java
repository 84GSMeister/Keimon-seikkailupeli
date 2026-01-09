package keimo.seikkailupeli.menu;

import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.Ruudut;

import java.awt.Color;

public class VirheRuutu {
    private static Teksti otsikkoTeksti = new Teksti("Paskan möivät", Color.white, 2000, 300, KeimoFontit.fontti_keimo_100, true);
    private static Teksti tekstiTexture = new Teksti("Virheteksti", Color.white, 3000, 900);
    private static Teksti näppäimetTexture = new Teksti("F1: Käynnistä uudelleen    Esc: sulje", Color.white, 4200, 300, KeimoFontit.fontti_keimo_100, false);
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/6f, 0, 5f/6f, otsikkoTeksti);
    private static MenuKomponentti tekstiLabel = new MenuKomponentti(1, 1f/2f, 0, 0, tekstiTexture);
    private static MenuKomponentti näppäimetLabel = new MenuKomponentti(1, 1f/6f, 0, -5f/6f, näppäimetTexture);
    private static String virheteksti = "";

    public static void siirryVirheruutuun(String virheviesti) {
        virheteksti = virheviesti;
        Peli.aktiivinenRuutu = Ruudut.VIRHERUUTU;
    }

    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        shader.nollaaShaderEfektit();
        otsikkoLabel.renderöi(shader, window);

        tekstiTexture.päivitäTeksti(virheteksti, 2);
        tekstiLabel.renderöi(shader, window);

        näppäimetLabel.renderöi(shader, window);
    }
}
