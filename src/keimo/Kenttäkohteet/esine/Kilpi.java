package keimo.kenttäkohteet.esine;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import javax.swing.ImageIcon;

public final class Kilpi extends Esine {
    
    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Ootteko miettiny sitä, että onko tässä hommassa joku konsistenssi?
        //Milloin tarkalleen sanan runkoon tulee kirjainmuutos?
        switch (sijamuoto) {
            case "nominatiivi": return "Kilpi";
            case "genetiivi": return "Kilven";
            case "esiivi": return "Kilpenä";
            case "partitiivi": return "Kilpeä";
            case "translatiivi": return "Kilveksi";
            case "inessiivi": return "Kilvessä";
            case "elatiivi": return "Kilvestä";
            case "illatiivi": return "Kilpeen";
            case "adessiivi": return "Kilvellä";
            case "ablatiivi": return "Kilveltä";
            case "allatiivi": return "Kilvelle";
            default: return "Kilpi";
        }
    }

    public Kilpi(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kilpi";
        super.tiedostonNimi = "kilpi.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Pidä kilpeä kädessä kun menet vihollisen luo!";
        super.asetaTiedot();
    }
}
