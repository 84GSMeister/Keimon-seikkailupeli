package keimo.kenttäkohteet.kiintopiste;

import keimo.Utility.*;
import keimo.Utility.KäännettäväKuvake.KääntöValinta;
import keimo.Utility.KäännettäväKuvake.PeilausValinta;
import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.esine.Esine;

import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public abstract class Kiintopiste extends KenttäKohde {

    protected boolean erillisDialogi = false;
    public boolean onkoErillisDialogi() {
        return erillisDialogi;
    }

    protected boolean ignooraaEsineValintaDialogissa = false;
    public boolean ignooraaEsineValinta() {
        return ignooraaEsineValintaDialogissa;
    }

    protected boolean ohitaDialogiTesktiboksi = false;
    public boolean ohitaDialogiTesktiboksi() {
        return ohitaDialogiTesktiboksi;
    }

    boolean vuorovaikutus = false;
    public boolean vuorovaikutusToimii() {
        return vuorovaikutus;
    }

    ArrayList<String> käyvätEsineet = new ArrayList<String>();

    public String kokeileEsinettä(Esine e) {
        return e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + this.annaNimiSijamuodossa("illatiivi") + ".";
    }

    public Esine suoritaMuutoksetEsineelle(Esine e) {
        return super.suoritaMuutoksetEsineelle(e);
    }

    public String vuorovaikuta(Esine e) {
        return "Vuorovaikutus";
    }

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
        päivitäKuvanAsento();
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
        päivitäKuvanAsento();
        päivitäLisäOminaisuudet();
    }

    public void päivitäKuvanAsento() {
        File kuvaTiedosto = new File("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        if (kuvaTiedosto.isFile()) {
            if (kääntöAsteet == 0 && !xPeilaus && !yPeilaus) {
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
            }
            else {
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
                this.kuvake = new KäännettäväKuvake(kuvake, kääntöAsteet, xPeilaus, yPeilaus);
            }
        }
        else {
            if (kääntöAsteet == 0 && !xPeilaus && !yPeilaus) {
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppahylly_kuvavirhe.png");
            }
            else {
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppahylly_kuvavirhe.png");
                this.kuvake = new KäännettäväKuvake(kuvake, kääntöAsteet, xPeilaus, yPeilaus);
            }
        }
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
        päivitäKuvanAsento();
        super.asetaTiedot();
    }
}
