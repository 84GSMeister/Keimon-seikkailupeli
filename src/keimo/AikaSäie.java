package keimo;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class AikaSäie extends Thread {

    static boolean ajastinKäynnissä = true;
    static double kulunutAika = 0;
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
            //odotaMillisekunteja(10);
        }
    }

    @Override
    public void run() {
        ajastin();
    }
}
