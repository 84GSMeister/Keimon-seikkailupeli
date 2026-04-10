package keimo.seikkailupeli.objektit.kenttäkohteet.esine;

import keimo.seikkailupeli.assets.Assets;

public final class Ponuainekset extends Esine {

    public Ponuainekset(int sijX, int sijY){
        super(sijX, sijY);
        super.nimi = "Leivonta-ainekset";
        super.tiedostonNimi = "ponuainekset.png";
        super.tekstuuri = Assets.annaTekstuuri("ponuainekset");
        super.katsomisTeksti = "Ponua voisi käyttää boolissa.";
        super.hinta = 8 * 0.22 + 4 * 1.55 + 4.99;
        super.asetaTiedot();
    }
    
    @Override
    public String käytä(){
        return käyttöTeksti;
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Leivonta-ainekset";
            case "genetiivi":    return "Leivonta-aineksien";
            case "esiivi":       return "Leivonta-aineksina";
            case "partitiivi":   return "Leivonta-aineksina";
            case "translatiivi": return "Leivonta-aineksiksi";
            case "inessiivi":    return "Leivonta-aineksissa";
            case "elatiivi":     return "Leivonta-aineksista";
            case "illatiivi":    return "Leivonta-aineksiin";
            case "adessiivi":    return "Leivonta-aineksilla";
            case "ablatiivi":    return "Leivonta-aineksilta";
            case "allatiivi":    return "Leivonta-aineksille";
            default:             return "Leivonta-ainekset";
        }
    }
}
