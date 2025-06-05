package keimo;

import keimo.Utility.Käännettävä;
import keimo.entityt.npc.Vihollinen;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.keimoEngine.äänet.Äänet;
import keimo.kenttäkohteet.*;
import keimo.kenttäkohteet.esine.*;
import keimo.Liikkuminen.*;
import keimo.Maastot.IsoLaatta;
import keimo.Maastot.Maasto;

import javax.swing.*;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Pelaaja implements Käännettävä {
    
    public static final Esine[] esineet = new Esine[6];
    static int valittuEsine = 0;
    public static int sijX;
    public static int sijY;
    public static int alkuHuone;
    public static int alkuSijX;
    public static int alkuSijY;
    public static int pelaajanKokoPx = 64;
    public static final Rectangle hitbox = new Rectangle(0, 0, 64, 64);
    public static int hp;
    public static double raha;
    public static int kuparit;
    static int syödytRuoat;
    public static int nopeus;
    public static int vakionopeus = 8;
    public static ImageIcon kuvake;
    public static int kuolemattomuusAika;
    public static int reaktioAika;
    public static int hyökkäysAika;
    public static int käyttöViive;
    public static Ase käytettyAse;
    static boolean vihollisenKohdalla = false;
    static Vihollinen vihollinenKohdalla;
    public static Vihollinen viimeisinOsunutVihollinen;
    public static ArrayList<Esine> ostosKori = new ArrayList<Esine>();
    public static double ostostenHintaYhteensä;
    public static float känninVoimakkuusFloat;
    public static int känniKuolemattomuus;
    private static Random r = new Random();
    public static boolean pelaajaLiikkuu = false;
    public static boolean noclip = false;
    public static boolean ohitaTavoitteet = false;
    public static boolean loputonRaha = false;

    /**
     * Valitse tila, jonka mukaan kuvake valitaan grafiikkasäikeessä sekä
     * @param parannus kasvata hp:ta
     */

    public static void syöRuoka(int parannus) {
        paranna(parannus);
        syödytRuoat++;
        switch (syödytRuoat) {
            case 0:
                keimonKylläisyys = KeimonKylläisyys.LAIHA;
                break;
            case 1:
                keimonKylläisyys = KeimonKylläisyys.NORMAALI;
                break;
            case 2:
                keimonKylläisyys = KeimonKylläisyys.LIHAVA;
                break;
            case 3:
                keimonKylläisyys = KeimonKylläisyys.ERITTÄIN_LIHAVA;
                break;
            case 4:
                keimonKylläisyys = KeimonKylläisyys.YLENSYÖNTI;
                break;
            default:
                keimonKylläisyys = KeimonKylläisyys.LAIHA;
                break;
        }
    }

    public static void nollaaKylläisyys() {
        keimonKylläisyys = KeimonKylläisyys.LAIHA;
        syödytRuoat = 0;
    }

    /**
     * Siirrä pelaajan hitboxia (java.awt.Rectangle) nopeuden verran (pikseleissä) valittuun suuntaan.
     * Sen jälkeen päivitä pelaajan sijainti pelikentällä (tile) vastaamaan hitboxin keskipistettä lähinnä olevaa ruutua.
     * @param liikkuminen
     * @return siirtyikö pelaaja (valitussa suunnassa ei este tai kentän reuna)
     */

    static boolean siirry(Liikkuminen liikkuminen) {
        boolean pelaajaSiirtyi = false;
        float vinoNopeus = (float)(nopeus/Math.sqrt(2));
        if (liikkuminen instanceof LiikkuminenVasemmalle) {
            if ((int)hitbox.getMinX() > Peli.kentänAlaraja) {
                hitbox.setLocation((int)hitbox.getMinX() - nopeus, (int)hitbox.getMinY());
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenOikealle) {
            if ((int)hitbox.getMaxX() < Peli.kentänKoko * pelaajanKokoPx) {
                hitbox.setLocation((int)hitbox.getMinX() + nopeus, (int)hitbox.getMinY());
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenYlös) {
            if ((int)hitbox.getMinY() > Peli.kentänAlaraja) {
                hitbox.setLocation((int)hitbox.getMinX(), (int)hitbox.getMinY() - nopeus);
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenAlas) {
            if ((int)hitbox.getMaxY() < Peli.kentänKoko * pelaajanKokoPx) {
                hitbox.setLocation((int)hitbox.getMinX(), (int)hitbox.getMinY() + nopeus);
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenYläVasemmalle) {
            if ((int)hitbox.getMinX() > Peli.kentänAlaraja && (int)hitbox.getMinY() > Peli.kentänAlaraja) {
                hitbox.setLocation((int)(hitbox.getMinX() - vinoNopeus), (int)(hitbox.getMinY() - vinoNopeus));
                pelaajaSiirtyi = true;
            }
            else if ((int)hitbox.getMinX() > Peli.kentänAlaraja) {
                hitbox.setLocation((int)(hitbox.getMinX() - vinoNopeus), (int)hitbox.getY());
                pelaajaSiirtyi = true;
            }
            else if ((int)hitbox.getMinY() > Peli.kentänAlaraja) {
                hitbox.setLocation((int)hitbox.getX(), (int)(hitbox.getMinY() - vinoNopeus));
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenYläOikealle) {
            if ((int)hitbox.getMaxX() < Peli.kentänKoko * pelaajanKokoPx && (int)hitbox.getMinY() > Peli.kentänAlaraja) {
                hitbox.setLocation((int)(hitbox.getMinX() + vinoNopeus), (int)(hitbox.getMinY() - vinoNopeus));
                pelaajaSiirtyi = true;
            }
            else if ((int)hitbox.getMaxX() < Peli.kentänKoko * pelaajanKokoPx) {
                hitbox.setLocation((int)(hitbox.getMinX() + vinoNopeus), (int)hitbox.getY());
                pelaajaSiirtyi = true;
            }
            else if ((int)hitbox.getMinY() > Peli.kentänAlaraja) {
                hitbox.setLocation((int)hitbox.getX(), (int)(hitbox.getMinY() - vinoNopeus));
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenAlaVasemmalle) {
            if ((int)hitbox.getMinX() > Peli.kentänAlaraja && (int)hitbox.getMaxY() < Peli.kentänKoko * pelaajanKokoPx) {
                hitbox.setLocation((int)(hitbox.getMinX() - vinoNopeus), (int)(hitbox.getMinY() + vinoNopeus));
                pelaajaSiirtyi = true;
            }
            else if ((int)hitbox.getMinX() > Peli.kentänAlaraja) {
                hitbox.setLocation((int)(hitbox.getMinX() - vinoNopeus), (int)hitbox.getY());
                pelaajaSiirtyi = true;
            }
            else if ((int)hitbox.getMaxY() < Peli.kentänKoko * pelaajanKokoPx) {
                hitbox.setLocation((int)hitbox.getX(), (int)(hitbox.getMinY() + vinoNopeus));
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenAlaOikealle) {
            if ((int)hitbox.getMaxX() < Peli.kentänKoko * pelaajanKokoPx && (int)hitbox.getMaxY() < Peli.kentänKoko * pelaajanKokoPx) {
                hitbox.setLocation((int)(hitbox.getMinX() + vinoNopeus), (int)(hitbox.getMinY() + vinoNopeus));
                pelaajaSiirtyi = true;
            }
            else if ((int)hitbox.getMaxX() < Peli.kentänKoko * pelaajanKokoPx) {
                hitbox.setLocation((int)(hitbox.getMinX() + vinoNopeus), (int)hitbox.getY());
                pelaajaSiirtyi = true;
            }
            else if ((int)hitbox.getMaxY() < Peli.kentänKoko * pelaajanKokoPx) {
                hitbox.setLocation((int)hitbox.getX(), (int)(hitbox.getMinY() + vinoNopeus));
                pelaajaSiirtyi = true;
            }
        }
        sijX = (int)hitbox.getCenterX() / pelaajanKokoPx;
        sijY = (int)hitbox.getCenterY() / pelaajanKokoPx;
        return pelaajaSiirtyi;
    }

    /**
     * Tarkista, voiko valittuun suuntaan liikkua
     * Jos voi, kutsu siirry() -metodia
     * Tätä metodia toistetaan niin kauan, kun jokin nuolinäppäin on pohjassa
     * @param suunta liikkeen suunta
     * @return siirtyikö pelaaja (valitussa suunnassa ei este tai kentän reuna)
     */
     public static boolean kokeileLiikkumista(Suunta suunta) {
        return kokeileLiikkumista(suunta, true);
     }

     public static boolean kokeileLiikkumista(Suunta suunta, boolean känniLiike) {
        boolean pelaajaSiirtyi = false;
        boolean pelaajaVoiLiikkuaVasen = true;
        boolean pelaajaVoiLiikkuaOikea = true;
        boolean pelaajaVoiLiikkuaYlös = true;
        boolean pelaajaVoiLiikkuaAlas = true;
        float harhaliikkeenTodennäköisyys = känninVoimakkuusFloat/5 - 0.3f;
        int tarkistaVasen = (int)(hitbox.getMinX()-nopeus)/pelaajanKokoPx;
        int tarkistaOikea = (int)(hitbox.getMaxX())/pelaajanKokoPx;
        int tarkistaAlas = (int)(hitbox.getMaxY())/pelaajanKokoPx;
        int tarkistaYlös = (int)(hitbox.getMinY()-nopeus)/pelaajanKokoPx;
        Rectangle uusiSijainti = new Rectangle(64, 64);
        try {
            switch (suunta) {
                case VASEN -> {
                    uusiSijainti.setLocation((int)(hitbox.getMinX()-nopeus), (int)Pelaaja.hitbox.getY());
                }
                case OIKEA -> {
                    uusiSijainti.setLocation((int)(hitbox.getCenterX()), (int)Pelaaja.hitbox.getY());
                }
                case ALAS -> {
                    uusiSijainti.setLocation((int)Pelaaja.hitbox.getX(), (int)(hitbox.getCenterY()));
                }
                case YLÖS -> {
                    uusiSijainti.setLocation((int)Pelaaja.hitbox.getX(), (int)(hitbox.getMinY()-nopeus));
                }
                case YLÄVASEN -> {
                    uusiSijainti.setLocation((int)(hitbox.getMinX()-nopeus), (int)(hitbox.getMinY()-nopeus));
                }
                case YLÄOIKEA -> {
                    uusiSijainti.setLocation((int)(hitbox.getCenterX()), (int)(hitbox.getMinY()-nopeus));
                }
                case ALAVASEN -> {
                    uusiSijainti.setLocation((int)(hitbox.getMinX()-nopeus), (int)(hitbox.getCenterY()));
                }
                case ALAOIKEA -> {
                    uusiSijainti.setLocation((int)(hitbox.getCenterX()), (int)(hitbox.getCenterY()));
                }
            }
            if (
                tarkistaVasen >= 0 &&
                tarkistaOikea < Peli.annaMaastoKenttä().length &&
                tarkistaYlös >= 0 &&
                tarkistaAlas < Peli.annaMaastoKenttä().length
            ) {
                if (Peli.annaMaastoKenttä()[tarkistaVasen][sijY] != null) {
                    if (uusiSijainti.intersects(Peli.annaMaastoKenttä()[tarkistaVasen][sijY].hitbox) && Peli.annaMaastoKenttä()[tarkistaVasen][sijY].estääköLiikkumisen(suunta)) {
                        pelaajaVoiLiikkuaVasen = false;
                    }
                }
                if (Peli.annaMaastoKenttä()[tarkistaOikea][sijY] != null) {
                    if (uusiSijainti.intersects(Peli.annaMaastoKenttä()[tarkistaOikea][sijY].hitbox) && Peli.annaMaastoKenttä()[tarkistaOikea][sijY].estääköLiikkumisen(suunta)) {
                        pelaajaVoiLiikkuaOikea = false;
                    }
                }
                // if (Peli.annaMaastoKenttä()[tarkistaVasen][tarkistaAlas] != null) {
                //     if (uusiSijainti.intersects(Peli.annaMaastoKenttä()[tarkistaVasen][tarkistaAlas].hitbox) && Peli.annaMaastoKenttä()[tarkistaVasen][tarkistaAlas].estääköLiikkumisen(suunta)) {
                //         pelaajaVoiLiikkuaVasen = false;
                //         pelaajaVoiLiikkuaAlas = false;
                //     }
                // }
                // if (Peli.annaMaastoKenttä()[tarkistaOikea][tarkistaAlas] != null) {
                //     if (uusiSijainti.intersects(Peli.annaMaastoKenttä()[tarkistaOikea][tarkistaAlas].hitbox) && Peli.annaMaastoKenttä()[tarkistaOikea][tarkistaAlas].estääköLiikkumisen(suunta)) {
                //         pelaajaVoiLiikkuaOikea = false;
                //         pelaajaVoiLiikkuaAlas = false;
                //     }
                // }
                // if (Peli.annaMaastoKenttä()[tarkistaVasen][tarkistaYlös] != null) {
                //     if (uusiSijainti.intersects(Peli.annaMaastoKenttä()[tarkistaVasen][tarkistaYlös].hitbox) && Peli.annaMaastoKenttä()[tarkistaVasen][tarkistaYlös].estääköLiikkumisen(suunta)) {
                //         pelaajaVoiLiikkuaVasen = false;
                //         pelaajaVoiLiikkuaYlös = false;
                //     }
                // }
                // if (Peli.annaMaastoKenttä()[tarkistaOikea][tarkistaYlös] != null) {
                //     if (uusiSijainti.intersects(Peli.annaMaastoKenttä()[tarkistaOikea][tarkistaYlös].hitbox) && Peli.annaMaastoKenttä()[tarkistaOikea][tarkistaYlös].estääköLiikkumisen(suunta)) {
                //         pelaajaVoiLiikkuaOikea = false;
                //         pelaajaVoiLiikkuaYlös = false;
                //     }
                // }
                if (Peli.annaMaastoKenttä()[sijX][tarkistaAlas] != null) {
                    if (uusiSijainti.intersects(Peli.annaMaastoKenttä()[sijX][tarkistaAlas].hitbox) && Peli.annaMaastoKenttä()[sijX][tarkistaAlas].estääköLiikkumisen(suunta)) {
                        pelaajaVoiLiikkuaAlas = false;
                    }
                }
                if (Peli.annaMaastoKenttä()[sijX][tarkistaYlös] != null) {
                    if (uusiSijainti.intersects(Peli.annaMaastoKenttä()[sijX][tarkistaYlös].hitbox) && Peli.annaMaastoKenttä()[sijX][tarkistaYlös].estääköLiikkumisen(suunta)) {
                        pelaajaVoiLiikkuaYlös = false;
                    }
                }
                for (Maasto[] mm : Peli.annaMaastoKenttä()) {
                    for (Maasto m : mm) {
                        if (m instanceof IsoLaatta) {
                            if (uusiSijainti.intersects(m.hitbox)) {
                                if (uusiSijainti.getCenterX() > m.hitbox.getCenterX()) {
                                    pelaajaVoiLiikkuaVasen = false;
                                }
                                else if (uusiSijainti.getCenterX() < m.hitbox.getCenterX()) {
                                    pelaajaVoiLiikkuaOikea = false;
                                }
                                if (uusiSijainti.getCenterY() > m.hitbox.getCenterY()) {
                                    pelaajaVoiLiikkuaYlös = false;
                                }
                                else if (uusiSijainti.getCenterY() < m.hitbox.getCenterY()) {
                                    pelaajaVoiLiikkuaAlas = false;
                                }
                            }
                        }
                    }
                }
            }
            if (
                tarkistaVasen >= 0 &&
                tarkistaOikea < Peli.annaObjektiKenttä().length &&
                tarkistaYlös >= 0 &&
                tarkistaAlas < Peli.annaObjektiKenttä().length
            ) {
                if (Peli.annaObjektiKenttä()[tarkistaVasen][sijY] != null) {
                    if (uusiSijainti.intersects(Peli.annaObjektiKenttä()[tarkistaVasen][sijY].hitbox) && Peli.annaObjektiKenttä()[tarkistaVasen][sijY].onkoEste()) {
                        pelaajaVoiLiikkuaVasen = false;
                    }
                }
                if (Peli.annaObjektiKenttä()[tarkistaOikea][sijY] != null) {
                    if (uusiSijainti.intersects(Peli.annaObjektiKenttä()[tarkistaOikea][sijY].hitbox) && Peli.annaObjektiKenttä()[tarkistaOikea][sijY].onkoEste()) {
                        pelaajaVoiLiikkuaOikea = false;
                    }
                }
                // if (Peli.annaObjektiKenttä()[tarkistaVasen][tarkistaAlas] != null) {
                //     if (uusiSijainti.intersects(Peli.annaObjektiKenttä()[tarkistaVasen][tarkistaAlas].hitbox) && Peli.annaObjektiKenttä()[tarkistaVasen][tarkistaAlas].onkoEste()) {
                //         pelaajaVoiLiikkuaVasen = false;
                //         pelaajaVoiLiikkuaAlas = false;
                //     }
                // }
                // if (Peli.annaObjektiKenttä()[tarkistaOikea][tarkistaAlas] != null) {
                //     if (uusiSijainti.intersects(Peli.annaObjektiKenttä()[tarkistaOikea][tarkistaAlas].hitbox) && Peli.annaObjektiKenttä()[tarkistaOikea][tarkistaAlas].onkoEste()) {
                //         pelaajaVoiLiikkuaOikea = false;
                //         pelaajaVoiLiikkuaAlas = false;
                //     }
                // }
                // if (Peli.annaObjektiKenttä()[tarkistaVasen][tarkistaYlös] != null) {
                //     if (uusiSijainti.intersects(Peli.annaObjektiKenttä()[tarkistaVasen][tarkistaYlös].hitbox) && Peli.annaObjektiKenttä()[tarkistaVasen][tarkistaYlös].onkoEste()) {
                //         pelaajaVoiLiikkuaVasen = false;
                //         pelaajaVoiLiikkuaYlös = false;
                //     }
                // }
                // if (Peli.annaObjektiKenttä()[tarkistaOikea][tarkistaYlös] != null) {
                //     if (uusiSijainti.intersects(Peli.annaObjektiKenttä()[tarkistaOikea][tarkistaYlös].hitbox) && Peli.annaObjektiKenttä()[tarkistaOikea][tarkistaYlös].onkoEste()) {
                //         pelaajaVoiLiikkuaOikea = false;
                //         pelaajaVoiLiikkuaYlös = false;
                //     }
                // }
                if (Peli.annaObjektiKenttä()[sijX][tarkistaAlas] != null) {
                    if (uusiSijainti.intersects(Peli.annaObjektiKenttä()[sijX][tarkistaAlas].hitbox) && Peli.annaObjektiKenttä()[sijX][tarkistaAlas].onkoEste()) {
                        pelaajaVoiLiikkuaAlas = false;
                    }
                }
                if (Peli.annaObjektiKenttä()[sijX][tarkistaYlös] != null) {
                    if (uusiSijainti.intersects(Peli.annaObjektiKenttä()[sijX][tarkistaYlös].hitbox) && Peli.annaObjektiKenttä()[sijX][tarkistaYlös].onkoEste()) {
                        pelaajaVoiLiikkuaYlös = false;
                    }
                }
            }
            else {
                pelaajaVoiLiikkuaVasen = false;
                if (tarkistaVasen >= 0) {
                    if (Peli.annaMaastoKenttä()[tarkistaVasen][sijY] != null) {
                        if (!Peli.annaMaastoKenttä()[tarkistaVasen][sijY].estääköLiikkumisen(suunta)) {
                            pelaajaVoiLiikkuaVasen = true;
                        }
                    }
                }
                pelaajaVoiLiikkuaOikea = false;
                if (tarkistaOikea < Peli.annaMaastoKenttä().length) {
                    if (Peli.annaMaastoKenttä()[tarkistaOikea][sijY] != null) {
                        if (!Peli.annaMaastoKenttä()[tarkistaOikea][sijY].estääköLiikkumisen(suunta)) {
                            pelaajaVoiLiikkuaOikea = true;
                        }
                    }
                }
                pelaajaVoiLiikkuaAlas = false;
                if (tarkistaAlas < Peli.annaMaastoKenttä().length) {
                    if (Peli.annaMaastoKenttä()[sijX][tarkistaAlas] != null) {
                        if (!Peli.annaMaastoKenttä()[sijX][tarkistaAlas].estääköLiikkumisen(suunta)) {
                            pelaajaVoiLiikkuaAlas = true;
                        }
                    }
                }
                pelaajaVoiLiikkuaYlös = false;
                if (tarkistaYlös >= 0) {
                    if (Peli.annaMaastoKenttä()[sijX][tarkistaYlös] != null) {
                        if (!Peli.annaMaastoKenttä()[sijX][tarkistaYlös].estääköLiikkumisen(suunta)) {
                            pelaajaVoiLiikkuaYlös = true;
                        }
                    }
                }
            }
            if (Pelaaja.noclip) {
                pelaajaVoiLiikkuaVasen = true;
                pelaajaVoiLiikkuaOikea = true;
                pelaajaVoiLiikkuaYlös = true;
                pelaajaVoiLiikkuaAlas = true;
            }

            switch (suunta) {
                case YLÄVASEN -> {
                    if (pelaajaVoiLiikkuaVasen && pelaajaVoiLiikkuaYlös) {
                        pelaajaSiirtyi = siirry(new LiikkuminenYläVasemmalle());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.YLÖS, false);
                            else kokeileLiikkumista(Suunta.ALAS, false);
                        }
                    }
                    else if (pelaajaVoiLiikkuaVasen) {
                        pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.YLÖS, false);
                            else kokeileLiikkumista(Suunta.ALAS, false);
                        }
                    }
                    else if (pelaajaVoiLiikkuaYlös) {
                        pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.VASEN, false);
                            else kokeileLiikkumista(Suunta.OIKEA, false);
                        }
                    }
                }
                case YLÄOIKEA -> {
                    if (pelaajaVoiLiikkuaOikea && pelaajaVoiLiikkuaYlös) {
                        pelaajaSiirtyi = siirry(new LiikkuminenYläOikealle());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.YLÖS, false);
                            else kokeileLiikkumista(Suunta.ALAS, false);
                        }
                    }
                    else if (pelaajaVoiLiikkuaOikea) {
                        pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.YLÖS, false);
                            else kokeileLiikkumista(Suunta.ALAS, false);
                        }
                    }
                    else if (pelaajaVoiLiikkuaYlös) {
                        pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.VASEN, false);
                            else kokeileLiikkumista(Suunta.OIKEA, false);
                        }
                    }
                }
                case ALAVASEN -> {
                    if (pelaajaVoiLiikkuaVasen && pelaajaVoiLiikkuaAlas) {
                        pelaajaSiirtyi = siirry(new LiikkuminenAlaVasemmalle());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.YLÖS, false);
                            else kokeileLiikkumista(Suunta.ALAS, false);
                        }
                    }
                    else if (pelaajaVoiLiikkuaVasen) {
                        pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.YLÖS, false);
                            else kokeileLiikkumista(Suunta.ALAS, false);
                        }
                    }
                    else if (pelaajaVoiLiikkuaAlas) {
                        pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.VASEN, false);
                            else kokeileLiikkumista(Suunta.OIKEA, false);
                        }
                    }
                }
                case ALAOIKEA -> {
                    if (pelaajaVoiLiikkuaOikea && pelaajaVoiLiikkuaAlas) {
                        pelaajaSiirtyi = siirry(new LiikkuminenAlaOikealle());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.YLÖS, false);
                            else kokeileLiikkumista(Suunta.ALAS, false);
                        }
                    }
                    else if (pelaajaVoiLiikkuaOikea) {
                        pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.YLÖS, false);
                            else kokeileLiikkumista(Suunta.ALAS, false);
                        }
                    }
                    else if (pelaajaVoiLiikkuaAlas) {
                        pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.VASEN, false);
                            else kokeileLiikkumista(Suunta.OIKEA, false);
                        }
                    }
                }
                case VASEN -> {
                    if (pelaajaVoiLiikkuaVasen) {
                        pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.YLÖS, false);
                            else kokeileLiikkumista(Suunta.ALAS, false);
                        }
                    }
                }
                case OIKEA -> {
                    if (pelaajaVoiLiikkuaOikea) {
                        pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.YLÖS, false);
                            else kokeileLiikkumista(Suunta.ALAS, false);
                        }
                    }
                }
                case YLÖS -> {
                    if (pelaajaVoiLiikkuaYlös) {
                        pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.VASEN, false);
                            else kokeileLiikkumista(Suunta.OIKEA, false);
                        }
                    }
                }
                case ALAS -> {
                    if (pelaajaVoiLiikkuaAlas) {
                        pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                        if (harhaliikkeenTodennäköisyys > Math.random() && känniLiike) {
                            if (r.nextBoolean()) kokeileLiikkumista(Suunta.VASEN, false);
                            else kokeileLiikkumista(Suunta.OIKEA, false);
                        }
                    }
                }
            }
        }
        catch (NullPointerException npe) {
            System.out.println("Ongelma liikkeessä! Viimeisin pelaajan liike perutaan.");
            npe.printStackTrace();
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            System.out.println("Ongelma liikkeessä! Viimeisin pelaajan liike perutaan (kentän ulkopuolella).");
            aioobe.printStackTrace();
        }
        pelaajaLiikkuu = pelaajaSiirtyi;
        return pelaajaSiirtyi;
    }

    public static void annaEsine(Esine e) {
        if (e != null) {
            if (annaEsineidenMäärä() < annaTavaraluettelonKoko()) {
                for (int i = 0; i < esineet.length; i++) {
                    if (esineet[i] == null) {
                        esineet[i] = e;
                        break;
                    }
                }
            }
            Peli.valittuEsine = esineet[Peli.esineValInt];
        }
    }

    public static KeimonState keimonState = KeimonState.IDLE;
    public enum KeimonState {
        IDLE,
        JUOKSU,
        KUOLLUT;

        @Override
        public String toString() {
            char x = this.name().charAt(0);
            String uusiNimi = x + this.name().substring(1).toLowerCase();
            return uusiNimi;
        }
    }

    public static KeimonKylläisyys keimonKylläisyys = KeimonKylläisyys.LAIHA;
    public enum KeimonKylläisyys {
        LAIHA,
        NORMAALI,
        LIHAVA,
        ERITTÄIN_LIHAVA,
        YLENSYÖNTI;

        @Override
        public String toString() {
            char x = this.name().charAt(0);
            String uusiNimi = x + this.name().substring(1).toLowerCase();
            if (uusiNimi.contains("_")) {
                uusiNimi.replace("_", " ");
            }
            return uusiNimi;
        }
    }

    public static KeimonTerveys keimonTerveys = KeimonTerveys.OK;
    public enum KeimonTerveys {
        HUONO,
        OK,
        HYVÄ,
        ÜBER;

        @Override
        public String toString() {
            char x = this.name().charAt(0);
            String uusiNimi = x + this.name().substring(1).toLowerCase();
            return uusiNimi;
        }
    }

    public static Suunta keimonSuunta = Suunta.ALAS;
    public static SuuntaVasenOikea keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;

    /**
     * Pakottaa pelaaja pysähtymään. Hyödyllinen tapauksissa, joissa näppäimet voivat jäädä jumiin.
     */
    public static void pakotaPelaajanPysäytys() {
        pelaajaLiikkuuVasen = false;
        pelaajaLiikkuuOikea = false;
        pelaajaLiikkuuYlös = false;
        pelaajaLiikkuuAlas = false;
        keimonState = KeimonState.IDLE;
    }
    
    public static boolean pelaajaLiikkuuVasen = false;
    public static boolean pelaajaLiikkuuOikea = false;
    public static boolean pelaajaLiikkuuYlös = false;
    public static boolean pelaajaLiikkuuAlas = false;

    /**
     * Vaihda pelaajan sijainti valittuun ruutuun (oviruutuja varten)
     * Päivitä hitbox ruudun kohdalle
     * @param kohdeX
     * @param kohdeY
     */

    public static void teleport(int kohdeX, int kohdeY) {
        sijX = kohdeX;
        sijY = kohdeY;
        hitbox.setLocation(sijX * pelaajanKokoPx, sijY * pelaajanKokoPx);
    }

    public static void teleporttaaLähimpäänTurvalliseenKohtaan(int alkuX, int alkuY) {
        boolean kenttäTurvallinen = false;
        boolean maastoTurvallinen = false;
        for (int y = alkuY; y < Peli.kentänKoko; y++) {
            for (int x = alkuX; x < Peli.kentänKoko; x++) {
                if (Peli.maastokenttä[x][y] != null) {
                    if (Peli.maastokenttä[x][y].estääköLiikkumisen(null)) {
                        maastoTurvallinen = false;
                    }
                    else maastoTurvallinen = true;
                }
                if (Peli.pelikenttä[x][y] instanceof VisuaalinenObjekti) {
                    VisuaalinenObjekti vo = (VisuaalinenObjekti)Peli.pelikenttä[x][y];
                    if (vo.onkoEste()) {
                        kenttäTurvallinen = false;
                    }
                    else kenttäTurvallinen = true;
                }
                else kenttäTurvallinen = true;

                if (kenttäTurvallinen && maastoTurvallinen) {
                    teleport(x, y);
                    return;
                }
            }
        }
        if (alkuX == 0 && alkuY == 0) {
            Dialogit.avaaDialogi("", "Huoneessa ei ole turvallisia ruutuja", "Turvallinen teleporttaus epäonnistui");
        }
        else {
            teleporttaaLähimpäänTurvalliseenKohtaan(0, 0);
        }
    }

    public static void teleporttaaSpawniin() {
        sijX = alkuSijX;
        sijY = alkuSijY;
        hitbox.setLocation(sijX * pelaajanKokoPx, sijY * pelaajanKokoPx);
    }

    public static int annaEsineidenMäärä() {
        int määrä = 0;
        for (int i = 0; i < esineet.length; i++) {
            if (esineet[i] != null) {
                määrä++;
            }
        }
        return määrä;
    }
    public static int annaTavaraluettelonKoko() {
        return esineet.length;
    }

    int annaHp() {
        return hp;
    }

    static void vahingoita(int määrä) {
        hp -= määrä;
        kuolemattomuusAika = 120;
        Äänet.toistaSFX("pelaaja_damage");
        päivitäTerveys();
    }
        
    static void paranna(int määrä) {
        hp += määrä;
        päivitäTerveys();
    }

    public static void päivitäTerveys() {
        if (hp >= 30) {
            keimonTerveys = KeimonTerveys.ÜBER;
        }
        else if (hp >= 20) {
            keimonTerveys = KeimonTerveys.HYVÄ;
        }
        else if (hp < 6) {
            keimonTerveys = KeimonTerveys.HUONO;
        }
        else {
            keimonTerveys = KeimonTerveys.OK;
        }
    }

    public static void vähennäKuolemattomuusAikaa() {
        if (kuolemattomuusAika > 0) {
            kuolemattomuusAika--;
        }
        if (känniKuolemattomuus > 0) {
            känniKuolemattomuus--;
        }
    }

    public static void vähennäReaktioAikaa() {
        if (reaktioAika > 0) {
            reaktioAika--;
        }
    }

    public static void vähennäHyökkäysAikaa() {
        if (hyökkäysAika > 0) {
            hyökkäysAika--;
        }
        if (käyttöViive > 0) {
            käyttöViive--;
        }
    }

    public static String lisääOstosKoriin(Esine e) {
        int tyhjätPaikat = 0;
        for (Esine esine : esineet) {
            if (esine == null) {
                tyhjätPaikat++;
            }
        }
        if (ostosKori.size() >= tyhjätPaikat) {
            return "Ostoskoriin ei voi lisätä enempää tavaraa kuin tavaraluettelossa on tyhjiä paikkoja!";
        }
        else if (e != null) {
            ostosKori.add(e);
            nopeus = Math.round((8 - Pelaaja.ostosKori.size()) * pelaajanKokoPx / 64f);
            return "Ostoskoriin lisättiin " + e.annaNimi() + " (+ " + e.annaHinta() + "€)";
        }
        else {
            return "tyhjä hylly";
        }
    }

    public static void tyhjennäOstoskori() {
        ostosKori.removeAll(ostosKori);
        ostostenHintaYhteensä = 0;
        nopeus = Math.round((8 - Pelaaja.ostosKori.size()) * pelaajanKokoPx / 64f);
    }

    public Pelaaja() {
        hp = Peli.aloitusHp;
        raha = 0;
        kuparit = 0;
        syödytRuoat = 0;
        nopeus = 8;
        for (int i = 0; i < esineet.length; i++) esineet[i] = null;
        kuvake = new ImageIcon("tiedostot/kuvat/keimo_idle.gif");
        keimonState = KeimonState.IDLE;
        keimonKylläisyys = KeimonKylläisyys.LAIHA;
        keimonTerveys = KeimonTerveys.OK;
        sijX = alkuSijX;
        sijY = alkuSijY;
        pelaajaLiikkuuVasen = false;
        pelaajaLiikkuuOikea = false;
        pelaajaLiikkuuAlas = false;
        pelaajaLiikkuuYlös = false;
        kuolemattomuusAika = 0;
        känniKuolemattomuus = 0;
        reaktioAika = 0;
        känninVoimakkuusFloat = 0;
        ostostenHintaYhteensä = 0;
        ostosKori.removeAll(ostosKori);
    }
}
