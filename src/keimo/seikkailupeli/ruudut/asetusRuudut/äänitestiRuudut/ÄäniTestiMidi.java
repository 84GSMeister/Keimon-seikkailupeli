package keimo.seikkailupeli.ruudut.asetusRuudut.äänitestiRuudut;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenRenderöinti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.äänet.MidiToistin;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyöteLaitteet;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.ruudut.asetusRuudut.AsetusRuutu.AsetusRuudut;
import keimo.seikkailupeli.äänet.Musat;
import keimo.seikkailupeli.äänet.Äänet;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ÄäniTestiMidi {

    private static int valinta = 0;
    private static int asetustenMäärä = 3;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin2");
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static Teksti infoTeksti = new Teksti("info", Väri.white, 2500, 300);
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -0.85f, 0, osoitinKuvake, 0, 10, 0);
    private static MenuKomponentti infoTekstiLabel = new MenuKomponentti(1, 0.25f, 0, -0.75f, infoTeksti);

    private static Teksti asetusMidiraitaTeksti;
    private static Teksti asetusSoundfontTeksti;

    private static Teksti tilaMidiraitaTeksti;
    private static Teksti tilaSoundfontTeksti;

    private static List<File> midiTiedostot;
    private static List<File> soundfontTiedostot;
    private static int valittuMidi = 0;
    private static int valittuSoundfont = 0;

    private static String infoTekstiNäppäimistöString = "Äänitesti\n" +
    "Space: Toista, Esc: Pysäytä\n" +
    "Kokeile viedä MIDI-tiedostoja hakemistoon \"tiedostot/äänet/midi\"\n" +
    "tai Soundfont-tiedostoja hakemistoon \"tiedostot/äänet/soundfontit\"!";
    private static String infoTekstiOhjainString = "Äänitesti\n" +
    "A: Toista, B: Pysäytä\n" +
    "Kokeile viedä MIDI-tiedostoja hakemistoon \"tiedostot/äänet/midi\"\n" +
    "tai Soundfont-tiedostoja hakemistoon \"tiedostot/äänet/soundfontit\"!";

    public static void alusta() {
        listaaÄänet();
    }

    private static void listaaÄänet() {
        try {
            midiTiedostot = Stream.of(new File("tiedostot/äänet/midi/").listFiles())
                .filter(file -> !file.isDirectory() && ((file.getName().endsWith(".mid"))))
                .collect(Collectors.toList());
            soundfontTiedostot = Stream.of(new File("tiedostot/äänet/soundfontit/").listFiles())
                .filter(file -> !file.isDirectory() && ((file.getName().endsWith(".sf2"))))
                .collect(Collectors.toList());
        }
        catch (Exception e) {
            System.out.println("Virhe ladatessa ääniä");
            e.printStackTrace();
        }
    }

    public static void alustaGrafiikat() {
        asetusMidiraitaTeksti = new Teksti("Midi-raita", Väri.white, 400, 48);
        asetusSoundfontTeksti = new Teksti("Soundfont", Väri.white, 400, 48);
        tilaMidiraitaTeksti = new Teksti("Midi-raita", Väri.white, 900, 48);
        tilaSoundfontTeksti = new Teksti("Soundfont", Väri.white, 900, 48);
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
            case 0 -> { // Midi-raita
                if (kasvata) {
                    if (valittuMidi < midiTiedostot.size()-1) valittuMidi++;
                }
                else {
                    if (valittuMidi > 0) valittuMidi--;
                }
            }
            case 1 -> { // Soundfont
                if (kasvata) {
                    if (valittuSoundfont < soundfontTiedostot.size()) valittuSoundfont++;
                }
                else {
                    if (valittuSoundfont > 0) valittuSoundfont--;
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
        Musat.suljeMusa();
        MidiToistin.suljeMusat();
        File valittuMidiTiedosto = midiTiedostot.get(valittuMidi);
        if (valittuSoundfont > 0) {
            File valittuSoundfontTiedosto = soundfontTiedostot.get(valittuSoundfont-1);
            MidiToistin.toistaMidiMusa(valittuMidiTiedosto, valittuSoundfontTiedosto, PelinAsetukset.musaVolyymi);
        }
        else {
            MidiToistin.toistaMidiMusa(valittuMidiTiedosto, PelinAsetukset.musaVolyymi);
        }
    }
    
    public static void render(Shader shader, Ikkuna window) {
        shader.bind();
        
        tilaMidiraitaTeksti.päivitäTeksti(midiTiedostot.get(valittuMidi).getName());
        if (valittuSoundfont > 0) {
            tilaSoundfontTeksti.päivitäTeksti(soundfontTiedostot.get(valittuSoundfont-1).getName());
        }
        else {
            tilaSoundfontTeksti.päivitäTeksti("Järjestelmän vakio midi");
        }

        otsikkoLabel.renderöi(shader, window);

        osoitinLabel.muutaOffsetY(1f/3f - (float)((valinta) - (valinta == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f));
        osoitinLabel.renderöiPyörivä(shader, window);

        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, annaAsetusTekstuuri(i), window, 1f/2.5f, 1f/15f, 1, 1f/2.5f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            StaattinenRenderöinti.renderöiKomponenttiJaSkaalaa(shader, annaTilaTeksti(i), window, 1f/2.5f, 1f/15f, 1, 1f/2f +1f/10f, offsetY, 0);
        }

        if (Peli.viimeisinSyöteLaite == SyöteLaitteet.NÄPPÄIMISTÖ) {
            infoTeksti.päivitäTeksti(infoTekstiNäppäimistöString);
        }
        else if (Peli.viimeisinSyöteLaite == SyöteLaitteet.PELIOHJAIN) {
            infoTeksti.päivitäTeksti(infoTekstiOhjainString);
        }
        infoTekstiLabel.renderöi(shader, window);
    }

    private static Renderöitävä annaAsetusTekstuuri(int indeksi) {
        switch (indeksi) {
            case 0: return asetusMidiraitaTeksti;
            case 1: return asetusSoundfontTeksti;
            case 2: return hyväksyTekstuuri;
            default: return hyväksyTekstuuri;
        }
    }

    private static Renderöitävä annaTilaTeksti(int indeksi) {
        switch (indeksi) {
            case 0: return tilaMidiraitaTeksti;
            case 1: return tilaSoundfontTeksti;
            default: return tyhjäTekstuuri;
        }
    }
}
