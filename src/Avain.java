import javax.swing.ImageIcon;

public class Avain extends Esine {
    
    String katso(){
        System.out.println("Onkohan kentällä jotain lukittua, johon tätä voisi käyttää?.");
        return "Onkohan kentällä jotain lukittua, johon tätä voisi käyttää?.";
    }

    String käytä(){
        return "";
    }

    String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Avain";
            case "genetiivi":
                return "Avaimen";
            case "esiivi":
                return "Avaimena";
            case "partitiivi":
                return "Avainta";
            case "translatiivi":
                return "Avaimeksi";
            case "inessiivi":
                return "Avaimessa";
            case "elatiivi":
                return "Avaimesta";
            case "illatiivi":
                return "Avaimeen";
            case "adessiivi":
                return "Avaimella";
            case "ablatiivi":
                return "Avaimelta";
            case "allatiivi":
                return "Avaimelle";
            default:
                return "Avain";
        }
    }

    Avain(){
        this.nimi = "Avain";
        this.kenttäkäyttö = true;
        this.sopiiKäytettäväksi = "Kirstu";
        this.kuvake = new ImageIcon("tiedostot/kuvat/avain.png");
    }
}
