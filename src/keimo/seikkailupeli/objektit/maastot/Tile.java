package keimo.seikkailupeli.objektit.maastot;

import keimo.keimoengine.collision.Neliö;

import java.util.ArrayList;

public class Tile extends Maasto {

    private int leveys = 1;
    private int korkeus = 1;

    public Tile(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Tile";
        super.estääLiikkumisen = false;
        
        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("leveys=")) {
                    try {
                        this.leveys = Integer.parseInt(ominaisuus.substring(7));
                    }
                    catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                else if (ominaisuus.startsWith("korkeus=")) {
                    try {
                        this.korkeus = Integer.parseInt(ominaisuus.substring(8));
                    }
                    catch (NumberFormatException e) {
                        e.printStackTrace();
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
            this.tekstuurinNimi = katsomisTeksti;
        }

        päivitäLisäOminaisuudet();

        super.hitbox = new Neliö(leveys * 64, korkeus * 64);
        super.hitbox.setLocation(sijX * 64, sijY * 64);
        super.asetaTiedot();
    }

    public int annaLeveys() {
        return leveys;
    }

    public int annaKorkeus() {
        return korkeus;
    }

    public void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet != null) {
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("leveys="));
            if (leveys > 1) this.lisäOminaisuudet.add("leveys=" + leveys);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("korkeus="));
            if (korkeus > 1) this.lisäOminaisuudet.add("korkeus=" + korkeus);
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
}
