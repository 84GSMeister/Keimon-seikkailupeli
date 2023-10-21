package keimo.NPCt;

import keimo.Peli;
import keimo.Ruudut.PeliRuutu;

import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Ammus extends Entity {
    
    public static int ammusId = 0;

    public int id = 0;
    public int nopeus;
    public int damage;
    public int elinAika = 240;
    public ImageIcon kuvake;
    public Rectangle hitbox = new Rectangle(0, 0, 16, 16);
    //public SuuntaVasenOikea suunta = SuuntaVasenOikea.VASEN;
    public SuuntaDiagonaali suunta8 = SuuntaDiagonaali.VASEN;

    public int annaSijX() {
        return sijX;
    }

    public int annaSijY() {
        return sijY;
    }

    public int annaAlkuSijX() {
        return alkuSijX;
    }

    public int annaAlkuSijY() {
        return alkuSijY;
    }

    public Icon annaKuvake() {
        return kuvake;
    }

    public boolean kokeileLiikettä(SuuntaDiagonaali suunta) {
        boolean liikeOnnistui = false;
        switch (suunta) {
            case OIKEA:
                if (hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx + 64) {
                    liikeOnnistui = true;
                }
            break;
            case VASEN:
                if (hitbox.getMinX() > -PeliRuutu.pelaajanKokoPx) {
                    liikeOnnistui = true;
                }
            break;
            case ALAS:
                if (hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx + 64) {
                    liikeOnnistui = true;
                }
            break;
            case YLÖS:
                if (hitbox.getMinY() > -PeliRuutu.pelaajanKokoPx) {
                    liikeOnnistui = true;
                }
            break;
            case ALAOIKEA:
                if (hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx + 64 && hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx + 64) {
                    liikeOnnistui = true;
                }
            break;
            case ALAVASEN:
                if (hitbox.getMinX() > -PeliRuutu.pelaajanKokoPx && hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx + 64) {
                    liikeOnnistui = true;
                }
            break;
            case YLÄOIKEA:
                if (hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx + 64 && hitbox.getMinY() > -PeliRuutu.pelaajanKokoPx) {
                    liikeOnnistui = true;
                }
            break;
            case YLÄVASEN:
                if (hitbox.getMinX() > -PeliRuutu.pelaajanKokoPx && hitbox.getMinY() > -PeliRuutu.pelaajanKokoPx) {
                    liikeOnnistui = true;
                }
            break;
            default:
            break;
        }
        return liikeOnnistui;
    }

    // public void liikuta(SuuntaVasenOikea suunta) {
    //     switch (suunta) {
    //         case OIKEA:
    //             this.hitbox.setLocation((int)this.hitbox.getLocation().getX() + nopeus, (int)this.hitbox.getLocation().getY());
    //         break;
    //         case VASEN:
    //             this.hitbox.setLocation((int)this.hitbox.getLocation().getX() - nopeus, (int)this.hitbox.getLocation().getY());
    //         break;
    //         default:
    //         break;
    //     }
    //     this.sijX = (int)this.hitbox.getCenterX() / PeliRuutu.pelaajanKokoPx;
    //     this.sijY = (int)this.hitbox.getCenterY() / PeliRuutu.pelaajanKokoPx;
    // }

    public void liikuta8suuntaan(SuuntaDiagonaali suunta) {
        switch (suunta) {
            case OIKEA:
                this.hitbox.setLocation((int)this.hitbox.getLocation().getX() + nopeus, (int)this.hitbox.getLocation().getY());
            break;
            case VASEN:
                this.hitbox.setLocation((int)this.hitbox.getLocation().getX() - nopeus, (int)this.hitbox.getLocation().getY());
            break;
            case ALAOIKEA:
                this.hitbox.setLocation((int)this.hitbox.getLocation().getX() + nopeus, (int)this.hitbox.getLocation().getY());
                this.hitbox.setLocation((int)this.hitbox.getLocation().getX(), (int)this.hitbox.getLocation().getY() + nopeus);
            break;
            case ALAS:
                this.hitbox.setLocation((int)this.hitbox.getLocation().getX(), (int)this.hitbox.getLocation().getY() + nopeus);
            break;
            case ALAVASEN:
                this.hitbox.setLocation((int)this.hitbox.getLocation().getX() - nopeus, (int)this.hitbox.getLocation().getY());
                this.hitbox.setLocation((int)this.hitbox.getLocation().getX(), (int)this.hitbox.getLocation().getY() + nopeus);
            break;
            case YLÄOIKEA:
                this.hitbox.setLocation((int)this.hitbox.getLocation().getX() + nopeus, (int)this.hitbox.getLocation().getY());
                this.hitbox.setLocation((int)this.hitbox.getLocation().getX(), (int)this.hitbox.getLocation().getY() - nopeus);
            break;
            case YLÄVASEN:
                this.hitbox.setLocation((int)this.hitbox.getLocation().getX() - nopeus, (int)this.hitbox.getLocation().getY());
                this.hitbox.setLocation((int)this.hitbox.getLocation().getX(), (int)this.hitbox.getLocation().getY() - nopeus);
            break;
            case YLÖS:
                this.hitbox.setLocation((int)this.hitbox.getLocation().getX(), (int)this.hitbox.getLocation().getY() - nopeus);
            break;
            default:
            break;
        }
        this.sijX = (int)this.hitbox.getCenterX() / PeliRuutu.pelaajanKokoPx;
        this.sijY = (int)this.hitbox.getCenterY() / PeliRuutu.pelaajanKokoPx;
    }

    public Ammus(int sijX, int sijY) {
        this.id = ammusId;
        ammusId++;
        this.sijX = sijX;
        this.sijY = sijY;
        this.nopeus = 12;
        this.damage = 4;
        this.alkuSijX = sijX;
        this.alkuSijY = sijY;
        this.hitbox.setLocation(sijX, sijY);
        this.kuvake = new ImageIcon("tiedostot/kuvat/npc/ammus.png");
        this.suuntaDiagonaali = SuuntaDiagonaali.VASEN;
    }

    public Ammus(int sijX, int sijY, SuuntaVasenOikea suunta, int vahinko) {
        this.id = ammusId;
        ammusId++;
        this.nopeus = 12;
        this.damage = vahinko;
        this.hitbox.setLocation(sijX, sijY);
        this.sijX = sijX/PeliRuutu.pelaajanKokoPx;
        this.sijY = sijY/PeliRuutu.pelaajanKokoPx;
        this.kuvake = new ImageIcon("tiedostot/kuvat/npc/ammus.png");
        if (suunta == SuuntaVasenOikea.OIKEA) {
            this.suunta8 = SuuntaDiagonaali.OIKEA;
        }
        else {
            this.suunta8 = SuuntaDiagonaali.VASEN;
        }
    }

    public Ammus(int sijX, int sijY, SuuntaDiagonaali suunta8, int vahinko) {
        this.id = ammusId;
        ammusId++;
        this.nopeus = 12;
        this.damage = vahinko;
        this.hitbox.setLocation(sijX, sijY);
        this.sijX = sijX/PeliRuutu.pelaajanKokoPx;
        this.sijY = sijY/PeliRuutu.pelaajanKokoPx;
        this.kuvake = new ImageIcon("tiedostot/kuvat/npc/ammus.png");
        this.suunta8 = suunta8;
    }
}
