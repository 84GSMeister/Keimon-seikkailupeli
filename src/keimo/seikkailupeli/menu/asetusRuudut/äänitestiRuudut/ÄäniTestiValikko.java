package keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.äänet.MidiToistin;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.menu.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.AsetusRuutu.AsetusRuudut;
import keimo.seikkailupeli.äänet.Musat;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;

public class ÄäniTestiValikko {
    private static int valinta = 0;
    private static int asetustenMäärä = 4;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -0.6f, 0, osoitinKuvake, 10, 0, 0);

    private static Teksti asetus1Teksti = new Teksti("Peliäänitesti", Color.white, 600, 48);
    private static Teksti asetus2Teksti = new Teksti("Miditesti", Color.white, 600, 48);
    private static Teksti asetus3Teksti = new Teksti("Wooftesti", Color.white,600, 48);

    public static void alustaGrafiikat() {
        ÄäniTestiRuutu.alustaGrafiikat();
        ÄäniTestiMidi.alustaGrafiikat();
        ÄäniTestiWoof.alustaGrafiikat();
    }

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
            case "enter" -> {
                valitse(valinta);
            }
            case "esc" -> {
                peruuta();
            }
        }
        Äänet.toistaSFX("Valinta");
    }

    static void valitse(int valinta) {

        switch (valinta) {
            case 0: // Peliäänitesti
                ÄäniTestiRuutu.alusta();
                AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.ÄÄNITESTI_PELIÄÄNET;
                break;
            case 1: // Miditesti
                ÄäniTestiMidi.alusta();
                AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.ÄÄNITESTI_MIDI;
                break;
            case 2: // Wooftesti
                ÄäniTestiWoof.alusta();
                AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.ÄÄNITESTI_WOOF;
                break;
            case 3: // Takaisin
                hyväksy();
                peruuta();
                break;
            default:
                break;
        }
    }

    private static void hyväksy() {

    }

    private static void peruuta() {
        AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.ÄÄNET;
        Äänet.suljeÄänet();
        Musat.suljeMusa();
        MidiToistin.suljeMusat();
    }

    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        otsikkoLabel.renderöi(shader, window);

        osoitinLabel.muutaOffsetY(-1f/7.5f + (float)((2-valinta) + (valinta == asetustenMäärä-1 ? 0 : 1)) * (1f/5f));
        osoitinLabel.renderöiPyörivä(shader, window);

        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = -1f/7.5f + ((2-i) + (i == asetustenMäärä-1 ? 0 : 1)) * (1f/5f);
            Komponentti.renderöiKomponenttiJaSkaalaa(shader, annaAsetusTekstuuri(i), window, 1f/2f, 1f/15f, 1, 0, offsetY, 0);
        }
    }

    private static Renderöitävä annaAsetusTekstuuri(int indeksi) {
        switch (indeksi) {
            case 0: return asetus1Teksti;
            case 1: return asetus2Teksti;
            case 2: return asetus3Teksti;
            case 3: return hyväksyTekstuuri;
            default: return hyväksyTekstuuri;
        }
    }
}
