package keimo.seikkailupeli.objektit.kenttäkohteet.warp;

import keimo.keimoengine.grafiikat.Tekstuuri;

import java.util.ArrayList;

public class Salaovi extends Warp {

    private Tekstuuri suljettuTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/salaovi.png");
    private Tekstuuri avattuTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/reunawarppi.png");

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
