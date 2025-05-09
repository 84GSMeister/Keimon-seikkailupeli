package keimo.kenttäkohteet.esine;

import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.äänet.Äänet;

public class Olutlasi extends Juoma {

    public Olutlasi(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Olutlasi";
        super.tiedostonNimi = "olutlasi.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Laadukasta Keimo-baarin hanaolutta";
        super.käyttö = true;
        super.hinta = 4.95;
        super.voltit = 0.6f;
        super.känniKuolemattomuus = 600;
        super.asetaTiedot();
    }

    @Override
    public String käytä(){
        super.käytä();
        Äänet.toistaSFX("Juoman_kaato");
        return katso();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Olutlasi";
            case "genetiivi":    return "Olutlasin";
            case "esiivi":       return "Olutlasina";
            case "partitiivi":   return "Olutlasia";
            case "translatiivi": return "Olutlasiksi";
            case "inessiivi":    return "Olutlasissa";
            case "elatiivi":     return "Olutlasista";
            case "illatiivi":    return "Olutlasiin";
            case "adessiivi":    return "Olutlasilla";
            case "ablatiivi":    return "Olutlasilta";
            case "allatiivi":    return "Olutlasille";
            default:             return "Olutlasi";
        }
    }
}
