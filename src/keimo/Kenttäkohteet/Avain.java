package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Avain extends Esine {
    
    public String katso(){
        return katsomisTeksti;
    }

    public String käytä(){
        poista = true;
        return käyttöTeksti;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
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

    public Avain(){
        super.nimi = "Avain";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Kirstu");
        super.kuvake = new ImageIcon("tiedostot/kuvat/avain.png");
        super.katsomisTeksti = "Onkohan kentällä jotain lukittua, johon tätä voisi käyttää?.";
        super.asetaTiedot();
    }

    public Avain(int sijX, int sijY){
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.nimi = "Avain";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Kirstu");
        super.kuvake = new ImageIcon("tiedostot/kuvat/avain.png");
        super.katsomisTeksti = "Onkohan kentällä jotain lukittua, johon tätä voisi käyttää?.";
        super.asetaTiedot();
    }
}
