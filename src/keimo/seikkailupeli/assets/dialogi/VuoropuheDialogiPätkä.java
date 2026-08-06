package keimo.seikkailupeli.assets.dialogi;

import java.text.DecimalFormat;
import java.util.Arrays;

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
        dialoginPituus++;
        dialogiTekstit = Arrays.copyOf(dialogiTekstit, dialoginPituus);
        dialogiTekstit[dialoginPituus-1] = "teksti " + (dialoginPituus-1);
        dialogiPuhujat = Arrays.copyOf(dialogiPuhujat, dialoginPituus);
        dialogiPuhujat[dialoginPituus-1] = "puhuja " + (dialoginPituus-1);
        dialogiKuvienTiedostoNimet = Arrays.copyOf(dialogiKuvienTiedostoNimet, dialoginPituus);
        dialogiKuvienTiedostoNimet[dialoginPituus-1] = "";
    }

    public void poistaSivu(int sivunumero) {
        String[] dialogiTekstitAlku = Arrays.copyOfRange(dialogiTekstit, 0, sivunumero);
        String[] dialogiTekstitLoppu = Arrays.copyOfRange(dialogiTekstit, sivunumero+1, dialoginPituus);
        String[] dialogiTekstitUusi = new String[dialogiTekstitAlku.length + dialogiTekstitLoppu.length];
        int i = 0;
        for (String s : dialogiTekstitAlku) {
            dialogiTekstitUusi[i] = s;
            i++;
        }
        for (String s : dialogiTekstitLoppu) {
            dialogiTekstitUusi[i] = s;
            i++;
        }
        dialogiTekstit = dialogiTekstitUusi;


        String[] dialogiPuhujatAlku = Arrays.copyOfRange(dialogiPuhujat, 0, sivunumero);
        String[] dialogiPuhujatLoppu = Arrays.copyOfRange(dialogiPuhujat, sivunumero+1, dialoginPituus);
        String[] dialogiPuhujatUusi = new String[dialogiPuhujatAlku.length + dialogiPuhujatLoppu.length];
        i = 0;
        for (String s : dialogiPuhujatAlku) {
            dialogiPuhujatUusi[i] = s;
            i++;
        }
        for (String s : dialogiPuhujatLoppu) {
            dialogiPuhujatUusi[i] = s;
            i++;
        }
        dialogiPuhujat = dialogiPuhujatUusi;


        String[] dialogiKuvatAlku = Arrays.copyOfRange(dialogiKuvienTiedostoNimet, 0, sivunumero);
        String[] dialogiKuvatLoppu = Arrays.copyOfRange(dialogiKuvienTiedostoNimet, sivunumero+1, dialoginPituus);
        String[] dialogiKuvatUusi = new String[dialogiKuvatAlku.length + dialogiKuvatLoppu.length];
        i = 0;
        for (String s : dialogiKuvatAlku) {
            dialogiKuvatUusi[i] = s;
            i++;
        }
        for (String s : dialogiKuvatLoppu) {
            dialogiKuvatUusi[i] = s;
            i++;
        }
        dialogiKuvienTiedostoNimet = dialogiKuvatUusi;
        dialoginPituus--;
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

    public VuoropuheDialogiPätkä(int id, String vuoropuheTunniste, int dialoginPituus, String[] dialogiKuvienTiedostoNimet, String[] dialogiTekstit, String[] dialogiPuhujat, boolean valinta, String valinnanNimi, String valinnanOtsikko, String[] valinnanVaihtoehdot, String[] valinnanVaihtoehtojenKohdeDialogit, String[] triggerit) {
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
        this.id = id;
    }
}
