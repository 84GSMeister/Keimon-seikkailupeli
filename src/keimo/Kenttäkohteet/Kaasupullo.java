package keimo.Kenttäkohteet;

import javax.swing.ImageIcon;

public final class Kaasupullo extends Esine {

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Kaasupullo";
            case "genetiivi": return "Kaasupullon";
            case "esiivi": return "Kaasupullona";
            case "partitiivi": return "Kaasupulloa";
            case "translatiivi": return "Kaasupulloksi";
            case "inessiivi": return "Kaasupullossa";
            case "elatiivi": return "Kaasupullosta";
            case "illatiivi": return "Kaasupulloon";
            case "adessiivi": return "Kaasupullolla";
            case "ablatiivi": return "Kaasupullolta";
            case "allatiivi": return "Kaasupullolle";
            default: return "Kaasupullo";
        }
    }

    public Kaasupullo(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kaasupullo";
        super.yhdistettävä = true;
        super.kelvollisetYhdistettävät.add("Kaasusytytin");
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kaasupullo.png");
        super.katsomisTeksti = "Tätä tarvitaan varmaankin kaasusytyttimen kanssa.";
        super.asetaTiedot();
    }
}
