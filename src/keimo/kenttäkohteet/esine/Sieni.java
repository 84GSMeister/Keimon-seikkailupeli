package keimo.kenttäkohteet.esine;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public class Sieni extends Ruoka {

    public Sieni(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Sieni";
        super.tiedostonNimi = "sieni.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.käyttö = true;
        super.heal = 3;
        super.kolmiUlotteinen = true;
        super.obj3dMallinTunniste = "Sieni";
        super.katsomisTeksti = "Kärpässienelläkin voi kuulemma saada aikaan jonkinmoisen efektin, vaikka se ei oikeaa taikasientä vastaakaan.";
        super.asetaTiedot();
    }

    @Override
    public String käytä() {
        super.käytä();
        super.poista = true;
        return "Sieniä, sieniä! (sait 3 elämäpistettä)";
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Sieni";
            case "genetiivi":    return "Sienen";
            case "esiivi":       return "Sienenä";
            case "partitiivi":   return "Sientä";
            case "translatiivi": return "Sieneksi";
            case "inessiivi":    return "Sienessä";
            case "elatiivi":     return "Sienestä";
            case "illatiivi":    return "Sieneen";
            case "adessiivi":    return "Sienellä";
            case "ablatiivi":    return "Sieneltä";
            case "allatiivi":    return "Sienelle";
            default:             return "Sieni";
        }
    }
}
