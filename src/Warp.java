public class Warp extends KenttäKohde{
    
    int kohdeHuone;
    int kohdeRuutuX;
    int kohdeRuutuY;

    enum Suunta {
        YLÖS,
        ALAS,
        VASEN,
        OIKEA;
    }

    int annaKohdeHuone() {
        return kohdeHuone;
    }
    int annaKohdeRuutuX() {
        return kohdeRuutuX;
    }
    int annaKohdeRuutuY() {
        return kohdeRuutuY;
    }

    Warp(int luontiKohdeHuone, int luontiKohdeRuutuX, int luontiKohdeRuutuY, Suunta suunta) {
        this.nimi = "Warp";
        this.katsomisTeksti = "Paina Space kulkeaksesi oviruudusta";
        this.kohdeHuone = luontiKohdeHuone;
        this.kohdeRuutuX = luontiKohdeRuutuX;
        this.kohdeRuutuY = luontiKohdeRuutuY;
    }
    Warp() {
        
    }
}
