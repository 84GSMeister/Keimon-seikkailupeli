import javax.swing.ImageIcon;

public class ReunaWarppi extends Warp {
    
    public ReunaWarppi(int luontiKohdeHuone, int luontiKohdeRuutuX, int luontiKohdeRuutuY, Suunta luontiSuunta) {
        
        this.nimi = "Oviruutu";
        this.katsomisTeksti = "Paina Spacea tai nuolen suuntaista nuolinäppäintä kulkeaksesi oviruudusta!";
        this.kohdeHuone = luontiKohdeHuone;
        this.kohdeRuutuX = luontiKohdeRuutuX;
        this.kohdeRuutuY = luontiKohdeRuutuY;
        this.suunta = luontiSuunta;

        switch (suunta) {
            case YLÖS:
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_ylös.png");
                break;
            case ALAS:
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_alas.png");
                break;
            case VASEN:
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_vasen.png");
                break;
            case OIKEA:
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_oikea.png");
                break;
            default:
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_oikea.png");
                break;
        }
        
    }
}
