package keimo.NPCt;

import java.awt.Rectangle;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.Ruudut.PeliRuutu;
import keimo.Utility.Käännettävä.Suunta;

public abstract class LiikkuvaObjekti extends Entity {

    public Rectangle sisäHitbox;
    protected int sisäHitboxOffset;
    public Rectangle ulkoHitbox;
    protected int ulkoHitboxOffset;

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
                if (this.hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
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
                if (this.hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX(), (int)this.hitbox.getMinY() + Pelaaja.nopeus);
                    this.sisäHitbox.setLocation((int)this.hitbox.getMinX() + sisäHitboxOffset, (int)this.hitbox.getMinY() + Pelaaja.nopeus + sisäHitboxOffset);
                    this.ulkoHitbox.setLocation((int)this.hitbox.getMinX() - Pelaaja.nopeus - ulkoHitboxOffset, (int)this.hitbox.getMinY() - ulkoHitboxOffset);
                    NPCSiirtyi = true;
                }
                break;
            default:
                return false;
        }
        this.sijX = (int)this.hitbox.getCenterX() / PeliRuutu.pelaajanKokoPx;
        this.sijY = (int)this.hitbox.getCenterY() / PeliRuutu.pelaajanKokoPx;
        return NPCSiirtyi;
    }

    public boolean kokeileLiikkumista(Suunta suunta) {
        boolean NPCSiirtyi = false;
        try {
            switch (suunta) {
                case VASEN:
                    this.suuntaVasenOikea = SuuntaVasenOikea.VASEN;
                    this.suuntaDiagonaali = SuuntaDiagonaali.VASEN;
                    if (hitbox.getMinX() > 0) {
                        if (Peli.annaMaastoKenttä()[(int)hitbox.getMinX()/PeliRuutu.pelaajanKokoPx][sijY] == null) {
                            NPCSiirtyi = siirrä(Suunta.VASEN);
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[(int)hitbox.getMinX()/PeliRuutu.pelaajanKokoPx][sijY].estääköLiikkumisen(suunta)) {
                                NPCSiirtyi = siirrä(Suunta.VASEN);
                            }
                        }
                    }
                    break;
                case OIKEA:
                    this.suuntaVasenOikea = SuuntaVasenOikea.OIKEA;
                    this.suuntaDiagonaali = SuuntaDiagonaali.OIKEA;
                    if (hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                        if (Peli.annaMaastoKenttä()[(int)hitbox.getMaxX()/PeliRuutu.pelaajanKokoPx][sijY] == null) {
                            NPCSiirtyi = siirrä(Suunta.OIKEA);
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[(int)hitbox.getMaxX()/PeliRuutu.pelaajanKokoPx][sijY].estääköLiikkumisen(suunta)) {
                                NPCSiirtyi = siirrä(Suunta.OIKEA);
                            }
                        }
                    }
                    break;
                case ALAS:
                    this.suuntaDiagonaali = SuuntaDiagonaali.ALAS;
                    if (hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                        if (Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMaxY()/PeliRuutu.pelaajanKokoPx] == null) {
                            NPCSiirtyi = siirrä(Suunta.ALAS);
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMaxY()/PeliRuutu.pelaajanKokoPx].estääköLiikkumisen(suunta)) {
                                NPCSiirtyi = siirrä(Suunta.ALAS);
                            }
                        }
                    }
                    break;
                case YLÖS:
                    this.suuntaDiagonaali = SuuntaDiagonaali.YLÖS;
                    if (hitbox.getMinY() > 0) {
                        if (Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMinY()/PeliRuutu.pelaajanKokoPx] == null) {
                            NPCSiirtyi = siirrä(Suunta.YLÖS);
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMinY()/PeliRuutu.pelaajanKokoPx].estääköLiikkumisen(suunta)) {
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
