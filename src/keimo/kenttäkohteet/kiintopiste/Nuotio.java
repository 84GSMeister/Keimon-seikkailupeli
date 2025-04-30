package keimo.kenttäkohteet.kiintopiste;

import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.*;

public final class Nuotio extends Kiintopiste {
    
    private boolean sytyke = false;
    private boolean polttoaine = false;
    private boolean sytytetty = false;

    private Tekstuuri sammunutTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/nuotio_sammunut.png");
    private Tekstuuri sytytettyTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/nuotio.png");
    
    public String kokeileEsinettä(Esine e) {
        
        if (e instanceof Makkara) {
            Makkara makkara = (Makkara)e;
            if (this.sytytetty) {
                if (makkara.käristetty) {
                    return "Makkara on jo käristetty hiileksi!";
                }
                else if (makkara.paistettu) {
                    return "Paistoit liikaa! Nyt makkara on pikimusta.";
                }
                else {
                    return "Mmm.. onpas hyvän näköistä kyrsää.";
                }
            }
            else {
                return "Makkaraa ei voi paistaa ennen kuin nuotio on sytytetty.";
            }
        }
        else if (e instanceof Paperi) {
            this.sytyke = true;
            return "Nuotioon lisättiin sytyke.";
        }
        else if (e instanceof Hiili) {
            this.polttoaine = true;
            return "Nuotioon lisättiin palavaa ainetta.";
        }
        else if (tavoiteSuoritettu) {
            return "Nuotion liekit leimuavat komeasti.";
        }
        else if (e == null) {
            return katso();
        }
        else if (e instanceof Kaasusytytin) {
            if (sytytetty){
                return "Nuotion liekit leimuavat komeasti.";
            }
            else if (polttoaine && sytyke){
                super.tavoiteSuoritettu = true;
                this.sytytetty = true;
                super.tekstuuri = sytytettyTekstuuri;
                return "Nuotio sytytettiin!";
            }
            else if (polttoaine && !sytyke) {
                return "Nuotiossa on palavaa ainetta, mutta sinne tarvitaan jokin sytyke.";
            }
            else if (!polttoaine && sytyke) {
                return "Pelkkä sytyke ei pala kovin kauan. Tarvitaan jotain muuta palavaa.";
            }
            else {
                return "Nuotio tarvitsee palavaa ainetta.";
            }
        }
        else if (e instanceof Vesiämpäri) {
            if (sytytetty) {
                this.sytytetty = false;
                super.tekstuuri = sammunutTekstuuri;
                return "Nuotio sammutettiin";
            }
            else {
                return "Kaadoit vettä sammuneeseen nuotioon. Mitään ei tapahtunut.";
            }
            
        }

        else {
            return "Mitään ei tapahtunut.";
        }
    }

    @Override
    public String katso(){
        if (this.sytytetty) {    
            return "Nuotion liekit leimuavat komeasti.";
        }
        else {
            if (this.sytyke && !this.polttoaine) {
                return "Pelkkä paperi ei pala kovin kauan. Tarvitaan jotain muuta palavaa.";
            }
            else if (this.polttoaine && !this.sytyke) {
                return "Nuotiossa on poltettavaa, mutta tarvitaan vielä jotain sytykkeeksi.";
            }
            else if (this.sytyke && this.polttoaine) {
                return "Nuotiossa on polttoainetta ja sytykettä. Enää tarvitsee vain työkalun, jolla sen voi sytyttää.";
            }
            else {
                return "Nuotio on tyhjä. Löytyisiköhän lähistöltä siihen jotain palavaa?";
            }            
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Nuotio";
            case "genetiivi":    return "Nuotion";
            case "esiivi":       return "Nuotiona";
            case "partitiivi":   return "Nuotiota";
            case "translatiivi": return "Nuotioksi";
            case "inessiivi":    return "Nuotiossa";
            case "elatiivi":     return "Nuotiosta";
            case "illatiivi":    return "Nuotioon";
            case "adessiivi":    return "Nuotiolla";
            case "ablatiivi":    return "Nuotiolta";
            case "allatiivi":    return "Nuotiolle";
            default:             return "Nuotio";
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "lisää sytyke": return "Nuotioon lisättiin sytyke.";
            case "lisää polttoaine": return "Nuotioon lisättiin palavaa ainetta.";
            case "sytytä": return "Nuotio sytytettiin!";
            case "sammuta": return "Nuotio sammutettiin.";
            case "ei_toimiva": return "Kaasusytyttimessä ei ole kaasua. Löytyisiköhän jostain pullo sitä?";
            case "makkara_ei_sytytetty": return "Makkaraa ei voi paistaa ennen kuin nuotio on sytytetty.";
            case null, default: return katso();
        }
    }

    public boolean onSytytetty () {
        return sytytetty;
    }

    public boolean onkoPolttoaine() {
        return polttoaine;
    }

    public boolean onkoSytyke() {
        return sytyke;
    }

    public void sytytä(boolean sytytys) {
        this.sytytetty = sytytys;
        if (sytytys) {
            super.tekstuuri = sytytettyTekstuuri;
        }
        else {
            super.tekstuuri = sammunutTekstuuri;
        }
    }

    public Nuotio(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Nuotio";
        super.tiedostonNimi = "nuotio_sammunut.png";
        super.tekstuuri = sammunutTekstuuri;
        super.katsomisTeksti = "Nuotio on tyhjä. Löytyisiköhän lähistöltä siihen jotain palavaa?";
        super.asetaTiedot();
    }
}
