import javax.swing.ImageIcon;

public class Hiili extends Esine {

    String katso(){
        return katsomisTeksti;
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
        super.nimi = "Hiili";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Nuotio");
        super.kuvake = new ImageIcon("tiedostot/kuvat/hiili.png");
        super.katsomisTeksti = "Tämä sopisi hyvin nuotiossa poltettavaksi.";
        super.asetaTiedot();
    }

    Hiili(int sijX, int sijY){
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.nimi = "Hiili";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Nuotio");
        super.kuvake = new ImageIcon("tiedostot/kuvat/hiili.png");
        super.katsomisTeksti = "Tämä sopisi hyvin nuotiossa poltettavaksi.";
        super.asetaTiedot();
    }
}
