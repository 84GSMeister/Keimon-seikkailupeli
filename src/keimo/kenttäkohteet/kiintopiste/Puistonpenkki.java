package keimo.kenttäkohteet.kiintopiste;

import keimo.Pelaaja;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;

import java.util.ArrayList;

public final class Puistonpenkki extends Lepopaikka {

    public Puistonpenkki (int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Penkki";
        super.tiedostonNimi = "puistonpenkki.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Nukuttaako?";
        super.asetaTiedot();
    }

    @Override
    public String vuorovaikuta(Esine e) {
        super.hpVähennys = Math.round(Pelaaja.känninVoimakkuusFloat*1.6);
        return super.vuorovaikuta(e);
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Penkki p = new Penkki(false, 0, 0);
        //System.out.println("Nouseeko " + p.annaNimiSijamuodossa("elatiivi") + " 100? Ei nouse?!?")
        // ^ Nouseeko Penkistä 100? Ei nouse?!?
        switch (sijamuoto) {
            case "nominatiivi":  return "Penkki";
            case "genetiivi":    return "Penkin";
            case "essiivi":      return "Penkkinä";
            case "partitiivi":   return "Penkkiä";
            case "translatiivi": return "Penkiksi";
            case "inessiivi":    return "Penkissä";
            case "elatiivi":     return "Penkistä";
            case "illatiivi":    return "Penkkiin";
            case "adessiivi":    return "Penkillä";
            case "ablatiivi":    return "Penkiltä";
            case "allatiivi":    return "Penkille";
            default:             return "Penkki";
        }
    }
}
