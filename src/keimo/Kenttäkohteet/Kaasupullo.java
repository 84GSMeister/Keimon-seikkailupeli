package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Kaasupullo extends Esine {

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Kaasupullo";
            case "genetiivi":
                return "Kaasupullon";
            case "esiivi":
                return "Kaasupullona";
            case "partitiivi":
                return "Kaasupulloa";
            case "translatiivi":
                return "Kaasupulloksi";
            case "inessiivi":
                return "Kaasupullossa";
            case "elatiivi":
                return "Kaasupullosta";
            case "illatiivi":
                return "Kaasupulloon";
            case "adessiivi":
                return "Kaasupullolla";
            case "ablatiivi":
                return "Kaasupullolta";
            case "allatiivi":
                return "Kaasupullolle";
            default:
                return "Kaasupullo";
        }
    }

    public Kaasupullo(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        this.nimi = "Kaasupullo";
        this.yhdistettävä = true;
        this.kelvollisetYhdistettävät.add("Kaasusytytin");
        this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kaasupullo.png");
        this.katsomisTeksti = "Tätä tarvitaan varmaankin kaasusytyttimen kanssa.";
        super.asetaTiedot();
    }
}
