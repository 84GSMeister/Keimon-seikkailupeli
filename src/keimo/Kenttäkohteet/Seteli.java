package keimo.Kenttäkohteet;

import javax.swing.ImageIcon;

public class Seteli extends Esine{
    
    public Seteli(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        this.nimi = "Seteli";
        this.kenttäkäyttö = true;
        this.sopiiKäytettäväksi.add("Juhani");
        this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/seteli.png");
        super.asetaTiedot();
    }
}
