package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Ämpärikone extends Kiintopiste {
    
    public String kokeileEsinettä(Esine e) {
        return "Mitään ei tapahtunut.";
    }

    public static Vesiämpäri annaÄmpäri() {
        return new Vesiämpäri(false, 0, 0);
    }

    public String katso(){
        return "Täältä saa ilmaisia ämpäreitä";
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

    

    public Ämpärikone(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Ämpärikone";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/ämpärikone.png");
        super.asetaTiedot();
    }
}
