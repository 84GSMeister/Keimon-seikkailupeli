package keimo.Maastot;

import java.awt.Image;

import javax.swing.ImageIcon;

public class EsteTile extends Maasto{

    @Override
    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kuva=" + tiedostonNimi;
        this.lisäOminaisuudet[1] = "kääntö=" + kääntöAsteet;
        this.lisäOminaisuudet[2] = "x-peilaus=" + (xPeilaus ? "kyllä" : "ei");
        this.lisäOminaisuudet[3] = "y-peilaus=" + (yPeilaus ? "kyllä" : "ei");
    }

    @Override
    protected void luoSkaalattuKuvake() {
        ImageIcon kenttäkohteenSkaalattuKuvake = (ImageIcon)super.kuvake;
        Image kuvake64 = kenttäkohteenSkaalattuKuvake.getImage();
        Image kuvake96 = kuvake64.getScaledInstance(96, 96, Image.SCALE_SMOOTH);
        kenttäkohteenSkaalattuKuvake = new ImageIcon(kuvake96);
        super.skaalattuKuvake = kenttäkohteenSkaalattuKuvake; 
    }

    public EsteTile(int sijX, int sijY, String[] ominaisuusLista) {
        super.nimi = "EsteTile";
        super.estääLiikkumisen = true;
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.lisäOminaisuuksia = true;

        for (String ominaisuus : ominaisuusLista) {
            if (ominaisuus.startsWith("kuva=")) {
                this.tiedostonNimi = ominaisuus.substring(5);
                this.katsomisTeksti = ominaisuus.substring(5, ominaisuus.length()-4);
            }
            else if (ominaisuus.startsWith("kääntö=")) {
                try {
                    this.kääntöAsteet = Integer.parseInt(ominaisuus.substring(7, ominaisuus.length()));
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

        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kuva=" + tiedostonNimi;
        this.lisäOminaisuudet[1] = "kääntö=" + kääntöAsteet;
        this.lisäOminaisuudet[2] = "x-peilaus=" + (xPeilaus ? "kyllä" : "ei");
        this.lisäOminaisuudet[3] = "y-peilaus=" + (yPeilaus ? "kyllä" : "ei");
        super.kuvake = new ImageIcon("tiedostot/kuvat/maasto/" + tiedostonNimi);
        super.asetaTiedot();
        luoSkaalattuKuvake();
    }
}

