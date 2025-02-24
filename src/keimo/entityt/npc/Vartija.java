package keimo.entityt.npc;

import keimo.Ruudut.PeliRuutu;
import keimo.Utility.*;
import keimo.keimoEngine.grafiikat.Animaatio;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Vartija extends Vihollinen {

    public boolean liikkuu = false;
    public int x1, y1, x2, y2;
    private Tekstuuri offTekstuuri = new Tekstuuri("tiedostot/kuvat/npc/vartija_off.png");
    private Animaatio onTekstuuri = new Animaatio(15, "tiedostot/kuvat/npc/vartija_on.gif");
    
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
        this.lisäOminaisuudet = new String[3];
        this.lisäOminaisuudet[0] = "liiketapa=" + liikeTapa;
        this.lisäOminaisuudet[1] = "näköalue1=" + x1 + "_" + y1;
        this.lisäOminaisuudet[2] = "näköalue2=" + x2 + "_" + y2; 
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
            this.tekstuuri = onTekstuuri;
            switch (this.suuntaVasenOikea) {
                case VASEN:
                    this.kuvaTiedosto = "tiedostot/kuvat/npc/vartija_on.gif";
                    this.kuvake = new SkaalattavaKuvake(kuvaTiedosto, SkaalattavaKuvake.Peilaus.PEILAA_X);
                break;
                case OIKEA:
                    this.kuvaTiedosto = "tiedostot/kuvat/npc/vartija_on.gif";    
                    this.kuvake = new ImageIcon(kuvaTiedosto);
                break;
            }
        }
        else {
            this.tekstuuri = offTekstuuri;
            switch (this.suuntaVasenOikea) {
                case VASEN:
                    this.kuvaTiedosto = "tiedostot/kuvat/npc/vartija_off.png";
                    this.kuvake = new SkaalattavaKuvake(kuvaTiedosto, SkaalattavaKuvake.Peilaus.PEILAA_X);
                break;
                case OIKEA:
                    this.kuvaTiedosto = "tiedostot/kuvat/npc/vartija_off.png";
                    this.kuvake = new ImageIcon(kuvaTiedosto);
                break;
            }
        }
    }

    public Vartija(int sijX, int sijY, String[] ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = Integer.MAX_VALUE;
        super.vahinko = 666;
        super.nopeus = 9;
        super.tekeeVahinkoa = false;
        super.kuvaTiedosto = "tiedostot/kuvat/npc/vartija_off.png";
        super.kuvake = new ImageIcon(kuvaTiedosto);
        super.tekstuuri = new Tekstuuri(kuvaTiedosto);
        super.kilpiTehoaa = false;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox.setLocation(sijX * PeliRuutu.pelaajanKokoPx, sijY * PeliRuutu.pelaajanKokoPx);
        super.nimi = "Vartija";
        super.lisäOminaisuuksia = true;
        super.lisäOminaisuudet = ominaisuusLista;
        this.x1 = 0;
        this.x2 = 29;
        this.y1 = 0;
        this.y2 = 4;
        super.asetaTiedot();
    }
}
