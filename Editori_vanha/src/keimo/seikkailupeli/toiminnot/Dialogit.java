package keimo.seikkailupeli.toiminnot;

import keimo.editori.DialogiEditori.VuoropuheDialogiPätkä;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Juomalasi;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Dialogit {

    // public static Renderöitävä dialogiKuvake = new Tekstuuri("tiedostot/kuvat/tyhjä.png");
    // public static Teksti dialogiTeksti = new Teksti("teksti", Color.black, 1500, 240, KeimoFontit.fontti_keimo_36, false);
    // public static Teksti dialogiNimi = new Teksti("nimi", 1500, 48);

    // public static Tekstuuri vakiokuva = new Tekstuuri("tiedostot/kuvat/pelaaja_og.png");

    // public static void avaaDialogi(Renderöitävä tekstuuri, String teksti, String nimi) {
    //     Peli.syötteenTila = SyötteenTila.DIALOGI;
    //     Peli.dialoginAvausViive = 5;
    //     dialogiTekstiString = teksti;
    //     tekstiäJäljellä = teksti.length();
    //     dialogiKuvake = tekstuuri;
    //     dialogiTeksti.päivitäTeksti("");
    //     dialogiNimi.päivitäTeksti(nimi);
    // }

    // public static void avaaDialogi(String tekstuurinPolku, String teksti, String nimi) {
    //     if (tekstuurinPolku == null || tekstuurinPolku == "") {
    //         avaaDialogi(vakiokuva, teksti, nimi);
    //     }
    //     else {
    //         Tekstuuri tekstuuri = new Tekstuuri(tekstuurinPolku);
    //         avaaDialogi(tekstuuri, teksti, nimi);
    //     }
    // }

    public static int tekstiäJäljellä;
    public static boolean tekstiAuki = false;

    public static String dialogiTekstiString = "";
    static String kelattuTeksti = "";

    public static int dialogiaJäljellä = 0;
    public static boolean useitaRuutuja = false;

    

    // public static void avaaPitkäDialogiRuutu(String vuoropuheRuudunTunniste) {
    //     if (PitkätDialogit.luoYksityiskohtainenVuoropuheRuutu(vuoropuheRuudunTunniste)) {
    //         avaaDialogi(new Tekstuuri(PitkätDialogit.dialogiKuvienTiedostoNimet[0]), PitkätDialogit.dialogiTekstit[0], PitkätDialogit.dialogiPuhujat[0]);
    //     }
    // }

    // public static boolean äläSuljeNuolilla = false;
    public static String valintaTulossa = null;

    // public static void kelaaDialogi() {
    //     if (tekstiäJäljellä <= 1) {
    //         edistäDialogia();
    //     }
    //     else {
    //         tekstiäJäljellä = 1;
    //     }
    //     Äänet.toistaSFX("Valinta");
    // }

    // private static void edistäDialogia() {
    //     if (dialogiaJäljellä > 1) {
    //         PitkätDialogit.siirrySeuraavaanDialogiRuutuun(PitkätDialogit.dialoginPituus - dialogiaJäljellä + 1);
    //     }
    //     else if (valintaTulossa != null) {
    //         VuoropuheDialogiPätkä vdp = PitkätDialogit.vuoropuheDialogiKartta.get(valintaTulossa);
    //         if (vdp != null) {
    //             if (vdp.onkoValinta()) {
    //                 DialogiValintaIkkuna.avaaToimintoIkkuna(valintaTulossa);
    //                 valintaTulossa = null;
    //             }
    //             else {
    //                 suljeDialogi();
    //             }
    //         }
    //         // else if (valintaTulossa != null && valintaTulossa != "") {
    //         //     DialogiValintaIkkuna.avaaToimintoIkkuna(valintaTulossa);
    //         // }
    //         else {
    //             suljeDialogi();
    //         }
    //     }
    //     else {
    //         suljeDialogi();
    //     }
    // }

    // private static void suljeDialogi() {
    //     Peli.pauseDialogi = false;
    //     Peli.syötteenTila = SyötteenTila.PELI;
    //     tekstiAuki = false;
    //     useitaRuutuja = false;
    // }

    // public static void scrollaaDialogiTeksti() {
    //     if (Peli.syötteenTila == SyötteenTila.DIALOGI) {
    //         if (tekstiäJäljellä > 0) {
    //             String tulostettavaTeksti = dialogiTekstiString.substring(0, dialogiTekstiString.length()-tekstiäJäljellä +1);
    //             dialogiTeksti.päivitäTeksti(tulostettavaTeksti);
    //             tekstiäJäljellä--;
    //         }
    //     }
    // }

    // public static void haeTavoiteVinkkiTeksti(String tavoite) {
    //     String tavoiteVinkki = "";
    //     switch (tavoite) {
    //         case "Sytytä nuotio" -> {
    //             tavoiteVinkki = "Nuotiopaikka ei ole tässä suunnassa.";
    //         }
    //         case "Etsi Pasi" -> {
    //             tavoiteVinkki = "Pitäisiköhän kuitenkin suunnata Keimo-baaria kohti ensin?";
    //         }
    //         case "Avaa takahuone" -> {
    //             tavoiteVinkki = "Tästä ei pääse. Kuulemma joku metsän asukas osaisi ehkä neuvoa reitin metsän siimekseen...";
    //         }
    //         case "Etsi pesäpallomaila" -> {
    //             tavoiteVinkki = "Ota varmuuden vuoksi ase mukaan";
    //         }
    //         case "Löydä salahuone" -> {
    //             tavoiteVinkki = "Tänne ei vielä pääse.\nEhkä jokin aktiviteetti pitää suorittaa ensin?";
    //         }
    //         default -> {
    //             tavoiteVinkki = "Huoneeseen warppaaminen vaatii tavoitteen " + tavoite;
    //         }
    //     }
    //     Dialogit.avaaDialogi("", tavoiteVinkki, "Huone lukittu");
    // }

    public class PitkätDialogit {

        protected static DecimalFormat df = new DecimalFormat("##.##");
        public static int dialoginPituus = 0;
        public static String[] dialogiKuvienTiedostoNimet;
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

        // public static void siirrySeuraavaanDialogiRuutuun(int ruudunNro) {
        //     if (ruudunNro >= 0) {
        //         if (dialogiKuvienTiedostoNimet.length > ruudunNro && dialogiTekstit.length > ruudunNro && dialogiPuhujat.length > ruudunNro) {
        //             Dialogit.avaaDialogi(new Tekstuuri(dialogiKuvienTiedostoNimet[ruudunNro]), dialogiTekstit[ruudunNro], dialogiPuhujat[ruudunNro]);
        //             dialogiaJäljellä--;
        //         }
        //     }
        // }

        public static boolean luoYksityiskohtainenVuoropuheRuutu(String vuoropuheRuudunTunniste) {
            if (vuoropuheRuudunTunniste != null) {
                useitaRuutuja = true;
                vuoropuheTunniste = vuoropuheRuudunTunniste;
                if (vuoropuheDialogiKartta.containsKey(vuoropuheRuudunTunniste)) {
                    VuoropuheDialogiPätkä dp = vuoropuheDialogiKartta.get(vuoropuheRuudunTunniste);
                    if (dp != null) {
                        dialogiaJäljellä = dp.annaPituus();
                        dialoginPituus = dp.annaPituus();
                        dialogiKuvienTiedostoNimet = new String[dp.annaPituus()];
                        dialogiTekstit = new String[dp.annaPituus()];
                        dialogiPuhujat = new String[dp.annaPituus()];
                        for (int i = 0; i < dp.annaPituus(); i++) {
                            dialogiKuvienTiedostoNimet[i] = dp.annaKuvienTiedostoNimet()[i];
                            dialogiTekstit[i] = dp.annaTekstit()[i];
                            dialogiPuhujat[i] = dp.annaPuhujat()[i];
                        }
                        if (dp.onkoValinta()) {
                            valintaTulossa = dp.annaTunniste();
                        }
                    }
                    return true;
                }
                else return false;
                // else {
                //     // Hardkoodatut dialogit. Nääkin olis tavoitteena saada kst-tiedostoon.
                //     switch (vuoropuheRuudunTunniste) {
                //         case "kauppa_normaali":
                //             dialoginPituus = 4;
                //             dialogiaJäljellä = dialoginPituus;
                //             dialogiKuvienTiedostoNimet = new String[dialoginPituus];
                //             dialogiTekstit = new String[dialoginPituus];
                //             dialogiPuhujat = new String[dialoginPituus];

                //             dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //             dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //             dialogiKuvienTiedostoNimet[2] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                //             dialogiKuvienTiedostoNimet[3] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";

                //             dialogiTekstit[0] = "Hyvää päivää!";
                //             dialogiTekstit[1] = "Se tekisi " + df.format(Pelaaja.ostostenHintaYhteensä) + " euroa.";
                //             dialogiTekstit[2] = "...";
                //             dialogiTekstit[3] = "Kiitos. Näkemiin.";

                //             dialogiPuhujat[0] = "ASS-Market kassa";
                //             dialogiPuhujat[1] = "ASS-Market kassa";
                //             dialogiPuhujat[2] = "Keimo";
                //             dialogiPuhujat[3] = "ASS-Market kassa";
                //         return true;
                //         case "kauppa_eivaraa":
                //             dialoginPituus = 4;
                //             dialogiaJäljellä = dialoginPituus;
                //             dialogiKuvienTiedostoNimet = new String[dialoginPituus];
                //             dialogiTekstit = new String[dialoginPituus];
                //             dialogiPuhujat = new String[dialoginPituus];

                //             dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //             dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //             dialogiKuvienTiedostoNimet[2] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                //             dialogiKuvienTiedostoNimet[3] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";

                //             dialogiTekstit[0] = "Hyvää päivää!";
                //             dialogiTekstit[1] = "Se tekisi " + df.format(Pelaaja.ostostenHintaYhteensä) + " euroa.";
                //             dialogiTekstit[2] = "No perhana eihän mulla oo varaa näihin.";
                //             dialogiTekstit[3] = "Tervetuloa takaisin kun on varaa.";

                //             dialogiPuhujat[0] = "ASS-Market kassa";
                //             dialogiPuhujat[1] = "ASS-Market kassa";
                //             dialogiPuhujat[2] = "Keimo";
                //             dialogiPuhujat[3] = "ASS-Market kassa";
                //         return true;
                //         // case "baari_normaali_vanha":
                //         //     Olutlasi olutlasi = new Olutlasi(0, 0);
                //         //     dialoginPituus = 5;
                //         //     dialogiaJäljellä = dialoginPituus;
                //         //     dialogiKuvienTiedostoNimet = new String[dialoginPituus];
                //         //     dialogiTekstit = new String[dialoginPituus];
                //         //     dialogiPuhujat = new String[dialoginPituus];

                //         //     dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //         //     dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                //         //     dialogiKuvienTiedostoNimet[2] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //         //     dialogiKuvienTiedostoNimet[3] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //         //     dialogiKuvienTiedostoNimet[4] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";

                //         //     dialogiTekstit[0] = "Hyvää iltaa! Mitä saisi olla?";
                //         //     dialogiTekstit[1] = "Iltaa. Yksi hanaolut.";
                //         //     dialogiTekstit[2] = "Se tekisi " + df.format(olutlasi.annaHinta()) + " euroa.";
                //         //     dialogiTekstit[3] = "Tässä, olkaa hyvä.";
                //         //     dialogiTekstit[4] = "Kiitos. ";

                //         //     dialogiPuhujat[0] = "Keimo-baarin tarjoilija";
                //         //     dialogiPuhujat[1] = "Keimo";
                //         //     dialogiPuhujat[2] = "Keimo-baarin tarjoilija";
                //         //     dialogiPuhujat[3] = "Keimo-baarin tarjoilija";
                //         //     dialogiPuhujat[4] = "Keimo";
                //         // return true;
                //         // case "baari_normaali":
                //         //     vuoropuheTunniste = "baari_normaali1";
                //         //     dialoginPituus = 1;
                //         //     dialogiKuvienTiedostoNimet = new String[dialoginPituus];
                //         //     dialogiTekstit = new String[dialoginPituus];
                //         //     dialogiPuhujat = new String[dialoginPituus];
                //         //     valinta = true;
                //         //     valinnanNimi = "baari_valinta";
                //         //     valintaTulossa = "baari_valinta";
                //         //     valinnanOtsikko = "Mitä saisi olla?";
                //         //     vaihtoehtojenMäärä = 3;
                //         //     valinnanVaihtoehdot = new String[vaihtoehtojenMäärä];
                //         //     valinnanVaihtoehtojenKohdeDialogit = new String[vaihtoehtojenMäärä];
                //         //     vaihtoehtojenTriggerit = new String[vaihtoehtojenMäärä];

                //         //     dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";

                //         //     dialogiTekstit[0] = "Iltaa! Mitä saisi olla?";

                //         //     dialogiPuhujat[0] = "Keimo-baarin tarjoilija";

                //         //     valinnanVaihtoehdot[0] = "Hanaolut";
                //         //     valinnanVaihtoehdot[1] = "Hanalonkero";
                //         //     valinnanVaihtoehdot[2] = "Hanasiideri";
                //         //     valinnanVaihtoehtojenKohdeDialogit[0] = "baari_juoma1";
                //         //     valinnanVaihtoehtojenKohdeDialogit[1] = "baari_juoma2";
                //         //     valinnanVaihtoehtojenKohdeDialogit[1] = "baari_juoma3";
                //         //     vaihtoehtojenTriggerit[0] = "baari_juoma1";
                //         //     vaihtoehtojenTriggerit[1] = "baari_juoma2";
                //         //     vaihtoehtojenTriggerit[2] = "baari_juoma3";
                //         // return true;
                //         case "baari_juoma1":
                //             dialoginPituus = 4;
                //             dialogiaJäljellä = dialoginPituus;
                //             dialogiKuvienTiedostoNimet = new String[dialoginPituus];
                //             dialogiTekstit = new String[dialoginPituus];
                //             dialogiPuhujat = new String[dialoginPituus];

                //             dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                //             dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //             dialogiKuvienTiedostoNimet[2] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //             dialogiKuvienTiedostoNimet[3] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";

                //             dialogiTekstit[0] = "Iltaa. Yksi hanaolut.";
                //             dialogiTekstit[1] = "Se tekisi " + "4.95" + " euroa.";
                //             dialogiTekstit[2] = "Tässä, olkaa hyvä.";
                //             dialogiTekstit[3] = "Kiitos. ";

                //             dialogiPuhujat[0] = "Keimo";
                //             dialogiPuhujat[1] = "Keimo-baarin tarjoilija";
                //             dialogiPuhujat[2] = "Keimo-baarin tarjoilija";
                //             dialogiPuhujat[3] = "Keimo";
                //         return true;
                //         case "baari_juoma2":
                //             dialoginPituus = 4;
                //             dialogiaJäljellä = dialoginPituus;
                //             dialogiKuvienTiedostoNimet = new String[dialoginPituus];
                //             dialogiTekstit = new String[dialoginPituus];
                //             dialogiPuhujat = new String[dialoginPituus];

                //             dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                //             dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //             dialogiKuvienTiedostoNimet[2] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //             dialogiKuvienTiedostoNimet[3] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";

                //             dialogiTekstit[0] = "Iltaa. Yksi hanalonkero.";
                //             dialogiTekstit[1] = "Se tekisi " + "6.95" + " euroa.";
                //             dialogiTekstit[2] = "Tässä, olkaa hyvä.";
                //             dialogiTekstit[3] = "Kiitos. ";

                //             dialogiPuhujat[0] = "Keimo";
                //             dialogiPuhujat[1] = "Keimo-baarin tarjoilija";
                //             dialogiPuhujat[2] = "Keimo-baarin tarjoilija";
                //             dialogiPuhujat[3] = "Keimo";
                //         return true;
                //         case "baari_juoma3":
                //             dialoginPituus = 4;
                //             dialogiaJäljellä = dialoginPituus;
                //             dialogiKuvienTiedostoNimet = new String[dialoginPituus];
                //             dialogiTekstit = new String[dialoginPituus];
                //             dialogiPuhujat = new String[dialoginPituus];

                //             dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                //             dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //             dialogiKuvienTiedostoNimet[2] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //             dialogiKuvienTiedostoNimet[3] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";

                //             dialogiTekstit[0] = "Iltaa. Yksi hanasiideri.";
                //             dialogiTekstit[1] = "Se tekisi " + "5.95" + " euroa.";
                //             dialogiTekstit[2] = "Tässä, olkaa hyvä.";
                //             dialogiTekstit[3] = "Kiitos. ";

                //             dialogiPuhujat[0] = "Keimo";
                //             dialogiPuhujat[1] = "Keimo-baarin tarjoilija";
                //             dialogiPuhujat[2] = "Keimo-baarin tarjoilija";
                //             dialogiPuhujat[3] = "Keimo";
                //         return true;
                //         case "baari_eivaraa":
                //             dialoginPituus = 5;
                //             dialogiaJäljellä = dialoginPituus;
                //             dialogiKuvienTiedostoNimet = new String[dialoginPituus];
                //             dialogiTekstit = new String[dialoginPituus];
                //             dialogiPuhujat = new String[dialoginPituus];

                //             dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //             dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                //             dialogiKuvienTiedostoNimet[2] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                //             dialogiKuvienTiedostoNimet[3] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                //             dialogiKuvienTiedostoNimet[4] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";

                //             dialogiTekstit[0] = "Hyvää iltaa! Mitä saisi olla?";
                //             dialogiTekstit[1] = "Iltaa. Yksi hanaolut";
                //             dialogiTekstit[2] = "Se tekisi " + "4.95" + " euroa.";
                //             dialogiTekstit[3] = "No hemmetti, ei oo varaa.";
                //             dialogiTekstit[4] = "Sitten ei tule olutta.";

                //             dialogiPuhujat[0] = "Keimo-baarin tarjoilija";
                //             dialogiPuhujat[1] = "Keimo";
                //             dialogiPuhujat[2] = "Keimo-baarin tarjoilija";
                //             dialogiPuhujat[3] = "Keimo";
                //             dialogiPuhujat[4] = "Keimo-baarin tarjoilija";
                //         return true;
                //         case "kuu_baari":
                //             dialoginPituus = 6;
                //             dialogiaJäljellä = dialoginPituus;
                //             dialogiKuvienTiedostoNimet = new String[dialoginPituus];
                //             dialogiTekstit = new String[dialoginPituus];
                //             dialogiPuhujat = new String[dialoginPituus];

                //             dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/kenttäkohteet/kuuhahmo_2.png";
                //             dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                //             dialogiKuvienTiedostoNimet[2] = "tiedostot/kuvat/kenttäkohteet/kuuhahmo_2.png";
                //             dialogiKuvienTiedostoNimet[3] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                //             dialogiKuvienTiedostoNimet[4] = "tiedostot/kuvat/kenttäkohteet/kuuhahmo_2.png";
                //             dialogiKuvienTiedostoNimet[5] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";

                //             dialogiTekstit[0] = "Hyvää iltaa! Mitä saisi olla?";
                //             dialogiTekstit[1] = "Hmm... Tää on mulle uus paikka. Mitä suosittelet?";
                //             dialogiTekstit[2] = "Kuuolut on klassikko. Talo tarjoaa";
                //             dialogiTekstit[3] = "Oikeasti? No jumalauta, laitetaan sellainen";
                //             dialogiTekstit[4] = "Tässä, olkaa hyvä.";
                //             dialogiTekstit[5] = "Kiitos. ";

                //             dialogiPuhujat[0] = "Kuu-baarin tarjoilija";
                //             dialogiPuhujat[1] = "Keimo";
                //             dialogiPuhujat[2] = "Kuu-baarin tarjoilija";
                //             dialogiPuhujat[3] = "Keimo";
                //             dialogiPuhujat[4] = "Kuu-baarin tarjoilija";
                //             dialogiPuhujat[5] = "Keimo";
                //         return true;
                //         case "kuu_baari_2":
                //             dialoginPituus = 3;
                //             dialogiaJäljellä = dialoginPituus;
                //             dialogiKuvienTiedostoNimet = new String[dialoginPituus];
                //             dialogiTekstit = new String[dialoginPituus];
                //             dialogiPuhujat = new String[dialoginPituus];

                //             dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                //             dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/kenttäkohteet/kuuhahmo_2.png";
                //             dialogiKuvienTiedostoNimet[2] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";

                //             dialogiTekstit[0] = "Iltaa. Yksi kuuolut.";
                //             dialogiTekstit[1] = "Tässä, olkaa hyvä.";
                //             dialogiTekstit[2] = "Kiitos. ";

                //             dialogiPuhujat[0] = "Keimo";
                //             dialogiPuhujat[1] = "Kuu-baarin tarjoilija";
                //             dialogiPuhujat[2] = "Keimo";
                //         return true;
                //         case null, default:
                //             Dialogit.avaaDialogi("", "Dialogia ei löytynyt. Objekti on määritetty avaamaan dialogi " + "\"" + vuoropuheRuudunTunniste + "\"" + ", jota ei löytynyt dialogikartasta eikä vakiodialogivalikoimasta. Onkohan kst-tiedostoa menty käpelöimään muuten kuin pelinsisäisellä editorilla? :(", "Dialogia ei löytynyt");
                //         return false;
                //     }
                // }
            }
            else return false;
        }

        // public static void luoVakioVuoropuheDialogit() {

        //     vuoropuheTunniste = "kyläkauppa";
        //     dialoginPituus = 9;
        //     dialogiKuvienTiedostoNimet = new String[dialoginPituus];
        //     dialogiTekstit = new String[dialoginPituus];
        //     dialogiPuhujat = new String[dialoginPituus];

        //     dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
        //     dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
        //     dialogiKuvienTiedostoNimet[2] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
        //     dialogiKuvienTiedostoNimet[3] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
        //     dialogiKuvienTiedostoNimet[4] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
        //     dialogiKuvienTiedostoNimet[5] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
        //     dialogiKuvienTiedostoNimet[6] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
        //     dialogiKuvienTiedostoNimet[7] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
        //     dialogiKuvienTiedostoNimet[8] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";


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
        //     vuoropuheDialogiKartta.put(vuoropuheTunniste, new VuoropuheDialogiPätkä(vuoropuheTunniste, dialoginPituus, dialogiKuvienTiedostoNimet, dialogiTekstit, dialogiPuhujat, false, null, null, null, null, null));


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
        //     dialogiKuvienTiedostoNimet = new String[dialoginPituus];
        //     dialogiTekstit = new String[dialoginPituus];
        //     dialogiPuhujat = new String[dialoginPituus];
        //     valinta = true;
        //     valinnanNimi = "goblin_valinta";
        //     valinnanOtsikko = "Polku pimeälle puolelle?";
        //     vaihtoehtojenMäärä = 2;
        //     valinnanVaihtoehdot = new String[vaihtoehtojenMäärä];
        //     valinnanVaihtoehtojenKohdeDialogit = new String[vaihtoehtojenMäärä];
        //     vaihtoehtojenTriggerit = new String[vaihtoehtojenMäärä];

        //     dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png";
        //     dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png";

        //     dialogiTekstit[0] = "Hrmm...";
        //     dialogiTekstit[1] = "Polku pimeälle puolelle, onko?";

        //     dialogiPuhujat[0] = "Jumal Yoda";
        //     dialogiPuhujat[1] = "Jumal Yoda";

        //     valinnanVaihtoehdot[0] = "Hmm... no kyllä on!";
        //     valinnanVaihtoehdot[1] = "Ei";
        //     valinnanVaihtoehtojenKohdeDialogit[0] = "goblin_kyllä";
        //     valinnanVaihtoehtojenKohdeDialogit[1] = "goblin_ei";
        //     vaihtoehtojenTriggerit[0] = "Avaa takahuone";

        //     vuoropuheDialogiKartta.put(vuoropuheTunniste, new VuoropuheDialogiPätkä(vuoropuheTunniste, dialoginPituus, dialogiKuvienTiedostoNimet, dialogiTekstit, dialogiPuhujat, true, valinnanNimi, valinnanOtsikko, valinnanVaihtoehdot, valinnanVaihtoehtojenKohdeDialogit, vaihtoehtojenTriggerit));
            

        //     vuoropuheTunniste = "goblin_kyllä";
        //     dialoginPituus = 2;
        //     dialogiKuvienTiedostoNimet = new String[dialoginPituus];
        //     dialogiTekstit = new String[dialoginPituus];
        //     dialogiPuhujat = new String[dialoginPituus];

        //     dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
        //     dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png";

        //     dialogiTekstit[0] = "Kiinnostunut.";
        //     dialogiTekstit[1] = "Siinä tapauksessa reitin pimeälle puolelle sinulle avaan.";

        //     dialogiPuhujat[0] = "Keimo";
        //     dialogiPuhujat[1] = "Jumal Yoda";
        //     vuoropuheDialogiKartta.put(vuoropuheTunniste, new VuoropuheDialogiPätkä(vuoropuheTunniste, dialoginPituus, dialogiKuvienTiedostoNimet, dialogiTekstit, dialogiPuhujat, false, null, null, null, null, null));


        //     vuoropuheTunniste = "goblin_ei";
        //     dialoginPituus = 2;
        //     dialogiKuvienTiedostoNimet = new String[dialoginPituus];
        //     dialogiTekstit = new String[dialoginPituus];
        //     dialogiPuhujat = new String[dialoginPituus];

        //     dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
        //     dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png";

        //     dialogiTekstit[0] = "Ei kiinosta  virhe";
        //     dialogiTekstit[1] = "...";

        //     dialogiPuhujat[0] = "Keimo";
        //     dialogiPuhujat[1] = "Jumal Yoda";
        //     vuoropuheDialogiKartta.put(vuoropuheTunniste, new VuoropuheDialogiPätkä(vuoropuheTunniste, dialoginPituus, dialogiKuvienTiedostoNimet, dialogiTekstit, dialogiPuhujat, false, null, null, null, null, null));
        // }
    }

    public class DialogiTriggerit {
        public static void suoritaDialogiTriggeri(String triggeri) {
            switch (triggeri) {
                case "baari_juoma1" -> {
                    ArrayList<String> ominaisuusLista = new ArrayList<>();
                    ominaisuusLista.add("juoma=OLUT");
                    Juomalasi juomalasi = new Juomalasi(0, 0, ominaisuusLista);
                    if (!Pelaaja.loputonRaha) Pelaaja.raha -= juomalasi.annaHinta();
                    Pelaaja.annaEsine(juomalasi);
                }
                case "baari_juoma2" -> {
                    ArrayList<String> ominaisuusLista = new ArrayList<>();
                    ominaisuusLista.add("juoma=LONKERO");
                    Juomalasi juomalasi = new Juomalasi(0, 0, ominaisuusLista);
                    if (!Pelaaja.loputonRaha) Pelaaja.raha -= juomalasi.annaHinta();
                    Pelaaja.annaEsine(juomalasi);
                }
                case "baari_juoma3" -> {
                    ArrayList<String> ominaisuusLista = new ArrayList<>();
                    ominaisuusLista.add("juoma=SIIDERI");
                    Juomalasi juomalasi = new Juomalasi(0, 0, ominaisuusLista);
                    if (!Pelaaja.loputonRaha) Pelaaja.raha -= juomalasi.annaHinta();
                    Pelaaja.annaEsine(juomalasi);
                }
            }
        }
    }
}
