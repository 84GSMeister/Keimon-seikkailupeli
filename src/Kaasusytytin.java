import javax.swing.ImageIcon;

public class Kaasusytytin extends Esine{
    
    boolean toimiva;

    String katso(){
        return katsomisTeksti;
    }

    String käytä(){
        return käyttöTeksti;
    }

    String annaNimiSijamuodossa(String sijamuoto) {
        if (toimiva){
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
        else {
            switch (sijamuoto) {
                case "nominatiivi":
                    return "Tyhjä kaasusytytin";
                case "genetiivi":
                    return "Tyhjän kaasusytyttimen";    
                case "esiivi":
                    return "Tyhjänä kaasusytytimenä";
                case "partitiivi":
                    return "Tyhjää kaasusytytintä";
                case "translatiivi":
                    return "Tyhjäksi kaasusytyttimeksi";
                case "inessiivi":
                    return "Tyhjässä kaasusytyttimessä";
                case "elatiivi":
                    return "Tyhjästä kaasusytyttimestä";
                case "illatiivi":
                    return "Tyhjään kaasusytyttimeen";
                case "adessiivi":
                    return "Tyhjällä kaasusytyttimellä";
                case "ablatiivi":
                    return "Tyhjältä kaasusytyttimeltä";
                case "allatiivi":
                    return "Tyhjälle kaasusytyttimelle";
                default:
                    return "Tyhjä kaasusytytin";
            }
        }
    }
    
    Kaasusytytin(String toimivuus){
        switch (toimivuus) {
            case "toimiva":
                this.nimi = "Toimiva kaasusytytin";
                this.toimiva = true;
                this.kenttäkäyttö = true;
                this.sopiiKäytettäväksi.add("Nuotio");
                this.kuvake = new ImageIcon("tiedostot/kuvat/kaasusytytin.png");
                this.katsomisTeksti = "Tätä voisi käyttää nuotion sytyttämiseen.";
                this.käyttöTeksti = "Leimahti!";
                break;
            case "tyhjä":
                this.nimi = "Tyhjä kaasusytytin";
                this.toimiva = false;
                this.yhdistettävä = true;
                this.kelvollisetYhdistettävät.add("Kaasupullo");
                this.kuvake = new ImageIcon("tiedostot/kuvat/tyhjäkaasusytytin.png");
                this.katsomisTeksti = "Tästä puuttuu kaasupullo. Löytyisiköhän sellainen kentältä?";
                this.käyttöTeksti = "Kaasusytytin ei toimi ilman kaasupulloa.";
                break;
            default:
                this.nimi = "Toimiva kaasusytytin";
                this.toimiva = true;
                this.kenttäkäyttö = true;
                this.sopiiKäytettäväksi.add("Nuotio");
                this.kuvake = new ImageIcon("tiedostot/kuvat/kaasusytytin.png");
                this.katsomisTeksti = "Tätä voisi käyttää nuotion sytyttämiseen.";
                this.käyttöTeksti = "Leimahti!";
                break;
        }
        
    }
}
