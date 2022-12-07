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
                System.out.println("Kirstu avattiin.");
                return "Kirstu avattiin.";
            }
            else {
                System.out.println("Mitään ei tapahtunut.");
                return "Mitään ei tapahtunut.";
            }
        }
    }

    String katso(){
        if (tavoiteSuoritettu) {    
            System.out.println("Avattu kirstu");
            return "Avattu kirstu";
        }
        else {
            System.out.println("Kirstu on lukittu. Siihen tarvitsee varmaankin avaimen. Löytyisiköhän kentältä sellainen?");
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
}
