package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;
import java.text.DecimalFormat;

import keimo.Pelaaja;
import keimo.Säikeet.ÄänentoistamisSäie;

public class Kuparilager extends Esine {

    DecimalFormat df = new DecimalFormat("##.##");

    public String katso() {
        return "Kannattaa kerätä talteen kaikki tölkit.";
    }

    public String käytä(){
        poista = true;
        Pelaaja.kuparit++;
        ÄänentoistamisSäie.toistaSFX("tölkki");
        return "Rahaa tulossa tölkeistä: " + df.format(0.15f * Pelaaja.kuparit) + "€";
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Kuparilager";
            case "genetiivi":
                return "Kuparilagerin";
            case "esiivi":
                return "Kuparilagerina";
            case "partitiivi":
                return "Kuparilageria";
            case "translatiivi":
                return "Kuparilageriksi";
            case "inessiivi":
                return "Kuparilagerissa";
            case "elatiivi":
                return "Kuparilagerista";
            case "illatiivi":
                return "Kuparilageriin";
            case "adessiivi":
                return "Kuparilagerilla";
            case "ablatiivi":
                return "Kuparilagerilta";
            case "allatiivi":
                return "Kuparilagerille";
            default:
                return "Kuparilager";
        }
    }

    public Kuparilager(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kuparilager";
        super.katsomisTeksti = "0,15€ lisää saldoon!";
        super.käyttö = true;
        super.poista = true;
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kuparilager.png");
        super.asetaTiedot();
    }
}
