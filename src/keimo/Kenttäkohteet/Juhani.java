package keimo.Kenttäkohteet;

import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Juhani extends NPC_KenttäKohde{

    public ArrayList<String> kelvollisetEsineet = new ArrayList<String>();

    public Huume annaHuume() {
        return new Huume(0, 0);
    }
    
    public Juhani(int sijX, int sijY){
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.nimi = "Juhani";
        super.kuvake = new ImageIcon("tiedostot/kuvat/juhani.png");
        super.katsomisTeksti = "Osta Juhanilta kahel kybäl yksi huume pois.";
        this.kelvollisetEsineet.add("Seteli");
        super.asetaTiedot();
    }
}
