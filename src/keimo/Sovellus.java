package keimo;

import keimo.keimoengine.KeimoEngine;
import keimo.seikkailupeli.Peli;

import javax.swing.JOptionPane;

public class Sovellus {

    public static KeimoEngine engine;
    
    public static void käynnistä(String[] args) {
        boolean peli = false;
        for (String arg : args) {
            if (arg.contains("-peli")) {
                peli = true;
            }
        }
        if (peli) käynnistäPeli();
        else {
            String viesti = "Sovellus on yritetty käynnistää virheellisillä argumenteilla. Tämä voi tapahtua, jos sovellus on käynnistetty jar-tiedostosta suoraan.\n" +
                            "Versio 0.8.2:sta eteenpäin mukana tulee exe-tiedosto, joka käynnistää sovelluksen automaattisesti oikeilla argumenteilla.\n\n" +
                            "Peli on mahdollista käynnistää komentoriviltä vivulla \"-peli\", jos tiedät mitä teet (varaudu ongelmiin).";
            String otsikko = "Virheelliset argumentit";
            int virheTyyppi = JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(null, viesti, otsikko, virheTyyppi);
        }
    }

    private static void käynnistäPeli() {
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
