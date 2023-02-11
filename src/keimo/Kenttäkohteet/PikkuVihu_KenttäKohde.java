package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

import keimo.PelinAsetukset;;

public class PikkuVihu_KenttäKohde extends Vihollinen_KenttöKohde {

    public void kukista(String kukistusTapa) {
        if (!this.kukistettu) {
            this.kukistettu = true;
            switch (kukistusTapa) {
                case "Ämpäri": this.kuvake = new ImageIcon("tiedostot/kuvat/pikkuvihu_suutari.png"); break;
                case "Pesäpallomaila": this.kuvake = new ImageIcon("tiedostot/kuvat/pikkuvihu_lyöty.png"); break;
                default: this.kuvake = new ImageIcon("tiedostot/kuvat/pikkuvihu_suutari.png"); break;
            }
        }
    }

    public String katso() {
        if (!onkoKukistettu()) {
            return "Voi ei! Se on ilkeä vihollinen";
        }
        else {
            return "Vihollinen on kukistettu ja nyt täysin harmiton.";
        }
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
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

    public PikkuVihu_KenttäKohde(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.vahinko = 1 * PelinAsetukset.vaikeusAste;
        super.hp = 3 * PelinAsetukset.vaikeusAste;
        super.nimi = "Pikkuvihu";
        super.kuvake = new ImageIcon("tiedostot/kuvat/pikkuvihu.png");
        super.tehoavatAseet.add("Vesiämpäri");
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
