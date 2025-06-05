package keimo.keimoEngine.menu.asetusRuudut;

import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Animaatio;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.äänet.MidiToistin;
import keimo.keimoEngine.äänet.Musat;
import keimo.keimoEngine.äänet.Äänet;

import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joml.Matrix4f;

public class ÄäniTestiRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 5;
    private static Shader asetusRuutuShader = new Shader("staattinen");
    private static Tekstuuri otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_asetukset.png");
    private static Animaatio osoitinKuvake = new Animaatio(30, "tiedostot/kuvat/menu/main_osoitin.gif");
    private static Tekstuuri tyhjäTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");

    private static Teksti asetusÄäniPankkiTeksti = new Teksti("Äänipankki", Color.white, 200, 16);
    private static Teksti asetusÄäniValintaTeksti = new Teksti("Valitse ääni", Color.white, 200, 16);
    private static Teksti asetusNuottiTeksti = new Teksti("Taajuus", Color.white, 200, 16);
    private static Teksti asetusWoofTeksti = new Teksti("Woof", Color.white, 200, 16);
    private static Tekstuuri hyväksyTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/asetukset_takaisin.png");

    private static Teksti tilaÄäniPankkiTeksti = new Teksti("0", Color.white, 300, 16);
    private static Teksti tilaÄäniValintaTeksti = new Teksti("0", Color.white, 400, 16);
    private static Teksti tilaNuottiTeksti = new Teksti("50", Color.white, 300, 16);
    private static Teksti tilaWoofTeksti = new Teksti("Ei", Color.white, 300, 16);

    private static int valittuÄäni = 0;
    private static int taajuus = 64;
    private static boolean woof = false;
    private static List<File> ääniTiedostot;
    private static List<File> musaTiedostot;
    private static List<File> udoHaukkuuTiedostot;
    private static List<File> tölkkiTiedostot;
    private static List<File> woofTiedostot;

    private static Teksti infoTeksti = new Teksti("info", Color.white, 700, 100);
    private static String infoTekstiString = "Äänitesti\n " +
    "Space: Toista\n " +
    "Esc: Pysäytä ";

    private enum Äänipankit {
        PELIÄÄNET,
        PELIMUSAT,
        UDO_HAUKKUU,
        TÖLKKI,
        WOOF;
    }
    private static Äänipankit äänipankki = Äänipankit.PELIÄÄNET;
    private static int valittuÄänipankki;

    public static void alusta() {
        Musat.suljeMusa();
        listaaÄänet();
        tilaÄäniPankkiTeksti.päivitäTeksti(äänipankki.toString());
        tilaÄäniValintaTeksti.päivitäTeksti(ääniTiedostot.get(valittuÄäni).getName());
    }

    private static void listaaÄänet() {
        ääniTiedostot = Stream.of(new File("tiedostot/äänet/").listFiles())
            .filter(file -> !file.isDirectory() && ((file.getName().endsWith(".wav")) || (file.getName().endsWith(".mp3")) || (file.getName().endsWith(".ogg"))))
            .collect(Collectors.toList());
        musaTiedostot = Stream.of(new File("tiedostot/musat/").listFiles())
            .filter(file -> !file.isDirectory() && ((file.getName().endsWith(".wav")) || (file.getName().endsWith(".mp3")) || (file.getName().endsWith(".ogg"))))
            .collect(Collectors.toList());
        udoHaukkuuTiedostot = Stream.of(new File("tiedostot/musat/udo_haukkuu/").listFiles())
            .filter(file -> !file.isDirectory() && ((file.getName().endsWith(".wav")) || (file.getName().endsWith(".mp3")) || (file.getName().endsWith(".ogg"))))
            .collect(Collectors.toList());
        tölkkiTiedostot = Stream.of(new File("tiedostot/äänet/tölkki/").listFiles())
            .filter(file -> !file.isDirectory() && ((file.getName().endsWith(".wav")) || (file.getName().endsWith(".mp3")) || (file.getName().endsWith(".ogg"))))
            .collect(Collectors.toList());
        woofTiedostot = Stream.of(new File("tiedostot/äänet/woof/").listFiles())
            .filter(file -> !file.isDirectory() && ((file.getName().endsWith(".wav")) || (file.getName().endsWith(".mp3")) || (file.getName().endsWith(".ogg"))))
            .collect(Collectors.toList());
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
                MidiToistin.suljeMusat();
            }
        }
    }

    static void säädäAsetusta(int valinta, boolean kasvata) {
        switch (valinta) {
            case 0 -> { // Äänipankki
                if (kasvata) {
                    if (valittuÄänipankki < Äänipankit.values().length-1) valittuÄänipankki++;
                }
                else {
                    if (valittuÄänipankki > 0) valittuÄänipankki--;
                }
                valittuÄäni = 0;
                switch (valittuÄänipankki) {
                    case 0: äänipankki = Äänipankit.PELIÄÄNET; tilaÄäniValintaTeksti.päivitäTeksti(ääniTiedostot.get(valittuÄäni).getName()); break;
                    case 1: äänipankki = Äänipankit.PELIMUSAT; tilaÄäniValintaTeksti.päivitäTeksti(musaTiedostot.get(valittuÄäni).getName()); break;
                    case 2: äänipankki = Äänipankit.UDO_HAUKKUU; tilaÄäniValintaTeksti.päivitäTeksti(udoHaukkuuTiedostot.get(valittuÄäni).getName()); break;
                    case 3: äänipankki = Äänipankit.TÖLKKI; tilaÄäniValintaTeksti.päivitäTeksti(tölkkiTiedostot.get(valittuÄäni).getName()); break;
                    case 4: äänipankki = Äänipankit.WOOF; tilaÄäniValintaTeksti.päivitäTeksti(woofTiedostot.get(valittuÄäni).getName()); break;
                }
                tilaÄäniPankkiTeksti.päivitäTeksti(äänipankki.toString());
            }
            case 1 -> { // Valitse ääni
                int raja = 0;
                switch (äänipankki) {
                    case PELIÄÄNET: raja = ääniTiedostot.size()-1; break;
                    case PELIMUSAT: raja = musaTiedostot.size()-1; break;
                    case UDO_HAUKKUU: raja = udoHaukkuuTiedostot.size()-1; break;
                    case TÖLKKI: raja = tölkkiTiedostot.size()-1; break;
                    case WOOF: raja = woofTiedostot.size()-1; break;
                }
                if (kasvata) {
                    if (valittuÄäni < raja) valittuÄäni++;
                }
                else {
                    if (valittuÄäni > 0) valittuÄäni--;
                }
                switch (äänipankki) {
                    case PELIÄÄNET: tilaÄäniValintaTeksti.päivitäTeksti(ääniTiedostot.get(valittuÄäni).getName()); break;
                    case PELIMUSAT: tilaÄäniValintaTeksti.päivitäTeksti(musaTiedostot.get(valittuÄäni).getName()); break;
                    case UDO_HAUKKUU: tilaÄäniValintaTeksti.päivitäTeksti(udoHaukkuuTiedostot.get(valittuÄäni).getName()); break;
                    case TÖLKKI: tilaÄäniValintaTeksti.päivitäTeksti(tölkkiTiedostot.get(valittuÄäni).getName()); break;
                    case WOOF: tilaÄäniValintaTeksti.päivitäTeksti(woofTiedostot.get(valittuÄäni).getName()); break;
                }
                tilaÄäniPankkiTeksti.päivitäTeksti(äänipankki.toString());
                
            }
            case 2 -> { // Taajuus
                if (kasvata) {
                    if (taajuus < 127) taajuus++;
                }
                else {
                    if (taajuus > 0) taajuus--;
                }
            }
            case 3 -> { // Woof
                woof = !woof;
            }
            default -> {

            }
        }
    }

    static void hyväksy(int valinta) {
        if (valinta == 0 || valinta == 1 || valinta == 2 || valinta == 3) {
            toistaValittuÄäni();
        }
        if (valinta == 4) {
            MidiToistin.suljeMusat();
            KeimoEngine.valitseAktiivinenRuutu("asetusruutu_ääni");
            Äänet.toistaSFX("Valinta");
        }
    }

    private static void toistaValittuÄäni() {
        float sampleRate = (float)(44100 * Math.pow(2d, (((double)taajuus-64d)/12d)));
        toistaValittuÄäni(sampleRate);
    }

    public static void toistaValittuÄäni(float sampleRate) {
        switch (äänipankki) {
            case PELIÄÄNET -> {
                File ääniTiedosto = ääniTiedostot.get(valittuÄäni);
                MidiToistin.toistaResamplattavaÄäni(sampleRate, ääniTiedosto, false, false);
            }
            case PELIMUSAT -> {
                MidiToistin.suljeMusat();
                File ääniTiedosto = musaTiedostot.get(valittuÄäni);
                MidiToistin.toistaResamplattavaÄäni(sampleRate, ääniTiedosto, true, woof);
            }
            case UDO_HAUKKUU -> {
                MidiToistin.suljeMusat();
                File ääniTiedosto = udoHaukkuuTiedostot.get(valittuÄäni);
                MidiToistin.toistaResamplattavaÄäni(sampleRate, ääniTiedosto, true, woof);
            }
            case TÖLKKI -> {
                File ääniTiedosto = tölkkiTiedostot.get(valittuÄäni);
                MidiToistin.toistaResamplattavaÄäni(sampleRate, ääniTiedosto, false, false);
            }
            case WOOF -> {
                File ääniTiedosto = woofTiedostot.get(valittuÄäni);
                MidiToistin.toistaResamplattavaÄäni(sampleRate, ääniTiedosto, false, false);
            }
        }
    }

    public static void render(Window window) {
        asetusRuutuShader.bind();
        float scaleXOtsikko = 1;
        if (window.getWidth() != 0 && window.getWidth() != 0) {
            scaleXOtsikko = window.getWidth()/ (window.getWidth()*2/window.getHeight());
        }
        float scaleYOtsikko = window.getHeight()/16;
        float scaleXValinnat = window.getWidth()/4;
        float scaleYValinnat = window.getHeight()/30;
        float scaleXOsoitin = window.getHeight()/20;
        float scaleYOsoitin = scaleYValinnat;
        float scaleXInfo = scaleXOtsikko;
        float scaleYInfo = window.getHeight()/8;
        float keskitysX = window.getWidth()/4;
        float offsetYValinta = window.getHeight()/15;

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(0, scaleYOtsikko*6f, 0);
        matOtsikko.scale(scaleXOtsikko, scaleYOtsikko, 0);
        asetusRuutuShader.setUniform("projection", matOtsikko);
        otsikkoTekstuuri.bind(0);
        Assets.getModel().render();

        for (int i = 0; i < asetustenMäärä; i++) {
            Matrix4f matOsoitin = new Matrix4f();
            window.getView().scale(1, matOsoitin);
            if (i == asetustenMäärä-1) matOsoitin.translate(-scaleXOsoitin - keskitysX*1.5f, scaleYOtsikko*4f -1.2f*i*offsetYValinta, 0);
            else matOsoitin.translate(-scaleXOsoitin - keskitysX*1.5f, scaleYOtsikko*4f -i*offsetYValinta, 0);
            matOsoitin.scale(scaleXOsoitin, scaleYOsoitin, 0);
            asetusRuutuShader.setUniform("projection", matOsoitin);
            if (valinta == i) osoitinKuvake.bind(0);
            else tyhjäTekstuuri.bind(0);
            Assets.getModel().render();
        }

        for (int i = 0; i < asetustenMäärä; i++) {
            Matrix4f matValinta = new Matrix4f();
            window.getView().scale(1, matValinta);
            if (i == asetustenMäärä-1) matValinta.translate(scaleXValinnat - keskitysX*1.5f, scaleYOtsikko*4f -1.2f*i*offsetYValinta, 0);
            else matValinta.translate(scaleXValinnat - keskitysX*1.5f, scaleYOtsikko*4f -i*offsetYValinta, 0);
            matValinta.scale(scaleXValinnat, scaleYValinnat, 0);
            asetusRuutuShader.setUniform("projection", matValinta);
            switch (i) {
                case 0: asetusÄäniPankkiTeksti.bind(0); break;
                case 1: asetusÄäniValintaTeksti.bind(0); break;
                case 2: asetusNuottiTeksti.bind(0); break;
                case 3: asetusWoofTeksti.bind(0); break;
                default: hyväksyTekstuuri.bind(0); break;
            }
            Assets.getModel().render();
        }

        if (äänipankki == Äänipankit.WOOF) tilaNuottiTeksti.päivitäTeksti("" + haeNuotti(taajuus));
        else tilaNuottiTeksti.päivitäTeksti("" + (float)(44100 * Math.pow(2d, (((double)taajuus-64d)/12d))));
        tilaWoofTeksti.päivitäTeksti(woof ? "Kyllä" : "Ei");
        
        for (int i = 0; i < asetustenMäärä; i++) {
            Matrix4f matStatus = new Matrix4f();
            window.getView().scale(1, matStatus);
            matStatus.translate(scaleXValinnat + keskitysX/8, scaleYOtsikko*4f -i*offsetYValinta, 0);
            matStatus.scale(scaleXValinnat, scaleYValinnat, 0);
            asetusRuutuShader.setUniform("projection", matStatus);
            switch (i) {
                case 0: tilaÄäniPankkiTeksti.bind(0); break;
                case 1: tilaÄäniValintaTeksti.bind(0); break;
                case 2: tilaNuottiTeksti.bind(0); break;
                case 3: tilaWoofTeksti.bind(0); break;
                default: tyhjäTekstuuri.bind(0); break;
            }
            Assets.getModel().render();
        }

        Matrix4f matInfoTeksti = new Matrix4f();
        window.getView().scale(1, matInfoTeksti);
        matInfoTeksti.translate(0, -window.getHeight()/2+scaleYInfo, 0);
        matInfoTeksti.scale(scaleXInfo, scaleYInfo, 0);
        asetusRuutuShader.setUniform("projection", matInfoTeksti);
        infoTeksti.päivitäTeksti(infoTekstiString, false, 58);
        infoTeksti.bind(0);
        Assets.getModel().render();
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
