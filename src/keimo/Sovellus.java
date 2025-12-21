package keimo;

import keimo.keimoengine.KeimoEngine;
import keimo.seikkailupeli.Peli;

public class Sovellus {

    public static KeimoEngine engine;
    
    public static void käynnistä() {
        Peli.uusiPeli();
        engine = new KeimoEngine();
        engine.start();
        while (!KeimoEngine.glKäynnistetty) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
