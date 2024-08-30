package keimo.HuoneEditori.TarinaEditori;

import javax.swing.ImageIcon;

public class TarinaPätkä {
    
    static int tarinaId = 0;

    public static void nollaaTarinaId() {
        tarinaId = 0;
    }

    protected int id;
    protected String nimi;
    protected int pituus;
    protected ImageIcon[] kuvat;
    protected String[] kuvatiedostot;
    protected String[] tekstit;
    protected String taustamusa;

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

    public ImageIcon[] annaKuvat() {
        return kuvat;
    }

    public String[] annaTekstit() {
        return tekstit;
    }

    public String annaTaustamusa() {
        return taustamusa;
    }

    public TarinaPätkä(String nimi, int pituus, String[] kuvatiedostot, String[] tekstit) {
        this.nimi = nimi;
        this.pituus = pituus;
        kuvat = new ImageIcon[kuvatiedostot.length];
        for (int i = 0; i < kuvatiedostot.length; i++) {
            kuvat[i] = new ImageIcon(kuvatiedostot[i]); 
        }
        this.kuvatiedostot = kuvatiedostot;
        this.tekstit = tekstit;
        this.id = tarinaId;
        tarinaId++;
    }

    public TarinaPätkä(String nimi, int pituus, String[] kuvatiedostot, ImageIcon[] kuvat, String[] tekstit) {
        this.nimi = nimi;
        this.pituus = pituus;
        this.kuvat = kuvat;
        this.kuvatiedostot = kuvatiedostot;
        this.tekstit = tekstit;
        this.id = tarinaId;
        tarinaId++;
    }
}
