package keimo.Ikkunat;

import keimo.Pelaaja;
import keimo.PääIkkuna;

import java.text.DecimalFormat;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class VuoropuheDialogit {

    protected static DecimalFormat df = new DecimalFormat("##.##");
    public static int dialoginPituus = 0;
    public static Icon[] dialogiKuvat;
    public static String[] dialogiTekstit;
    public static String[] dialogiPuhujat;
    public static String vuoropuheTunniste;

    public static void siirrySeuraavaanDialogiRuutuun(int ruudunNro) {
        PääIkkuna.avaaDialogi(dialogiKuvat[ruudunNro], dialogiTekstit[ruudunNro], dialogiPuhujat[ruudunNro]);
        System.out.println("" + dialogiKuvat[ruudunNro] + " " + dialogiTekstit[ruudunNro] + " " + dialogiPuhujat[ruudunNro]);
        PääIkkuna.dialogiaJäljellä--;
    }

    public static void luoYksityiskohtainenVuoropuheRuutu(String vuoropuheRuudunTunniste) {
        PääIkkuna.useitaRuutuja = true;
        vuoropuheTunniste = vuoropuheRuudunTunniste;
        switch (vuoropuheRuudunTunniste) {
            case "kyläkauppa":
                dialoginPituus = 9;
                PääIkkuna.dialogiaJäljellä = dialoginPituus;
                dialogiKuvat = new Icon[dialoginPituus];
                dialogiTekstit = new String[dialoginPituus];
                dialogiPuhujat = new String[dialoginPituus];

                dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
                dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
                dialogiKuvat[2] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
                dialogiKuvat[3] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
                dialogiKuvat[4] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
                dialogiKuvat[5] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
                dialogiKuvat[6] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
                dialogiKuvat[7] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
                dialogiKuvat[8] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");

                dialogiTekstit[0] = "Vihko auki!";
                dialogiTekstit[1] = "8 hiivaa...";
                dialogiTekstit[2] = "...4 kiloa taloussokeria...";
                dialogiTekstit[3] = "...ja Mennen.";
                dialogiTekstit[4] = "...";
                dialogiTekstit[5] = "Meinasit ruveta ponua keittelemään?";
                dialogiTekstit[6] = "Hiljaa saatana";
                dialogiTekstit[7] = "Ei noilla vielä kuuhun mennä.";
                dialogiTekstit[8] = "Haista sinä mursu paska!";

                dialogiPuhujat[0] = "Keimo";
                dialogiPuhujat[1] = "ASS-Market kassa";
                dialogiPuhujat[2] = "ASS-Market kassa";
                dialogiPuhujat[3] = "ASS-Market kassa";
                dialogiPuhujat[4] = "ASS-Market kassa";
                dialogiPuhujat[5] = "ASS-Market kassa";
                dialogiPuhujat[6] = "Keimo";
                dialogiPuhujat[7] = "ASS-Market kassa";
                dialogiPuhujat[8] = "Keimo";
            break;
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
            break;
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
            break;
            case "goblin_alku":
                dialoginPituus = 2;
                PääIkkuna.dialogiaJäljellä = dialoginPituus;
                dialogiKuvat = new Icon[dialoginPituus];
                dialogiTekstit = new String[dialoginPituus];
                dialogiPuhujat = new String[dialoginPituus];

                dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");
                dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");

                dialogiTekstit[0] = "Hrmm...";
                dialogiTekstit[1] = "Polku pimeälle puolelle, onko?";

                dialogiPuhujat[0] = "Jumal Yoda";
                dialogiPuhujat[1] = "Jumal Yoda";
            break;
            case "goblin_kyllä":
                dialoginPituus = 2;
                PääIkkuna.dialogiaJäljellä = dialoginPituus;
                dialogiKuvat = new Icon[dialoginPituus];
                dialogiTekstit = new String[dialoginPituus];
                dialogiPuhujat = new String[dialoginPituus];

                dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
                dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");

                dialogiTekstit[0] = "Kiinnostunut.";
                dialogiTekstit[1] = "Siinä tapauksessa reitin pimeälle puolelle sinulle avaan.";

                dialogiPuhujat[0] = "Keimo";
                dialogiPuhujat[1] = "Jumal Yoda";
            break;
            case "goblin_ei":
                dialoginPituus = 2;
                PääIkkuna.dialogiaJäljellä = dialoginPituus;
                dialogiKuvat = new Icon[dialoginPituus];
                dialogiTekstit = new String[dialoginPituus];
                dialogiPuhujat = new String[dialoginPituus];

                dialogiKuvat[0] = new ImageIcon("tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png");
                dialogiKuvat[1] = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");

                dialogiTekstit[0] = "Ei kiinnosta  virhe";
                dialogiTekstit[1] = "...";

                dialogiPuhujat[0] = "Keimo";
                dialogiPuhujat[1] = "Jumal Yoda";
            break;
        }
    }
}
