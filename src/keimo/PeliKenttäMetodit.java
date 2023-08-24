package keimo;

import keimo.Kenttäkohteet.*;
import keimo.Kenttäkohteet.Käännettävä.Suunta;
import keimo.Maastot.*;
import keimo.NPCt.*;
import keimo.Ruudut.PeliRuutu;
import keimo.TarkistettavatArvot.PelinLopetukset;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;

public class PeliKenttäMetodit {
    
    static KenttäKohde[][] pelikenttä;
    static Maasto[][] maastokenttä;

    public static boolean teleporttaaViholliset = false;

    public static void suoritaPelikenttäMetoditJoka10Tick() {
        suoritaReitinhakuVihollisille();
    }

    public static void suoritaPelikenttäMetoditJoka2Tick() {
        tarkistaVartijanAktiivisuus();
        tarkistaVihollistenLiikerata();
    }

    public static void suoritaPelikenttäMetoditJokaTick() {
        if (teleporttaaViholliset) {
            teleporttaaViholliset();
        }
        tarkistaVihollisCollision();
        Pelaaja.vähennäKuolemattomuusAikaa();
        Peli.vähennäKäyttöViivettä();
        liikutaVihollisia();
    }

    static void teleporttaaViholliset() {
        try {
            for (NPC npc : Peli.npcLista) {
                npc.teleport(npc.annaAlkuSijX(), npc.annaAlkuSijY());
            }
        }
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin vihollisten teleporttaus peruutettiin (konkurrenssi-issue)");
            cme.printStackTrace();
        }
        finally {
            teleporttaaViholliset = false;
        }
    }

    static void tarkistaVihollistenLiikerata() {
        Vihollinen.liikkeenPituus = PeliRuutu.esineenKokoPx;
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
                                case SEURAA_REITTIÄ:
                                    if (vihollinen.reitti != null) {
                                        if (vihollinen.reitti.size() >= 1) {
                                            PathFindingExample.Point seuraavaPiste = vihollinen.reitti.get(0);
                                            if (vihollinen.sijX == seuraavaPiste.x && vihollinen.sijY == seuraavaPiste.y) {
                                                vihollinen.reitti.remove(0);
                                            }
                                            else {
                                                if (vihollinen.sijX > seuraavaPiste.x) {
                                                    vihollinen.kokeileLiikkumista(Suunta.VASEN);
                                                }
                                                else if (vihollinen.sijX < seuraavaPiste.x) {
                                                    vihollinen.kokeileLiikkumista(Suunta.OIKEA);
                                                }
                                                if (vihollinen.sijY < seuraavaPiste.y) {
                                                    vihollinen.kokeileLiikkumista(Suunta.ALAS);
                                                }
                                                else if (vihollinen.sijY > seuraavaPiste.y) {
                                                    vihollinen.kokeileLiikkumista(Suunta.YLÖS);
                                                }
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
        try {
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
        catch (ConcurrentModificationException cme) {
            System.out.println("Viimeisin collision-tarkistus peruutettiin (konkurrenssi-issue).");
            //cme.printStackTrace();
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

    public static void suoritaReitinhakuVihollisille() {
        try {
            if (Peli.npcLista != null) {
                for (NPC npc : Peli.npcLista) {
                    if (npc instanceof Vihollinen) {
                        Vihollinen vihollinen = (Vihollinen)npc;
                        PathFindingExample.reitinHakuPelaajaaKohti(vihollinen);
                    }
                }
            }
        }
        catch (ConcurrentModificationException e) {
            System.out.println("Viimeisin vihollisten reitinhaun laskeminen peruutettiin (konkurrenssi-issue)");
        }
    }

    public static class PathFindingExample {

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

        public static boolean voiKävellä(Maasto[][] map, Point point) {
            if (point.y < 0 || point.y > map.length - 1) return false;
            if (point.x < 0 || point.x > map[0].length - 1) return false;
            if (Peli.maastokenttä[point.x][point.y] instanceof EsteTile) return false;
            if (Peli.maastokenttä[point.x][point.y] instanceof Tile) return true;
            else return false;
        }

        public static List<Point> etsiViereiset(Maasto[][] map, Point point) {
            List<Point> neighbors = new ArrayList<>();
            Point up = point.offset(0,  1);
            Point down = point.offset(0,  -1);
            Point left = point.offset(-1, 0);
            Point right = point.offset(1, 0);
            if (voiKävellä(map, up)) neighbors.add(up);
            if (voiKävellä(map, down)) neighbors.add(down);
            if (voiKävellä(map, left)) neighbors.add(left);
            if (voiKävellä(map, right)) neighbors.add(right);
            return neighbors;
        }

        public static List<Point> etsiReitti(Maasto[][] map, Point start, Point end) {
            boolean finished = false;
            List<Point> used = new ArrayList<>();
            used.add(start);
            while (!finished) {
                List<Point> newOpen = new ArrayList<>();
                for(int i = 0; i < used.size(); ++i){
                    Point point = used.get(i);
                    for (Point neighbor : etsiViereiset(map, point)) {
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

        public static void reitinHakuPelaajaaKohti(Vihollinen vihollinen) {
            
            Maasto[][] kartta = Peli.maastokenttä;
    
            Point alku = new Point(vihollinen.sijX, vihollinen.sijY, null);
            Point loppu = new Point(Pelaaja.sijX, Pelaaja.sijY, null);
            List<Point> path = etsiReitti(kartta, alku, loppu);
            vihollinen.reitti = path;
        }
    }
}
