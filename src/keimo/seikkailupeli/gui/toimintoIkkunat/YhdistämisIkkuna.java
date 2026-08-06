package keimo.seikkailupeli.gui.toimintoIkkunat;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenRenderöinti;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Esine;
import keimo.seikkailupeli.toiminnot.Dialogit;
import keimo.seikkailupeli.äänet.Äänet;

import org.joml.Vector4f;

public class YhdistämisIkkuna {
    private static Renderöitävä kehysTekstuuri = Assets.annaTekstuuri("ikkuna_kehys_yhdistäminen");
    private static Renderöitävä kehysTekstuuriValmis = Assets.annaTekstuuri("ikkuna_kehys_yhdistäminen_valmis");
    private static Teksti vaihtoehtoTeksti;
    private static Renderöitävä yhdistettäväSlotTekstuuri = Assets.annaTekstuuri("hud_tavarapaikka_yhdistettävä");
    private static LabelKomponentti kehysKomponentti;
    private static LabelKomponentti valittuEsineLabel = new LabelKomponentti(0.1f, 0.1f, -0.3f, -0.1f);
    private static Nappi[] esineidenKuvakkeet = new Nappi[Pelaaja.esineet.length];

    public static int valintaInt = 0;
    private static float siirräY;
    private static float siirtymäZoom2;
    private static float siirtymäZoom3;
    private static boolean yhdistäminenOnnistui = false;
    private static float onnistumisSiirtymä;
    private static int onnistumisAjastin;
    private static Esine e1, e2;

    private static float rotaatioMäärä = 0;
    private static float rotaatioRaja = 20;
    private static float rotaatioNopeus = 1;
    private static boolean rotaatioMyötäpäivään = true;

    private static void alustaGrafiikat() {
        if (vaihtoehtoTeksti == null) {
            vaihtoehtoTeksti = new Teksti("vaihtoehto", Väri.green, 400, 70);
            kehysKomponentti = new LabelKomponentti(0.5f, 0.5f, 0, 0, kehysTekstuuri);
            for (int i = 0; i < esineidenKuvakkeet.length; i++) {
                float scaleX = 0.08f * siirtymäZoom3, scaleY = 0.12f * siirtymäZoom3;
                float offsetX = -0.08f + 0.188f * (i % 3);
                float offsetY = (i / 3 <= 0 ? 0f : -0.35f);
                esineidenKuvakkeet[i] = new Nappi(scaleX, scaleY, offsetX, offsetY, kehysTekstuuriValmis);
            }
        }
    }

    public static void renderöi(Shader peliShader, Ikkuna window) {
        alustaGrafiikat();
        peliShader.bind();
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
        if (yhdistäminenOnnistui) {
            renderöiOnnistumisAnimaatio(peliShader, window);
        }
        else {
            renderöiYhdistämisValikko(peliShader, window);
        }
    }

    private static void renderöiYhdistämisValikko(Shader peliShader, Ikkuna window) {
        if (siirräY > 0) siirräY -= 0.05f;
        if (siirräY <= 0 && siirtymäZoom2 < 1) siirtymäZoom2 += 0.05;
        if (siirtymäZoom2 >= 1 && siirtymäZoom3 < 1) siirtymäZoom3 += 0.05;
        kehysKomponentti.muutaOffsetY(siirräY);
        kehysKomponentti.päivitäSisältö(kehysTekstuuri);
        kehysKomponentti.renderöi(peliShader, window);
        
        if (siirräY <= 0) {
            valittuEsineLabel.päivitäSisältö(Peli.valittuEsine.annaTekstuuri());
            valittuEsineLabel.muutaKokoa(0.1f * siirtymäZoom2, 0.1f * siirtymäZoom2, -0.3f, -0.1f);
            valittuEsineLabel.renderöi(peliShader, window);
        }

        float rotZ = laskeRotZ();
        for (int i = 0; i < Pelaaja.esineet.length; i++) {
            float scaleX = 0.08f * siirtymäZoom3, scaleY = 0.12f * siirtymäZoom3;
            float offsetX = -0.08f + 0.188f * (i % 3);
            float offsetY = (i / 3 <= 0 ? 0f : -0.35f);
            if (Pelaaja.esineet[i] != null) {
                if (Peli.esineValInt == i) peliShader.setUniform("himmennys", new Vector4f(0.75f));
                else peliShader.setUniform("himmennys", new Vector4f(0f));
                if (esineidenKuvakkeet[i] != null) {
                    esineidenKuvakkeet[i].muutaKokoa(scaleX, scaleY, offsetX, offsetY);
                    esineidenKuvakkeet[i].päivitäSisältö(Pelaaja.esineet[i].annaTekstuuri());
                    esineidenKuvakkeet[i].renderöiRotaatio(peliShader, window, 0, 0, rotZ);
                }
                peliShader.setUniform("himmennys", new Vector4f(0f));
            }
            if (Peli.yhdistettäväTavarapaikka == i && siirtymäZoom2 >= 1) {
                scaleX = 0.1f; scaleY = 0.16f;
                offsetY = (i / 3 <= 0 ? -0.01f : -0.35f) + 0.05f;
                StaattinenRenderöinti.renderöiKomponentti(peliShader, yhdistettäväSlotTekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
            }
        }
    }

    private static float laskeRotZ() {
        if (rotaatioMyötäpäivään) {
            if (rotaatioMäärä < rotaatioRaja) rotaatioMäärä += rotaatioNopeus;
            else rotaatioMyötäpäivään = false;
        }
        else {
            if (rotaatioMäärä > -rotaatioRaja) rotaatioMäärä -= rotaatioNopeus;
            else rotaatioMyötäpäivään = true;
        }
        return -rotaatioMäärä;
    }

    private static void renderöiOnnistumisAnimaatio(Shader peliShader, Ikkuna window) {
        if (onnistumisAjastin <= 0) {
            suljeValikko();
        }
        else {
            kehysKomponentti.päivitäSisältö(kehysTekstuuriValmis);
            kehysKomponentti.renderöi(peliShader, window);

            if (onnistumisSiirtymä > 0) {
                float scaleX = 0.1f, scaleY = 0.16f;
                float offsetX = 1f/3f * onnistumisSiirtymä;
                StaattinenRenderöinti.renderöiKomponentti(peliShader, e1.annaTekstuuri(), window, scaleX, scaleY, 1, offsetX, 0, 0);

                offsetX = -0.5f * onnistumisSiirtymä;
                StaattinenRenderöinti.renderöiKomponentti(peliShader, e2.annaTekstuuri(), window, scaleX, scaleY, 1, offsetX, 0, 0);

                onnistumisSiirtymä -= 0.025;
            }
            else {
                float scaleX = 0.1f, scaleY = 0.16f;
                Renderöitävä yhdistettyTekstuuri = haeYhditetynEsineenTekstuuri();
                if (yhdistettyTekstuuri != null) StaattinenRenderöinti.renderöiKomponentti(peliShader, yhdistettyTekstuuri, window, scaleX, scaleY, 1, 0, 0, 0);
                if (onnistumisSiirtymä < 0) {
                    onnistumisSiirtymä = 0;
                    Äänet.toistaSFX("Yhdistäminen");
                }
            }

            onnistumisAjastin--;
        }
    }

    private static Renderöitävä haeYhditetynEsineenTekstuuri() {
        if (e1.annaNimi().equals("Kaasusytytin") || e2.annaNimi().equals("Kaasupullo")) {
            return Assets.annaTekstuuri("kaasusytytin");
        }
        else if (e1.annaNimi().equals("Kaasupullo") || e2.annaNimi().equals("Kaasusytytin")) {
            return Assets.annaTekstuuri("kaasusytytin");
        }
        else if (e1.annaNimi().equals("Jallupullo") || e2.annaNimi().equals("Paskanmarjat")) {
            return Assets.annaTekstuuri("paskanmarjabooli");
        }
        else if (e1.annaNimi().equals("Paskanmarjat") || e2.annaNimi().equals("Jallupullo")) {
            return Assets.annaTekstuuri("paskanmarjabooli");
        }
        else {
            System.out.println("Ei-tunnettu yhditelmä: " + e1.annaNimi() + ", " + e2.annaNimi());
            return null;
        }
    }

    public static void näytäOnnistunutYhdistäminen(Esine esine1, Esine esine2) {
        e1 = esine1;
        e2 = esine2;
        yhdistäminenOnnistui = true;
        onnistumisAjastin = 150;
        onnistumisSiirtymä = 1;
    }

    public static void avaaValikko() {
        siirräY = 1.5f; // Laatikko liikkuu 3/4 ruudun verran (kokonaan näytön yläpuolelle)
        siirtymäZoom2 = 0; // Valitun esineen skaala täysin näkymättömiin
        siirtymäZoom3 = 0; // Esineiden skaala täysin näkymättömiin
        valintaInt = 0;
        yhdistäminenOnnistui = false;
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.YHDISTÄMINEN;
        rotaatioMäärä = 3;
    }

    public static void suljeValikko() {
        Peli.syötteenTila = SyötteenTila.PELI;
        Pelaaja.käyttöViive = 50;
        Peli.yhdistäminenKäynnissä = false;
        Peli.yhdistettäväTavarapaikka = -1;
        if (yhdistäminenOnnistui) {
            Dialogit.avaaDialogi(Pelaaja.esineet[Peli.esineValInt].annaDialogiTekstuuri(), "Yhdistäminen onnistui! " + "Sait uuden esineen: " + Pelaaja.esineet[Peli.esineValInt].annaNimiSijamuodossa("nominatiivi"), "Yhdistäminen");
        }
    }
}
