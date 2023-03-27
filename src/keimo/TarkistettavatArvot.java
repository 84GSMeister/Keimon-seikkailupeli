package keimo;

public class TarkistettavatArvot {
    
    public static int uusiKentänKoko = 10;
    public static int npcId = 0;

    protected static int lyödytVihut = 0;
    protected static int ämpäröidytVihut = 0;

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
        KUOLEMA_VIHOLLINEN,
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
