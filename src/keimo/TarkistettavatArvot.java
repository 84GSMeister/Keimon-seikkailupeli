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

    public static PelinLopetukset pelinLoppuSyy = PelinLopetukset.VAKIO;

    public static enum PelinLopetukset {
        VAKIO,
        NORMAALI_VOITTO,
        KUOLEMA_TESTI,
        KUOLEMA_VIHOLLINEN_PIKKUVIHU_PASSIIVINEN,
        KUOLEMA_VIHOLLINEN_PIKKUVIHU_LYÖTY,
        KUOLEMA_VIHOLLINEN_PIKKUVIHU_ÄMPÄRÖITY,
        KUOLEMA_VIHOLLINEN_PAHAVIHU_PASSIIVINEN,
        KUOLEMA_VIHOLLINEN_PAHAVIHU_LYÖTY,
        KUOLEMA_VIHOLLINEN_PAHAVIHU_ÄMPÄRÖITY,
        //KUOLEMA_VIHOLLINEN_PAHAJAPIKKUVIHU_PASSIIVINEN,
        //KUOLEMA_VIHOLLINEN_PAHAJAPIKKUVIHU_LYÖTY,
        //KUOLEMA_VIHOLLINEN_PAHAJAPIKKUVIHU_ÄMPÄRÖITY,
        KUOLEMA_VIHOLLINEN_ASEVIHU,
        KUOLEMA_VIHOLLINEN_AMMUS,
        KUOLEMA_JUHANI,
        KUOLEMA_SILLALTA_ALAS,
        YLENSYÖNTI,
        HIILTYNYT_MAKKARA,
        ALKOHOLIMYRKYTYS,
        VARTIJA;
    }

    public static int annaLyödytVihut() {
        return lyödytVihut;
    }

    public static int annaÄmpäröidytVihut() {
        return ämpäröidytVihut;
    }
}
