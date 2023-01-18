package keimo.Maastot;
import javax.swing.ImageIcon;

public class Seinä extends Maasto{
    
    public Seinä() {
        super.nimi = "Tiili";
        super.kuvake = new ImageIcon("tiedostot/kuvat/maasto/tiili.png");
        super.estääLiikkumisen = true;
        super.asetaTiedot();
    }

    public Seinä(int sijX, int sijY) {
        super.nimi = "Tiili";
        super.kuvake = new ImageIcon("tiedostot/kuvat/maasto/tiili.png");
        super.estääLiikkumisen = true;
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.asetaTiedot();
    }
}