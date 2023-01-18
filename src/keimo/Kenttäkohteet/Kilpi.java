package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Kilpi extends Esine{
    
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Kilpi";
            case "genetiivi":
                return "Kilven";
            case "esiivi":
                return "Kilpenä";
            case "partitiivi":
                return "Kilpeä";
            case "translatiivi":
                return "Kilveksi";
            case "inessiivi":
                return "Kilvessä";
            case "elatiivi":
                return "Kilvestä";
            case "illatiivi":
                return "Kilpeen";
            case "adessiivi":
                return "Kilvellä";
            case "ablatiivi":
                return "Kilveltä";
            case "allatiivi":
                return "Kilvelle";
            default:
                return "Kilpi";
        }
    }

    public Kilpi() {
        this.nimi = "Kilpi";
        this.kuvake = new ImageIcon("tiedostot/kuvat/kilpi.png");
        this.katsomisTeksti = "Pidä kilpeä kädessä kun menet vihollisen luo!";
        super.asetaTiedot();
    }
    public Kilpi(int sijX, int sijY) {
        this.määritettySijainti = true;
        this.sijX = sijX;
        this.sijY = sijY;
        this.nimi = "Kilpi";
        this.kuvake = new ImageIcon("tiedostot/kuvat/kilpi.png");
        this.katsomisTeksti = "Pidä kilpeä kädessä kun menet vihollisen luo!";
        super.asetaTiedot();
    }
}
