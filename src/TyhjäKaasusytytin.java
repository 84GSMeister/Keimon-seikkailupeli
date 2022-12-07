import javax.swing.ImageIcon;

public class TyhjäKaasusytytin extends Esine {

    String katso(){
        System.out.println("Tästä puuttuu kaasupullo. Löytyisiköhän sellainen kentältä?");
        return "Tästä puuttuu kaasupullo. Löytyisiköhän sellainen kentältä?";
    }

    String käytä(){
        System.out.println("Kaasusytytin ei toimi ilman kaasupulloa.");
        return "Kaasusytytin ei toimi ilman kaasupulloa.";
    }
    
    TyhjäKaasusytytin(){
        this.nimi = "Tyhjä kaasusytytin";
        this.yhdistettävä = true;
        this.kelvollisetYhdistettävät.add("Kaasupullo");
        this.kuvake = new ImageIcon("tiedostot/kuvat/tyhjäkaasusytytin.png");
    }
}
