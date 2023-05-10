package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

import keimo.PääIkkuna;

public class Nuotio extends Kiintopiste {
    
    private boolean sytyke = false;
    private boolean polttoaine = false;
    private boolean sytytetty = false;
    
    @Override
    public String kokeileEsinettä(Esine e) {
        
        this.vuorovaikutus = false;
        
        String statusTeksti = "";
        if (tavoiteSuoritettu) {    
            statusTeksti = "Nuotion liekit leimuavat komeasti.";
        }
        if (e instanceof Paperi) {
            this.sytyke = true;
            this.vuorovaikutus = true;
            statusTeksti = "Nuotioon lisättiin sytyke.";
        }
        else if (e instanceof Hiili) {
            this.polttoaine = true;
            this.vuorovaikutus = true;
            statusTeksti = "Nuotioon lisättiin palavaa ainetta.";
        }
        else if (e instanceof Kaasusytytin) {
            if (sytytetty){
                return "Nuotion liekit leimuavat komeasti.";
            }
            else if (polttoaine && sytyke){
                this.tavoiteSuoritettu = true;
                this.sytytetty = true;
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/nuotio.png");
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
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/nuotio_sammunut.png");
                this.vuorovaikutus = true;
                statusTeksti = "Nuotio sammutettiin";
            }
            else {
                statusTeksti = "Kaadoit vettä sammuneeseen nuotioon. Mitään ei tapahtunut.";
            }
            
        }
        else if (e instanceof Makkara) {
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

        else {
            statusTeksti = "Mitään ei tapahtunut.";
        }
        return statusTeksti;
    }

    @Override
    public Esine suoritaMuutoksetEsineelle(Esine e) {
        if (e instanceof Makkara) {
            Makkara makkara = (Makkara)e;
            if (this.sytytetty) {
                makkara.paista();
                return makkara;
            }
            else {
                PääIkkuna.hudTeksti.setText("Makkaraa ei voi paistaa ennen kuin nuotio on sytytetty.");
                return makkara;
            }
        }
        else if (e instanceof Paperi) {
            return null;
        }
        else if (e instanceof Hiili) {
            return null;
        }
        else {
            if (!e.sopiiKäytettäväksi.contains(this.nimi)) {
                PääIkkuna.hudTeksti.setText(e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + this.annaNimiSijamuodossa("illatiivi"));
            }
            return e;
        }
    }

    @Override
    public String katso(){
        if (sytytetty) {    
            return "Nuotion liekit leimuavat komeasti.";
        }
        else {
            if (sytyke && !polttoaine) {
                return "Pelkkä paperi ei pala kovin kauan. Tarvitaan jotain muuta palavaa.";
            }
            else if (polttoaine && !sytyke) {
                return "Nuotiossa on poltettavaa, mutta tarvitaan vielä jotain sytykkeeksi.";
            }
            else if (sytyke && polttoaine) {
                return "Nuotiossa on polttoainetta ja sytykettä. Enää tarvitsee vain työkalun, jolla sen voi sytyttää.";
            }
            else {
                return "Nuotio on tyhjä. Siihen pitäisi varmaankin lisätä jotain palavaa.";
            }            
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Nuotio";
            case "genetiivi":
                return "Nuotion";
            case "esiivi":
                return "Nuotiona";
            case "partitiivi":
                return "Nuotiota";
            case "translatiivi":
                return "Nuotioksi";
            case "inessiivi":
                return "Nuotiossa";
            case "elatiivi":
                return "Nuotiosta";
            case "illatiivi":
                return "Nuotioon";
            case "adessiivi":
                return "Nuotiolla";
            case "ablatiivi":
                return "Nuotiolta";
            case "allatiivi":
                return "Nuotiolle";
            default:
                return "Nuotio";
        }
    }

    public boolean onSytytetty () {
        return sytytetty;
    }

    public Nuotio(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Nuotio";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/nuotio_sammunut.png");
        super.asetaTiedot();
    }
}
