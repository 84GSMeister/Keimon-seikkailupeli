package keimo.seikkailupeli.gui.toimintoIkkunat;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.Latauspalkki;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyöteLaitteet;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Vesiämpäri;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.Ämpärikone;
import keimo.seikkailupeli.toiminnot.Dialogit;

import java.awt.Color;
import java.util.Random;

public class ÄmpäriJonoIkkuna {
    private static Renderöitävä kehysTekstuuri = Assets.annaTekstuuri("ikkuna_kehys_musta");
    private static Renderöitävä taustaTekstuuri = Assets.annaTekstuuri("toimintoikkuna_ämpärijono");
    private static Teksti ämpäriJonoTeksti = new Teksti("Jonossa", Color.black, 900, 48);
    private static Teksti ohjeTeksti = new Teksti("Poistu", Color.black, 500, 48);
    private static Tekstuuri surunaamaTekstuuri;
    private static Tekstuuri keimoTekstuuri;
    private static StaattinenKomponentti kehysKomponentti = new StaattinenKomponentti(0.5f, 0.5f, 0, 0, kehysTekstuuri);
    private static StaattinenKomponentti taustaLabel = new StaattinenKomponentti(0.4f, 0.4f, 0, 0, taustaTekstuuri);
    private static StaattinenKomponentti OhjeLabel;
    private static Latauspalkki edistymispalkki = new Latauspalkki(1f/3f, 0.05f, 0.0f, -0.2f);
    private static StaattinenKomponentti jonossaLabel;

    private static Random r = new Random();
    private static String statusTeksti = "Jonotetaan";
    private static int ämpäriJononPituus;
    private static int ämpäriJononPituusAlussa;
    public static boolean keskeytetty = false;
    private static boolean valmis = false;
    public static Ämpärikone ämpärikone;
    private static float siirräY;
    private static float venytäX;

    public static void alustaGrafiikat() {
        ämpäriJonoTeksti = new Teksti("Jonossa", Color.black, 900, 48);
        ohjeTeksti = new Teksti("Poistu", Color.black, 500, 48);
        surunaamaTekstuuri = new Tekstuuri("tiedostot/kuvat/surunaama.png");
        keimoTekstuuri = new Tekstuuri("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
        OhjeLabel = new StaattinenKomponentti(0.15f, 0.05f, 0.15f, 0.2f, ohjeTeksti);
        jonossaLabel = new StaattinenKomponentti(1f/3f, 0.05f, 0, -0.1f, ämpäriJonoTeksti);
    }
    
    public static void renderöiIkkuna(Shader peliShader, Ikkuna window) {
        peliShader.bind();
        peliShader.nollaaShaderEfektit();
        if (siirräY > 0) siirräY -= 0.05f;
        if (venytäX < 1) venytäX += 0.05f;

        kehysKomponentti.muutaKokoa(0.5f * venytäX, 0.5f, 0, 0);
        kehysKomponentti.renderöi(peliShader, window);
        taustaLabel.muutaKokoa(0.4f * venytäX, 0.4f, 0, 0);
        taustaLabel.renderöi(peliShader, window);

        if (venytäX >= 1) {
            ohjeTeksti.päivitäTeksti((Peli.viimeisinSyöteLaite == SyöteLaitteet.NÄPPÄIMISTÖ ? "Space: " : "A: ") + "Poistu");
            OhjeLabel.renderöi(peliShader, window);
            edistymispalkki.päivitäLatausProsentti(100f * ((float)(ämpäriJononPituusAlussa-ämpäriJononPituus) / (float)ämpäriJononPituusAlussa));
            edistymispalkki.renderöi(peliShader, window);
            ämpäriJonoTeksti.päivitäTeksti(statusTeksti);
            jonossaLabel.renderöi(peliShader, window);
        }
    }

    public static void avaaToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.ÄMPÄRIJONO;
        venytäX = 0;
        if (Peli.annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY] instanceof Ämpärikone) {
            valmis = false;
            ämpäriJononPituus = r.nextInt(1000, 4000);
            ämpäriJononPituusAlussa = ämpäriJononPituus;
            keskeytetty = false;
        }
        else suljeToimintoIkkuna();
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
    }

    public static void tarkistaTila() {
        if (valmis) {
            valmis();
        }
    }

    public enum VirheenTyyppi {
        PAKKAUS,
        MUOTO,
        KÄSI,
        MERKKI;
    }
    
    public static void simuloiÄmpärijono() {
        if (ämpäriJononPituus > 0 && !keskeytetty) {
            statusTeksti = "Jonossa sijalla: " + ämpäriJononPituus;
            ämpäriJononPituus -= r.nextInt(10);
        }
        else valmis = true;
    }

    private static void valmis() {
        try {
            valmis = false;
            Peli.pauseDialogi = false;
            Peli.valintaDialogi = false;
            suljeToimintoIkkuna();
            if (keskeytetty) {
                Dialogit.avaaDialogi(keimoTekstuuri, "Ei kiinosta  virhe", "Keimo");
            }
            else if (Pelaaja.annaEsineidenMäärä() < 6) {
                Vesiämpäri ämpäri = new Vesiämpäri(0, 0);
                Pelaaja.annaEsine(ämpäri);
                Dialogit.avaaDialogi(ämpäri.annaDialogiTekstuuri(), "Sait uuden " + ämpäri.annaNimiSijamuodossa("genetiivi"), "Ämpärijono");
            }
            else {
                Dialogit.avaaDialogi(surunaamaTekstuuri, "Menit jonottamaan ämpäriä, vaikka sinun tavaraluettelosi on täynnä. Miltä nyt tuntuu?", "Ämpärijono");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
