package keimo.kenttäkohteet.kiintopiste;

import keimo.kenttäkohteet.esine.Esine;

import java.util.ArrayList;

public abstract class Säiliö extends Kiintopiste {

    protected Esine sisältö;

    public Säiliö(int sijX, int sijY, ArrayList<String> lisäOminaisuudet) {
        super(sijX, sijY, lisäOminaisuudet);
    }

    protected Esine luoSisältö(String esineenNimi, ArrayList<String> ominaisuusLista) {
        return Esine.luoEsine(esineenNimi, ominaisuusLista);
    }

    public void asetaSisältö(String esineenNimi, ArrayList<String> ominaisuusLista) {
        this.sisältö = luoSisältö(esineenNimi, null);
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
