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

    String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Pikkuvihu";
            case "genetiivi":
                return "Pikkuvihun";
            case "esiivi":
                return "Pikkuvihuna";
            case "partitiivi":
                return "Pikkuvihua";
            case "translatiivi":
                return "Pikkuvihuksi";
            case "inessiivi":
                return "Pikkuvihussa";
            case "elatiivi":
                return "Pikkuvihusta";
            case "illatiivi":
                return "Pikkuvihuun";
            case "adessiivi":
                return "Pikkuvihulla";
            case "ablatiivi":
                return "Pikkuvihulta";
            case "allatiivi":
                return "Pikkuvihulle";
            default:
                return "Pikkuvihu";
        }
    }

    PikkuVihu() {
        super.vahinko = 1 * PelinAsetukset.vaikeusAste;
        super.hp = 3 * PelinAsetukset.vaikeusAste;
        super.nimi = "Pikkuvihu";
        super.kuvake = new ImageIcon("tiedostot/kuvat/pikkuvihu.png");
        super.tehoavatAseet.add("Vesiämpäri");
        super.asetaTiedot();
    }

    PikkuVihu(int sijX, int sijY) {
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.vahinko = 1 * PelinAsetukset.vaikeusAste;
        super.hp = 3 * PelinAsetukset.vaikeusAste;
        super.nimi = "Pikkuvihu";
        super.kuvake = new ImageIcon("tiedostot/kuvat/pikkuvihu.png");
        super.tehoavatAseet.add("Vesiämpäri");
        super.asetaTiedot();
    }
}
