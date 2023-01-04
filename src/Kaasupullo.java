import javax.swing.ImageIcon;

public class Kaasupullo extends Esine {

    String katso(){
        return katsomisTeksti;
    }

    String käytä(){
        return käyttöTeksti;
    }
    
    Kaasupullo(){
        this.nimi = "Kaasupullo";
        this.yhdistettävä = true;
        this.kelvollisetYhdistettävät.add("Tyhjä kaasusytytin");
        this.kuvake = new ImageIcon("tiedostot/kuvat/kaasupullo.png");
        this.katsomisTeksti = "Tätä tarvitaan varmaankin kaasusytyttimen kanssa.";
        this.käyttöTeksti ="Päästit kaasua vapaaksi, mitään ei tapahtunut.";
    }

    Kaasupullo(int sijX, int sijY){
        this.määritettySijainti = true;
        this.sijX = sijX;
        this.sijY = sijY;
        this.nimi = "Kaasupullo";
        this.yhdistettävä = true;
        this.kelvollisetYhdistettävät.add("Tyhjä kaasusytytin");
        this.kuvake = new ImageIcon("tiedostot/kuvat/kaasupullo.png");
        this.katsomisTeksti = "Tätä tarvitaan varmaankin kaasusytyttimen kanssa.";
        this.käyttöTeksti ="Päästit kaasua vapaaksi, mitään ei tapahtunut.";
    }
}
