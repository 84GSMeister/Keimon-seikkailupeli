package keimo.NPCt;

import keimo.Ruudut.PeliRuutu;
import keimo.Säikeet.*;
import keimo.Utility.*;

import javax.swing.ImageIcon;

public class Pahavihu extends Vihollinen {

    @Override
    public void kukista(String kukistusTapa) {
        super.kukista(kukistusTapa);
        switch (kukistusTapa) {
            case "Ämpäri": this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pahavihu_suutari.png"); break;
            case "Pesäpallomaila": this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pahavihu_lyöty.png"); break;
            default: this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pahavihu_suutari.png"); break;
        }
    }

    @Override
    public void vahingoita(int määrä) {
        super.vahingoita(määrä);
        ÄänentoistamisSäie.toistaSFX("pikkuvihu_damage");
    }

    public String katso() {
        if (!onkoKukistettu()) {
            return "Voi ei! Se on ilkeä vihollinen";
        }
        else {
            return "Vihollinen on kukistettu ja nyt täysin harmiton.";
        }
    }

    public void päivitäLisäOminaisuudet(LiikeTapa liikeTapa) {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[1];
        this.lisäOminaisuudet[0] = "liiketapa=" + liikeTapa;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Paha vihu";
            case "genetiivi": return "Pahan vihun";
            case "esiivi": return "Pahana vihuna";
            case "partitiivi": return "Pahaa vihua";
            case "translatiivi": return "Pahaksi vihuksi";
            case "inessiivi": return "Pahassa vihussa";
            case "elatiivi": return "Pahasta vihusta";
            case "illatiivi": return "Pahaan vihuun";
            case "adessiivi": return "Pahalla vihulla";
            case "ablatiivi": return "Pahalta vihulta";
            case "allatiivi": return "Pahalle vihulle";
            default: return "Paha vihu";
        }
    }

    @Override
    public void valitseKuvake() {
        if (this.hurtAika > 0) {
            this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pahavihu_hurt.png");
        }
        else {
            switch (this.suuntaVasenOikea) {
                case VASEN:
                    this.kuvake = new SkaalattavaKuvake("tiedostot/kuvat/npc/pahavihu.gif", SkaalattavaKuvake.Peilaus.PEILAA_X);
                break;
                case OIKEA:
                    this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pahavihu.gif");
                break;
            }
        }
    }

    public Pahavihu(int sijX, int sijY, String[] ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = 5;
        super.vahinko = 2;
        super.nopeus = 4;
        super.tekeeVahinkoa = true;
        super.kuvake = new ImageIcon("tiedostot/kuvat/npc/pahavihu.gif");
        super.kilpiTehoaa = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox.setLocation(sijX * PeliRuutu.pelaajanKokoPx, sijY * PeliRuutu.pelaajanKokoPx);
        super.nimi = "Pahavihu";
        super.lisäOminaisuuksia = true;
        super.lisäOminaisuudet = ominaisuusLista;
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
