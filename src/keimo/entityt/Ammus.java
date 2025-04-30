package keimo.entityt;

import keimo.Peli;
import keimo.entityt.npc.Vihollinen;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.awt.Rectangle;

public class Ammus extends Entity {
    
    public static int ammusId = 0;

    public int id = 0;
    public int nopeus;
    public int damage;
    public int elinAika = 240;
    public Vihollinen ampuja;

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

    public boolean kokeileLiikettä(Suunta suunta) {
        boolean liikeOnnistui = false;
        switch (suunta) {
            case OIKEA:
                if (hitbox.getMaxX() < Peli.kentänKoko * 64 + 64) {
                    liikeOnnistui = true;
                }
            break;
            case VASEN:
                if (hitbox.getMinX() > -64) {
                    liikeOnnistui = true;
                }
            break;
            case ALAS:
                if (hitbox.getMaxY() < Peli.kentänKoko * 64 + 64) {
                    liikeOnnistui = true;
                }
            break;
            case YLÖS:
                if (hitbox.getMinY() > -64) {
                    liikeOnnistui = true;
                }
            break;
            case ALAOIKEA:
                if (hitbox.getMaxX() < Peli.kentänKoko * 64 + 64 && hitbox.getMaxY() < Peli.kentänKoko * 64 + 64) {
                    liikeOnnistui = true;
                }
            break;
            case ALAVASEN:
                if (hitbox.getMinX() > -64 && hitbox.getMaxY() < Peli.kentänKoko * 64 + 64) {
                    liikeOnnistui = true;
                }
            break;
            case YLÄOIKEA:
                if (hitbox.getMaxX() < Peli.kentänKoko * 64 + 64 && hitbox.getMinY() > -64) {
                    liikeOnnistui = true;
                }
            break;
            case YLÄVASEN:
                if (hitbox.getMinX() > -64 && hitbox.getMinY() > -64) {
                    liikeOnnistui = true;
                }
            break;
            default:
            break;
        }
        return liikeOnnistui;
    }

    public void liikuta8suuntaan(Suunta suunta) {
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
        this.sijX = (int)this.hitbox.getCenterX() / 64;
        this.sijY = (int)this.hitbox.getCenterY() / 64;
    }

    public Ammus(int sijX, int sijY, SuuntaVasenOikea suunta, int vahinko, Vihollinen ampuja) {
        super(sijX, sijY);
        super.nimi = "Ammus";
        this.id = ammusId;
        ammusId++;
        this.nopeus = 12;
        this.damage = vahinko;
        this.sijX = sijX/64;
        this.sijY = sijY/64;
        this.leveys = 16;
        this.korkeus = 16;
        this.hitbox = new Rectangle(sijX, sijX, 16, 16);
        this.hitbox.setLocation(sijX, sijY);
        this.kuvaTiedosto = "tiedostot/kuvat/entity/ammus.png";
        this.tekstuuri = new Tekstuuri(kuvaTiedosto);
        this.ampuja = ampuja;
        if (suunta == SuuntaVasenOikea.OIKEA) {
            this.suunta = Suunta.OIKEA;
        }
        else {
            this.suunta = Suunta.VASEN;
        }
    }

    public Ammus(int sijX, int sijY, Suunta suunta8, int vahinko, Vihollinen ampuja) {
        super(sijX, sijY);
        super.nimi = "Ammus";
        this.id = ammusId;
        ammusId++;
        this.nopeus = 12;
        this.damage = vahinko;
        this.sijX = sijX/64;
        this.sijY = sijY/64;
        this.leveys = 16;
        this.korkeus = 16;
        this.hitbox = new Rectangle(sijX, sijX, 16, 16);
        this.hitbox.setLocation(sijX, sijY);
        this.kuvaTiedosto = "tiedostot/kuvat/entity/ammus.png";
        this.tekstuuri = new Tekstuuri(kuvaTiedosto);
        this.ampuja = ampuja;
        this.suunta = suunta8;
    }
}
