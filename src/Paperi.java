import javax.swing.ImageIcon;

public class Paperi extends Esine {

    String katso(){
        System.out.println("Tämä sopisi hyvin nuotion sytykkeeksi.");
        return "Tämä sopisi hyvin nuotion sytykkeeksi.";
    }

    String annaNimiSijamuodossa(String sijamuoto) {
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

    Paperi(){
        this.nimi = "Paperi";
        this.kenttäkäyttö = true;
        this.sopiiKäytettäväksi = "Nuotio";
        this.kuvake = new ImageIcon("tiedostot/kuvat/paperi.png");
    }
}
