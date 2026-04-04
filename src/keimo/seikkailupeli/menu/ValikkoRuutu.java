package keimo.seikkailupeli.menu;

import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Renderöinti;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.menu.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.äänet.Äänet;

public class ValikkoRuutu {
    
    private static int valinta = 0;
    private static int vaihtoehtojenMäärä = 5;
    private static Renderöitävä otsikkoKuva = Assets.annaTekstuuri("menu_logo");
    private static Renderöitävä valintaAloitaTekstuuri = Assets.annaTekstuuri("menu_main_aloita");
    private static Renderöitävä valintaAsetuksetTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä valintaEditoriTekstuuri = Assets.annaTekstuuri("menu_main_editori");
    private static Renderöitävä valintaKehittäjätTekstuuri = Assets.annaTekstuuri("menu_main_kehittäjät");
    private static Renderöitävä valintaLopetaTekstuuri = Assets.annaTekstuuri("menu_main_lopeta");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static MenuKomponentti logo = new MenuKomponentti(1, 0.5f, 0, 0.5f, otsikkoKuva);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -1f/10f -1f/2f, 0, osoitinKuvake, 10, 0, 0);
    private static MenuKomponentti valintaLabel = new MenuKomponentti(1f/2f, 1f/10f, 0, 0);
    private static boolean siirtymä = false;

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
        Äänet.toistaSFX("Valinta");
    }

    static void hyväksy(int valinta) {

        switch (valinta) {
            case 0: // Aloita peli
                jatka();
                break;
            case 1: // Asetukset
                AsetusRuutu.pelissä = false;
                Renderöinti.siirrySeuraavaanRuutuun("asetusruutu");
                break;
            case 2: // Huone-editori
                Renderöinti.siirrySeuraavaanRuutuun("editoriruutu_varmistus");
                break;
            case 3: // Kehittäjät
                Renderöinti.siirrySeuraavaanRuutuun("kehittäjäruutu");
                break;
            case 4: // Lopeta
                System.exit(0);
                break;
            default:
                break;
        }
    }

    public static void jatka() {
        siirtymä = true;
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        try {
            shader.bind();
            shader.nollaaShaderEfektit();
            tarkistaSiirtymä();
            logo.renderöi(shader, window);
            osoitinLabel.muutaOffsetY(-1f/2f + (float)((2-valinta) * (1f/5f)));
            osoitinLabel.renderöiPyörivä(shader, window);

            for (int i = 2; i >= -2; i--) {
                valintaLabel.muutaOffsetY(-1f/2f +i*(1f/5f));
                valintaLabel.päivitäSisältö(annaValikkoTeksti(2-i));
                valintaLabel.renderöi(shader, window);
            }
            
        }
        catch (Exception e) {
            System.out.println("Valikkoruudun renderöinti epäonnistui.");
            e.printStackTrace();
        }
    }

    private static Renderöitävä annaValikkoTeksti(int valikkoElementti) {
        switch (valikkoElementti) {
            case 0: return valintaAloitaTekstuuri;
            case 1: return valintaAsetuksetTekstuuri;
            case 2: return valintaEditoriTekstuuri;
            case 3: return valintaKehittäjätTekstuuri;
            case 4: return valintaLopetaTekstuuri;
            default: return valintaAloitaTekstuuri;
        }
    }

    private static void tarkistaSiirtymä() {
        if (siirtymä) {
            if (!Peli.peliKäynnissä) {
                Renderöinti.siirrySeuraavaanRuutuun("peliruutu");
            }
            else {
                Renderöinti.siirrySeuraavaanRuutuun("peliruutu");
                Pelaaja.pakotaPelaajanPysäytys();
                Peli.pause = false;
            }
            Äänet.toistaSFX("Valinta");
            siirtymä = false;
        }
    }
}
