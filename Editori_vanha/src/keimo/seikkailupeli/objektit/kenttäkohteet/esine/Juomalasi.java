package keimo.seikkailupeli.objektit.kenttäkohteet.esine;

import java.util.ArrayList;

public class Juomalasi extends Juoma {

    private String juoma;

    public Juomalasi(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY);
        super.nimi = "Juomalasi";
        String juomaTyyppi = "";
        if (ominaisuusLista != null) {
            this.lisäOminaisuuksia = true;
            this.lisäOminaisuudet = new ArrayList<>();
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("juoma=")) {
                    juomaTyyppi = ominaisuus.substring(6);
                }
            }
            asetaJuoma(juomaTyyppi);
        }
        else asetaJuoma("TYHJÄ");
        super.asetaTiedot();
    }

    @Override
    public String käytä(){
        super.käytä();
        asetaJuoma("TYHJÄ");
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

    public String annaJuoma() {
        return juoma;
    }

    public void asetaJuoma(String juomaTyyppi) {
        this.juoma = juomaTyyppi;
        switch (juomaTyyppi) {
            case "TYHJÄ" -> {
                super.käyttö = false;
                super.katsomisTeksti = "Lasi on tyhjä. Pitäisiköhän tilata lisää?";
                super.tiedostonNimi = "juomalasi.png";
            }
            case "OLUT" -> {
                super.käyttö = true;
                super.katsomisTeksti = "Laadukasta Keimo-baarin hanaolutta";
                super.tiedostonNimi = "juomalasi_olut.png";
                super.hinta = 4.95;
                super.voltit = 0.6f;
                super.känniKuolemattomuus = 600;
            }
            case "LONKERO" -> {
                super.käyttö = true;
                super.katsomisTeksti = "Laadukasta Keimo-baarin lonkeroa";
                super.tiedostonNimi = "juomalasi_lonkero.png";
                super.hinta = 6.95;
                super.voltit = 0.6f;
                super.känniKuolemattomuus = 600;
            }
            case "SIIDERI" -> {
                super.käyttö = true;
                super.katsomisTeksti = "Laadukasta Keimo-baarin siideriä";
                super.tiedostonNimi = "juomalasi_siideri.png";
                super.hinta = 5.95;
                super.voltit = 0.6f;
                super.känniKuolemattomuus = 600;
            }
            case "KUUOLUT" -> {
                super.käyttö = true;
                super.katsomisTeksti = "Uskaltaakohan tätä juoda?";
                super.tiedostonNimi = "juomalasi_kuuolut.png";
                super.hinta = 0;
                super.voltit = 0.6f;
                super.känniKuolemattomuus = 600;
            }
        }
    }
}
