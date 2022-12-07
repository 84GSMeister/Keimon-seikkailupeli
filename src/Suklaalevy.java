import javax.swing.ImageIcon;

public class Suklaalevy extends Ruoka {

    String katso() {
        return "Voisin syödä tämän.";
    }
    
    String käytä(){
        poista = true;
        return "Se maistui hyvältä";
    }

    Suklaalevy(){
        this.nimi = "Suklaalevy";
        this.kuvake = new ImageIcon("tiedostot/kuvat/suklaalevy.png");
        this.heal = 2;
    }
}
