package keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.TavoiteLista;
import keimo.seikkailupeli.toiminnot.Dialogit;

import java.util.ArrayList;

public final class JumalYoda extends NPC_KenttäKohde {

    private Renderöitävä pimeäTekstuuri = Assets.annaTekstuuri("jumalyoda_goblin");
    private Renderöitävä normaaliTekstuuri = Assets.annaTekstuuri("jumalyoda");

    public JumalYoda(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Jumal Yoda";
        super.tiedostonNimi = "goblin.png";
        super.tekstuuri = pimeäTekstuuri;
        super.dialogiTekstuuri = Assets.annaDialogiTekstuuri("dialogi_yoda");
        super.katsomisTeksti = "Polku pimeälle puolelle?";
        super.dialogit.add("metsä");
        super.dialogit.add("kuu");
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Vaikee kuvitella, että näitäkään ikinä tarvis, mutta laitetaan ihan vaan perfektionismin tähden.
        switch (sijamuoto) {
            case "nominatiivi":  return "Jumal Yoda";
            case "genetiivi":    return "Jumal Yodan";
            case "essiivi":      return "Jumal Yodana";
            case "partitiivi":   return "Jumal Yodaa";
            case "translatiivi": return "Jumal Yodaksi";
            case "inessiivi":    return "Jumal Yodassa";
            case "elatiivi":     return "Jumal Yodasta";
            case "illatiivi":    return "Jumal Yodaan";
            case "adessiivi":    return "Jumal Yodalla";
            case "ablatiivi":    return "Jumal Yodalta";
            case "allatiivi":    return "Jumal Yodalle";
            default:             return "Jumal Velho";
        }
    }

    @Override
    public void juttele() {
        switch (this.annaDialogi()) {
            case "metsä" -> {
                if (TavoiteLista.tavoiteLista.get("Avaa takahuone")) {
                    Dialogit.avaaDialogi(this.annaDialogiTekstuuri(), "Hrmm...", "Jumal Yoda");
                }
                else {
                    if (TavoiteLista.tavoiteLista.get("Löydä Jumal Yoda")) {
                        this.löydä(true);
                        Dialogit.avaaPitkäDialogiRuutu("goblin_alku");
                    }
                    else {
                        Dialogit.avaaPitkäDialogiRuutu("goblin_pimeä");
                    }
                }
            }
            case "kuu" -> {
                Dialogit.avaaPitkäDialogiRuutu("yoda_kuu");
            }
            case null, default -> {
                Dialogit.avaaDialogi(this.annaNimiSijamuodossa("allatiivi") + " ei ole määritetty dialogia " + "\"" + this.annaDialogi() + "\".", "Virheellinen dialogi");
            }
        }
    }

    public void löydä(boolean löydetty) {
        if (löydetty) this.tekstuuri = normaaliTekstuuri;
        else this.tekstuuri = pimeäTekstuuri;
    }
}
