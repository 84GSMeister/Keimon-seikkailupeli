package keimo.seikkailupeli;

public class PelinAsetukset {
    
    public static final int RUUDUNPÄIVITYS = 60;

    public static int vaikeusAste = 1;
    public static boolean musiikkiPäällä = true;
    public static boolean äänetPäällä = true;
    public static int tavoiteFPS = 0;
    public static int tavoiteTickrate = 60;
    public static AjoitusMuoto ajoitus = AjoitusMuoto.TARKKA;
    public static double musaVolyymi = 0.5d;
    public static double ääniVolyymi = 0.5d;
    public static int pelinNopeus = 60;
    public static int resoluutioX, resoluutioY;
    public static float zoom = 1;
    public static boolean debugTiedot = false;

    public static void valitseVaikeusaste(String vaikeusAsteNimi) {
        switch (vaikeusAsteNimi) {
            case "Passiivinen": vaikeusAste = 0; break;
            case "Normaali": vaikeusAste = 1; break;
            case "Vaikea": vaikeusAste = 2; break;
            case "Järjetön": vaikeusAste = 10; break;
            default: vaikeusAste = 1; break;
        }
    }

    public static enum AjoitusMuoto {
        TARKKA,
        NOPEA,
        ERITTÄIN_NOPEA;
    }
}
