package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Paperi extends Esine {

    public String katso(){
        System.out.println("Tämä sopisi hyvin nuotion sytykkeeksi.");
        return "Tämä sopisi hyvin nuotion sytykkeeksi.";
    }

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
        super.sopiiKäytettäväksi.add("Nuotio");
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/paperi.png");
        super.asetaTiedot();
    }
}
