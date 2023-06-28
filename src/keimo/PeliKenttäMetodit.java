package keimo;

import keimo.Kenttäkohteet.*;
import keimo.Kenttäkohteet.Käännettävä.Suunta;
import keimo.Maastot.*;
import keimo.NPCt.*;
import keimo.TarkistettavatArvot.PelinLopetukset;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;

public class PeliKenttäMetodit {
    
    static KenttäKohde[][] pelikenttä;
    static Maasto[][] maastokenttä;

    public static void suoritaPelikenttäMetoditJoka2Tick() {
        tarkistaVartijanAktiivisuus();
    }

    public static void suoritaPelikenttäMetoditJokaTick() {
        tarkistaVihollisCollision();
        Pelaaja.vähennäKuolemattomuusAikaa();
        Peli.vähennäKäyttöViivettä();
        liikutaVihollisia();
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
                                    if (vihollinen instanceof Vartija) {
                                        Vartija vartija = (Vartija)vihollinen;
                                        if (vartija.liikkuu) {
                                            int etäisyysX = (int)(Pelaaja.hitbox.getCenterX() - vihollinen.hitbox.getCenterX());
                                            int etäisyysY = (int)(Pelaaja.hitbox.getCenterY() - vihollinen.hitbox.getCenterY());
                                            int etäisyysXits = (int)Math.abs(etäisyysX);
                                            int etäisyysYits = (int)Math.abs(etäisyysY);
                                            if (etäisyysXits > etäisyysYits) {
                                                if (etäisyysX < 0) {
                                                    vartija.kokeileLiikkumista(Suunta.VASEN);
                                                }
                                                else {
                                                    vartija.kokeileLiikkumista(Suunta.OIKEA);
                                                }
                                            }
                                            else {
                                                if (etäisyysY < 0) {
                                                    vartija.kokeileLiikkumista(Suunta.YLÖS);
                                                }
                                                else {
                                                    vartija.kokeileLiikkumista(Suunta.ALAS);
                                                }
                                            }
                                        }
                                    }
                                    else {
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
                                    }
                                break;
                                case STAATTINEN:
                                break;
                            }
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
                                        else if (Pelaaja.viimeisinOsunutVihollinen instanceof Vartija) {
                                            TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.VARTIJA;
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

    public static void tarkistaVartijanAktiivisuus() {
        try {
            if (Peli.huone.annaId() == 14) {
                for (NPC npc : Peli.npcLista) {
                    if (npc instanceof Vartija) {
                        Vartija vartija = (Vartija)npc;
                        if (Pelaaja.ostostenHintaYhteensä > 0 && Pelaaja.sijY <= 4) {
                            vartija.tekeeVahinkoa = true;
                            vartija.liikkuu = true;
                        }
                        else {
                            vartija.tekeeVahinkoa = false;
                            vartija.liikkuu = false;
                        }
                    }
                }
            }
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin vartijan liikkeen tarkistus peruttiin (konkurrenssi-issue).");
            cme.printStackTrace();
        }
    }

    static class PathFindingExample {

        public static class Point {
            public int x;
            public int y;
            public Point previous;
    
            public Point(int x, int y, Point previous) {
                this.x = x;
                this.y = y;
                this.previous = previous;
            }
    
            @Override
            public String toString() { return String.format("(%d, %d)", x, y); }
    
            @Override
            public boolean equals(Object o) {
                Point point = (Point) o;
                return x == point.x && y == point.y;
            }
    
            @Override
            public int hashCode() { return Objects.hash(x, y); }
    
            public Point offset(int ox, int oy) { return new Point(x + ox, y + oy, this);  }
        }
    
        public static boolean IsWalkable(int[][] map, Point point) {
            if (point.y < 0 || point.y > map.length - 1) return false;
            if (point.x < 0 || point.x > map[0].length - 1) return false;
            if (Peli.maastokenttä[point.x][point.y] instanceof EsteTile) return false;
            return map[point.y][point.x] == 0;
        }
    
        public static List<Point> FindNeighbors(int[][] map, Point point) {
            List<Point> neighbors = new ArrayList<>();
            Point up = point.offset(0,  1);
            Point down = point.offset(0,  -1);
            Point left = point.offset(-1, 0);
            Point right = point.offset(1, 0);
            if (IsWalkable(map, up)) neighbors.add(up);
            if (IsWalkable(map, down)) neighbors.add(down);
            if (IsWalkable(map, left)) neighbors.add(left);
            if (IsWalkable(map, right)) neighbors.add(right);
            return neighbors;
        }
    
        public static List<Point> FindPath(int[][] map, Point start, Point end) {
            boolean finished = false;
            List<Point> used = new ArrayList<>();
            used.add(start);
            while (!finished) {
                List<Point> newOpen = new ArrayList<>();
                for(int i = 0; i < used.size(); ++i){
                    Point point = used.get(i);
                    for (Point neighbor : FindNeighbors(map, point)) {
                        if (!used.contains(neighbor) && !newOpen.contains(neighbor)) {
                            newOpen.add(neighbor);
                        }
                    }
                }
    
                for(Point point : newOpen) {
                    used.add(point);
                    if (end.equals(point)) {
                        finished = true;
                        break;
                    }
                }
    
                if (!finished && newOpen.isEmpty())
                    return null;
            }
    
            List<Point> path = new ArrayList<>();
            Point point = used.get(used.size() - 1);
            while(point.previous != null) {
                path.add(0, point);
                point = point.previous;
            }
            return path;
        }
    
        public static void run() {
            int[][] map = {
                    {0, 0, 0, 0, 0},
                    {0, 0, 1, 0, 1},
                    {1, 0, 0, 1, 1},
                    {0, 0, 0, 1, 0},
                    {1, 1, 0, 0, 1}
            };
    
            Point start = new Point(0, 0, null);
            Point end = new Point(3, 4, null);
            List<Point> path = FindPath(map, start, end);
            if (path != null) {
                for (Point point : path) {
                    System.out.println(point);
                }
            }
            else
                System.out.println("No path found");
        }
    }
}
