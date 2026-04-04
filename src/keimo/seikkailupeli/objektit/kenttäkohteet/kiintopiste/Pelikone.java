package keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.seikkailupeli.assets.Assets;

import java.util.ArrayList;

public final class Pelikone extends Kiintopiste {

    private int tyyppi = 0;

    public Pelikone(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Pelikone";
        super.tiedostonNimi = "pelikone.png";
        super.tekstuuri = Assets.annaTekstuuri("pelikone");
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
            super.tekstuuri = valitseTekstuuri();
            päivitäLisäOminaisuudet(ominaisuusLista);
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
    public void päivitäLisäOminaisuudet(ArrayList<String> ominaisuusLista) {
        super.päivitäLisäOminaisuudet(ominaisuusLista);
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("tyyppi="));
        this.lisäOminaisuudet.add("tyyppi=" + this.annaTyyppi());
    }

    public enum PeliTyyppi {
        SIM3D,
        PONG,
        POKERI,
        TETRIS,
        OVERFLOW,
        KEIMOÄLY;
    }

    public int annaTyyppi() {
        return tyyppi;
    }

    private Renderöitävä valitseTekstuuri() {
        switch (tyyppi) {
            default: return Assets.annaTekstuuri("pelikone");
            case 5: return Assets.annaTekstuuri("pelikone_keimoäly");
        }
    }
}
