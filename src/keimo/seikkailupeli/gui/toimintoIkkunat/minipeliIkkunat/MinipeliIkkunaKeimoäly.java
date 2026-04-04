package keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.äänet.Musat;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class MinipeliIkkunaKeimoäly {

    private static Renderöitävä kehysTekstuuri = Assets.annaTekstuuri("minipeli_kehys");
    private static StaattinenKomponentti kehysKomponentti = new StaattinenKomponentti(2f/3f, 2f/2.4f, 0, -1f/6f, kehysTekstuuri);
    private static Renderöitävä taustaTekstuuri = Assets.annaTekstuuri("minipeli_keimoäly_tausta");
    private static StaattinenKomponentti taustaKomponentti = new StaattinenKomponentti(1f/2f, 1f/2f, 0, 0, taustaTekstuuri);
    private static Renderöitävä puhekuplaVastausTekstuuri = Assets.annaTekstuuri("minipeli_keimoäly_puhekupla_vastaus");
    private static Renderöitävä puhekuplaKysymysTekstuuri = Assets.annaTekstuuri("minipeli_keimoäly_puhekupla_kysymys");
    private static Teksti aloitusTeksti;
    private static Teksti teksti;
    private static float siirtymä = 0;
    private static int viestiAjastin = 0;
    private static ArrayList<String> pyyntöViestit = new ArrayList<>();
    private static ArrayList<String> vastausViestit = new ArrayList<>();
    private static String syöteTekstiString = "";
    private static Teksti syöteTeksti;
    private static int scroll = 0;
    private static Random random = new Random();
    private static String[] vastaukset = new String[] {
        "En tiedä.",
        "Tuo kysymys on minulle liian vaikea.",
        "En osaa vastata tuohon kysymykseen.",
        "En tiedä vastausta tuohon.",
        "En osaa auttaa tuossa.",
        "Tuohon en voi mitään sanoa.",
        "En voi auttaa asian kanssa.",
        "Valitettavasti en osaa vastata.",
        "En pysty vastaamaan tuohon.",
        "[#+$0}}}!\"{'€%=/@*‰(-;?"
    };

    private static void alustaGrafiikat() {
        if (teksti == null) {
            teksti = new Teksti("Tähän tulee Keimoäly", Color.black, 300, 48);
            aloitusTeksti = new Teksti("Hei! Olen Keimoäly.", Color.green, 2400, 96);
            syöteTeksti = new Teksti(syöteTekstiString, Color.red, 1000, 48);
        }
    }

    public static void renderöiKehys(Ikkuna window, Shader peliShader) {
        alustaGrafiikat();
        if (siirtymä < 1) siirtymä += 0.05;
        peliShader.bind();
        peliShader.nollaaShaderEfektit();
        //peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
        kehysKomponentti.muutaKokoa(2f/3f * siirtymä, 2f/2.4f * siirtymä, 0, -1f/6f);
        kehysKomponentti.renderöi(peliShader, window);
    }

    public static void renderöiIkkuna(Ikkuna window, Shader peliShader) {
        if (siirtymä >= 1) {
            taustaKomponentti.renderöi(peliShader, window);

            // Renderöi aloitusteksti
            float scaleX = 1f/2f;
            float scaleY = 0.05f;
            float offsetX = 0;
            float offsetY = 0.45f;
            aloitusTeksti.päivitäTeksti("Hei! Olen Keimo-Äly, huipputyhmä keinoälyalgoritmi. Voit kysyä minulta mitä vain - todennäköisesti en osaa vastata.", 2);
            Komponentti.renderöiKomponentti(peliShader, aloitusTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);

            // Renderöi vastaustekstit
            scaleX = 1f/4f;
            offsetX = -1f/4f;
            int alkuindeksi = 0;
            if (scroll > 2) alkuindeksi = scroll - 2;
            for (int i = alkuindeksi; i < vastausViestit.size(); i++) {
                scaleY = 0.05f;
                offsetY = 0.25f - i * scaleY * 4 + (scroll-2) * scaleY * 4;
                teksti.päivitäTeksti(vastausViestit.get(i), 1, 10);
                Komponentti.renderöiKomponentti(peliShader, puhekuplaVastausTekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
                scaleY = 0.03f;
                Komponentti.renderöiKomponentti(peliShader, teksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
            }

            // Renderöi pyyntötekstit
            offsetX = 1f/4f;
            for (int i = alkuindeksi; i < pyyntöViestit.size(); i++) {
                scaleY = 0.05f;
                offsetY = 0.35f - i * scaleY * 4 + (scroll-2) * scaleY * 4;
                teksti.päivitäTeksti(pyyntöViestit.get(i), 1, 10);
                Komponentti.renderöiKomponentti(peliShader, puhekuplaKysymysTekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
                scaleY = 0.03f;
                Komponentti.renderöiKomponentti(peliShader, teksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
            }

            if (viestiAjastin > 0) {
                if (viestiAjastin == 1) vastausViestit.add(vastaukset[random.nextInt(vastaukset.length)]);
                viestiAjastin--;
            }
            if (pyyntöViestit.size() > 2) scroll = pyyntöViestit.size() - 2;
            else scroll = 0;

            // Renderöi syötettävä teksti
            scaleX = 0.45f;
            scaleY = 0.05f;
            offsetX = 0;
            offsetY = -0.45f;
            syöteTeksti.päivitäTeksti(syöteTekstiString + "_", 0);
            Komponentti.renderöiKomponentti(peliShader, syöteTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
        }
    }

    public static void lisääKirjainSyötteeseen(String kirjain) {
        if (syöteTekstiString.length() < 28) {
            syöteTekstiString += kirjain;
        }
    }

    public static void pyyhiKirjain() {
        if (syöteTekstiString.length() > 0) {
            syöteTekstiString = syöteTekstiString.substring(0, syöteTekstiString.length()-1);
        }
    }

    public static void lähetäViesti() {
        if (viestiAjastin <= 0 && syöteTekstiString.length() > 0) {
            pyyntöViestit.add(syöteTekstiString);
            syöteTekstiString = "";
            viestiAjastin = random.nextInt(20, 90);
        }
    }

    private static void nollaa() {
        pyyntöViestit.clear();
        vastausViestit.clear();
    }

    public static void avaaToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.MINIPELI_KEIMOÄLY;
        Musat.suljeMusa();
        Musat.toistaPeliMusa("minipeli_keimoäly");
        nollaa();
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
        Pelaaja.käyttöViive = 50;
        siirtymä = 0;
        Äänet.suljeÄänet();
    }
}
