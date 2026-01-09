package keimo.seikkailupeli.gui.toimintoIkkunat;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.Ruudut;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.dialogi.VuoropuheDialogiPätkä;
import keimo.seikkailupeli.menu.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.toiminnot.Dialogit;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;
import java.util.ArrayList;

import org.joml.Vector4f;

public class DialogiValintaIkkuna {

    private static Shader peliShader = new Shader("shader");

    private static Renderöitävä kehysTekstuuri = Assets.annaTekstuuri("ikkuna_kehys_musta");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    private static Teksti vaihtoehtoTeksti = new Teksti("vaihtoehto", Color.green, 400, 70);
    private static String otsikkoTeksti = "";
    private static ArrayList<String> valintaTekstit = new ArrayList<>();
    private static StaattinenKomponentti kehysKomponentti = new StaattinenKomponentti(0.5f, 0.5f, 0, 0, kehysTekstuuri);
    private static StaattinenKomponentti valintaOtsikkoKomponentti = new StaattinenKomponentti(0.25f, 1f/15f, 0, 0.25f, vaihtoehtoTeksti);

    public static int valintaInt = 0;
    private static int valintojenMäärä = 0;
    private static String valintaDialoginTunniste = "";
    private static float siirräY;

    public static void renderöi(Shader shader, Ikkuna window) {
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        if (siirräY > 0) siirräY -= 0.05f;
        kehysKomponentti.muutaOffsetY(siirräY);
        kehysKomponentti.renderöi(peliShader, window);

        vaihtoehtoTeksti.päivitäTeksti(otsikkoTeksti, 1, 5);
        valintaOtsikkoKomponentti.muutaOffsetY(0.25f + 1f/16f + siirräY);
        valintaOtsikkoKomponentti.renderöi(peliShader, window);
        
        for (int i = 0; i < valintojenMäärä; i++) {
            Renderöitävä osoitin;
            if (i == valintaInt) osoitin = osoitinKuvake;
            else osoitin = tyhjäTekstuuri;
            float scaleY;
            if (valintojenMäärä >= 4) scaleY = 1f / (3.25f*valintojenMäärä);
            else scaleY = 1f/15f;
            float offsetY = 0.25f - i * scaleY*1.75f - scaleY*1.75f;
            Komponentti.renderöiKomponentti(peliShader, osoitin, window, 1f/18f, scaleY, 1, -1/8f -1/6f, offsetY + siirräY, 0);

            vaihtoehtoTeksti.päivitäTeksti(valintaTekstit.get(i), 1, 7);
            offsetY = 0.25f - i * scaleY*1.75f - scaleY*1.75f;
            Komponentti.renderöiKomponentti(peliShader, vaihtoehtoTeksti, window, 0.25f, scaleY, 1, -1/8f +1/6f, offsetY + siirräY, 0);
        }
    }

    public static void avaaToimintoIkkuna(String valinta) {
        siirräY = 1.5f; // Laatikko liikkuu 3/4 ruudun verran (kokonaan näytön yläpuolelle)
        valintaDialoginTunniste = valinta;
        valintaInt = 0;
        if (Dialogit.PitkätDialogit.vuoropuheDialogiKartta != null) {
            vdp = Dialogit.PitkätDialogit.vuoropuheDialogiKartta.get(valinta);
            luoValinnat();
            Peli.syötteenTila = SyötteenTila.TOIMINTO;
            Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.VALINTADIALOGI;
        }
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
    }

    private static void luoValinnat() {
        valintaTekstit.clear();
        if (valintaDialoginTunniste == "pause") {
            otsikkoTeksti = "pause";
            valintojenMäärä = 6;
            valintaTekstit.add("jatka");
            valintaTekstit.add("asetukset");
            valintaTekstit.add("ohjeet");
            valintaTekstit.add("siirry editoriin");
            valintaTekstit.add("uusi peli");
            valintaTekstit.add("lopeta");
        }
        else {
            if (vdp.onkoValinta()) {
                otsikkoTeksti = vdp.annaValinnanOtsikko();
                for (int i = 0; i < vdp.annaValinnanVaihtoehdot().length; i++) {
                    valintaTekstit.add(vdp.annaValinnanVaihtoehdot()[i]);
                }
                valintojenMäärä = vdp.annaValinnanVaihtoehdot().length;
            }
        }
    }

    public static void pienennäValintaa() {
        if (valintaInt <= 0) valintaInt = valintojenMäärä-1;
        else valintaInt--;
        Äänet.toistaSFX("Valinta");
    }
    public static void kasvataValintaa() {
        if (valintaInt >= valintojenMäärä-1) valintaInt = 0;
        else valintaInt++;
        Äänet.toistaSFX("Valinta");
    }

    private static VuoropuheDialogiPätkä vdp;

    public static void hyväksyValinta() {
        if (valintaDialoginTunniste == "pause") {
            switch (valintaInt) {
                case 0: // Jatka
                    Peli.pausetaPeli(false);
                    suljeToimintoIkkuna();
                break;
                case 1: // Asetukset
                    AsetusRuutu.pelissä = true;
                    Peli.aktiivinenRuutu = Ruudut.ASETUSRUUTU;
                break;
                case 2: // Ohjeet
                    OhjeIkkuna.avaaToimintoIkkuna();
                break;
                case 3: // Siirry editoriin
                    KeimoEngine.valitseAktiivinenRuutu("editoriruutu");
                break;
                case 4: // Uusi peli
                    Peli.nollaaPeli();
                    KeimoEngine.lataaTarinaRuutu("alku");
                break;
                case 5: // Lopeta
                    System.exit(0);
                break;
            }
        }
        else {
            if (vdp.annaTriggerit() != null) {
                if (vdp.annaTriggerit()[valintaInt] != null) {
                    Dialogit.DialogiTriggerit.suoritaDialogiTriggeri(vdp.annaTriggerit()[valintaInt]);
                }
            }
            suljeToimintoIkkuna();
            if (vdp.annaValinnanVaihtoehtojenKohdeDialogit()[valintaInt] != null && vdp.annaValinnanVaihtoehtojenKohdeDialogit()[valintaInt] != "") {
                Dialogit.avaaPitkäDialogiRuutu(vdp.annaValinnanVaihtoehtojenKohdeDialogit()[valintaInt]);
            }
        }
        Äänet.toistaSFX("Hyväksy");
        Pelaaja.käyttöViive = 30;
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
