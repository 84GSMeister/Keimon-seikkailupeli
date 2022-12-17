import javax.swing.ImageIcon;

public class Kaasusytytin extends Esine{
    
    String katso(){
        return katsomisTeksti;
    }

    String käytä(){
        return käyttöTeksti;
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
    
    Kaasusytytin(String toimivuus){
        switch (toimivuus) {
            case "toimiva":
                this.nimi = "Toimiva kaasusytytin";
                this.kenttäkäyttö = true;
                this.sopiiKäytettäväksi.add("Nuotio");
                this.kuvake = new ImageIcon("tiedostot/kuvat/kaasusytytin.png");
                this.katsomisTeksti = "Tätä voisi käyttää nuotion sytyttämiseen.";
                this.käyttöTeksti = "Leimahti!";
                break;
            case "tyhjä":
                this.nimi = "Tyhjä kaasusytytin";
                this.yhdistettävä = true;
                this.kelvollisetYhdistettävät.add("Kaasupullo");
                this.kuvake = new ImageIcon("tiedostot/kuvat/tyhjäkaasusytytin.png");
                this.katsomisTeksti = "Tästä puuttuu kaasupullo. Löytyisiköhän sellainen kentältä?";
                this.käyttöTeksti = "Kaasusytytin ei toimi ilman kaasupulloa.";
                break;
            default:
                this.nimi = "Toimiva kaasusytytin";
                this.kenttäkäyttö = true;
                this.sopiiKäytettäväksi.add("Nuotio");
                this.kuvake = new ImageIcon("tiedostot/kuvat/kaasusytytin.png");
                this.katsomisTeksti = "Tätä voisi käyttää nuotion sytyttämiseen.";
                this.käyttöTeksti = "Leimahti!";
                break;
        }
        
    }
}
