import javax.swing.ImageIcon;

public class Kirstu extends Kiintopiste {
    
    String kokeileEsinettä(Esine e) {
        if (tavoiteSuoritettu) {
            return "Kirstu on jo avattu.";
        }
        else {
            if (e instanceof Avain) {
                this.vuorovaikutus = true;
                this.tavoiteSuoritettu = true;
                this.kuvake = new ImageIcon("tiedostot/kuvat/kirstu_avattu.png");
                return "Kirstu avattiin.";
            }
            else {
                return "Mitään ei tapahtunut.";
            }
        }
    }

    String katso(){
        if (tavoiteSuoritettu) {    
            return "Avattu kirstu";
        }
        else {
            return "Kirstu on lukittu. Siihen tarvitsee varmaankin avaimen. Löytyisiköhän kentältä sellainen?";
        }
    }

    String annaNimi() {
        return nimi;
    }

    String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Kirstu";
            case "genetiivi":
                return "Kirstun";
            case "esiivi":
                return "Kirstuna";
            case "partitiivi":
                return "Kirstua";
            case "translatiivi":
                return "Kirstuksi";
            case "inessiivi":
                return "Kirstussa";
            case "elatiivi":
                return "Kirstusta";
            case "illatiivi":
                return "Kirstuun";
            case "adessiivi":
                return "Kirstulla";
            case "ablatiivi":
                return "Kirstulta";
            case "allatiivi":
                return "Kirstulle";
            default:
                return "Kirstu";
        }
    }

    Kirstu() {
        this.nimi = "Kirstu";
        this.kuvake = new ImageIcon("tiedostot/kuvat/kirstu.png");
    }

    Kirstu(int sijX, int sijY) {
        this.määritettySijainti = true;
        this.sijX = sijX;
        this.sijY = sijY;
        this.nimi = "Kirstu";
        this.kuvake = new ImageIcon("tiedostot/kuvat/kirstu.png");
    }
}
