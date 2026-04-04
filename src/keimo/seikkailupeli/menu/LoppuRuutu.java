package keimo.seikkailupeli.menu;

import keimo.TarkistettavatArvot;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;

import org.joml.Vector4f;

public class LoppuRuutu {

    private static int valinta = 0;
    private static int vaihtoehtojenMäärä = 2;
    private static int kelausViive = 0;
    private static Renderöitävä otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/vakio_otsikko.png");
    private static Renderöitävä kuvaTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/voitto_normaali.jpg");
    private static Teksti tekstiTexture = new Teksti("Tarinan teksti 1", Color.WHITE, 800, 150, KeimoFontit.fontti_keimo_36, false);
    private static Teksti tilastotTeksti = new Teksti("Tilastot:", Color.WHITE, 1100, 250, KeimoFontit.fontti_keimo_36, false);
    private static String häviönSyyTeksti = "Häviön syy";
    private static Renderöitävä valintaUusiPeliTekstuuri = Assets.annaTekstuuri("menu_loppu_uusipeli");
    private static Renderöitävä valintaLopetaTekstuuri = Assets.annaTekstuuri("menu_main_lopeta");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");

    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/5f, 0, 4f/5f, otsikkoTekstuuri);
    private static MenuKomponentti kuvaLabel = new MenuKomponentti(1, 2f/5f, 0, 1f/5f, kuvaTekstuuri);
    private static MenuKomponentti tekstiLabel = new MenuKomponentti(1, 1f/5f, 0, -2f/5f, tekstiTexture);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -1f/10f -1f/2f, 0, osoitinKuvake, 10, 0, 0);
    private static MenuKomponentti valintaLabel = new MenuKomponentti(1f/2f, 1f/10f, 0, 0);
    private static MenuKomponentti tilastotLabel = new MenuKomponentti(1f/3f, 1f/5f, 2f/3f, -4f/5f, tilastotTeksti);

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
                häviönSyyTeksti = "Joit liikaa ja sinulle tuli alkoholimyrkytys.";
            }
            case HIILTYNYT_MAKKARA -> {
                otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/tarina/loppu/häviö_otsikko.png");
                kuvaTekstuuri = new Animaatio(20, "tiedostot/kuvat/tarina/loppu/häviö_ylensyönti.gif");
                häviönSyyTeksti = "Sait ruokamyrkytyksen pilaantuneesta makkarasta.";
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
                häviönSyyTeksti = "Söit liikaa ja sinulle tuli paha olo.";
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
        Peli.vaatiiUudelleenkäynnistyksen = true;
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        shader.bind();
        shader.setUniform("color", new Vector4f(0f, 0f, 0f, 0f));
        if (kelausViive > 0) kelausViive--;

        otsikkoLabel.päivitäSisältö(otsikkoTekstuuri);
        otsikkoLabel.renderöi(shader, window);

        kuvaLabel.päivitäSisältö(kuvaTekstuuri);
        kuvaLabel.renderöi(shader, window);

        tekstiTexture.päivitäTeksti(häviönSyyTeksti, 2);
        tekstiLabel.renderöi(shader, window);

        if (kelausViive <= 0) {
            osoitinLabel.muutaOffsetY(-3.5f/5f -valinta*(1f/5f));
            osoitinLabel.renderöiPyörivä(shader, window);

            for (int i = 0; i < vaihtoehtojenMäärä; i++) {
                valintaLabel.muutaOffsetY(-3.5f/5f -i*(1f/5f));
                valintaLabel.päivitäSisältö(annaValikkoTeksti(i));
                valintaLabel.renderöi(shader, window);
            }
        }

        tilastotTeksti.päivitäTeksti("Tilastot:\n\n" + "Mukiloit raa'asti " + TarkistettavatArvot.annaLyödytVihut() + " vihollista\nsekä ärsytit " + TarkistettavatArvot.annaÄmpäröidytVihut() + " vihollista.");
        tilastotLabel.renderöi(shader, window);
    }

    private static Renderöitävä annaValikkoTeksti(int valikkoElementti) {
        switch (valikkoElementti) {
            case 0: return valintaUusiPeliTekstuuri;
            case 1: return valintaLopetaTekstuuri;
            default: return valintaUusiPeliTekstuuri;
        }
    }
}
