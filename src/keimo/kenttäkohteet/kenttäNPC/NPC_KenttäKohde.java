package keimo.kenttäkohteet.kenttäNPC;

import keimo.kenttäkohteet.KenttäKohde;

import java.util.ArrayList;

public abstract class NPC_KenttäKohde extends KenttäKohde {
    
    protected int hp = 10;
    protected String valittuDialogi = "";
    protected ArrayList<String> dialogit = new ArrayList<>();

    public NPC_KenttäKohde(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY);
        if (ominaisuusLista != null) {
            this.lisäOminaisuudet = new ArrayList<>();
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("dialogi=")) {
                    asetaDialogi(ominaisuus.substring(8));
                }
            }
            päivitäLisäOminaisuudet();
        }
        else {
            if (this.dialogit.size() > 0) {
                this.valittuDialogi = this.dialogit.get(0);
                päivitäLisäOminaisuudet();
            }
        }
    }

    public abstract void juttele();

    public String annaDialogi() {
        return valittuDialogi;
    }

    public ArrayList<String> annaDialogiLista() {
        return dialogit;
    }

    public void asetaDialogi(String dialogi) {
        this.valittuDialogi = dialogi;
        päivitäLisäOminaisuudet();
    }

    public void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet == null) this.lisäOminaisuudet = new ArrayList<>();
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("dialogi="));
        this.lisäOminaisuudet.add("dialogi=" + this.valittuDialogi);
    }

    protected void valitseVakioDialogi() {
        if (this.dialogit.size() > 0) {
            this.valittuDialogi = this.dialogit.get(0);
            päivitäLisäOminaisuudet();
        }
    }
}
