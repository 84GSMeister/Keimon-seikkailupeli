package keimo.Kenttäkohteet;

import keimo.Utility.*;
import keimo.Utility.KäännettäväKuvake.KääntöValinta;
import keimo.Utility.KäännettäväKuvake.PeilausValinta;

import javax.swing.ImageIcon;

public class VisuaalinenObjekti extends KenttäKohde {

    int kääntöAsteet = 0;
    boolean xPeilaus = false;
    boolean yPeilaus = false;
    protected String tiedostonNimi;
    
    private boolean este = false;
    public boolean onkoEste() {
        return este;
    }

    public String annaKuvanTiedostoNimi() {
        return tiedostonNimi;
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
        if (kääntöAsteet == 0 && !xPeilaus && !yPeilaus) {
            kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/" + tiedostonNimi);
        }
        else {
            kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/" + tiedostonNimi);
            kuvake = new KäännettäväKuvake(kuvake, kääntöAsteet, xPeilaus, yPeilaus);
        }
    }

    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kuva=" + tiedostonNimi;
        this.lisäOminaisuudet[1] = "kääntö=" + kääntöAsteet;
        this.lisäOminaisuudet[2] = "x-peilaus=" + (xPeilaus ? "kyllä" : "ei");
        this.lisäOminaisuudet[3] = "y-peilaus=" + (yPeilaus ? "kyllä" : "ei");
        super.asetaTiedot();
    }

    public VisuaalinenObjekti(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY);
        this.nimi = "Koriste-esine";
        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kuva=")) {
                    tiedostonNimi = ominaisuus.substring(5);
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
            if (tiedostonNimi.endsWith("_e.png")) {
                this.este = true;
            }
            switch (tiedostonNimi) {
                case "":
                    this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/vakiokuva.png");
                break;
                default:
                    this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/" + tiedostonNimi);
                break;
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
