package keimo.keimoEngine.gui.hud;

import keimo.Pelaaja;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.ikkuna.Window;

import java.awt.Color;
import java.text.DecimalFormat;

import org.joml.Matrix4f;

public class OstosLista {

    private static Teksti otsikkoTeksti = new Teksti("Ostoskori", Color.black, 120, 50);
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");
    private static Teksti esineenNimiTeksti = new Teksti("Esine", Color.black, 150, 20);
    private static Teksti hintaTeksti = new Teksti("Yht.", Color.black, 150, 50);
    private static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

    public static void render(Shader shader, Window window) {
        shader.bind();
        shader.setUniform("sampler", 0);

        float scaleXOtsikko = window.getWidth()/12;
        float scaleYOtsikko = window.getHeight()/24;
        float scaleXKuvake = window.getWidth()/60;
        float scaleXTeksti = window.getWidth()/30;
        float scaleY = window.getHeight()/30;
        float keskitysX = window.getWidth()/24;
        float offsetY = window.getHeight()/32;

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(window.getWidth()/2-scaleXOtsikko, window.getHeight()/30 + 2*offsetY, 0);
        matOtsikko.scale(scaleXOtsikko, scaleYOtsikko, 0);
        shader.setUniform("projection", matOtsikko);
        otsikkoTeksti.bind(0);
        Assets.getModel().render();

        for (int i = 0; i < Pelaaja.ostosKori.size(); i++) {
            Matrix4f matKuvake = new Matrix4f();
            window.getView().scale(1, matKuvake);
            matKuvake.translate(window.getWidth()/2 - 2*scaleXTeksti - scaleXKuvake - keskitysX, window.getHeight()/12 - i*offsetY, 0);
            matKuvake.scale(scaleXKuvake, scaleY, 0);
            shader.setUniform("projection", matKuvake);
            if (Pelaaja.ostosKori.get(i) != null) Pelaaja.ostosKori.get(i).annaDialogiTekstuuri().bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();
        }

        for (int i = 0; i < Pelaaja.ostosKori.size(); i++) {
            Matrix4f matNimi = new Matrix4f();
            window.getView().scale(1, matNimi);
            matNimi.translate(window.getWidth()/2-scaleXTeksti - keskitysX, window.getHeight()/12 - i*offsetY - 10, 0);
            matNimi.scale(scaleXTeksti, scaleY, 0);
            shader.setUniform("projection", matNimi);
            if (Pelaaja.ostosKori.get(i) != null) {
                esineenNimiTeksti.päivitäTeksti(Pelaaja.ostosKori.get(i).annaNimi());
                esineenNimiTeksti.bind(0);
            }
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();
        }

        Matrix4f matHinta = new Matrix4f();
        window.getView().scale(1, matHinta);
        matHinta.translate(window.getWidth()/2-scaleXOtsikko, -window.getHeight()/10 - 2*offsetY, 0);
        matHinta.scale(scaleXOtsikko, scaleYOtsikko, 0);
        shader.setUniform("projection", matHinta);
        hintaTeksti.päivitäTeksti("Yht. " + kaksiDesimaalia.format(Pelaaja.ostostenHintaYhteensä) + "e.");
        hintaTeksti.bind(0);
        Assets.getModel().render();
    }
}
