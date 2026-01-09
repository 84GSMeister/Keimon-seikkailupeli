package keimo.seikkailupeli.objektit.kenttäkohteet.kerättävä;

import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.äänet.Äänet;

public final class Kolikko extends Kerättävä {

    public Kolikko(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Kolikko";
        super.tiedostonNimi = "kolikko.png";
        super.tekstuuri = Assets.annaTekstuuri("kolikko");
        super.kolmiUlotteinen = true;
        super.obj3dMallinTunniste = "Kolikko";
        super.liikeNopeus = 0f;
        super.pyörimisNopeus = 8f;
        super.asetaTiedot();
    }
    
    @Override
    public void kerää() {
        Pelaaja.raha += 1;
        Äänet.toistaSFX(super.nimi, true);
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kolikko";
            case "genetiivi":    return "Kolikon";
            case "essiivi":      return "Kolikkona";
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
}
