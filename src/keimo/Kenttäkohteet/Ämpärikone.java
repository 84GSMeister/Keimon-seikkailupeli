package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Ämpärikone extends Kiintopiste{
    
    public String kokeileEsinettä(Esine e) {
        return "Mitään ei tapahtunut.";
    }

    Vesiämpäri annaÄmpäri () {
        return new Vesiämpäri();
    }

    public String katso(){
        return "Täältä saa ilmaisia ämpäreitä";
    }

    public String annaNimi() {
        return nimi;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
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

    public Ämpärikone() {
        super.nimi = "Ämpärikone";
        super.kuvake = new ImageIcon("tiedostot/kuvat/ämpärikone.png");
        super.asetaTiedot();
    }

    public Ämpärikone(int sijX, int sijY) {
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.nimi = "Ämpärikone";
        super.kuvake = new ImageIcon("tiedostot/kuvat/ämpärikone.png");
        super.asetaTiedot();
    }
}
