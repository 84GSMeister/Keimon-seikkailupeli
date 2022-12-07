import javax.swing.ImageIcon;

public class Ämpärikone extends Kiintopiste{
    
    String kokeileEsinettä(Esine e) {
        if (tavoiteSuoritettu) {
            return "Kirstu on jo avattu.";
        }
        else {
            if (e instanceof Avain) {
                this.vuorovaikutus = true;
                this.tavoiteSuoritettu = true;
                this.kuvake = new ImageIcon("tiedostot/kuvat/kirstu_avattu.png");
                System.out.println("Kirstu avattiin.");
                return "Kirstu avattiin.";
            }
            else {
                System.out.println("Mitään ei tapahtunut.");
                return "Mitään ei tapahtunut.";
            }
        }
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
        this.nimi = "Ämpärikone";
        this.kuvake = new ImageIcon("tiedostot/kuvat/ämpärikone.png");
    }
}
