import javax.swing.ImageIcon;

public class Seinä extends Maasto{
    
    Seinä() {
        super.nimi = "Tiili";
        super.kuvake = new ImageIcon("tiedostot/kuvat/maasto/tiili.png");
        super.estääLiikkumisen = true;
        super.asetaTiedot();
    }

    Seinä(int sijX, int sijY) {
        super.nimi = "Tiili";
        super.kuvake = new ImageIcon("tiedostot/kuvat/maasto/tiili.png");
        super.estääLiikkumisen = true;
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.asetaTiedot();
    }
}