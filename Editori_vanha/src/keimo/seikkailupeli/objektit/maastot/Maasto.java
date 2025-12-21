package keimo.seikkailupeli.objektit.maastot;

import keimo.editori.HuoneEditoriIkkuna;
import keimo.seikkailupeli.objektit.PeliObjekti;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public abstract class Maasto extends PeliObjekti {
    
    protected String tekstuurinNimi;
    protected boolean estääLiikkumisen = false;
    protected boolean estääLiikkumisenVasen = false;
    protected boolean estääLiikkumisenOikea = false;
    protected boolean estääLiikkumisenAlas = false;
    protected boolean estääLiikkumisenYlös = false;

    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kuva="));
        this.lisäOminaisuudet.add("kuva="+ tiedostonNimi);
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kääntö="));
        this.lisäOminaisuudet.add("kääntö=" + kääntöAsteet);
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("x-peilaus="));
        this.lisäOminaisuudet.add("x-peilaus=" + (xPeilaus ? "kyllä" : "ei"));
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("y-peilaus="));
        this.lisäOminaisuudet.add("y-peilaus=" + (yPeilaus ? "kyllä" : "ei"));
    }

    public boolean estääköLiikkumisen(Suunta suunta) {
        if (this.estääLiikkumisen) {
            return true;
        }
        else {
            switch (suunta) {
                case VASEN: return estääLiikkumisenVasen;
                case OIKEA: return estääLiikkumisenOikea;
                case ALAS: return estääLiikkumisenAlas;
                case YLÖS: return estääLiikkumisenYlös;
                case null, default: return false;
            }
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        return katsomisTeksti;
    }

    public String annaTekstuurinNimi() {
        return tekstuurinNimi;
    }

    public String annaKuvanTiedostoNimi() {
        return tiedostonNimi;
    }

    public static Maasto luoMaastoTiedoilla(String maastonNimi, int sijX, int sijY, ArrayList<String> ominaisuusLista) {

        Maasto luotavaMaasto;

        switch (maastonNimi) {

            case "Tile":
                luotavaMaasto = new Tile(sijX, sijY, ominaisuusLista);
                break;

            case "IsoLaatta":
                luotavaMaasto = new IsoLaatta(sijX, sijY, ominaisuusLista);
                break;

            case "EsteTile":
                luotavaMaasto = new Tile(sijX, sijY, ominaisuusLista);
                break;
            
            case "Yksisuuntainen Tile":
                luotavaMaasto = new Tile(sijX, sijY, ominaisuusLista);
                break;

            default:
                luotavaMaasto = null;
                break;
        }

        return luotavaMaasto;
    }

    public static Maasto luoRandomMaasto(int sijX, int sijY) {
        Random r = new Random();
        Object[] lista = HuoneEditoriIkkuna.listaaKuvat("tiedostot/kuvat/maasto").toArray();
        Object kuvaTiedosto = lista[r.nextInt(lista.length)];
        String[] ominaisuusListaArray = {"kuva=" + kuvaTiedosto,"kääntö=0","x-peilaus=ei","y-peilaus=ei"};
        ArrayList<String> ominaisuusLista = new ArrayList<>();
        for (String s : ominaisuusListaArray) {
            ominaisuusLista.add(s);
        }
        return luoMaastoTiedoilla("Tile", sijX, sijY, ominaisuusLista);
    }

    String tiedot = "";
    void asetaTiedot() {
        tiedot = "";
        tiedot += "Nimi: " + this.annaNimi() + "\n";

        List<Suunta> esteSuunnat = new ArrayList<>();
        if (this.estääköLiikkumisen(Suunta.VASEN)) esteSuunnat.add(Suunta.VASEN);
        if (this.estääköLiikkumisen(Suunta.OIKEA)) esteSuunnat.add(Suunta.OIKEA);
        if (this.estääköLiikkumisen(Suunta.YLÖS)) esteSuunnat.add(Suunta.YLÖS);
        if (this.estääköLiikkumisen(Suunta.ALAS)) esteSuunnat.add(Suunta.ALAS);
        tiedot += "Estää liikkumisen: " + esteSuunnat.toString() + "\n";

        tiedot += "Kuva: " + this.tiedostonNimi;
    }
    
    public String annaTiedot() {
        return tiedot;
    }

    protected void luoSkaalattuKuvake() {
         
    }
}
