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

    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[1];
        String triggeritString = "";
        for (Point p : vaaditutTriggerit) {
            triggeritString += "" + p.x + "_" + p.y + ";";
        }
        this.lisäOminaisuudet[0] = "triggerit=" + triggeritString;
    }
    
    AvattavaEste(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY);

        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("triggerit=")) {
                    String[] triggerit = ominaisuus.substring(10).split(";");
                    for (String s : triggerit) {
                        String[] xy = s.split("_");
                        try {
                            int x = Integer.parseInt(xy[0]);
                            int y = Integer.parseInt(xy[1]);
                            vaaditutTriggerit.add(new Point(x, y));
                        }
                        catch (NumberFormatException nfe) {
                            System.out.println("Triggerilistan parsiminen epäonnistui.");
                            nfe.printStackTrace();
                        }
                    }
                }
            }
            päivitäLisäOminaisuudet();
        }
        else {
            this.lisäOminaisuuksia = false;
        }
        
        super.asetaTiedot();
    }
}
