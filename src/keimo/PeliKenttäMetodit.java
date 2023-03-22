package keimo;
import javax.swing.Timer;

import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;
import keimo.NPCt.*;
import keimo.TarkistettavatArvot.PelinLopetukset;

public class PeliKenttäMetodit {
    
    static KenttäKohde[][] pelikenttä;
    static Maasto[][] maastokenttä;

    // static Timer päivitysTiheys = new Timer(300, e -> {
    //     if (!Peli.pause) {
    //         suoritaPelikenttäMetodit();
    //     }
    // });

    static void suoritaPelikenttäMetodit() {
        kopioiPelikenttä();
        nollaaVihollistenTila();
        liikutaVihollisiaJatkuvaSilmukka();
        liikutaVihollisiaEsteeseenAsti();
        asetaPelikenttä();
    }

    static void suoritaPelikenttäMetoditNopea() {
        liikutaVihollisiaPikseliliikeJatkuvaSilmukka();
        Pelaaja.vähennäKuolemattomuusAikaa();
    }

    static void nollaaVihollistenTila() {
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                if (pelikenttä[j][i] instanceof Vihollinen_KenttöKohde) {
                    Vihollinen_KenttöKohde vihollinen = (Vihollinen_KenttöKohde)pelikenttä[j][i];
                    vihollinen.onJoLiikutettu = false;
                }
            }
        }
    }

    static boolean liikutaVihollisiaJatkuvaSilmukka() {
        boolean vihollinenLiikutettiin = false;
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                if (pelikenttä[j][i] instanceof Vihollinen_KenttöKohde) {
                    if (pelikenttä[j][i] instanceof PikkuVihu_KenttäKohde) {
                        Vihollinen_KenttöKohde vihollinen = (Vihollinen_KenttöKohde)pelikenttä[j][i];
                        if (!vihollinen.onJoLiikutettu && !vihollinen.onkoKukistettu()) {
                            String suunta = vihollinen.liikeSuuntaLoop[vihollinen.seuraavaLiikesuunta % vihollinen.liikeSuuntaLoop.length];
                            if (liikuta(j, i, suunta)) {
                                vihollinen.onJoLiikutettu = true;
                                if ((Pelaaja.sijX == j ) && (Pelaaja.sijY == i) && (!vihollinen.onkoKukistettu()) && (Pelaaja.kuolemattomuusAika <= 0)) {
                                    Pelaaja.vahingoita(vihollinen.vahinko);
                                }
                            }
                            vihollinen.seuraavaLiikesuunta++;
                        }
                    }
                }
            }
        }
        return vihollinenLiikutettiin;
    }

    //static int loopinVaihe = 0;
    static boolean liikutaVihollisiaPikseliliikeJatkuvaSilmukka() {
        boolean vihollinenLiikutettiin = false;
        boolean pelaajanKohdallaVihollinen = false;
        if (Peli.npcLista != null) {
            for (NPC npc : Peli.npcLista) {
                if (npc instanceof Vihollinen) {
                    Vihollinen vihollinen = (Vihollinen)npc;
                    if (!vihollinen.onkoKukistettu()) {
                        switch (vihollinen.liikeTapa) {
                            case LOOP_NELIÖ_MYÖTÄPÄIVÄÄN:
                                if (vihollinen.liikuVielä > 0) {
                                    vihollinen.siirrä(vihollinen.liikeSuuntaLoopNeliöMyötäpäivään[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopNeliöMyötäpäivään.length]);
                                    vihollinen.liikuVielä--;
                                }
                                else {
                                    vihollinen.liikeLoopinVaihe++;
                                    vihollinen.liikuVielä = vihollinen.liikkeenPituus;
                                }
                                break;
                            case LOOP_NELIÖ_VASTAPÄIVÄÄN:
                                if (vihollinen.liikuVielä > 0) {
                                    vihollinen.siirrä(vihollinen.liikeSuuntaLoopNeliöVastapäivään[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopNeliöVastapäivään.length]);
                                    vihollinen.liikuVielä--;
                                }
                                else {
                                    vihollinen.liikeLoopinVaihe++;
                                    vihollinen.liikuVielä = vihollinen.liikkeenPituus;
                                }
                                break;
                        }
                        if (Pelaaja.hitbox.intersects(vihollinen.hitbox)) {
                            pelaajanKohdallaVihollinen = true;
                            System.out.println("collision - pelaaja: " + Pelaaja.hitbox.getMinX() + " - " + Pelaaja.hitbox.getMaxX() + ", " + Pelaaja.hitbox.getMinY() + " - " + Pelaaja.hitbox.getMaxY()  + ", vihollinen: " + vihollinen.hitbox.getMinX() + " - " + vihollinen.hitbox.getMaxX() + ", " + vihollinen.hitbox.getMinY() + " - " + vihollinen.hitbox.getMaxY());
                            if (Pelaaja.reaktioAika <= 0) {
                                if (Pelaaja.kuolemattomuusAika <= 0) {
                                    if (Pelaaja.esineet[Peli.esineValInt] instanceof Kilpi && vihollinen.kilpiTehoaa) {

                                    }
                                    else {
                                        TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN;
                                        Pelaaja.vahingoita(vihollinen.vahinko);
                                    }
                                }
                            }
                            //else {
                            //    Pelaaja.vähennäReaktioAikaa();
                            //}
                            //Pelaaja.vihollisenKohdalla = true;
                            //Pelaaja.vihollinenKohdalla = vihollinen;
                            //System.out.println("reaktioaika: " + Pelaaja.reaktioAika);
                        }
                        //else {
                        //    Pelaaja.vihollisenKohdalla = false;
                        //    Pelaaja.vihollinenKohdalla = null;
                        //    Pelaaja.reaktioAika = 10;
                        //}
                        //System.out.println("Vihollisen kohdalla: " + (Pelaaja.vihollisenKohdalla ? "Kyllä" : "Ei"));
                    }
                }
            }
            if (pelaajanKohdallaVihollinen) {
                Pelaaja.vähennäReaktioAikaa();
            }
            int leikkaavatVihollisHitboxit = 0;
            for (NPC npc : Peli.npcLista) {
                if (npc instanceof Vihollinen) {
                    Vihollinen vihollinen = (Vihollinen)npc;
                    if (Pelaaja.hitbox.intersects(vihollinen.hitbox)) {
                        leikkaavatVihollisHitboxit++;
                        Pelaaja.vihollisenKohdalla = true;
                        Pelaaja.vihollinenKohdalla = vihollinen;
                    }
                }
            }
            if (leikkaavatVihollisHitboxit == 0) {
                pelaajanKohdallaVihollinen = false;
                Pelaaja.reaktioAika = 8;
                Pelaaja.vihollisenKohdalla = false;
                Pelaaja.vihollinenKohdalla = null;
            }
            else {
                pelaajanKohdallaVihollinen = true;
            }
        }
        return vihollinenLiikutettiin;
    }

    static boolean liikutaVihollisiaEsteeseenAsti() {
        boolean vihollinenLiikutettiin = false;
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                if (pelikenttä[j][i] instanceof Vihollinen_KenttöKohde) {
                    if (pelikenttä[j][i] instanceof PahaVihu) {
                        Vihollinen_KenttöKohde vihollinen = (Vihollinen_KenttöKohde)pelikenttä[j][i];
                        if (!vihollinen.onJoLiikutettu && !vihollinen.onkoKukistettu()) {
                            String suunta = vihollinen.liikeSuuntaLoop[vihollinen.seuraavaLiikesuunta % vihollinen.liikeSuuntaLoop.length];
                            if (liikuta(j, i, suunta)) {
                                vihollinen.onJoLiikutettu = true;
                                if ((Pelaaja.sijX == j ) && (Pelaaja.sijY == i)) {
                                    Pelaaja.vahingoita(vihollinen.vahinko);
                                }
                                return true;
                            }
                            vihollinen.seuraavaLiikesuunta++;
                        }
                    }
                }
            }
        }
        return vihollinenLiikutettiin;
    }

    static boolean liikuta(int objSijX, int objSijY, String suunta) {
        boolean kohdeLiikutettiin = false;
        if (pelikenttä[objSijX][objSijY] == null) {
            return false;
        }
        else {
            switch (suunta) {
                case "vasen":
                    if (objSijX > 0) {
                        if (pelikenttä[objSijX-1][objSijY] == null && !(maastokenttä[objSijX-1][objSijY] instanceof EsteTile)) {
                            KenttäKohde k = pelikenttä[objSijX][objSijY];
                            pelikenttä[objSijX-1][objSijY] = k;
                            pelikenttä[objSijX][objSijY] = null;
                            return true;
                        }
                    }
                    break;
                case "oikea":
                    if (objSijX < Peli.kentänKoko -1) {
                        if (pelikenttä[objSijX+1][objSijY] == null && !(maastokenttä[objSijX+1][objSijY] instanceof EsteTile)) {
                            KenttäKohde k = pelikenttä[objSijX][objSijY];
                            pelikenttä[objSijX+1][objSijY] = k;
                            pelikenttä[objSijX][objSijY] = null;
                            return true;
                        }
                    }
                    break;
                case "alas":
                    if (objSijY < Peli.kentänKoko -1) {
                        if (pelikenttä[objSijX][objSijY+1] == null && !(maastokenttä[objSijX][objSijY+1] instanceof EsteTile)) {
                            KenttäKohde k = pelikenttä[objSijX][objSijY];
                            pelikenttä[objSijX][objSijY+1] = k;
                            pelikenttä[objSijX][objSijY] = null;
                            return true;
                        }
                    }
                    break;
                case "ylös":
                    if (objSijY > 0) {
                        if (pelikenttä[objSijX][objSijY-1] == null && !(maastokenttä[objSijX][objSijY-1] instanceof EsteTile)) {
                            KenttäKohde k = pelikenttä[objSijX][objSijY];
                            pelikenttä[objSijX][objSijY-1] = k;
                            pelikenttä[objSijX][objSijY] = null;
                            return true;
                        }
                    }
                    break;
                default:
                    return false;
            }
        }
        return kohdeLiikutettiin;
    }

    static void kopioiPelikenttä() {
        pelikenttä = Peli.pelikenttä;
        maastokenttä = Peli.maastokenttä;
    }

    static void asetaPelikenttä() {
        Peli.pelikenttä = pelikenttä;
        Peli.maastokenttä = maastokenttä;
    }
}
