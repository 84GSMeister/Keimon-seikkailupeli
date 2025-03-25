package keimo.kenttäkohteet.kiintopiste;

import keimo.kenttäkohteet.esine.Esine;

public class Säiliö extends Kiintopiste {

    protected Esine sisältö;

    protected Esine luoSisältö(String esineenNimi, String[] ominaisuusLista) {
        return Esine.luoEsine(esineenNimi, ominaisuusLista);
    }

    public void asetaSisältö(String esineenNimi, String[] ominaisuusLista) {
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
    
    public Säiliö(boolean määritettySijainti, int sijX, int sijY, String[] lisäOminaisuudet) {
        super(määritettySijainti, sijX, sijY, lisäOminaisuudet);
    }
}
