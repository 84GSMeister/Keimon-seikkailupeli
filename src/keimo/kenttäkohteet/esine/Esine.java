package keimo.kenttäkohteet.esine;

import keimo.kenttäkohteet.KenttäKohde;

import java.util.Random;
import java.util.ArrayList;

public abstract class Esine extends KenttäKohde {

    Random r = new Random();

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
        if (esineA instanceof Kaasupullo) {
            if (esineB instanceof Kaasusytytin) {
                String[] toimivuus = {"toimivuus=toimiva"};
                return new Kaasusytytin(false, 0, 0, toimivuus);
            }
        }
        else if (esineA instanceof Kaasusytytin) {
            if (esineB instanceof Kaasupullo) {
                String[] toimivuus = {"toimivuus=toimiva"};
                return new Kaasusytytin(false, 0, 0, toimivuus);
            }
        }
        return null;
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

    public static Esine luoEsine(String esineenNimi, String[] ominaisuusLista) {
        switch (esineenNimi) {
            case "Avain":
                return new Avain(false, 0, 0);
            case "Hiili":
                return new Hiili(false, 0, 0);
            case "Huume":
                return new Huume(false, 0, 0);
            case "Jallupullo":
                return new Jallupullo(false, 0, 0);
            case "Kaasupullo":
                return new Kaasupullo(false, 0, 0);
            case "Kaasusytytin":
                return new Kaasusytytin(false, 0, 0, ominaisuusLista);
            case "Kilpi":
                return new Kilpi(false, 0, 0);
            case "Kuparilager":
                return new Kuparilager(false, 0, 0);
            case "Makkara":
                return new Makkara(false, 0, 0);
            case "Paperi":
                return new Paperi(false, 0, 0);
            case "Pesäpallomaila":
                return new Pesäpallomaila(false, 0, 0);
            case "Seteli":
                return new Seteli(false, 0, 0);
            case "Pontikka-ainekset":
                return new Ponuainekset(false, 0, 0);
            case "Suklaalevy":
                return new Suklaalevy(false, 0, 0);
            case "Vesiämpäri":
                return new Vesiämpäri(false, 0, 0);
            case null, default:
                return null;
        }
    }

    public Esine(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }
}
