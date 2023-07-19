package keimo.Kenttäkohteet;

import keimo.Utility.KäännettäväKuvake;

import javax.swing.ImageIcon;


public class Avain extends Esine {

    @Override
    public String käytä(){
        super.poista = true;
        return käyttöTeksti;
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Avain";
            case "genetiivi":
                return "Avaimen";
            case "esiivi":
                return "Avaimena";
            case "partitiivi":
                return "Avainta";
            case "translatiivi":
                return "Avaimeksi";
            case "inessiivi":
                return "Avaimessa";
            case "elatiivi":
                return "Avaimesta";
            case "illatiivi":
                return "Avaimeen";
            case "adessiivi":
                return "Avaimella";
            case "ablatiivi":
                return "Avaimelta";
            case "allatiivi":
                return "Avaimelle";
            default:
                return "Avain";
        }
    }
    
    public Avain(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Avain";
        super.kenttäkäyttö = true;
        super.sopiiKäytettäväksi.add("Kirstu");
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/avain.png");
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
        super.katsomisTeksti = "Onkohan kentällä jotain lukittua, johon tätä voisi käyttää?";
        super.asetaTiedot();
    }
}
