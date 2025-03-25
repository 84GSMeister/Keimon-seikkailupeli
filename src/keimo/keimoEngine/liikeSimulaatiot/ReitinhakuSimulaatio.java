package keimo.keimoEngine.liikeSimulaatiot;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.Maastot.Maasto;
import keimo.Utility.Käännettävä.Suunta;
import keimo.entityt.Entity;
import keimo.entityt.npc.Vihollinen;
import keimo.entityt.npc.Vihollinen.LiikeTapa;

public class ReitinhakuSimulaatio {
    
    public static void simuloiReitinhaku(Vihollinen vihollinen) {
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
    }

    public static void suoritaReitinhakuVihollisille() {
        try {
            if (Peli.entityLista != null) {
                for (Entity npc : Peli.entityLista) {
                    if (npc instanceof Vihollinen) {
                        Vihollinen vihollinen = (Vihollinen)npc;
                        if (vihollinen.liikeTapa == LiikeTapa.SEURAA_REITTIÄ) {
                            PathFindingExample.reitinHakuPelaajaaKohti(vihollinen);
                        }
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
            if (Peli.maastokenttä[point.x][point.y] != null) {
                if (Peli.maastokenttä[point.x][point.y].estääköLiikkumisen(null)) return false;
                else return true;
            }
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
