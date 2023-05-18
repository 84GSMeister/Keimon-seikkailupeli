package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Suklaalevy extends Ruoka {

    @Override
    public String käytä(){
        poista = true;
        return "Se maistui hyvältä. Sait " + this.heal + " elämäpistettä.";
    }
    
    @Override
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

    public Suklaalevy(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Suklaalevy";
        super.katsomisTeksti = "Voisin syödä tämän.";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/suklaalevy.png");
        super.heal = 2;
        super.käyttö = true;
        super.asetaTiedot();
    }
}
