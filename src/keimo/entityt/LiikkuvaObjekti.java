package keimo.entityt;

import keimo.Pelaaja;
import keimo.Peli;

import java.awt.Rectangle;

public abstract class LiikkuvaObjekti extends Entity {

    public Rectangle sisäHitbox;
    protected int sisäHitboxOffset;
    public Rectangle ulkoHitbox;
    protected int ulkoHitboxOffset;
    public boolean voiTyöntää;

    private boolean siirrä(Suunta suunta) {
        boolean NPCSiirtyi = false;
        switch (suunta) {
            case VASEN:
                if (this.hitbox.getMinX() > Peli.kentänAlaraja) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX() - Pelaaja.nopeus, (int)this.hitbox.getMinY());
                    this.sisäHitbox.setLocation((int)this.hitbox.getMinX() - Pelaaja.nopeus + sisäHitboxOffset, (int)this.hitbox.getMinY() + sisäHitboxOffset);
                    this.ulkoHitbox.setLocation((int)this.hitbox.getMinX() - Pelaaja.nopeus - ulkoHitboxOffset, (int)this.hitbox.getMinY() - ulkoHitboxOffset);
                    NPCSiirtyi = true;
                }
                break;
            case OIKEA:
                if (this.hitbox.getMaxX() < Peli.kentänKoko * 64) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX() + Pelaaja.nopeus, (int)this.hitbox.getMinY());
                    this.sisäHitbox.setLocation((int)this.hitbox.getMinX() + Pelaaja.nopeus + sisäHitboxOffset, (int)this.hitbox.getMinY() + sisäHitboxOffset);
                    this.ulkoHitbox.setLocation((int)this.hitbox.getMinX() - Pelaaja.nopeus - ulkoHitboxOffset, (int)this.hitbox.getMinY() - ulkoHitboxOffset);
                    NPCSiirtyi = true;
                }
                break;
            case YLÖS:
                if (this.hitbox.getMinY() > Peli.kentänAlaraja) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX(), (int)this.hitbox.getMinY() - Pelaaja.nopeus);
                    this.sisäHitbox.setLocation((int)this.hitbox.getMinX() + sisäHitboxOffset, (int)this.hitbox.getMinY() - Pelaaja.nopeus + sisäHitboxOffset);
                    this.ulkoHitbox.setLocation((int)this.hitbox.getMinX() - Pelaaja.nopeus - ulkoHitboxOffset, (int)this.hitbox.getMinY() - ulkoHitboxOffset);
                    NPCSiirtyi = true;
                }
                break;
            case ALAS:
                if (this.hitbox.getMaxY() < Peli.kentänKoko * 64) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX(), (int)this.hitbox.getMinY() + Pelaaja.nopeus);
                    this.sisäHitbox.setLocation((int)this.hitbox.getMinX() + sisäHitboxOffset, (int)this.hitbox.getMinY() + Pelaaja.nopeus + sisäHitboxOffset);
                    this.ulkoHitbox.setLocation((int)this.hitbox.getMinX() - Pelaaja.nopeus - ulkoHitboxOffset, (int)this.hitbox.getMinY() - ulkoHitboxOffset);
                    NPCSiirtyi = true;
                }
                break;
            default:
                return false;
        }
        this.sijX = (int)this.hitbox.getCenterX() / 64;
        this.sijY = (int)this.hitbox.getCenterY() / 64;
        return NPCSiirtyi;
    }

    public boolean kokeileLiikkumista(Suunta suunta) {
        boolean NPCSiirtyi = false;
        try {
            switch (suunta) {
                case VASEN:
                    this.suuntaVasenOikea = SuuntaVasenOikea.VASEN;
                    this.suunta = Suunta.VASEN;
                    if (hitbox.getMinX() > 0) {
                        if (Peli.annaMaastoKenttä()[(int)hitbox.getMinX()/64][sijY] == null) {
                            NPCSiirtyi = siirrä(Suunta.VASEN);
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[(int)hitbox.getMinX()/64][sijY].estääköLiikkumisen(suunta)) {
                                NPCSiirtyi = siirrä(Suunta.VASEN);
                            }
                        }
                    }
                    break;
                case OIKEA:
                    this.suuntaVasenOikea = SuuntaVasenOikea.OIKEA;
                    this.suunta = Suunta.OIKEA;
                    if (hitbox.getMaxX() < Peli.kentänKoko * 64) {
                        if (Peli.annaMaastoKenttä()[(int)hitbox.getMaxX()/64][sijY] == null) {
                            NPCSiirtyi = siirrä(Suunta.OIKEA);
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[(int)hitbox.getMaxX()/64][sijY].estääköLiikkumisen(suunta)) {
                                NPCSiirtyi = siirrä(Suunta.OIKEA);
                            }
                        }
                    }
                    break;
                case ALAS:
                    this.suunta = Suunta.ALAS;
                    if (hitbox.getMaxY() < Peli.kentänKoko * 64) {
                        if (Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMaxY()/64] == null) {
                            NPCSiirtyi = siirrä(Suunta.ALAS);
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMaxY()/64].estääköLiikkumisen(suunta)) {
                                NPCSiirtyi = siirrä(Suunta.ALAS);
                            }
                        }
                    }
                    break;
                case YLÖS:
                    this.suunta = Suunta.YLÖS;
                    if (hitbox.getMinY() > 0) {
                        if (Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMinY()/64] == null) {
                            NPCSiirtyi = siirrä(Suunta.YLÖS);
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMinY()/64].estääköLiikkumisen(suunta)) {
                                NPCSiirtyi = siirrä(Suunta.YLÖS);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            //aioobe.printStackTrace();
        }
        return NPCSiirtyi;
    }

    LiikkuvaObjekti(int sijX, int sijY) {
        super(sijX, sijY);
    }
    
}
