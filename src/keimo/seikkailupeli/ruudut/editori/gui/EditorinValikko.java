package keimo.seikkailupeli.ruudut.editori.gui;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenRenderöinti;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.Renderöinti;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.huone.HuoneLista;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu;
import keimo.seikkailupeli.äänet.Äänet;

import java.util.ArrayList;

public class EditorinValikko {
    private static Renderöitävä kehysTekstuuri = Assets.annaTekstuuri("ikkuna_kehys_musta");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin_vanha");
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    private static Teksti vaihtoehtoTeksti = new Teksti("vaihtoehto", Väri.green, 400, 70);
    private static String otsikkoTeksti = "";
    private static ArrayList<String> valintaTekstit = new ArrayList<>();
    private static LabelKomponentti kehysKomponentti = new LabelKomponentti(0.5f, 0.5f, 0, 0, kehysTekstuuri);
    private static LabelKomponentti valintaOtsikkoKomponentti = new LabelKomponentti(0.25f, 1f/15f, 0, 0.25f, vaihtoehtoTeksti);

    public static int valintaInt = 0;
    private static int valintojenMäärä = 0;
    private static float siirräY;

    public static void renderöi(Shader shader, Ikkuna window) {
        shader.bind();

        if (siirräY > 0) siirräY -= 0.05f;
        kehysKomponentti.muutaOffsetY(siirräY);
        kehysKomponentti.renderöi(shader, window);

        vaihtoehtoTeksti.päivitäTeksti(otsikkoTeksti, 1);
        valintaOtsikkoKomponentti.muutaOffsetY(0.25f + 1f/16f + siirräY);
        valintaOtsikkoKomponentti.renderöi(shader, window);
        
        for (int i = 0; i < valintojenMäärä; i++) {
            Renderöitävä osoitin;
            if (i == valintaInt) osoitin = osoitinKuvake;
            else osoitin = tyhjäTekstuuri;
            float offsetY = 0.25f - i * 1f/8f - 1f/8f;
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, osoitin, window, 1f/18f, 1f/15f, 1, -1/8f -1/6f, offsetY + siirräY, 0);

            vaihtoehtoTeksti.päivitäTeksti(valintaTekstit.get(i), 1);
            offsetY = 0.25f - i * 1f/8f - 1f/8f;
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, vaihtoehtoTeksti, window, 0.25f, 1f/15f, 1, -1/8f +1/6f, offsetY + siirräY, 0);
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
                Renderöinti.siirrySeuraavaanRuutuun("peliruutu");
            }
            case 2 -> { // Tallenna ja poistu
                EditoriRuutu.kopioiEditorinHuonekarttaPeliin();
                suljeValikko();
                Renderöinti.siirrySeuraavaanRuutuun("valikkoruutu");
            }
            case 3 -> { // Poistu tallentamatta
                HuoneLista.lataaReferenssiHuonekartta();
                suljeValikko();
                Renderöinti.siirrySeuraavaanRuutuun("valikkoruutu");
            }
        }
        Äänet.toistaSFX("Hyväksy");
    }
    
    public static void peruValinta() {
        suljeValikko();
    }
}
