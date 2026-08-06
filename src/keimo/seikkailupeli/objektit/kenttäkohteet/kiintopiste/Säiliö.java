package keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste;

import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Esine;

import java.util.ArrayList;

public abstract class Säiliö extends Kiintopiste {

    protected Esine sisältö;

    public Säiliö(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        if (ominaisuusLista != null) {
            String esineenNimi = "";
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("sisältö=")) {
                    esineenNimi = ominaisuus.substring(8);
                }
            }
            this.sisältö = luoSisältö(esineenNimi, ominaisuusLista);
            päivitäLisäOminaisuudet();
        }
    }

    protected Esine luoSisältö(String esineenNimi, ArrayList<String> ominaisuusLista) {
        return Esine.luoEsine(esineenNimi, ominaisuusLista);
    }

    private void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet != null) {
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("sisältö="));
            this.lisäOminaisuudet.add("sisältö=" + this.annaSisältö());
        }
    }

    public String annaSisältö() {
        if (this.sisältö != null) {
            return sisältö.annaNimi();
        }
        else {
            return "tyhjä";
        }
    }

    public Esine annaSisältöEsine() {
        return sisältö;
    }
}
