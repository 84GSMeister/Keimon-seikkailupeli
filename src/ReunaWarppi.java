import javax.swing.ImageIcon;

public class ReunaWarppi extends Warp {
    
    public ReunaWarppi(int sijX, int sijY, int luontiKohdeHuone, int luontiKohdeRuutuX, int luontiKohdeRuutuY, Suunta luontiSuunta) {
        
        this.nimi = "Oviruutu";
        this.katsomisTeksti = "Paina Spacea tai nuolen suuntaista nuolinäppäintä kulkeaksesi oviruudusta!";
        this.kohdeHuone = luontiKohdeHuone;
        this.kohdeRuutuX = luontiKohdeRuutuX;
        this.kohdeRuutuY = luontiKohdeRuutuY;
        this.suunta = luontiSuunta;
        this.sijX = sijX;
        this.sijY = sijY;
        this.määritettySijainti = true;

        switch (suunta) {
            case YLÖS:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_ylös.png"), KenttäKohde.Suunta.YLÖS);
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_ylös.png"); 
                break;
            case ALAS:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_alas.png"), KenttäKohde.Suunta.ALAS);
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_alas.png"); 
                break;
            case VASEN:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_vasen.png"), KenttäKohde.Suunta.VASEN);
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_vasen.png");    
                break;
            case OIKEA:
                //asetaKuvake(new ImageIcon("tiedostot/kuvat/nuoli_vasen.png"), KenttäKohde.Suunta.OIKEA);
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_oikea.png");     
                break;
            default:
                this.kuvake = new ImageIcon("tiedostot/kuvat/nuoli_oikea.png");
                break;
        }
    }
}
