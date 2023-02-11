package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ReunaWarppi extends Warp {

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Oviruutu";
            case "genetiivi":
                return "Oviruudun";
            case "esiivi":
                return "Oviruutuna";
            case "partitiivi":
                return "Oviruutua";
            case "translatiivi":
                return "Oviruuduksi";
            case "inessiivi":
                return "Oviruudussa";
            case "elatiivi":
                return "Oviruudusta";
            case "illatiivi":
                return "Oviruutuun";
            case "adessiivi":
                return "Oviruudulla";
            case "ablatiivi":
                return "Oviruudulta";
            case "allatiivi":
                return "Oviruudulle";
            default:
                return "Oviruutu";
        }
    }
    
    public void asetaSuunta(Suunta suunta) {
        super.asetaSuunta(suunta);
        this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/reunawarppi.png");
        switch (suunta) {
            case YLÖS:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_ylös.png"), KenttäKohde.Suunta.YLÖS);
                //this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_ylös.png");
                this.suunta = Suunta.YLÖS;
                this.kuvake = new RotatedIcon(kuvake, 0, false);
                break;
            case ALAS:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_alas.png"), KenttäKohde.Suunta.ALAS);
                //this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_alas.png");
                this.suunta = Suunta.ALAS;
                this.kuvake = new RotatedIcon(kuvake, 180, false);
                break;
            case VASEN:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_vasen.png"), KenttäKohde.Suunta.VASEN);
                //this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_vasen.png");
                this.suunta = Suunta.VASEN;
                this.kuvake = new RotatedIcon(kuvake, 270, false);
                break;
            case OIKEA:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_vasen.png"), KenttäKohde.Suunta.OIKEA);
                //this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_oikea.png");
                this.suunta = Suunta.OIKEA;
                this.kuvake = new RotatedIcon(kuvake, 90, false);
                break;
            default:
                //this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_oikea.png");
                this.suunta = Suunta.YLÖS;
                this.kuvake = new RotatedIcon(kuvake, 0, false);
                break;
        }
    }

    public ReunaWarppi(int sijX, int sijY, int luontiKohdeHuone, int luontiKohdeRuutuX, int luontiKohdeRuutuY, Suunta luontiSuunta) {
        super(true, sijX, sijY, luontiKohdeHuone, luontiKohdeRuutuX, luontiKohdeRuutuY, luontiSuunta);
        this.nimi = "Oviruutu";
        this.katsomisTeksti = "Paina Spacea tai nuolen suuntaista nuolinäppäintä kulkeaksesi oviruudusta!";

        asetaSuunta(luontiSuunta);
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
        super(true, sijX, sijY, 0, 0, 0, Suunta.VASEN);
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

        // this.sijX = sijX;
        // this.sijY = sijY;
        // this.määritettySijainti = true;

        this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/reunawarppi.png");
        asetaSuunta(suunta);
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kohdehuone=" + kohdeHuone;
        this.lisäOminaisuudet[1] = "kohderuutuX=" + kohdeRuutuX;
        this.lisäOminaisuudet[2] = "kohderuutuY=" + kohdeRuutuY;
        this.lisäOminaisuudet[3] = "suunta=" + annaSuunta();
        super.asetaTiedot();
    }
}
