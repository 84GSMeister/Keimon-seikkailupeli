package keimo;

public class TarkistettavatArvot {
    
    public static int uusiKentänKoko = 10;
    public static int npcId = 0;

    public static int lyödytVihut = 0;
    public static int ämpäröidytVihut = 0;

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
        KUOLEMA_VIHOLLINEN_PIKKUVIHU_PASSIIVINEN,
        KUOLEMA_VIHOLLINEN_PIKKUVIHU_LYÖTY,
        KUOLEMA_VIHOLLINEN_PIKKUVIHU_ÄMPÄRÖITY,
        KUOLEMA_VIHOLLINEN_PAHAVIHU_PASSIIVINEN,
        KUOLEMA_VIHOLLINEN_PAHAVIHU_LYÖTY,
        KUOLEMA_VIHOLLINEN_PAHAVIHU_ÄMPÄRÖITY,
        KUOLEMA_VIHOLLINEN_PAHAJAPIKKUVIHU_PASSIIVINEN,
        KUOLEMA_VIHOLLINEN_PAHAJAPIKKUVIHU_LYÖTY,
        KUOLEMA_VIHOLLINEN_PAHAJAPIKKUVIHU_ÄMPÄRÖITY,
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
