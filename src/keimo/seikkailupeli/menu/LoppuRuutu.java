package keimo.seikkailupeli.menu;

import keimo.TarkistettavatArvot;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.ikkuna.Window;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class LoppuRuutu {

    private static int valinta = 0;
    private static int vaihtoehtojenMäärä = 2;
    private static int kelausViive = 0;
    private static Shader loppuRuutuShader = new Shader("staattinen");
    private static Renderöitävä otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/vakio_otsikko.png");
    private static Renderöitävä kuvaTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/voitto_normaali.jpg");
    private static Teksti tekstiTexture = new Teksti("Tarinan teksti 1", Color.WHITE, 2200, 400, KeimoFontit.fontti_keimo_100, false);
    private static Teksti tilastotTeksti = new Teksti("Tilastot:", Color.WHITE, 4400, 400, KeimoFontit.fontti_keimo_100, false);
    private static Teksti tappolaskuriTeksti = new Teksti("Vihollisia tapettu: 0", Color.WHITE, 4400, 400, KeimoFontit.fontti_keimo_100, false);
    private static String häviönSyyTeksti = "Häviön syy";
    private static Tekstuuri valintaUusiPeliTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_uusipeli.png");
    private static Tekstuuri valintaLopetaTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_lopeta.png");
    private static Animaatio osoitinKuvake = new Animaatio("tiedostot/kuvat/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");

    public static void painaNäppäintä(String näppäin) {
        if (kelausViive <= 0) {
            switch (näppäin) {
                case "ylös" -> {
                    valinta--;
                    if (valinta < 0) {
                        valinta = vaihtoehtojenMäärä-1;
                    }
                }
                case "alas" -> {
                    valinta++;
                    if (valinta > vaihtoehtojenMäärä-1) {
                        valinta = 0;
                    }
                }
                case "enter" -> {
                    hyväksy(valinta);
                }
            }
            Äänet.toistaSFX("Valinta");
        }
    }

    static void hyväksy(int valinta) {

        if (kelausViive <= 0) {
            switch (valinta) {
                case 0: // Uusi peli
                    jatka();
                    break;
                case 1: // Lopeta
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }

    public static void lataaLopetus(PelinLopetukset pelinLopetus) {
        kelausViive = 120;
        switch (pelinLopetus) {
            case ALKOHOLIMYRKYTYS -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_ylensyönti.gif");
                häviönSyyTeksti = "Joit liikaa ja sinulle\ntuli alkoholimyrkytys.";
            }
            case HIILTYNYT_MAKKARA -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_ylensyönti.gif");
                häviönSyyTeksti = "Sait ruokamyrkytyksen\npilaantuneesta makkarasta.";
            }
            case KUOLEMA_JUHANI -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(15, "tiedostot/kuvat/tarina/loppu/häviö_kuolema_juhani.gif", 1);
                häviönSyyTeksti = "Juhanille ei vittuilla!";
                kelausViive = 240;
            }
            case KUOLEMA_SILLALTA_ALAS -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(30, "tiedostot/kuvat/tarina/loppu/häviö_kuolema_silta.gif", 1);
                häviönSyyTeksti = "Hyppäsit sillalta.";
                kelausViive = 240;
            }
            case KUOLEMA_BOSS -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_kuolema_boss.gif");
                häviönSyyTeksti = "Peli vei sinut mukanaan.";
            }
            case KUOLEMA_VIHOLLINEN_ASEVIHU_LYÖTY -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_kuolema_asevihu_lyöty.gif");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_ASEVIHU_PASSIIVINEN -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20,"tiedostot/kuvat/tarina/loppu/häviö_kuolema_asevihu_passiivinen.gif");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_ASEVIHU_ÄMPÄRÖITY -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20,"tiedostot/kuvat/tarina/loppu/häviö_kuolema_asevihu_ämpäröity.gif");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_PAHAVIHU_LYÖTY -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_kuolema_pahavihu_lyöty.gif");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_PAHAVIHU_PASSIIVINEN -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_kuolema_pahavihu_passiivinen.gif");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_PAHAVIHU_ÄMPÄRÖITY -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_kuolema_pahavihu_ämpäröity.gif");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_PIKKUVIHU_LYÖTY -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_kuolema_pikkuvihu_lyöty.gif");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_PIKKUVIHU_PASSIIVINEN -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_kuolema_pikkuvihu_passiivinen.gif");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_PIKKUVIHU_ÄMPÄRÖITY -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_kuolema_pikkuvihu_ämpäröity.gif");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case NORMAALI_VOITTO -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/voitto_otsikko.png");
                kuvaTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/voitto_normaali.jpg");
                häviönSyyTeksti = "Voitto!";
            }
            case VARTIJA -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_vartija.gif");
                häviönSyyTeksti = "Jäit kiinni näpistyksestä!";
            }
            case YLENSYÖNTI -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_ylensyönti.gif");
                häviönSyyTeksti = "Söit liikaa ja sinulle\ntuli paha olo.";
            }
            case null, default -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/vakio_otsikko.png");
                kuvaTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/vakioloppuruutu.png");
                häviönSyyTeksti = "Vakioloppuruutu";
            }
        }
    }

    public static void jatka() {
        Äänet.toistaSFX("Valinta");
        Peli.nollaaPeli();
        KeimoEngine.lataaTarinaRuutu("alku");
    }

    public static void render(Window window) {
        loppuRuutuShader.bind();
        loppuRuutuShader.setUniform("sampler", 0);
        loppuRuutuShader.setUniform("color", new Vector4f(0f, 0f, 0f, 0f));
        if (kelausViive > 0) kelausViive--;

        float scaleX = 1;
        if (window.getWidth() > 0 && window.getHeight() > 0) scaleX = window.getWidth()/ (window.getWidth()*2/window.getHeight());
        float scaleYOtsikko = window.getHeight()/10;
        float scaleYKuva = window.getHeight()/5;
        float scaleYTeksti = window.getHeight()/10;

        float scaleXValinnat = window.getWidth()/4;
        float scaleYValinnat = window.getHeight()/20;
        float scaleXOsoitin = window.getHeight()/20;
        float scaleYOsoitin = scaleYValinnat;
        float keskitysX = window.getWidth()/4;
        float offsetYValinta = window.getHeight()/10;

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(0, scaleYOtsikko*4f, 0);
        matOtsikko.scale(scaleX, scaleYOtsikko, 0);
        loppuRuutuShader.setUniform("projection", matOtsikko);
        otsikkoTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matKuva = new Matrix4f();
        window.getView().scale(1, matKuva);
        matKuva.translate(0, scaleYKuva*0.5f, 0);
        matKuva.scale(scaleX, scaleYKuva, 0);
        loppuRuutuShader.setUniform("projection", matKuva);
        kuvaTekstuuri.bind(0);
        Assets.getModel().render();

        Matrix4f matTeksti = new Matrix4f();
        window.getView().scale(1, matTeksti);
        matTeksti.translate(0, -scaleYTeksti*2f, 0);
        matTeksti.scale(scaleX, scaleYTeksti, 0);
        loppuRuutuShader.setUniform("projection", matTeksti);
        tekstiTexture.päivitäTeksti(häviönSyyTeksti);
        tekstiTexture.bind(0);
        Assets.getModel().render();

        if (kelausViive <= 0) {
            Matrix4f matValintaUusipeli = new Matrix4f();
            window.getView().scale(1, matValintaUusipeli);
            matValintaUusipeli.translate(scaleXValinnat - keskitysX, -scaleYKuva -1*offsetYValinta, 0);
            matValintaUusipeli.scale(scaleXValinnat, scaleYValinnat, 0);
            loppuRuutuShader.setUniform("projection", matValintaUusipeli);
            valintaUusiPeliTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matValintaLopeta = new Matrix4f();
            window.getView().scale(1, matValintaLopeta);
            matValintaLopeta.translate(scaleXValinnat - keskitysX, -scaleYKuva -2*offsetYValinta, 0);
            matValintaLopeta.scale(scaleXValinnat, scaleYValinnat, 0);
            loppuRuutuShader.setUniform("projection", matValintaLopeta);
            valintaLopetaTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matOsoitin1 = new Matrix4f();
            window.getView().scale(1, matOsoitin1);
            matOsoitin1.translate(-scaleXOsoitin - keskitysX, -scaleYKuva -1*offsetYValinta, 0);
            matOsoitin1.scale(scaleXOsoitin, scaleYOsoitin, 0);
            loppuRuutuShader.setUniform("projection", matOsoitin1);
            if (valinta == 0) osoitinKuvake.bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matOsoitin2 = new Matrix4f();
            window.getView().scale(1, matOsoitin2);
            matOsoitin2.translate(-scaleXOsoitin - keskitysX, -scaleYKuva -2*offsetYValinta, 0);
            matOsoitin2.scale(scaleXOsoitin, scaleYOsoitin, 0);
            loppuRuutuShader.setUniform("projection", matOsoitin2);
            if (valinta == 1) osoitinKuvake.bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();
        }

        Matrix4f matTilastotTeksti = new Matrix4f();
        window.getView().scale(1, matTilastotTeksti);
        matTilastotTeksti.translate(scaleXValinnat*1.5f, -scaleYKuva -offsetYValinta, 0);
        matTilastotTeksti.scale(scaleXValinnat, scaleYValinnat, 0);
        loppuRuutuShader.setUniform("projection", matTilastotTeksti);
        tilastotTeksti.bind(0);
        Assets.getModel().render();

        Matrix4f matTilastotTeksti2 = new Matrix4f();
        window.getView().scale(1, matTilastotTeksti2);
        matTilastotTeksti2.translate(scaleXValinnat*1.5f, -scaleYKuva -2*offsetYValinta, 0);
        matTilastotTeksti2.scale(scaleXValinnat, scaleYValinnat, 0);
        loppuRuutuShader.setUniform("projection", matTilastotTeksti2);
        tappolaskuriTeksti.päivitäTeksti("Mukiloit raa'asti " + TarkistettavatArvot.annaLyödytVihut() + " vihollista\nsekä ärsytit " + TarkistettavatArvot.annaÄmpäröidytVihut() + " vihollista.");
        tappolaskuriTeksti.bind(0);
        Assets.getModel().render();
    }
}
