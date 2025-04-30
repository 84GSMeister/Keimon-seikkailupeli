package keimo.kenttäkohteet.kiintopiste;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public final class Silta extends Kiintopiste {

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        // Silta s = new Silta(false, 0, 0);
        // System.out.println("Tekis mieli hypätä " + s.annaNimiSijamuodossa("ablatiivi") + " alas.");
        // ^ Tekis mieli hypätä Sillalta alas.
        switch (sijamuoto) {
            case "nominatiivi":  return "Silta";
            case "genetiivi":    return "Sillan";
            case "esiivi":       return "Siltana";
            case "partitiivi":   return "Siltaa";
            case "translatiivi": return "Sillaksi";
            case "inessiivi":    return "Sillassa";
            case "elatiivi":     return "Sillasta";
            case "illatiivi":    return "Siltaan";
            case "adessiivi":    return "Sillalla";
            case "ablatiivi":    return "Sillalta";
            case "allatiivi":    return "Sillalle";
            default:             return "Silta";
        }
    }
    
    public Silta (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Silta";
        super.tiedostonNimi = "asfaltti_silta.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Kiva näkymä";
        super.asetaTiedot();
    }
}
