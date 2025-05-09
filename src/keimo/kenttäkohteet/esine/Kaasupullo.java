package keimo.kenttäkohteet.esine;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public final class Kaasupullo extends Esine {

    public Kaasupullo(int sijX, int sijY){
        super(sijX, sijY);
        super.nimi = "Kaasupullo";
        super.tiedostonNimi = "kaasupullo.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Tätä tarvitaan varmaankin kaasusytyttimen kanssa.";
        super.yhdistettävä = true;
        super.kelvollisetYhdistettävät.add("Kaasusytytin");
        super.liikeNopeus = 6f;
        super.pyörimisNopeus = 2f;
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kaasupullo";
            case "genetiivi":    return "Kaasupullon";
            case "esiivi":       return "Kaasupullona";
            case "partitiivi":   return "Kaasupulloa";
            case "translatiivi": return "Kaasupulloksi";
            case "inessiivi":    return "Kaasupullossa";
            case "elatiivi":     return "Kaasupullosta";
            case "illatiivi":    return "Kaasupulloon";
            case "adessiivi":    return "Kaasupullolla";
            case "ablatiivi":    return "Kaasupullolta";
            case "allatiivi":    return "Kaasupullolle";
            default:             return "Kaasupullo";
        }
    }
}
