package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Makkara extends Ruoka{
    
    boolean paistettu = false;
    boolean käristetty = false;
    
    public String katso() {
        if (käristetty) {
            return "Makkara on läpeensä käristynyt. Ei näytä järin maittavalta.";
        }
        else if (paistettu) {
            return "Mmm... onpas herkullisen näköistä kyrsää.";
        }
        else {
            return "Makkaran voisi varmaan paistaa nuotiolla";
        }
    }
    
    public String käytä(){
        poista = true;
        if (käristetty) {
            this.heal = -1;
            return "Ugh. Olipas pahaa hiiltynyttä makkaraa. Menetit 1 elämäpisteen";
        }
        else if (paistettu) {
            this.heal = 5;
            return "Se oli erittäin makoisaa kyrsää. Sait " + this.heal + " elämäpistettä";
        }
        else {
            this.heal = 1;
            return "Ei niin hyvää raakana. Sait " + this.heal + " elämäpisteen";
        }
    }

    public String paista() {
        if (käristetty) {
            return "Makkara on jo käristetty hiileksi";
        }
        else if (paistettu) {
            this.käristetty = true;
            this.nimi = "Käristetty makkara";
            this.kuvake = new ImageIcon("tiedostot/kuvat/makkarat_käristetty.png");
            return "Paistoit liikaa! Nyt makkara on pikimusta.";
        }
        else {
            this.paistettu = true;
            this.nimi = "Paistettu makkara";
            this.kuvake = new ImageIcon("tiedostot/kuvat/makkarat_paistettu.png");
            return "Mmm.. onpas hyvän näköistä kyrsää.";
        }
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Makkara";
            case "genetiivi":
                return "Makkaran";
            case "esiivi":
                return "Makkarana";
            case "partitiivi":
                return "Makkaraa";
            case "translatiivi":
                return "Makkaraksi";
            case "inessiivi":
                return "Makkarassa";
            case "elatiivi":
                return "Makkarasta";
            case "illatiivi":
                return "Makkaraan";
            case "adessiivi":
                return "Makkaralla";
            case "ablatiivi":
                return "Makkaralta";
            case "allatiivi":
                return "Makkaralle";
            default:
                return "Makkara";
        }
    }

    public Makkara(){
        super.nimi = "Makkara";
        super.kuvake = new ImageIcon("tiedostot/kuvat/makkarat.png");
        super.heal = 1;
        super.käyttö = true;
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Nuotio");
        super.asetaTiedot();
    }

    public Makkara(int sijX, int sijY){
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.nimi = "Makkara";
        super.kuvake = new ImageIcon("tiedostot/kuvat/makkarat.png");
        super.heal = 1;
        super.käyttö = true;
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Nuotio");
        super.asetaTiedot();
    }
}
