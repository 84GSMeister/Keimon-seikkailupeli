package keimo;

import keimo.editori.HuoneEditoriIkkuna;

public class Sovellus {
    
    public static void käynnistä() {

        HuoneEditoriIkkuna.käynnistäEditori();

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        HuoneEditoriIkkuna.päivitäEditoriIkkuna();
                        Thread.sleep(50);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
