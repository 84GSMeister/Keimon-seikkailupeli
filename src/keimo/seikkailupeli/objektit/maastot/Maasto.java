package keimo.seikkailupeli.objektit.maastot;

import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.PeliObjekti;
import keimo.seikkailupeli.objektit.Suunnallinen.Suunta;

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

    public Maasto(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kuva=")) {
                    this.tiedostonNimi = ominaisuus.substring(5);
                    this.katsomisTeksti = ominaisuus.substring(5, ominaisuus.length()-4);
                    this.tekstuurinNimi = katsomisTeksti;
                }
            }
        }
        päivitäLisäOminaisuudet();
    }

    public void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet != null) {
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kuva="));
            this.lisäOminaisuudet.add("kuva="+ tiedostonNimi);
        }
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
        // Säilytetään legacy-nimet toistaiseksi, jotta vanhoja kst-tiedostoja voidaan lukea.
        switch (maastonNimi) {
            case "Tile", "IsoLaatta", "Laatta", "EsteTile", "Yksisuuntainen Tile": return new Tile(sijX, sijY, ominaisuusLista);
            case null, default: return null;
        }
    }

    public static Maasto luoRandomMaasto(int sijX, int sijY) {
        Random r = new Random();
        Object kuvaTiedosto = Assets.annaTileTekstuurit().keySet().toArray()[r.nextInt(Assets.annaTileTekstuurit().size())];
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
}
