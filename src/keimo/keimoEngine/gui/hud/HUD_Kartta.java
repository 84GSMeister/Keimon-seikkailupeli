package keimo.keimoEngine.gui.hud;

import keimo.Peli;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.ikkuna.Window;

import java.awt.Color;

import org.joml.Matrix4f;

public class HUD_Kartta {
    private static Teksti alueTeksti = new Teksti("Alue", Color.black, 120, 50);
    private static Tekstuuri karttaTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/kartat/kartta_pohja.png");
    private static Teksti huoneTeksti = new Teksti("Yht.", Color.black, 200, 50);

    public static void render(Shader shader, Window window) {
        shader.bind();
        shader.setUniform("sampler", 0);

        float scaleXUlopmi = window.getWidth()/12;
        float scaleXSisempi = window.getWidth()/15;
        float scaleYOtsikko = window.getHeight()/24;
        float scaleYKartta = window.getHeight()/12;
        float keskitysYKartta = window.getHeight()/6;
        float keskitysYTekstit = window.getHeight()/12;

        Matrix4f matAlue = new Matrix4f();
        window.getView().scale(1, matAlue);
        matAlue.translate(window.getWidth()/2-scaleXUlopmi, window.getHeight()/2 - keskitysYKartta + keskitysYTekstit, 0);
        matAlue.scale(scaleXSisempi, scaleYOtsikko, 0);
        shader.setUniform("projection", matAlue);
        alueTeksti.päivitäTeksti(Peli.huone.annaAlue());
        alueTeksti.bind(0);
        Assets.getModel().render();

        Matrix4f matKartta = new Matrix4f();
        window.getView().scale(1, matKartta);
        matKartta.translate(window.getWidth()/2-scaleXUlopmi, window.getHeight()/2 -keskitysYKartta, 0);
        matKartta.scale(scaleXSisempi, scaleYKartta, 0);
        shader.setUniform("projection", matKartta);
        karttaTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matHuone = new Matrix4f();
        window.getView().scale(1, matHuone);
        matHuone.translate(window.getWidth()/2-scaleXUlopmi, window.getHeight()/2 - keskitysYKartta - 1.75f*keskitysYTekstit, 0);
        matHuone.scale(scaleXSisempi, scaleYOtsikko, 0);
        shader.setUniform("projection", matHuone);
        huoneTeksti.päivitäTeksti(Peli.huone.annaNimi());
        huoneTeksti.bind(0);
        Assets.getModel().render();
    }
}
