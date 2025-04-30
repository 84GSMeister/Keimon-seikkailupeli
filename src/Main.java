import keimo.*;

public class Main {
    
    //Käytetään Peli-luokan metodia, koska Main-luokkaa ei voi kutsua Peli-luokasta käsin.
    static void uusiPeli() {
        Peli.uusiPeli();
    }

    public static void main(String[] args) {
        uusiPeli();
    }
}