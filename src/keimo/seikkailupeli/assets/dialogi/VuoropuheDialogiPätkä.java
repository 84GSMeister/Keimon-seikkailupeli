package keimo.seikkailupeli.assets.dialogi;

import java.text.DecimalFormat;

public class VuoropuheDialogiPätkä {

    protected static DecimalFormat df = new DecimalFormat("##.##");

    protected int id;
    public String vuoropuheTunniste;
    private int dialoginPituus = 0;
    private String[] dialogiKuvienTiedostoNimet;
    private String[] dialogiTekstit;
    private String[] dialogiPuhujat;
    private boolean valinta = false;
    private String valinnanNimi;
    private String valinnanOtsikko;
    private String[] valinnanVaihtoehdot;
    private String[] valinnanVaihtoehtojenKohdeDialogit;
    private String[] triggerit;

    static int tarinaId = 0;

    public static void nollaaTarinaId() {
        tarinaId = 0;
    }

    public int annaId() {
        return id;
    }

    public String annaTunniste() {
        return vuoropuheTunniste;
    }

    public int annaPituus() {
        return dialoginPituus;
    }

    public void lisääSivu() {
        this.dialoginPituus++;
    }

    public void poistaSivu() {
        this.dialoginPituus--;
    }

    public String[] annaKuvienTiedostoNimet() {
        return dialogiKuvienTiedostoNimet;
    }

    public String[] annaTekstit() {
        return dialogiTekstit;
    }

    public String[] annaPuhujat() {
        return dialogiPuhujat;
    }

    public boolean onkoValinta() {
        return valinta;
    }

    public String annaValinnanNimi() {
        return valinnanNimi;
    }

    public String annaValinnanOtsikko() {
        return valinnanOtsikko;
    }

    public String[] annaValinnanVaihtoehdot() {
        return valinnanVaihtoehdot;
    }

    public String[] annaValinnanVaihtoehtojenKohdeDialogit() {
        return valinnanVaihtoehtojenKohdeDialogit;
    }

    public String[] annaTriggerit() {
        return triggerit;
    }

    public VuoropuheDialogiPätkä(String vuoropuheTunniste, int dialoginPituus, String[] dialogiKuvienTiedostoNimet, String[] dialogiTekstit, String[] dialogiPuhujat, String[] dialogiÄänet, boolean valinta, String valinnanNimi, String valinnanOtsikko, String[] valinnanVaihtoehdot, String[] valinnanVaihtoehtojenKohdeDialogit, String[] triggerit) {
        this.vuoropuheTunniste = vuoropuheTunniste;
        this.dialoginPituus = dialoginPituus;
        this.dialogiKuvienTiedostoNimet = dialogiKuvienTiedostoNimet;
        this.dialogiTekstit = dialogiTekstit;
        this.dialogiPuhujat = dialogiPuhujat;
        this.valinta = valinta;
        this.valinnanNimi = valinnanNimi;
        this.valinnanOtsikko = valinnanOtsikko;
        this.valinnanVaihtoehdot = valinnanVaihtoehdot;
        this.valinnanVaihtoehtojenKohdeDialogit = valinnanVaihtoehtojenKohdeDialogit;
        this.triggerit = triggerit;
    }
}
