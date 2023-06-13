package keimo.Säikeet;

import java.text.DecimalFormat;
import javax.swing.Timer;

import keimo.*;
import keimo.Ruudut.PeliRuutu;

public class TekstinPäivitys {

    static boolean ajastinKäynnissä = true;
    public static double kulunutAika = 0;
    static int kulunutAikaMin = 0;
    static double kulunutAikaSek = 0;
    static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

    public static Timer päivitäTekstitNopea = new Timer(16, e -> {
        PeliRuutu.ylätekstiViive.setText("Päivitysaika: " + kaksiDesimaalia.format(GrafiikanPäivitysSäie.aikaErotusMs) + " ms");
        PeliRuutu.ylätekstiKuvat.setText("Kuvia: " + GrafiikanPäivitysSäie.frameja);
        kulunutAika = (System.nanoTime() - Peli.aikaReferenssi)/1_000_000;
        kulunutAikaSek = (double)kulunutAika/1000f;
        kulunutAikaMin = (int)kulunutAikaSek / 60;
        kulunutAikaSek = kulunutAikaSek % 60;
        PeliRuutu.ylätekstiAika.setText("Aika: " + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek));
    });

    public static Timer päivitäTekstitHidas = new Timer(50, e -> {
        PeliRuutu.ylätekstiTickrate.setText("Tickrate: " + kaksiDesimaalia.format((1000/AikaSäie.aikaErotusMs)));
        PeliRuutu.ylätekstiTicks.setText("Tickejä: " + Peli.globaaliTickit);
    });

    public static Timer laskeFpsKertymä = new Timer(50, e -> {
        GrafiikanPäivitysSäie.fpsFloat = GrafiikanPäivitysSäie.kuviaKertymässä * 1_000_000f / GrafiikanPäivitysSäie.kertymänAika;
        GrafiikanPäivitysSäie.fps = (long)GrafiikanPäivitysSäie.fpsFloat;
        PeliRuutu.ylätekstiFPS.setText("FPS: " + kaksiDesimaalia.format(GrafiikanPäivitysSäie.fpsFloat));
        GrafiikanPäivitysSäie.kertymänAika = 0;
        GrafiikanPäivitysSäie.kuviaKertymässä = 0;
    });

    public static void aloitaAjastimet() {
        päivitäTekstitNopea.start();
        päivitäTekstitHidas.start();
        laskeFpsKertymä.start();
    }

    public static void suljeAjastimet() {
        päivitäTekstitNopea.stop();
        päivitäTekstitHidas.stop();
        laskeFpsKertymä.stop();
    }
}
