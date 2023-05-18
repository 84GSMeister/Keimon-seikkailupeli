package keimo;

import java.util.ConcurrentModificationException;

import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;
import keimo.NPCt.*;
import keimo.TarkistettavatArvot.PelinLopetukset;

public class PeliKenttäMetodit {
    
    static KenttäKohde[][] pelikenttä;
    static Maasto[][] maastokenttä;

    public static void suoritaPelikenttäMetodit() {

    }

    public static void suoritaPelikenttäMetoditJoka2Tick() {
        liikutaVihollisia();
    }

    public static void suoritaPelikenttäMetoditJokaTick() {
        tarkistaVihollisCollision();
        Pelaaja.vähennäKuolemattomuusAikaa();
        Peli.vähennäKäyttöViivettä();
    }

    static boolean liikutaVihollisia() {
        boolean vihollinenLiikutettiin = false;
        //boolean pelaajanKohdallaVihollinen = false;
        try {
            if (Peli.npcLista != null) {
                for (NPC npc : Peli.npcLista) {
                    if (npc instanceof Vihollinen) {
                        Vihollinen vihollinen = (Vihollinen)npc;
                        if (!vihollinen.onkoKukistettu()) {
                            vihollinen.valitseKuvake();
                            switch (vihollinen.liikeTapa) {
                                case LOOP_NELIÖ_MYÖTÄPÄIVÄÄN:
                                    if (vihollinen.liikuVielä > 0) {
                                        vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopNeliöMyötäpäivään[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopNeliöMyötäpäivään.length]);
                                        vihollinen.liikuVielä--;
                                    }
                                    else {
                                        vihollinen.liikeLoopinVaihe++;
                                        vihollinen.liikuVielä = vihollinen.liikkeenPituus;
                                    }
                                break;
                                case LOOP_NELIÖ_VASTAPÄIVÄÄN:
                                    if (vihollinen.liikuVielä > 0) {
                                        vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopNeliöVastapäivään[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopNeliöVastapäivään.length]);
                                        vihollinen.liikuVielä--;
                                    }
                                    else {
                                        vihollinen.liikeLoopinVaihe++;
                                        vihollinen.liikuVielä = vihollinen.liikkeenPituus;
                                    }
                                break;
                                case LOOP_VASEN_OIKEA:
                                    if (vihollinen.liikuVielä > 0) {
                                        vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopVasenOikea[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopVasenOikea.length]);
                                        vihollinen.liikuVielä--;
                                    }
                                    else {
                                        vihollinen.liikeLoopinVaihe++;
                                        vihollinen.liikuVielä = vihollinen.liikkeenPituus;
                                    }
                                break;
                                case LOOP_YLÖS_ALAS:
                                    if (vihollinen.liikuVielä > 0) {
                                        vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopYlösAlas[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopVasenOikea.length]);
                                        vihollinen.liikuVielä--;
                                    }
                                    else {
                                        vihollinen.liikeLoopinVaihe++;
                                        vihollinen.liikuVielä = vihollinen.liikkeenPituus;
                                    }
                                break;
                                case NELIÖ_MYÖTÄPÄIVÄÄN_ESTEESEEN_ASTI:
                                    if (vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopNeliöMyötäpäivään[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopNeliöMyötäpäivään.length])) {

                                    }
                                    else {
                                        vihollinen.liikeLoopinVaihe++;
                                        vihollinen.liikuVielä = vihollinen.liikkeenPituus;
                                    }
                                break;
                                case NELIÖ_VASTAPÄIVÄÄN_ESTEESEEN_ASTI:
                                    if (vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopNeliöVastapäivään[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopNeliöVastapäivään.length])) {
                                        
                                    }
                                    else {
                                        vihollinen.liikeLoopinVaihe++;
                                        vihollinen.liikuVielä = vihollinen.liikkeenPituus;
                                    }
                                break;
                                case VASEN_OIKEA_ESTEESEEN_ASTI:
                                    if (vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopVasenOikea[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopVasenOikea.length])) {
                                        ;
                                    }
                                    else {
                                        vihollinen.liikeLoopinVaihe++;
                                        vihollinen.liikuVielä = vihollinen.liikkeenPituus;
                                    }
                                break;
                                case YLÖS_ALAS_ESTEESEEN_ASTI:
                                    if (vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopYlösAlas[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopVasenOikea.length])) {
                                        ;
                                    }
                                    else {
                                        vihollinen.liikeLoopinVaihe++;
                                        vihollinen.liikuVielä = vihollinen.liikkeenPituus;
                                    }
                                break;
                                case SEURAA_PELAAJAA:
                                    int etäisyysX = (int)(Pelaaja.hitbox.getCenterX() - vihollinen.hitbox.getCenterX());
                                    int etäisyysY = (int)(Pelaaja.hitbox.getCenterY() - vihollinen.hitbox.getCenterY());
                                    int etäisyysXits = (int)Math.abs(etäisyysX);
                                    int etäisyysYits = (int)Math.abs(etäisyysY);
                                    if (etäisyysXits > etäisyysYits) {
                                        if (etäisyysX < 0) {
                                            vihollinen.kokeileLiikkumista("vasen");
                                        }
                                        else {
                                            vihollinen.kokeileLiikkumista("oikea");
                                        }
                                    }
                                    else {
                                        if (etäisyysY < 0) {
                                            vihollinen.kokeileLiikkumista("ylös");
                                        }
                                        else {
                                            vihollinen.kokeileLiikkumista("alas");
                                        }
                                    }
                                break;
                                case STAATTINEN:
                                break;
                            }
                            // if (Pelaaja.hitbox.intersects(vihollinen.hitbox)) {
                            //     pelaajanKohdallaVihollinen = true;
                            //     System.out.println("collision - pelaaja: " + Pelaaja.hitbox.getMinX() + " - " + Pelaaja.hitbox.getMaxX() + ", " + Pelaaja.hitbox.getMinY() + " - " + Pelaaja.hitbox.getMaxY()  + ", vihollinen: " + vihollinen.hitbox.getMinX() + " - " + vihollinen.hitbox.getMaxX() + ", " + vihollinen.hitbox.getMinY() + " - " + vihollinen.hitbox.getMaxY());
                            //     if (Pelaaja.reaktioAika <= 0) {
                            //         if (Pelaaja.kuolemattomuusAika <= 0) {
                            //             if (Pelaaja.esineet[Peli.esineValInt] instanceof Kilpi && vihollinen.kilpiTehoaa) {

                            //             }
                            //             else {
                            //                 if (TarkistettavatArvot.lyödytVihut > 0) {
                            //                     TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PIESTY;
                            //                 }
                            //                 else if (TarkistettavatArvot.ämpäröidytVihut > 0) {
                            //                     TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_ÄMPÄRÖITY;
                            //                 }
                            //                 else {
                            //                     TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_NEUTRAALI;
                            //                 }
                            //                 Pelaaja.vahingoita(vihollinen.vahinko);
                            //             }
                            //         }
                            //     }
                            // }
                        }
                    }
                }
                // if (pelaajanKohdallaVihollinen) {
                //     Pelaaja.vähennäReaktioAikaa();
                // }
                // int leikkaavatVihollisHitboxit = 0;
                // for (NPC npc : Peli.npcLista) {
                //     if (npc instanceof Vihollinen) {
                //         Vihollinen vihollinen = (Vihollinen)npc;
                //         if (Pelaaja.hitbox.intersects(vihollinen.hitbox)) {
                //             leikkaavatVihollisHitboxit++;
                //             Pelaaja.vihollisenKohdalla = true;
                //             Pelaaja.vihollinenKohdalla = vihollinen;
                //         }
                //     }
                // }
                // if (leikkaavatVihollisHitboxit == 0) {
                //     pelaajanKohdallaVihollinen = false;
                //     Pelaaja.reaktioAika = 8;
                //     Pelaaja.vihollisenKohdalla = false;
                //     Pelaaja.vihollinenKohdalla = null;
                // }
                // else {
                //     pelaajanKohdallaVihollinen = true;
                // }
            }
        }
        catch (ConcurrentModificationException e) {
            System.out.println("Viimeisin vihollisten liikuttamisyritys peruutettiin (konkurrenssi-issue)");
        }
        return vihollinenLiikutettiin;
    }

    public static void tarkistaVihollisCollision() {
        boolean pelaajanKohdallaVihollinen = false;
        if (Peli.npcLista != null) {
            for (NPC npc : Peli.npcLista) {
                if (npc instanceof Vihollinen) {
                    Vihollinen vihollinen = (Vihollinen)npc;
                    if (!vihollinen.onkoKukistettu()) {
                        vihollinen.valitseKuvake();
                        if (Pelaaja.hitbox.intersects(vihollinen.hitbox)) {
                            pelaajanKohdallaVihollinen = true;
                            Pelaaja.viimeisinOsunutVihollinen = vihollinen;
                            System.out.println("collision - pelaaja: " + Pelaaja.hitbox.getMinX() + " - " + Pelaaja.hitbox.getMaxX() + ", " + Pelaaja.hitbox.getMinY() + " - " + Pelaaja.hitbox.getMaxY()  + ", vihollinen: " + vihollinen.hitbox.getMinX() + " - " + vihollinen.hitbox.getMaxX() + ", " + vihollinen.hitbox.getMinY() + " - " + vihollinen.hitbox.getMaxY());
                            if (Pelaaja.reaktioAika <= 0) {
                                if (Pelaaja.kuolemattomuusAika <= 0) {
                                    if (Pelaaja.esineet[Peli.esineValInt] instanceof Kilpi && vihollinen.kilpiTehoaa) {

                                    }
                                    else {
                                        if (Pelaaja.viimeisinOsunutVihollinen instanceof Pikkuvihu) {
                                            if (TarkistettavatArvot.lyödytVihut > 0) {
                                                TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PIKKUVIHU_LYÖTY;
                                            }
                                            else if (TarkistettavatArvot.ämpäröidytVihut > 0) {
                                                TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PIKKUVIHU_ÄMPÄRÖITY;
                                            }
                                            else {
                                                TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PIKKUVIHU_PASSIIVINEN;
                                            }
                                        }
                                        else if (Pelaaja.viimeisinOsunutVihollinen instanceof Pahavihu) {
                                            if (TarkistettavatArvot.lyödytVihut > 0) {
                                                TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PAHAVIHU_LYÖTY;
                                            }
                                            else if (TarkistettavatArvot.ämpäröidytVihut > 0) {
                                                TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PAHAVIHU_ÄMPÄRÖITY;
                                            }
                                            else {
                                                TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PAHAVIHU_PASSIIVINEN;
                                            }
                                        }
                                        
                                        Pelaaja.vahingoita(vihollinen.vahinko);
                                    }
                                }
                            }
                        }
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
    }
}
