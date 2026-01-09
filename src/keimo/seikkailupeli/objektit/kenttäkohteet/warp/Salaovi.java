package keimo.seikkailupeli.objektit.kenttäkohteet.warp;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.seikkailupeli.assets.Assets;

import java.util.ArrayList;

public class Salaovi extends Warp {

    private Renderöitävä suljettuTekstuuri = Assets.annaTekstuuri("salaovi");
    private Renderöitävä avattuTekstuuri = Assets.annaTekstuuri("oviruutu");

    public Salaovi(int x, int y, ArrayList<String> ominaisuusLista) {
        super(x, y, ominaisuusLista);
        super.nimi = "Salaovi";
        super.tiedostonNimi = "salaovi.png";
        super.tekstuuri = suljettuTekstuuri;
        super.asetaTiedot();
    }
    
     @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Oviruutu";
            case "genetiivi":    return "Oviruudun";
            case "essiivi":      return "Oviruutuna";
            case "partitiivi":   return "Oviruutua";
            case "translatiivi": return "Oviruuduksi";
            case "inessiivi":    return "Oviruudussa";
            case "elatiivi":     return "Oviruudusta";
            case "illatiivi":    return "Oviruutuun";
            case "adessiivi":    return "Oviruudulla";
            case "ablatiivi":    return "Oviruudulta";
            case "allatiivi":    return "Oviruudulle";
            default:             return "Oviruutu";
        }
    }

    public void asetaNäkyväksi() {
        this.tekstuuri = avattuTekstuuri;
    }
}
