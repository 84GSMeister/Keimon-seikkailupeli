package keimo.Kenttäkohteet;

import java.util.ArrayList;
import javax.swing.ImageIcon;

import keimo.PääIkkuna;

public class Juhani extends NPC_KenttäKohde{

    public ArrayList<String> kelvollisetEsineet = new ArrayList<String>();

    public Huume annaHuume() {
        return new Huume(false, 0, 0);
    }

    public Esine suoritaMuutoksetEsineelle(Esine e) {
        if (e instanceof Seteli) {
            e = annaHuume();
            PääIkkuna.hudTeksti.setText("Ostit Juhanilta huumeen.");
            return e;
        }
        else {
            PääIkkuna.hudTeksti.setText(e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + this.annaNimiSijamuodossa("illatiivi"));
            return e;
        }
    }
    
    public Juhani(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Juhani";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/juhani.png");
        super.katsomisTeksti = "Osta Juhanilta kahel kybäl yksi huume pois.";
        this.kelvollisetEsineet.add("Seteli");
        super.asetaTiedot();
    }
}
