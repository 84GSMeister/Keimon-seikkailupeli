package keimo.keimoEngine.gui.hud;

import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.ikkuna.Window;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class TavoitePopup {
    private static Teksti tavoiteInfoTeksti = new Teksti("tavoite", Color.black, 400, 20);
    private static Teksti tavoiteTeksti = new Teksti("tavoite", Color.black, 400, 20);
    private static Shader shaderStaattinen = new Shader("staattinen");
    private static Shader shader = new Shader("shader");

    private static Tekstuuri tavoiteKehysTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/dialogi_teksti_kehys.png");
    private static Tekstuuri otsikkoKehysTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/dialogi_nimi_kehys.png");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");
    
    public static String suoritettuTavoite = "";
    public static int popupAjastin = 0;

    public static void renderöiTavoitePopup(Window window) {
        if (popupAjastin > 0) {
            shaderStaattinen.bind();
            shaderStaattinen.setUniform("sampler", 0);
            
            float scaleXPohja = window.getWidth()/4;
            float scaleYPohja = window.getHeight()/16;

            Matrix4f matDialogiPohja = new Matrix4f();
            window.getView().scale(1, matDialogiPohja);
            matDialogiPohja.translate(0, window.getHeight()/2 -4*scaleYPohja, 0);
            matDialogiPohja.scale(scaleXPohja, scaleYPohja, 0);
            shaderStaattinen.setUniform("projection", matDialogiPohja);
            if (popupAjastin < 50) shaderStaattinen.setUniform("color", new Vector4f(85, 79, 67, 0.7f * popupAjastin/50f));
            else shaderStaattinen.setUniform("color", new Vector4f(85, 79, 67, 0.7f));
            tyhjäTekstuuri.bind(0);
            Assets.getModel().render();

            shader.bind();
            shader.setUniform("sampler", 0);
            if (popupAjastin < 50) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 1- popupAjastin/50f));
            else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0));

            float scaleXTeksti = scaleXPohja;
            float scaleYTeksti = scaleYPohja * (2.5f/4f);
            float offsetXTeksti = 0;
            float offsetYTeksti = scaleYPohja * (1.5f/4f);

            Matrix4f matDialogiTeksti = new Matrix4f();
            window.getView().scale(1, matDialogiTeksti);
            matDialogiTeksti.translate(offsetXTeksti, window.getHeight()/2 -4*scaleYPohja - offsetYTeksti, 0);
            matDialogiTeksti.scale(scaleXTeksti, scaleYTeksti, 0);
            shader.setUniform("projection", matDialogiTeksti);
            tavoiteKehysTekstuuri.bind(0);
            Assets.getModel().render();
            tavoiteTeksti.päivitäTeksti(suoritettuTavoite, false, 36, Color.black);
            tavoiteTeksti.bind(0);
            Assets.getModel().render();

            float scaleXOtsikko = scaleXPohja;
            float scaleYOtsikko = scaleYPohja * (1.5f/4f);
            float offsetXOtiskko = 0;
            float offsetYOtsikko = scaleYPohja * (2.5f/4f);

            Matrix4f matOtsikko = new Matrix4f();
            window.getView().scale(1, matOtsikko);
            matOtsikko.translate(offsetXOtiskko, window.getHeight()/2 -4*scaleYPohja + offsetYOtsikko, 0);
            matOtsikko.scale(scaleXOtsikko, scaleYOtsikko, 0);
            shader.setUniform("projection", matOtsikko);
            otsikkoKehysTekstuuri.bind(0);
            Assets.getModel().render();
            tavoiteInfoTeksti.päivitäTeksti("Tavoite suoritettu:", false, 36, Color.black);
            tavoiteInfoTeksti.bind(0);
            Assets.getModel().render();

            popupAjastin--;
        }
    }
}

