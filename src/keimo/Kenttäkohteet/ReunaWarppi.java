package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class ReunaWarppi extends Warp {
    
    public ReunaWarppi(int sijX, int sijY, int luontiKohdeHuone, int luontiKohdeRuutuX, int luontiKohdeRuutuY, Suunta luontiSuunta) {
        
        this.nimi = "Oviruutu";
        this.katsomisTeksti = "Paina Spacea tai nuolen suuntaista nuolinäppäintä kulkeaksesi oviruudusta!";
        this.kohdeHuone = luontiKohdeHuone;
        this.kohdeRuutuX = luontiKohdeRuutuX;
        this.kohdeRuutuY = luontiKohdeRuutuY;
        this.suunta = luontiSuunta;
        this.sijX = sijX;
        this.sijY = sijY;
        this.määritettySijainti = true;

        switch (suunta) {
            case YLÖS:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_ylös.png"), KenttäKohde.Suunta.YLÖS);
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_ylös.png"); 
                break;
            case ALAS:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_alas.png"), KenttäKohde.Suunta.ALAS);
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_alas.png"); 
                break;
            case VASEN:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_vasen.png"), KenttäKohde.Suunta.VASEN);
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_vasen.png");    
                break;
            case OIKEA:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_vasen.png"), KenttäKohde.Suunta.OIKEA);
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_oikea.png");     
                break;
            default:
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_oikea.png");
                break;
        }
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kohdehuone=" + kohdeHuone;
        this.lisäOminaisuudet[1] = "kohderuutuX=" + kohdeRuutuX;
        this.lisäOminaisuudet[2] = "kohderuutuY=" + kohdeRuutuY;
        this.lisäOminaisuudet[3] = "suunta=" + annaSuunta();
        super.asetaTiedot();
    }

    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kohdehuone=" + kohdeHuone;
        this.lisäOminaisuudet[1] = "kohderuutuX=" + kohdeRuutuX;
        this.lisäOminaisuudet[2] = "kohderuutuY=" + kohdeRuutuY;
        this.lisäOminaisuudet[3] = "suunta=" + annaSuunta();
    }

    public ReunaWarppi(int sijX, int sijY, String[] ominaisuusLista) {
        
        this.nimi = "Oviruutu";
        this.katsomisTeksti = "Paina Spacea tai nuolen suuntaista nuolinäppäintä kulkeaksesi oviruudusta!";

        this.suunta = Suunta.VASEN;

        for (String ominaisuus : ominaisuusLista) {
            if (ominaisuus.startsWith("kohdehuone=")) {
                this.kohdeHuone = Integer.parseInt("" + ominaisuus.charAt(ominaisuus.indexOf("=") +1));
            }
            else if (ominaisuus.startsWith("kohderuutuX=")) {
                this.kohdeRuutuX = Integer.parseInt("" + ominaisuus.charAt(ominaisuus.indexOf("=") +1));
            }
            else if (ominaisuus.startsWith("kohderuutuY=")) {
                this.kohdeRuutuY = Integer.parseInt("" + ominaisuus.charAt(ominaisuus.indexOf("=") +1));
            }
            else if (ominaisuus.startsWith("suunta=")) {
                String suuntaString = ominaisuus.substring(7);
                switch (suuntaString) {
                    case "vasen", "VASEN": this.suunta = Suunta.VASEN; break;
                    case "oikea", "OIKEA": this.suunta = Suunta.OIKEA; break;
                    case "ylös", "YLÖS": this.suunta = Suunta.YLÖS; break;
                    case "alas", "ALAS": this.suunta = Suunta.ALAS; break;
                    default: this.suunta = Suunta.VASEN; break;
                }
            }
        }

        this.sijX = sijX;
        this.sijY = sijY;
        this.määritettySijainti = true;

        switch (suunta) {
            case YLÖS:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_ylös.png"), KenttäKohde.Suunta.YLÖS);
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_ylös.png"); 
                break;
            case ALAS:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_alas.png"), KenttäKohde.Suunta.ALAS);
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_alas.png"); 
                break;
            case VASEN:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_vasen.png"), KenttäKohde.Suunta.VASEN);
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_vasen.png");    
                break;
            case OIKEA:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_vasen.png"), KenttäKohde.Suunta.OIKEA);
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_oikea.png");     
                break;
            default:
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_oikea.png");
                break;
        }
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kohdehuone=" + kohdeHuone;
        this.lisäOminaisuudet[1] = "kohderuutuX=" + kohdeRuutuX;
        this.lisäOminaisuudet[2] = "kohderuutuY=" + kohdeRuutuY;
        this.lisäOminaisuudet[3] = "suunta=" + annaSuunta();
        super.asetaTiedot();
    }
}
