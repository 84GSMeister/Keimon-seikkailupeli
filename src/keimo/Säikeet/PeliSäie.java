package keimo.Säikeet;

import keimo.*;
import keimo.Ikkunat.AsetusIkkuna;
import keimo.Ikkunat.CustomViestiIkkunat;
import keimo.Ruudut.PeliRuutu;
import keimo.PelinAsetukset.AjoitusMuoto;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class PeliSäie extends Thread {

    public static boolean peliLoopKäynnissä = true;
    public static double kulunutAika = 0;
    static int kulunutAikaMin = 0;
    static double kulunutAikaSek = 0;
    static double aikaErotusNs = 0;
    static double aikaErotusMs = 0;
    public static boolean pelinTilanPäivitysYliajo = false;
    //public static int idlaaTickejä = 0;
    //public static boolean keskeytäNormaaliToiminta = false;

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

    protected static void scrollaaDialogiTeksti() {
        if (PääIkkuna.tekstiAuki) {
            if (PääIkkuna.tekstiäJäljellä > 0) {
                String tulostettavaTeksti = PääIkkuna.dialogiTeksti.substring(0, PääIkkuna.dialogiTeksti.length()-PääIkkuna.tekstiäJäljellä +1);
                if (!(PääIkkuna.dialogiTeksti.contains("<html>") && tulostettavaTeksti.length() < 6)) {
                    PeliRuutu.vuoropuheTeksti.setText(tulostettavaTeksti);
                }
                PääIkkuna.tekstiäJäljellä--;
            }
        }
    }

    static final double TAVOITE_TICKRATE = PelinAsetukset.tavoiteTickrate;
    static final double TAVOITE_PÄIVITYSAIKA = 1000000000 / TAVOITE_TICKRATE;
    void pääMetodi() {
        try {
            while (peliLoopKäynnissä) {
                if (PelinAsetukset.ajoitus == AjoitusMuoto.ERITTÄIN_NOPEA) {
                    peliLoopNopeaSäie();
                }
                else {
                    peliLoop();
                }
                //System.out.println("pelisäie: " + "operaatioaika: " + (operaatioAika - alkuAika)/1_000_000 + " ms, " + "odotusaika: " + (loppuAika - operaatioAika)/1_000_000 + " ms");
            }
        }
        catch (Exception e) {
            System.out.println("Ongelma pelisäikeessä");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString();
            System.out.println(sStackTrace);
            String viesti = "Pelisäie on kaatunut.\n\nHäire sovelluksessa. Ilmoitathan kehittäjille.\n\nÄlä pliis paina rastia vaan valitse jokin vaihtoehdoista.\n\nVirhekoodi:\n" + sStackTrace;
            String otsikko = "Ongelma säikeessä";
            int virheenJälkeenValinta = CustomViestiIkkunat.GrafiikkäSäieVirhe.showDialog(viesti, otsikko);
            switch (virheenJälkeenValinta) {
                case JOptionPane.YES_OPTION: run(); break;
                case JOptionPane.NO_OPTION: System.exit(0); break;
            }
        }
    }

    public static void peliLoop() {
        double alkuAika = System.nanoTime();
        if (pelinTilanPäivitysYliajo) {
            Peli.pause = false;
            PääIkkuna.tekstiAuki = false;
            Peli.valintaDialogi = false;
            pelinTilanPäivitysYliajo = false;
        }
        
        if (Peli.peliKäynnissä) {
            if (!Peli.pause && !Peli.pauseDialogi) {
                Peli.pelaajanLiike();
                Peli.pelinKulku();
                ÄänentoistamisSäie.toistaÄäniJono();
                PeliKenttäMetodit.suoritaPelikenttäMetoditJokaTick();
                if (Peli.globaaliTickit % 2 == 0) {
                    PeliKenttäMetodit.suoritaPelikenttäMetoditJoka2Tick();
                }
                if (Peli.globaaliTickit % 10 == 0) {
                    PeliKenttäMetodit.suoritaPelikenttäMetoditJoka10Tick();
                }
                if (Peli.globaaliTickit % 60 == 0) {
                    PeliKenttäMetodit.suoritaPelikenttäMetoditJoka60Tick();
                }
                if (Peli.globaaliTickit % 100 == 0) {
                    PeliKenttäMetodit.suoritaPelikenttäMetoditJoka100Tick();
                    // SwingUtilities.invokeLater(new Runnable() {
                    //     @Override
                    //     public void run() {
                    //         EventQueue evnt = Toolkit.getDefaultToolkit().getSystemEventQueue();
                    //         if (evnt.peekEvent() != null) {
                    //             //System.out.println(EventQueue.getCurrentEvent());
                    //             System.out.println(evnt.peekEvent(1));
                    //             int eventIndex = 0;
                    //             //while (true) {
                    //                 if (evnt.peekEvent() != null) {
                    //                     //try {
                    //                         System.out.println(evnt.getCurrentEvent());
                    //                     //}
                    //                     //catch (InterruptedException ie) {
                    //                     //    System.out.println("interrupted");
                    //                     //}
                    //                 }
                    //                 //else {
                    //                 //    break;
                    //                 //}
                    //             //}
                    //         }
                    //     }
                    // });
                }
                if ((Peli.globaaliTickit-1) % 2000 == 0) {
                    PeliKenttäMetodit.suoritaPelikenttäMetoditJoka2000Tick();
                }
            }
            if (Peli.globaaliTickit % 2 == 0) {
                scrollaaDialogiTeksti();
            }
            if (Peli.globaaliTickit % 5 == 0) {
                String muuttuvaMerkki = "" + AsetusIkkuna.ikkunaTeksti.charAt(0);
                AsetusIkkuna.ikkunaTeksti = AsetusIkkuna.ikkunaTeksti.substring(1) + muuttuvaMerkki;
                //PääIkkuna.ikkuna.setTitle(ikkunaTeksti);
                if (AsetusIkkuna.ikkuna != null) {
                    AsetusIkkuna.ikkuna.setTitle(AsetusIkkuna.ikkunaTeksti);
                }
            }
        }
        
        double operaatioAika = System.nanoTime();

        while (System.nanoTime() - alkuAika < TAVOITE_PÄIVITYSAIKA) {
            Thread.yield();
            try {
                if (PelinAsetukset.ajoitus == AjoitusMuoto.TARKKA) {}
                else {
                    Thread.sleep(1);
                }
            }
            catch (Exception e) {

            }

            //now = System.nanoTime();
        }
        if (!Peli.pause) {
            Peli.globaaliTickit++;
            PeliRuutu.ylätekstiTicks.setText("Tickejä: " + Peli.globaaliTickit);
            PeliRuutu.ylätekstiTickrate.setText("Tickrate: " + kaksiDesimaalia.format((1000/aikaErotusMs)));
            double loppuAika = System.nanoTime();
            aikaErotusNs = loppuAika - alkuAika;
            aikaErotusMs = (aikaErotusNs/1_000_100f);
        }
    }

    static final double TIME_BETWEEN_UPDATES = 1000000000 / PelinAsetukset.RUUDUNPÄIVITYS;
    //If we are able to get as high as this FPS, don't render again.
    static double TARGET_FPS = PelinAsetukset.tavoiteFPS;
    static final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
    //At the very most we will update the game this many times before a new render.
    //If you're worried about visual hitches more than perfect timing, set this to 1.
    static final int MAX_UPDATES_BEFORE_RENDER = 1;
    //We will need the last update time.
    static double lastUpdateTime = System.nanoTime();
    //Store the last time we rendered.
    static double lastRenderTime = System.nanoTime();

    public static void peliLoopNopeaSäie() {
        double alkuAika = System.nanoTime();
        double now = System.nanoTime();
        int updateCount = 0;

        //Do as many game updates as we need to, potentially playing catchup.
        while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
            //MyGame.this.scene.update();
            peliLoop();

            lastUpdateTime += TIME_BETWEEN_UPDATES;
            updateCount++;
        }

        //If for some reason an update takes forever, we don't want to do an insane number of catchups.
        //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
        if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
            lastUpdateTime = now - TIME_BETWEEN_UPDATES;
        }

        //Render. To do so, we need to calculate interpolation for a smooth render.
        float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
        //MyGame.this.scene.render(interpolation);
        PääIkkuna.vaatiiPäivityksen = true;

        GrafiikanPäivitysSäie.alkuAika = System.nanoTime();
        GrafiikanPäivitysSäie.suoritaGrafiikanPäivitys();
        double operaatioAika = System.nanoTime();
        while (System.nanoTime() - alkuAika < GrafiikanPäivitysSäie.TAVOITE_RUUDUNPÄIVITYSAIKA) {
            Thread.yield();
            try {
                if (PelinAsetukset.ajoitus == AjoitusMuoto.TARKKA) {}
                else {
                    Thread.sleep(1);
                }
                //LockSupport.parkNanos(1_000_000);
            }
            catch (Exception e) {

            }

        }
        GrafiikanPäivitysSäie.frameja++;
        GrafiikanPäivitysSäie.kuviaKertymässä++;
        GrafiikanPäivitysSäie.loppuAika = System.nanoTime();
        GrafiikanPäivitysSäie.aikaErotusNs = (GrafiikanPäivitysSäie.loppuAika - GrafiikanPäivitysSäie.alkuAika);
        GrafiikanPäivitysSäie.aikaErotusUs = GrafiikanPäivitysSäie.aikaErotusNs/1000;
        GrafiikanPäivitysSäie.aikaErotusMs = GrafiikanPäivitysSäie.aikaErotusUs/1000;

        PeliRuutu.ylätekstiViive.setText("Päivitysaika: " + GrafiikanPäivitysSäie.neljäDesimaalia.format(GrafiikanPäivitysSäie.aikaErotusMs) + " ms");
        PeliRuutu.ylätekstiKuvat.setText("Kuvia: " + GrafiikanPäivitysSäie.frameja);

        if (GrafiikanPäivitysSäie.kuviaKertymässä >= 10) {
            GrafiikanPäivitysSäie.kuviaKertymässä = 0;
            GrafiikanPäivitysSäie.päivitysAikaKertymä = 0;
        }
        else {
            GrafiikanPäivitysSäie.päivitysAikaKertymä += aikaErotusMs;
        }
        if (GrafiikanPäivitysSäie.kuviaKertymässä > 0) {
            double keskivertoPäivitysAika10Framea = GrafiikanPäivitysSäie.päivitysAikaKertymä/GrafiikanPäivitysSäie.kuviaKertymässä;
            //System.out.println("kertymä: " +  päivitysAikaKertymä + ", n: " + kuviaKertymässä + ", ka 10 framea: " + keskivertoPäivitysAika10Framea);
            PeliRuutu.ylätekstiFPS.setText("FPS: " + GrafiikanPäivitysSäie.neljäDesimaalia.format(1000/keskivertoPäivitysAika10Framea));
        }
        lastRenderTime = now;
    }

    @Override
    public void run() {
        if (!tarkistaTarviikoPeliUudelleenkäynnistää.isRunning()) {
            tarkistaTarviikoPeliUudelleenkäynnistää.start();
        }
        this.setPriority(4);
        pääMetodi();
    }
}
