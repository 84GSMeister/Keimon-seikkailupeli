package keimo.Kenttäkohteet;
import javax.swing.ImageIcon;

import keimo.PelinAsetukset;

public class PahaVihu extends Vihollinen{

    public void kukista() {
        this.kukistettu = true;
        this.kuvake = new ImageIcon("tiedostot/kuvat/pikkuvihu_suutari.png");
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
                return "Pahavihu";
            case "genetiivi":
                return "Pahavihun";
            case "esiivi":
                return "Pahavihuna";
            case "partitiivi":
                return "Pahavihua";
            case "translatiivi":
                return "Pahavihuksi";
            case "inessiivi":
                return "Pahavihussa";
            case "elatiivi":
                return "Pahavihusta";
            case "illatiivi":
                return "Pahavihuun";
            case "adessiivi":
                return "Pahavihulla";
            case "ablatiivi":
                return "Pahavihulta";
            case "allatiivi":
                return "Pahavihulle";
            default:
                return "Pahavihu";
        }
    }

    public PahaVihu() {
        super.vahinko = 1 * PelinAsetukset.vaikeusAste;
        super.hp = 3 * PelinAsetukset.vaikeusAste;
        super.nimi = "Pahavihu";
        super.kuvake = new ImageIcon("tiedostot/kuvat/pahavihu.png");
        super.kilpiTehoaa = false;
        super.asetaTiedot();
    }

    public PahaVihu(int sijX, int sijY) {
        super.määritettySijainti = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.vahinko = 1 * PelinAsetukset.vaikeusAste;
        super.hp = 3 * PelinAsetukset.vaikeusAste;
        super.nimi = "Pahavihu";
        super.kuvake = new ImageIcon("tiedostot/kuvat/pahavihu.png");
        super.kilpiTehoaa = false;
        super.asetaTiedot();
    }
}
