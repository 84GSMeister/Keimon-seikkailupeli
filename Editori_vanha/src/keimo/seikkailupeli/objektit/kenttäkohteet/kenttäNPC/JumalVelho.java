package keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC;

import keimo.editori.TavoiteEditori.TavoiteLista;

import java.util.ArrayList;

public final class JumalVelho extends NPC_KenttäKohde {

    public JumalVelho(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Jumal Velho";
        super.tiedostonNimi = "velho.png";
        super.katsomisTeksti = "No se on se Jumal Velho!";
        super.dialogit.add("metsä");
        super.dialogit.add("kuu");
        if (ominaisuusLista == null) super.valitseVakioDialogi();
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Jumal Velho";
            case "genetiivi":    return "Jumal Velhon";
            case "essiivi":      return "Jumal Velhona";
            case "partitiivi":   return "Jumal Velhoa";
            case "translatiivi": return "Jumal Velhoksi";
            case "inessiivi":    return "Jumal Velhossa";
            case "elatiivi":     return "Jumal Velhosta";
            case "illatiivi":    return "Jumal Velhoon";
            case "adessiivi":    return "Jumal Velholla";
            case "ablatiivi":    return "Jumal Velholta";
            case "allatiivi":    return "Jumal Velholle";
            default:             return "Jumal Velho";
        }
    }

    @Override
    public void juttele() {

    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "löydä": return "Tervetuloa kaikki mun bordello dello dello dello dello dello dello delloon";
            case "anna_paskanmarjat": return "Näin se booli keittyy,\n paskanmarjat jaloviinaan peittyy.";
            case "booli_vinkki": return "Jumal velhon booli: tarvitaan vain oikeat ainekset (pontikkaa ja jallua)";
            case "kuu": return "Jumal Velhon boolilla pääsee kuuhun!";
            case null, default: return katso();
        }
    }

    public void löydäJumalVelho() {
        TavoiteLista.suoritaTavoite("Löydä Jumal Yoda");
    }

    public boolean löydetty() {
        return TavoiteLista.tavoiteLista.get("Löydä Jumal Yoda");
    }
}
