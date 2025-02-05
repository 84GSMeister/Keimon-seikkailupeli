package keimo.keimoEngine.menu;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.menu.asetusRuudut.AsetusRuutu;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ValikkoRuutu {
    
    private static int valinta = 0;
    private static int vaihtoehtojenMäärä = 5;
    private static Shader valikkoRuutuShader = new Shader("staattinen");
    private static Tekstuuri otsikkoKuva = new Tekstuuri("tiedostot/kuvat/menu/KEIMON_logo.png");
    private static Tekstuuri valintaAloitaTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_aloita.png");
    private static Tekstuuri valintaAsetuksetTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_asetukset.png");
    private static Tekstuuri valintaEditoriTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_editori.png");
    private static Tekstuuri valintaKehittäjätTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_kehittäjät.png");
    private static Tekstuuri valintaLopetaTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_lopeta.png");
    private static Animaatio osoitinKuvake = new Animaatio(30, "tiedostot/kuvat/animaatiot/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");

    public static void painaNäppäintä(String näppäin) {
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
        ÄänentoistamisSäie.toistaSFX("Valinta");
    }

    static void hyväksy(int valinta) {

        switch (valinta) {
            case 0: // Aloita peli
                jatka();
                break;
            case 1: // Asetukset
                AsetusRuutu.pelissä = false;
                KeimoEngine.valitseAktiivinenRuutu("asetusruutu");
                break;
            case 2: // Huone-editori
                KeimoEngine.suljePeliIkkuna();
                KeimoEngine.siirryEditoriin = true;
                break;
            case 3: // Kehittäjät
                KeimoEngine.valitseAktiivinenRuutu("kehittäjäruutu");
                break;
            case 4: // Lopeta
                System.exit(0);
                break;
            default:
                break;
        }
    }

    public static void jatka() {
        if (!Peli.peliAloitettu) {
            KeimoEngine.valitseAktiivinenRuutu("peliruutu");
        }
        else {
            KeimoEngine.valitseAktiivinenRuutu("peliruutu");
            Pelaaja.pakotaPelaajanPysäytys();
            if (Peli.huone != null) {
                ÄänentoistamisSäie.toistaPeliMusa(Peli.huone.annaHuoneenMusa());
            }
            Peli.pause = false;
        }
        ÄänentoistamisSäie.toistaSFX("Valinta");
    }

    public static void render(Window window) {
        try {
            float scaleXOtsikko = window.getWidth()/ (window.getWidth()*2/window.getHeight());
            float scaleYOtsikko = window.getHeight()/4;
            float scaleXValinnat = window.getWidth()/4;
            float scaleYValinnat = window.getHeight()/20;
            float scaleXOsoitin = window.getHeight()/20;
            float scaleYOsoitin = scaleYValinnat;
            float keskitysX = window.getWidth()/4;
            float offsetYValinta = window.getHeight()/10;

            valikkoRuutuShader.bind();
            valikkoRuutuShader.setUniform("addcolor", new Vector4f(0, 0, 0, 0));
            valikkoRuutuShader.setUniform("subcolor", new Vector4f(0, 0, 0, 0));
            valikkoRuutuShader.setUniform("himmennys", new Vector4f(0, 0, 0, 0));

            Matrix4f matOtsikko = new Matrix4f();
            window.getView().scale(1, matOtsikko);
            matOtsikko.translate(0, scaleYOtsikko, 0);
            matOtsikko.scale(scaleXOtsikko, scaleYOtsikko, 0);
            valikkoRuutuShader.setUniform("projection", matOtsikko);
            otsikkoKuva.bind(0);
            Assets.getModel().render();

            Matrix4f matValintaAloita = new Matrix4f();
            window.getView().scale(1, matValintaAloita);
            matValintaAloita.translate(scaleXValinnat - keskitysX, -scaleYOtsikko +2*offsetYValinta, 0);
            matValintaAloita.scale(scaleXValinnat, scaleYValinnat, 0);
            valikkoRuutuShader.setUniform("projection", matValintaAloita);
            valintaAloitaTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matValintaAsetukset = new Matrix4f();
            window.getView().scale(1, matValintaAsetukset);
            matValintaAsetukset.translate(scaleXValinnat - keskitysX, -scaleYOtsikko +1*offsetYValinta, 0);
            matValintaAsetukset.scale(scaleXValinnat, scaleYValinnat, 0);
            valikkoRuutuShader.setUniform("projection", matValintaAsetukset);
            valintaAsetuksetTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matValintaEditori = new Matrix4f();
            window.getView().scale(1, matValintaEditori);
            matValintaEditori.translate(scaleXValinnat - keskitysX, -scaleYOtsikko, 0);
            matValintaEditori.scale(scaleXValinnat, scaleYValinnat, 0);
            valikkoRuutuShader.setUniform("projection", matValintaEditori);
            valintaEditoriTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matValintaKehittäjät = new Matrix4f();
            window.getView().scale(1, matValintaKehittäjät);
            matValintaKehittäjät.translate(scaleXValinnat - keskitysX, -scaleYOtsikko -1*offsetYValinta, 0);
            matValintaKehittäjät.scale(scaleXValinnat, scaleYValinnat, 0);
            valikkoRuutuShader.setUniform("projection", matValintaKehittäjät);
            valintaKehittäjätTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matValintaLopeta = new Matrix4f();
            window.getView().scale(1, matValintaLopeta);
            matValintaLopeta.translate(scaleXValinnat - keskitysX, -scaleYOtsikko -2*offsetYValinta, 0);
            matValintaLopeta.scale(scaleXValinnat, scaleYValinnat, 0);
            valikkoRuutuShader.setUniform("projection", matValintaLopeta);
            valintaLopetaTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matOsoitin1 = new Matrix4f();
            window.getView().scale(1, matOsoitin1);
            matOsoitin1.translate(-scaleXOsoitin - keskitysX, -scaleYOtsikko +2*offsetYValinta, 0);
            matOsoitin1.scale(scaleXOsoitin, scaleYOsoitin, 0);
            valikkoRuutuShader.setUniform("projection", matOsoitin1);
            if (valinta == 0) osoitinKuvake.bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matOsoitin2 = new Matrix4f();
            window.getView().scale(1, matOsoitin2);
            matOsoitin2.translate(-scaleXOsoitin - keskitysX, -scaleYOtsikko +1*offsetYValinta, 0);
            matOsoitin2.scale(scaleXOsoitin, scaleYOsoitin, 0);
            valikkoRuutuShader.setUniform("projection", matOsoitin2);
            if (valinta == 1) osoitinKuvake.bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matOsoitin3 = new Matrix4f();
            window.getView().scale(1, matOsoitin3);
            matOsoitin3.translate(-scaleXOsoitin - keskitysX, -scaleYOtsikko, 0);
            matOsoitin3.scale(scaleXOsoitin, scaleYOsoitin, 0);
            valikkoRuutuShader.setUniform("projection", matOsoitin3);
            if (valinta == 2) osoitinKuvake.bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matOsoitin4 = new Matrix4f();
            window.getView().scale(1, matOsoitin4);
            matOsoitin4.translate(-scaleXOsoitin - keskitysX, -scaleYOtsikko -1*offsetYValinta, 0);
            matOsoitin4.scale(scaleXOsoitin, scaleYOsoitin, 0);
            valikkoRuutuShader.setUniform("projection", matOsoitin4);
            if (valinta == 3) osoitinKuvake.bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matOsoitin5 = new Matrix4f();
            window.getView().scale(1, matOsoitin5);
            matOsoitin5.translate(-scaleXOsoitin - keskitysX, -scaleYOtsikko -2*offsetYValinta, 0);
            matOsoitin5.scale(scaleXOsoitin, scaleYOsoitin, 0);
            valikkoRuutuShader.setUniform("projection", matOsoitin5);
            if (valinta == 4) osoitinKuvake.bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();
        }
        catch (Exception e) {
            System.out.println("Valikkoruudun renderöinti epäonnistui.");
            e.printStackTrace();
        }
    }
}
