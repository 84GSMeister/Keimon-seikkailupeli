package keimo.NPCt;

import keimo.Ruudut.PeliRuutu;
import keimo.Utility.SkaalattavaKuvake;

import javax.swing.ImageIcon;

public class Pikkuvihu extends Vihollinen {

    @Override
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

    @Override
    public void vahingoita(int määrä) {
        super.vahingoita(määrä);
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
            case "nominatiivi": return "Pikkuvihu";
            case "genetiivi": return "Pikkuvihun";
            case "esiivi": return "Pikkuvihuna";
            case "partitiivi": return "Pikkuvihua";
            case "translatiivi": return "Pikkuvihuksi";
            case "inessiivi": return "Pikkuvihussa";
            case "elatiivi": return "Pikkuvihusta";
            case "illatiivi": return "Pikkuvihuun";
            case "adessiivi": return "Pikkuvihulla";
            case "ablatiivi": return "Pikkuvihulta";
            case "allatiivi": return "Pikkuvihulle";
            default: return "Pikkuvihu";
        }
    }

    @Override
    public void valitseKuvake() {
        if (this.hurtAika > 0) {
            this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu_hurt.png");
        }
        else {
            switch (this.suuntaVasenOikea) {
                case VASEN:
                    this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu.gif");
                break;
                case OIKEA:
                    this.kuvake = new SkaalattavaKuvake("tiedostot/kuvat/npc/pikkuvihu.gif", SkaalattavaKuvake.Peilaus.PEILAA_X);
                break;
            }
        }
    }

    public Pikkuvihu(int sijX, int sijY, String[] ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = 2;
        super.vahinko = 1;
        super.nopeus = 3;
        super.tekeeVahinkoa = true;
        super.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu.gif");
        super.kilpiTehoaa = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox.setLocation(sijX * PeliRuutu.pelaajanKokoPx, sijY * PeliRuutu.pelaajanKokoPx);
        super.nimi = "Pikkuvihu";
        super.lisäOminaisuuksia = true;
        super.lisäOminaisuudet = ominaisuusLista;
        super.tehoavatAseet.add("Vesiämpäri");
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
