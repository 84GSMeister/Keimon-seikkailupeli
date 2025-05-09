package keimo.kenttäkohteet.kerättävä;

import keimo.Pelaaja;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.äänet.Äänet;

public final class Seteli extends Kerättävä {

    public Seteli(int sijX, int sijY){
        super(sijX, sijY);
        super.nimi = "Seteli";
        super.tiedostonNimi = "seteli.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.asetaTiedot();
    }

    @Override
    public void kerää() {
        Pelaaja.raha += 20;
        Äänet.toistaSFX("Raha2");
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Seteli";
            case "genetiivi":    return "Setelin";
            case "essiivi":      return "Setelinä";
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
}
