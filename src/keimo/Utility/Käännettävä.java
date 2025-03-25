package keimo.Utility;

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
}
