package keimo.seikkailupeli.gui.hud;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;

import java.text.DecimalFormat;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class OstosLista {

    private static Teksti otsikkoTeksti = new Teksti("Ostoskori", Väri.black, 360, 96);
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("tyhjä");
    private static Teksti esineenNimiTeksti = new Teksti("Esine", Väri.black, 480, 96);
    private static Teksti hintaTeksti = new Teksti("Yht.", Väri.black, 450, 96);
    private static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
    private static Shader shader1 = new Shader("shader");

    public static void render(Shader shader, Ikkuna window) {
        shader1.bind();
        shader1.nollaaShaderEfektit();
        shader1.setUniform("subcolor", new Vector4f(0, 0, 0, 0));
        shader1.setUniform("color", new Vector4f(0, 0, 0, 1));

        float scaleXOtsikko = window.getWidth()/14f;
        float scaleYOtsikko = window.getHeight()/24f;
        float scaleXKuvake = window.getWidth()/60f;
        float scaleXTeksti = window.getWidth()/25f;
        float scaleY = window.getHeight()/40f;
        float keskitysX = window.getWidth()/12f;
        float keskitysY = window.getHeight()/15f;
        float offsetY = window.getHeight()/40f;

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(window.getWidth()/2-scaleXOtsikko, keskitysY + 1*offsetY, 0);
        matOtsikko.scale(scaleXOtsikko, scaleYOtsikko, 0);
        shader1.asetaSijainti(matOtsikko);
        otsikkoTeksti.bind(0);
        Assets.getModel().render();

        for (int i = 0; i < Pelaaja.ostosKori.size(); i++) {
            Matrix4f matKuvake = new Matrix4f();
            window.getView().scale(1, matKuvake);
            matKuvake.translate(window.getWidth()/2 - scaleXTeksti - keskitysX, keskitysY - i*offsetY, 0);
            matKuvake.scale(scaleXKuvake, scaleY, 0);
            shader1.asetaSijainti(matKuvake);
            if (Pelaaja.ostosKori.get(i) != null) Pelaaja.ostosKori.get(i).annaDialogiTekstuuri().bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();
        }

        for (int i = 0; i < Pelaaja.ostosKori.size(); i++) {
            Matrix4f matNimi = new Matrix4f();
            window.getView().scale(1, matNimi);
            matNimi.translate(window.getWidth()/2 + scaleXKuvake - keskitysX, keskitysY - i*offsetY, 0);
            matNimi.scale(scaleXTeksti, scaleY, 0);
            shader1.asetaSijainti(matNimi);
            if (Pelaaja.ostosKori.get(i) != null) {
                esineenNimiTeksti.päivitäTeksti(Pelaaja.ostosKori.get(i).annaNimi());
                esineenNimiTeksti.bind(0);
            }
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();
        }

        Matrix4f matHinta = new Matrix4f();
        window.getView().scale(1, matHinta);
        matHinta.translate(window.getWidth()/2-scaleXOtsikko, keskitysY - 8*offsetY, 0);
        matHinta.scale(scaleXOtsikko, scaleYOtsikko, 0);
        shader1.asetaSijainti(matHinta);
        hintaTeksti.päivitäTeksti("Yht. " + kaksiDesimaalia.format(Pelaaja.ostostenHintaYhteensä) + "€");
        hintaTeksti.bind(0);
        Assets.getModel().render();
    }
}
