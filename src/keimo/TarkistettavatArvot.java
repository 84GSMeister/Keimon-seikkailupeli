package keimo;

public class TarkistettavatArvot {
    
    public static int uusiKentänKoko = 10;
    public static int npcId = 0;

    private static int lyödytVihut = 0;
    private static int ämpäröidytVihut = 0;

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

    public static PelinLopetukset pelinLoppuSyy = null;

    public static enum PelinLopetukset {
        NORMAALI_VOITTO,
        KUOLEMA_VIHOLLINEN_PIKKUVIHU_PASSIIVINEN,
        KUOLEMA_VIHOLLINEN_PIKKUVIHU_LYÖTY,
        KUOLEMA_VIHOLLINEN_PIKKUVIHU_ÄMPÄRÖITY,
        KUOLEMA_VIHOLLINEN_PAHAVIHU_PASSIIVINEN,
        KUOLEMA_VIHOLLINEN_PAHAVIHU_LYÖTY,
        KUOLEMA_VIHOLLINEN_PAHAVIHU_ÄMPÄRÖITY,
        KUOLEMA_VIHOLLINEN_ASEVIHU_PASSIIVINEN,
        KUOLEMA_VIHOLLINEN_ASEVIHU_LYÖTY,
        KUOLEMA_VIHOLLINEN_ASEVIHU_ÄMPÄRÖITY,
        KUOLEMA_JUHANI,
        KUOLEMA_SILLALTA_ALAS,
        KUOLEMA_BOSS,
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

    public static int luoNpcId() {
        npcId++;
        return npcId -1;
    }
}
