package keimo.seikkailupeli.objektit.kenttäkohteet.esine;

import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.seikkailupeli.äänet.Äänet;

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
        Äänet.toistaSFX("Juoman_kaato");
        return katso();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Juomalasi";
            case "genetiivi":    return "Juomalasin";
            case "esiivi":       return "Juomalasina";
            case "partitiivi":   return "Juomalasia";
            case "translatiivi": return "Juomalasiksi";
            case "inessiivi":    return "Juomalasissa";
            case "elatiivi":     return "Juomalasista";
            case "illatiivi":    return "Juomalasiin";
            case "adessiivi":    return "Juomalasilla";
            case "ablatiivi":    return "Juomalasilta";
            case "allatiivi":    return "Juomalasille";
            default:             return "Juomalasi";
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
                super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
            }
            case "OLUT" -> {
                super.käyttö = true;
                super.katsomisTeksti = "Laadukasta Keimo-baarin hanaolutta";
                super.tiedostonNimi = "juomalasi_olut.png";
                super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
                super.hinta = 4.95;
                super.voltit = 0.6f;
                super.känniKuolemattomuus = 600;
            }
            case "LONKERO" -> {
                super.käyttö = true;
                super.katsomisTeksti = "Laadukasta Keimo-baarin lonkeroa";
                super.tiedostonNimi = "juomalasi_lonkero.png";
                super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
                super.hinta = 6.95;
                super.voltit = 0.6f;
                super.känniKuolemattomuus = 600;
            }
            case "SIIDERI" -> {
                super.käyttö = true;
                super.katsomisTeksti = "Laadukasta Keimo-baarin siideriä";
                super.tiedostonNimi = "juomalasi_siideri.png";
                super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
                super.hinta = 5.95;
                super.voltit = 0.6f;
                super.känniKuolemattomuus = 600;
            }
            case "KUUOLUT" -> {
                super.käyttö = true;
                super.katsomisTeksti = "Uskaltaakohan tätä juoda?";
                super.tiedostonNimi = "juomalasi_kuuolut.png";
                super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
                super.hinta = 0;
                super.voltit = 0.6f;
                super.känniKuolemattomuus = 600;
            }
        }
    }
}
