package keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC;

import java.util.ArrayList;

public final class Kauppias extends NPC_KenttäKohde {

    public Kauppias(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Kauppias";
        super.tiedostonNimi = "kauppias.png";
        super.katsomisTeksti = "Kylien kauppias";
        super.dialogit.add("vakio");
        if (ominaisuusLista == null) super.valitseVakioDialogi();
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kauppias";
            case "genetiivi":    return "Kauppiaan";
            case "essiivi":      return "Kauppiaana";
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
    public void juttele() {

    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "juttele": return "Asioi tiskin toiselta puolelta.";
            case null, default: return katso();
        }
    }
}
