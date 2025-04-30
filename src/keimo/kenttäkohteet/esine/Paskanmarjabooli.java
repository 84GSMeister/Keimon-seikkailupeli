package keimo.kenttäkohteet.esine;

import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.grafiikat.Tekstuuri;

public class Paskanmarjabooli extends Juoma {

    @Override
    public String käytä(){
        super.käytä();
        ÄänentoistamisSäie.toistaSFX("Käytä");
        return katso();
    }
    
    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Paskanmarjabooli";
            case "genetiivi":    return "Paskanmarjaboolin";
            case "esiivi":       return "Paskanmarjaboolina";
            case "partitiivi":   return "Paskanmarjaboolia";
            case "translatiivi": return "Paskanmarjabooliksi";
            case "inessiivi":    return "Paskanmarjaboolissa";
            case "elatiivi":     return "Paskanmarjaboolista";
            case "illatiivi":    return "Paskanmarjabooliin";
            case "adessiivi":    return "Paskanmarjaboolilla";
            case "ablatiivi":    return "Paskanmarjaboolilta";
            case "allatiivi":    return "Paskanmarjaboolille";
            default:             return "Paskanmarjabooli";
        }
    }

    public Paskanmarjabooli(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Paskanmarjabooli";
        super.tiedostonNimi = "paskanmarjabooli.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Tällä pääsee kuuhun asti!";
        super.käyttö = true;
        super.voltit = 6;
        super.känniKuolemattomuus = 12000;
        super.asetaTiedot();
    }
}
