import javax.swing.ImageIcon;

public class Kaasupullo extends Esine {

    String katso(){
        System.out.println("Tätä tarvitaan varmaankin kaasusytyttimen kanssa.");
        return "Tätä tarvitaan varmaankin kaasusytyttimen kanssa.";
    }

    String käytä(){
        System.out.println("Päästit kaasua vapaaksi, mitään ei tapahtunut.");
        return "Päästit kaasua vapaaksi, mitään ei tapahtunut.";
    }
    
    Kaasupullo(){
        this.nimi = "Kaasupullo";
        this.yhdistettävä = true;
        this.kelvollisetYhdistettävät.add("Tyhjä kaasusytytin");
        this.kuvake = new ImageIcon("tiedostot/kuvat/kaasupullo.png");
    }
}
