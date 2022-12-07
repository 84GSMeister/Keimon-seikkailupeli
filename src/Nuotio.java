import javax.swing.ImageIcon;

public class Nuotio extends Kiintopiste {
    
    private boolean sytyke = false;
    private boolean polttoaine = false;
    private boolean sytytetty = false;
    
    String kokeileEsinettä(Esine e) {
        
        String statusTeksti = "";
        if (tavoiteSuoritettu) {    
            statusTeksti = "Nuotion liekit leimuavat komeasti";
        }
        if (e instanceof Paperi) {
            this.sytyke = true;
            this.vuorovaikutus = true;
            statusTeksti = "Nuotioon lisättiin sytyke.";
        }
        else if (e instanceof Hiili) {
            this.polttoaine = true;
            this.vuorovaikutus = true;
            statusTeksti = "Nuotioon lisättiin palavaa ainetta.";
        }
        else if (e instanceof Kaasusytytin) {
            if (polttoaine){
                if (sytyke) {
                    this.tavoiteSuoritettu = true;
                    this.sytytetty = true;
                    this.kuvake = new ImageIcon("tiedostot/kuvat/nuotio.png");
                    statusTeksti = "Nuotio sytytettiin!";
                }
                else {
                    statusTeksti = "Nuotio tarvitsee jonkin sytykkeen.";
                }
            }
            else {
                statusTeksti = "Nuotio tarvitsee palavaa ainetta.";
            }
        }
        else if (e instanceof Vesiämpäri) {
            this.sytytetty = false;
            this.kuvake = new ImageIcon("tiedostot/kuvat/nuotio_sammunut.png");
            statusTeksti = "Nuotio sammutettiin";
        }
        else {
            System.out.println("Mitään ei tapahtunut.");
            statusTeksti = "Mitään ei tapahtunut.";
        }
        return statusTeksti;
        }

    String katso(){
        if (tavoiteSuoritettu) {    
            System.out.println("Nuotion liekit leimuavat komeasti");
            return "Nuotion liekit leimuavat komeasti";
        }
        else {
            if (sytyke && !polttoaine) {
                System.out.println("Pelkkä paperi ei pala kovin kauan. Tarvitaan jotain muuta palavaa.");
                return "Pelkkä paperi ei pala kovin kauan. Tarvitaan jotain muuta palavaa.";
            }
            else if (polttoaine && !sytyke) {
                System.out.println("Nuotiossa on poltettavaa, mutta tarvitaan vielä jotain sytykkeeksi");
                return "Nuotiossa on poltettavaa, mutta tarvitaan vielä jotain sytykkeeksi";
            }
            else if (sytyke && polttoaine) {
                System.out.println("Nuotiossa on polttoainetta ja sytykettä. Enää tarvitsee vain työkalun, jolla sen voi sytyttää.");
                return "Nuotiossa on polttoainetta ja sytykettä. Enää tarvitsee vain työkalun, jolla sen voi sytyttää.";
            }
            else {
                System.out.println("Nuotio on tyhjä. Siihen pitäisi varmaankin lisätä jotain palavaa");
                return "Nuotio on tyhjä. Siihen pitäisi varmaankin lisätä jotain palavaa";
            }            
        }
    }

    boolean onSytytetty () {
        return sytytetty;
    }

    String annaNimi() {
        return nimi;
    }

    String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Nuotio";
            case "genetiivi":
                return "Nuotion";
            case "esiivi":
                return "Nuotiona";
            case "partitiivi":
                return "Nuotiota";
            case "translatiivi":
                return "Nuotioksi";
            case "inessiivi":
                return "Nuotiossa";
            case "elatiivi":
                return "Nuotiosta";
            case "illatiivi":
                return "Nuotioon";
            case "adessiivi":
                return "Nuotiolla";
            case "ablatiivi":
                return "Nuotiolta";
            case "allatiivi":
                return "Nuotiolle";
            default:
                return "Nuotio";
        }
    }
    
    Nuotio() {
        this.nimi = "Nuotio";
        this.kuvake = new ImageIcon("tiedostot/kuvat/nuotio_sammunut.png");
    }
}
