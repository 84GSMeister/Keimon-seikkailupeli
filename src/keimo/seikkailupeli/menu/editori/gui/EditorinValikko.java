package keimo.seikkailupeli.menu.editori.gui;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.Animaatio;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.ikkuna.Window;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.HuoneLista;
import keimo.seikkailupeli.menu.editori.EditoriRuutu;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;
import java.util.ArrayList;

import org.joml.Vector4f;

public class EditorinValikko {
    private static Shader peliShader = new Shader("shader");

    private static Tekstuuri kehysTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/toimintoikkunat/toimintoikkuna_kehys_valikko.png");
    private static Animaatio osoitinTekstuuri = new Animaatio("tiedostot/kuvat/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");
    private static Teksti vaihtoehtoTeksti = new Teksti("vaihtoehto", Color.green, 400, 70);
    private static String otsikkoTeksti = "";
    private static ArrayList<String> valintaTekstit = new ArrayList<>();
    private static StaattinenKomponentti kehysKomponentti = new StaattinenKomponentti(0.5f, 0.5f, 0, 0, kehysTekstuuri);
    private static StaattinenKomponentti valintaOtsikkoKomponentti = new StaattinenKomponentti(0.25f, 1f/15f, 0, 0.25f, vaihtoehtoTeksti);

    public static int valintaInt = 0;
    private static int valintojenMäärä = 0;
    private static float siirräY;

    public static void renderöi(Shader shader, Window window) {
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        if (siirräY > 0) siirräY -= 0.05f;
        kehysKomponentti.muutaOffsetY(siirräY);
        kehysKomponentti.renderöi(peliShader, window);

        vaihtoehtoTeksti.päivitäTeksti(otsikkoTeksti, 1);
        valintaOtsikkoKomponentti.muutaOffsetY(0.25f + 1f/16f + siirräY);
        valintaOtsikkoKomponentti.renderöi(peliShader, window);
        
        for (int i = 0; i < valintojenMäärä; i++) {
            Renderöitävä osoitin;
            if (i == valintaInt) osoitin = osoitinTekstuuri;
            else osoitin = tyhjäTekstuuri;
            float offsetY = 0.25f - i * 1f/8f - 1f/8f;
            Komponentti.renderöiKomponentti(peliShader, osoitin, window, 1f/18f, 1f/15f, 1, -1/8f -1/6f, offsetY + siirräY, 0);

            vaihtoehtoTeksti.päivitäTeksti(valintaTekstit.get(i), 1);
            offsetY = 0.25f - i * 1f/8f - 1f/8f;
            Komponentti.renderöiKomponentti(peliShader, vaihtoehtoTeksti, window, 0.25f, 1f/15f, 1, -1/8f +1/6f, offsetY + siirräY, 0);
        }
    }

    public static void avaaValikko() {
        siirräY = 1.5f; // Laatikko liikkuu 3/4 ruudun verran (kokonaan näytön yläpuolelle)
        valintaInt = 0;
        luoValinnat();
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.VALINTADIALOGI;
    }

    public static void suljeValikko() {
        Peli.syötteenTila = SyötteenTila.PELI;
    }

    private static void luoValinnat() {
        valintaTekstit.clear();
        otsikkoTeksti = "Valikko";
        valintojenMäärä = 4;
        valintaTekstit.add("Jatka");
        valintaTekstit.add("Kokeile pelissä");
        valintaTekstit.add("Tallenna ja poistu");
        valintaTekstit.add("Poistu tallentamatta");
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

    public static void hyväksyValinta() {
        switch (valintaInt) {
            case 0 -> { // Jatka
                suljeValikko();
            }
            case 1 -> { // Kokeile pelissä
                Peli.nollaaPeli();
                EditoriRuutu.kopioiEditorinHuonekarttaPeliin();
                suljeValikko();
                KeimoEngine.valitseAktiivinenRuutu("peliruutu");
            }
            case 2 -> { // Tallenna ja poistu
                EditoriRuutu.kopioiEditorinHuonekarttaPeliin();
                suljeValikko();
                KeimoEngine.valitseAktiivinenRuutu("valikkoruutu");
            }
            case 3 -> { // Poistu tallentamatta
                HuoneLista.lataaReferenssiHuonekartta();
                suljeValikko();
                KeimoEngine.valitseAktiivinenRuutu("valikkoruutu");
            }
        }
        Äänet.toistaSFX("Hyväksy");
    }
    
    public static void peruValinta() {
        suljeValikko();
    }
}
