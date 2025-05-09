package keimo.kenttäkohteet.kiintopiste;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.util.ArrayList;

public final class BaariRuutu extends Kiintopiste {

    public static boolean kuubaariLöydetty = false;
    public String tyyppi = "";

    public BaariRuutu (int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Baariruutu";
        super.tiedostonNimi = "baariruutu.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.dialogiTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
        super.katsomisTeksti = "Tästä tilataan.";
        if (ominaisuusLista != null) {
            this.lisäOminaisuudet = new ArrayList<>();
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("tyyppi=")) {
                    this.tyyppi = ominaisuus.substring(7);
                }
            }
        }
        päivitäLisäOminaisuudet();
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Baariruutu";
            case "genetiivi":    return "Baariruudun";
            case "essiivi":      return "Baariruutuna";
            case "partitiivi":   return "Baariruutua";
            case "translatiivi": return "Baariruuduksi";
            case "inessiivi":    return "Baariruudussa";
            case "elatiivi":     return "Baariruudusta";
            case "illatiivi":    return "Baariruutuun";
            case "adessiivi":    return "Baariruudulla";
            case "ablatiivi":    return "Baariruudulta";
            case "allatiivi":    return "Baariruudulle";
            default:             return "Baariruutu";
        }
    }

    public void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet != null) {
            this.lisäOminaisuuksia = true;
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("tyyppi="));
            this.lisäOminaisuudet.add("tyyppi=" + tyyppi);
            super.asetaTiedot();
        }
    }
}
