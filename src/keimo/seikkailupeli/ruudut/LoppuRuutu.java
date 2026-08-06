package keimo.seikkailupeli.ruudut;

import keimo.TarkistettavatArvot;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.äänet.Äänet;

public class LoppuRuutu {

    private static int valinta = 0;
    private static int vaihtoehtojenMäärä = 2;
    private static int kelausViive = 0;
    private static Renderöitävä otsikkoTekstuuri;
    private static Renderöitävä kuvaTekstuuri;
    private static Teksti tekstiTexture;
    private static Teksti tilastotTeksti;
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
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_ylensyönti");
                häviönSyyTeksti = "Joit liikaa ja sinulle tuli alkoholimyrkytys.";
            }
            case HIILTYNYT_MAKKARA -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_ylensyönti");
                häviönSyyTeksti = "Sait ruokamyrkytyksen pilaantuneesta makkarasta.";
            }
            case KUOLEMA_JUHANI -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_juhani");
                häviönSyyTeksti = "Juhanille ei vittuilla!";
                kelausViive = 240;
            }
            case KUOLEMA_SILLALTA_ALAS -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_silta");
                häviönSyyTeksti = "Hyppäsit sillalta.";
                kelausViive = 240;
            }
            case KUOLEMA_BOSS -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_boss");
                häviönSyyTeksti = "Peli vei sinut mukanaan.";
            }
            case KUOLEMA_VIHOLLINEN_ASEVIHU_LYÖTY -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_asevihu_lyöty");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_ASEVIHU_PASSIIVINEN -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_asevihu_passiivinen");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_ASEVIHU_ÄMPÄRÖITY -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_asevihu_ämpäröity");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_PAHAVIHU_LYÖTY -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_pahavihu_lyöty");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_PAHAVIHU_PASSIIVINEN -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_pahavihu_passiivinen");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_PAHAVIHU_ÄMPÄRÖITY -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_pahavihu_ämpäröity");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_PIKKUVIHU_LYÖTY -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_pikkuvihu_lyöty");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_PIKKUVIHU_PASSIIVINEN -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_pikkuvihu_passiivinen");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case KUOLEMA_VIHOLLINEN_PIKKUVIHU_ÄMPÄRÖITY -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_pikkuvihu_ämpäröity");
                häviönSyyTeksti = "Sait selkääsi!";
            }
            case NORMAALI_VOITTO -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_voitto");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_voitto_normaali");
                häviönSyyTeksti = "Voitto!";
            }
            case VARTIJA -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_vartija");
                häviönSyyTeksti = "Jäit kiinni näpistyksestä!";
            }
            case YLENSYÖNTI -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_häviö");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_häviö_ylensyönti");
                häviönSyyTeksti = "Söit liikaa ja sinulle tuli paha olo.";
            }
            case null, default -> {
                otsikkoTekstuuri = Assets.annaTekstuuri("loppu_otsikko_vakio");
                kuvaTekstuuri = Assets.annaTekstuuri("loppu_vakio");
                häviönSyyTeksti = "Vakioloppuruutu";
            }
        }
    }

    public static void jatka() {
        Äänet.toistaSFX("Valinta");
        Peli.vaatiiUudelleenkäynnistyksen = true;
    }

    private static void alustaGrafiikat() {
        if (tekstiTexture == null) {
            tekstiTexture = new Teksti("Tarinan teksti 1", Väri.WHITE, 800, 150, KeimoFontit.fontti_keimo_36, false);
            tekstiLabel.päivitäSisältö(tekstiTexture);
            tilastotTeksti = new Teksti("Tilastot:", Väri.WHITE, 1100, 250, KeimoFontit.fontti_keimo_36, false);
            tilastotLabel.päivitäSisältö(tilastotTeksti);
        }
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        alustaGrafiikat();
        shader.bind();
        shader.nollaaShaderEfektit();
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

        int lyöty = TarkistettavatArvot.annaLyödytVihut();
        int ämpäröity = TarkistettavatArvot.annaÄmpäröidytVihut();
        String vihollistaL = (lyöty == 1 ? "Vihollisen" : "Vihollista");
        String vihollistaÄ = (ämpäröity == 1 ? "Vihollisen" : "Vihollista");
        tilastotTeksti.päivitäTeksti("Tilastot:\n\n" + "Mukiloit raa'asti " + lyöty + " " + vihollistaL + "\nsekä ärsytit " + ämpäröity + " " + vihollistaÄ + ".");
        //tilastotTeksti.päivitäTeksti("Tilastot:\n\n" + "Mukiloit raa'asti " + TarkistettavatArvot.annaLyödytVihut() + " " + (TarkistettavatArvot.annaLyödytVihut() == 1 ? "Vihollisen" : "Vihollista") + "\nsekä ärsytit " + TarkistettavatArvot.annaÄmpäröidytVihut() + " " + (TarkistettavatArvot.annaÄmpäröidytVihut() == 1 ? "Vihollisen" : "Vihollista") + ".");
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
