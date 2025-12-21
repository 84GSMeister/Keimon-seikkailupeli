package keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.ikkuna.Kamera;
import keimo.keimoengine.ikkuna.Window;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;

import java.awt.Color;
import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class MinipeliIkkunaOverflow {
    private static Shader peliShader = new Shader("shader");

    private static Tekstuuri kehysTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/minipelit/minipeli_kehys.png");
    private static Teksti teksti = new Teksti("Tähän tulee hassu laskuohjelma", Color.green, 200, 48);
    private static float siirräY = 600;
    private static boolean säikeetKäynnissä = false;

    private static Random random = new Random();
    private static byte luku8 = 0;
    private static short luku16 = 0;
    private static int luku32 = 0;
    private static long luku64 = 0L;
    private static long overflow8 = 0L;
    private static long overflow16 = 0L;
    private static long overflow32 = 0L;
    private static long overflow64 = 0L;

    private static Teksti info8TekstiLuku = new Teksti("", Color.green, 400, 48);
    private static Teksti luku8Teksti = new Teksti("", Color.green, 400, 48);
    private static Teksti info8TekstiOverflow = new Teksti("", Color.green, 400, 48);
    private static Teksti overflow8Teksti = new Teksti("", Color.green, 400, 48);
    private static Teksti info16TekstiLuku = new Teksti("", Color.green, 400, 48);
    private static Teksti luku16Teksti = new Teksti("", Color.green, 400, 48);
    private static Teksti info16TekstiOverflow = new Teksti("", Color.green, 400, 48);
    private static Teksti overflow16Teksti = new Teksti("", Color.green, 400, 48);
    private static Teksti info32TekstiLuku = new Teksti("", Color.green, 400, 48);
    private static Teksti luku32Teksti = new Teksti("", Color.green, 400, 48);
    private static Teksti info32TekstiOverflow = new Teksti("", Color.green, 400, 48);
    private static Teksti overflow32Teksti = new Teksti("", Color.green, 400, 48);
    private static Teksti info64TekstiLuku = new Teksti("", Color.green, 400, 48);
    private static Teksti luku64Teksti = new Teksti("", Color.green, 400, 48);
    private static Teksti info64TekstiOverflow = new Teksti("", Color.green, 400, 48);
    private static Teksti overflow64Teksti = new Teksti("", Color.green, 400, 48);

    public static void renderöiKehys(Window window) {
        float ruudunLeveys = window.getWidth();
        float ruudunKorkeus = window.getHeight();
        float scaleX = ruudunLeveys/3f;
        float scaleY = ruudunKorkeus/2.4f;
        float offsetY = ruudunKorkeus/12f;
        if (siirräY > 0) siirräY -= 20;
        peliShader.bind();
        peliShader.setUniform("sampler", 0);
        peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        Matrix4f matKehys = new Matrix4f();
        window.getView().scale(1, matKehys);
        matKehys.translate(0, siirräY - offsetY, 0);
        matKehys.scale(scaleX, scaleY, 0);
        peliShader.setUniform("projection", matKehys);
        kehysTekstuuri.bind(0);
        Assets.getModel().render();
    }
    
    public static void renderöiIkkuna(Window window, Kamera kamera) {
        // float ruudunLeveys = window.getWidth();
        // float ruudunKorkeus = window.getWidth() * (3f/4f);
        // if (siirräY > 0) siirräY -= 10;
        // peliShader.bind();
        // peliShader.setUniform("sampler", 0);
        // peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));

        // float scaleXValikkoKuvake = ruudunLeveys/4f;
        // float scaleYValikkoKuvake = ruudunKorkeus/4f;
        // Matrix4f matValikkoKuvake = new Matrix4f();
        // window.getView().scale(1, matValikkoKuvake);
        // matValikkoKuvake.scale(scaleXValikkoKuvake, scaleYValikkoKuvake, 0);
        // peliShader.setUniform("projection", matValikkoKuvake);
        // alkuruutuTekstuuri.bind(0);
        // Assets.getModel().render();

        peliShader.bind();
        peliShader.nollaaShaderEfektit();

        float skaalaX = 1f/8f, skaalaY = 1f/16f, skaalaZ = 1;
        float offsetX = -1f/2.5f, offsetY = 1f/2f, offsetZ = 0;
        info8TekstiLuku.päivitäTeksti("byte 8");
        Komponentti.renderöiKomponentti(peliShader, info8TekstiLuku, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = -1f/2.5f; offsetY = 1f/4f; offsetZ = 0;
        luku8Teksti.päivitäTeksti("" + luku8);
        Komponentti.renderöiKomponentti(peliShader, luku8Teksti, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = -1f/2.5f; offsetY = -1f/4f; offsetZ = 0;
        info8TekstiOverflow.päivitäTeksti("b-yliv:");
        Komponentti.renderöiKomponentti(peliShader, info8TekstiOverflow, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = -1f/2.5f; offsetY = -1f/2f; offsetZ = 0;
        overflow8Teksti.päivitäTeksti("" + overflow8);
        Komponentti.renderöiKomponentti(peliShader, overflow8Teksti, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = -1f/6f; offsetY = 1f/2f; offsetZ = 0;
        info16TekstiLuku.päivitäTeksti("short 16");
        Komponentti.renderöiKomponentti(peliShader, info16TekstiLuku, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = -1f/6f; offsetY = 1f/4f; offsetZ = 0;
        luku16Teksti.päivitäTeksti("" + luku16);
        Komponentti.renderöiKomponentti(peliShader, luku16Teksti, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = -1f/6f; offsetY = -1f/4f; offsetZ = 0;
        info16TekstiOverflow.päivitäTeksti("s-yliv:");
        Komponentti.renderöiKomponentti(peliShader, info16TekstiOverflow, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = -1f/6f; offsetY = -1f/2f; offsetZ = 0;
        overflow16Teksti.päivitäTeksti("" + overflow16);
        Komponentti.renderöiKomponentti(peliShader, overflow16Teksti, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = 1f/6f; offsetY = 1f/2f; offsetZ = 0;
        info32TekstiLuku.päivitäTeksti("int 32");
        Komponentti.renderöiKomponentti(peliShader, info32TekstiLuku, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = 1f/6f; offsetY = 1f/4f; offsetZ = 0;
        luku32Teksti.päivitäTeksti("" + luku32);
        Komponentti.renderöiKomponentti(peliShader, luku32Teksti, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = 1f/6f; offsetY = -1f/4f; offsetZ = 0;
        info32TekstiOverflow.päivitäTeksti("i-yliv:");
        Komponentti.renderöiKomponentti(peliShader, info32TekstiOverflow, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = 1f/6f; offsetY = -1f/2f; offsetZ = 0;
        overflow32Teksti.päivitäTeksti("" + overflow32);
        Komponentti.renderöiKomponentti(peliShader, overflow32Teksti, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = 1f/2.5f; offsetY = 1f/2f; offsetZ = 0;
        info64TekstiLuku.päivitäTeksti("long 64");
        Komponentti.renderöiKomponentti(peliShader, info64TekstiLuku, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = 1f/2.5f; offsetY = 1f/4f; offsetZ = 0;
        luku64Teksti.päivitäTeksti("" + luku64);
        Komponentti.renderöiKomponentti(peliShader, luku64Teksti, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = 1f/2.5f; offsetY = -1f/4f; offsetZ = 0;
        info64TekstiOverflow.päivitäTeksti("l-yliv:");
        Komponentti.renderöiKomponentti(peliShader, info64TekstiOverflow, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);

        skaalaX = 1f/8f; skaalaY = 1f/16f; skaalaZ = 1;
        offsetX = 1f/2.5f; offsetY = -1f/2f; offsetZ = 0;
        overflow64Teksti.päivitäTeksti("" + overflow64);
        Komponentti.renderöiKomponentti(peliShader, overflow64Teksti, window, skaalaX, skaalaY, skaalaZ, offsetX, offsetY, offsetZ);
    }

    public static void pelaa() {

    }

    private static void nollaa() {
        luku8 = 0;
        luku16 = 0;
        luku32 = 0;
        luku64 = 0L;
        overflow8 = 0L;
        overflow16 = 0L;
        overflow32 = 0L;
        overflow64 = 0L;
    }

    public static void avaaToimintoIkkuna() {
        säikeetKäynnissä = true;
        new luku8Thread().start();
        new luku16Thread().start();
        new luku32Thread().start();
        new luku64Thread().start();
        nollaa();
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.MINIPELI_4;
    }

    public static void suljeToimintoIkkuna() {
        säikeetKäynnissä = false;
        Peli.syötteenTila = SyötteenTila.PELI;
        Pelaaja.käyttöViive = 50;
        siirräY = 600;
    }

    private static class luku8Thread extends Thread {
        @Override
        public void run() {
            try {
                while (säikeetKäynnissä) {
                    luku8++;
                    if (luku8 == Byte.MAX_VALUE) overflow8++;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class luku16Thread extends Thread {
        @Override
        public void run() {
            try {
                while (säikeetKäynnissä) {
                    luku16++;
                    if (luku16 == Short.MAX_VALUE) overflow16++;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class luku32Thread extends Thread {
        @Override
        public void run() {
            try {
                while (säikeetKäynnissä) {
                    luku32++;
                    if (luku32 == Integer.MAX_VALUE) overflow32++;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class luku64Thread extends Thread {
        @Override
        public void run() {
            try {
                while (säikeetKäynnissä) {
                    luku64++;
                    if (luku64 == Long.MAX_VALUE) overflow64++;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
