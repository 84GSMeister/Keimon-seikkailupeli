package keimo.HuoneEditori.DialogiEditori;

import java.text.DecimalFormat;

import javax.swing.Icon;

public class VuoropuheDialogiPätkä {

    protected static DecimalFormat df = new DecimalFormat("##.##");

    public String vuoropuheTunniste;
    private int dialoginPituus = 0;
    private Icon[] dialogiKuvat;
    private String[] dialogiTekstit;
    private String[] dialogiPuhujat;
    private boolean valinta = false;
    private String valinnanNimi;
    private String valinnanOtsikko;
    private String[] valinnanVaihtoehdot;
    private String[] valinnanVaihtoehtojenKohdeDialogit;
    private String[] triggerit;

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

    public Icon[] annaKuvat() {
        return dialogiKuvat;
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

    public VuoropuheDialogiPätkä(String vuoropuheTunniste, int dialoginPituus, Icon[] dialogiKuvat, String[] dialogiTekstit, String[] dialogiPuhujat) {
        this.vuoropuheTunniste = vuoropuheTunniste;
        this.dialoginPituus = dialoginPituus;
        this.dialogiKuvat = dialogiKuvat;
        this.dialogiTekstit = dialogiTekstit;
        this.dialogiPuhujat = dialogiPuhujat;
    }

    public VuoropuheDialogiPätkä(String vuoropuheTunniste, int dialoginPituus, Icon[] dialogiKuvat, String[] dialogiTekstit, String[] dialogiPuhujat, boolean valinta, String valinnanNimi, String valinnanOtsikko, String[] valinnanVaihtoehdot, String[] valinnanVaihtoehtojenKohdeDialogit, String[] triggerit) {
        this.vuoropuheTunniste = vuoropuheTunniste;
        this.dialoginPituus = dialoginPituus;
        this.dialogiKuvat = dialogiKuvat;
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
