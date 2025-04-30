package keimo.kenttäkohteet.kenttäNPC;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public final class Kauppias extends NPC_KenttäKohde {

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kauppias";
            case "genetiivi":    return "Kauppiaan";
            case "esiivi":       return "Kauppiaana";
            case "partitiivi":   return "Kauppiasta";
            case "translatiivi": return "Kauppiaaksi";
            case "inessiivi":    return "Kauppiaassa";
            case "elatiivi":     return "Kauppiaasta";
            case "illatiivi":    return "Kauppiaaseen";
            case "adessiivi":    return "Kauppiaalla";
            case "ablatiivi":    return "Kauppiaalta";
            case "allatiivi":    return "Kauppiaalle";
            default:             return "Kauppias";
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "juttele": return "Asioi tiskin toiselta puolelta.";
            case null, default: return katso();
        }
    }
    
    public Kauppias (boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kauppias";
        super.tiedostonNimi = "kauppias.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Kylien kauppias";
        super.asetaTiedot();
    }
}
