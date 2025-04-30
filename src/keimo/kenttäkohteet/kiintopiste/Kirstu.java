package keimo.kenttäkohteet.kiintopiste;

import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.keimoEngine.grafiikat.Tekstuuri;

public class Kirstu extends Säiliö {

    boolean avattu;

    @Override
    public String katso(){
        if (super.tavoiteSuoritettu) {    
            return "Avattu kirstu";
        }
        else {
            return "Kirstu on lukittu. Minneköhän sen avain on unohtunut?";
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kirstu";
            case "genetiivi":    return "Kirstun";
            case "esiivi":       return "Kirstuna";
            case "partitiivi":   return "Kirstua";
            case "translatiivi": return "Kirstuksi";
            case "inessiivi":    return "Kirstussa";
            case "elatiivi":     return "Kirstusta";
            case "illatiivi":    return "Kirstuun";
            case "adessiivi":    return "Kirstulla";
            case "ablatiivi":    return "Kirstulta";
            case "allatiivi":    return "Kirstulle";
            default:             return "Kirstu";
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "avaa": return "Kirstu avattiin. Sait " + sisältö.annaNimiSijamuodossa("genetiivi");
            case null, default: return katso();
        }
    }
    
    @Override
    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "sisältö=" + this.annaSisältö();
        super.päivitäLisäOminaisuudet();
    }

    public void avaa() {
        super.tavoiteSuoritettu = true;
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kirstu_avattu.png");
        this.avattu = true;
        TavoiteLista.tarkistaTavoiteEsine(this.luoSisältö(this.annaSisältö(), null));
    }

    public boolean onkoAvattu() {
        return avattu;
    }

    public Kirstu(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Kirstu";
        super.tiedostonNimi = "kirstu.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Kirstu on lukittu. Minneköhän sen avain on unohtunut?";

        if (ominaisuusLista != null) {
            String esineenNimi = "";
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("sisältö=")) {
                    esineenNimi = ominaisuus.substring(8);
                }
            }
            this.sisältö = luoSisältö(esineenNimi, ominaisuusLista);
            päivitäLisäOminaisuudet();
        }
        else {
            this.lisäOminaisuuksia = false;
        }
        
        super.asetaTiedot();
    }
}
