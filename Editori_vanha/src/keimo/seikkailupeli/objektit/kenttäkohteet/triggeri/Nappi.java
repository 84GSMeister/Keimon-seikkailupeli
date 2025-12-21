package keimo.seikkailupeli.objektit.kenttäkohteet.triggeri;


public class Nappi extends Triggeri {

    public Nappi(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Nappi";
        super.tiedostonNimi = "nappi.png";
        super.katsomisTeksti = "Mitähän tästä tapahtuu?";
        super.vaadittuEsine = null;
        super.asetaTiedot();
    }

    @Override
    public void triggeröi() {
        if (!super.onkoTriggeröity()) {
            super.triggeröi();
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        // Nappi n = new Nappi(false, 0, 0);
        // System.out.println("Ei mennyt ihan " + n.annaNimiSijamuodossa("illatiivi") + ".");
        // ^ Ei mennyt ihan Nappiin.
        switch (sijamuoto) {
            case "nominatiivi":  return "Nappi";
            case "genetiivi":    return "Napin";
            case "essiivi":      return "Nappina";
            case "partitiivi":   return "Nappia";
            case "translatiivi": return "Napiksi";
            case "inessiivi":    return "Napissa";
            case "elatiivi":     return "Napista";
            case "illatiivi":    return "Nappiin";
            case "adessiivi":    return "Napilla";
            case "ablatiivi":    return "Napilta";
            case "allatiivi":    return "Napille";
            default:             return "Makkara";
        }
    }
}
