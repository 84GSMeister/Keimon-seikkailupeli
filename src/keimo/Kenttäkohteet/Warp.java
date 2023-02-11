package keimo.Kenttäkohteet;
public class Warp extends KenttäKohde{
    
    int kohdeHuone;
    int kohdeRuutuX;
    int kohdeRuutuY;
    Suunta suunta;

    public enum Suunta {
        YLÖS,
        ALAS,
        VASEN,
        OIKEA;
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
        this.suunta = suunta;
    }

    public Warp(boolean määritettySijainti, int sijX, int sijY, int luontiKohdeHuone, int luontiKohdeRuutuX, int luontiKohdeRuutuY, Suunta luontiSuunta) {
        super(määritettySijainti, sijX, sijY);
        this.nimi = "Warp";
        this.katsomisTeksti = "Paina välilyöntiä kulkeaksesi oviruudusta!";
        this.kohdeHuone = luontiKohdeHuone;
        this.kohdeRuutuX = luontiKohdeRuutuX;
        this.kohdeRuutuY = luontiKohdeRuutuY;
        this.suunta = luontiSuunta;
    }
}
