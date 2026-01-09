package keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.äänet.PeliääniToistin;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.Peli.SyöteLaitteet;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.äänet.Musat;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joml.Matrix4f;

public class ÄäniTestiRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 5;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä tyhjäTekstuuri = Assets.annaTekstuuri("menu_tyhjä");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);

    private static Teksti asetusÄäniPankkiTeksti = new Teksti("Äänipankki", Color.white, 600, 48);
    private static Teksti asetusÄäniValintaTeksti = new Teksti("Valitse ääni", Color.white, 600, 48);
    private static Teksti asetusNuottiTeksti = new Teksti("Taajuus", Color.white, 600, 48);
    private static Teksti asetusTakaperinTeksti = new Teksti("Takaperin", Color.white, 600, 48);

    private static Teksti tilaÄäniPankkiTeksti = new Teksti("0", Color.white, 800, 48);
    private static Teksti tilaÄäniValintaTeksti = new Teksti("0", Color.white, 1000, 48);
    private static Teksti tilaNuottiTeksti = new Teksti("50", Color.white, 1200, 48);
    private static Teksti tilaTakaperinTeksti = new Teksti("Ei", Color.white, 800, 48);

    private static int valittuÄäni = 0;
    private static int taajuus = 64;
    private static boolean takaperin = false;
    private static List<File> ääniTiedostot;
    private static List<File> musaTiedostot;
    private static List<File> udoHaukkuuTiedostot;
    private static List<File> tölkkiTiedostot;
    private static List<File> woofTiedostot;

    private static DecimalFormat kolmeDesimaalia = new DecimalFormat("###.###");
    private static Teksti infoTeksti = new Teksti("info", Color.white, 2000, 300);
    private static String infoTekstiNäppäimistöString = "Äänitesti\n " +
    "Space: Toista\n " +
    "Esc: Pysäytä ";
    private static String infoTekstiOhjainString = "Äänitesti\n " +
    "A: Toista\n " +
    "B: Pysäytä ";

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
        switch (valittuÄänipankki) {
            case 0: äänipankki = Äänipankit.PELIÄÄNET; tilaÄäniValintaTeksti.päivitäTeksti(ääniTiedostot.get(valittuÄäni).getName()); break;
            case 1: äänipankki = Äänipankit.PELIMUSAT; tilaÄäniValintaTeksti.päivitäTeksti(musaTiedostot.get(valittuÄäni).getName()); break;
            case 2: äänipankki = Äänipankit.UDO_HAUKKUU; tilaÄäniValintaTeksti.päivitäTeksti(udoHaukkuuTiedostot.get(valittuÄäni).getName()); break;
            case 3: äänipankki = Äänipankit.TÖLKKI; tilaÄäniValintaTeksti.päivitäTeksti(tölkkiTiedostot.get(valittuÄäni).getName()); break;
            case 4: äänipankki = Äänipankit.WOOF; tilaÄäniValintaTeksti.päivitäTeksti(woofTiedostot.get(valittuÄäni).getName()); break;
        }
    }

    private static void listaaÄänet() {
        try {
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
        catch (Exception e) {
            System.out.println("Virhe ladatessa ääniä");
            e.printStackTrace();
        }
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
                PeliääniToistin.suljeÄänet();
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
            case 3 -> { // Takaperin
                takaperin = !takaperin;
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
            PeliääniToistin.suljeÄänet();
            KeimoEngine.valitseAktiivinenRuutu("asetusruutu_äänitesti");
            Äänet.toistaSFX("Valinta");
        }
    }

    private static void toistaValittuÄäni() {
        float sampleRate = (float)(44100 * Math.pow(2d, (((double)taajuus-64d)/12d)));
        toistaValittuÄäni(sampleRate);
    }

    public static void toistaValittuÄäni(float sampleRate) {
        // Vaihda funktiot päikseen jos haluat ohittaa säielogiikan ja toistaa suoraan pääsäikeessä (ongelmatapauksissa tms.).
        switch (äänipankki) {
            case PELIÄÄNET -> {
                File ääniTiedosto = ääniTiedostot.get(valittuÄäni);
                //PeliääniToistin.toistaResamplattavaÄäni(sampleRate, ääniTiedosto, PelinAsetukset.ääniVolyymi, 0, false);
                Äänet.toistaÄäni(ääniTiedosto, PelinAsetukset.ääniVolyymi, 0, false, sampleRate, false, takaperin);
            }
            case PELIMUSAT -> {
                PeliääniToistin.suljeÄänet();
                File ääniTiedosto = musaTiedostot.get(valittuÄäni);
                //PeliääniToistin.toistaResamplattavaÄäni(sampleRate, ääniTiedosto, PelinAsetukset.musaVolyymi, 0, true);
                Musat.toistaPeliMusa(ääniTiedosto, PelinAsetukset.ääniVolyymi, 0, sampleRate, true, takaperin);
            }
            case UDO_HAUKKUU -> {
                PeliääniToistin.suljeÄänet();
                File ääniTiedosto = udoHaukkuuTiedostot.get(valittuÄäni);
                //PeliääniToistin.toistaResamplattavaÄäni(sampleRate, ääniTiedosto, PelinAsetukset.musaVolyymi, 0, true);
                Musat.toistaPeliMusa(ääniTiedosto, PelinAsetukset.ääniVolyymi, 0, sampleRate, true, takaperin);
            }
            case TÖLKKI -> {
                File ääniTiedosto = tölkkiTiedostot.get(valittuÄäni);
                //PeliääniToistin.toistaResamplattavaÄäni(sampleRate, ääniTiedosto, PelinAsetukset.ääniVolyymi, 0, false);
                Äänet.toistaÄäni(ääniTiedosto, PelinAsetukset.ääniVolyymi, 0, false, sampleRate, false, takaperin);
            }
            case WOOF -> {
                File ääniTiedosto = woofTiedostot.get(valittuÄäni);
                //PeliääniToistin.toistaResamplattavaÄäni(sampleRate, ääniTiedosto, PelinAsetukset.ääniVolyymi, 0, false);
                Äänet.toistaÄäni(ääniTiedosto, PelinAsetukset.ääniVolyymi, 0, false, sampleRate, false, takaperin);
            }
        }
    }

    public static void render(Shader asetusRuutuShader, Ikkuna window) {
        float scaleXOtsikko = 1;
        if (window.getWidth() != 0 && window.getWidth() != 0) {
            scaleXOtsikko = window.getWidth()/ (window.getWidth()*2/window.getHeight());
        }
        float scaleXInfo = scaleXOtsikko;
        float scaleYInfo = window.getHeight()/8;

        float muunnettuTaajuus = (float)(44100 * Math.pow(2d, (((double)taajuus-64d)/12d)));
        int nuottiMuutos = (taajuus-64);
        String kerroinMuutos = kolmeDesimaalia.format(muunnettuTaajuus/44100f);
        tilaNuottiTeksti.päivitäTeksti("" + muunnettuTaajuus + " (" + nuottiMuutos + " nuottia) X" + kerroinMuutos);
        tilaTakaperinTeksti.päivitäTeksti(takaperin ? "Kyllä" : "Ei");

        otsikkoLabel.renderöi(asetusRuutuShader, window);
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponentti(asetusRuutuShader, annaOsoitinKuvake(i), window, 1f/12f, 1f/12f, 1, -1f/12f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponentti(asetusRuutuShader, annaAsetusTekstuuri(i), window, 1f/2.5f, 1f/15f, 1, 1f/2.5f -3f/4f, offsetY, 0);
        }
        for (int i = 0; i < asetustenMäärä; i++) {
            float offsetY = 1f/3f - (float)((i) - (i == asetustenMäärä-1 ? 0 : 1)) * (1f/7.5f);
            Komponentti.renderöiKomponentti(asetusRuutuShader, annaTilaTeksti(i), window, 1f/2.25f, 1f/15f, 1, 1f/2f, offsetY, 1f/10f);
        }


        Matrix4f matInfoTeksti = new Matrix4f();
        window.getView().scale(1, matInfoTeksti);
        matInfoTeksti.translate(0, -window.getHeight()/2+scaleYInfo, 0);
        matInfoTeksti.scale(scaleXInfo, scaleYInfo, 0);
        asetusRuutuShader.setUniform("projection", matInfoTeksti);
        if (Peli.viimeisinSyöteLaite == SyöteLaitteet.NÄPPÄIMISTÖ) {
            infoTeksti.päivitäTeksti(infoTekstiNäppäimistöString, 0, 58);
        }
        else if (Peli.viimeisinSyöteLaite == SyöteLaitteet.PELIOHJAIN) {
            infoTeksti.päivitäTeksti(infoTekstiOhjainString, 0, 58);
        }
        infoTeksti.bind(0);
        Assets.getModel().render();
    }

    private static Renderöitävä annaAsetusTekstuuri(int indeksi) {
        switch (indeksi) {
            case 0: return asetusÄäniPankkiTeksti;
            case 1: return asetusÄäniValintaTeksti;
            case 2: return asetusNuottiTeksti;
            case 3: return asetusTakaperinTeksti;
            default: return hyväksyTekstuuri;
        }
    }

    private static Renderöitävä annaOsoitinKuvake(int valikkoElementti) {
        if (valikkoElementti == valinta) return osoitinKuvake;
        else return tyhjäTekstuuri;
    }

    private static Renderöitävä annaTilaTeksti(int indeksi) {
        switch (indeksi) {
            case 0: return tilaÄäniPankkiTeksti;
            case 1: return tilaÄäniValintaTeksti;
            case 2: return tilaNuottiTeksti;
            case 3: return tilaTakaperinTeksti;
            default: return tyhjäTekstuuri;
        }
    }
}
