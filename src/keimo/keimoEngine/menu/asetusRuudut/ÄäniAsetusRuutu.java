package keimo.keimoEngine.menu.asetusRuudut;

import keimo.PelinAsetukset;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Animaatio;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.ikkuna.Window;

import java.awt.Color;
import java.text.DecimalFormat;

import org.joml.Matrix4f;

public class ÄäniAsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 6;
    private static Shader asetusRuutuShader = new Shader("staattinen");
    private static Tekstuuri otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_asetukset.png");
    private static Animaatio osoitinKuvake = new Animaatio(30, "tiedostot/kuvat/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");

    private static Teksti asetusMusaTeksti = new Teksti("Musiikki", Color.white, 200, 16);
    private static Teksti asetusMusanVoimakkuusTeksti = new Teksti("Musiikin voim.", Color.white, 200, 16);
    private static Teksti asetusÄänetTeksti = new Teksti("Äänet (SFX)", Color.white, 200, 16);
    private static Teksti asetusÄäntenVoimakkuusTeksti = new Teksti("Äänten voim.", Color.white, 200, 16);
    private static Teksti asetusÄänitestiTeksti = new Teksti("Äänitesti", Color.white, 200, 16);
    private static Tekstuuri hyväksyTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/asetukset_takaisin.png");

    private static Teksti tilaMusaTeksti = new Teksti("Päällä", Color.white, 200, 16);
    private static Teksti tilaMusanVoimakkuusTeksti = new Teksti("50", Color.white, 200, 16);
    private static Teksti tilaÄänetTeksti = new Teksti("Päällä", Color.white, 200, 16);
    private static Teksti tilaÄäntenVoimakkuusTeksti = new Teksti("50", Color.white, 200, 16);

    private static boolean musiikkiPäällä = true;
    private static float musanVoimakkuus = 0.5f;
    private static boolean äänetPäällä = true;
    private static float ääntenVoimakkuus = 0.5f;
    private static DecimalFormat nollaDesimaalia = new DecimalFormat("##.");

    private static Teksti infoTeksti = new Teksti("info", Color.white, 700, 100);
    private static String infoTekstiMusa = "Musat\n" + 
    "Musiikin voimakkuuden muutoksessa voi kestää hetki riippuen puskurin koosta.";
    private static String infoTekstiÄänet = "Äänet (SFX)\n" +
    "Vaikuttaa kaikkiin pelin ääniin.";

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
            case "vasen" -> {
                säädäAsetusta(valinta, false);
                päivitäAsetukset();
            }
            case "oikea" -> {
                säädäAsetusta(valinta, true);
                päivitäAsetukset();
            }
            case "enter" -> {
                hyväksy(valinta);
            }
        }
        ÄänentoistamisSäie.toistaSFX("Valinta");
    }

    static void säädäAsetusta(int valinta, boolean kasvata) {
        switch (valinta) {
            case 0: // Musiikki päällä
                musiikkiPäällä = !musiikkiPäällä;
            break;
            case 1: // Musiikin voimakkuus
                if (kasvata) {
                    if (musanVoimakkuus < 0.995f) musanVoimakkuus += 0.01f;
                }
                else {
                    if (musanVoimakkuus > 0.005f) musanVoimakkuus -= 0.01f;
                }
            break;
            case 2: // Äänet päällä
                äänetPäällä = !äänetPäällä;
            break;
            case 3: // Ääniefektien voimakkuus
                if (kasvata) {
                    if (ääntenVoimakkuus < 0.995f) ääntenVoimakkuus += 0.01f;
                }
                else {
                    if (ääntenVoimakkuus > 0.005f) ääntenVoimakkuus -= 0.01f;
                }
            break;
            case 4: // Avaa Äänitesti
                //KeimoEngine.valitseAktiivinenRuutu("asetusruutu");
            break;
            case 5: // Hyväksy
                //KeimoEngine.valitseAktiivinenRuutu("asetusruutu");
            break;
            default:
            break;
        }
    }

    static void päivitäAsetukset() {
        PelinAsetukset.musiikkiPäällä = musiikkiPäällä;
        PelinAsetukset.musaVolyymi = musanVoimakkuus;
        PelinAsetukset.äänetPäällä = äänetPäällä;
        PelinAsetukset.ääniVolyymi = ääntenVoimakkuus;
        ÄänentoistamisSäie.asetaMusanVolyymi(musiikkiPäällä ? musanVoimakkuus : 0);
        ÄänentoistamisSäie.asetaSFXVolyymi(äänetPäällä ? ääntenVoimakkuus : 0);
    }

    static void hyväksy(int valinta) {
        if (valinta == 4) {
            ÄäniTestiRuutu.listaaÄänet();
            ÄänentoistamisSäie.suljeMusa();
            KeimoEngine.valitseAktiivinenRuutu("asetusruutu_äänitesti");
        }
        else if (valinta == 5) {
            KeimoEngine.valitseAktiivinenRuutu("asetusruutu");
        }
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
        float scaleXInfo = scaleXOtsikko;
        float scaleYInfo = window.getHeight()/8;
        float keskitysX = window.getWidth()/4;
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
            if (i == asetustenMäärä-1) matOsoitin.translate(-scaleXOsoitin - keskitysX*1.5f, scaleYOtsikko*4f -1.2f*i*offsetYValinta, 0);
            else matOsoitin.translate(-scaleXOsoitin - keskitysX*1.5f, scaleYOtsikko*4f -i*offsetYValinta, 0);
            matOsoitin.scale(scaleXOsoitin, scaleYOsoitin, 0);
            asetusRuutuShader.setUniform("projection", matOsoitin);
            if (valinta == i) osoitinKuvake.bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();
        }

        for (int i = 0; i < asetustenMäärä; i++) {
            Matrix4f matValinta = new Matrix4f();
            window.getView().scale(1, matValinta);
            if (i == asetustenMäärä-1) matValinta.translate(scaleXValinnat - keskitysX*1.5f, scaleYOtsikko*4f -1.2f*i*offsetYValinta, 0);
            else matValinta.translate(scaleXValinnat - keskitysX*1.5f, scaleYOtsikko*4f -i*offsetYValinta, 0);
            matValinta.scale(scaleXValinnat, scaleYValinnat, 0);
            asetusRuutuShader.setUniform("projection", matValinta);
            switch (i) {
                case 0: asetusMusaTeksti.bind(0); break;
                case 1: asetusMusanVoimakkuusTeksti.bind(0); break;
                case 2: asetusÄänetTeksti.bind(0); break;
                case 3: asetusÄäntenVoimakkuusTeksti.bind(0); break;
                case 4: asetusÄänitestiTeksti.bind(0); break;
                case 5: hyväksyTekstuuri.bind(0); break;
            }
            Assets.getModel().render();
        }

        tilaMusaTeksti.päivitäTeksti(musiikkiPäällä ? "Päällä" : "Pois");
        tilaMusanVoimakkuusTeksti.päivitäTeksti("" + nollaDesimaalia.format(musanVoimakkuus*100f));
        tilaÄänetTeksti.päivitäTeksti(äänetPäällä ? "Päällä" : "Pois");
        tilaÄäntenVoimakkuusTeksti.päivitäTeksti("" + nollaDesimaalia.format(ääntenVoimakkuus*100f));
        for (int i = 0; i < asetustenMäärä; i++) {
            Matrix4f matStatus = new Matrix4f();
            window.getView().scale(1, matStatus);
            matStatus.translate(scaleXValinnat + keskitysX, scaleYOtsikko*4f -i*offsetYValinta, 0);
            matStatus.scale(scaleXValinnat, scaleYValinnat, 0);
            asetusRuutuShader.setUniform("projection", matStatus);
            switch (i) {
                case 0: tilaMusaTeksti.bind(0); break;
                case 1: tilaMusanVoimakkuusTeksti.bind(0); break;
                case 2: tilaÄänetTeksti.bind(0); break;
                case 3: tilaÄäntenVoimakkuusTeksti.bind(0); break;
                default: tyhjäTekstuuri.bind(0); break;
            }
            Assets.getModel().render();
        }

        Matrix4f matInfoTeksti = new Matrix4f();
        window.getView().scale(1, matInfoTeksti);
        matInfoTeksti.translate(0, -window.getHeight()/2+scaleYInfo, 0);
        matInfoTeksti.scale(scaleXInfo, scaleYInfo, 0);
        asetusRuutuShader.setUniform("projection", matInfoTeksti);
        switch (valinta) {
            case 0: infoTeksti.päivitäTeksti(infoTekstiMusa, false, 58); break;
            case 1: infoTeksti.päivitäTeksti(infoTekstiMusa); break;
            case 2: infoTeksti.päivitäTeksti(infoTekstiÄänet); break;
            case 3: infoTeksti.päivitäTeksti(infoTekstiÄänet); break;
            default: infoTeksti.päivitäTeksti(""); break;
        }
        infoTeksti.bind(0);
        Assets.getModel().render();
    } 
}
