package keimo.seikkailupeli.objektit;

public interface Käännettävä {
    
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

    public SuuntaVasenOikea suuntaVasenOikea = SuuntaVasenOikea.OIKEA;
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

    public static Suunta haeSuunta(String suuntaString) {
        switch (suuntaString) {
            case "vasen", "Vasen", "VASEN": return Suunta.VASEN;
            case "oikea", "Oikea", "OIKEA": return Suunta.OIKEA;
            case "alas", "Alas", "ALAS": return Suunta.ALAS;
            case "ylös", "Ylös", "YLÖS": return Suunta.YLÖS;
            default: return Suunta.YLÖS;
        }
    }
}
