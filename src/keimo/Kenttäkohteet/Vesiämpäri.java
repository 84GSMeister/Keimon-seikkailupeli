package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Vesiämpäri extends Esine {

    public String katso() {
        return "Näyttää juuri oikean kokoiselta vihollisen päähän.";
    }

    public String käytä() {
        this.poista = true;
        return "Vihollinen ämpäröitiin ja on nyt harmiton.";
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
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

    public Vesiämpäri(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        this.nimi = "Vesiämpäri";
        this.kenttäkäyttö = true;
        this.sopiiKäytettäväksi.add("Nuotio");
        this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/vesiämpäri.png");
        super.asetaTiedot();
    }
}
