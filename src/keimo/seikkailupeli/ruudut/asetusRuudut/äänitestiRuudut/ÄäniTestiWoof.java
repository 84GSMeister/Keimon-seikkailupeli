package keimo.seikkailupeli.ruudut.asetusRuudut.äänitestiRuudut;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenRenderöinti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.äänet.MidiToistin;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu.AsetusRuudut;
import keimo.seikkailupeli.äänet.Musat;
import keimo.seikkailupeli.äänet.Äänet;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ÄäniTestiWoof {
    
    private static int valinta = 0;
    private static int asetustenMäärä = 3;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin3");
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -0.85f, 0, osoitinKuvake, 0, 0, 5);

    private static Teksti asetusWoofÄäniTeksti;
    private static Teksti asetusNuottiTeksti;

    private static Teksti tilaWoofÄäniTeksti;
    private static Teksti tilaNuottiTeksti;

    private static List<File> woofTiedostot;
    private static int valittuWoofTiedosto = 0;
    private static int valittuNuotti = 64;

    public static void alusta() {
        listaaÄänet();
    }

    private static void listaaÄänet() {
        try {
            woofTiedostot = Stream.of(new File("tiedostot/äänet/woof/").listFiles())
                .filter(file -> !file.isDirectory() && ((file.getName().endsWith(".wav"))))
                .collect(Collectors.toList());
        }
        catch (Exception e) {
            System.out.println("Virhe ladatessa ääniä");
            e.printStackTrace();
        }
    }

    public static void alustaGrafiikat() {
        asetusWoofÄäniTeksti = new Teksti("Woof", Väri.white, 400, 48);
        asetusNuottiTeksti = new Teksti("Nuotti", Väri.white, 400, 48);
        tilaWoofÄäniTeksti = new Teksti("Woof", Väri.white, 900, 48);
        tilaNuottiTeksti = new Teksti("E5", Väri.white, 900, 48);
    }

    public static void painaNäppäintä(String näppäin) {
        switch (näppäin) {
            case "ylös" -> {
                valinta--;
                if (valinta < 0) {
                    valinta = asetustenMäärä-1;
                }
                Äänet.toistaSFX("Valinta");
            }
            case "alas" -> {
                valinta++;
                if (valinta > asetustenMäärä-1) {
                    valinta = 0;
                }
                Äänet.toistaSFX("Valinta");
            }
            case "vasen" -> {
                säädäAsetusta(valinta, false);
                Äänet.toistaSFX("Valinta");
            }
            case "oikea" -> {
                säädäAsetusta(valinta, true);
                Äänet.toistaSFX("Valinta");
            }
            case "enter" -> {
                hyväksy(valinta);
            }
            case "esc" -> {
                Äänet.suljeÄänet();
                Musat.suljeMusa();
                MidiToistin.suljeMusat();
            }
        }
    }

    static void säädäAsetusta(int valinta, boolean kasvata) {
        switch (valinta) {
            case 0 -> { // Woof-ääni
                if (kasvata) {
                    if (valittuWoofTiedosto < woofTiedostot.size()-1) valittuWoofTiedosto++;
                }
                else {
                    if (valittuWoofTiedosto > 0) valittuWoofTiedosto--;
                }
            }
            case 1 -> { // Nuotti
                if (kasvata) {
                    if (valittuNuotti < 127) valittuNuotti++;
                }
                else {
                    if (valittuNuotti > 0) valittuNuotti--;
                }
            }
            default -> {

            }
        }
    }

    private static void hyväksy(int valinta) {
        if (valinta == 0 || valinta == 1) {
            toistaValittuÄäni();
        }
        if (valinta == 2) {
            AsetusRuutu.aktiivinenAsetusRuutu = AsetusRuudut.ÄÄNITESTI_VALIKKO;
            Äänet.toistaSFX("Valinta");
        }
    }

    private static void toistaValittuÄäni() {
        float sampleRate = (float)(44100 * Math.pow(2d, (((double)valittuNuotti-64d)/12d)));
        toistaValittuÄäni(sampleRate);
    }

    public static void toistaValittuÄäni(float sampleRate) {
        MidiToistin.suljeMusat();
        File valittuTiedosto = woofTiedostot.get(valittuWoofTiedosto);
        Äänet.toistaÄäni(valittuTiedosto, 1, 0, false, sampleRate, false, false);
    }
    
    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        
        tilaWoofÄäniTeksti.päivitäTeksti(woofTiedostot.get(valittuWoofTiedosto).getName());
        tilaNuottiTeksti.päivitäTeksti(haeNuotti(valittuNuotti));

        otsikkoLabel.renderöi(shader, window);

        osoitinLabel.muutaOffsetY(1f/3f - (float)((valinta) - (valinta == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f));
        osoitinLabel.renderöiPyörivä(shader, window);

        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, annaAsetusTekstuuri(i), window, 1f/2.5f, 1f/15f, 1, 1f/2.5f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, annaTilaTeksti(i), window, 1f/2.25f, 1f/15f, 1, 1f/2f, offsetY, 1f/10f);
        }
    }

    private static Renderöitävä annaAsetusTekstuuri(int indeksi) {
        switch (indeksi) {
            case 0: return asetusWoofÄäniTeksti;
            case 1: return asetusNuottiTeksti;
            case 2: return hyväksyTekstuuri;
            default: return hyväksyTekstuuri;
        }
    }

    private static Renderöitävä annaTilaTeksti(int indeksi) {
        switch (indeksi) {
            case 0: return tilaWoofÄäniTeksti;
            case 1: return tilaNuottiTeksti;
            default: return tyhjäTekstuuri;
        }
    }

    private static String haeNuotti(int midiNuotti) {
        String nuotti;
        switch (midiNuotti) {
            case 0: nuotti = "C0"; break;
            case 1: nuotti = "C#0"; break;
            case 2: nuotti = "D0"; break;
            case 3: nuotti = "D#0"; break;
            case 4: nuotti = "E0"; break;
            case 5: nuotti = "F0"; break;
            case 6: nuotti = "F#0"; break;
            case 7: nuotti = "G0"; break;
            case 8: nuotti = "G#0"; break;
            case 9: nuotti = "A0"; break;
            case 10: nuotti = "A#0"; break;
            case 11: nuotti = "B0"; break;
            case 12: nuotti = "C1"; break;
            case 13: nuotti = "C#1"; break;
            case 14: nuotti = "D1"; break;
            case 15: nuotti = "D#1"; break;
            case 16: nuotti = "E1"; break;
            case 17: nuotti = "F1"; break;
            case 18: nuotti = "F#1"; break;
            case 19: nuotti = "G1"; break;
            case 20: nuotti = "G#1"; break;
            case 21: nuotti = "A1"; break;
            case 22: nuotti = "A#1"; break;
            case 23: nuotti = "B1"; break;
            case 24: nuotti = "C2"; break;
            case 25: nuotti = "C#2"; break;
            case 26: nuotti = "D2"; break;
            case 27: nuotti = "D#2"; break;
            case 28: nuotti = "E2"; break;
            case 29: nuotti = "F2"; break;
            case 30: nuotti = "F#2"; break;
            case 31: nuotti = "G2"; break;
            case 32: nuotti = "G#2"; break;
            case 33: nuotti = "A2"; break;
            case 34: nuotti = "A#2"; break;
            case 35: nuotti = "B2"; break;
            case 36: nuotti = "C3"; break;
            case 37: nuotti = "C#3"; break;
            case 38: nuotti = "D3"; break;
            case 39: nuotti = "D#3"; break;
            case 40: nuotti = "E3"; break;
            case 41: nuotti = "F3"; break;
            case 42: nuotti = "F#3"; break;
            case 43: nuotti = "G3"; break;
            case 44: nuotti = "G#3"; break;
            case 45: nuotti = "A3"; break;
            case 46: nuotti = "A#3"; break;
            case 47: nuotti = "B3"; break;
            case 48: nuotti = "C4"; break;
            case 49: nuotti = "C#4"; break;
            case 50: nuotti = "D4"; break;
            case 51: nuotti = "D#4"; break;
            case 52: nuotti = "E4"; break;
            case 53: nuotti = "F4"; break;
            case 54: nuotti = "F#4"; break;
            case 55: nuotti = "G4"; break;
            case 56: nuotti = "G#4"; break;
            case 57: nuotti = "A4"; break;
            case 58: nuotti = "A#4"; break;
            case 59: nuotti = "B4"; break;
            case 60: nuotti = "C5"; break;
            case 61: nuotti = "C#5"; break;
            case 62: nuotti = "D5"; break;
            case 63: nuotti = "D#5"; break;
            case 64: nuotti = "E5"; break;
            case 65: nuotti = "F5"; break;
            case 66: nuotti = "F#5"; break;
            case 67: nuotti = "G5"; break;
            case 68: nuotti = "G#5"; break;
            case 69: nuotti = "A5"; break;
            case 70: nuotti = "A#5"; break;
            case 71: nuotti = "B5"; break;
            case 72: nuotti = "C6"; break;
            case 73: nuotti = "C#6"; break;
            case 74: nuotti = "D6"; break;
            case 75: nuotti = "D#6"; break;
            case 76: nuotti = "E6"; break;
            case 77: nuotti = "F6"; break;
            case 78: nuotti = "F#6"; break;
            case 79: nuotti = "G6"; break;
            case 80: nuotti = "G#6"; break;
            case 81: nuotti = "A6"; break;
            case 82: nuotti = "A#6"; break;
            case 83: nuotti = "B6"; break;
            case 84: nuotti = "C7"; break;
            case 85: nuotti = "C#7"; break;
            case 86: nuotti = "D7"; break;
            case 87: nuotti = "D#7"; break;
            case 88: nuotti = "E7"; break;
            case 89: nuotti = "F7"; break;
            case 90: nuotti = "F#7"; break;
            case 91: nuotti = "G7"; break;
            case 92: nuotti = "G#7"; break;
            case 93: nuotti = "A7"; break;
            case 94: nuotti = "A#7"; break;
            case 95: nuotti = "B7"; break;
            case 96: nuotti = "C8"; break;
            case 97: nuotti = "C#8"; break;
            case 98: nuotti = "D8"; break;
            case 99: nuotti = "D#8"; break;
            case 100: nuotti = "E8"; break;
            case 101: nuotti = "F8"; break;
            case 102: nuotti = "F#8"; break;
            case 103: nuotti = "G8"; break;
            case 104: nuotti = "G#8"; break;
            case 105: nuotti = "A8"; break;
            case 106: nuotti = "A#8"; break;
            case 107: nuotti = "B8"; break;
            case 108: nuotti = "C9"; break;
            case 109: nuotti = "C#9"; break;
            case 110: nuotti = "D9"; break;
            case 111: nuotti = "D#9"; break;
            case 112: nuotti = "E9"; break;
            case 113: nuotti = "F9"; break;
            case 114: nuotti = "F#9"; break;
            case 115: nuotti = "G9"; break;
            case 116: nuotti = "G#9"; break;
            case 117: nuotti = "A9"; break;
            case 118: nuotti = "A#9"; break;
            case 119: nuotti = "B9"; break;
            case 120: nuotti = "C10"; break;
            case 121: nuotti = "C#10"; break;
            case 122: nuotti = "D10"; break;
            case 123: nuotti = "D#10"; break;
            case 124: nuotti = "E10"; break;
            case 125: nuotti = "F10"; break;
            case 126: nuotti = "F#10"; break;
            case 127: nuotti = "G10"; break;
            default: nuotti = "Ei määritelty"; break;
        }
        return nuotti;
    }
}
