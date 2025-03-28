package keimo.kenttäkohteet.kiintopiste;

import keimo.PääIkkuna;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;
import keimo.kenttäkohteet.esine.Avain;

import javax.swing.ImageIcon;

public class Kirstu extends Säiliö {

    boolean avattu;

    @Override
    public String kokeileEsinettä(Esine e) {
        if (super.tavoiteSuoritettu) {
            return "Kirstu on jo avattu.";
        }
        else {
            if (e == null) {
                return katso();
            }
            else if (e instanceof Avain) {
                if (this.sisältö != null) {
                    return "Kirstu avattiin. Sait uuden esineen: " + this.sisältö.annaNimi();
                }
                else {
                    return "Kirstu avattiin, mutta se on tyhjä.";
                }
            }
            else {
                return "Mitään ei tapahtunut.";
            }
        }
    }

    @Override
    public Esine suoritaMuutoksetEsineelle(Esine e) {
        if (super.tavoiteSuoritettu) {
            return e;
        }
        else {
            if (e instanceof Avain) {
                avaa();
                return sisältö;
            }
            else if (e != null) {
                if (!e.sopiiKäytettäväksi.contains(this.nimi)) {
                    PääIkkuna.hudTeksti.setText(e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + this.annaNimiSijamuodossa("illatiivi"));
                }
                return e;
            }
            else {
                return null;
            }
        }
    }

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
        super.vuorovaikutus = true;
        super.tavoiteSuoritettu = true;
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kirstu_avattu.png");
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
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
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
