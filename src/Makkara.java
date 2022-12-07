import javax.swing.ImageIcon;

public class Makkara extends Ruoka{
    
    boolean paistettu = false;
    boolean käristetty = false;
    
    String katso() {
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
    
    String käytä(){
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

    String paista() {
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

    Makkara(){
        this.nimi = "Makkara";
        this.kuvake = new ImageIcon("tiedostot/kuvat/makkarat.png");
        this.heal = 1;
        this.erikoiskäyttö = true;
    }
}
