package keimo.kenttäkohteet.kiintopiste;

import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;

import javax.swing.ImageIcon;

public final class KaljaAutomaatti extends Kiintopiste {

    @Override
    public String vuorovaikuta(Esine e) {
        return katsomisTeksti;
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Tässä todistamme jälleen kauniin kielemme kukkasia.
        switch (sijamuoto) {
            case "nominatiivi":  return "Kalja-automaatti";
            case "genetiivi":    return "Kalja-automaatin";
            case "esiivi":       return "Kalja-automaattina";
            case "partitiivi":   return "Kalja-automaattia";
            case "translatiivi": return "Kalja-automaatiksi";
            case "inessiivi":    return "Kalja-automaatissa";
            case "elatiivi":     return "Kalja-automaatista";
            case "illatiivi":    return "Kalja-automaattiin";
            case "adessiivi":    return "Kalja-automaatilla";
            case "ablatiivi":    return "Kalja-automaatilta";
            case "allatiivi":    return "Kalja-automaatille";
            default:             return "Kalja-automaatti";
        }
    }

    public KaljaAutomaatti(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Kalja-automaatti";
        super.tiedostonNimi = "kalja-automaatti.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Tähän spawnaa kaljaa";
        super.erillisDialogi = true;
        super.ignooraaEsineValintaDialogissa = true;
        super.ohitaDialogiTesktiboksi = true;
        super.asetaTiedot();
    }
}
