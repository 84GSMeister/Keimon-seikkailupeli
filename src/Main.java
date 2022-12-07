public class Main {
    
    static int kentänKoko = 10;
    static int kentänAlaraja = 0;
    static int kentänYläraja = kentänAlaraja + kentänKoko - 1;
    static KenttäKohde[][] pelikenttä;// = new Tarkastettava[Main.kentänKoko][Main.kentänKoko];
    static String häviönSyy = "";
    static int uusiKentänKoko = 10;
    static int suklaidenMäärä = 10;
    static int makkaroidenMäärä = 3;
    static int vihujenMäärä = 5;
    static int aloitusHp = 10;
    static int pelaajanSijX;
    static int pelaajanSijY;
    static int pelaajanKylläisyys = 0;
    
    static void uusiPeli() {
        
        kentänKoko = uusiKentänKoko;
        pelikenttä = new KenttäKohde[Main.kentänKoko][Main.kentänKoko];
        Peli peli = new Peli();
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
