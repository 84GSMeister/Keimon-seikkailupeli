package keimo.NPCt;

import javax.swing.ImageIcon;

import keimo.PelinAsetukset;
import keimo.PääIkkuna;
import keimo.TarkistettavatArvot;
import keimo.ÄänentoistamisSäie;

public class Pikkuvihu extends Vihollinen {

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
    
    public Pikkuvihu(LiikeTapa liikeTapa) {
        super(liikeTapa);
        this.vahinko = 1 * PelinAsetukset.vaikeusAste;
        this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu.gif");
        this.kilpiTehoaa = true;
        this.sijX = 0;
        this.sijY = 0;
        this.sijX_PX_vy = 0;
        this.sijY_PX_vy = 0;
        this.sijX_PX_vy = sijX_PX_vy + PääIkkuna.pelaajanKokoPx;
        this.sijY_PX_vy = sijY_PX_vy + PääIkkuna.pelaajanKokoPx;
        super.tehoavatAseet.add("Vesiämpäri");
        super.tehoavatAseet.add("Pesäpallomaila");
    }

    public Pikkuvihu(int sijX, int sijY, LiikeTapa liikeTapa) {
        super(liikeTapa);
        this.vahinko = 1 * PelinAsetukset.vaikeusAste;
        this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu.gif");
        this.kilpiTehoaa = true;
        this.sijX = sijX;
        this.sijY = sijY;
        this.sijX_PX_vy = this.sijX * PääIkkuna.pelaajanKokoPx;
        this.sijY_PX_vy = this.sijY * PääIkkuna.pelaajanKokoPx;
        this.sijX_PX_oa = this.sijX_PX_vy + PääIkkuna.pelaajanKokoPx;
        this.sijY_PX_oa = this.sijY_PX_vy + PääIkkuna.pelaajanKokoPx;
        this.hitbox.setLocation(sijX_PX_vy, sijY_PX_vy);
        this.nimi = "Pikkuvihu";
        super.tehoavatAseet.add("Vesiämpäri");
        super.tehoavatAseet.add("Pesäpallomaila");
    }
}
