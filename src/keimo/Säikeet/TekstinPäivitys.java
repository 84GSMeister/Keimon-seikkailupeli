package keimo.Säikeet;

import java.text.DecimalFormat;
import javax.swing.Timer;

import keimo.*;
import keimo.PelinAsetukset.AjoitusMuoto;
import keimo.Ruudut.PeliRuutu;

public class TekstinPäivitys {

    static boolean ajastinKäynnissä = true;
    public static double kulunutAika = 0;
    public static double pauseAlkuAika = 0;
    public static double pauseLoppuAika = 0;
    static int kulunutAikaMin = 0;
    static double kulunutAikaSek = 0;
    static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

    public static Timer päivitäTekstitNopea = new Timer(16, e -> {
        if (PelinAsetukset.ajoitus == AjoitusMuoto.ERITTÄIN_NOPEA) {
            PeliRuutu.ylätekstiViive.setText("Päivitysaika: " + kaksiDesimaalia.format(GrafiikkaAikaSäieNopea.aikaErotusMs) + " ms");
            PeliRuutu.ylätekstiKuvat.setText("Kuvia: " + GrafiikkaAikaSäieNopea.frameja);
        }
        else {
            PeliRuutu.ylätekstiViive.setText("Päivitysaika: " + kaksiDesimaalia.format(GrafiikanPäivitysSäie.aikaErotusMs) + " ms");
            PeliRuutu.ylätekstiKuvat.setText("Kuvia: " + GrafiikanPäivitysSäie.frameja);
        }
        kulunutAika = (System.nanoTime() - Peli.aikaReferenssi)/1_000_000;
        if (!Peli.ajastinPysäytetty) {
            kulunutAikaSek = (double)kulunutAika/1000f;
            kulunutAikaMin = (int)kulunutAikaSek / 60;
            kulunutAikaSek = kulunutAikaSek % 60;
            PeliRuutu.ylätekstiAika.setText("Aika: " + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek));
        }
    });

    public static Timer päivitäTekstitHidas = new Timer(50, e -> {
        if (PelinAsetukset.ajoitus == AjoitusMuoto.ERITTÄIN_NOPEA) {
            PeliRuutu.ylätekstiTickrate.setText("Tickrate: " + kaksiDesimaalia.format((1000/GrafiikkaAikaSäieNopea.aikaErotusMs)));
        }
        else {
            PeliRuutu.ylätekstiTickrate.setText("Tickrate: " + kaksiDesimaalia.format((1000/PeliSäie.aikaErotusMs)));
        }
        PeliRuutu.ylätekstiTicks.setText("Tickejä: " + Peli.globaaliTickit);
    });

    public static Timer laskeFpsKertymä = new Timer(50, e -> {
        if (PelinAsetukset.ajoitus == AjoitusMuoto.ERITTÄIN_NOPEA) {
            GrafiikkaAikaSäieNopea.fpsFloat = GrafiikkaAikaSäieNopea.kuviaKertymässä * 1_000_000f / GrafiikkaAikaSäieNopea.kertymänAika;
            GrafiikkaAikaSäieNopea.fps = (long)GrafiikkaAikaSäieNopea.fpsFloat;
            PeliRuutu.ylätekstiFPS.setText("FPS: " + kaksiDesimaalia.format(GrafiikkaAikaSäieNopea.fpsFloat));
            GrafiikkaAikaSäieNopea.kertymänAika = 0;
            GrafiikkaAikaSäieNopea.kuviaKertymässä = 0;
        }
        else {
            GrafiikanPäivitysSäie.fpsFloat = GrafiikanPäivitysSäie.kuviaKertymässä * 1_000_000f / GrafiikanPäivitysSäie.kertymänAika;
            GrafiikanPäivitysSäie.fps = (long)GrafiikanPäivitysSäie.fpsFloat;
            PeliRuutu.ylätekstiFPS.setText("FPS: " + kaksiDesimaalia.format(GrafiikanPäivitysSäie.fpsFloat));
            GrafiikanPäivitysSäie.kertymänAika = 0;
            GrafiikanPäivitysSäie.kuviaKertymässä = 0;
        }
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
