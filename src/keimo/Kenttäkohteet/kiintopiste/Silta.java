package keimo.kenttäkohteet.kiintopiste;

import keimo.Ruudut.Lisäruudut.ValintaDialogiRuutu;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;

import javax.swing.ImageIcon;

public final class Silta extends Kiintopiste {

    @Override
    public String vuorovaikuta(Esine e) {
        ValintaDialogiRuutu.luoValintaDialogiIkkuna("silta");
        return katsomisTeksti;
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        // Silta s = new Silta(false, 0, 0);
        // System.out.println("Tekis mieli hypätä " + s.annaNimiSijamuodossa("ablatiivi") + " alas.");
        // ^ Tekis mieli hypätä Sillalta alas.
        switch (sijamuoto) {
            case "nominatiivi":  return "Silta";
            case "genetiivi":    return "Sillan";
            case "esiivi":       return "Siltana";
            case "partitiivi":   return "Siltaa";
            case "translatiivi": return "Sillaksi";
            case "inessiivi":    return "Sillassa";
            case "elatiivi":     return "Sillasta";
            case "illatiivi":    return "Siltaan";
            case "adessiivi":    return "Sillalla";
            case "ablatiivi":    return "Sillalta";
            case "allatiivi":    return "Sillalle";
            default:             return "Silta";
        }
    }
    
    public Silta (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Silta";
        super.tiedostonNimi = "asfaltti_silta.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/silta_dialogi.png");
        super.katsomisTeksti = "Kiva näkymä";
        super.erillisDialogi = true;
        super.ignooraaEsineValintaDialogissa = true;
        super.asetaTiedot();
    }
}
