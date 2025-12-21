package keimo.seikkailupeli.objektit.kenttäkohteet.esine;

public class Kartta extends Esine {

    public Kartta(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Kartta";
        super.tiedostonNimi = "kartta.png";
        super.katsomisTeksti = "Parempi vilkaista karttaa, jos on eksynyt.";
        super.käyttö = true;
        super.asetaTiedot();
    }

    @Override
    public String käytä() {
        return katso();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kartta";
            case "genetiivi":    return "Kartan";
            case "esiivi":       return "Karttana";
            case "partitiivi":   return "Karttaa";
            case "translatiivi": return "Kartaksi";
            case "inessiivi":    return "Kartassa";
            case "elatiivi":     return "Kartasta";
            case "illatiivi":    return "Karttaan";
            case "adessiivi":    return "Kartalla";
            case "ablatiivi":    return "Kartalta";
            case "allatiivi":    return "Kartalle";
            default:             return "Kartta";
        }
    }
}
