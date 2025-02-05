package keimo.Maastot;

import keimo.Ruudut.PeliRuutu;

import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Tile extends Maasto {

    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kuva=" + tiedostonNimi;
        this.lisäOminaisuudet[1] = "kääntö=" + kääntöAsteet;
        this.lisäOminaisuudet[2] = "x-peilaus=" + (xPeilaus ? "kyllä" : "ei");
        this.lisäOminaisuudet[3] = "y-peilaus=" + (yPeilaus ? "kyllä" : "ei");
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

    public Tile(int sijX, int sijY, String[] ominaisuusLista) {
        super.nimi = "Tile";
        super.estääLiikkumisen = false;
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.lisäOminaisuuksia = true;

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

        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kuva=" + tiedostonNimi;
        this.lisäOminaisuudet[1] = "kääntö=" + kääntöAsteet;
        this.lisäOminaisuudet[2] = "x-peilaus=" + (xPeilaus ? "kyllä" : "ei");
        this.lisäOminaisuudet[3] = "y-peilaus=" + (yPeilaus ? "kyllä" : "ei");

        super.kuvake = new ImageIcon("tiedostot/kuvat/maasto/" + tiedostonNimi);
        super.hitbox = new Rectangle(PeliRuutu.esineenKokoPx, PeliRuutu.esineenKokoPx);
        super.hitbox.setLocation(sijX * PeliRuutu.esineenKokoPx, sijY * PeliRuutu.esineenKokoPx);
        super.asetaTiedot();
        //luoSkaalattuKuvake();
    }
}
