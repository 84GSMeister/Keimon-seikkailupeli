package keimo.NPCt;

import keimo.Ruudut.PeliRuutu;
import keimo.Utility.SkaalattavaKuvake;

import javax.swing.ImageIcon;

public class Asevihu extends Vihollinen {

    @Override
    public void kukista(String kukistusTapa) {
        super.kukista(kukistusTapa);
        switch (kukistusTapa) {
            case "Ämpäri": this.kuvake = new ImageIcon("tiedostot/kuvat/npc/asevihu_ämpäröity.png"); break;
            case "Pesäpallomaila": this.kuvake = new ImageIcon("tiedostot/kuvat/npc/asevihu_lyöty.png"); break;
            default: this.kuvake = new ImageIcon("tiedostot/kuvat/npc/asevihu_ämpäröity.png"); break;
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
            case "nominatiivi": return "Asevihu";
            case "genetiivi": return "Asevihun";
            case "esiivi": return "Asevihuna";
            case "partitiivi": return "Asevihua";
            case "translatiivi": return "Asevihuksi";
            case "inessiivi": return "Asevihussa";
            case "elatiivi": return "Asevihusta";
            case "illatiivi": return "Asevihuun";
            case "adessiivi": return "Asevihulla";
            case "ablatiivi": return "Asevihulta";
            case "allatiivi": return "Asevihulle";
            default: return "Asevihu";
        }
    }

    @Override
    public void valitseKuvake() {
        if (this.hurtAika > 0) {
            this.kuvake = new ImageIcon("tiedostot/kuvat/npc/asevihu_hurt.png");
        }
        else {
            switch (this.suuntaVasenOikea) {
                case VASEN:
                    this.kuvake = new ImageIcon("tiedostot/kuvat/npc/asevihu.png");
                break;
                case OIKEA:
                    this.kuvake = new SkaalattavaKuvake("tiedostot/kuvat/npc/asevihu.png", SkaalattavaKuvake.Peilaus.PEILAA_X);
                break;
            }
        }
    }

    public Asevihu(int sijX, int sijY, String[] ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = 2;
        super.vahinko = 1;
        super.nopeus = 3;
        super.tekeeVahinkoa = true;
        super.ampuu = true;
        super.ammusVahinko = 2;
        super.kuvake = new ImageIcon("tiedostot/kuvat/npc/asevihu.png");
        super.kilpiTehoaa = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox.setLocation(sijX * PeliRuutu.pelaajanKokoPx, sijY * PeliRuutu.pelaajanKokoPx);
        super.nimi = "Asevihu";
        super.lisäOminaisuuksia = true;
        super.lisäOminaisuudet = ominaisuusLista;
        super.tehoavatAseet.add("Vesiämpäri");
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
