package keimo.kenttäkohteet.kenttäNPC;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public final class Pasi extends NPC_KenttäKohde {

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Pasi";
            case "genetiivi":    return "Pasin";
            case "esiivi":       return "Pasina";
            case "partitiivi":   return "Pasia";
            case "translatiivi": return "Pasiksi";
            case "inessiivi":    return "Pasissa";
            case "elatiivi":     return "Pasista";
            case "illatiivi":    return "Pasiin";
            case "adessiivi":    return "Pasilla";
            case "ablatiivi":    return "Pasilta";
            case "allatiivi":    return "Pasille";
            default:             return "Pasi";
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case null, default: return katso();
        }
    }
    
    public Pasi (boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Pasi";
        super.tiedostonNimi = "pasi.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = annaNimiSijamuodossa("adessiivi") + " on kengät isonneet.";
        super.asetaTiedot();
    }
}
