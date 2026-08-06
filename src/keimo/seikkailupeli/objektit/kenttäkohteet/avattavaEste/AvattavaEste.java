package keimo.seikkailupeli.objektit.kenttäkohteet.avattavaEste;

import keimo.keimoengine.collision.Piste;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.objektit.kenttäkohteet.triggeri.Triggeri;

import java.util.ArrayList;

public abstract class AvattavaEste extends KenttäKohde {

    protected boolean avattu = false;
    protected ArrayList<Piste> vaaditutTriggerit = new ArrayList<>();

    public AvattavaEste(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.este = true;
        if (ominaisuusLista != null) {
            //this.lisäOminaisuudet = new ArrayList<>();
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("triggerit=")) {
                    String[] triggerit = ominaisuus.substring(10).split(";");
                    for (String s : triggerit) {
                        String[] xy = s.split("_");
                        try {
                            int x = Integer.parseInt(xy[0]);
                            int y = Integer.parseInt(xy[1]);
                            vaaditutTriggerit.add(new Piste(x, y));
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
        // else {
        //     this.lisäOminaisuuksia = false;
        // }
        
        super.asetaTiedot();
    }

    public void tarkistaTriggerit() {
        int aktivoimattomatTriggerit = vaaditutTriggerit.size();
        for (Piste piste : vaaditutTriggerit) {
            if (Peli.annaObjektiKenttä()[(int)piste.annaX()][(int)piste.annaY()] instanceof Triggeri) {
                Triggeri trg = (Triggeri)Peli.annaObjektiKenttä()[(int)piste.annaX()][(int)piste.annaY()];
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

    public ArrayList<Piste> annaVaaditutTriggerit() {
        return this.vaaditutTriggerit;
    }

    public void lisääTriggeri(int x, int y) {
        this.vaaditutTriggerit.add(new Piste(x, y));
        super.asetaTiedot();
    }

    public void tyhjennäTriggerit() {
        this.vaaditutTriggerit.clear();
        super.asetaTiedot();
    }

    private void päivitäLisäOminaisuudet() {
        //super.päivitäLisäOminaisuudet(ominaisuusLista);
        if (this.lisäOminaisuudet != null) {
            //this.lisäOminaisuuksia = true;
            String triggeritString = "";
            for (Piste p : vaaditutTriggerit) {
                triggeritString += "" + p.x + "_" + p.y + ";";
            }
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("triggerit="));
            this.lisäOminaisuudet.add("triggerit=" + triggeritString);
        }
    }
}
