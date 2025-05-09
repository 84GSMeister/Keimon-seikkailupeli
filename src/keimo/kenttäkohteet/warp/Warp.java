package keimo.kenttäkohteet.warp;

import keimo.kenttäkohteet.KenttäKohde;

import java.util.ArrayList;

public abstract class Warp extends KenttäKohde {

    protected int kohdeHuone;
    protected int kohdeRuutuX;
    protected int kohdeRuutuY;
    protected Suunta suunta;

    public Warp(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY);
        super.katsomisTeksti = "Paina nuolen suuntaista nuolinäppäintä tai wasd-näppäintä kulkeaksesi oviruudusta!";
        if (ominaisuusLista != null) {
            this.lisäOminaisuudet = new ArrayList<>();
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kohdehuone=")) {
                    this.kohdeHuone = Integer.parseInt("" + ominaisuus.substring(ominaisuus.indexOf("=") +1));
                }
                else if (ominaisuus.startsWith("kohderuutuX=")) {
                    this.kohdeRuutuX = Integer.parseInt("" + ominaisuus.substring(ominaisuus.indexOf("=") +1));
                }
                else if (ominaisuus.startsWith("kohderuutuY=")) {
                    this.kohdeRuutuY = Integer.parseInt("" + ominaisuus.substring(ominaisuus.indexOf("=") +1));
                }
                else if (ominaisuus.startsWith("suunta=")) {
                    String suuntaString = ominaisuus.substring(7);
                    switch (suuntaString) {
                        case "vasen", "VASEN", "Vasen": this.suunta = Suunta.VASEN; break;
                        case "oikea", "OIKEA", "Oikea": this.suunta = Suunta.OIKEA; break;
                        case "ylös", "YLÖS", "Ylös": this.suunta = Suunta.YLÖS; break;
                        case "alas", "ALAS", "Alas": this.suunta = Suunta.ALAS; break;
                        default: this.suunta = Suunta.YLÖS; break;
                    }
                }
            }
            asetaSuunta(suunta);
            päivitäLisäOminaisuudet();
        }
    }

    public int annaKohdeHuone() {
        return kohdeHuone;
    }
    public int annaKohdeRuutuX() {
        return kohdeRuutuX;
    }
    public int annaKohdeRuutuY() {
        return kohdeRuutuY;
    }
    public Suunta annaSuunta() {
        return suunta;
    }

    public void asetaKohdeHuone(int huoneenId) {
        this.kohdeHuone = huoneenId;
    }
    public void asetaKohdeRuudut(int x, int y) {
        this.kohdeRuutuX = x;
        this.kohdeRuutuY = y;
    }
    public void asetaSuunta(Suunta suunta) {
        switch (suunta) {
            case YLÖS: this.suunta = Suunta.YLÖS; break;
            case ALAS: this.suunta = Suunta.ALAS; break;
            case VASEN: this.suunta = Suunta.VASEN; break;
            case OIKEA: this.suunta = Suunta.OIKEA; break;
            case null, default: this.suunta = Suunta.YLÖS; break;
        }
    }

    public void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet != null) {
            this.lisäOminaisuuksia = true;
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kohdehuone="));
            this.lisäOminaisuudet.add("kohdehuone=" + kohdeHuone);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kohderuutuX="));
            this.lisäOminaisuudet.add("kohderuutuX=" + kohdeRuutuX);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("kohderuutuY="));
            this.lisäOminaisuudet.add("kohderuutuY=" + kohdeRuutuY);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("suunta="));
            this.lisäOminaisuudet.add("suunta=" + annaSuunta());
        }
    }

    public void ennenWarppia() {

    }

    public void warpinJälkeen() {

    }

    @Override
    public int annaKääntöAsteet() {
        switch (this.annaSuunta()) {
            case YLÖS: return 0;
            case OIKEA: return 90;
            case ALAS: return 180;
            case VASEN: return 270;
            default: return 0;
        }
    }
}
