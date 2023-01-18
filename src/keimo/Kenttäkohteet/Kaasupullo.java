package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Kaasupullo extends Esine {

    public String katso(){
        return katsomisTeksti;
    }

    public String käytä(){
        return käyttöTeksti;
    }
    
    public Kaasupullo(){
        this.nimi = "Kaasupullo";
        this.yhdistettävä = true;
        this.kelvollisetYhdistettävät.add("Tyhjä kaasusytytin");
        this.kuvake = new ImageIcon("tiedostot/kuvat/kaasupullo.png");
        this.katsomisTeksti = "Tätä tarvitaan varmaankin kaasusytyttimen kanssa.";
        this.käyttöTeksti ="Päästit kaasua vapaaksi, mitään ei tapahtunut.";
        super.asetaTiedot();
    }

    public Kaasupullo(int sijX, int sijY){
        this.määritettySijainti = true;
        this.sijX = sijX;
        this.sijY = sijY;
        this.nimi = "Kaasupullo";
        this.yhdistettävä = true;
        this.kelvollisetYhdistettävät.add("Tyhjä kaasusytytin");
        this.kuvake = new ImageIcon("tiedostot/kuvat/kaasupullo.png");
        this.katsomisTeksti = "Tätä tarvitaan varmaankin kaasusytyttimen kanssa.";
        this.käyttöTeksti ="Päästit kaasua vapaaksi, mitään ei tapahtunut.";
        super.asetaTiedot();
    }
}
