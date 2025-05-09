package keimo.kenttäkohteet;

import keimo.Utility.KäännettäväKuvake.KääntöValinta;
import keimo.Utility.KäännettäväKuvake.PeilausValinta;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.util.ArrayList;

public class VisuaalinenObjekti extends KenttäKohde {

    public VisuaalinenObjekti(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY);
        this.nimi = "Koriste-esine";
        if (ominaisuusLista != null) {
            this.lisäOminaisuudet = new ArrayList<>();
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kuva=")) {
                    tiedostonNimi = ominaisuus.substring(5);
                    this.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/" + tiedostonNimi);
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
            päivitäLisäOminaisuudet();
        }
        else {
            this.lisäOminaisuuksia = false;
        }
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        return katsomisTeksti;
    }
    
    public String annaKuvanTiedostoNimi() {
        return tiedostonNimi;
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
        if (this.lisäOminaisuudet != null) {
            this.lisäOminaisuuksia = true;
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kuva="));
            this.lisäOminaisuudet.add("kuva="+ tiedostonNimi);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kääntö="));
            this.lisäOminaisuudet.add("kääntö=" + kääntöAsteet);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("x-peilaus="));
            this.lisäOminaisuudet.add("x-peilaus=" + (xPeilaus ? "kyllä" : "ei"));
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("y-peilaus="));
            this.lisäOminaisuudet.add("y-peilaus=" + (yPeilaus ? "kyllä" : "ei"));
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("katsottava="));
            this.lisäOminaisuudet.add("katsottava=" + (katsottava ? "kyllä" : "ei"));
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("dialogi="));
            this.lisäOminaisuudet.add("dialogi=" + katsomisDialogi);
            super.asetaTiedot();
        }
    }
}
