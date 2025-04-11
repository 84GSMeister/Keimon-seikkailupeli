package keimo.kenttäkohteet.esine;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import javax.swing.ImageIcon;

public final class Hiili extends Esine {

    @Override
    public String käytä() {
        super.poista = true;
        return super.käytä();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Hiili h = new Hiili(false, 0, 0);
        //System.out.println("Mä käyn ihan " + h.annaNimiSijamuodossa("essiivi") + " tässä yrittäessäni koodata.");
        // ^ Mä käyn ihan Hiilenä tässä yrittäessäni koodata.
        switch (sijamuoto) {
            case "nominatiivi":  return "Hiili";
            case "genetiivi":    return "Hiilen";
            case "esiivi":       return "Hiilenä";
            case "partitiivi":   return "Hiiltä";
            case "translatiivi": return "Hiileksi";
            case "inessiivi":    return "Hiilessä";
            case "elatiivi":     return "Hiilestä";
            case "illatiivi":    return "Hiileen";
            case "adessiivi":    return "Hiilellä";
            case "ablatiivi":    return "Hiileltä";
            case "allatiivi":    return "Hiilelle";
            default:             return "Hiili";
        }
    }

    public Hiili(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Hiili";
        super.tiedostonNimi = "hiili.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Tämä sopisi hyvin nuotiossa poltettavaksi.";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Nuotio");
        super.liikeNopeus = 6f;
        super.pyörimisNopeus = 2f;
        super.asetaTiedot();
    }
}
