package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

import keimo.PääIkkuna;

public class Kirstu extends Kiintopiste {
    
    protected Esine sisältö;

    public String kokeileEsinettä(Esine e) {
        if (tavoiteSuoritettu) {
            return "Kirstu on jo avattu.";
        }
        else {
            if (e instanceof Avain) {
                if (sisältö != null) {
                    return "Kirstu avattiin. Sait uuden esineen: " + sisältö.annaNimi();
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

    public Esine suoritaMuutoksetEsineelle(Esine e) {
        if (tavoiteSuoritettu) {
            return e;
        }
        else {
            if (e instanceof Avain) {
                avaa();
                return sisältö;
            }
            else {
                if (!e.sopiiKäytettäväksi.contains(this.nimi)) {
                    PääIkkuna.hudTeksti.setText(e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + this.annaNimiSijamuodossa("illatiivi"));
                }
                return e;
            }
        }
    }

    public void avaa() {
        this.vuorovaikutus = true;
        this.tavoiteSuoritettu = true;
        this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kirstu_avattu.png");
    }

    public String katso(){
        if (tavoiteSuoritettu) {    
            return "Avattu kirstu";
        }
        else {
            return "Kirstu on lukittu. Siihen tarvitsee varmaankin avaimen. Löytyisiköhän kentältä sellainen?";
        }
    }

    public String annaNimi() {
        return nimi;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Kirstu";
            case "genetiivi":
                return "Kirstun";
            case "esiivi":
                return "Kirstuna";
            case "partitiivi":
                return "Kirstua";
            case "translatiivi":
                return "Kirstuksi";
            case "inessiivi":
                return "Kirstussa";
            case "elatiivi":
                return "Kirstusta";
            case "illatiivi":
                return "Kirstuun";
            case "adessiivi":
                return "Kirstulla";
            case "ablatiivi":
                return "Kirstulta";
            case "allatiivi":
                return "Kirstulle";
            default:
                return "Kirstu";
        }
    }

    protected Esine luoSisältö(String esineenNimi) {
        switch (esineenNimi) {
            case "Avain":
                return new Avain(false, 0, 0);
            case "Hiili":
                return new Hiili(false, 0, 0);
            case "Huume":
                return new Huume(false, 0, 0);
            case "Kaasupullo":
                return new Kaasupullo(false, 0, 0);
            case "Kaasusytytin":
                return new Kaasusytytin(false, 0, 0, "tyhjä");
            case "Kilpi":
                return new Kilpi(false, 0, 0);
            case "Makkara":
                return new Makkara(false, 0, 0);
            case "Paperi":
                return new Paperi(false, 0, 0);
            case "Pesäpallomaila":
                return new Pesäpallomaila(false, 0, 0);
            case "Seteli":
                return new Seteli(false, 0, 0);
            case "Suklaalevy":
                return new Suklaalevy(false, 0, 0);
            case "Vesiämpäri":
                return new Vesiämpäri(false, 0, 0);
            default:
                return null;
        }
    }

    public void asetaSisältö(String esineenNimi) {
        sisältö = luoSisältö(esineenNimi);
    }

    public String annaSisältö() {
        if (sisältö != null) {
            return sisältö.annaNimi();
        }
        else {
            return "tyhjä";
        }
    }

    public Kirstu(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kirstu";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kirstu.png");

        if (ominaisuusLista != null) {
            String esineenNimi = "";
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("sisältö=")) {
                    esineenNimi = ominaisuus.substring(8);
                }
            }
            this.sisältö = luoSisältö(esineenNimi);
            this.lisäOminaisuuksia = true;
            this.lisäOminaisuudet = new String[1];
            this.lisäOminaisuudet[0] = "sisältö=" + esineenNimi;
        }
        else {
            this.lisäOminaisuuksia = false;
        }
        
        super.asetaTiedot();
    }
}
