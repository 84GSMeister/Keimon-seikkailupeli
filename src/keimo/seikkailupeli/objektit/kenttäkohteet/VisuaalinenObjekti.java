package keimo.seikkailupeli.objektit.kenttäkohteet;

import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.Renderöitävä;

import java.util.ArrayList;
import java.util.HashMap;

public class VisuaalinenObjekti extends KenttäKohde {

    private static HashMap<String, Renderöitävä> objektiTekstuurit = new HashMap<>();

    public VisuaalinenObjekti(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Koriste-esine";
        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kuva=")) {
                    tiedostonNimi = ominaisuus.substring(5);
                    if (objektiTekstuurit.containsKey(tiedostonNimi)) {
                        this.tekstuuri = objektiTekstuurit.get(tiedostonNimi);
                    }
                    else {
                        objektiTekstuurit.put(tiedostonNimi, new Tekstuuri("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/" + tiedostonNimi));
                        this.tekstuuri = objektiTekstuurit.get(tiedostonNimi);
                    }
                    this.katsomisTeksti = ominaisuus.substring(5, ominaisuus.length()-4);
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
        }
        päivitäLisäOminaisuudet();
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

    public void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet != null) {
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kuva="));
            this.lisäOminaisuudet.add("kuva="+ tiedostonNimi);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("katsottava="));
            if (katsottava) this.lisäOminaisuudet.add("katsottava=" + (katsottava ? "kyllä" : "ei"));
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("dialogi="));
            if (katsottava) this.lisäOminaisuudet.add("dialogi=" + katsomisDialogi);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("tavoite="));
            super.asetaTiedot();
        }
    }
}
