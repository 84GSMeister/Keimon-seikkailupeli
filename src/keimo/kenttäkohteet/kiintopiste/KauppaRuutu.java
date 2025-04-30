package keimo.kenttäkohteet.kiintopiste;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public class KauppaRuutu extends Kiintopiste {

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kaupparuutu";
            case "genetiivi":    return "Kaupparuudun";
            case "esiivi":       return "Kaupparuutuna";
            case "partitiivi":   return "Kaupparuutua";
            case "translatiivi": return "Kaupparuuduksi";
            case "inessiivi":    return "Kaupparuudussa";
            case "elatiivi":     return "Kaupparuudusta";
            case "illatiivi":    return "Kaupparuutuun";
            case "adessiivi":    return "Kaupparuudulla";
            case "ablatiivi":    return "Kaupparuudulta";
            case "allatiivi":    return "Kaupparuudulle";
            default:             return "Kaupparuutu";
        }
    }
    
    public KauppaRuutu (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Kaupparuutu";
        super.tiedostonNimi = "kaupparuutu.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.dialogiTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
        super.katsomisTeksti = "Kylien kauppias";
        super.asetaTiedot();
    }
}
