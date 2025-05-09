package keimo.kenttäkohteet.esine;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public final class Paperi extends Esine {

    public Paperi(int sijX, int sijY){
        super(sijX, sijY);
        super.nimi = "Paperi";
        super.tiedostonNimi = "paperi.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Tämä sopisi hyvin nuotion sytykkeeksi.";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Nuotio");
        super.liikeNopeus = 6f;
        super.pyörimisNopeus = 2f;
        super.asetaTiedot();
    }

    @Override
    public String käytä() {
        super.poista = true;
        return super.käytä();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Paperi";
            case "genetiivi":    return "Paperin";
            case "esiivi":       return "Paperina";
            case "partitiivi":   return "Paperia";
            case "translatiivi": return "Paperiksi";
            case "inessiivi":    return "Paperissa";
            case "elatiivi":     return "Paperista";
            case "illatiivi":    return "Paperiin";
            case "adessiivi":    return "Paperilla";
            case "ablatiivi":    return "Paperilta";
            case "allatiivi":    return "Paperille";
            default:             return "Paperi";
        }
    }
}
