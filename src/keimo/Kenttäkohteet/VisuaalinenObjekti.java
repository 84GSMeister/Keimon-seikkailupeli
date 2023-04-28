package keimo.Kenttäkohteet;

import javax.swing.ImageIcon;

public class VisuaalinenObjekti extends KenttäKohde{
    
    private boolean este = false;
    public boolean onkoEste() {
        return este;
    }

    public VisuaalinenObjekti(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY);
        this.nimi = "Koriste-esine";
        String kuva = "";
        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kuva=")) {
                    kuva = ominaisuus.substring(5);
                }
            }
            if (kuva.endsWith("_e.png")) {
                this.este = true;
            }
            switch (kuva) {
                case "":
                    this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/vakiokuva.png");
                break;
                default:
                    this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/" + kuva);
                break;
            }
            this.lisäOminaisuuksia = true;
            this.lisäOminaisuudet = new String[1];
            this.lisäOminaisuudet[0] = "kuva=" + kuva;
        }
        else {
            this.lisäOminaisuuksia = false;
        }
        super.asetaTiedot();
    }
}
