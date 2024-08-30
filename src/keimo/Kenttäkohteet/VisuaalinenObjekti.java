package keimo.Kenttäkohteet;

import keimo.Utility.*;
import keimo.Utility.KäännettäväKuvake.KääntöValinta;
import keimo.Utility.KäännettäväKuvake.PeilausValinta;

import java.io.File;
import javax.swing.ImageIcon;

public class VisuaalinenObjekti extends KenttäKohde {

    private int kääntöAsteet = 0;
    private boolean xPeilaus = false;
    private boolean yPeilaus = false;

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        return katsomisTeksti;
    }
    
    private boolean este = false;
    public boolean onkoEste() {
        return este;
    }

    public String annaKuvanTiedostoNimi() {
        return tiedostonNimi;
    }

    public int annaKääntöAsteet() {
        return kääntöAsteet;
    }

    public boolean annaXPeilaus() {
        return xPeilaus;
    }

    public boolean annaYPeilaus() {
        return yPeilaus;
    }

    private boolean katsottava = false;
    public boolean onkoKatsottava() {
        return katsottava;
    }
    public void asetaKatsottava(boolean katsottava) {
        this.katsottava = katsottava;
    }

    private String katsomisDialogi;
    public String annaKatsomisDialogi() {
        return katsomisDialogi;
    }
    public void asetaKatsomisDialogi(String dialogi) {
        if (dialogi == null) {
            this.katsottava = false;
            this.katsomisDialogi = null;
        }
        else {
            this.katsottava = true;
            this.katsomisDialogi = dialogi;
        }
        päivitäLisäOminaisuudet();
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
        File kuvaTiedosto = new File("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/" + tiedostonNimi);
        if (kuvaTiedosto.isFile()) {
            if (kääntöAsteet == 0 && !xPeilaus && !yPeilaus) {
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/" + tiedostonNimi);
            }
            else {
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/" + tiedostonNimi);
                this.kuvake = new KäännettäväKuvake(kuvake, kääntöAsteet, xPeilaus, yPeilaus);
            }
        }
        else {
            this.kuvake = new ImageIcon("tiedostot/kuvat/virhekuva_objekti.png");
        }
    }

    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[6];
        this.lisäOminaisuudet[0] = "kuva=" + tiedostonNimi;
        this.lisäOminaisuudet[1] = "kääntö=" + kääntöAsteet;
        this.lisäOminaisuudet[2] = "x-peilaus=" + (xPeilaus ? "kyllä" : "ei");
        this.lisäOminaisuudet[3] = "y-peilaus=" + (yPeilaus ? "kyllä" : "ei");
        this.lisäOminaisuudet[4] = "katsottava=" + (katsottava ? "kyllä" : "ei");
        this.lisäOminaisuudet[5] = "dialogi=" + katsomisDialogi;
        super.asetaTiedot();
    }

    public VisuaalinenObjekti(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY);
        this.nimi = "Koriste-esine";
        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kuva=")) {
                    tiedostonNimi = ominaisuus.substring(5);
                    this.katsomisTeksti = ominaisuus.substring(5, ominaisuus.length()-4);
                }
                else if (ominaisuus.startsWith("kääntö=")) {
                    try {
                        this.kääntöAsteet = Integer.parseInt(ominaisuus.substring(7));
                    }
                    catch (NumberFormatException e) {
                        System.out.println("virheellinen syöte: " + kääntöAsteet);
                        e.printStackTrace();
                        this.kääntöAsteet = 0;
                    }
                }
                else if (ominaisuus.startsWith("x-peilaus=")) {
                    if (ominaisuus.substring(10).startsWith("kyllä")) {
                        this.xPeilaus = true;
                    }
                    else {
                        this.xPeilaus = false;
                    }
                }
                else if (ominaisuus.startsWith("y-peilaus=")) {
                    if (ominaisuus.substring(10).startsWith("kyllä")) {
                        this.yPeilaus = true;
                    }
                    else {
                        this.yPeilaus = false;
                    }
                }
                else if (ominaisuus.startsWith("katsottava=")) {
                    if (ominaisuus.substring(11).startsWith("kyllä")) {
                        this.katsottava = true;
                    }
                    else {
                        this.katsottava = false;
                    }
                }
                else if (ominaisuus.startsWith("dialogi=")) {
                    this.katsomisDialogi = ominaisuus.substring(8);
                }
            }
            if (tiedostonNimi.endsWith("_e.png")) {
                this.este = true;
            }
            this.lisäOminaisuuksia = true;
            this.lisäOminaisuudet = new String[6];
            this.lisäOminaisuudet[0] = "kuva=" + tiedostonNimi;
            this.lisäOminaisuudet[1] = "kääntö=" + kääntöAsteet;
            this.lisäOminaisuudet[2] = "x-peilaus=" + (xPeilaus ? "kyllä" : "ei");
            this.lisäOminaisuudet[3] = "y-peilaus=" + (yPeilaus ? "kyllä" : "ei");
            this.lisäOminaisuudet[4] = "katsottava=" + (katsottava ? "kyllä" : "ei");
            this.lisäOminaisuudet[5] = "dialogi=" + katsomisDialogi;
        }
        else {
            this.lisäOminaisuuksia = false;
        }
        päivitäKuvanAsento();
        super.asetaTiedot();
    }
}
