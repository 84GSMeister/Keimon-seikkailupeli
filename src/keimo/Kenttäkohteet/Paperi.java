package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Paperi extends Esine {

    @Override
    public String käytä() {
        poista = true;
        return super.käytä();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Paperi";
            case "genetiivi":
                return "Paperin";
            case "esiivi":
                return "Paperina";
            case "partitiivi":
                return "Paperia";
            case "translatiivi":
                return "Paperiksi";
            case "inessiivi":
                return "Paperissa";
            case "elatiivi":
                return "Paperista";
            case "illatiivi":
                return "Paperiin";
            case "adessiivi":
                return "Paperilla";
            case "ablatiivi":
                return "Paperilta";
            case "allatiivi":
                return "Paperille";
            default:
                return "Paperi";
        }
    }

    public Paperi(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Paperi";
        super.kenttäkäyttö = true;
        super.katsomisTeksti = "Tämä sopisi hyvin nuotion sytykkeeksi.";
        super.sopiiKäytettäväksi.add("Nuotio");
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/paperi.png");
        super.asetaTiedot();
    }
}
