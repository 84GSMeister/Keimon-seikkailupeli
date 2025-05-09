package keimo.kenttäkohteet.avattavaEste;

import java.awt.Point;
import java.util.ArrayList;

import keimo.kenttäkohteet.KenttäKohde;
import keimo.kenttäkohteet.triggeri.Triggeri;
import keimo.Peli;

public abstract class AvattavaEste extends KenttäKohde {

    protected boolean avattu = false;
    protected ArrayList<Point> vaaditutTriggerit = new ArrayList<>();

    public AvattavaEste(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY);
        super.este = true;
        if (ominaisuusLista != null) {
            this.lisäOminaisuudet = new ArrayList<>();
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
        if (aktivoimattomatTriggerit <= 0) {
            avaa(true);
        }
        else {
            avaa(false);
        }
    }

    protected void avaa(boolean avaus) {
        avattu = avaus;
        este = !avaus;
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
        if (this.lisäOminaisuudet != null) {
            this.lisäOminaisuuksia = true;
            String triggeritString = "";
            for (Point p : vaaditutTriggerit) {
                triggeritString += "" + p.x + "_" + p.y + ";";
            }
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("triggerit="));
            this.lisäOminaisuudet.add("triggerit=" + triggeritString);
        }
    }
}
