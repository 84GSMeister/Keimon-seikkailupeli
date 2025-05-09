package keimo.kenttäkohteet.esine;

import keimo.kenttäkohteet.KenttäKohde;

import java.util.Random;
import java.util.ArrayList;

public abstract class Esine extends KenttäKohde {

    Random r = new Random();

    public Esine(int sijX, int sijY) {
        super(sijX, sijY);
    }

    protected boolean poista = false;
    public boolean poistoon(){
        return poista;
    }

    protected boolean kenttäkäyttö = false;
    public boolean onkoKenttäkäyttöön() {
        return kenttäkäyttö;
    }

    protected boolean käyttö = false;
    public boolean onkoKäyttö() {
        return käyttö;
    }

    protected boolean yhdistettävä = false;
    public boolean onkoYhdistettävä() {
        return yhdistettävä;
    }

    protected double hinta = Double.POSITIVE_INFINITY;
    public double annaHinta() {
        return hinta;
    }

    /**
     * Yhdistää kaksi esinettä: poistaa esineet ja antaa tilalle yhdistelmän tuloksen.
     * Aseta vanhoille tavaraluettelon paikoille null ja luo uusi esine
     * @.pre {
     * @param esineA instanceof Esine &&
     * @param esineB instanceof Esine
     * }
     * @.post p.esineet[a] instanceof Esine && p.esineet[b] == null
     */
    public static Esine yhdistä2Esinettä(Esine esineA, Esine esineB) {
        if ((esineA instanceof Kaasupullo && esineB instanceof Kaasusytytin) || (esineA instanceof Kaasusytytin && esineB instanceof Kaasupullo)) {
            ArrayList<String> toimivuus = new ArrayList<>();
            toimivuus.add("toimivuus=toimiva");
            return new Kaasusytytin(0, 0, toimivuus);
        }
        else if ((esineA instanceof Jallupullo && esineB instanceof Paskanmarjat) || (esineA instanceof Paskanmarjat && esineB instanceof Jallupullo)) {
            return new Paskanmarjabooli( 0, 0);
        }
        else return null;
    }

    public ArrayList<String> kelvollisetYhdistettävät = new ArrayList<String>();
    public ArrayList<String> sopiiKäytettäväksi = new ArrayList<String>();

    String käyttöTeksti = "Mitään ei tapahtunut.";
    public String käytä() {
        return käyttöTeksti;
    }

    public String annaNimi() {
        return nimi;
    }

    public static Esine luoEsine(String esineenNimi, ArrayList<String> ominaisuusLista) {
        switch (esineenNimi) {
            case "Avain":
                return new Avain(0, 0);
            case "Hiili":
                return new Hiili(0, 0);
            case "Huume":
                return new Huume(0, 0);
            case "Jallupullo":
                return new Jallupullo(0, 0);
            case "Kaasupullo":
                return new Kaasupullo(0, 0);
            case "Kaasusytytin":
                return new Kaasusytytin( 0, 0, ominaisuusLista);
            case "Kilpi":
                return new Kilpi(0, 0);
            case "Kuparilager":
                return new Kuparilager(0, 0);
            case "Makkara":
                return new Makkara(0, 0);
            case "Paperi":
                return new Paperi(0, 0);
            case "Pesäpallomaila":
                return new Pesäpallomaila(0, 0);
            case "Pontikka-ainekset":
                return new Ponuainekset(0, 0);
            case "Suklaalevy":
                return new Suklaalevy(0, 0);
            case "Vesiämpäri":
                return new Vesiämpäri(0, 0);
            case null, default:
                return null;
        }
    }
}
