package keimo.keimoEngine.gui.hud;

import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.ikkuna.Window;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class TavoitePopup {
    private static Teksti tavoiteTeksti = new Teksti("tavoite", Color.black, 400, 20);
    private static Shader shader = new Shader("shader");
    private static Tekstuuri tavoiteKehysTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/tavoitepopup.png");
    
    public static String suoritettuTavoite = "";
    public static int popupAjastin = 0;

    public static void renderöiTavoitePopup(Window window) {
        if (popupAjastin > 0) {
            float scaleXPohja = window.getWidth()/4;
            float scaleYPohja = window.getHeight()/16;
            Matrix4f matDialogiPohja = new Matrix4f();
            window.getView().scale(1, matDialogiPohja);
            matDialogiPohja.translate(0, window.getHeight()/2 -4*scaleYPohja, 0);
            matDialogiPohja.scale(scaleXPohja, scaleYPohja, 0);

            shader.bind();
            shader.setUniform("sampler", 0);
            shader.setUniform("projection", matDialogiPohja);
            if (popupAjastin < 50) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 1f - popupAjastin/50f));
            else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
            tavoiteKehysTekstuuri.bind(0);
            Assets.getModel().render();

            float scaleXTeksti = scaleXPohja;
            float scaleYTeksti = scaleYPohja * (2.5f/4f);
            float offsetXTeksti = 0;
            float offsetYTeksti = scaleYPohja * (2f/4f);

            Matrix4f matTavoiteTeksti = new Matrix4f();
            window.getView().scale(1, matTavoiteTeksti);
            matTavoiteTeksti.translate(offsetXTeksti, window.getHeight()/2 -4*scaleYPohja - offsetYTeksti, 0);
            matTavoiteTeksti.scale(scaleXTeksti, scaleYTeksti, 0);
            
            shader.setUniform("projection", matTavoiteTeksti);
            if (popupAjastin < 50) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 1f - popupAjastin/50f));
            else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
            tavoiteTeksti.päivitäTeksti(suoritettuTavoite, false, 36, Color.black);
            tavoiteTeksti.bind(0);
            Assets.getModel().render();

            popupAjastin--;
        }
    }
}

