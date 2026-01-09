package keimo.seikkailupeli.objektit.kenttäkohteet.esine;

import keimo.seikkailupeli.assets.Assets;

public final class Paskanmarjat extends Esine {

    public Paskanmarjat(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Paskanmarjat";
        super.tiedostonNimi = "paskanmarjat.png";
        super.tekstuuri = Assets.annaTekstuuri("paskanmarjat");
        super.katsomisTeksti = "Näyttää harvinaisen epämiellyttäviltä. Ehkä näistä kuitenkin saa hyvän boolin.";
        super.yhdistettävä = true;
        super.kelvollisetYhdistettävät.add("Jallupullo");
        super.liikeNopeus = 6f;
        super.pyörimisNopeus = 2f;
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Paskanmarjat";
            case "genetiivi":    return "Paskanmarjojen";
            case "esiivi":       return "Paskanmarjoina";
            case "partitiivi":   return "Paskanmarjoja";
            case "translatiivi": return "Paskanmarjoiksi";
            case "inessiivi":    return "Paskanmarjoissa";
            case "elatiivi":     return "Paskanmarjoista";
            case "illatiivi":    return "Paskanmarjoihin";
            case "adessiivi":    return "Paskanmarjoilla";
            case "ablatiivi":    return "Paskanmarjoilta";
            case "allatiivi":    return "Paskanmarjoille";
            default:             return "Paskanmarjat";
        }
    }
}
