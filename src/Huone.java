import java.util.Random;

public class Huone {
    
    private int id;
    private String nimi;
    private int huoneenKoko;
    private KenttäKohde[][] huoneenSisältö;

    Random r = new Random();

    public int annaId() {
        return id;
    }

    public String annaNimi() {
        return nimi;
    }

    public int annaKoko() {
        return huoneenKoko;
    }
    
    public KenttäKohde[][] annaHuoneenSisältö() {
        return huoneenSisältö;
    }

    Huone(int luontiId, int luontiKoko, String luontiNimi) {
        this.id = luontiId;
        this.nimi = luontiNimi;
        this.huoneenKoko = luontiKoko;
        this.huoneenSisältö = HuoneLista.huoneet(luontiId);
    }

}
