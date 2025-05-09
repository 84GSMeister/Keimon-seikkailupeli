package keimo.Maastot;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Tile extends Maasto {

    public void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet != null) {
            super.lisäOminaisuuksia = true;
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kuva="));
            this.lisäOminaisuudet.add("kuva="+ tiedostonNimi);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kääntö="));
            this.lisäOminaisuudet.add("kääntö=" + kääntöAsteet);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("x-peilaus="));
            this.lisäOminaisuudet.add("x-peilaus=" + (xPeilaus ? "kyllä" : "ei"));
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("y-peilaus="));
            this.lisäOminaisuudet.add("y-peilaus=" + (yPeilaus ? "kyllä" : "ei"));
        }
    }

    public void päivitäEsteenSuunta() {
        switch (kääntöAsteet) {
            case 0:
                super.estääLiikkumisenVasen = true;
                super.estääLiikkumisenOikea = true;
                super.estääLiikkumisenAlas = true;
                super.estääLiikkumisenYlös = false;
            break;
            case 90:
                super.estääLiikkumisenVasen = true;
                super.estääLiikkumisenOikea = false;
                super.estääLiikkumisenAlas = true;
                super.estääLiikkumisenYlös = true;
            break;
            case 180:
                super.estääLiikkumisenVasen = true;
                super.estääLiikkumisenOikea = true;
                super.estääLiikkumisenAlas = false;
                super.estääLiikkumisenYlös = true;
            break;
            case 270:
                super.estääLiikkumisenVasen = false;
                super.estääLiikkumisenOikea = true;
                super.estääLiikkumisenAlas = true;
                super.estääLiikkumisenYlös = true;
            break;
            default:
            break;
        }
        super.asetaTiedot();
    }

    public Tile(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super.nimi = "Tile";
        super.estääLiikkumisen = false;
        super.sijX = sijX;
        super.sijY = sijY;
        
        if (ominaisuusLista != null) {
            super.lisäOminaisuudet = new ArrayList<>();
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kuva=")) {
                    this.tiedostonNimi = ominaisuus.substring(5);
                    this.katsomisTeksti = ominaisuus.substring(5, ominaisuus.length()-4);
                    this.tekstuuri = katsomisTeksti;
                }
                else if (ominaisuus.startsWith("kääntö=")) {
                    try {
                        this.kääntöAsteet = Integer.parseInt(ominaisuus.substring(7));
                    }
                    catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                else if (ominaisuus.startsWith("x-peilaus=")) {
                    if (ominaisuus.substring(10).startsWith("kyllä")) {
                        super.xPeilaus = true;
                    }
                    else {
                        super.xPeilaus = false;
                    }
                }
                else if (ominaisuus.startsWith("y-peilaus=")) {
                    if (ominaisuus.substring(10).startsWith("kyllä")) {
                        super.yPeilaus = true;
                    }
                    else {
                        super.yPeilaus = false;
                    }
                }   
            }

            if (katsomisTeksti.endsWith("_e")) {
                this.estääLiikkumisen = true;
            }
            if (katsomisTeksti.endsWith("_y")) {
                päivitäEsteenSuunta();
            }
        }
        else {
            this.tiedostonNimi = "virhetekstuuri.png";
            this.katsomisTeksti = "virheellinen tile";
            this.tekstuuri = katsomisTeksti;
        }

        päivitäLisäOminaisuudet();

        super.hitbox = new Rectangle(64, 64);
        super.hitbox.setLocation(sijX * 64, sijY * 64);
        super.asetaTiedot();
    }
}
