package keimo.Kenttäkohteet;

import javax.swing.ImageIcon;

public class Pesäpallomaila extends Esine{

    public String käytä() {
        return käyttöTeksti;
    }

    public Pesäpallomaila(int sijX, int sijY){
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.nimi = "Pesäpallomaila";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Pikkuvihu");
        super.kuvake = new ImageIcon("tiedostot/kuvat/pesäpallomaila.png");
        super.käyttöTeksti = "Löit vihollista turpaan";
        super.asetaTiedot();
    }
}
