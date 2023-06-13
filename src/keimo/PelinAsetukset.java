package keimo;
public class PelinAsetukset {
    
    public static int RUUDUNPÄIVITYS = 60;

    public static int vaikeusAste = 1;
    public static boolean musiikkiPäällä = false;
    public static int musiikkiValinta = 0;
    public static int tavoiteFPS = 200;
    public static int tavoiteTickrate = 60;

    public static void valitseVaikeusaste(String vaikeusAsteNimi) {
        switch (vaikeusAsteNimi) {
            case "Passiivinen":
                vaikeusAste = 0;
            break;
            case "Normaali":
                vaikeusAste = 1;
            break;
            case "Vaikea":
                vaikeusAste = 2;
            break;
            case "Järjetön":
                vaikeusAste = 10;
            break;
            default:
                vaikeusAste = 1;
            break;
        }
    }
}
