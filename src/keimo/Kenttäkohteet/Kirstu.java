package keimo.Kenttäkohteet;

import keimo.PääIkkuna;
import keimo.TavoiteLista;
import keimo.Utility.KäännettäväKuvake;

import javax.swing.ImageIcon;

public class Kirstu extends Kiintopiste {
    
    protected Esine sisältö;

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
            case "nominatiivi": return "Kirstu";
            case "genetiivi": return "Kirstun";
            case "esiivi": return "Kirstuna";
            case "partitiivi": return "Kirstua";
            case "translatiivi": return "Kirstuksi";
            case "inessiivi": return "Kirstussa";
            case "elatiivi": return "Kirstusta";
            case "illatiivi": return "Kirstuun";
            case "adessiivi": return "Kirstulla";
            case "ablatiivi": return "Kirstulta";
            case "allatiivi": return "Kirstulle";
            default: return "Kirstu";
        }
    }

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
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
        TavoiteLista.tarkistaTavoiteEsine(this.luoSisältö(this.annaSisältö(), null));
    }

    protected Esine luoSisältö(String esineenNimi, String[] ominaisuusLista) {
        return Esine.luoEsine(esineenNimi, ominaisuusLista);
    }

    public void asetaSisältö(String esineenNimi, String[] ominaisuusLista) {
        this.sisältö = luoSisältö(esineenNimi, null);
    }

    public String annaSisältö() {
        if (this.sisältö != null) {
            return sisältö.annaNimi();
        }
        else {
            return "tyhjä";
        }
    }

    public Kirstu(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Kirstu";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kirstu.png");
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
        super.tiedostonNimi = "kirstu.png";
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
