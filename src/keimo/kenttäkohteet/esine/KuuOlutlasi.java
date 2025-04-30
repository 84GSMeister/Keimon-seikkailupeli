package keimo.kenttäkohteet.esine;

import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.grafiikat.Tekstuuri;

public class KuuOlutlasi extends Juoma {

    @Override
    public String käytä(){
        super.käytä();
        ÄänentoistamisSäie.toistaSFX("Juoman_kaato");
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

    public KuuOlutlasi(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "KuuOlutlasi";
        super.tiedostonNimi = "olutlasi_kuu.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Uskaltaakohan tätä juoda?";
        super.käyttö = true;
        super.hinta = 4.95;
        super.voltit = 0.6f;
        super.känniKuolemattomuus = 600;
        super.asetaTiedot();
    }
}
