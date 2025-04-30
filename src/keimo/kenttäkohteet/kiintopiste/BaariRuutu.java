package keimo.kenttäkohteet.kiintopiste;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public class BaariRuutu extends Kiintopiste {

    public static boolean kuubaariLöydetty = false;

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Baariruutu";
            case "genetiivi":    return "Baariruudun";
            case "esiivi":       return "Baariruutuna";
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
    
    public BaariRuutu (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Baariruutu";
        super.tiedostonNimi = "baariruutu.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.dialogiTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
        super.katsomisTeksti = "Tästä tilataan.";
        super.asetaTiedot();
    }
}
