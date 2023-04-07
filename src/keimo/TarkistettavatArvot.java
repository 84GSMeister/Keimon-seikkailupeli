package keimo;

public class TarkistettavatArvot {
    
    public static int uusiKentänKoko = 10;
    public static int npcId = 0;

    protected static int lyödytVihut = 0;
    protected static int ämpäröidytVihut = 0;

    public static void nollaa() {
        uusiKentänKoko = 10;
        npcId = 0;
        lyödytVihut = 0;
        ämpäröidytVihut = 0;
    }

    public static void lisääTappoLaskuriin(String tappoTapa) {
        switch (tappoTapa) {
            case "Pesäpallomaila": lyödytVihut++; break;
            case "Vesiämpäri": ämpäröidytVihut++; break;
            default: break;
        }
    }

    public static PelinLopetukset pelinLoppuSyy = PelinLopetukset.NORMAALI_VOITTO;

    public static enum PelinLopetukset {
        NORMAALI_VOITTO,
        KUOLEMA_VIHOLLINEN_NEUTRAALI,
        KUOLEMA_VIHOLLINEN_PIESTY,
        KUOLEMA_VIHOLLINEN_ÄMPÄRÖITY,
        KUOLEMA_JUHANI,
        YLENSYÖNTI;
    }

    public static int annaLyödytVihut() {
        return lyödytVihut;
    }

    public static int annaÄmpäröidytVihut() {
        return ämpäröidytVihut;
    }
}
