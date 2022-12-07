import java.util.ArrayList;

import javax.swing.ImageIcon;

public class PikkuVihu extends Vihollinen{

    void kukista() {
        this.kukistettu = true;
        this.kuvake = new ImageIcon("tiedostot/kuvat/pikkuvihu_suutari.png");
    }

    String katso() {
        if (!onkoKukistettu()) {
            return "Voi ei! Se on ilkeä vihollinen";
        }
        else {
            return "Vihollinen on kukistettu ja nyt täysin harmiton.";
        }
    }

    PikkuVihu() {
        this.vahinko = 1;
        this.hp = 3;
        this.nimi = "Pikkuvihu";
        this.kuvake = new ImageIcon("tiedostot/kuvat/pikkuvihu.png");
        this.tehoavatAseet.add("Vesiämpäri");
    }

}
