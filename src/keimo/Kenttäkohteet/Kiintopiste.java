package keimo.Kenttäkohteet;
import java.util.ArrayList;

import keimo.PääIkkuna;

public abstract class Kiintopiste extends KenttäKohde {

    boolean vuorovaikutus = false;
    public boolean vuorovaikutusToimii() {
        return vuorovaikutus;
    }

    ArrayList<String> käyvätEsineet = new ArrayList<String>();

    public String kokeileEsinettä(Esine e) {
        System.out.println("Mitään ei tapahtunut!");
        return "Mitään ei tapahtunut!";
    }

    public Esine suoritaMuutoksetEsineelle(Esine e) {
        PääIkkuna.hudTeksti.setText(e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + this.annaNimiSijamuodossa("illatiivi"));
        return e;
    }

    public String annaNimi(){
        return nimi;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Tämä kohde";
            case "genetiivi":
                return "Tämän kohteem";
            case "esiivi":
                return "Tänä kohteena";
            case "partitiivi":
                return "Tätä kohdetta";
            case "translatiivi":
                return "Täksi kohteeksi";
            case "inessiivi":
                return "Tässä kohteessa";
            case "elatiivi":
                return "Tästä kohteesta";
            case "illatiivi":
                return "Tähän kohteeseen";
            case "adessiivi":
                return "Tällä kohteella";
            case "ablatiivi":
                return "Tältä kohteelta";
            case "allatiivi":
                return "Tälle kohteelle";
            default:
                return "Tämä kohde";
        }
    }

    public Kiintopiste(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }
}
