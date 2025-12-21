package keimo.seikkailupeli.objektit.kenttäkohteet.kerättävä;

import keimo.seikkailupeli.objektit.Pelaaja;

public final class Seteli extends Kerättävä {

    public Seteli(int sijX, int sijY){
        super(sijX, sijY);
        super.nimi = "Seteli";
        super.tiedostonNimi = "seteli.png";
        super.asetaTiedot();
    }

    @Override
    public void kerää() {
        Pelaaja.raha += 20;
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Seteli";
            case "genetiivi":    return "Setelin";
            case "essiivi":      return "Setelinä";
            case "partitiivi":   return "Seteliä";
            case "translatiivi": return "Seteliksi";
            case "inessiivi":    return "Setelissä";
            case "elatiivi":     return "Setelistä";
            case "illatiivi":    return "Seteliin";
            case "adessiivi":    return "Setelillä";
            case "ablatiivi":    return "Seteliltä";
            case "allatiivi":    return "Setelille";
            default:             return "Seteli";
        }
    }
}
