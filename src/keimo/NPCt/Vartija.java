package keimo.NPCt;

import keimo.Ruudut.PeliRuutu;
import keimo.Utility.*;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Vartija extends Vihollinen {

    public boolean liikkuu = false;
    
    @Override
    public void kukista(String kukistusTapa) {
        super.kukista(kukistusTapa);
        switch (kukistusTapa) {
            case "Ämpäri": this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu_suutari.png"); break;
            case "Pesäpallomaila": this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu_lyöty.png"); break;
            default: this.kuvake = new ImageIcon("tiedostot/kuvat/npc/pikkuvihu_suutari.png"); break;
        }
        JOptionPane.showMessageDialog(null, "Huijaatko?", "", JOptionPane.QUESTION_MESSAGE);
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
            case "nominatiivi": return "Vartija";
            case "genetiivi": return "Vartijan";
            case "esiivi": return "Vartijana";
            case "partitiivi": return "Vartijaa";
            case "translatiivi": return "Vartijaksi";
            case "inessiivi": return "Vartijassa";
            case "elatiivi": return "Vartijasta";
            case "illatiivi": return "Vartijaan";
            case "adessiivi": return "Vartijalla";
            case "ablatiivi": return "Vartijalta";
            case "allatiivi": return "Vartijalle";
            default: return "Vartija";
        }
    }

    @Override
    public void valitseKuvake() {
        if (this.liikkuu) {
            switch (this.suuntaVasenOikea) {
                case VASEN:
                    this.kuvake = new SkaalattavaKuvake("tiedostot/kuvat/npc/vartija_on.gif", SkaalattavaKuvake.Peilaus.PEILAA_X);
                break;
                case OIKEA:
                    this.kuvake = new ImageIcon("tiedostot/kuvat/npc/vartija_on.gif");
                break;
            }
        }
        else {
            switch (this.suuntaVasenOikea) {
                case VASEN:
                    this.kuvake = new SkaalattavaKuvake("tiedostot/kuvat/npc/vartija_off.png", SkaalattavaKuvake.Peilaus.PEILAA_X);
                break;
                case OIKEA:
                    this.kuvake = new ImageIcon("tiedostot/kuvat/npc/vartija_off.png");
                break;
            }
        }
    }

    public Vartija(int sijX, int sijY, String[] ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = Integer.MAX_VALUE;
        super.vahinko = 666;
        super.nopeus = 8;
        super.tekeeVahinkoa = false;
        super.kuvake = new ImageIcon("tiedostot/kuvat/npc/vartija_off.png");
        super.kilpiTehoaa = false;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox.setLocation(sijX * PeliRuutu.pelaajanKokoPx, sijY * PeliRuutu.pelaajanKokoPx);
        super.nimi = "Vartija";
        super.lisäOminaisuuksia = true;
        super.lisäOminaisuudet = ominaisuusLista;
        super.asetaTiedot();
    }
}
