package keimo.kenttäkohteet.kerättävä;

import keimo.Pelaaja;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import javax.swing.ImageIcon;

public final class Kolikko extends Kerättävä {
    
    @Override
    public void kerää() {
        Pelaaja.raha += 1;
        ÄänentoistamisSäie.toistaSFX(super.nimi);
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kolikko";
            case "genetiivi":    return "Kolikon";
            case "esiivi":       return "Kolikkona";
            case "partitiivi":   return "Kolikkoa";
            case "translatiivi": return "Kolikoksi";
            case "inessiivi":    return "Kolikossa";
            case "elatiivi":     return "Kolikosta";
            case "illatiivi":    return "Kolikkoon";
            case "adessiivi":    return "Kolikolla";
            case "ablatiivi":    return "Kolikolta";
            case "allatiivi":    return "Kolikolle";
            default:             return "Kolikko";
        }
    }

    public Kolikko(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kolikko";
        super.tiedostonNimi = "kolikko.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.kolmiUlotteinen = true;
        super.obj3dMallinTunniste = "Kolikko";
        super.liikeNopeus = 0f;
        super.pyörimisNopeus = 8f;
        super.asetaTiedot();
    }
}
