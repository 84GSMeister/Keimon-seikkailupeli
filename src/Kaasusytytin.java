import javax.swing.ImageIcon;

public class Kaasusytytin extends Esine{
    
    String katso(){
        System.out.println("Tätä voisi käyttää nuotion sytyttämiseen.");
        return "Tätä voisi käyttää nuotion sytyttämiseen.";
    }

    String käytä(){
        System.out.println("Leimahti!");
        return "Leimahti!";
    }

    String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Toimiva kaasusytytin";
            case "genetiivi":
                return "Toimivan kaasusytyttimen";    
            case "esiivi":
                return "Toimivana kaasusytytimenä";
            case "partitiivi":
                return "Toimivaa kaasusytytintä";
            case "translatiivi":
                return "Toimivaksi kaasusytyttimeksi";
            case "inessiivi":
                return "Toimivassa kaasusytyttimessä";
            case "elatiivi":
                return "Toimivasta kaasusytyttimestä";
            case "illatiivi":
                return "Toimivaan kaasusytyttimeen";
            case "adessiivi":
                return "Toimivalla kaasusytyttimellä";
            case "ablatiivi":
                return "Toimivalta kaasusytyttimeltä";
            case "allatiivi":
                return "Toimivalle kaasusytyttimelle";
            default:
                return "Toimiva kaasusytytin";
        }
    }
    
    Kaasusytytin(){
        this.nimi = "Toimiva kaasusytytin";
        this.kenttäkäyttö = true;
        this.sopiiKäytettäväksi = "Nuotio";
        this.kuvake = new ImageIcon("tiedostot/kuvat/kaasusytytin.png");
    }
}
