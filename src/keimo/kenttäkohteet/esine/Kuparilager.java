package keimo.kenttäkohteet.esine;

import keimo.Pelaaja;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import javax.swing.ImageIcon;
import java.text.DecimalFormat;

public final class Kuparilager extends Juoma {

    DecimalFormat df = new DecimalFormat("##.##");

    @Override
    public String käytä(){
        super.käytä();
        Pelaaja.kuparit++;
        ÄänentoistamisSäie.toistaSFX("tölkki");
        return "Rahaa tulossa tölkeistä: " + df.format(0.15f * Pelaaja.kuparit) + "€";
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kuparilager";
            case "genetiivi":    return "Kuparilagerin";
            case "esiivi":       return "Kuparilagerina";
            case "partitiivi":   return "Kuparilageria";
            case "translatiivi": return "Kuparilageriksi";
            case "inessiivi":    return "Kuparilagerissa";
            case "elatiivi":     return "Kuparilagerista";
            case "illatiivi":    return "Kuparilageriin";
            case "adessiivi":    return "Kuparilagerilla";
            case "ablatiivi":    return "Kuparilagerilta";
            case "allatiivi":    return "Kuparilagerille";
            default:             return "Kuparilager";
        }
    }

    public Kuparilager(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kuparilager";
        super.tiedostonNimi = "kuparilager.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Uskollinen kupari";
        super.käyttö = true;
        super.kolmiUlotteinen = true;
        super.obj3dMallinTunniste = "tölkki";
        super.hinta = 1.05;
        super.voltit = 0.4f;
        super.känniKuolemattomuus = 400;
        super.asetaTiedot();
    }
}
