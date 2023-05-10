package keimo.NPCt;

import javax.swing.ImageIcon;

import keimo.PelinAsetukset;
import keimo.PääIkkuna;
import keimo.Säikeet.*;
import keimo.Utility.KäännettäväKuvake;
import keimo.Utility.SkaalattavaKuvake;

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

    public void päivitäLisäOminaisuudet(LiikeTapa liikeTapa) {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[1];
        this.lisäOminaisuudet[0] = "liiketapa=" + liikeTapa;
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

    @Override
    public void valitseKuvake() {
        switch (this.npcnSuuntaVasenOikea) {
            case VASEN:
                this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu.gif");
            break;
            case OIKEA:
                this.kuvake = new SkaalattavaKuvake("tiedostot/kuvat/npc/pikkuvihu.gif", SkaalattavaKuvake.Peilaus.PEILAA_X);
            break;
        }
    }

    public Pikkuvihu(int sijX, int sijY, String[] ominaisuusLista) {
        super(ominaisuusLista);
        this.vahinko = 1 * PelinAsetukset.vaikeusAste;
        this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu.gif");
        this.kilpiTehoaa = true;
        this.sijX = sijX;
        this.sijY = sijY;
        this.hitbox.setLocation(sijX * PääIkkuna.pelaajanKokoPx, sijY * PääIkkuna.pelaajanKokoPx);
        this.nimi = "Pikkuvihu";
        super.lisäOminaisuuksia = true;
        super.lisäOminaisuudet = ominaisuusLista;
        super.tehoavatAseet.add("Vesiämpäri");
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
