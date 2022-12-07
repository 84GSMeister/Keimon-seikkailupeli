public abstract class Kiintopiste extends KenttäKohde {

    boolean vuorovaikutus = false;
    boolean vuorovaikutusToimii() {
        return vuorovaikutus;
    }

    boolean Suoritettu = false;

    String kokeileEsinettä(Esine e) {
        System.out.println("Mitään ei tapahtunut!");
        return "Mitään ei tapahtunut!";
    }

    String annaNimi(){
        return nimi;
    }

    Kiintopiste() {

    }
}
