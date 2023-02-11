package keimo.Maastot;

import javax.swing.ImageIcon;

public class EsteTile extends Maasto{

    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[1];
        this.lisäOminaisuudet[0] = "kuva=" + tiedostonNimi;
        //this.lisäOminaisuudet[1] = "este=" + (this.estääLiikkumisen ? "kyllä" : "ei");
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
            }
            else if (ominaisuus.startsWith("este=")) {
                if (ominaisuus.substring(5) == "kyllä") {
                    super.estääLiikkumisen = true;
                }
            }
        }

        this.lisäOminaisuudet = new String[1];
        this.lisäOminaisuudet[0] = "kuva=" + tiedostonNimi;
        //this.lisäOminaisuudet[1] = "este=" + (this.estääLiikkumisen ? "kyllä" : "ei");
        super.kuvake = new ImageIcon("tiedostot/kuvat/maasto/" + tiedostonNimi);

        super.asetaTiedot();
    }
}

