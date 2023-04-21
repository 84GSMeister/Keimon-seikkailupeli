package keimo.Säikeet;

import keimo.*;

import java.text.DecimalFormat;
import java.util.concurrent.locks.LockSupport;

public class AikaSäie extends Thread {

    public static boolean ajastinKäynnissä = true;
    public static double kulunutAika = 0;
    static int kulunutAikaMin = 0;
    static double kulunutAikaSek = 0;

    static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

    /**
    public static void odotaMillisekunteja(long millisekunnit) {
        
        long waitUntil = System.nanoTime() + (millisekunnit * 1_000_000);
        
        kulunutAika += 0.01;
        kulunutAikaSek = kulunutAika % 60;
        kulunutAikaMin = (int)kulunutAika / 60;

        PääIkkuna.ylätekstiAika.setText("Aika: " + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek));
        
        while(waitUntil > System.nanoTime()){
            ;
        }
    }
    class AjastimenPäivittäjä extends SwingWorker<Void, JLabel> {
        @Override
        protected Void doInBackground() {
        
            while (!isCancelled()) {
                odotaMillisekunteja(10);
            //publish(PääIkkuna.päivitäIkkuna());
            return null;
        }
    }
    */

    static void ajastin() {
        
        while (ajastinKäynnissä) {
            long alkuAika = System.nanoTime();
            LockSupport.parkNanos(2_000_000);
            Peli.globaaliAika++;
            if (Peli.peliKäynnissä && !Peli.pause) {
                Peli.pelaajanLiike();
                if (Peli.globaaliAika % 20 == 0) {
                    Peli.pelinKulku();
                }
            // if (Peli.globaaliAika % 20 == 10) {
            //     Peli.taustaToimet();
            // }
            if (Peli.globaaliAika % 20 == 15) {
                PeliKenttäMetodit.suoritaPelikenttäMetodit();
            }
            if (Peli.globaaliAika % 2 == 0) {
                PeliKenttäMetodit.suoritaPelikenttäMetoditNopea();
            }
            long loppuAika = System.nanoTime();
            long aikaErotusNs = loppuAika - alkuAika;
            if (aikaErotusNs < 10_000_000) {
                LockSupport.parkNanos(15_000_000);
            }
        }
        //System.out.println(Peli.globaaliAika);
        }
    }

    @Override
    public void run() {
        ajastin();
    }
}
