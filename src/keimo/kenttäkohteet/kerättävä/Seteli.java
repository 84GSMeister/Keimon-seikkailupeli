package keimo.kenttäkohteet.kerättävä;

import keimo.Pelaaja;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.grafiikat.Tekstuuri;

public final class Seteli extends Kerättävä {

    @Override
    public void kerää() {
        Pelaaja.raha += 20;
        ÄänentoistamisSäie.toistaSFX("Raha2");
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Seteli";
            case "genetiivi":    return "Setelin";
            case "esiivi":       return "Setelinä";
            case "partitiivi":   return "Seteliä";
            case "translatiivi": return "Seteliksi";
            case "inessiivi":    return "Setelissä";
            case "elatiivi":     return "Setelistä";
            case "illatiivi":    return "Seteliin";
            case "adessiivi":    return "Setelillä";
            case "ablatiivi":    return "Seteliltä";
            case "allatiivi":    return "Setelille";
            default:             return "Seteli";
        }
    }

    public Seteli(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Seteli";
        super.tiedostonNimi = "seteli.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.asetaTiedot();
    }
}
