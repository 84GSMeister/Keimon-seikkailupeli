package keimo.Kenttäkohteet;

import java.awt.Point;
import java.util.ArrayList;

import keimo.Peli;

public abstract class AvattavaEste extends KenttäKohde {

    protected boolean avattu = false;
    protected ArrayList<Point> vaaditutTriggerit = new ArrayList<>();

    public void tarkistaTriggerit() {
        int aktivoimattomatTriggerit = vaaditutTriggerit.size();
        for (Point piste : vaaditutTriggerit) {
            if (Peli.pelikenttä[(int)piste.getX()][(int)piste.getY()] instanceof Triggeri) {
                Triggeri trg = (Triggeri)Peli.pelikenttä[(int)piste.getX()][(int)piste.getY()];
                if (trg.onkoTriggeröity()) {
                    aktivoimattomatTriggerit--;
                }
            }
        }
        //System.out.println(aktivoimattomatTriggerit);
        if (aktivoimattomatTriggerit <= 0) {
            avaa(true);
        }
        else {
            avaa(false);
        }
    }

    protected void avaa(boolean avaus) {
        avattu = avaus;
    }

    public boolean onkoAvattu() {
        return this.avattu;
    }

    public ArrayList<Point> annaVaaditutTriggerit() {
        return this.vaaditutTriggerit;
    }

    public void lisääTriggeri(int x, int y) {
        this.vaaditutTriggerit.add(new Point(x, y));
        super.asetaTiedot();
    }

    public void tyhjennäTriggerit() {
        this.vaaditutTriggerit.clear();
        super.asetaTiedot();
    }
    
    AvattavaEste(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }
}
