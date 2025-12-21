package keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.keimoengine.ikkuna.Window;
import keimo.keimoengine.äänet.MidiToistin;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.äänet.Musat;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;
import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class MinipeliIkkunaPong {
    private static Shader peliShader = new Shader("shader");

    private static Tekstuuri kehysTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/minipeli_kehys.png");
    private static Tekstuuri alkuruutuTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pong/alkuruutu.png");
    private static Tekstuuri valkoinenTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/pong/valkoinen.png");
    private static Teksti teksti = new Teksti("Tähän tulee pong", Color.green, 200, 48);
    private static float siirtymä = 0;
    private static boolean valikko = true;

    public static int pelaajanSijY = 0;
    public static int vihollisenSijY = 0;
    private static Random random = new Random();
    private static boolean vihollisenSuuntaYlös = random.nextBoolean();

    public static int minX = -17;
    public static int maxX = 17;
    public static int minY = -12;
    public static int maxY = 9;
    private static int mailaXPelaaja = -15;
    private static int mailaXVihollinen = 15;
    private static int mailanKorkeus = 8;

    public static float pallonSijX = 0;
    public static float pallonSijY = 0;
    private static float nopeus = 0.5f;
    public static boolean pallonSuuntaOikea = false;
    public static boolean pallonSuuntaYlös = random.nextBoolean();

    private static int pelaajanPisteet = 0;
    private static int vihollisenPisteet = 0;

    public static void renderöiKehys(Window window) {
        float ruudunLeveys = window.getWidth();
        float ruudunKorkeus = window.getHeight();
        float scaleX = ruudunLeveys/3f;
        float scaleY = ruudunKorkeus/2.4f;
        float offsetY = ruudunKorkeus/12f;
        if (siirtymä < 1) siirtymä += 0.05;
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matKehys = new Matrix4f();
        window.getView().scale(1, matKehys);
        matKehys.translate(0, - offsetY, 0);
        matKehys.scale(scaleX * siirtymä, scaleY * siirtymä, 0);
        peliShader.setUniform("projection", matKehys);
        kehysTekstuuri.bind(0);
        Assets.getModel().render();
    }
    
    public static void renderöiIkkuna(Window window, Kamera kamera) {
        float ruudunLeveys = window.getWidth();
        float ruudunKorkeus = window.getHeight();
        if (siirtymä >= 1) {
            peliShader.bind();
            peliShader.setUniform("sampler", 0);
            peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

            if (valikko) {
                // Renderöi valikko
                float scaleXValikkoKuvake = ruudunLeveys/4f;
                float scaleYValikkoKuvake = ruudunKorkeus/4f;
                Matrix4f matValikkoKuvake = new Matrix4f();
                window.getView().scale(1, matValikkoKuvake);
                matValikkoKuvake.scale(scaleXValikkoKuvake, scaleYValikkoKuvake, 0);
                peliShader.setUniform("projection", matValikkoKuvake);
                alkuruutuTekstuuri.bind(0);
                Assets.getModel().render();
            }
            else {
                // Renderöi mailat
                float scaleXMaila = ruudunLeveys/64f;
                float scaleYMaila = ruudunKorkeus/64f * mailanKorkeus;
                float liikeSkaalaY = ruudunKorkeus/64f;
                Matrix4f matMailaVas = new Matrix4f();
                window.getView().scale(1, matMailaVas);
                matMailaVas.translate(-ruudunLeveys/4, liikeSkaalaY * pelaajanSijY, 0);
                matMailaVas.scale(scaleXMaila, scaleYMaila, 0);
                peliShader.setUniform("projection", matMailaVas);
                valkoinenTekstuuri.bind(0);
                Assets.getModel().render();
                Matrix4f matMailaOik = new Matrix4f();
                window.getView().scale(1, matMailaOik);
                matMailaOik.translate(ruudunLeveys/4, liikeSkaalaY * vihollisenSijY, 0);
                matMailaOik.scale(scaleXMaila, scaleYMaila, 0);
                peliShader.setUniform("projection", matMailaOik);
                valkoinenTekstuuri.bind(0);
                Assets.getModel().render();

                // Renderöi pallo
                float scaleXPallo = ruudunLeveys/64f;
                float scaleYPallo = ruudunKorkeus/48f;
                float liikeSkaalaXPallo = ruudunLeveys/64f;
                float liikeSkaalaYPallo = ruudunKorkeus/64f;
                Matrix4f matPallo = new Matrix4f();
                window.getView().scale(1, matPallo);
                matPallo.translate(liikeSkaalaXPallo * pallonSijX, liikeSkaalaYPallo * pallonSijY, 0);
                matPallo.scale(scaleXPallo, scaleYPallo, 0);
                peliShader.setUniform("projection", matPallo);
                valkoinenTekstuuri.bind(0);
                Assets.getModel().render();

                //Renderöi pisteet
                float scaleXPisteet = ruudunLeveys/16f;
                float scaleYPisteet = ruudunKorkeus/32f;
                float keskitysXPisteet = ruudunLeveys/12f;
                float offsetXPisteet = ruudunLeveys/32f;
                Matrix4f matTekstiPelaajanPisteet = new Matrix4f();
                window.getView().scale(1, matTekstiPelaajanPisteet);
                matTekstiPelaajanPisteet.translate(-keskitysXPisteet + offsetXPisteet, 0, 0);
                matTekstiPelaajanPisteet.scale(scaleXPisteet, scaleYPisteet, 0);
                peliShader.setUniform("projection", matTekstiPelaajanPisteet);
                teksti.päivitäTeksti("" + pelaajanPisteet);
                teksti.bind(0);
                Assets.getModel().render();
                Matrix4f matTekstiVihollisenPisteet = new Matrix4f();
                window.getView().scale(1, matTekstiVihollisenPisteet);
                matTekstiVihollisenPisteet.translate(keskitysXPisteet + offsetXPisteet, 0, 0);
                matTekstiVihollisenPisteet.scale(scaleXPisteet, scaleYPisteet, 0);
                peliShader.setUniform("projection", matTekstiVihollisenPisteet);
                teksti.päivitäTeksti("" + vihollisenPisteet);
                teksti.bind(0);
                Assets.getModel().render();
            }
        }
    }

    public static void pelaa() {
        if (!valikko) {
            if (pallonSuuntaOikea) pallonSijX += nopeus;
            else pallonSijX -= nopeus;
            if (pallonSuuntaYlös) pallonSijY += nopeus;
            else pallonSijY -= nopeus;
            if (pallonSijY <= minY) pallonSuuntaYlös = true;
            else if (pallonSijY >= maxX) pallonSuuntaYlös = false;

            int pelaajanYlä = pelaajanSijY + mailanKorkeus;
            int pelaajanAla = pelaajanSijY - mailanKorkeus;
            int vihollisenYlä = vihollisenSijY + mailanKorkeus;
            int vihollisenAla = vihollisenSijY - mailanKorkeus;

            // Liikuta vihollisen mailaa edestakaisin
            if (vihollisenSuuntaYlös) vihollisenSijY++;
            else vihollisenSijY--;
            if (vihollisenSijY <= minY) vihollisenSuuntaYlös = true;
            else if (vihollisenSijY >= maxY) vihollisenSuuntaYlös = false;

            // Pelaaja torjuu
            if (pallonSijX <= mailaXPelaaja && (pelaajanYlä > pallonSijY && pelaajanAla < pallonSijY)) {
                if (!pallonSuuntaOikea) Äänet.toistaSFX("Ping");
                pallonSuuntaOikea = true;
                
            }
            // Vihollinen torjuu
            else if (pallonSijX >= mailaXVihollinen && (vihollisenYlä > pallonSijY && vihollisenAla < pallonSijY)) {
                if (pallonSuuntaOikea) Äänet.toistaSFX("Pong");
                pallonSuuntaOikea = false;
            }

            // Pelaaja ei torju -> Vihollinen saa pisteen
            else if (pallonSijX <= minX) {
                pallonSuuntaOikea = true;
                pallonSuuntaYlös = random.nextBoolean();
                vihollisenPisteet++;
                pallonSijX = 0;
                pallonSijY = 0;
            }
            // Vihollinen ei torju -> Pelaaja saa pisteen
            else if (pallonSijX >= maxX) {
                pallonSuuntaOikea = false;
                pallonSuuntaYlös = random.nextBoolean();
                pelaajanPisteet++;
                pallonSijX = 0;
                pallonSijY = 0;
            }
        }
    }

    private static void nollaa() {
        pallonSuuntaOikea = false;
        pallonSuuntaYlös = random.nextBoolean();
        pallonSijX = 0;
        pallonSijY = 0;
        pelaajanPisteet = 0;
        vihollisenPisteet = 0;
    }

    public static void ohitaValikko() {
        valikko = false;
    }

    public static void avaaToimintoIkkuna() {
        valikko = true;
        nollaa();
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.MINIPELI_1;
        Musat.suljeMusa();
        Musat.toistaPeliMusa("minipeli_pong");
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
        Pelaaja.käyttöViive = 50;
        siirtymä = 0;
        MidiToistin.suljeMusat();
    }
}
