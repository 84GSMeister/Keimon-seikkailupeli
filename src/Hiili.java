import javax.swing.ImageIcon;

public class Hiili extends Esine {

    String katso(){
        System.out.println("Tämä sopisi hyvin nuotiossa poltettavaksi.");
        return "Tämä sopisi hyvin nuotiossa poltettavaksi.";
    }

    String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Hiili";
            case "genetiivi":
                return "Hiilen";
            case "esiivi":
                return "Hiilenä";
            case "partitiivi":
                return "Hiiltä";
            case "translatiivi":
                return "Hiileksi";
            case "inessiivi":
                return "Hiilessä";
            case "elatiivi":
                return "Hiilestä";
            case "illatiivi":
                return "Hiileen";
            case "adessiivi":
                return "Hiilellä";
            case "ablatiivi":
                return "Hiileltä";
            case "allatiivi":
                return "Hiilelle";
            default:
                return "Hiili";
        }
    }

    Hiili(){
        this.nimi = "Hiili";
        this.kenttäkäyttö = true;
        this.sopiiKäytettäväksi.add("Nuotio");
        this.kuvake = new ImageIcon("tiedostot/kuvat/hiili.png");
    }
}
