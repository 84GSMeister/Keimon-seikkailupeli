package keimo.Kenttäkohteet;

import javax.swing.ImageIcon;

public class Huume extends Ruoka{

    public String käytä(){
        poista = true;
        return "Never stop the madness!";
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Huume";
            case "genetiivi":
                return "Huumeen";
            case "esiivi":
                return "Huumeena";
            case "partitiivi":
                return "Huumetta";
            case "translatiivi":
                return "Huumeeksi";
            case "inessiivi":
                return "Huumeessa";
            case "elatiivi":
                return "Huumeesta";
            case "illatiivi":
                return "Huumeeseen";
            case "adessiivi":
                return "Huumeella";
            case "ablatiivi":
                return "Huumeelta";
            case "allatiivi":
                return "Huumeelle";
            default:
                return "Huume";
        }
    }
    
    public Huume(int sijX, int sijY){
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.nimi = "Huume";
        super.kuvake = new ImageIcon("tiedostot/kuvat/huume.png");
        super.käyttö = true;
        super.heal = 30;
        super.asetaTiedot();
    }
}
