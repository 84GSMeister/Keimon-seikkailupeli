import javax.swing.ImageIcon;

public class Ämpärikone extends Kiintopiste{
    
    String kokeileEsinettä(Esine e) {
        return "Mitään ei tapahtunut.";
    }

    Vesiämpäri annaÄmpäri () {
        return new Vesiämpäri();
    }

    String katso(){
        return "Täältä saa ilmaisia ämpäreitä";
    }

    String annaNimi() {
        return nimi;
    }

    String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Ämpärikone";
            case "genetiivi":
                return "Ämpärikoneen";
            case "esiivi":
                return "Ämpärikoneena";
            case "partitiivi":
                return "Ämpärikonetta";
            case "translatiivi":
                return "Ämpärikoneeksi";
            case "inessiivi":
                return "Ämpärikoneessa";
            case "elatiivi":
                return "Ämpärikoneesta";
            case "illatiivi":
                return "Ämpärikoneeseen";
            case "adessiivi":
                return "Ämpärikoneella";
            case "ablatiivi":
                return "Ämpärikoneelta";
            case "allatiivi":
                return "Ämpärikoneelle";
            default:
                return "Ämpärikone";
        }
    }

    Ämpärikone() {
        super.nimi = "Ämpärikone";
        super.kuvake = new ImageIcon("tiedostot/kuvat/ämpärikone.png");
        super.asetaTiedot();
    }

    Ämpärikone(int sijX, int sijY) {
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.nimi = "Ämpärikone";
        super.kuvake = new ImageIcon("tiedostot/kuvat/ämpärikone.png");
        super.asetaTiedot();
    }
}
