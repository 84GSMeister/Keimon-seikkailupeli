import java.util.HashMap;

public class Main {
    
    static int kentänKoko = 10;
    static int kentänAlaraja = 0;
    static int kentänYläraja = kentänAlaraja + kentänKoko - 1;
    static KenttäKohde[][] pelikenttä;// = new Tarkastettava[Main.kentänKoko][Main.kentänKoko];
    static Maasto[][] maastokenttä;
    static String häviönSyy = "";
    static int uusiKentänKoko = 10;
    static int aloitusHp = 10;
    static int pelaajanSijX;
    static int pelaajanSijY;
    static int pelaajanKylläisyys = 0;
    static boolean huoneVaihdettava = true;
    static int uusiHuone = 0;
    static HashMap<Integer, Huone> huoneKartta = new HashMap<Integer, Huone>();
    static int huoneidenMäärä = 0;
    static boolean pause = false;
    
    static void uusiPeli() {
        
        kentänKoko = uusiKentänKoko;
        kentänYläraja = kentänAlaraja + kentänKoko - 1;
        pelikenttä = new KenttäKohde[Main.kentänKoko][Main.kentänKoko];
        maastokenttä = new Maasto[Main.kentänKoko][Main.kentänKoko];
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
