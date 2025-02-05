package keimo.HuoneEditori.DialogiEditori;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PääIkkuna;
import keimo.keimoEngine.toiminnot.Dialogit;

import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class VuoropuheDialogit {

    protected static DecimalFormat df = new DecimalFormat("##.##");
    public static int dialoginPituus = 0;
    public static Icon[] dialogiKuvat;
    public static String[] dialogiTekstit;
    public static String[] dialogiPuhujat;
    public static String vuoropuheTunniste;
    public static boolean valinta = false;
    public static int vaihtoehtojenMäärä = 0;
    public static String valinnanNimi;
    public static String valinnanOtsikko;
    public static String[] valinnanVaihtoehdot;
    public static String[] valinnanVaihtoehtojenKohdeDialogit;
    public static String[] vaihtoehtojenTriggerit;

    public static HashMap<String, VuoropuheDialogiPätkä> vuoropuheDialogiKartta = new HashMap<>();

    public static void siirrySeuraavaanDialogiRuutuun(int ruudunNro) {
        if (ruudunNro >= 0) {
            if (dialogiKuvat.length > ruudunNro && dialogiTekstit.length > ruudunNro && dialogiPuhujat.length > ruudunNro) {
                if (Peli.legacy) {
                    PääIkkuna.avaaDialogi(dialogiKuvat[ruudunNro], dialogiTekstit[ruudunNro], dialogiPuhujat[ruudunNro]);
                    System.out.println("" + dialogiKuvat[ruudunNro] + " " + dialogiTekstit[ruudunNro] + " " + dialogiPuhujat[ruudunNro]);
                    PääIkkuna.dialogiaJäljellä--;
                }
                else {
                    Dialogit.avaaDialogi("", dialogiTekstit[ruudunNro], dialogiPuhujat[ruudunNro]);
                }
            }
        }
    }

    public static boolean luoYksityiskohtainenVuoropuheRuutu(String vuoropuheRuudunTunniste) {
        //if (vuoropuheRuudunTunniste != null) {
            PääIkkuna.useitaRuutuja = true;
            vuoropuheTunniste = vuoropuheRuudunTunniste;
            if (vuoropuheDialogiKartta.containsKey(vuoropuheRuudunTunniste)) {
                VuoropuheDialogiPätkä dp = vuoropuheDialogiKartta.get(vuoropuheRuudunTunniste);
                if (dp != null) {
                    PääIkkuna.dialogiaJäljellä = dp.annaPituus();
                    dialoginPituus = dp.annaPituus();
                    dialogiKuvat = new ImageIcon[dp.annaPituus()];
                    dialogiTekstit = new String[dp.annaPituus()];
                    dialogiPuhujat = new String[dp.annaPituus()];
                    for (int i = 0; i < dp.annaPituus(); i++) {
                        dialogiKuvat[i] = dp.annaKuvat()[i];
                        dialogiTekstit[i] = dp.annaTekstit()[i];
                        dialogiPuhujat[i] = dp.annaPuhujat()[i];
                    }
                    if (dp.onkoValinta()) {
                        
                    }
                }
                return true;
            }
            else {
                switch (vuoropuheRuudunTunniste) {
            //     case "kyläkauppa":
            //         dialoginPituus = 9;
            //         PääIkkuna.dialogiaJäljellä = dialoginPituus;
            //         dialogiKuvat = new Icon[dialoginPituus];
            //         dialogiTekstit = new String[dialoginPituus];
            //         dialogiPuhujat = new String[dialoginPituus];

            //         dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
            //         dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
            //         dialogiKuvat[2] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
            //         dialogiKuvat[3] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
            //         dialogiKuvat[4] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
            //         dialogiKuvat[5] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
            //         dialogiKuvat[6] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
            //         dialogiKuvat[7] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
            //         dialogiKuvat[8] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");

            //         dialogiTekstit[0] = "Vihko auki!";
            //         dialogiTekstit[1] = "8 hiivaa...";
            //         dialogiTekstit[2] = "...4 kiloa taloussokeria...";
            //         dialogiTekstit[3] = "...ja Mennen.";
            //         dialogiTekstit[4] = "...";
            //         dialogiTekstit[5] = "Meinasit ruveta ponua keittelemään?";
            //         dialogiTekstit[6] = "Hiljaa saatana";
            //         dialogiTekstit[7] = "Ei noilla vielä kuuhun mennä.";
            //         dialogiTekstit[8] = "Haista sinä mursu paska!";

            //         dialogiPuhujat[0] = "Keimo";
            //         dialogiPuhujat[1] = "ASS-Market kassa";
            //         dialogiPuhujat[2] = "ASS-Market kassa";
            //         dialogiPuhujat[3] = "ASS-Market kassa";
            //         dialogiPuhujat[4] = "ASS-Market kassa";
            //         dialogiPuhujat[5] = "ASS-Market kassa";
            //         dialogiPuhujat[6] = "Keimo";
            //         dialogiPuhujat[7] = "ASS-Market kassa";
            //         dialogiPuhujat[8] = "Keimo";
            //     break;
                    case "kauppa_normaali":
                        dialoginPituus = 4;
                        PääIkkuna.dialogiaJäljellä = dialoginPituus;
                        dialogiKuvat = new Icon[dialoginPituus];
                        dialogiTekstit = new String[dialoginPituus];
                        dialogiPuhujat = new String[dialoginPituus];

                        dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
                        dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
                        dialogiKuvat[2] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
                        dialogiKuvat[3] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");

                        dialogiTekstit[0] = "Hyvää päivää!";
                        dialogiTekstit[1] = "Se tekisi " + df.format(Pelaaja.ostostenHintaYhteensä) + " euroa.";
                        dialogiTekstit[2] = "...";
                        dialogiTekstit[3] = "Kiitos. Näkemiin.";

                        dialogiPuhujat[0] = "ASS-Market kassa";
                        dialogiPuhujat[1] = "ASS-Market kassa";
                        dialogiPuhujat[2] = "Keimo";
                        dialogiPuhujat[3] = "ASS-Market kassa";
                    return true;
                    case "kauppa_eivaraa":
                        dialoginPituus = 4;
                        PääIkkuna.dialogiaJäljellä = dialoginPituus;
                        dialogiKuvat = new Icon[dialoginPituus];
                        dialogiTekstit = new String[dialoginPituus];
                        dialogiPuhujat = new String[dialoginPituus];

                        dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
                        dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
                        dialogiKuvat[2] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
                        dialogiKuvat[3] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");

                        dialogiTekstit[0] = "Hyvää päivää!";
                        dialogiTekstit[1] = "Se tekisi " + df.format(Pelaaja.ostostenHintaYhteensä) + " euroa.";
                        dialogiTekstit[2] = "No perhana eihän mulla oo varaa näihin.";
                        dialogiTekstit[3] = "Tervetuloa takaisin kun on varaa.";

                        dialogiPuhujat[0] = "ASS-Market kassa";
                        dialogiPuhujat[1] = "ASS-Market kassa";
                        dialogiPuhujat[2] = "Keimo";
                        dialogiPuhujat[3] = "ASS-Market kassa";
                    return true;
            //     case "goblin_alku":
            //         dialoginPituus = 2;
            //         PääIkkuna.dialogiaJäljellä = dialoginPituus;
            //         dialogiKuvat = new Icon[dialoginPituus];
            //         dialogiTekstit = new String[dialoginPituus];
            //         dialogiPuhujat = new String[dialoginPituus];

            //         dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");
            //         dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");

            //         dialogiTekstit[0] = "Hrmm...";
            //         dialogiTekstit[1] = "Polku pimeälle puolelle, onko?";

            //         dialogiPuhujat[0] = "Jumal Yoda";
            //         dialogiPuhujat[1] = "Jumal Yoda";
            //     break;
            //     case "goblin_kyllä":
            //         dialoginPituus = 2;
            //         PääIkkuna.dialogiaJäljellä = dialoginPituus;
            //         dialogiKuvat = new Icon[dialoginPituus];
            //         dialogiTekstit = new String[dialoginPituus];
            //         dialogiPuhujat = new String[dialoginPituus];

            //         dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
            //         dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");

            //         dialogiTekstit[0] = "Kiinnostunut.";
            //         dialogiTekstit[1] = "Siinä tapauksessa reitin pimeälle puolelle sinulle avaan.";

            //         dialogiPuhujat[0] = "Keimo";
            //         dialogiPuhujat[1] = "Jumal Yoda";
            //     break;
            //     case "goblin_ei":
            //         dialoginPituus = 2;
            //         PääIkkuna.dialogiaJäljellä = dialoginPituus;
            //         dialogiKuvat = new Icon[dialoginPituus];
            //         dialogiTekstit = new String[dialoginPituus];
            //         dialogiPuhujat = new String[dialoginPituus];

            //         dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
            //         dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");

            //         dialogiTekstit[0] = "Ei kiinosta  virhe";
            //         dialogiTekstit[1] = "...";

            //         dialogiPuhujat[0] = "Keimo";
            //         dialogiPuhujat[1] = "Jumal Yoda";
            //     break;
                    case null, default:
                        PääIkkuna.näytäVirheIlmoitusDialogi("Dialogia ei löytynyt. Objekti on määritetty avaamaan dialogi " + "\"" + vuoropuheRuudunTunniste + "\"" + ", jota ei löytynyt dialogikartasta eikä vakiodialogivalikoimasta. Onkohan kst-tiedostoa menty käpelöimään muuten kuin pelinsisäisellä editorilla? :(", "Dialogia ei löytynyt");
                    return false;
                }
            }
        //}
    }

    // public static void luoVakioVuoropuheDialogit() {

    //     vuoropuheTunniste = "kyläkauppa";
    //     dialoginPituus = 9;
    //     dialogiKuvat = new Icon[dialoginPituus];
    //     dialogiTekstit = new String[dialoginPituus];
    //     dialogiPuhujat = new String[dialoginPituus];

    //     dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
    //     dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
    //     dialogiKuvat[2] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
    //     dialogiKuvat[3] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
    //     dialogiKuvat[4] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
    //     dialogiKuvat[5] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
    //     dialogiKuvat[6] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
    //     dialogiKuvat[7] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
    //     dialogiKuvat[8] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");

    //     dialogiTekstit[0] = "Vihko auki!";
    //     dialogiTekstit[1] = "8 hiivaa...";
    //     dialogiTekstit[2] = "...4 kiloa taloussokeria...";
    //     dialogiTekstit[3] = "...ja Mennen.";
    //     dialogiTekstit[4] = "...";
    //     dialogiTekstit[5] = "Meinasit ruveta ponua keittelemään?";
    //     dialogiTekstit[6] = "Hiljaa saatana";
    //     dialogiTekstit[7] = "Ei noilla vielä kuuhun mennä.";
    //     dialogiTekstit[8] = "Haista sinä mursu paska!";

    //     dialogiPuhujat[0] = "Keimo";
    //     dialogiPuhujat[1] = "ASS-Market kassa";
    //     dialogiPuhujat[2] = "ASS-Market kassa";
    //     dialogiPuhujat[3] = "ASS-Market kassa";
    //     dialogiPuhujat[4] = "ASS-Market kassa";
    //     dialogiPuhujat[5] = "ASS-Market kassa";
    //     dialogiPuhujat[6] = "Keimo";
    //     dialogiPuhujat[7] = "ASS-Market kassa";
    //     dialogiPuhujat[8] = "Keimo";
    //     vuoropuheDialogiKartta.put(vuoropuheTunniste, new VuoropuheDialogiPätkä(vuoropuheTunniste, dialoginPituus, dialogiKuvat, dialogiTekstit, dialogiPuhujat));


    //     // vuoropuheTunniste = "kauppa_normaali";        
    //     // dialoginPituus = 4;
    //     // dialogiKuvat = new Icon[dialoginPituus];
    //     // dialogiTekstit = new String[dialoginPituus];
    //     // dialogiPuhujat = new String[dialoginPituus];

    //     // dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
    //     // dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
    //     // dialogiKuvat[2] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
    //     // dialogiKuvat[3] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");

    //     // dialogiTekstit[0] = "Hyvää päivää!";
    //     // dialogiTekstit[1] = "Se tekisi " + df.format(Pelaaja.ostostenHintaYhteensä) + " euroa.";
    //     // dialogiTekstit[2] = "...";
    //     // dialogiTekstit[3] = "Kiitos. Näkemiin.";

    //     // dialogiPuhujat[0] = "ASS-Market kassa";
    //     // dialogiPuhujat[1] = "ASS-Market kassa";
    //     // dialogiPuhujat[2] = "Keimo";
    //     // dialogiPuhujat[3] = "ASS-Market kassa";
    //     // vuoropuheDialogiKartta.put(vuoropuheTunniste, new VuoropuheDialogiPätkä(vuoropuheTunniste, dialoginPituus, dialogiKuvat, dialogiTekstit, dialogiPuhujat));


    //     // vuoropuheTunniste = "kauppa_eivaraa";
    //     // dialoginPituus = 4;
    //     // dialogiKuvat = new Icon[dialoginPituus];
    //     // dialogiTekstit = new String[dialoginPituus];
    //     // dialogiPuhujat = new String[dialoginPituus];

    //     // dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
    //     // dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
    //     // dialogiKuvat[2] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
    //     // dialogiKuvat[3] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");

    //     // dialogiTekstit[0] = "Hyvää päivää!";
    //     // dialogiTekstit[1] = "Se tekisi " + df.format(Pelaaja.ostostenHintaYhteensä) + " euroa.";
    //     // dialogiTekstit[2] = "No perhana eihän mulla oo varaa näihin.";
    //     // dialogiTekstit[3] = "Tervetuloa takaisin kun on varaa.";

    //     // dialogiPuhujat[0] = "ASS-Market kassa";
    //     // dialogiPuhujat[1] = "ASS-Market kassa";
    //     // dialogiPuhujat[2] = "Keimo";
    //     // dialogiPuhujat[3] = "ASS-Market kassa";
    //     // vuoropuheDialogiKartta.put(vuoropuheTunniste, new VuoropuheDialogiPätkä(vuoropuheTunniste, dialoginPituus, dialogiKuvat, dialogiTekstit, dialogiPuhujat));

        
    //     vuoropuheTunniste = "goblin_alku";
    //     dialoginPituus = 2;
    //     dialogiKuvat = new Icon[dialoginPituus];
    //     dialogiTekstit = new String[dialoginPituus];
    //     dialogiPuhujat = new String[dialoginPituus];
    //     valinta = true;
    //     valinnanNimi = "goblin_valinta";
    //     valinnanOtsikko = "Polku pimeälle puolelle?";
    //     vaihtoehtojenMäärä = 2;
    //     valinnanVaihtoehdot = new String[vaihtoehtojenMäärä];
    //     valinnanVaihtoehtojenKohdeDialogit = new String[vaihtoehtojenMäärä];
    //     vaihtoehtojenTriggerit = new String[vaihtoehtojenMäärä];

    //     dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");
    //     dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");

    //     dialogiTekstit[0] = "Hrmm...";
    //     dialogiTekstit[1] = "Polku pimeälle puolelle, onko?";

    //     dialogiPuhujat[0] = "Jumal Yoda";
    //     dialogiPuhujat[1] = "Jumal Yoda";

    //     valinnanVaihtoehdot[0] = "Hmm... no kyllä on!";
    //     valinnanVaihtoehdot[1] = "Ei";
    //     valinnanVaihtoehtojenKohdeDialogit[0] = "goblin_kyllä";
    //     valinnanVaihtoehtojenKohdeDialogit[1] = "goblin_ei";
    //     vaihtoehtojenTriggerit[0] = "Avaa takahuone";

    //     vuoropuheDialogiKartta.put(vuoropuheTunniste, new VuoropuheDialogiPätkä(vuoropuheTunniste, dialoginPituus, dialogiKuvat, dialogiTekstit, dialogiPuhujat, valinta, valinnanNimi, valinnanOtsikko, valinnanVaihtoehdot, valinnanVaihtoehtojenKohdeDialogit, vaihtoehtojenTriggerit));
        

    //     vuoropuheTunniste = "goblin_kyllä";
    //     dialoginPituus = 2;
    //     dialogiKuvat = new Icon[dialoginPituus];
    //     dialogiTekstit = new String[dialoginPituus];
    //     dialogiPuhujat = new String[dialoginPituus];

    //     dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
    //     dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");

    //     dialogiTekstit[0] = "Kiinnostunut.";
    //     dialogiTekstit[1] = "Siinä tapauksessa reitin pimeälle puolelle sinulle avaan.";

    //     dialogiPuhujat[0] = "Keimo";
    //     dialogiPuhujat[1] = "Jumal Yoda";
    //     vuoropuheDialogiKartta.put(vuoropuheTunniste, new VuoropuheDialogiPätkä(vuoropuheTunniste, dialoginPituus, dialogiKuvat, dialogiTekstit, dialogiPuhujat));


    //     vuoropuheTunniste = "goblin_ei";
    //     dialoginPituus = 2;
    //     dialogiKuvat = new Icon[dialoginPituus];
    //     dialogiTekstit = new String[dialoginPituus];
    //     dialogiPuhujat = new String[dialoginPituus];

    //     dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
    //     dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");

    //     dialogiTekstit[0] = "Ei kiinosta  virhe";
    //     dialogiTekstit[1] = "...";

    //     dialogiPuhujat[0] = "Keimo";
    //     dialogiPuhujat[1] = "Jumal Yoda";
    //     vuoropuheDialogiKartta.put(vuoropuheTunniste, new VuoropuheDialogiPätkä(vuoropuheTunniste, dialoginPituus, dialogiKuvat, dialogiTekstit, dialogiPuhujat));
    // }
}
