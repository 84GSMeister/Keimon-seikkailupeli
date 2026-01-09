package keimo.seikkailupeli.menu.asetusRuudut;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.äänet.Musat;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;

public class ÄäniAsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 6;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static Teksti infoTeksti = new Teksti("info", Color.white, 2000, 300);
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti infoTekstiLabel = new MenuKomponentti(1, 0.25f, 0, -0.75f, infoTeksti);

    private static Teksti asetusMusaTeksti = new Teksti("Musiikki", Color.white, 600, 48);
    private static Teksti asetusMusanVoimakkuusTeksti = new Teksti("Musiikin voim.", Color.white, 600, 48);
    private static Teksti asetusÄänetTeksti = new Teksti("Äänet (SFX)", Color.white, 600, 48);
    private static Teksti asetusÄäntenVoimakkuusTeksti = new Teksti("Äänten voim.", Color.white, 600, 48);
    private static Teksti asetusÄänitestiTeksti = new Teksti("Äänitesti", Color.white, 600, 48);

    private static Teksti tilaMusaTeksti = new Teksti("Päällä", Color.white, 600, 48);
    private static Teksti tilaMusanVoimakkuusTeksti = new Teksti("50", Color.white, 600, 48);
    private static Teksti tilaÄänetTeksti = new Teksti("Päällä", Color.white, 600, 48);
    private static Teksti tilaÄäntenVoimakkuusTeksti = new Teksti("50", Color.white, 600, 48);

    private static boolean musiikkiPäällä = true;
    private static float musanVoimakkuus = 0.5f;
    private static boolean äänetPäällä = true;
    private static float ääntenVoimakkuus = 0.5f;

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
            KeimoEngine.valitseAktiivinenRuutu("asetusruutu_äänitesti");
        }
        else if (valinta == 5) {
            KeimoEngine.valitseAktiivinenRuutu("asetusruutu");
        }
    }

    static void peruuta() {
        KeimoEngine.valitseAktiivinenRuutu("asetusruutu");
    }

    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        
        tilaMusaTeksti.päivitäTeksti(musiikkiPäällä ? "Päällä" : "Pois");
        tilaMusanVoimakkuusTeksti.päivitäTeksti("" + (int)(musanVoimakkuus*100f));
        tilaÄänetTeksti.päivitäTeksti(äänetPäällä ? "Päällä" : "Pois");
        tilaÄäntenVoimakkuusTeksti.päivitäTeksti("" + (int)(ääntenVoimakkuus*100f));

        otsikkoLabel.renderöi(shader, window);
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponentti(shader, annaOsoitinKuvake(i), window, 1f/12f, 1f/12f, 1, -1f/12f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponentti(shader, annaAsetusTekstuuri(i), window, 1f/2.5f, 1f/15f, 1, 1f/2.5f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponentti(shader, annaTilaTeksti(i), window, 1f/2.5f, 1f/15f, 1, 1f/2.5f +1f/4f, offsetY, 0);
        }
        annaInfoTeksti(valinta);
        infoTekstiLabel.renderöi(shader, window);
    }

    private static Renderöitävä annaAsetusTekstuuri(int indeksi) {
        switch (indeksi) {
            case 0: return asetusMusaTeksti;
            case 1: return asetusMusanVoimakkuusTeksti;
            case 2: return asetusÄänetTeksti;
            case 3: return asetusÄäntenVoimakkuusTeksti;
            case 4: return asetusÄänitestiTeksti;
            case 5: return hyväksyTekstuuri;
            default: return hyväksyTekstuuri;
        }
    }

    private static Renderöitävä annaOsoitinKuvake(int valikkoElementti) {
        if (valikkoElementti == valinta) return osoitinKuvake;
        else return tyhjäTekstuuri;
    }

    private static Renderöitävä annaTilaTeksti(int indeksi) {
        switch (indeksi) {
            case 0: return tilaMusaTeksti;
            case 1: return tilaMusanVoimakkuusTeksti;
            case 2: return tilaÄänetTeksti;
            case 3: return tilaÄäntenVoimakkuusTeksti;
            case 4: return tyhjäTekstuuri;
            case 5: return tyhjäTekstuuri;
            default: return tyhjäTekstuuri;
        }
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
