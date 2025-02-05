package keimo.kenttäkohteet.kiintopiste;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import javax.swing.ImageIcon;

public class Pelikone extends Kiintopiste {
    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Tässä todistamme jälleen kauniin kielemme kukkasia.
        switch (sijamuoto) {
            case "nominatiivi":  return "Pelikone";
            case "genetiivi":    return "Pelikoneen";
            case "esiivi":       return "Pelikoneena";
            case "partitiivi":   return "Pelikonetta";
            case "translatiivi": return "Pelikoneeksi";
            case "inessiivi":    return "Pelikoneessa";
            case "elatiivi":     return "Pelikoneesta";
            case "illatiivi":    return "Pelikoneeseen";
            case "adessiivi":    return "Pelikoneella";
            case "ablatiivi":    return "Pelikoneelta";
            case "allatiivi":    return "Pelikoneelle";
            default:             return "Pelikone";
        }
    }

    public Pelikone(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Pelikone";
        super.tiedostonNimi = "pelikone.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Mitenkäs tätä pelataan?";
        super.erillisDialogi = true;
        super.ignooraaEsineValintaDialogissa = true;
        super.ohitaDialogiTesktiboksi = true;
        super.asetaTiedot();
    }
}
