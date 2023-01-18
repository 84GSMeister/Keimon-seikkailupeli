package keimo.Maastot;
import javax.swing.ImageIcon;

public class Hiekka extends Maasto{
    
    public Hiekka() {
        super.nimi = "Hiekka";
        super.kuvake = new ImageIcon("tiedostot/kuvat/maasto/hiekka.png");
        super.asetaTiedot();
    }

    public Hiekka(int sijX, int sijY) {
        super.nimi = "Hiekka";
        super.kuvake = new ImageIcon("tiedostot/kuvat/maasto/hiekka.png");
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.asetaTiedot();
    }
}
