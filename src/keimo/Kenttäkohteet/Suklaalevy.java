package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Suklaalevy extends Ruoka {

    public String katso() {
        return "Voisin syödä tämän.";
    }
    
    public String käytä(){
        poista = true;
        return "Se maistui hyvältä";
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Suklaalevy";
            case "genetiivi":
                return "Suklaalevyn";
            case "esiivi":
                return "Suklaalevynä";
            case "partitiivi":
                return "Suklaalevyä";
            case "translatiivi":
                return "Suklaalevyksi";
            case "inessiivi":
                return "Suklaalevyssä";
            case "elatiivi":
                return "Suklaalevystä";
            case "illatiivi":
                return "Suklaalevyyn";
            case "adessiivi":
                return "Suklaalevyllä";
            case "ablatiivi":
                return "Suklaalevyltä";
            case "allatiivi":
                return "Suklaalevylle";
            default:
                return "Suklaalevy";
        }
    }

    public Suklaalevy(){
        super.nimi = "Suklaalevy";
        super.kuvake = new ImageIcon("tiedostot/kuvat/suklaalevy.png");
        super.heal = 2;
        super.käyttö = true;
        super.asetaTiedot();
    }

    public Suklaalevy(int sijX, int sijY){
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.nimi = "Suklaalevy";
        super.kuvake = new ImageIcon("tiedostot/kuvat/suklaalevy.png");
        super.heal = 2;
        super.käyttö = true;
        super.asetaTiedot();
    }
}
