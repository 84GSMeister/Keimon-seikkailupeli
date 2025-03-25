package keimo.kenttäkohteet.kiintopiste;

import keimo.Pelaaja;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;

import javax.swing.ImageIcon;

public final class Puistonpenkki extends Lepopaikka {

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
            case "esiivi":       return "Penkkinä";
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
    
    public Puistonpenkki (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Penkki";
        super.tiedostonNimi = "puistonpenkki.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Nukuttaako?";
        super.ignooraaEsineValintaDialogissa = true;
        super.asetaTiedot();
    }
}
