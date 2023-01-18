package keimo;

import java.util.HashMap;

import keimo.Kenttäkohteet.KenttäKohde;
import keimo.Maastot.Maasto;

public class Main {
    
    static int kentänKoko = 10;
    static int kentänAlaraja = 0;
    static int kentänYläraja = kentänAlaraja + kentänKoko - 1;
    static String häviönSyy = "";
    static int uusiKentänKoko = 10;
    static int aloitusHp = 10;
    static int pelaajanKylläisyys = 0;
    static boolean huoneVaihdettava = true;
    static int uusiHuone = 0;
    static HashMap<Integer, Huone> huoneKartta = new HashMap<Integer, Huone>();
    static int huoneidenMäärä = 0;
    static boolean pause = false;
    
    static void uusiPeli() {
        
        kentänKoko = uusiKentänKoko;
        kentänYläraja = kentänAlaraja + kentänKoko - 1;
        Peli peli = new Peli();
        pause = true;
    }

    /**
     * Main class.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        uusiPeli();
    }
}
