package keimo.kenttäkohteet.kenttäNPC;

import keimo.PääIkkuna;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;

import javax.swing.ImageIcon;

public final class Pasi extends NPC_KenttäKohde {
    @Override
    public void näytäDialogi(Esine e) {
        if (TavoiteLista.nykyinenTavoite.startsWith("Etsi Pasi")) TavoiteLista.suoritaPääTavoite(5);
        PääIkkuna.avaaPitkäDialogiRuutu("pasi");
    }

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
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/pasi_dialogi.png");
        super.katsomisTeksti = annaNimiSijamuodossa("adessiivi") + " on kengät isonneet.";
        super.asetaTiedot();
    }
}
