package keimo.seikkailupeli.menu.asetusRuudut;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Renderöinti;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.menu.asetusRuudut.grafiikkatestiRuudut.GrafiikkaTestiMatriisit;
import keimo.seikkailupeli.menu.asetusRuudut.grafiikkatestiRuudut.GrafiikkaTestiShaderit;
import keimo.seikkailupeli.menu.asetusRuudut.grafiikkatestiRuudut.GrafiikkaTestiTekstuurit;
import keimo.seikkailupeli.menu.asetusRuudut.grafiikkatestiRuudut.GrafiikkaTestiValikko;
import keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut.ÄäniTestiMidi;
import keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut.ÄäniTestiRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut.ÄäniTestiValikko;
import keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut.ÄäniTestiWoof;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;

public class AsetusRuutu {
    private static int valinta = 0;
    private static int asetustenMäärä = 5;
    private static Renderöitävä otsikkoTekstuuri = Assets.annaTekstuuri("menu_main_asetukset");
    private static Renderöitävä osoitinKuvake = Assets.annaTekstuuri("menu_osoitin");
    private static Renderöitävä hyväksyTekstuuri = Assets.annaTekstuuri("menu_asetukset_takaisin");
    private static MenuKomponentti otsikkoLabel = new MenuKomponentti(1, 1f/8f, 0, 0.75f, otsikkoTekstuuri);
    private static MenuKomponentti osoitinLabel = new MenuKomponentti(1f/10f, 1f/10f, -0.6f, 0, osoitinKuvake, 10, 0, 0);
    private static Renderöitävä[] asetusTekstit;
    public static boolean grafiikatAlustettu = false;
    
    public static boolean pelissä = false;

    public static enum AsetusRuudut {
        ASETUSRUUTU,
        GRAFIIKKA,
        ÄÄNET,
        PELI,
        OHJAIMET,
        ÄÄNITESTI_VALIKKO,
        ÄÄNITESTI_PELIÄÄNET,
        ÄÄNITESTI_MIDI,
        ÄÄNITESTI_WOOF,
        GRAFIIKKATESTI_VALIKKO,
        GRAFIIKKATESTI_TEKSTUURIT,
        GRAFIIKKATESTI_SHADERIT,
        GRAFIIKKATESTI_MATRIISIT;
    }
    public static AsetusRuudut aktiivinenAsetusRuutu = AsetusRuudut.ASETUSRUUTU;

    public static void alustaGrafiikat() {
        if (!grafiikatAlustettu) {
            asetusTekstit = new Renderöitävä[] {
                new Teksti("Grafiikka", Color.white, 400, 48),
                new Teksti("Ääni", Color.white, 400, 48),
                new Teksti("Peli", Color.white,400, 48),
                new Teksti("Ohjaimet", Color.white,400, 48),
                hyväksyTekstuuri,
            };
            GrafiikkaTestiValikko.alustaGrafiikat();
            ÄäniTestiValikko.alustaGrafiikat();
            grafiikatAlustettu = true;
        }
    }

    public static void painaNäppäintä(String näppäin) {
        switch (aktiivinenAsetusRuutu) {
            case ASETUSRUUTU -> {
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
            case GRAFIIKKA -> {
                GrafiikkaAsetusRuutu.painaNäppäintä(näppäin);
            }
            case ÄÄNET -> {
                ÄäniAsetusRuutu.painaNäppäintä(näppäin);
            }
            case PELI -> {
                PeliAsetusRuutu.painaNäppäintä(näppäin);
            }
            case OHJAIMET -> {
                OhjainAsetusRuutu.painaNäppäintä(näppäin);
            }
            case ÄÄNITESTI_VALIKKO -> {
                ÄäniTestiValikko.painaNäppäintä(näppäin);
            }
            case ÄÄNITESTI_PELIÄÄNET -> {
                ÄäniTestiRuutu.painaNäppäintä(näppäin);
            }
            case ÄÄNITESTI_MIDI -> {
                ÄäniTestiMidi.painaNäppäintä(näppäin);
            }
            case ÄÄNITESTI_WOOF -> {
                ÄäniTestiWoof.painaNäppäintä(näppäin);
            }
            case GRAFIIKKATESTI_VALIKKO -> {
                GrafiikkaTestiValikko.painaNäppäintä(näppäin);
            }
            case GRAFIIKKATESTI_TEKSTUURIT -> {
                GrafiikkaTestiTekstuurit.painaNäppäintä(näppäin);
            }
            case GRAFIIKKATESTI_SHADERIT -> {
                GrafiikkaTestiShaderit.painaNäppäintä(näppäin);
            }
            case GRAFIIKKATESTI_MATRIISIT -> {
                GrafiikkaTestiMatriisit.painaNäppäintä(näppäin);
            }
        }
    }

    static void valitse(int valinta) {

        switch (valinta) {
            case 0 -> { // Grafiikka
                aktiivinenAsetusRuutu = AsetusRuudut.GRAFIIKKA;
            }
            case 1 -> { // Ääni
                aktiivinenAsetusRuutu = AsetusRuudut.ÄÄNET;
            }
            case 2 -> { // Peli
                aktiivinenAsetusRuutu = AsetusRuudut.PELI;
            }
            case 3 -> { // Ohjaimet
                aktiivinenAsetusRuutu = AsetusRuudut.OHJAIMET;
            }
            case 4 -> { // Takaisin
                hyväksy();
                peruuta();
            }
        }
    }

    private static void hyväksy() {

    }

    private static void peruuta() {
        if (pelissä) {
            Peli.syötteenTila = SyötteenTila.TOIMINTO;
            Renderöinti.siirrySeuraavaanRuutuun("peliruutu");
            Peli.pause = true;
        }
        else Renderöinti.siirrySeuraavaanRuutuun("valikkoruutu");
        valinta = 0;
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        alustaGrafiikat();
        switch (aktiivinenAsetusRuutu) {
            case ASETUSRUUTU -> {
                shader.bind();
                otsikkoLabel.renderöi(shader, window);

                osoitinLabel.muutaOffsetY(-1f/7.5f + (float)((2-valinta) + (valinta == asetustenMäärä-1 ? 0 : 1)) * (1f/5f));
                osoitinLabel.renderöiPyörivä(shader, window);

                for (int i = 0; i < asetustenMäärä; i++) {
                    float offsetY = -1f/7.5f + ((2-i) + (i == asetustenMäärä-1 ? 0 : 1)) * (1f/5f);
                    Komponentti.renderöiKomponenttiJaSkaalaa(shader, asetusTekstit[i], window, 1f/2f, 1f/15f, 1, 0, offsetY, 0);
                }
            }
            case GRAFIIKKA -> {
                GrafiikkaAsetusRuutu.render(shader, window);
            }
            case ÄÄNET -> {
                ÄäniAsetusRuutu.render(shader, window);
            }
            case PELI -> {
                PeliAsetusRuutu.render(shader, window);
            }
            case OHJAIMET -> {
                OhjainAsetusRuutu.render(shader, window);
            }
            case ÄÄNITESTI_VALIKKO -> {
                ÄäniTestiValikko.render(shader, window);
            }
            case ÄÄNITESTI_PELIÄÄNET -> {
                ÄäniTestiRuutu.render(shader, window);
            }
            case ÄÄNITESTI_MIDI -> {
                ÄäniTestiMidi.render(shader, window);
            }
            case ÄÄNITESTI_WOOF -> {
                ÄäniTestiWoof.render(shader, window);
            }
            case GRAFIIKKATESTI_VALIKKO -> {
                GrafiikkaTestiValikko.render(shader, window);
            }
            case GRAFIIKKATESTI_TEKSTUURIT -> {
                GrafiikkaTestiTekstuurit.render(shader, window);
            }
            case GRAFIIKKATESTI_SHADERIT -> {
                GrafiikkaTestiShaderit.render(shader, window);
            }
            case GRAFIIKKATESTI_MATRIISIT -> {
                GrafiikkaTestiMatriisit.render(shader, window);
            }
        }
    }
}
