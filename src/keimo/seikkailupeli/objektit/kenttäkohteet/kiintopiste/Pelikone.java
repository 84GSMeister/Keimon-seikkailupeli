package keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste;

import keimo.keimoengine.grafiikat.Tekstuuri;

import java.util.ArrayList;

public final class Pelikone extends Kiintopiste {

    private int tyyppi = 0;

    public Pelikone(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Pelikone";
        super.tiedostonNimi = "pelikone.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Mitenkäs tätä pelataan?";

        if (ominaisuusLista != null) {
            this.lisäOminaisuudet = new ArrayList<>();
            String tyyppiString = "";
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("tyyppi=")) {
                    try {
                        tyyppiString = ominaisuus.substring(7);
                        this.tyyppi = Integer.parseInt(tyyppiString);
                    }
                    catch (NumberFormatException nfe) {
                        this.tyyppi = 0;
                    }
                }
            }
            päivitäLisäOminaisuudet();
        }
        else {
            this.lisäOminaisuuksia = false;
        }

        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Tässä todistamme jälleen kauniin kielemme kukkasia.
        switch (sijamuoto) {
            case "nominatiivi":  return "Pelikone";
            case "genetiivi":    return "Pelikoneen";
            case "essiivi":      return "Pelikoneena";
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

    @Override
    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("tyyppi="));
        this.lisäOminaisuudet.add("tyyppi=" + this.annaTyyppi());
        super.päivitäLisäOminaisuudet();
    }

    public int annaTyyppi() {
        return tyyppi;
    }
}
