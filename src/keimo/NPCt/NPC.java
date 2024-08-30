package keimo.NPCt;

import keimo.Peli;
import keimo.TarkistettavatArvot;
import keimo.Ruudut.PeliRuutu;
import keimo.Utility.Käännettävä;

import java.awt.Point;
import java.awt.Rectangle;

public abstract class NPC extends Entity implements Käännettävä {
    
    
    public int nopeus;
    //public Rectangle hitbox = new Rectangle(0, 0, PeliRuutu.pelaajanKokoPx, PeliRuutu.pelaajanKokoPx);
    protected int hp;
    //public ImageIcon kuvake;
    
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
        boolean NPCSiirtyi = false;
        try {
            int objektiCollisiot = 0;
            for (Entity entity : Peli.entityLista) {
                if (entity != this && entity instanceof LiikkuvaObjekti) {
                    if (entity.hitbox.intersects(this.hitbox)) {
                        objektiCollisiot++;
                    }
                }
            }
            if (objektiCollisiot == 0) {
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
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            //aioobe.printStackTrace();
        }
        return NPCSiirtyi;
    }

    public boolean kokeileLiikkumista(Suunta suunta, boolean ignoraaCollision) {
        boolean NPCSiirtyi = false;
        try {
            int objektiCollisiot = 0;
            if (!ignoraaCollision) {
                for (Entity entity : Peli.entityLista) {
                    if (entity != this && entity instanceof LiikkuvaObjekti) {
                        if (entity.hitbox.intersects(this.hitbox)) {
                            objektiCollisiot++;
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
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            //aioobe.printStackTrace();
        }
        return NPCSiirtyi;
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
        this.id = TarkistettavatArvot.npcId;
        TarkistettavatArvot.npcId++;
        this.onLadattuPelissä = false;
        this.hitbox = new Rectangle(0, 0, PeliRuutu.pelaajanKokoPx, PeliRuutu.pelaajanKokoPx);
        this.hitbox.setLocation(sijX * PeliRuutu.pelaajanKokoPx, sijY * PeliRuutu.pelaajanKokoPx);
    }
}
