import javax.swing.ImageIcon;

public class Vesiämpäri extends Esine {

    String katso() {
        return "Näyttää juuri oikean kokoiselta vihollisen päähän.";
    }

    String käytä() {
        this.poista = true;
        return "Vihollinen ämpäröitiin ja on nyt harmiton.";
    }

    String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Vesiämpäri";
            case "genetiivi":
                return "Vesiämpärin";
            case "esiivi":
                return "Vesiämpärinä";
            case "partitiivi":
                return "Vesiämpäriä";
            case "translatiivi":
                return "Vesiämpäriksi";
            case "inessiivi":
                return "Vesiämpärissä";
            case "elatiivi":
                return "Vesiämpäristä";
            case "illatiivi":
                return "Vesiämpäriin";
            case "adessiivi":
                return "Vesiämpärillä";
            case "ablatiivi":
                return "Vesiämpäriltä";
            case "allatiivi":
                return "Vesiämpärille";
            default:
                return "Vesiämpäri";
        }
    }
    
    Vesiämpäri(){
        this.nimi = "Vesiämpäri";
        this.kenttäkäyttö = true;
        this.sopiiKäytettäväksi.add("Nuotio");
        this.kuvake = new ImageIcon("tiedostot/kuvat/vesiämpäri.png");
    }

    Vesiämpäri(int sijX, int sijY){
        this.määritettySijainti = true;
        this.sijX = sijX;
        this.sijY = sijY;
        this.nimi = "Vesiämpäri";
        this.kenttäkäyttö = true;
        this.sopiiKäytettäväksi.add("Nuotio");
        this.kuvake = new ImageIcon("tiedostot/kuvat/vesiämpäri.png");
    }
}
