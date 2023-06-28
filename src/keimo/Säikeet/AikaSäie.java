package keimo.Säikeet;

import keimo.*;
import keimo.Ikkunat.CustomViestiIkkunat;
import keimo.PelinAsetukset.AjoitusMuoto;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.concurrent.locks.LockSupport;

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class AikaSäie extends Thread {

    public static boolean ajastinKäynnissä = true;
    public static double kulunutAika = 0;
    static int kulunutAikaMin = 0;
    static double kulunutAikaSek = 0;
    static double aikaErotusMs = 0;

    static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

    static Timer tarkistaTarviikoPeliUudelleenkäynnistää = new Timer(100, e -> {
        if (PääIkkuna.uusiIkkuna) {
            System.out.println("uusi peli");
            Peli.suljePeli();
            PääIkkuna.uusiIkkuna = false;
            Peli.uusiHuone = 0;
            Peli.huoneVaihdettava = true;
            Peli.uusiPeli();
        }
    });


    void ajastin() {
        try {
            final double PÄIVITYSAIKA = 1000000000 / PelinAsetukset.tavoiteTickrate;
            final double TAVOITE_TICKRATE = PelinAsetukset.tavoiteTickrate;
            final double TAVOITE_PÄIVITYSAIKA = 1000000000 / TAVOITE_TICKRATE;
            //At the very most we will update the game this many times before a new render.
            //If you're worried about visual hitches more than perfect timing, set this to 1.
            final int MAX_UPDATES_BEFORE_RENDER = 5;
            //We will need the last update time.
            double lastUpdateTime = System.nanoTime();
            //Store the last time we rendered.
            double lastRenderTime = System.nanoTime();

            while (ajastinKäynnissä) {
                double alkuAika = System.nanoTime();
                double now = System.nanoTime();
                int updateCount = 0;

                //Do as many game updates as we need to, potentially playing catchup.
                while (now - lastUpdateTime > PÄIVITYSAIKA && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                    lastUpdateTime += PÄIVITYSAIKA;
                    updateCount++;
                }

                //If for some reason an update takes forever, we don't want to do an insane number of catchups.
                //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                if (now - lastUpdateTime > PÄIVITYSAIKA) {
                    lastUpdateTime = now - PÄIVITYSAIKA;
                }

                Peli.globaaliTickit++;
                
                if (Peli.peliKäynnissä && !Peli.pause) {
                    Peli.pelaajanLiike();
                    Peli.pelinKulku();
                    PeliKenttäMetodit.suoritaPelikenttäMetoditJokaTick();
                    if (Peli.globaaliTickit % 2 == 0) {
                        PeliKenttäMetodit.suoritaPelikenttäMetoditJoka2Tick();
                    }
                }
                lastRenderTime = now;

                //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                while (now - lastRenderTime < TAVOITE_PÄIVITYSAIKA && now - lastUpdateTime < PÄIVITYSAIKA) {
                    //allow the threading system to play threads that are waiting to run.
                    Thread.yield();

                    //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
                    //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
                    //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
                    //On my OS it does not unpuase the game if i take this away
                    try {
                        if (PelinAsetukset.ajoitus == AjoitusMuoto.TARKKA) {}
                        else {
                            Thread.sleep(1);
                        }
                        //LockSupport.parkNanos(1_000_000);
                    }
                    catch (Exception e) {

                    }

                    now = System.nanoTime();
                }
                double loppuAika = System.nanoTime();
                double aikaErotusNs = loppuAika - alkuAika;
                aikaErotusMs = (aikaErotusNs/1_000_100f);
            }
        }
        catch (Exception e) {
            System.out.println("Ongelma ruudunpäivityksessä");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString();
            System.out.println(sStackTrace);
            String viesti = "Ajoitussäie on kaatunut.\n\nHäire sovelluksessa. Ilmoitathan kehittäjille.\n\n" + sStackTrace;
            String otsikko = "Ongelma säikeessä";
            int virheenJälkeenValinta = CustomViestiIkkunat.GrafiikkäSäieVirhe.showDialog(viesti, otsikko);
            switch (virheenJälkeenValinta) {
                case JOptionPane.YES_OPTION: run(); break;
                case JOptionPane.NO_OPTION: System.exit(0); break;
            }
        }
    }

    @Override
    public void run() {
        if (!tarkistaTarviikoPeliUudelleenkäynnistää.isRunning()) {
            tarkistaTarviikoPeliUudelleenkäynnistää.start();
        }
        this.setPriority(4);
        ajastin();
    }
}
