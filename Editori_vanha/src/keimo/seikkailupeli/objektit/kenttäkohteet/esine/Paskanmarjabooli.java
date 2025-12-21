package keimo.seikkailupeli.objektit.kenttäkohteet.esine;

public class Paskanmarjabooli extends Juoma {

    public Paskanmarjabooli(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Paskanmarjabooli";
        super.tiedostonNimi = "paskanmarjabooli.png";
        super.katsomisTeksti = "Tällä pääsee kuuhun asti!";
        super.käyttö = true;
        super.voltit = 6;
        super.känniKuolemattomuus = 12000;
        super.asetaTiedot();
    }

    @Override
    public String käytä(){
        super.käytä();
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
}
