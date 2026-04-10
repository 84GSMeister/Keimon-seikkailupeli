package keimo.seikkailupeli.gui.toimintoIkkunat;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;
import java.util.ArrayList;

import org.joml.Vector4f;

public class HuijauskoodiValikko {
    private static Renderöitävä kehysTekstuuri = Assets.annaTekstuuri("ikkuna_kehys_musta");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin_vanha");
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    private static Teksti vaihtoehtoTeksti;
    private static Teksti tilaTeksti;
    private static String otsikkoTeksti = "";
    private static ArrayList<String> valintaTekstit = new ArrayList<>();
    private static StaattinenKomponentti kehysKomponentti;
    private static StaattinenKomponentti valintaOtsikkoKomponentti;

    public static int valintaInt = 0;
    private static int valintojenMäärä = 0;
    private static float siirräY;

    private static void alustaGrafiikat() {
        if (vaihtoehtoTeksti == null) {
            vaihtoehtoTeksti = new Teksti("vaihtoehto", Color.green, 400, 70);
            tilaTeksti = new Teksti("tila", Color.green, 200, 70);
            kehysKomponentti = new StaattinenKomponentti(0.5f, 0.5f, 0, 0, kehysTekstuuri);
            valintaOtsikkoKomponentti = new StaattinenKomponentti(0.25f, 1f/15f, 0, 0.25f, vaihtoehtoTeksti);
        }
    }

    public static void renderöi(Shader peliShader, Ikkuna window) {
        alustaGrafiikat();
        peliShader.bind();
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        if (siirräY > 0) siirräY -= 0.05f;
        kehysKomponentti.muutaOffsetY(siirräY);
        kehysKomponentti.renderöi(peliShader, window);

        vaihtoehtoTeksti.päivitäTeksti(otsikkoTeksti, 1, 11);
        valintaOtsikkoKomponentti.muutaOffsetY(0.25f + 1f/16f + siirräY);
        valintaOtsikkoKomponentti.renderöi(peliShader, window);
        
        for (int i = 0; i < valintojenMäärä; i++) {
            Renderöitävä osoitin;
            if (i == valintaInt) osoitin = osoitinKuvake;
            else osoitin = tyhjäTekstuuri;
            float offsetY = 0.25f - i * 1f/8f - 1f/8f;
            Komponentti.renderöiKomponenttiJaSkaalaa(peliShader, osoitin, window, 1f/18f, 1f/15f, 1, -0.25f, offsetY + siirräY, 0);

            String uusiTeksti = valintaTekstit.get(i) + ": ";
            vaihtoehtoTeksti.päivitäTeksti(uusiTeksti, 1, 13, Color.yellow);
            offsetY = 0.25f - i * 1f/8f - 1f/8f;
            Komponentti.renderöiKomponenttiJaSkaalaa(peliShader, vaihtoehtoTeksti, window, 1f/6f, 1f/15f, 1, 0f, offsetY + siirräY, 0);

            uusiTeksti = (koodiValittu(i) ? "Kyllä" : "Ei");
            if (koodiValittu(i)) tilaTeksti.päivitäTeksti(uusiTeksti, 0, 5, Color.green);
            else tilaTeksti.päivitäTeksti(uusiTeksti, 0, 30, Color.red);
            offsetY = 0.25f - i * 1f/8f - 1f/8f;
            Komponentti.renderöiKomponenttiJaSkaalaa(peliShader, tilaTeksti, window, 1f/12f, 1f/15f, 1, 0.3f, offsetY + siirräY, 0);
        }
    }

    public static void avaaValikko() {
        siirräY = 1.5f; // Laatikko liikkuu 3/4 ruudun verran (kokonaan näytön yläpuolelle)
        valintaInt = 0;
        luoValinnat();
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.HUIJAUSKOODIT;
    }

    public static void suljeValikko() {
        Peli.syötteenTila = SyötteenTila.PELI;
        Pelaaja.käyttöViive = 50;
    }

    private static boolean koodiValittu(int koodi) {
        switch (koodi) {
            case 0: return Pelaaja.noclip;
            case 1: return Pelaaja.ohitaTavoitteet;
            case 2: return Pelaaja.loputonRaha;
            default: return false;
        }
    }

    private static void luoValinnat() {
        valintaTekstit.clear();
        otsikkoTeksti = "Huijauskoodit";
        valintojenMäärä = 3;
        valintaTekstit.add("Noclip");
        valintaTekstit.add("Ohita tavoitteet");
        valintaTekstit.add("Loputon raha");
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

    public static void muutaValintaa() {
        switch (valintaInt) {
            case 0 -> { // Noclip
                Pelaaja.noclip = !Pelaaja.noclip;
            }
            case 1 -> { // Ohita tavoitteet
                Pelaaja.ohitaTavoitteet = !Pelaaja.ohitaTavoitteet;
            }
            case 2 -> { // Loputon raha
                Pelaaja.loputonRaha = !Pelaaja.loputonRaha;
            }
        }
        Äänet.toistaSFX("Valinta");
    }
    
    public static void peruValinta() {
        suljeValikko();
    }
}
