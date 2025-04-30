package keimo.kenttäkohteet.kiintopiste;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.io.File;

public class KauppaHylly extends Säiliö {

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
                super.tiedostonNimi = "kauppahylly_" + esineenNimi + ".png";
                super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
            }
            else {
                super.tiedostonNimi = "kauppahylly_" + esineenNimi + ".png";
                super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kauppahylly_" + "kuvavirhe" + ".png");
                
            }
            if (luoSisältö(esineenNimi, null) != null) {
                super.katsomisTeksti = "Hyllystä saa " + luoSisältö(esineenNimi, null).annaNimiSijamuodossa("partitiivi");
            }
            else {
                super.katsomisTeksti = "tyhjä hylly";
            }
        }
    }

    public KauppaHylly(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Kauppahylly";
        super.tiedostonNimi = "kauppahylly.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
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
            this.sisältö = luoSisältö(esineenNimi, ominaisuusLista);
            File file = new File("tiedostot/kuvat/kenttäkohteet/kauppahylly_" + esineenNimi + ".png");
            if (file.isFile()) {
                super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kauppahylly_" + esineenNimi + ".png");
            }
            else {
                super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/kauppahylly_" + "kuvavirhe" + ".png");
            }
            päivitäLisäOminaisuudet();
            if (luoSisältö(esineenNimi, ominaisuusLista) != null) {
                super.katsomisTeksti = "Hyllystä saa " + luoSisältö(esineenNimi, ominaisuusLista).annaNimiSijamuodossa("partitiivi");
            }
        }
        else {
            this.lisäOminaisuuksia = false;
        }
        
        super.asetaTiedot();
    }
}
