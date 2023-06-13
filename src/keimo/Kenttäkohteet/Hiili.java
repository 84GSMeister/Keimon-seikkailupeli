package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Hiili extends Esine {

    @Override
    public String käytä() {
        super.poista = true;
        return super.käytä();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
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

    public Hiili(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Hiili";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Nuotio");
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/hiili.png");
        super.katsomisTeksti = "Tämä sopisi hyvin nuotiossa poltettavaksi.";
        super.asetaTiedot();
    }
}
