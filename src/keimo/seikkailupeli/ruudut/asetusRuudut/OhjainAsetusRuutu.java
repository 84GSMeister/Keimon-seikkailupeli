package keimo.seikkailupeli.ruudut.asetusRuudut;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenRenderöinti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.PelinAsetukset.OhjainKuvakkeet;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu.AsetusRuudut;
import keimo.seikkailupeli.äänet.Äänet;

public class OhjainAsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 2;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static Teksti infoTeksti = new Teksti("info", Väri.white, 2000, 300);
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -0.85f, 0, osoitinKuvake, 10, 0, 0);
    private static MenuKomponentti infoTekstiLabel = new MenuKomponentti(1, 0.25f, 0, -0.75f, infoTeksti);

    private static Renderöitävä[] asetusTekstit = new Renderöitävä[] {
        new Teksti("Ohjainkuvakkeet", Väri.white, 600, 48),
        hyväksyTekstuuri,
    };

    private static Renderöitävä[] tilaKuvakkeet = new Renderöitävä[] {
        Assets.annaTekstuuri("asetukset_ohjaimet_xbox"),
        new Teksti("", Väri.white, 600, 48),
    };

    private static String[] ohjainKuvakeValinnat = {"Xbox", "Nintendo", "Playstation"};
    private static int ohjainKuvakeValinta = 0;
    private static String valittuOhjainKuvake = "Xbox";

    private static String infoTekstiOhjainkuvakkeet = "Valitse ohjainkuvakkeet\n" + 
    "Vaikuttaa ainoastaan visuaalisiin kuvakkeisiin.\n" +
    "Näppäinten uudelleenmääritys tulossa myöhemmin.";

    public static void painaNäppäintä(String näppäin) {
        switch (näppäin) {
            case "ylös" -> {
                valinta--;
                if (valinta < 0) {
                    valinta = asetustenMäärä-1;
                }
            }
            case "alas" -> {
                valinta++;
                if (valinta > asetustenMäärä-1) {
                    valinta = 0;
                }
            }
            case "vasen" -> {
                säädäAsetusta(valinta, false);
                päivitäAsetukset();
            }
            case "oikea" -> {
                säädäAsetusta(valinta, true);
                päivitäAsetukset();
            }
            case "enter" -> {
                hyväksy(valinta);
            }
            case "esc" -> {
                peruuta();
            }
        }
        Äänet.toistaSFX("Valinta");
    }

    static void säädäAsetusta(int valinta, boolean kasvata) {
        switch (valinta) {
            case 0: // Ohjainkuvakkeet
                if (kasvata) {
                    if (ohjainKuvakeValinta < ohjainKuvakeValinnat.length-1) ohjainKuvakeValinta++;
                }
                else {
                    if (ohjainKuvakeValinta > 0) ohjainKuvakeValinta--;
                }
                valittuOhjainKuvake = ohjainKuvakeValinnat[ohjainKuvakeValinta];
            break;
            case 1: // Hyväksy
                
            break;
            default:
            break;
        }
    }

    static void päivitäAsetukset() {
        switch (valittuOhjainKuvake) {
            case "Xbox": PelinAsetukset.ohjainKuvakkeet = OhjainKuvakkeet.XBOX; break;
            case "Nintendo": PelinAsetukset.ohjainKuvakkeet = OhjainKuvakkeet.NINTENDO; break;
            case "Playstation": PelinAsetukset.ohjainKuvakkeet = OhjainKuvakkeet.PLAYSTATION; break;
        }
    }

    static void hyväksy(int valinta) {
        if (valinta == asetustenMäärä -1) {
            AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.ASETUSRUUTU;
            valinta = 0;
        }
    }

    static void peruuta() {
        AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.ASETUSRUUTU;
        valinta = 0;
    }

    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        
        //tilaTekstit[0].päivitäTeksti(valittuOhjainKuvake);
        tilaKuvakkeet[0] = annaOhjainKuvakeTekstuuri(valittuOhjainKuvake);

        otsikkoLabel.renderöi(shader, window);

        osoitinLabel.muutaOffsetY(1f/3f - (float)((valinta) - (valinta == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f));
        osoitinLabel.renderöiPyörivä(shader, window);

        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, asetusTekstit[i], window, 1f/2.5f, 1f/15f, 1, 1f/2.5f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, tilaKuvakkeet[i], window, 1f/3f, 1f/15f, 1, 1f/2.5f +1f/4f, offsetY, 0);
        }
        annaInfoTeksti(valinta);
        infoTekstiLabel.renderöi(shader, window);
    }

    private static Renderöitävä annaOhjainKuvakeTekstuuri(String valittuOhjainKuvake) {
        switch (valittuOhjainKuvake) {
            case "Xbox": return Assets.annaTekstuuri("asetukset_ohjaimet_xbox");
            case "Nintendo": return Assets.annaTekstuuri("asetukset_ohjaimet_nintendo");
            case "Playstation": return Assets.annaTekstuuri("asetukset_ohjaimet_playstation");
            case null, default: return Assets.annaTekstuuri("asetukset_ohjaimet_xbox");
        }
    }

    private static void annaInfoTeksti(int indeksi) {
        switch (indeksi) {
            case 0: infoTeksti.päivitäTeksti(infoTekstiOhjainkuvakkeet, 0, 58); break;
            default: infoTeksti.päivitäTeksti(""); break;
        }
    }
}
