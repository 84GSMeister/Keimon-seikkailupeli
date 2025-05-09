package keimo.keimoEngine.menu.asetusRuudut;

import keimo.Peli;
import keimo.Peli.SyötteenTila;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Animaatio;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.äänet.Äänet;

import java.awt.Color;

import org.joml.Matrix4f;

public class AsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 4;
    private static Shader asetusRuutuShader = new Shader("staattinen");
    private static Tekstuuri otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_asetukset.png");
    private static Animaatio osoitinKuvake = new Animaatio(30, "tiedostot/kuvat/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");

    private static Teksti asetus1Teksti = new Teksti("Grafiikka", Color.white, 200, 16);
    private static Teksti asetus2Teksti = new Teksti("Ääni", Color.white, 200, 16);
    private static Teksti asetus3Teksti = new Teksti("Peli", Color.white, 200, 16);
    private static Tekstuuri hyväksyTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/asetukset_takaisin.png");
    
    public static boolean pelissä = false;

    public static void painaNäppäintä(String näppäin) {
        switch (näppäin) {
            case "ylös" -> {
                valinta--;
                if (valinta < 0) {
                    valinta = asetustenMäärä-1;
                }
            }
            case "alas" -> {
                valinta++;
                if (valinta > asetustenMäärä-1) {
                    valinta = 0;
                }
            }
            case "enter" -> {
                valitse(valinta);
            }
        }
        Äänet.toistaSFX("Valinta");
    }

    static void valitse(int valinta) {

        switch (valinta) {
            case 0: // Grafiikka
                KeimoEngine.valitseAktiivinenRuutu("asetusruutu_grafiikka");
                break;
            case 1: // Ääni
                KeimoEngine.valitseAktiivinenRuutu("asetusruutu_ääni");
                break;
            case 2: // Peli
                KeimoEngine.valitseAktiivinenRuutu("asetusruutu_peli");
                break;
            case 3: // Takaisin
                hyväksy();
                if (pelissä) {
                    Peli.syötteenTila = SyötteenTila.TOIMINTO;
                    KeimoEngine.valitseAktiivinenRuutu("peliruutu");
                    Peli.pause = true;
                }
                else KeimoEngine.valitseAktiivinenRuutu("valikkoruutu");
                break;
            default:
                break;
        }
    }

    private static void hyväksy() {

    }

    public static void render(Window window) {
        asetusRuutuShader.bind();
        float scaleXOtsikko = 1;
        if (window.getWidth() != 0 && window.getWidth() != 0) {
            scaleXOtsikko = window.getWidth()/ (window.getWidth()*2/window.getHeight());
        }
        float scaleYOtsikko = window.getHeight()/16;
        float scaleXValinnat = window.getWidth()/4;
        float scaleYValinnat = window.getHeight()/30;
        float scaleXOsoitin = window.getHeight()/20;
        float scaleYOsoitin = scaleYValinnat;
        float offsetYValinta = window.getHeight()/15;

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(0, scaleYOtsikko*6f, 0);
        matOtsikko.scale(scaleXOtsikko, scaleYOtsikko, 0);
        asetusRuutuShader.setUniform("projection", matOtsikko);
        otsikkoTekstuuri.bind(0);
        Assets.getModel().render();

        for (int i = 0; i < asetustenMäärä; i++) {
            Matrix4f matOsoitin = new Matrix4f();
            window.getView().scale(1, matOsoitin);
            if (i == asetustenMäärä-1) matOsoitin.translate(-scaleXValinnat, scaleYOtsikko*4f -1.2f*i*offsetYValinta, 0);
            else matOsoitin.translate(-scaleXValinnat, scaleYOtsikko*4f -i*offsetYValinta, 0);
            matOsoitin.scale(scaleXOsoitin, scaleYOsoitin, 0);
            asetusRuutuShader.setUniform("projection", matOsoitin);
            if (valinta == i) osoitinKuvake.bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();
        }

        for (int i = 0; i < asetustenMäärä; i++) {
            Matrix4f matValinta = new Matrix4f();
            window.getView().scale(1, matValinta);
            if (i == asetustenMäärä-1) matValinta.translate(scaleXOsoitin, scaleYOtsikko*4f -1.2f*i*offsetYValinta, 0);
            else matValinta.translate(scaleXOsoitin, scaleYOtsikko*4f -i*offsetYValinta, 0);
            matValinta.scale(scaleXValinnat, scaleYValinnat, 0);
            asetusRuutuShader.setUniform("projection", matValinta);
            switch (i) {
                case 0: asetus1Teksti.bind(0); break;
                case 1: asetus2Teksti.bind(0); break;
                case 2: asetus3Teksti.bind(0); break;
                case 3: hyväksyTekstuuri.bind(0); break;
            }
            Assets.getModel().render();
        }
    }   
}
