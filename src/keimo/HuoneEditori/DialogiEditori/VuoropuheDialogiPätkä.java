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

    public int annaPituus() {
        return dialoginPituus;
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

    public VuoropuheDialogiPätkä(String vuoropuheTunniste, int dialoginPituus, Icon[] dialogiKuvat, String[] dialogiTekstit, String[] dialogiPuhujat) {
        this.vuoropuheTunniste = vuoropuheTunniste;
        this.dialoginPituus = dialoginPituus;
        this.dialogiKuvat = dialogiKuvat;
        this.dialogiTekstit = dialogiTekstit;
        this.dialogiPuhujat = dialogiPuhujat;
    }
}
