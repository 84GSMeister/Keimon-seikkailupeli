package keimo.Kenttäkohteet;

import javax.swing.ImageIcon;

public class Seteli extends Esine{
    
    public Seteli(int sijX, int sijY){
        this.määritettySijainti = true;
        this.sijX = sijX;
        this.sijY = sijY;
        this.nimi = "Seteli";
        this.kenttäkäyttö = true;
        this.sopiiKäytettäväksi.add("Juhani");
        this.kuvake = new ImageIcon("tiedostot/kuvat/seteli.png");
        super.asetaTiedot();
    }
}
