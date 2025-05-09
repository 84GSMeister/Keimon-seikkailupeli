package keimo.kenttäkohteet.kiintopiste;

import keimo.Utility.KäännettäväKuvake.KääntöValinta;
import keimo.Utility.KäännettäväKuvake.PeilausValinta;
import keimo.kenttäkohteet.KenttäKohde;

import java.util.ArrayList;

public abstract class Kiintopiste extends KenttäKohde {

    ArrayList<String> käyvätEsineet = new ArrayList<String>();

    public Kiintopiste(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY);
        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (this.lisäOminaisuudet == null) this.lisäOminaisuudet = new ArrayList<>();
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
                    if (ominaisuus.substring(10).startsWith("kyllä")) xPeilaus = true;
                    else xPeilaus = false;
                }
                else if (ominaisuus.startsWith("y-peilaus=")) {
                    if (ominaisuus.substring(10).startsWith("kyllä")) yPeilaus = true;
                    else yPeilaus = false;
                }
            }
            päivitäLisäOminaisuudet();
        }
        else {
            this.lisäOminaisuuksia = false;
        }
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Tämä kohde";
            case "genetiivi":    return "Tämän kohteem";
            case "essiivi":      return "Tänä kohteena";
            case "partitiivi":   return "Tätä kohdetta";
            case "translatiivi": return "Täksi kohteeksi";
            case "inessiivi":    return "Tässä kohteessa";
            case "elatiivi":     return "Tästä kohteesta";
            case "illatiivi":    return "Tähän kohteeseen";
            case "adessiivi":    return "Tällä kohteella";
            case "ablatiivi":    return "Tältä kohteelta";
            case "allatiivi":    return "Tälle kohteelle";
            default:             return "Tämä kohde";
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
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kääntö="));
        this.lisäOminaisuudet.add("kääntö=" + kääntöAsteet);
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("x-peilaus="));
        this.lisäOminaisuudet.add("x-peilaus=" + (xPeilaus ? "kyllä" : "ei"));
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("y-peilaus="));
        this.lisäOminaisuudet.add("y-peilaus=" + (yPeilaus ? "kyllä" : "ei"));
        super.asetaTiedot();
    }
}
