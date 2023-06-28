package keimo.Kenttäkohteet;

import java.io.File;

import javax.swing.ImageIcon;

import keimo.Pelaaja;
import keimo.Utility.KäännettäväKuvake;

public class KauppaHylly extends Kiintopiste {
    
    protected Esine sisältö;

    @Override
    public String kokeileEsinettä(Esine e) {
        return Pelaaja.lisääOstosKoriin(this.sisältö);
    }

    protected Esine luoSisältö(String esineenNimi) {
        switch (esineenNimi) {
            case "Avain":
                return new Avain(true, 0, 0);
            case "Hiili":
                return new Hiili(true, 0, 0);
            case "Huume":
                return new Huume(true, 0, 0);
            case "Kaasupullo":
                return new Kaasupullo(true, 0, 0);
            case "Kaasusytytin":
                String[] ksOminaisuusLista = {"toimivuus=toimiva"};
                return new Kaasusytytin(true, 0, 0, ksOminaisuusLista);
            case "Kilpi":
                return new Kilpi(true, 0, 0);
            case "Kuparilager":
                return new Kuparilager(true, 0, 0);
            case "Makkara":
                return new Makkara(true, 0, 0);
            case "Paperi":
                return new Paperi(true, 0, 0);
            case "Pesäpallomaila":
                return new Pesäpallomaila(true, 0, 0);
            case "Pontikka-ainekset":
                return new Ponuainekset(true, 0, 0);
            case "Seteli":
                return new Seteli(true, 0, 0);
            case "Suklaalevy":
                return new Suklaalevy(true, 0, 0);
            case "Vesiämpäri":
                return new Vesiämpäri(true, 0, 0);
            default:
                return null;
        }
    }

    public void päivitäLisäOminaisuudet() {
        super.lisäOminaisuuksia = true;
        super.lisäOminaisuudet = new String[4];
        super.lisäOminaisuudet[0] = "sisältö=" + this.annaSisältö();
        super.lisäOminaisuudet[1] = "kääntö=" + kääntöAsteet;
        super.lisäOminaisuudet[2] = "x-peilaus=" + (xPeilaus ? "kyllä" : "ei");
        super.lisäOminaisuudet[3] = "y-peilaus=" + (yPeilaus ? "kyllä" : "ei");
        super.asetaTiedot();
    }

    public void päivitäTiedot() {
        if (this.lisäOminaisuudet != null) {
            String esineenNimi = "";
            for (String ominaisuus : this.lisäOminaisuudet) {
                if (ominaisuus.startsWith("sisältö=")) {
                    esineenNimi = ominaisuus.substring(8);
                }
            }
            File file = new File("tiedostot/kuvat/kenttäkohteet/kauppahylly_" + esineenNimi + ".png");
            if (file.isFile()) {
                super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppahylly_" + esineenNimi + ".png");
                super.tiedostonNimi = "kauppahylly_" + esineenNimi + ".png";
            }
            else {
                super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppahylly_" + "kuvavirhe" + ".png");
                super.tiedostonNimi = "kauppahylly_" + esineenNimi + ".png";
            }
            super.katsomisTeksti = "Hyllystä saa" + luoSisältö(esineenNimi).annaNimiSijamuodossa("partitiivi");
        }
    }

    public void asetaSisältö(String esineenNimi) {
        this.sisältö = luoSisältö(esineenNimi);
    }

    public String annaSisältö() {
        if (this.sisältö != null) {
            return sisältö.annaNimi();
        }
        else {
            return "tyhjä";
        }
    }

    public KauppaHylly(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Kauppahylly";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppahylly.png");
        super.tiedostonNimi = "kauppahylly.png";
        super.katsomisTeksti = "Tyhjä hylly";

        if (ominaisuusLista != null) {
            String esineenNimi = "";
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("sisältö=")) {
                    esineenNimi = ominaisuus.substring(8);
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
            this.sisältö = luoSisältö(esineenNimi);
            File file = new File("tiedostot/kuvat/kenttäkohteet/kauppahylly_" + esineenNimi + ".png");
            if (file.isFile()) {
                super.kuvake = new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppahylly_" + esineenNimi + ".png"), this.kääntöAsteet, this.xPeilaus, this.yPeilaus);
            }
            else {
                super.kuvake = new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppahylly_" + "kuvavirhe" + ".png"), this.kääntöAsteet, this.xPeilaus, this.yPeilaus);
            }
            päivitäLisäOminaisuudet();
            if (luoSisältö(esineenNimi) != null) {
                super.katsomisTeksti = "Hyllystä saa" + luoSisältö(esineenNimi).annaNimiSijamuodossa("partitiivi");
            }
        }
        else {
            this.lisäOminaisuuksia = false;
        }
        
        super.asetaTiedot();
    }
}
