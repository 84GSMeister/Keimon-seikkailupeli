package keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC;

import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;

import java.util.ArrayList;

public abstract class NPC_KenttäKohde extends KenttäKohde {
    
    protected int hp = 10;
    protected String valittuDialogi = "";
    protected ArrayList<String> dialogit = new ArrayList<>();

    public NPC_KenttäKohde(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("dialogi=")) {
                    this.valittuDialogi = ominaisuus.substring(8);
                }
            }
        }
        else {
            if (this.dialogit.size() > 0) {
                this.valittuDialogi = this.dialogit.get(0);
            }
        }
        päivitäLisäOminaisuudet();
    }

    public abstract void juttele();

    public String annaDialogi() {
        return valittuDialogi;
    }

    public ArrayList<String> annaDialogiLista() {
        return dialogit;
    }

    private void päivitäLisäOminaisuudet() {
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
