package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

public class Kaasupullo extends Esine {

    public String käytä(){
        return käyttöTeksti;
    }

    public Kaasupullo(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        this.nimi = "Kaasupullo";
        this.yhdistettävä = true;
        this.kelvollisetYhdistettävät.add("Kaasusytytin");
        this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kaasupullo.png");
        this.katsomisTeksti = "Tätä tarvitaan varmaankin kaasusytyttimen kanssa.";
        this.käyttöTeksti ="Päästit kaasua vapaaksi, mitään ei tapahtunut.";
        super.asetaTiedot();
    }
}
