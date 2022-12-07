import javax.swing.ImageIcon;

public class Kilpi extends Esine{
    
    String käytä(){
        return "Pidä kilpeä kädessä kun menet vihollisen luo!";
    }
    
    Kilpi() {
        this.nimi = "Kilpi";
        this.kuvake = new ImageIcon("tiedostot/kuvat/kilpi.png");
    }
}
