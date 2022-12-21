public class Warp extends KenttäKohde{
    
    int kohdeHuone;
    int kohdeRuutuX;
    int kohdeRuutuY;
    Suunta suunta;

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
    Suunta annaSuunta() {
        return suunta;
    }

    Warp(int luontiKohdeHuone, int luontiKohdeRuutuX, int luontiKohdeRuutuY, Suunta luontiSuunta) {
        this.nimi = "Warp";
        this.katsomisTeksti = "Paina välilyöntiä kulkeaksesi oviruudusta!";
        this.kohdeHuone = luontiKohdeHuone;
        this.kohdeRuutuX = luontiKohdeRuutuX;
        this.kohdeRuutuY = luontiKohdeRuutuY;
        this.suunta = luontiSuunta;
    }
    Warp() {
        
    }
}
