package keimo.NPCt;

import javax.swing.ImageIcon;

import keimo.PelinAsetukset;
import keimo.PääIkkuna;
import keimo.Säikeet.*;

public class Pahavihu extends Vihollinen {

    public void kukista(String kukistusTapa) {
        if (!this.kukistettu) {
            this.kukistettu = true;
            switch (kukistusTapa) {
                case "Ämpäri": this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu_suutari.png"); break;
                case "Pesäpallomaila": this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu_lyöty.png"); break;
                default: this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu_suutari.png"); break;
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

    public void vahingoita(int määrä) {
        super.vahingoita(määrä);
        ÄänentoistamisSäie.toistaSFX("pikkuvihu_damage");
    }

    public void päivitäLisäOminaisuudet(LiikeTapa liikeTapa) {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[1];
        this.lisäOminaisuudet[0] = "liiketapa=" + liikeTapa;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Paha vihu";
            case "genetiivi":
                return "Pahan vihun";
            case "esiivi":
                return "Pahana vihuna";
            case "partitiivi":
                return "Pahaa vihua";
            case "translatiivi":
                return "Pahaksi vihuksi";
            case "inessiivi":
                return "Pahassa vihussa";
            case "elatiivi":
                return "Pahasta vihusta";
            case "illatiivi":
                return "Pahaan vihuun";
            case "adessiivi":
                return "Pahalla vihulla";
            case "ablatiivi":
                return "Pahalta vihulta";
            case "allatiivi":
                return "Pahalle vihulle";
            default:
                return "Paha vihu";
        }
    }

    public Pahavihu(int sijX, int sijY, String[] ominaisuusLista) {
        super(ominaisuusLista);
        this.vahinko = 2 * PelinAsetukset.vaikeusAste;
        this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pahavihu.png");
        this.kilpiTehoaa = false;
        this.sijX = sijX;
        this.sijY = sijY;
        this.hitbox.setLocation(sijX * PääIkkuna.pelaajanKokoPx, sijY * PääIkkuna.pelaajanKokoPx);
        this.nimi = "Pahavihu";
        super.asetaTiedot();
    }
}
