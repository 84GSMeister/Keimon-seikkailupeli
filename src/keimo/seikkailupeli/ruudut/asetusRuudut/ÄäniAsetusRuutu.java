package keimo.seikkailupeli.ruudut.asetusRuudut;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenRenderöinti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu.AsetusRuudut;
import keimo.seikkailupeli.äänet.Musat;
import keimo.seikkailupeli.äänet.Äänet;

public class ÄäniAsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 6;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static Teksti infoTeksti = new Teksti("info", Väri.white, 2000, 300);
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -0.85f, 0, osoitinKuvake, 10, 0, 0);
    private static MenuKomponentti infoTekstiLabel = new MenuKomponentti(1, 0.25f, 0, -0.75f, infoTeksti);

    private static Renderöitävä[] asetusTekstit = new Renderöitävä[] {
        new Teksti("Musiikki", Väri.white, 600, 48),
        new Teksti("Musiikin voim.", Väri.white, 600, 48),
        new Teksti("Äänet (SFX)", Väri.white, 600, 48),
        new Teksti("Äänten voim.", Väri.white, 600, 48),
        new Teksti("Äänitesti", Väri.white, 600, 48),
        hyväksyTekstuuri,
    };

    private static Teksti[] tilaTekstit = new Teksti[] {
        new Teksti("Päällä", Väri.white, 600, 48),
        new Teksti("50", Väri.white, 600, 48),
        new Teksti("Päällä", Väri.white, 600, 48),
        new Teksti("50", Väri.white, 600, 48),
        new Teksti("", Väri.white, 600, 48),
        new Teksti("", Väri.white, 600, 48),
    };

    private static boolean musiikkiPäällä = true;
    private static float musanVoimakkuus = 0.7f;
    private static boolean äänetPäällä = true;
    private static float ääntenVoimakkuus = 0.7f;

    private static String infoTekstiMusa = "Musat\n" + 
    "Musiikin voimakkuuden muutoksessa voi kestää hetki\n" +
    "riippuen puskurin koosta.";
    private static String infoTekstiÄänet = "Äänet (SFX)\n" +
    "Vaikuttaa kaikkiin pelin ääniin.";
    private static String infoTekstiÄäniTesti = "Avaa äänitesti";

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
            case 0: // Musiikki päällä
                musiikkiPäällä = !musiikkiPäällä;
            break;
            case 1: // Musiikin voimakkuus
                if (kasvata) {
                    if (musanVoimakkuus < 1f) musanVoimakkuus += 0.01f;
                }
                else {
                    if (musanVoimakkuus > 0.005f) musanVoimakkuus -= 0.01f;
                }
            break;
            case 2: // Äänet päällä
                äänetPäällä = !äänetPäällä;
            break;
            case 3: // Ääniefektien voimakkuus
                if (kasvata) {
                    if (ääntenVoimakkuus < 1f) ääntenVoimakkuus += 0.01f;
                }
                else {
                    if (ääntenVoimakkuus > 0.005f) ääntenVoimakkuus -= 0.01f;
                }
            break;
            case 4: // Avaa Äänitesti
                
            break;
            case 5: // Hyväksy
            break;
                
            default:
            break;
        }
    }

    static void päivitäAsetukset() {
        PelinAsetukset.musiikkiPäällä = musiikkiPäällä;
        PelinAsetukset.musaVolyymi = musanVoimakkuus;
        PelinAsetukset.äänetPäällä = äänetPäällä;
        PelinAsetukset.ääniVolyymi = ääntenVoimakkuus;
        Musat.asetaMusanVolyymi(musiikkiPäällä ? musanVoimakkuus : 0);
    }

    static void hyväksy(int valinta) {
        if (valinta == 4) {
            AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.ÄÄNITESTI_VALIKKO;
        }
        else if (valinta == asetustenMäärä -1) {
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
        
        tilaTekstit[0].päivitäTeksti(musiikkiPäällä ? "Päällä" : "Pois");
        tilaTekstit[1].päivitäTeksti("" + (int)(musanVoimakkuus*100f));
        tilaTekstit[2].päivitäTeksti(äänetPäällä ? "Päällä" : "Pois");
        tilaTekstit[3].päivitäTeksti("" + (int)(ääntenVoimakkuus*100f));

        otsikkoLabel.renderöi(shader, window);

        osoitinLabel.muutaOffsetY(1f/3f - (float)((valinta) - (valinta == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f));
        osoitinLabel.renderöiPyörivä(shader, window);

        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, asetusTekstit[i], window, 1f/2.5f, 1f/15f, 1, 1f/2.5f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, tilaTekstit[i], window, 1f/2.5f, 1f/15f, 1, 1f/2.5f +1f/4f, offsetY, 0);
        }
        annaInfoTeksti(valinta);
        infoTekstiLabel.renderöi(shader, window);
    }

    private static void annaInfoTeksti(int indeksi) {
        switch (indeksi) {
            case 0: infoTeksti.päivitäTeksti(infoTekstiMusa, 0, 58); break;
            case 1: infoTeksti.päivitäTeksti(infoTekstiMusa); break;
            case 2: infoTeksti.päivitäTeksti(infoTekstiÄänet); break;
            case 3: infoTeksti.päivitäTeksti(infoTekstiÄänet  ); break;
            case 4: infoTeksti.päivitäTeksti(infoTekstiÄäniTesti); break;
            default: infoTeksti.päivitäTeksti(""); break;
        }
    }
}
