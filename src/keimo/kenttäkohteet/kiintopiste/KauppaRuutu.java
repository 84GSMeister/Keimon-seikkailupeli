package keimo.kenttäkohteet.kiintopiste;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.util.ArrayList;

public final class KauppaRuutu extends Kiintopiste {

    public KauppaRuutu (int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Kaupparuutu";
        super.tiedostonNimi = "kaupparuutu.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.dialogiTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
        super.katsomisTeksti = "Kylien kauppias";
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kaupparuutu";
            case "genetiivi":    return "Kaupparuudun";
            case "essiivi":      return "Kaupparuutuna";
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
    
    
}
