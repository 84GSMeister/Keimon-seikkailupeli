package keimo.seikkailupeli.objektit.kenttäkohteet.warp;

import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.seikkailupeli.äänet.Äänet;

import java.util.ArrayList;

public final class PuuOvi extends Warp {

    private Tekstuuri suljettuTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/puuovi.png");
    private Tekstuuri avattuTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/puuovi_avattu.png");

    public PuuOvi(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Puuovi";
        super.tiedostonNimi = "puuovi.png";
        super.tekstuuri = suljettuTekstuuri;
        super.asetaTiedot();
    }
    
    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Puuovi";
            case "genetiivi":    return "Puuoven";
            case "essiivi":      return "Puuovena";
            case "partitiivi":   return "Puuovea";
            case "translatiivi": return "Puuoveksi";
            case "inessiivi":    return "Puuovessa";
            case "elatiivi":     return "Puuovesta";
            case "illatiivi":    return "Puuoveen";
            case "adessiivi":    return "Puuovella";
            case "ablatiivi":    return "Puuovelta";
            case "allatiivi":    return "Puuovelle";
            default:             return "Puuovi";
        }
    }

    @Override
    public void ennenWarppia() {
        Äänet.toistaSFX("oven_avaus", true);
        this.tekstuuri = avattuTekstuuri;
    }

    @Override
    public void warpinJälkeen() {
        Äänet.toistaSFX("oven_sulkeminen", true);
        this.tekstuuri = suljettuTekstuuri;
    }
}
