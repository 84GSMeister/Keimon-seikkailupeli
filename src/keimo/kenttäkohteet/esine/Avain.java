package keimo.kenttäkohteet.esine;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public final class Avain extends Esine {

    @Override
    public String käytä(){
        super.poista = true;
        return käyttöTeksti;
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Mitäs sit jos haluiskin monikkomuodot vielä kaikille?
        //Entäs kaikki muut liitteet ynnä possessiivisuffiksi?
        //Tähän en kyllä rupee.
        switch (sijamuoto) {
            case "nominatiivi":  return "Avain";
            case "genetiivi":    return "Avaimen";
            case "esiivi":       return "Avaimena";
            case "partitiivi":   return "Avainta";
            case "translatiivi": return "Avaimeksi";
            case "inessiivi":    return "Avaimessa";
            case "elatiivi":     return "Avaimesta";
            case "illatiivi":    return "Avaimeen";
            case "adessiivi":    return "Avaimella";
            case "ablatiivi":    return "Avaimelta";
            case "allatiivi":    return "Avaimelle";
            default:             return "Avain";
        }
    }
    
    public Avain(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Avain";
        super.tiedostonNimi = "avain.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Onkohan kentällä jotain lukittua, johon tätä voisi käyttää?";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Kirstu");
        super.liikeNopeus = 6f;
        super.pyörimisNopeus = 2f;
        super.asetaTiedot();
    }
}
