package keimo;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PelinAsetukset {
    
    public static final int RUUDUNPÄIVITYS = 60;

    public static int vaikeusAste = 1;
    public static boolean musiikkiPäällä = false;
    public static int musiikkiValinta = 0;
    public static int tavoiteFPS = 200;
    public static int tavoiteTickrate = 60;
    public static AjoitusMuoto ajoitus = AjoitusMuoto.TARKKA;
    public static double musaVolyymi = 0.5d;
    public static double ääniVolyymi = 0.5d;

    static List<String> musaLista = Stream.of(new File("tiedostot/musat").listFiles())
        .filter(file -> !file.isDirectory() && (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")))
        .map(File::getName).sorted()
        .collect(Collectors.toList());
    public static int musalistanPituus = musaLista.size();

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

    public static enum AjoitusMuoto {
        TARKKA,
        NOPEA,
        ERITTÄIN_NOPEA;
    }
}
