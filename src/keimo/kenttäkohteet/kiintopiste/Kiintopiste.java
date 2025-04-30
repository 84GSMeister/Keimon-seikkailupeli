package keimo.kenttäkohteet.kiintopiste;

import keimo.Utility.KäännettäväKuvake.KääntöValinta;
import keimo.Utility.KäännettäväKuvake.PeilausValinta;
import keimo.kenttäkohteet.KenttäKohde;

import java.util.ArrayList;

public abstract class Kiintopiste extends KenttäKohde {

    ArrayList<String> käyvätEsineet = new ArrayList<String>();

    public String annaNimi(){
        return nimi;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Tämä kohde";
            case "genetiivi": return "Tämän kohteem";
            case "esiivi": return "Tänä kohteena";
            case "partitiivi": return "Tätä kohdetta";
            case "translatiivi": return "Täksi kohteeksi";
            case "inessiivi": return "Tässä kohteessa";
            case "elatiivi": return "Tästä kohteesta";
            case "illatiivi": return "Tähän kohteeseen";
            case "adessiivi": return "Tällä kohteella";
            case "ablatiivi": return "Tältä kohteelta";
            case "allatiivi": return "Tälle kohteelle";
            default: return "Tämä kohde";
        }
    }

    public void käännäKuvaa(KääntöValinta kääntö) {
        switch (kääntö) {
            case MYÖTÄPÄIVÄÄN:
                this.kääntöAsteet += 90;
                this.kääntöAsteet = kääntöAsteet % 360;
            break;
            case VASTAPÄIVÄÄN:
                this.kääntöAsteet += 270;
                this.kääntöAsteet = kääntöAsteet % 360;
            break;
        }
        päivitäLisäOminaisuudet();
    }

    public void peilaaKuva(PeilausValinta peilaus) {
        switch (peilaus) {
            case PEILAA_VAAKA:
                if (this.xPeilaus) {
                    this.xPeilaus = false;
                }
                else {
                    this.xPeilaus = true;
                }
            break;
            case PEILAA_PYSTY:
                if (this.yPeilaus) {
                    this.yPeilaus = false;
                }
                else {
                    this.yPeilaus = true;
                }
            break;
        }
        päivitäLisäOminaisuudet();
    }

    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuudet[1] = "kääntö=" + kääntöAsteet;
        this.lisäOminaisuudet[2] = "x-peilaus=" + (xPeilaus ? "kyllä" : "ei");
        this.lisäOminaisuudet[3] = "y-peilaus=" + (yPeilaus ? "kyllä" : "ei");
        super.asetaTiedot();
    }

    public Kiintopiste(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY);
        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kuva=")) {
                    tiedostonNimi = ominaisuus.substring(5);
                    this.katsomisTeksti = ominaisuus.substring(5, ominaisuus.length()-4);
                }
                else if (ominaisuus.startsWith("kääntö=")) {
                    try {
                        kääntöAsteet = Integer.parseInt(ominaisuus.substring(7));
                    }
                    catch (NumberFormatException e) {
                        System.out.println("virheellinen syöte: " + kääntöAsteet);
                        e.printStackTrace();
                        kääntöAsteet = 0;
                    }
                }
                else if (ominaisuus.startsWith("x-peilaus=")) {
                    if (ominaisuus.substring(10).startsWith("kyllä")) {
                        xPeilaus = true;
                    }
                    else {
                        xPeilaus = false;
                    }
                }
                else if (ominaisuus.startsWith("y-peilaus=")) {
                    if (ominaisuus.substring(10).startsWith("kyllä")) {
                        yPeilaus = true;
                    }
                    else {
                        yPeilaus = false;
                    }
                }
            }
            this.lisäOminaisuuksia = true;
            this.lisäOminaisuudet = new String[4];
            this.lisäOminaisuudet[0] = "kuva=" + tiedostonNimi;
            this.lisäOminaisuudet[1] = "kääntö=" + kääntöAsteet;
            this.lisäOminaisuudet[2] = "x-peilaus=" + (xPeilaus ? "kyllä" : "ei");
            this.lisäOminaisuudet[3] = "y-peilaus=" + (yPeilaus ? "kyllä" : "ei");
        }
        else {
            this.lisäOminaisuuksia = false;
        }
        super.asetaTiedot();
    }
}
