package keimo;

public class TarkistettavatArvot {
    
    public static int uusiKentänKoko = 10;
    public static int npcId = 0;

    public static int lyödytVihut = 0;
    public static int ämpäröidytVihut = 0;

    public static void lisääTappoLaskuriin(String tappoTapa) {
        switch (tappoTapa) {
            case "Pesäpallomaila": lyödytVihut++; break;
            case "Vesiämpäri": ämpäröidytVihut++; break;
            default: break;
        }
    }

    public static int annaLyödytVihut() {
        return lyödytVihut;
    }

    public static int annaÄmpäröidytVihut() {
        return ämpäröidytVihut;
    }
}
