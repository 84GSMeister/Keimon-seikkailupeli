package keimo.entityt.npc;

import keimo.Peli;
import keimo.TarkistettavatArvot;
import keimo.Maastot.Maasto;
import keimo.Ruudut.PeliRuutu;
import keimo.Utility.Käännettävä;
import keimo.entityt.Entity;
import keimo.entityt.LiikkuvaObjekti;
import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.VisuaalinenObjekti;
import keimo.kenttäkohteet.avattavaEste.AvattavaEste;

import java.awt.Point;
import java.awt.Rectangle;

public abstract class NPC extends Entity implements Käännettävä {
    
    public int nopeus;
    public int hp;
    public boolean onLadattuPelissä = false;

    public Point annaSijaintiKentällä() {
        Point sijainti = new Point(sijX * PeliRuutu.esineenKokoPx, sijY * PeliRuutu.esineenKokoPx);
        return sijainti;
    }

    private boolean siirrä(Suunta suunta) {
        boolean NPCSiirtyi = false;
        switch (suunta) {
            case VASEN:
                if (this.hitbox.getMinX() > Peli.kentänAlaraja) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX() - this.nopeus, (int)this.hitbox.getMinY());
                    NPCSiirtyi = true;
                }
                break;
            case OIKEA:
                if (this.hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX() + this.nopeus, (int)this.hitbox.getMinY());
                    NPCSiirtyi = true;
                }
                break;
            case YLÖS:
                if (this.hitbox.getMinY() > Peli.kentänAlaraja) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX(), (int)this.hitbox.getMinY() - this.nopeus);
                    NPCSiirtyi = true;
                }
                break;
            case ALAS:
                if (this.hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX(), (int)this.hitbox.getMinY() + this.nopeus);
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
        return kokeileLiikkumista(suunta, false);
    }

    public boolean kokeileLiikkumista(Suunta suunta, boolean ignoraaCollision) {
        boolean npcSiirtyi = false;
        try {
            int objektiCollisiot = 0;
            synchronized (Peli.entityLista) {
                if (!ignoraaCollision) {
                    for (Entity entity : Peli.entityLista) {
                        if (entity != this && entity instanceof LiikkuvaObjekti) {
                            if (entity.hitbox.intersects(this.hitbox)) {
                                objektiCollisiot++;
                            }
                        }
                    }
                }
            }
            if (objektiCollisiot == 0) {
                switch (suunta) {
                    case VASEN:
                        this.suuntaVasenOikea = SuuntaVasenOikea.VASEN;
                        this.suuntaDiagonaali = SuuntaDiagonaali.VASEN;
                        if (hitbox.getMinX() > 0) {
                            Maasto m = Peli.annaMaastoKenttä()[(int)hitbox.getMinX()/PeliRuutu.pelaajanKokoPx][sijY];
                            KenttäKohde k = Peli.annaObjektiKenttä()[(int)hitbox.getMinX()/PeliRuutu.pelaajanKokoPx][sijY];
                            if (tarkistaLiikeMaasto(m, suunta) && tarkistaLiikeObjekti(k, suunta)) {
                                npcSiirtyi = siirrä(suunta);
                            }
                        }
                        break;
                    case OIKEA:
                        this.suuntaVasenOikea = SuuntaVasenOikea.OIKEA;
                        this.suuntaDiagonaali = SuuntaDiagonaali.OIKEA;
                        if (hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                            Maasto m = Peli.annaMaastoKenttä()[(int)hitbox.getMaxX()/PeliRuutu.pelaajanKokoPx][sijY];
                            KenttäKohde k = Peli.annaObjektiKenttä()[(int)hitbox.getMaxX()/PeliRuutu.pelaajanKokoPx][sijY];
                            if (tarkistaLiikeMaasto(m, suunta) && tarkistaLiikeObjekti(k, suunta)) {
                                npcSiirtyi = siirrä(suunta);
                            }
                        }
                        break;
                    case ALAS:
                        this.suuntaDiagonaali = SuuntaDiagonaali.ALAS;
                        if (hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                            Maasto m = Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMaxY()/PeliRuutu.pelaajanKokoPx];
                            KenttäKohde k = Peli.annaObjektiKenttä()[sijX][(int)hitbox.getMaxY()/PeliRuutu.pelaajanKokoPx];
                            if (tarkistaLiikeMaasto(m, suunta) && tarkistaLiikeObjekti(k, suunta)) {
                                npcSiirtyi = siirrä(suunta);
                            }
                        }
                        break;
                    case YLÖS:
                        this.suuntaDiagonaali = SuuntaDiagonaali.YLÖS;
                        if (hitbox.getMinY() > 0) {
                            Maasto m = Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMinY()/PeliRuutu.pelaajanKokoPx];
                            KenttäKohde k = Peli.annaObjektiKenttä()[sijX][(int)hitbox.getMinY()/PeliRuutu.pelaajanKokoPx];
                            if (tarkistaLiikeMaasto(m, suunta) && tarkistaLiikeObjekti(k, suunta)) {
                                npcSiirtyi = siirrä(suunta);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            //aioobe.printStackTrace();
        }
        return npcSiirtyi;
    }

    private boolean tarkistaLiikeMaasto(Maasto m, Suunta suunta) {
        if (m != null) {
            if (m.estääköLiikkumisen(suunta)) return false;
            else return true;
        }
        else return true;
    }

    private boolean tarkistaLiikeObjekti(KenttäKohde k, Suunta suunta) {
        if (k != null) {
            if (k instanceof VisuaalinenObjekti) {
                VisuaalinenObjekti vo = (VisuaalinenObjekti)k;
                if (vo.onkoEste()) return false;
                else return true;
            }
            else if (k instanceof AvattavaEste) {
                AvattavaEste ae = (AvattavaEste)k;
                if (ae.onkoAvattu()) return true;
                else return false;
            }
            else return true;
        }
        else return true;
    }

    public void teleport(int kohdeX, int kohdeY) {
        sijX = kohdeX;
        sijY = kohdeY;
        this.hitbox.setLocation(kohdeX * PeliRuutu.pelaajanKokoPx, kohdeY * PeliRuutu.pelaajanKokoPx);
    }


    public int annaHp() {
        return hp;
    }

    public String annaNimi() {
        return nimi;
    }

    void vahingoita(int määrä) {
        hp -= määrä;
    }

    void paranna(int määrä) {
        this.hp += määrä;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        return "Tältä kohteelta puuttuu sijamuotojen määritys.";
    }

    NPC(int sijX, int sijY) {
        super(sijX, sijY);
        super.leveys = 64;
        super.korkeus = 64;
        this.id = TarkistettavatArvot.npcId;
        TarkistettavatArvot.npcId++;
        this.onLadattuPelissä = false;
        this.hitbox = new Rectangle(0, 0, PeliRuutu.pelaajanKokoPx, PeliRuutu.pelaajanKokoPx);
        this.hitbox.setLocation(sijX * PeliRuutu.pelaajanKokoPx, sijY * PeliRuutu.pelaajanKokoPx);
    }
}
