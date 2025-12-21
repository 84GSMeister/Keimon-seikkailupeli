package keimo.seikkailupeli.objektit.kenttäkohteet.esine;

import keimo.seikkailupeli.objektit.Pelaaja;

public class Jallupullo extends Juoma {

    public Jallupullo(int sijX, int sijY){
        super(sijX, sijY);
        super.nimi = "Jallupullo";
        super.tiedostonNimi = "jallupullo.png";
        super.katsomisTeksti = "Elämän eliksiiri.";
        super.käyttö = true;
        super.yhdistettävä = true;
        super.kelvollisetYhdistettävät.add("Paskanmarjat");
        super.hinta = 18;
        super.voltit = 4;
        super.känniKuolemattomuus = 4000;
        super.asetaTiedot();
    }

    @Override
    public String käytä(){
        super.käytä();
        Pelaaja.kuparit++;
        return katso();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        // Jos joskus ajattelet, että työsi on turhaa, mieti sitä kaveria, joka kirjoittaa näitä.
        switch (sijamuoto) {
            case "nominatiivi":  return "Jallupullo";
            case "genetiivi":    return "Jallupullon";
            case "esiivi":       return "Jallupullona";
            case "partitiivi":   return "Jallupulloa";
            case "translatiivi": return "Jallupulloksi";
            case "inessiivi":    return "Jallupullossa";
            case "elatiivi":     return "Jallupullosta";
            case "illatiivi":    return "Jallupulloon";
            case "adessiivi":    return "Jallupullolla";
            case "ablatiivi":    return "Jallupullolta";
            case "allatiivi":    return "Jallupullolle";
            default:             return "Jallupullo";
        }
    }
}
