package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Nuotio extends Kiintopiste {
    
    private boolean sytyke = false;
    private boolean polttoaine = false;
    private boolean sytytetty = false;
    
    public String kokeileEsinettä(Esine e) {
        
        this.vuorovaikutus = false;
        
        String statusTeksti = "";
        if (tavoiteSuoritettu) {    
            statusTeksti = "Nuotion liekit leimuavat komeasti";
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
            if (polttoaine){
                if (sytyke) {
                    this.tavoiteSuoritettu = true;
                    this.sytytetty = true;
                    this.kuvake = new ImageIcon("tiedostot/kuvat/nuotio.png");
                    statusTeksti = "Nuotio sytytettiin!";
                }
                else {
                    statusTeksti = "Nuotio tarvitsee jonkin sytykkeen.";
                }
            }
            else {
                statusTeksti = "Nuotio tarvitsee palavaa ainetta.";
            }
        }
        else if (e instanceof Vesiämpäri) {
            if (sytytetty) {
                this.sytytetty = false;
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuotio_sammunut.png");
                this.vuorovaikutus = true;
                statusTeksti = "Nuotio sammutettiin";
            }
            else {
                statusTeksti = "Kaadoit vettä sammuneeseen nuotioon. Mitään ei tapahtunut.";
            }
            
        }
        else {
            statusTeksti = "Mitään ei tapahtunut.";
        }
        return statusTeksti;
        }

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

    public boolean onSytytetty () {
        return sytytetty;
    }

    public String annaNimi() {
        return nimi;
    }

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
    
    public Nuotio() {
        super.nimi = "Nuotio";
        super.kuvake = new ImageIcon("tiedostot/kuvat/nuotio_sammunut.png");
        super.asetaTiedot();
    }

    public Nuotio(int sijX, int sijY) {
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.nimi = "Nuotio";
        super.kuvake = new ImageIcon("tiedostot/kuvat/nuotio_sammunut.png");
        super.asetaTiedot();
    }
}
