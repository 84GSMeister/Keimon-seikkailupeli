package keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC;

import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.toiminnot.Dialogit;

import java.util.ArrayList;

public final class Kauppias extends NPC_KenttäKohde {

    public Kauppias(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Kauppias";
        super.tiedostonNimi = "kauppias.png";
        super.tekstuuri = Assets.annaTekstuuri("kauppias");
        super.katsomisTeksti = "Kylien kauppias";
        super.dialogit.add("vakio");
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
        switch (this.annaDialogi()) {
            case "vakio" -> {
                Dialogit.avaaDialogi(this.annaDialogiTekstuuri(), this.haeDialogiTeksti("juttele"), this.annaNimi());
            }
            case null, default -> {
                Dialogit.avaaDialogi(this.annaNimiSijamuodossa("allatiivi") + " ei ole määritetty dialogia " + "\"" + this.annaDialogi() + "\".", "Virheellinen dialogi");
            }
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "juttele": return "Asioi tiskin toiselta puolelta.";
            case null, default: return katso();
        }
    }
}
