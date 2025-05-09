package keimo.keimoEngine.liikeSimulaatiot;

import keimo.Peli;
import keimo.Utility.Käännettävä.Suunta;
import keimo.entityt.Ammus;
import keimo.entityt.npc.Boss;
import keimo.entityt.npc.Boss.BossState;
import keimo.keimoEngine.äänet.Äänet;
import keimo.entityt.npc.Vihollinen;

import java.awt.Point;
import java.util.Random;

public class BossSimulaatio {
    
    private static int bossLiikeSuunta = 0;
    private static int tilanKesto = 100;
    private static boolean alkuHuuto = true;
    private static boolean ampunut = false, huutanut = false;
    private static boolean arvoLiikeSuunta = true;
    private static boolean arvoPyörähdysSuunta = true;
    private static boolean vastapäivään = false;
    private static boolean aloitaYlöspäin = false;
    private static int ympyräNopeusX = 10;
    private static int ympyräNopeusY = 0;
    private static int ympyräLiikkeenSuuruus = 10;
    private static boolean kasvataYmpyräXNopeutta = false;
    private static boolean kasvataYmpyräYNopeutta = true;
    private static Random r = new Random();
    
    public static void simuloiBoss(Boss boss) {
        if (alkuHuuto) {
            bossHuuto(boss);
            alkuHuuto = false;
        }
        if (!boss.onkoKukistettu()) {
            switch (boss.bossTila) {
                case NORMAALI -> {
                    bossLiikePerus(boss);
                }
                case PYÖRÄHDYS -> {
                    bossLiikeYmpyrä(boss);
                }
                case LATAUS -> {
                    bossHuuto(boss);
                }
                case HYÖKKÄYS -> {
                    bossAmmus(boss);
                }
                case HYPER -> {
                    if (r.nextBoolean()) bossLiikeHyperMyötäpäivään(boss);
                    else bossLiikeHyperVastapäivään(boss);
                }
            }
            if (tilanKesto > 0) tilanKesto--;
            if (tilanKesto <= 0) valitseTila(boss);
        }
    }

    private static void valitseTila(Boss boss) {
        switch (boss.bossTila) {
            case LATAUS -> {
                boss.bossTila = BossState.HYÖKKÄYS;
                tilanKesto = 50;
            }
            case HYÖKKÄYS, PYÖRÄHDYS, HYPER -> {
                boss.bossTila = BossState.NORMAALI;
                tilanKesto = r.nextInt(100, 300);
            }
            default -> {
                int seuraavaTila = r.nextInt(10);
                if (seuraavaTila == 9) {
                    boss.bossTila = BossState.HYPER;
                    tilanKesto = 60;
                }
                else if (seuraavaTila > 4) {
                    boss.bossTila = BossState.PYÖRÄHDYS;
                    tilanKesto = 100;
                }
                else {
                    boss.bossTila = BossState.LATAUS;
                    tilanKesto = 80;
                }
            }
        }
        huutanut = false;
        ampunut = false;
        arvoLiikeSuunta = true;
        arvoPyörähdysSuunta = true;
        boss.valitseTekstuuri();
        boss.valitseVahinko();
    }

    private static int valitseLiikeSuuntaRandom(Vihollinen vihollinen){
        return r.nextInt(0, 8);
    }

    private static int valitseLiikeSuunta(Vihollinen vihollinen) {
        int liikeSuunta = r.nextInt(0, 8);
        int tileX = (int)(vihollinen.hitbox.getCenterX()/vihollinen.hitbox.width);
        int tileY = (int)(vihollinen.hitbox.getCenterY()/vihollinen.hitbox.height);
        switch (liikeSuunta) {
            case 0 -> {
                if (tileX > 0) {
                    if (Peli.maastokenttä[tileX-1][tileY] != null && !Peli.maastokenttä[tileX-1][tileY].estääköLiikkumisen(Suunta.VASEN)) {
                        return 0;
                    }
                    else valitseLiikeSuunta(vihollinen);
                }
                else valitseLiikeSuunta(vihollinen);
            }
            case 1 -> {
                if (tileX < Peli.maastokenttä.length-1) {
                    if (Peli.maastokenttä[tileX+1][tileY] != null && !Peli.maastokenttä[tileX+1][tileY].estääköLiikkumisen(Suunta.VASEN)) {
                        return 1;
                    }
                    else valitseLiikeSuunta(vihollinen);
                }
                else valitseLiikeSuunta(vihollinen);
            }
            case 2 -> {
                if (tileY < Peli.maastokenttä.length-1) {
                    if (Peli.maastokenttä[tileX][tileY+1] != null && !Peli.maastokenttä[tileX][tileY+1].estääköLiikkumisen(Suunta.VASEN)) {
                        return 2;
                    }
                    else valitseLiikeSuunta(vihollinen);
                }
                else valitseLiikeSuunta(vihollinen);
            }
            case 3 -> {
                if (tileY > 0) {
                    if (Peli.maastokenttä[tileX][tileY-1] != null && !Peli.maastokenttä[tileX][tileY-1].estääköLiikkumisen(Suunta.VASEN)) {
                        return 3;
                    }
                    else valitseLiikeSuunta(vihollinen);
                }
                else valitseLiikeSuunta(vihollinen);
            }
            case 4 -> {
                if (tileX > 0 && tileY > 0) {
                    if (
                        Peli.maastokenttä[tileX-1][tileY] != null && !Peli.maastokenttä[tileX-1][tileY].estääköLiikkumisen(Suunta.VASEN) &&
                        Peli.maastokenttä[tileX][tileY-1] != null && !Peli.maastokenttä[tileX][tileY-1].estääköLiikkumisen(Suunta.VASEN) &&
                        Peli.maastokenttä[tileX-1][tileY-1] != null && !Peli.maastokenttä[tileX-1][tileY-1].estääköLiikkumisen(Suunta.VASEN)
                    ) {
                        return 4;
                    }
                    else valitseLiikeSuunta(vihollinen);
                }
                else valitseLiikeSuunta(vihollinen);
            }
            case 5 -> {
                if (tileX > 0 && tileY < Peli.maastokenttä.length-1) {
                    if (
                        Peli.maastokenttä[tileX-1][tileY] != null && !Peli.maastokenttä[tileX-1][tileY].estääköLiikkumisen(Suunta.VASEN) &&
                        Peli.maastokenttä[tileX][tileY+1] != null && !Peli.maastokenttä[tileX][tileY+1].estääköLiikkumisen(Suunta.VASEN) &&
                        Peli.maastokenttä[tileX-1][tileY+1] != null && !Peli.maastokenttä[tileX-1][tileY+1].estääköLiikkumisen(Suunta.VASEN)
                    ) {
                        return 5;
                    }
                    else valitseLiikeSuunta(vihollinen);
                }
                else valitseLiikeSuunta(vihollinen);
            }
            case 6 -> {
                if (tileX < Peli.maastokenttä.length-1 && tileY > 0) {
                    if (
                        Peli.maastokenttä[tileX+1][tileY] != null && !Peli.maastokenttä[tileX+1][tileY].estääköLiikkumisen(Suunta.VASEN) &&
                        Peli.maastokenttä[tileX][tileY-1] != null && !Peli.maastokenttä[tileX][tileY-1].estääköLiikkumisen(Suunta.VASEN) &&
                        Peli.maastokenttä[tileX+1][tileY-1] != null && !Peli.maastokenttä[tileX+1][tileY-1].estääköLiikkumisen(Suunta.VASEN)
                    ) {
                        return 6;
                    }
                    else valitseLiikeSuunta(vihollinen);
                }
                else valitseLiikeSuunta(vihollinen);
            }
            case 7 -> {
                if (tileX < Peli.maastokenttä.length-1 && tileY < Peli.maastokenttä.length-1) {
                    if (
                        Peli.maastokenttä[tileX+1][tileY] != null && !Peli.maastokenttä[tileX+1][tileY].estääköLiikkumisen(Suunta.VASEN) &&
                        Peli.maastokenttä[tileX][tileY+1] != null && !Peli.maastokenttä[tileX][tileY+1].estääköLiikkumisen(Suunta.VASEN) &&
                        Peli.maastokenttä[tileX+1][tileY+1] != null && !Peli.maastokenttä[tileX+1][tileY+1].estääköLiikkumisen(Suunta.VASEN)
                    ) {
                        return 7;
                    }
                    else valitseLiikeSuunta(vihollinen);
                }
                else valitseLiikeSuunta(vihollinen);
            }
        }
        return 0;
    }

    private static void bossLiikePerus(Vihollinen vihollinen) {
        if (arvoLiikeSuunta) {
            bossLiikeSuunta = valitseLiikeSuuntaRandom(vihollinen);
            arvoLiikeSuunta = false;
        }
        if (bossLiikeSuunta == 0 || bossLiikeSuunta == 4 || bossLiikeSuunta == 5) {
            vihollinen.xPeilaus = false;
        }
        else if (bossLiikeSuunta == 1 || bossLiikeSuunta == 6 || bossLiikeSuunta == 7) {
            vihollinen.xPeilaus = true;
        }
        switch (bossLiikeSuunta) {
            case 0: if (vihollinen.kokeileLiikkumista(Suunta.VASEN)) break; else tilanKesto = 0; return;
            case 1: if (vihollinen.kokeileLiikkumista(Suunta.OIKEA)) break; else tilanKesto = 0; return;
            case 2: if (vihollinen.kokeileLiikkumista(Suunta.ALAS)) break; else tilanKesto = 0; return;
            case 3: if (vihollinen.kokeileLiikkumista(Suunta.YLÖS)) break; else tilanKesto = 0; return;
            case 4: if (vihollinen.kokeileLiikkumista(Suunta.VASEN) && vihollinen.kokeileLiikkumista(Suunta.YLÖS)) break; else tilanKesto = 0; return;
            case 5: if (vihollinen.kokeileLiikkumista(Suunta.VASEN) && vihollinen.kokeileLiikkumista(Suunta.ALAS)) break; else tilanKesto = 0; return;
            case 6: if (vihollinen.kokeileLiikkumista(Suunta.OIKEA) && vihollinen.kokeileLiikkumista(Suunta.YLÖS)) break; else tilanKesto = 0; return;
            case 7: if (vihollinen.kokeileLiikkumista(Suunta.OIKEA) && vihollinen.kokeileLiikkumista(Suunta.ALAS)) break; else tilanKesto = 0; return;
            default: return;
        }
    }

    private static boolean valitseYmpyräliikkeenSuunta(Boss boss) {
        if (boss.hitbox.getCenterX()/boss.hitbox.width < Peli.maastokenttä.length/2) return false;
        else return true;
    }

    private static boolean valitseYmpyräliikkeenAlkuKohta(Boss boss) {
        if (boss.hitbox.getCenterY()/boss.hitbox.height < Peli.maastokenttä.length/2) return false;
        else return true;
    }

    private static void bossLiikeYmpyrä(Boss boss) {
        if (arvoPyörähdysSuunta) {
            vastapäivään = valitseYmpyräliikkeenSuunta(boss);
            aloitaYlöspäin = valitseYmpyräliikkeenAlkuKohta(boss);
            ympyräLiikkeenSuuruus = r.nextInt(10, 20);
            if (aloitaYlöspäin) {
                if (vastapäivään) {
                    ympyräNopeusX = -ympyräLiikkeenSuuruus;
                    kasvataYmpyräXNopeutta = true;
                    kasvataYmpyräYNopeutta = false;
                    boss.xPeilaus = true;
                } 
                else {
                    ympyräNopeusX = ympyräLiikkeenSuuruus;
                    kasvataYmpyräXNopeutta = false;
                    kasvataYmpyräYNopeutta = false;
                    boss.xPeilaus = false;
                }
            }
            else {
                if (vastapäivään) {
                    ympyräNopeusX = -ympyräLiikkeenSuuruus;
                    kasvataYmpyräXNopeutta = true;
                    kasvataYmpyräYNopeutta = true;
                    boss.xPeilaus = true;
                } 
                else {
                    ympyräNopeusX = ympyräLiikkeenSuuruus;
                    kasvataYmpyräXNopeutta = false;
                    kasvataYmpyräYNopeutta = true;
                    boss.xPeilaus = false;
                }
            }
            ympyräNopeusY = 0;
            arvoPyörähdysSuunta = false;
        }

        int alkupNopeus = boss.nopeus;
        boss.nopeus = 1;
        boss.xPeilaus = false;
        if (ympyräNopeusX < 0) {
            for (int i = 0; i > ympyräNopeusX; i--) {
                boss.kokeileLiikkumista(Suunta.VASEN);
            }
        }
        else {
            for (int i = 0; i < ympyräNopeusX; i++) {
                boss.kokeileLiikkumista(Suunta.OIKEA);
            }
        }
        if (ympyräNopeusY < 0) {
            for (int i = 0; i > ympyräNopeusY; i--) {
                boss.kokeileLiikkumista(Suunta.YLÖS);
            }
        }
        else {
            for (int i = 0; i < ympyräNopeusY; i++) {
                boss.kokeileLiikkumista(Suunta.ALAS);
            }
        }

        if (kasvataYmpyräXNopeutta) ympyräNopeusX++;
        else ympyräNopeusX--;
        if (kasvataYmpyräYNopeutta) ympyräNopeusY++;
        else ympyräNopeusY--;

        if (ympyräNopeusX >= ympyräLiikkeenSuuruus) kasvataYmpyräXNopeutta = false;
        else if (ympyräNopeusX <= -ympyräLiikkeenSuuruus) kasvataYmpyräXNopeutta = true;
        if (ympyräNopeusY >= ympyräLiikkeenSuuruus) kasvataYmpyräYNopeutta = false;
        else if (ympyräNopeusY <= -ympyräLiikkeenSuuruus) kasvataYmpyräYNopeutta = true;

        boss.nopeus = alkupNopeus;
    }

    private static void bossLiikeHyperMyötäpäivään(Vihollinen vihollinen) {
        vihollinen.xPeilaus = false;
        int alkupNopeus = vihollinen.nopeus;
        vihollinen.nopeus = 1;

        if (tilanKesto > 0) {
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
            if (vihollinen.liikuVielä > Vihollinen.liikkeenPituus/2 && vihollinen.liikeX > -10) {
                vihollinen.liikeX--;
            }
            else if (vihollinen.liikeX < 10) {
                vihollinen.liikeX++;
            }
            if ((vihollinen.liikuVielä > Vihollinen.liikkeenPituus*3/4 || vihollinen.liikuVielä < Vihollinen.liikkeenPituus/4) && vihollinen.liikeY < 10) {
                vihollinen.liikeY++;
            }
            else if (vihollinen.liikeY > -10) {
                vihollinen.liikeY--;
            }
            vihollinen.liikuVielä--;
        }
        else {
            vihollinen.liikeLoopinVaihe++;
            bossLiikeSuunta = r.nextInt(0, 8);
            vihollinen.liikeMoodi = 0;
            if (vihollinen.liikeMoodi == 0) {
                vihollinen.liikuVielä = r.nextInt(Vihollinen.liikkeenPituus/10, Vihollinen.liikkeenPituus/2);
            }
            else {
                vihollinen.liikuVielä = Vihollinen.liikkeenPituus;
                vihollinen.liikeX = 10;
                vihollinen.liikeY = 0;
            }
        }
        vihollinen.nopeus = alkupNopeus;
    }

    private static void bossLiikeHyperVastapäivään(Vihollinen vihollinen) {
        vihollinen.xPeilaus = true;
        int alkupNopeus = vihollinen.nopeus;
        vihollinen.nopeus = 1;
        if (tilanKesto > 0) {
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
            if (vihollinen.liikuVielä > Vihollinen.liikkeenPituus/2 && vihollinen.liikeX > -10) {
                vihollinen.liikeX--;
            }
            else if (vihollinen.liikeX < 10) {
                vihollinen.liikeX++;
            }
            if ((vihollinen.liikuVielä > Vihollinen.liikkeenPituus*3/4 || vihollinen.liikuVielä < Vihollinen.liikkeenPituus/4) && vihollinen.liikeY > -10) {
                vihollinen.liikeY--;
            }
            else if (vihollinen.liikeY < 10) {
                vihollinen.liikeY++;
            }
            vihollinen.liikuVielä--;
        }
        else {
            vihollinen.liikeLoopinVaihe++;
            bossLiikeSuunta = r.nextInt(0, 8);
            vihollinen.liikeMoodi = 0;
            if (vihollinen.liikeMoodi == 0) {
                vihollinen.liikuVielä = r.nextInt(Vihollinen.liikkeenPituus/10, Vihollinen.liikkeenPituus/2);
            }
            else {
                vihollinen.liikuVielä = Vihollinen.liikkeenPituus;
                vihollinen.liikeX = 10;
                vihollinen.liikeY = 0;
            }
        }
        vihollinen.nopeus = alkupNopeus;
    }

    private static void bossAmmus(Boss boss) {
        if (!ampunut) {
            Peli.entityLista.add(new Ammus((int)boss.hitbox.getCenterX(), (int)boss.hitbox.getCenterY(), Suunta.VASEN, boss.ammusVahinko, boss));
            Peli.entityLista.add(new Ammus((int)boss.hitbox.getCenterX(), (int)boss.hitbox.getCenterY(), Suunta.OIKEA, boss.ammusVahinko, boss));
            Peli.entityLista.add(new Ammus((int)boss.hitbox.getCenterX(), (int)boss.hitbox.getCenterY(), Suunta.ALAS, boss.ammusVahinko, boss));
            Peli.entityLista.add(new Ammus((int)boss.hitbox.getCenterX(), (int)boss.hitbox.getCenterY(), Suunta.YLÖS, boss.ammusVahinko, boss));
            Peli.entityLista.add(new Ammus((int)boss.hitbox.getCenterX(), (int)boss.hitbox.getCenterY(), Suunta.YLÄVASEN, boss.ammusVahinko, boss));
            Peli.entityLista.add(new Ammus((int)boss.hitbox.getCenterX(), (int)boss.hitbox.getCenterY(), Suunta.YLÄOIKEA, boss.ammusVahinko, boss));
            Peli.entityLista.add(new Ammus((int)boss.hitbox.getCenterX(), (int)boss.hitbox.getCenterY(), Suunta.ALAVASEN, boss.ammusVahinko, boss));
            Peli.entityLista.add(new Ammus((int)boss.hitbox.getCenterX(), (int)boss.hitbox.getCenterY(), Suunta.ALAOIKEA, boss.ammusVahinko, boss));
            Point sijainti = new Point((int)boss.hitbox.getCenterX(), (int)boss.hitbox.getCenterY());
            Äänet.toistaSFX("ammus", sijainti);
            ampunut = true;
        }
    }

    private static void bossHuuto(Vihollinen vihollinen) {
        if (!huutanut) {
            Äänet.toistaSFX(vihollinen.ominaisHuuto);
            huutanut = true;
        }
    }
}
