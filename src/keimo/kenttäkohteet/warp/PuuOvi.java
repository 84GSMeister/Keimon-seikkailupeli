package keimo.kenttäkohteet.warp;

import keimo.Peli;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.Utility.KäännettäväKuvake;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import javax.swing.ImageIcon;

public final class PuuOvi extends Warp {

    private Tekstuuri suljettuTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/puuovi.png");
    private Tekstuuri avattuTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/puuovi_avattu.png");
    
    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Puuovi";
            case "genetiivi":    return "Puuoven";
            case "esiivi":       return "Puuovena";
            case "partitiivi":   return "Puuovea";
            case "translatiivi": return "Puuoveksi";
            case "inessiivi":    return "Puuovessa";
            case "elatiivi":     return "Puuovesta";
            case "illatiivi":    return "Puuoveen";
            case "adessiivi":    return "Puuovella";
            case "ablatiivi":    return "Puuovelta";
            case "allatiivi":    return "Puuovelle";
            default:             return "Puuovi";
        }
    }
    
    @Override
    public void asetaSuunta(Suunta suunta) {
        super.asetaSuunta(suunta);
        this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/puuovi.png");
        switch (suunta) {
            case YLÖS:
                this.suunta = Suunta.YLÖS;
                this.kuvake = new KäännettäväKuvake(kuvake, 0);
                break;
            case ALAS:
                this.suunta = Suunta.ALAS;
                this.kuvake = new KäännettäväKuvake(kuvake, 180);
                break;
            case VASEN:
                this.suunta = Suunta.VASEN;
                this.kuvake = new KäännettäväKuvake(kuvake, 270);
                break;
            case OIKEA:
                this.suunta = Suunta.OIKEA;
                this.kuvake = new KäännettäväKuvake(kuvake, 90);
                break;
            default:
                this.suunta = Suunta.YLÖS;
                this.kuvake = new KäännettäväKuvake(kuvake, 0);
                break;
        }
    }

    @Override
    public void ennenWarppia() {
        ÄänentoistamisSäie.toistaSFX("oven_avaus");
        this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/puuovi_avattu.png");
        this.tekstuuri = avattuTekstuuri;
        try {
            if (Peli.legacy) Thread.sleep(1700);
        }
        catch (InterruptedException ie) {
            System.out.println("Kuka kehtaa keskeyttää nukkumiseni? T. Vihainen Pelisäie");
            ie.printStackTrace();
        }
        finally {
           this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/puuovi.png");
        }
    }

    @Override
    public void warpinJälkeen() {
        ÄänentoistamisSäie.toistaSFX("oven_sulkeminen");
        this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/puuovi.png");
        this.tekstuuri = suljettuTekstuuri;
    }

    public PuuOvi(int sijX, int sijY, String[] ominaisuusLista) {
        super(true, sijX, sijY);
        this.nimi = "Puuovi";

        this.suunta = Suunta.VASEN;

        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kohdehuone=")) {
                    this.kohdeHuone = Integer.parseInt("" + ominaisuus.substring(ominaisuus.indexOf("=") +1));
                }
                else if (ominaisuus.startsWith("kohderuutuX=")) {
                    this.kohdeRuutuX = Integer.parseInt("" + ominaisuus.substring(ominaisuus.indexOf("=") +1));
                }
                else if (ominaisuus.startsWith("kohderuutuY=")) {
                    this.kohdeRuutuY = Integer.parseInt("" + ominaisuus.substring(ominaisuus.indexOf("=") +1));
                }
                else if (ominaisuus.startsWith("suunta=")) {
                    String suuntaString = ominaisuus.substring(7);
                    switch (suuntaString) {
                        case "vasen", "VASEN", "Vasen": this.suunta = Suunta.VASEN; break;
                        case "oikea", "OIKEA", "Oikea": this.suunta = Suunta.OIKEA; break;
                        case "ylös", "YLÖS", "Ylös": this.suunta = Suunta.YLÖS; break;
                        case "alas", "ALAS", "Alas": this.suunta = Suunta.ALAS; break;
                        default: this.suunta = Suunta.YLÖS; break;
                    }
                }
            }
        }

        super.tiedostonNimi = "puuovi.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = suljettuTekstuuri;
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
