package keimo.keimoEngine.gui.toimintoIkkunat;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.Peli.Ruudut;
import keimo.Peli.SyötteenTila;
import keimo.Peli.ToimintoIkkunanTyyppi;
import keimo.TarkistettavatArvot;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.HuoneEditori.DialogiEditori.VuoropuheDialogiPätkä;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.menu.asetusRuudut.AsetusRuutu;
import keimo.keimoEngine.toiminnot.Dialogit;

import java.awt.Color;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class DialogiValintaIkkuna {

    private static Shader peliShader = new Shader("shader");

    private static Tekstuuri kehysTekstuuri = new Tekstuuri("tiedostot/kuvat/toimintoikkunat/toimintoikkuna_kehys_valikko.png");
    private static Animaatio osoitinTekstuuri = new Animaatio(30, "tiedostot/kuvat/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");
    private static Teksti vaihtoehtoTeksti = new Teksti("vaihtoehto", Color.green, 150, 20);
    private static String otsikkoTeksti = "";
    private static ArrayList<String> valintaTekstit = new ArrayList<>();

    public static int valintaInt = 0;
    private static int valintojenMäärä = 0;
    private static String valintaDialoginTunniste = "";
    private static float siirräY = 600;
    
    public static void renderöiKehys(Window window) {
        float scaleX = window.getWidth()/4;
        float scaleY = window.getHeight()/4;
        if (siirräY > 0) siirräY -= 20;
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matKehys = new Matrix4f();
        window.getView().scale(1, matKehys);
        matKehys.translate(0, siirräY, 0);
        matKehys.scale(scaleX, scaleY, 0);
        peliShader.setUniform("projection", matKehys);
        kehysTekstuuri.bind(0);
        Assets.getModel().render();
    }

    public static void avaaToimintoIkkuna(String valinta) {
        System.out.println(valinta);
        valintaDialoginTunniste = valinta;
        valintaInt = 0;
        vdp = Dialogit.PitkätDialogit.vuoropuheDialogiKartta.get(valinta);
        luoValinnat();
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.VALINTADIALOGI;
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
        siirräY = 600;
    }

    private static void luoValinnat() {
        valintaTekstit.clear();
        if (valintaDialoginTunniste == "pause" || valintaDialoginTunniste == "silta") {
            switch (valintaDialoginTunniste) {
                case "pause":
                    otsikkoTeksti = "pause";
                    valintojenMäärä = 4;
                    valintaTekstit.add("jatka");
                    valintaTekstit.add("asetukset");
                    valintaTekstit.add("uusi peli");
                    valintaTekstit.add("lopeta");
                break;
                case "silta":
                    otsikkoTeksti = "hyppää?";
                    valintojenMäärä = 2;
                    valintaTekstit.add("kyllä");
                    valintaTekstit.add("ei");
                break;
            }
        }
        else {
            System.out.println(vdp.annaTunniste());
            if (vdp.onkoValinta()) {
                otsikkoTeksti = vdp.annaValinnanOtsikko();
                for (int i = 0; i < vdp.annaValinnanVaihtoehdot().length; i++) {
                    valintaTekstit.add(vdp.annaValinnanVaihtoehdot()[i]);
                }
                valintojenMäärä = vdp.annaValinnanVaihtoehdot().length;
            }
        }
    }

    public static void renderöiValinnat(Window window) {
        
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        float scaleXOsoitin = window.getWidth()/35;
        float scaleYOsoitin = window.getHeight()/30;
        float scaleXVaihtoehto = window.getWidth()/8;
        float scaleYVaihtoehto = scaleYOsoitin;
        float keskitysX = window.getWidth()/16;
        float keskitysY = window.getHeight()/8;
        float offsetX = window.getWidth()/12;
        float offsetY = window.getHeight()/16;

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(0, keskitysY + 0.5f*offsetY + siirräY, 0);
        matOtsikko.scale(scaleXVaihtoehto, scaleYVaihtoehto, 0);
        peliShader.setUniform("projection", matOtsikko);
        vaihtoehtoTeksti.päivitäTeksti(otsikkoTeksti, true);
        vaihtoehtoTeksti.bind(0);
        Assets.getModel().render();
        
        for (int i = 0; i < valintojenMäärä; i++) {
            Matrix4f matOsoitin = new Matrix4f();
            window.getView().scale(1, matOsoitin);
            matOsoitin.translate(-keskitysX -offsetX, keskitysY - i * offsetY - offsetY + siirräY, 0);
            matOsoitin.scale(scaleXOsoitin, scaleYOsoitin, 0);
            peliShader.setUniform("projection", matOsoitin);
            //vaihtoehtoTeksti.päivitäTeksti(valintaTekstit.get(i), true);
            //vaihtoehtoTeksti.bind(0);
            if (i == valintaInt) osoitinTekstuuri.bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();

            Matrix4f matVaihtoehto = new Matrix4f();
            window.getView().scale(1, matVaihtoehto);
            matVaihtoehto.translate(-keskitysX + offsetX, keskitysY - i * offsetY - offsetY + siirräY, 0);
            matVaihtoehto.scale(scaleXVaihtoehto, scaleYVaihtoehto, 0);
            peliShader.setUniform("projection", matVaihtoehto);
            vaihtoehtoTeksti.päivitäTeksti(valintaTekstit.get(i), true);
            vaihtoehtoTeksti.bind(0);
            Assets.getModel().render();
        }
    }

    public static void pienennäValintaa() {
        if (valintaInt <= 0) valintaInt = valintojenMäärä-1;
        else valintaInt--;
        ÄänentoistamisSäie.toistaSFX("Valinta");
    }
    public static void kasvataValintaa() {
        if (valintaInt >= valintojenMäärä-1) valintaInt = 0;
        else valintaInt++;
        ÄänentoistamisSäie.toistaSFX("Valinta");
    }

    private static VuoropuheDialogiPätkä vdp;

    public static void hyväksyValinta() {
        if (valintaDialoginTunniste == "pause" || valintaDialoginTunniste == "silta") {
            switch (valintaDialoginTunniste) {
                case "pause":
                    switch (valintaInt) {
                        case 0:
                            Peli.pausetaPeli(false);
                            suljeToimintoIkkuna();
                        break;
                        case 1:
                            AsetusRuutu.pelissä = true;
                            //Peli.syötteenTila = SyötteenTila.ASETUKSET;
                            Peli.aktiivinenRuutu = Ruudut.ASETUSRUUTU;
                        break;
                        case 2:
                            Peli.nollaaPeli();
                            KeimoEngine.lataaTarinaRuutu("alku");
                        break;
                        case 3:
                            System.exit(0);
                        break;
                    }
                break;
                case "silta":
                    if (valintaInt == 0) {
                        TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_SILLALTA_ALAS;
                        Pelaaja.hp = 0;
                    }
                    suljeToimintoIkkuna();
                break;
            }
        }
        else {
            if (vdp.annaTriggerit() != null) {
                if (vdp.annaTriggerit()[valintaInt] != null) {
                    TavoiteLista.suoritaTavoite(vdp.annaTriggerit()[valintaInt]);
                }
            }
            suljeToimintoIkkuna();
            if (vdp.annaValinnanVaihtoehtojenKohdeDialogit()[valintaInt] != null && vdp.annaValinnanVaihtoehtojenKohdeDialogit()[valintaInt] != "") {
                Dialogit.avaaPitkäDialogiRuutu(vdp.annaValinnanVaihtoehtojenKohdeDialogit()[valintaInt]);
            }
        }
        ÄänentoistamisSäie.toistaSFX("Hyväksy");
    }
    public static void peruValinta() {
        switch (valintaDialoginTunniste) {
            case "pause":
                Peli.pausetaPeli(false);
                suljeToimintoIkkuna();
            break;
        }
    }
}
