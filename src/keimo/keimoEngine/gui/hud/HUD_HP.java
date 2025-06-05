package keimo.keimoEngine.gui.hud;

import keimo.Pelaaja;
import keimo.Utility.KeimoFontit;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.ikkuna.Window;

import java.awt.Color;
import java.text.DecimalFormat;

import org.joml.Matrix4f;

public class HUD_HP {
    private static Tekstuuri hpTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/hp.png");
    private static Tekstuuri juomatTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/juomat.png");
    private static Teksti hpTeksti = new Teksti("HP", Color.black, 70, 40, KeimoFontit.fontti_keimo_20);
    private static Teksti juomatTeksti = new Teksti("Juomat", Color.black, 100, 40, KeimoFontit.fontti_keimo_20);
    private static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

    public static void render(Shader shader, Window window) {
        shader.bind();
        shader.setUniform("sampler", 0);

        float scaleXUlopmi = window.getWidth()/12;
        float scaleXSisempi = window.getWidth()/15;
        float scaleYUlompi = window.getHeight()/6;
        float scaleYSisempi = window.getHeight()/7;

        Matrix4f matHpKuvake = new Matrix4f();
        window.getView().scale(1, matHpKuvake);
        matHpKuvake.translate(-window.getWidth()/2 +scaleXUlopmi -scaleXSisempi/2f, window.getHeight()/2 - scaleYUlompi + scaleYSisempi/2f, 0);
        matHpKuvake.scale(scaleXSisempi/2, scaleYSisempi/2, 0);
        shader.setUniform("projection", matHpKuvake);
        hpTekstuuri.bind(0);
        //Assets.getModel().render();

        Matrix4f matHpTeksti = new Matrix4f();
        window.getView().scale(1, matHpTeksti);
        matHpTeksti.translate(-window.getWidth()/2 +scaleXUlopmi +scaleXSisempi/2f, window.getHeight()/2 - scaleYUlompi + scaleYSisempi/2f, 0);
        matHpTeksti.scale(scaleXSisempi/2, scaleYSisempi/2, 0);
        shader.setUniform("projection", matHpTeksti);
        hpTeksti.päivitäTeksti("" + Pelaaja.hp, false, 50, Color.black, 0, 15);
        hpTeksti.bind(0);
        //Assets.getModel().render();

        Matrix4f matJuomatKuvake = new Matrix4f();
        window.getView().scale(1, matJuomatKuvake);
        matJuomatKuvake.translate(-window.getWidth()/2 +scaleXUlopmi -scaleXSisempi/2f, window.getHeight()/2 - scaleYUlompi - scaleYSisempi/2f, 0);
        matJuomatKuvake.scale(scaleXSisempi/2, scaleYSisempi/2, 0);
        shader.setUniform("projection", matJuomatKuvake);
        juomatTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matJuomatTeksti = new Matrix4f();
        window.getView().scale(1, matJuomatTeksti);
        matJuomatTeksti.translate(-window.getWidth()/2 +scaleXUlopmi +scaleXSisempi/2f, window.getHeight()/2 - scaleYUlompi - scaleYSisempi/2f, 0);
        matJuomatTeksti.scale(scaleXSisempi/2, scaleYSisempi/2, 0);
        shader.setUniform("projection", matJuomatTeksti);
        juomatTeksti.päivitäTeksti(kaksiDesimaalia.format(Pelaaja.känninVoimakkuusFloat*(1.5f/4f)) + "‰", false, 50, Color.black, 0, 15);
        juomatTeksti.bind(0);
        Assets.getModel().render();
    }
}
