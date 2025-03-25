package keimo;

import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.Maastot.*;
import keimo.Ruudut.PeliRuutu;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.Utility.Käännettävä.Suunta;
import keimo.entityt.*;
import keimo.entityt.npc.*;
import keimo.keimoEngine.liikeSimulaatiot.*;
import keimo.kenttäkohteet.*;
import keimo.kenttäkohteet.avattavaEste.AvattavaEste;
import keimo.kenttäkohteet.esine.Kilpi;
import keimo.kenttäkohteet.esine.Kuparilager;
import keimo.kenttäkohteet.kiintopiste.KaljaAutomaatti;
import keimo.kenttäkohteet.triggeri.Triggeri;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ConcurrentModificationException;

public class PeliKenttäMetodit {
    
    static KenttäKohde[][] pelikenttä;
    static Maasto[][] maastokenttä;

    public static void suoritaPelikenttäMetoditJoka2000Tick() {
        tyhjennäAmmusLista();
        luoAmmus();
    }

    public static void suoritaPelikenttäMetoditJoka600Tick() {
        luoKalja();
    }

    public static void suoritaPelikenttäMetoditJoka100Tick() {
        luoAmmus();
        //debugLuoEntity();
        tarkistaPeliMusa();
    }

    public static void suoritaPelikenttäMetoditJoka60Tick() {

    }

    public static void suoritaPelikenttäMetoditJoka10Tick() {
        ReitinhakuSimulaatio.suoritaReitinhakuVihollisille();
        tarkistaPaineLaatat();
        tarkistaTriggerit();
        tarkistaPomonTila();
    }

    public static void suoritaPelikenttäMetoditJoka2Tick() {
        tarkistaVihollistenLiikerata();
    }

    public static void suoritaPelikenttäMetoditJokaTick() {
        tarkistaAmmusCollision();
        tarkistaVihollisCollision();
        tarkistaLiikutettavanEsineenCollision();
        Pelaaja.vähennäKuolemattomuusAikaa();
        Pelaaja.vähennäHyökkäysAikaa();
        Peli.vähennäKäyttöViivettä();
        liikutaAmmuksia();
        liikutaVihollisia();
        poistaTyhjätEsineetInvasta();
    }

    private static void tarkistaPeliMusa() {
        if (Peli.huone != null) {
            ÄänentoistamisSäie.toistaPeliMusa(Peli.huone.annaHuoneenMusa());
        }
    }

    public static void tyhjennäAmmusLista() {
        synchronized (Peli.entityLista) {
            try {
                for (Entity e : Peli.entityLista) {
                    if (e instanceof Ammus) {
                        Peli.entityLista.remove(e);
                    }
                }
            }
            catch (ConcurrentModificationException cme) {
                System.out.println("Viimeisin kentän tyhjennys peruutettiin (konkurrenssi-issue)");
                cme.printStackTrace();
            }
        }
    }

    static void tarkistaPaineLaatat() {
        synchronized (Peli.entityLista) {
            try {
                for (Entity entity : Peli.entityLista) {
                    if (entity instanceof NPC) {
                        NPC npc = (NPC)entity;
                        if (npc instanceof Vihollinen) {
                            Vihollinen v = (Vihollinen)npc;
                            if (Peli.pelikenttä[v.sijX][v.sijY] instanceof Triggeri) {
                                Triggeri trg = (Triggeri)Peli.pelikenttä[v.sijX][v.sijY];
                                if (trg.annaVaadittuVihollinen() != null) {
                                    if (trg.annaVaadittuVihollinen().annaNimi().startsWith(v.annaNimi())) {
                                        trg.triggeröi();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (ConcurrentModificationException cme) {
                System.out.println("Viimeisin triggerien tarkistus peruutettiin (konkurrenssi-issue)");
                cme.printStackTrace();
            }
        }
    }

    static void tarkistaTriggerit() {
        try {
            for (KenttäKohde[] kk: Peli.pelikenttä) {
                for (KenttäKohde k : kk) {
                    if (k instanceof AvattavaEste) {
                        AvattavaEste este = (AvattavaEste)k;
                        //if (!este.onkoAvattu()) {
                            este.tarkistaTriggerit();
                        //}
                    }
                }
            }
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin triggerien tarkistus peruutettiin (konkurrenssi-issue)");
            cme.printStackTrace();
        }
    }

    static void poistaTyhjätEsineetInvasta() {
        for (int i = 0; i < Pelaaja.esineet.length; i++) {
            if (Pelaaja.esineet[i] != null) {
                if (Pelaaja.esineet[i].poistoon()) {
                    if (Peli.valittuEsine == Pelaaja.esineet[i]) {
                        Peli.valittuEsine = null;
                    }
                    Pelaaja.esineet[i] = null;
                }
            }
        }
    }

    static void tarkistaVihollistenLiikerata() {
        Vihollinen.liikkeenPituus = PeliRuutu.esineenKokoPx;
    }

    static boolean liikutaVihollisia() {
        boolean vihollinenLiikutettiin = false;
        //boolean pelaajanKohdallaVihollinen = false;
        synchronized(Peli.entityLista) {
            try {
                if (Peli.entityLista != null) {
                    for (Entity npc : Peli.entityLista) {
                        if (npc instanceof Vihollinen) {
                            Vihollinen vihollinen = (Vihollinen)npc;
                            vihollinen.vähennäHurtAikaa();
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
                                            vihollinen.liikuVielä = Vihollinen.liikkeenPituus;
                                        }
                                    break;
                                    case LOOP_NELIÖ_VASTAPÄIVÄÄN:
                                        if (vihollinen.liikuVielä > 0) {
                                            vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopNeliöVastapäivään[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopNeliöVastapäivään.length]);
                                            vihollinen.liikuVielä--;
                                        }
                                        else {
                                            vihollinen.liikeLoopinVaihe++;
                                            vihollinen.liikuVielä = Vihollinen.liikkeenPituus;
                                        }
                                    break;
                                    case LOOP_VASEN_OIKEA:
                                        if (vihollinen.liikuVielä > 0) {
                                            vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopVasenOikea[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopVasenOikea.length]);
                                            vihollinen.liikuVielä--;
                                        }
                                        else {
                                            vihollinen.liikeLoopinVaihe++;
                                            vihollinen.liikuVielä = Vihollinen.liikkeenPituus;
                                        }
                                    break;
                                    case LOOP_YLÖS_ALAS:
                                        if (vihollinen.liikuVielä > 0) {
                                            vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopYlösAlas[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopVasenOikea.length]);
                                            vihollinen.liikuVielä--;
                                        }
                                        else {
                                            vihollinen.liikeLoopinVaihe++;
                                            vihollinen.liikuVielä = Vihollinen.liikkeenPituus;
                                        }
                                    break;
                                    case NELIÖ_MYÖTÄPÄIVÄÄN_ESTEESEEN_ASTI:
                                        if (vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopNeliöMyötäpäivään[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopNeliöMyötäpäivään.length])) {

                                        }
                                        else {
                                            vihollinen.liikeLoopinVaihe++;
                                            vihollinen.liikuVielä = Vihollinen.liikkeenPituus;
                                        }
                                    break;
                                    case NELIÖ_VASTAPÄIVÄÄN_ESTEESEEN_ASTI:
                                        if (vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopNeliöVastapäivään[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopNeliöVastapäivään.length])) {
                                            
                                        }
                                        else {
                                            vihollinen.liikeLoopinVaihe++;
                                            vihollinen.liikuVielä = Vihollinen.liikkeenPituus;
                                        }
                                    break;
                                    case VASEN_OIKEA_ESTEESEEN_ASTI:
                                        if (vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopVasenOikea[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopVasenOikea.length])) {
                                            ;
                                        }
                                        else {
                                            vihollinen.liikeLoopinVaihe++;
                                            vihollinen.liikuVielä = Vihollinen.liikkeenPituus;
                                        }
                                    break;
                                    case YLÖS_ALAS_ESTEESEEN_ASTI:
                                        if (vihollinen.kokeileLiikkumista(vihollinen.liikeSuuntaLoopYlösAlas[vihollinen.liikeLoopinVaihe % vihollinen.liikeSuuntaLoopVasenOikea.length])) {
                                            ;
                                        }
                                        else {
                                            vihollinen.liikeLoopinVaihe++;
                                            vihollinen.liikuVielä = Vihollinen.liikkeenPituus;
                                        }
                                    break;
                                    case VARTIJA_LIIKE:
                                        if (vihollinen instanceof Vartija) {
                                            Vartija vartija = (Vartija)vihollinen;
                                            VartijaSimulaatio.simuloiVartija(vartija);
                                        }
                                    break;
                                    case SEURAA_PELAAJAA:
                                        int etäisyysX = (int)(Pelaaja.hitbox.getCenterX() - vihollinen.hitbox.getCenterX());
                                        int etäisyysY = (int)(Pelaaja.hitbox.getCenterY() - vihollinen.hitbox.getCenterY());
                                        int etäisyysXits = (int)Math.abs(etäisyysX);
                                        int etäisyysYits = (int)Math.abs(etäisyysY);
                                        if (etäisyysXits > etäisyysYits) {
                                            if (etäisyysX < 0) {
                                                vihollinen.kokeileLiikkumista(Suunta.VASEN);
                                            }
                                            else {
                                                vihollinen.kokeileLiikkumista(Suunta.OIKEA);
                                            }
                                        }
                                        else {
                                            if (etäisyysY < 0) {
                                                vihollinen.kokeileLiikkumista(Suunta.YLÖS);
                                            }
                                            else {
                                                vihollinen.kokeileLiikkumista(Suunta.ALAS);
                                            }
                                        }
                                    break;
                                    case SEURAA_REITTIÄ:
                                        ReitinhakuSimulaatio.simuloiReitinhaku(vihollinen);
                                    break;
                                    case BOSS_LIIKE:
                                        if (vihollinen instanceof Boss) {
                                            Boss boss = (Boss)vihollinen;
                                            BossSimulaatio.simuloiBoss(boss);
                                        }
                                    break;
                                    case YMPYRÄLIIKE_MYÖTÄPÄIVÄÄN:
                                        int alkupNopeus = vihollinen.nopeus;
                                        vihollinen.nopeus = 1;
                                        if (vihollinen.liikuVielä > 0) {
                                            if (vihollinen.liikeX > 0) {
                                                for (int i = 0; i < vihollinen.liikeX; i++) {
                                                    vihollinen.kokeileLiikkumista(Suunta.OIKEA);
                                                }
                                            }
                                            else {
                                                for (int i = 0; i < -vihollinen.liikeX; i++) {
                                                    vihollinen.kokeileLiikkumista(Suunta.VASEN);
                                                }
                                            }
                                            if (vihollinen.liikeY > 0) {
                                                for (int i = 0; i < vihollinen.liikeY; i++) {
                                                    vihollinen.kokeileLiikkumista(Suunta.ALAS);
                                                }
                                            }
                                            else {
                                                for (int i = 0; i < -vihollinen.liikeY; i++) {
                                                    vihollinen.kokeileLiikkumista(Suunta.YLÖS);
                                                }
                                            }
                                            if (vihollinen.liikuVielä > Vihollinen.liikkeenPituus/2 && vihollinen.liikeX > -16) {
                                                vihollinen.liikeX--;
                                            }
                                            else if (vihollinen.liikeX < 16) {
                                                vihollinen.liikeX++;
                                            }
                                            if ((vihollinen.liikuVielä >= Vihollinen.liikkeenPituus*3/4 || vihollinen.liikuVielä <= Vihollinen.liikkeenPituus/4) && vihollinen.liikeY < 16) {
                                                vihollinen.liikeY++;
                                            }
                                            else if (vihollinen.liikeY > -16) {
                                                vihollinen.liikeY--;
                                            }
                                            vihollinen.liikuVielä--;
                                            //System.out.println("liikeX: " + vihollinen.liikeX + " liikeY: " + vihollinen.liikeY + " liikuvielä: " + vihollinen.liikuVielä);
                                        }
                                        else {
                                            vihollinen.liikuVielä = Vihollinen.liikkeenPituus;
                                            vihollinen.liikeX = 16;
                                            vihollinen.liikeY = 0;
                                        }
                                        vihollinen.nopeus = alkupNopeus;
                                    break;
                                    case YMPYRÄLIIKE_VASTAPÄIVÄÄN:
                                        alkupNopeus = vihollinen.nopeus;
                                        vihollinen.nopeus = 1;
                                        if (vihollinen.liikuVielä > 0) {
                                            if (vihollinen.liikeX > 0) {
                                                for (int i = 0; i < vihollinen.liikeX; i++) {
                                                    vihollinen.kokeileLiikkumista(Suunta.OIKEA);
                                                }
                                            }
                                            else {
                                                for (int i = 0; i < -vihollinen.liikeX; i++) {
                                                    vihollinen.kokeileLiikkumista(Suunta.VASEN);
                                                }
                                            }
                                            if (vihollinen.liikeY > 0) {
                                                for (int i = 0; i < vihollinen.liikeY; i++) {
                                                    vihollinen.kokeileLiikkumista(Suunta.ALAS);
                                                }
                                            }
                                            else {
                                                for (int i = 0; i < -vihollinen.liikeY; i++) {
                                                    vihollinen.kokeileLiikkumista(Suunta.YLÖS);
                                                }
                                            }
                                            if (vihollinen.liikuVielä > Vihollinen.liikkeenPituus/2 && vihollinen.liikeX > -16) {
                                                vihollinen.liikeX--;
                                            }
                                            else if (vihollinen.liikeX < 16) {
                                                vihollinen.liikeX++;
                                            }
                                            if ((vihollinen.liikuVielä >= Vihollinen.liikkeenPituus*3/4 || vihollinen.liikuVielä <= Vihollinen.liikkeenPituus/4) && vihollinen.liikeY > -16) {
                                                vihollinen.liikeY--;
                                            }
                                            else if (vihollinen.liikeY < 16) {
                                                vihollinen.liikeY++;
                                            }
                                            vihollinen.liikuVielä--;
                                        }
                                        else {
                                            vihollinen.liikuVielä = Vihollinen.liikkeenPituus;
                                            vihollinen.liikeX = 16;
                                            vihollinen.liikeY = 0;
                                        }
                                        vihollinen.nopeus = alkupNopeus;
                                    break;
                                    case STAATTINEN:
                                    break;
                                }
                                siirräVihollinenPoisEsineenSisältä(vihollinen);
                            }
                        }
                    }
                }
            }
            catch (ConcurrentModificationException e) {
                System.out.println("Viimeisin vihollisten liikuttamisyritys peruutettiin (konkurrenssi-issue)");
            }
            return vihollinenLiikutettiin;
        }
    }

    public static void tarkistaVihollisCollision() {
        boolean pelaajanKohdallaVihollinen = false;
        try {
            if (Peli.entityLista != null) {
                for (Entity npc : Peli.entityLista) {
                    if (npc instanceof Vihollinen) {
                        Vihollinen vihollinen = (Vihollinen)npc;
                        if (!vihollinen.onkoKukistettu()) {
                            vihollinen.valitseKuvake();
                            if (Pelaaja.hitbox.intersects(vihollinen.hitbox)) {
                                pelaajanKohdallaVihollinen = true;
                                Pelaaja.viimeisinOsunutVihollinen = vihollinen;
                                //System.out.println("collision - pelaaja: " + Pelaaja.hitbox.getMinX() + " - " + Pelaaja.hitbox.getMaxX() + ", " + Pelaaja.hitbox.getMinY() + " - " + Pelaaja.hitbox.getMaxY()  + ", vihollinen: " + vihollinen.hitbox.getMinX() + " - " + vihollinen.hitbox.getMaxX() + ", " + vihollinen.hitbox.getMinY() + " - " + vihollinen.hitbox.getMaxY());
                                if (Pelaaja.hyökkäysAika > 0) {
                                    if (vihollinen.tehoavatAseet.contains(Pelaaja.käytettyAse.annaNimi())) {
                                        if (vihollinen.annaHurtAika() <= 0) {
                                            vihollinen.vahingoita(Pelaaja.käytettyAse);
                                            Pelaaja.käytettyAse.käytä();
                                        }
                                    }
                                }
                                else {
                                    if (Pelaaja.reaktioAika <= 0) {
                                        if (Pelaaja.kuolemattomuusAika <= 0 && Pelaaja.känniKuolemattomuus <= 0) {
                                            if (Pelaaja.esineet[Peli.esineValInt] instanceof Kilpi && vihollinen.kilpiTehoaa) {

                                            }
                                            else {
                                                if (Pelaaja.viimeisinOsunutVihollinen instanceof Pikkuvihu) {
                                                    if (TarkistettavatArvot.annaLyödytVihut() > 0) {
                                                        TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PIKKUVIHU_LYÖTY;
                                                    }
                                                    else if (TarkistettavatArvot.annaÄmpäröidytVihut() > 0) {
                                                        TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PIKKUVIHU_ÄMPÄRÖITY;
                                                    }
                                                    else {
                                                        TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PIKKUVIHU_PASSIIVINEN;
                                                    }
                                                }
                                                else if (Pelaaja.viimeisinOsunutVihollinen instanceof Pahavihu) {
                                                    if (TarkistettavatArvot.annaLyödytVihut() > 0) {
                                                        TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PAHAVIHU_LYÖTY;
                                                    }
                                                    else if (TarkistettavatArvot.annaÄmpäröidytVihut() > 0) {
                                                        TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PAHAVIHU_ÄMPÄRÖITY;
                                                    }
                                                    else {
                                                        TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_PAHAVIHU_PASSIIVINEN;
                                                    }
                                                }
                                                else if (Pelaaja.viimeisinOsunutVihollinen instanceof Asevihu) {
                                                    if (TarkistettavatArvot.annaLyödytVihut() > 0) {
                                                        TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_ASEVIHU_LYÖTY;
                                                    }
                                                    else if (TarkistettavatArvot.annaÄmpäröidytVihut() > 0) {
                                                        TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_ASEVIHU_ÄMPÄRÖITY;
                                                    }
                                                    else {
                                                        TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_ASEVIHU_PASSIIVINEN;
                                                    }
                                                }
                                                else if (Pelaaja.viimeisinOsunutVihollinen instanceof Vartija) {
                                                    TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.VARTIJA;
                                                }
                                                else if (Pelaaja.viimeisinOsunutVihollinen instanceof Boss) {
                                                    TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_BOSS;
                                                }
                                                if (vihollinen.tekeeVahinkoa) {
                                                    Pelaaja.vahingoita(vihollinen.vahinko * PelinAsetukset.vaikeusAste);
                                                }
                                            }
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
                for (Entity npc : Peli.entityLista) {
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
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin collision-tarkistus peruutettiin (konkurrenssi-issue).");
            //cme.printStackTrace();
        }
    }

    public static void tarkistaAmmusCollision() {
        try {
            if (Peli.entityLista != null) {
                for (Entity entity : Peli.entityLista) {
                    if (entity instanceof Ammus) {
                        Ammus ammus = (Ammus)entity;
                        if (Pelaaja.hitbox.intersects(ammus.hitbox)) {
                            if (Pelaaja.kuolemattomuusAika <= 0 && Pelaaja.känniKuolemattomuus <= 0) {
                                Pelaaja.vahingoita(ammus.damage * PelinAsetukset.vaikeusAste);
                                if (ammus.ampuja instanceof Asevihu) {
                                    if (TarkistettavatArvot.annaLyödytVihut() > 0) TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_ASEVIHU_LYÖTY;
                                    else if (TarkistettavatArvot.annaÄmpäröidytVihut() > 0) TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_ASEVIHU_ÄMPÄRÖITY;
                                    else TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_VIHOLLINEN_ASEVIHU_PASSIIVINEN;
                                }
                                else if (ammus.ampuja instanceof Boss) {
                                    TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_BOSS;
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin ammusten collision-tarkistus peruttiin (konkurrenssi-issue)");
        }
    }

    public static void tarkistaLiikutettavanEsineenCollision() {
        // try {
        //     if (Peli.entityLista != null) {
        //         if (Peli.entityLista.size() > 0) {
        //             for (Entity entity : Peli.entityLista) {
        //                 if (entity instanceof LiikkuvaObjekti) {
        //                     LiikkuvaObjekti obj = (LiikkuvaObjekti)entity;
        //                     if (obj.ulkoHitbox.contains(Pelaaja.hitbox.getCenterX(), Pelaaja.hitbox.getCenterY())) {
        //                         int törmäykset = 0;
        //                         for (Entity törmääväEntity : Peli.entityLista) {
        //                             if (obj != törmääväEntity && obj.hitbox.intersects(törmääväEntity.hitbox)) {
        //                                 törmäykset++;
        //                             }
        //                         }
        //                         if (törmäykset == 0) {
        //                             if (obj.kokeileLiikkumista(Pelaaja.keimonSuunta)) {
        //                                 System.out.println("x: " + obj.sijX + ", y: " + obj.sijY);
        //                             }
        //                         }
        //                         else {
        //                             siirräEsinePoisMuidenEsineidenSisältä(obj);
        //                         }
        //                     }
        //                     if (obj.hitbox.contains(Pelaaja.hitbox.getCenterX(), Pelaaja.hitbox.getCenterY())) {
        //                         siirräPelaajaPoisEsineenSisältä(obj);
        //                     }
        //                     else {
        //                         Pelaaja.nopeus = Pelaaja.vakionopeus;
        //                     }
        //                 }
        //             }
        //         }
        //         else {
        //             Pelaaja.nopeus = Pelaaja.vakionopeus;
        //         }
        //     }
        // }
        // catch (ConcurrentModificationException cme) {
        //     System.out.println("Viimeisin liikutettavien objektien liikuttamisyritys peruttiin (konkurrenssi-issue)");
        // }
        try {
            if (Peli.entityLista != null) {
                if (Peli.entityLista.size() > 0) {
                    for (Entity entity : Peli.entityLista) {
                        if (entity instanceof LiikkuvaObjekti) {
                            LiikkuvaObjekti obj = (LiikkuvaObjekti)entity;
                            int tarkistaVasen = (int)(Pelaaja.hitbox.getMinX()-Pelaaja.nopeus)/PeliRuutu.pelaajanKokoPx;
                            int tarkistaOikea = (int)(Pelaaja.hitbox.getMaxX())/PeliRuutu.pelaajanKokoPx;
                            int tarkistaAlas = (int)(Pelaaja.hitbox.getMaxY())/PeliRuutu.pelaajanKokoPx;
                            int tarkistaYlös = (int)(Pelaaja.hitbox.getMinY()-Pelaaja.nopeus)/PeliRuutu.pelaajanKokoPx;
                            Rectangle uusiSijainti = new Rectangle(PeliRuutu.esineenKokoPx, PeliRuutu.esineenKokoPx);
                            switch (Pelaaja.keimonSuunta) {
                                case VASEN -> {
                                    uusiSijainti.setLocation((int)(Pelaaja.hitbox.getMinX()-Pelaaja.nopeus), (int)Pelaaja.hitbox.getY());
                                }
                                case OIKEA -> {
                                    uusiSijainti.setLocation((int)(Pelaaja.hitbox.getMaxX()), (int)Pelaaja.hitbox.getY());
                                }
                                case ALAS -> {
                                    uusiSijainti.setLocation((int)Pelaaja.hitbox.getX(), (int)(Pelaaja.hitbox.getMaxY()));
                                }
                                case YLÖS -> {
                                    uusiSijainti.setLocation((int)Pelaaja.hitbox.getX(), (int)(Pelaaja.hitbox.getMinY()-Pelaaja.nopeus));
                                }
                            }
                            if (Pelaaja.hitbox.intersects(obj.hitbox)) {
                            //if (uusiSijainti.intersects(obj.hitbox)) {
                                obj.kokeileLiikkumista(Pelaaja.keimonSuunta);
                            }
                        }
                    }
                }
                else {
                    Pelaaja.nopeus = Pelaaja.vakionopeus;
                }
            }
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin liikutettavien objektien liikuttamisyritys peruttiin (konkurrenssi-issue)");
        }
    }

    private static void siirräPelaajaPoisEsineenSisältä(Entity entity) {
        if (entity instanceof LiikkuvaObjekti) {
            LiikkuvaObjekti obj = (LiikkuvaObjekti)entity;
            double xDelta = Math.abs(obj.hitbox.getCenterX() - Pelaaja.hitbox.getCenterX());
            double yDelta = Math.abs(obj.hitbox.getCenterY() - Pelaaja.hitbox.getCenterY());
            if (xDelta > yDelta) {
                if (obj.hitbox.getCenterX() > Pelaaja.hitbox.getCenterX()) {
                    if (Pelaaja.kokeileLiikkumista(Suunta.VASEN)) {
                        System.out.println("Pelaaja siirrettiin vasemmalle");
                    }
                }
                else {
                    if (Pelaaja.kokeileLiikkumista(Suunta.OIKEA)) {
                        System.out.println("Pelaaja siirrettiin oikealle");
                    }
                }
            }
            else {
                if (obj.hitbox.getCenterY() > Pelaaja.hitbox.getCenterY()) {
                    if (Pelaaja.kokeileLiikkumista(Suunta.YLÖS)) {
                        System.out.println("Pelaaja siirrettiin ylös");
                    }
                }
                else {
                    if (Pelaaja.kokeileLiikkumista(Suunta.ALAS)) {
                        System.out.println("Pelaaja siirrettiin alas");
                    }
                }
            }
        }
    }

    private static void siirräEsinePoisMuidenEsineidenSisältä(Entity entity) {
        try {
            if (Peli.entityLista != null) {
                if (Peli.entityLista.size() > 0) {
                    //for (Entity entity1 : Peli.entityLista) {
                        //if (entity1 instanceof LiikkuvaObjekti) {
                            LiikkuvaObjekti obj1 = (LiikkuvaObjekti)entity;
                            for (Entity entity2 : Peli.entityLista) {
                                if (entity != entity2 && entity2 instanceof LiikkuvaObjekti) {
                                    LiikkuvaObjekti obj2 = (LiikkuvaObjekti)entity2;
                                    if (obj1.hitbox.intersects(obj2.hitbox)) {
                                        if (obj1.hitbox.getCenterX() > obj2.hitbox.getCenterX()) {
                                            obj2.kokeileLiikkumista(Suunta.VASEN);
                                            obj1.kokeileLiikkumista(Suunta.OIKEA);
                                        }
                                        else {
                                            obj2.kokeileLiikkumista(Suunta.OIKEA);
                                            obj1.kokeileLiikkumista(Suunta.VASEN);
                                        }
                                        if (obj1.hitbox.getCenterY() > obj2.hitbox.getCenterY()) {
                                            obj2.kokeileLiikkumista(Suunta.YLÖS);
                                            obj1.kokeileLiikkumista(Suunta.ALAS);
                                        }
                                        else {
                                            obj2.kokeileLiikkumista(Suunta.ALAS);
                                            obj1.kokeileLiikkumista(Suunta.YLÖS);
                                        }
                                    }
                                }
                            }
                        //}
                    //}
                }
            }
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin liikutettavien objektien liikuttamisyritys peruttiin (konkurrenssi-issue)");
        }
    }

    private static void siirräVihollinenPoisEsineenSisältä(Entity entity) {
        try {
            if (Peli.entityLista != null) {
                if (Peli.entityLista.size() > 0) {
                    Vihollinen vihollinen = (Vihollinen)entity;
                    for (Entity entity2 : Peli.entityLista) {
                        if (entity != entity2 && entity2 instanceof LiikkuvaObjekti) {
                            LiikkuvaObjekti obj2 = (LiikkuvaObjekti)entity2;
                            if (vihollinen.hitbox.intersects(obj2.hitbox)) {
                                double xDelta = Math.abs(obj2.hitbox.getCenterX() - vihollinen.hitbox.getCenterX());
                                double yDelta = Math.abs(obj2.hitbox.getCenterY() - vihollinen.hitbox.getCenterY());
                                if (xDelta > yDelta) {
                                    if (obj2.hitbox.getCenterX() > vihollinen.hitbox.getCenterX()) {
                                        if (vihollinen.kokeileLiikkumista(Suunta.VASEN, true)) {
                                            //System.out.println("Vihollinen siirrettiin vasemmalle");
                                        }
                                    }
                                    else {
                                        if (vihollinen.kokeileLiikkumista(Suunta.OIKEA, true)) {
                                            //System.out.println("Vihollinen siirrettiin oikealle");
                                        }
                                    }
                                }
                                else {
                                    if (obj2.hitbox.getCenterY() > vihollinen.hitbox.getCenterY()) {
                                        if (vihollinen.kokeileLiikkumista(Suunta.YLÖS, true)) {
                                            //System.out.println("Vihollinen siirrettiin ylös");
                                        }
                                    }
                                    else {
                                        if (vihollinen.kokeileLiikkumista(Suunta.ALAS, true)) {
                                            //System.out.println("Vihollinen siirrettiin alas");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin liikutettavien objektien liikuttamisyritys peruttiin (konkurrenssi-issue)");
        }
    }

    public static void luoAmmus() {
        synchronized(Peli.entityLista) {
            try {
                if (Peli.entityLista != null) {
                    //Iterator<Entity> i = Peli.entityLista.iterator();
                    //while (i.hasNext()) {
                    for (Entity npc : Peli.entityLista) {
                        //Entity npc = i.next();
                        if (npc instanceof Asevihu) {
                            Asevihu av = (Asevihu)npc;
                            if (!av.onkoKukistettu()) {
                                Peli.entityLista.add(new Ammus((int)av.hitbox.getCenterX(), (int)av.hitbox.getCenterY(), av.suuntaVasenOikea, av.ammusVahinko, av));
                                Point sijainti = new Point((int)av.hitbox.getCenterX(), (int)av.hitbox.getCenterY());
                                ÄänentoistamisSäie.toistaSFX("ammus", sijainti);
                            }
                        }
                    }
                }
                PääIkkuna.uudelleenpiirräObjektit = true;
                //System.out.println(Peli.ammusLista.size());
            }
            catch (ConcurrentModificationException cme) {
                //System.out.println("Viimeisin ammusten luontioperaatio peruttiin (konkurrenssi-issue)");
                //cme.printStackTrace();
                PääIkkuna.uudelleenpiirräObjektit = true;
            }
        }
    }

    public static void poistaAmmukset() {
        try {
            if (Peli.entityLista != null) {
                if (Peli.entityLista.size() > 0) {
                    for (Entity entity : Peli.entityLista) {
                        if (entity instanceof Ammus) {
                            Ammus ammus = (Ammus)entity;
                            if (ammus.elinAika <= 0) {
                                Peli.entityLista.remove(ammus);
                                Ammus.ammusId--;
                            }
                        }
                    }
                }
            }
        }
        catch (ConcurrentModificationException cme) {
            //System.out.println("Viimeisin ammusten poitoyritys peruttiin (konkurrenssi-issue)");
            //cme.printStackTrace();
        }
    }

    public static void liikutaAmmuksia() {
        synchronized(Peli.entityLista) {
            try {
                if (Peli.entityLista != null) {
                    if (Peli.entityLista.size() > 0) {
                        for (Entity entity : Peli.entityLista) {
                            if (entity instanceof Ammus) {
                                Ammus ammus = (Ammus)entity;
                                ammus.elinAika--;
                                if (ammus.kokeileLiikettä(ammus.suunta)) {
                                    ammus.liikuta8suuntaan(ammus.suunta);
                                }
                                if (ammus.elinAika <= 0) {
                                    Peli.entityLista.remove(ammus);
                                    Ammus.ammusId--;
                                }
                            }
                        }
                    }
                }
            }
            catch (ConcurrentModificationException cme) {
                //System.out.println("Viimeisin ammusten liikutus peruttiin (konkurrenssi-issue)");
                //cme.printStackTrace();
            }
        }
    }

    public static void tarkistaPomonTila() {
        if (TavoiteLista.nykyinenTavoite.startsWith("Voita pomo")) {
            try {
                if (Peli.entityLista != null) {
                    for (Entity entity : Peli.entityLista) {
                        if (entity instanceof Boss) {
                            Boss boss = (Boss)entity;
                            if (boss.onkoKukistettu()){
                                if (boss.annaHurtAika() <= 0) {
                                    TavoiteLista.suoritaPääTavoite(8);
                                    Peli.peliLäpäisty = true;
                                }
                            }
                        }
                    }
                }
                PääIkkuna.uudelleenpiirräObjektit = true;
            }
            catch (ConcurrentModificationException cme) {
                System.out.println("Viimeisin pomon tilan tarkistus peruttiin (konkurrenssi-issue)");
            }
        }
    }

    public static void luoKalja() {
        for (int y = 0; y < Peli.pelikenttä.length; y++) {
            for (int x = 0; x < Peli.pelikenttä.length; x++) {
                if (Peli.pelikenttä[x][y] instanceof KaljaAutomaatti) {
                    if (x > 0) {
                        if (Peli.pelikenttä[x-1][y] == null) {
                            Peli.pelikenttä[x-1][y] = new Kuparilager(true, 0, 0);
                        }
                    }
                }
            }
        }
    }

    public static void debugLuoEntity() {
        try {
            if (Peli.entityLista != null) {
                Peli.entityLista.add(new TyönnettäväLaatikko(12, 12));
                for (Entity entity : Peli.entityLista) {
                    System.out.println("entity: " + entity.annaNimi() + ", " + entity.sijX + ", " + entity.sijY);
                }
            }
            PääIkkuna.uudelleenpiirräObjektit = true;
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin ammusten luontioperaatio peruttiin (konkurrenssi-issue)");
        }
    }
}
