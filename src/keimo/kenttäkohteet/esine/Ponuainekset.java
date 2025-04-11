package keimo.kenttäkohteet.esine;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import javax.swing.ImageIcon;

public final class Ponuainekset extends Esine {
    
    @Override
    public String käytä(){
        return käyttöTeksti;
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Pontikka-ainekset";
            case "genetiivi":    return "Pontikka-ainekset";
            case "esiivi":       return "Pontikka-ainekset";
            case "partitiivi":   return "Pontikka-ainekset";
            case "translatiivi": return "Pontikka-ainekset";
            case "inessiivi":    return "Pontikka-ainekset";
            case "elatiivi":     return "Pontikka-ainekset";
            case "illatiivi":    return "Pontikka-ainekset";
            case "adessiivi":    return "Pontikka-ainekset";
            case "ablatiivi":    return "Pontikka-ainekset";
            case "allatiivi":    return "Pontikka-ainekset";
            default:             return "Pontikka-ainekset";
        }
    }
    
    public Ponuainekset(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Pontikka-ainekset";
        super.tiedostonNimi = "ponuainekset.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Ponua voisi käyttää boolissa.";
        super.hinta = 8 * 0.22 + 4 * 1.55 + 4.99;
        super.asetaTiedot();
    }
}
