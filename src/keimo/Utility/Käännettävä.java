package keimo.Utility;

public interface Käännettävä {
    
    public enum Suunta {
        VASEN,
        OIKEA,
        ALAS,
        YLÖS;

        @Override
        public String toString() {
            char x = this.name().charAt(0);
            String uusiNimi = x + this.name().substring(1).toLowerCase();
            return uusiNimi;
        }
    }
}
