package keimo.seikkailupeli.toiminnot;

import keimo.TarkistettavatArvot;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.*;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.assets.TavoiteLista;
import keimo.seikkailupeli.assets.dialogi.VuoropuheDialogiPätkä;
import keimo.seikkailupeli.gui.toimintoIkkunat.DialogiValintaIkkuna;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Juomalasi;
import keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC.Juhani;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Dialogit {

    public static Renderöitävä dialogiKuvake;
    public static Teksti dialogiTeksti;
    public static Teksti dialogiNimi;
    public static Tekstuuri vakiokuva;
    

    public static void luoTekstuurit() {
        dialogiKuvake = new Tekstuuri("tiedostot/kuvat/tyhjä.png");
        dialogiTeksti = new Teksti("teksti", Color.black, 1500, 224, KeimoFontit.fontti_keimo_36, false);
        dialogiNimi = new Teksti("nimi", 1500, 48);
        vakiokuva = new Tekstuuri("tiedostot/kuvat/pelaaja_og.png");
    }

    /**
     * Avaa ruudun alareunassa näkyvä dialogilaatikko. Kaikki pelin varsinainen tekstisisältö näytetään tässä.
     * @param tekstuuri Puhuvan NPC:n / objektin kuva
     * @param teksti dialogiteksti
     * @param nimi puhujan nimi / otsikko
     */
    public static void avaaDialogi(Renderöitävä tekstuuri, String teksti, String nimi) {
        Peli.syötteenTila = SyötteenTila.DIALOGI;
        Peli.dialoginAvausViive = 5;
        dialogiTekstiString = teksti;
        tekstiäJäljellä = teksti.length();
        dialogiKuvake = tekstuuri;
        dialogiNimiString = nimi;
    }

    /**
     * Avaa ruudun alareunassa näkyvä dialogilaatikko. Kaikki pelin varsinainen tekstisisältö näytetään tässä.
     * Jos tekstuuria ei ole saatavilla, käyetään vakiona pelaaja_og-kuvaketta.
     * @param tekstuuri Puhuvan NPC:n / objektin kuva
     * @param teksti dialogiteksti
     * @param nimi puhujan nimi / otsikko
     */
    public static void avaaDialogi(String tekstuurinPolku, String teksti, String nimi) {
        if (tekstuurinPolku == null || tekstuurinPolku == "") {
            avaaDialogi(vakiokuva, teksti, nimi);
        }
        else {
            Tekstuuri tekstuuri = new Tekstuuri(tekstuurinPolku);
            avaaDialogi(tekstuuri, teksti, nimi);
        }
    }

    public static int tekstiäJäljellä;
    public static boolean tekstiAuki = false;
    private static String dialogiNimiString = "";
    private static String dialogiTekstiString = "";
    static String kelattuTeksti = "";
    public static int dialogiaJäljellä = 0;
    public static boolean useitaRuutuja = false;

    public static void avaaPitkäDialogiRuutu(String vuoropuheRuudunTunniste) {
        if (PitkätDialogit.luoYksityiskohtainenVuoropuheRuutu(vuoropuheRuudunTunniste)) {
            avaaDialogi(PitkätDialogit.dialogiKuvakkeet.get(PitkätDialogit.vuoropuheTunniste + "_0"), PitkätDialogit.dialogiTekstit[0], PitkätDialogit.dialogiPuhujat[0]);
        }
    }

    public static boolean äläSuljeNuolilla = false;
    public static String valintaTulossa = null;

    /**
     * Kelatessa pelaaja voi aina ensimmäisellä painalluksella scrollata dialogin loppuun, jos scrollaus on kesken.
     * Jos scrollaus on valmis, painalluksella siirrytään seuraavaan ruutuun / suljetaan dialogi.
     */

    public static void kelaaDialogi() {
        if (tekstiäJäljellä <= 1) {
            edistäDialogia();
            Äänet.toistaSFX("Valinta");
        }
        else {
            tekstiäJäljellä = 1;
        }
    }

    /**
     * Tällä ohjataan dialogiruudun etenemistä.
     * Jos dialogiruutuja on useita, siirry seuraavaan ruutuun.
     * Jos dialogi on määritetty siirtymään valintaikkunaan ja viimeinen ruutu on auki, avaa valintaikkuna.
     * Jos dialogia on vain 1 ruutu tai viimeinen ruutu on auki eikä valintaa ole asetettu, sulje dialogi.
     */

    private static void edistäDialogia() {
        if (dialogiaJäljellä > 1) {
            PitkätDialogit.siirrySeuraavaanDialogiRuutuun(PitkätDialogit.dialoginPituus - dialogiaJäljellä + 1);
        }
        else if (valintaTulossa != null) {
            VuoropuheDialogiPätkä vdp = PitkätDialogit.vuoropuheDialogiKartta.get(valintaTulossa);
            if (vdp != null) {
                if (vdp.onkoValinta()) {
                    DialogiValintaIkkuna.avaaToimintoIkkuna(valintaTulossa);
                    valintaTulossa = null;
                }
                else {
                    suljeDialogi();
                }
            }
            else {
                suljeDialogi();
            }
        }
        else {
            suljeDialogi();
        }
    }

    private static void suljeDialogi() {
        Peli.pauseDialogi = false;
        Peli.syötteenTila = SyötteenTila.PELI;
        tekstiAuki = false;
        useitaRuutuja = false;
        Peli.dialoginAvausViive = 5;
        dialogiNimiString = "";
        dialogiTekstiString = "";
        tulostettavaTeksti = "";
    }

    /**
     * Lisää dialogilaatikkoon tulostettavaan tekstipätkään 1 merkki kerrallaan.
     */

    static String tulostettavaTeksti;
    public static void scrollaaDialogiTeksti() {
        if (Peli.syötteenTila == SyötteenTila.DIALOGI) {
            if (tekstiäJäljellä > 0) {
                tulostettavaTeksti = dialogiTekstiString.substring(0, dialogiTekstiString.length()-tekstiäJäljellä +1);
                tekstiäJäljellä--;
                Äänet.toistaSFX(haeDialogiÄäni(dialogiNimiString));
            }
        }
    }

    public static void renderöiDialogiTeksti() {
        dialogiNimi.päivitäTeksti(dialogiNimiString);
        dialogiTeksti.päivitäTeksti(tulostettavaTeksti, 2);
    }

    /**
     * Tulostetaan dialogilaatikkoon hieman kuvaavampi selitys sille, että jotain tavoitetta ei ole suoritettu.
     * @param tavoite puuttuvan tavoitteen nimi
     */

    public static void haeTavoiteVinkkiTeksti(String tavoite) {
        String tavoiteVinkki = "";
        switch (tavoite) {
            case "Sytytä nuotio" -> {
                switch (TavoiteLista.nykyinenTavoite) {
                    case "Löydä takaisin kotiin" -> {
                        tavoiteVinkki = "Koti ei ole tässä suunnassa.";
                    }
                    default -> {
                        tavoiteVinkki = "Nuotiopaikka ei ole tässä suunnassa.";
                    }
                }
            }
            case "Hae Pasi nuotiolle" -> {
                tavoiteVinkki = "Pitäisiköhän käväistä Pasin luona? (Pasi asuu Yo-kylässä.)";
            }
            case "Etsi Keimo-baari" -> {
                tavoiteVinkki = "Pitäisiköhän kuitenkin suunnata Keimo-baaria kohti ensin?";
            }
            case "Etsi Pasi" -> {
                tavoiteVinkki = "Pitäisiköhän etsiä Pasi ensin? Se on kuitenkin jossain päin Keimo-baaria.";
            }
            case "Avaa takahuone" -> {
                tavoiteVinkki = "Tästä ei pääse. Kuulemma joku metsän asukas osaisi ehkä neuvoa reitin metsän siimekseen...";
            }
            case "Etsi pesäpallomaila" -> {
                tavoiteVinkki = "Ota varmuuden vuoksi ase mukaan";
            }
            case "Löydä salahuone" -> {
                tavoiteVinkki = "Tänne ei vielä pääse. Ehkä jokin aktiviteetti pitää suorittaa ensin?";
            }
            default -> {
                tavoiteVinkki = "Huoneeseen warppaaminen vaatii tavoitteen " + tavoite;
            }
        }
        Dialogit.avaaDialogi("", tavoiteVinkki, "Huone lukittu");
    }

    private static String haeDialogiÄäni(String puhuja) {
        switch (puhuja) {
            case "Keimo": return "dialogi1";
            case "Pasi": return "dialogi_pasi";
            case "Juhani": return "dialogi_juhani";
            case "Jumal Velho", "Velho": return "dialogi_velho";
            case "Jumal Yoda", "Yoda", "Goblin", "Goblini": return "dialogi_yoda";
            case "Kauppias", "ASS-Market kassa", "Keimo-baarin tarjoilija": return "dialogi_kauppias";
            case "Kuuhahmo1", "Kuuhahmo2", "Kuuhahmo3", "Kuu-baarin tarjoilija": return "dialogi2";
            case null, default: return "dialogi3";
        }
    }

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
        public static HashMap<String, Renderöitävä> dialogiKuvakkeet = new HashMap<>();

        public static void lataaDialogiKuvakkeet() {
            for (VuoropuheDialogiPätkä vdp : vuoropuheDialogiKartta.values()) {
                for (int i = 0; i < vdp.annaPituus(); i++) {
                    dialogiKuvakkeet.put(vdp.annaTunniste() + "_" + i, new Tekstuuri(vdp.annaKuvienTiedostoNimet()[i]));
                }
            }
        }

        public static void siirrySeuraavaanDialogiRuutuun(int ruudunNro) {
            if (ruudunNro >= 0) {
                if (dialogiKuvienTiedostoNimet.length > ruudunNro && dialogiTekstit.length > ruudunNro && dialogiPuhujat.length > ruudunNro) {
                    Dialogit.avaaDialogi(dialogiKuvakkeet.get(vuoropuheTunniste + "_" + ruudunNro), dialogiTekstit[ruudunNro], dialogiPuhujat[ruudunNro]);
                    dialogiaJäljellä--;
                }
            }
        }

        public static boolean luoYksityiskohtainenVuoropuheRuutu(String vuoropuheRuudunTunniste) {
            if (vuoropuheRuudunTunniste != null) {
                useitaRuutuja = true;
                vuoropuheTunniste = vuoropuheRuudunTunniste;
                // Hae dialogia dialogikartasta (kst-tiedostossa)
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
                else {
                    // Hardkoodatut dialogit. Nääkin olis tavoitteena saada kst-tiedostoon.
                    switch (vuoropuheRuudunTunniste) {
                        case "kauppa_normaali":
                            dialoginPituus = 4;
                            dialogiaJäljellä = dialoginPituus;
                            dialogiKuvienTiedostoNimet = new String[dialoginPituus];
                            dialogiTekstit = new String[dialoginPituus];
                            dialogiPuhujat = new String[dialoginPituus];

                            dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                            dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                            dialogiKuvienTiedostoNimet[2] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                            dialogiKuvienTiedostoNimet[3] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";

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
                            dialogiaJäljellä = dialoginPituus;
                            dialogiKuvienTiedostoNimet = new String[dialoginPituus];
                            dialogiTekstit = new String[dialoginPituus];
                            dialogiPuhujat = new String[dialoginPituus];

                            dialogiKuvienTiedostoNimet[0] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                            dialogiKuvienTiedostoNimet[1] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";
                            dialogiKuvienTiedostoNimet[2] = "tiedostot/kuvat/vuoropuhe/keimo_lähikuva.png";
                            dialogiKuvienTiedostoNimet[3] = "tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png";

                            dialogiTekstit[0] = "Hyvää päivää!";
                            dialogiTekstit[1] = "Se tekisi " + df.format(Pelaaja.ostostenHintaYhteensä) + " euroa.";
                            dialogiTekstit[2] = "No perhana eihän mulla oo varaa näihin.";
                            dialogiTekstit[3] = "Tervetuloa takaisin kun on varaa.";

                            dialogiPuhujat[0] = "ASS-Market kassa";
                            dialogiPuhujat[1] = "ASS-Market kassa";
                            dialogiPuhujat[2] = "Keimo";
                            dialogiPuhujat[3] = "ASS-Market kassa";
                        return true;
                        case null, default:
                            Dialogit.avaaDialogi("", "Dialogia ei löytynyt. Objekti on määritetty avaamaan dialogi " + "\"" + vuoropuheRuudunTunniste + "\"" + ", jota ei löytynyt dialogikartasta eikä vakiodialogivalikoimasta. Onkohan kst-tiedostoa menty käpelöimään muuten kuin pelinsisäisellä editorilla? :(", "Dialogia ei löytynyt");
                        return false;
                    }
                }
            }
            else return false;
        }
    }

    public class DialogiTriggerit {

        /**
         * Määritetään, mitä tapahtuu dialogin mukana määritetystä triggeristä. Näitä käytetään valintaikkunoiden yhteydessä.
         * Triggerit löytyy kst-tiedostosta dialogin valinta-kohdasta
         * @param triggeri triggerin nimi
         */
        public static void suoritaDialogiTriggeri(String triggeri) {
            switch (triggeri) {
                case "Avaa takahuone" -> {
                    TavoiteLista.suoritaTavoite("Avaa takahuone");
                }
                case "silta_hyppää" -> {
                    TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.KUOLEMA_SILLALTA_ALAS;
                    Pelaaja.hp = 0;
                }
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
                case "juhani_huumeostettu" -> {
                    Juhani.annaHuume();
                }
            }
        }
    }
}
