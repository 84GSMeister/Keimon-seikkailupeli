package keimo.keimoEngine.gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.ikkuna.Window;

public class LatausRuutu {

    private static Shader shader = new Shader("staattinen");
    private static Tekstuuri latausRuudunTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/lataus/latausruutu.png");
    
    public static void renderöiLatausRuutu(Window window) {
        float scaleX = window.getWidth()/2;
        float scaleY = window.getHeight()/2;
        shader.bind();
        shader.setUniform("sampler", 0);
        shader.setUniform("color", new Vector4f(0f, 0f, 0f, 0f));

        Matrix4f matLatausRuutu = new Matrix4f();
        window.getView().scale(1, matLatausRuutu);
        matLatausRuutu.scale(scaleX, scaleY, 0);
        shader.setUniform("projection", matLatausRuutu);
        latausRuudunTekstuuri.bind(0);
        Assets.getModel().render();
    }
}
