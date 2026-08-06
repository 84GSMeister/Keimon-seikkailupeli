package keimo.seikkailupeli.assets.tarina;

public class TarinaPätkä {
    
    private int id;
    private String nimi;
    private int pituus;
    private String[] kuvatiedostot;
    private String[] tekstit;
    private String taustamusa;

    public int annaId() {
        return id;
    }

    public String annaNimi() {
        return nimi;
    }

    public int annaPituus() {
        return pituus;
    }

    public String[] annaKuvatiedostot() {
        return kuvatiedostot;
    }

    public String[] annaTekstit() {
        return tekstit;
    }

    public String annaTaustamusa() {
        return taustamusa;
    }

    public TarinaPätkä(int id, String nimi, int pituus, String[] kuvatiedostot, String[] tekstit) {
        this.nimi = nimi;
        this.pituus = pituus;
        this.kuvatiedostot = kuvatiedostot;
        this.tekstit = tekstit;
        this.id = id;
    }
}
