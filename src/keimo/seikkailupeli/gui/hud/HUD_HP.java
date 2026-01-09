package keimo.seikkailupeli.gui.hud;

import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;

import java.awt.Color;
import java.text.DecimalFormat;

import org.joml.Matrix4f;

public class HUD_HP {
    private static Tekstuuri hpTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/hp_eitekstiä.png");
    private static Tekstuuri juomatTekstuuri = new Tekstuuri("tiedostot/kuvat/hud/juomat.png");
    private static Teksti hpTeksti = new Teksti("HP", Color.black, 100, 48, KeimoFontit.fontti_keimo_36, true);
    private static Teksti juomatTeksti = new Teksti("Juomat", Color.black, 200, 48, KeimoFontit.fontti_keimo_36, true);
    private static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        shader.setUniform("sampler", 0);

        float scaleXUlopmi = window.getWidth()/12;
        float scaleXSisempi = window.getWidth()/15;
        float scaleYUlompi = window.getHeight()/6;
        float scaleYSisempi = window.getHeight()/7;

        Matrix4f matHpKuvake = new Matrix4f();
        window.getView().scale(1, matHpKuvake);
        matHpKuvake.translate(-window.getWidth()/2 +scaleXUlopmi -scaleXSisempi/2f, window.getHeight()/2 - scaleYUlompi + scaleYSisempi/2f, 0);
        matHpKuvake.scale(scaleXSisempi/4, scaleYSisempi/4, 0);
        shader.setUniform("projection", matHpKuvake);
        hpTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matHpTeksti = new Matrix4f();
        window.getView().scale(1, matHpTeksti);
        matHpTeksti.translate(-window.getWidth()/2 +scaleXUlopmi +scaleXSisempi/3f, window.getHeight()/2 - scaleYUlompi + scaleYSisempi/2f, 0);
        matHpTeksti.scale(scaleXSisempi/2, scaleYSisempi/4, 0);
        shader.setUniform("projection", matHpTeksti);
        hpTeksti.päivitäTeksti("" + Pelaaja.hp, 0, 50, Color.black);
        hpTeksti.bind(0);
        Assets.getModel().render();

        Matrix4f matJuomatKuvake = new Matrix4f();
        window.getView().scale(1, matJuomatKuvake);
        matJuomatKuvake.translate(-window.getWidth()/2 +scaleXUlopmi -scaleXSisempi/2f, window.getHeight()/2 - scaleYUlompi - scaleYSisempi/2f, 0);
        matJuomatKuvake.scale(scaleXSisempi/4, scaleYSisempi/4, 0);
        shader.setUniform("projection", matJuomatKuvake);
        juomatTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matJuomatTeksti = new Matrix4f();
        window.getView().scale(1, matJuomatTeksti);
        matJuomatTeksti.translate(-window.getWidth()/2 +scaleXUlopmi +scaleXSisempi/3f, window.getHeight()/2 - scaleYUlompi - scaleYSisempi/2f, 0);
        matJuomatTeksti.scale(scaleXSisempi/2, scaleYSisempi/4, 0);
        shader.setUniform("projection", matJuomatTeksti);
        juomatTeksti.päivitäTeksti(kaksiDesimaalia.format(Pelaaja.känninVoimakkuusFloat*(1.5f/4f)) + "‰", 0, 50, Color.black);
        juomatTeksti.bind(0);
        Assets.getModel().render();
    }
}
