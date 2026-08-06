package keimo.seikkailupeli.objektit;

public interface Suunnallinen {

    public enum Suunta {
        VASEN,
        OIKEA,
        ALAS,
        YLÖS,
        YLÄVASEN,
        ALAVASEN,
        YLÄOIKEA,
        ALAOIKEA;

        @Override
        public String toString() {
            char x = this.name().charAt(0);
            String uusiNimi = x + this.name().substring(1).toLowerCase();
            return uusiNimi;
        }
    }

    public enum SuuntaVasenOikea {
        VASEN,
        OIKEA;

        @Override
        public String toString() {
            char x = this.name().charAt(0);
            String uusiNimi = x + this.name().substring(1).toLowerCase();
            return uusiNimi;
        }
    }

    public Suunta annaSuunta();
    
    public void asetaSuunta(Suunta suunta);

    public static Suunta haeSuunta(String suuntaString) {
        switch (suuntaString) {
            case "vasen", "Vasen", "VASEN": return Suunta.VASEN;
            case "oikea", "Oikea", "OIKEA": return Suunta.OIKEA;
            case "alas", "Alas", "ALAS": return Suunta.ALAS;
            case "ylös", "Ylös", "YLÖS": return Suunta.YLÖS;
            default: return Suunta.YLÖS;
        }
    }

    public static Suunta haeSuunta(int asteet) {
        asteet %= 360;
        switch (asteet) {
            case 0: return Suunta.YLÖS;
            case 90: return Suunta.OIKEA;
            case 180: return Suunta.ALAS;
            case 270: return Suunta.VASEN;
            default: return Suunta.YLÖS;
        }
    }

    public static int haeAsteet(Suunta suunta) {
        switch (suunta) {
            case YLÖS: return 0;
            case OIKEA: return 90;
            case ALAS: return 180;
            case VASEN: return 270;
            case null, default: return 0;
        }
    }
}
