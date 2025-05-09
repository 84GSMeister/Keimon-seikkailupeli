package keimo.kenttäkohteet.kenttäNPC;

import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.toiminnot.Dialogit;

import java.util.ArrayList;

public final class Pasi extends NPC_KenttäKohde {

    public Pasi(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Pasi";
        super.tiedostonNimi = "pasi.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = annaNimiSijamuodossa("adessiivi") + " on kengät isonneet.";
        super.dialogit.add("baari");
        super.dialogit.add("kuu");
        if (ominaisuusLista == null) super.valitseVakioDialogi();
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        // Tässähän jopa tarvittiin uutta muotoa oikeassa lauseessa pitkästä aikaa!
        switch (sijamuoto) {
            case "nominatiivi":  return "Pasi";
            case "genetiivi":    return "Pasin";
            case "essiivi":      return "Pasina";
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
    public void juttele() {
        if (TavoiteLista.nykyinenTavoite.startsWith("Etsi Pasi")) TavoiteLista.suoritaPääTavoite(5);
        switch (this.annaDialogi()) {
            case "baari": Dialogit.avaaPitkäDialogiRuutu("pasi"); break;
            case "kuu": Dialogit.avaaPitkäDialogiRuutu("pasi_kuu"); break;
            case null, default: Dialogit.avaaDialogi("", "Objektille " + this.annaNimi() + " ei ole määritetty dialogia " + "\"" + this.annaDialogi() + "\".", "Virheellinen dialogi"); break;
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case null, default: return katso();
        }
    }
}
