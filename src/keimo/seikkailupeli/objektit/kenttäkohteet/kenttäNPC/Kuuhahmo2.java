package keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC;

import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.toiminnot.Dialogit;

import java.util.ArrayList;

public class Kuuhahmo2 extends NPC_KenttäKohde{
    
    public Kuuhahmo2(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Kuuhahmo2";
        super.tiedostonNimi = "kuuhahmo_2.png";
        super.tekstuuri = Assets.annaTekstuuri("kuuhahmo2");
        super.katsomisTeksti = "Ei ole erityisen puheliaita paikalliset kaverit.";
        super.dialogit.add("vakio");
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Tätä ei tehdä siksi, että kaikkia näitä tarvitsisi pelissä. Tätä tehdään taiteen itsensä vuoksi.
        switch (sijamuoto) {
            case "nominatiivi":  return "Kuuhahmo";
            case "genetiivi":    return "Kuuhahmon";
            case "essiivi":      return "Kuuhahmona";
            case "partitiivi":   return "Kuuhahmoa";
            case "translatiivi": return "Kuuhahmoksi";
            case "inessiivi":    return "Kuuhahmossa";
            case "elatiivi":     return "Kuuhahmosta";
            case "illatiivi":    return "Kuuhahmoon";
            case "adessiivi":    return "Kuuhahmolla";
            case "ablatiivi":    return "Kuuhahmolta";
            case "allatiivi":    return "Kuuhahmolle";
            default:             return "Kuuhahmo";
        }
    }

    @Override
    public void juttele() {
        switch (this.annaDialogi()) {
            case "vakio" -> {
                Dialogit.avaaDialogi(this.annaTekstuuri(), this.haeDialogiTeksti("yee"), this.annaNimi());
            }
            case null, default -> {
                Dialogit.avaaDialogi(this.annaNimiSijamuodossa("allatiivi") + " ei ole määritetty dialogia " + "\"" + this.annaDialogi() + "\".", "Virheellinen dialogi");
            }
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "yee": return "Yee!";
            case null, default: return katso();
        }
    }
}
