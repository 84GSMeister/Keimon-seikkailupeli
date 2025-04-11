package keimo.keimoEngine.menu;

import keimo.Peli;
import keimo.Peli.Ruudut;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.ikkuna.Window;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class VirheRuutu {
    private static Shader tarinaRuutuShader = new Shader("shader");
    private static Teksti otsikkoTeksti = new Teksti("Paskan möivät", Color.white, 300, 30);
    private static Teksti tekstiTexture = new Teksti("Virheteksti", Color.WHITE, 1000, 300);
    private static Teksti jatkaNappiTexture = new Teksti("F1: Käynnistä uudelleen    Esc: sulje", Color.WHITE, 500, 30);

    private static String virheteksti = "";

    public static void siirryVirheruutuun(String virheviesti) {
        virheteksti = virheviesti;
        Peli.aktiivinenRuutu = Ruudut.VIRHERUUTU;
    }

    public static void render(Window window) {
        tarinaRuutuShader.bind();
        tarinaRuutuShader.setUniform("sampler", 0);
        tarinaRuutuShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        float scaleX = 1;
        if (window.getWidth() > 0 && window.getHeight() > 0) scaleX = window.getWidth()/ (window.getWidth()*2/window.getHeight());
        float scaleYKuva = window.getHeight()/12;
        float scaleYTeksti = window.getHeight()/4;
        float scaleYJatkaNappi = window.getHeight()/12;

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(0, scaleYKuva*4, 0);
        matOtsikko.scale(scaleX, scaleYKuva, 0);
        tarinaRuutuShader.setUniform("projection", matOtsikko);
        otsikkoTeksti.bind(0);
        Assets.getModel().render();

        Matrix4f matTeksti = new Matrix4f();
        window.getView().scale(1, matTeksti);
        matTeksti.translate(0, 0, 0);
        matTeksti.scale(scaleX, scaleYTeksti, 0);
        tarinaRuutuShader.setUniform("projection", matTeksti);
        tekstiTexture.bind(0);
        Assets.getModel().render();
        tekstiTexture.päivitäTeksti(virheteksti);

        Matrix4f matJatkaNappi = new Matrix4f();
        window.getView().scale(1, matJatkaNappi);
        matJatkaNappi.translate(0, -scaleYJatkaNappi*5, 0);
        matJatkaNappi.scale(scaleX, scaleYJatkaNappi, 0);
        tarinaRuutuShader.setUniform("projection", matJatkaNappi);
        jatkaNappiTexture.bind(0);
        Assets.getModel().render();
    }
}
