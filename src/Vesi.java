import javax.swing.ImageIcon;

public class Vesi extends Maasto{
    
    Vesi() {
        super.nimi = "Vesi";
        super.kuvake = new ImageIcon("tiedostot/kuvat/maasto/vesi.png");
        super.estääLiikkumisen = true;
        super.asetaTiedot();
    }

    Vesi(int sijX, int sijY) {
        super.nimi = "Vesi";
        super.kuvake = new ImageIcon("tiedostot/kuvat/maasto/vesi.png");
        super.estääLiikkumisen = true;
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.asetaTiedot();
    }
}
