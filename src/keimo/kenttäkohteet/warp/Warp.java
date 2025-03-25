package keimo.kenttäkohteet.warp;

import keimo.kenttäkohteet.KenttäKohde;

public abstract class Warp extends KenttäKohde {
    
    protected int kohdeHuone;
    protected int kohdeRuutuX;
    protected int kohdeRuutuY;
    protected Suunta suunta;

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
        this.suunta = suunta;
    }

    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kohdehuone=" + kohdeHuone;
        this.lisäOminaisuudet[1] = "kohderuutuX=" + kohdeRuutuX;
        this.lisäOminaisuudet[2] = "kohderuutuY=" + kohdeRuutuY;
        this.lisäOminaisuudet[3] = "suunta=" + annaSuunta();
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

    public Warp(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.katsomisTeksti = "Paina nuolen suuntaista nuolinäppäintä tai wasd-näppäintä kulkeaksesi oviruudusta!";
    }
}
