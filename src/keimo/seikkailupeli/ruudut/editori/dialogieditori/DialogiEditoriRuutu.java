package keimo.seikkailupeli.ruudut.editori.dialogieditori;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.ObjektiListaNappi;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.Liukusäädin;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.DialogiIkkunat;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.dialogi.VuoropuheDialogiPätkä;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu;

import java.util.HashMap;

public class DialogiEditoriRuutu {
    
    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("editori_lisäikkuna_pohja");
    private static LabelKomponentti pohja = new LabelKomponentti(0.9f, 0.75f, 0, -0.15f, pohjaTekstuuri);
    private static Teksti otsikkoTeksti;
    private static LabelKomponentti otsikko = new LabelKomponentti(0.45f, 0.1f, 0, 0.4f, otsikkoTeksti);

    private static Renderöitävä okTekstuuri = Assets.annaTekstuuri("editori_nappi_ok");
    private static Renderöitävä peruutaTekstuuri = Assets.annaTekstuuri("editori_nappi_peruuta");
    private static Renderöitävä nuoliTekstuuri = Assets.annaTekstuuri("editori_nappi_nuoli");
    private static Renderöitävä uusiDialogiTekstuuri = Assets.annaTekstuuri("editori_dialogi_uusi");
    private static Nappi okNappi = new Nappi(0.15f, 0.05f, -0.2f, -0.8f, okTekstuuri);
    private static Nappi peruutaNappi = new Nappi(0.15f, 0.05f, 0.2f, -0.8f, peruutaTekstuuri);
    private static Nappi nuoliYlösNappi = new Nappi(0.08f, 0.1f, 0.825f, 0.5f, nuoliTekstuuri);
    private static Nappi nuoliAlasNappi = new Nappi(0.08f, -0.1f, 0.825f, -0.8f, nuoliTekstuuri);
    private static Liukusäädin scrollSäädin = new Liukusäädin(0.08f, 0.5f, 0.825f, -0.165f, true);
    private static Nappi uusiDialogiNappi = new Nappi(0.08f, 0.1f, 0.625f, -0.8f, uusiDialogiTekstuuri, new TooltipTeksti("Lisää uusi"));

    public static HashMap<String, VuoropuheDialogiPätkä> editorinDialogiKartta = new HashMap<>();
    private static Teksti dialogiOtsikkoTeksti;
    private static int scroll = 0;
    private static int maxScroll = 0;
    private static int rivejä = 14;
    protected static boolean päivitäValikko = true;
    protected static HashMap<Integer, ObjektiListaNappi> dialogiValikko = new HashMap<>();

    public enum Tilat {
        VAKIO,
        PÄTKÄN_VALINTA,
        PÄTKÄN_MUOKKAUS,
        TEKSTIN_MUOKKAUS,
        KUVAN_VALINTA;
    }
    public static Tilat dialogiEditorinTila = Tilat.VAKIO;

    public static void avaaDialogiEditori() {
        editorinDialogiKartta = Peli.peliTiedosto.annaDialogiKartta();
        EditoriRuutu.dialogiEditoriAuki = true;
    }

    public static void suljeDialogiEditori() {
        EditoriRuutu.dialogiEditoriAuki = false;
        EditoriRuutu.estäVahinkoPainallukset = true;
    }

    public static void kopioiDialogikarttaEditoriin() {
        editorinDialogiKartta.clear();
        for (VuoropuheDialogiPätkä vdp : Peli.peliTiedosto.annaDialogiKartta().values()) {
            String uusiNimi = vdp.annaTunniste();
            String[] uusiTekstiLista = vdp.annaTekstit();
            String[] uusiPuhujaLista = vdp.annaPuhujat();
            String[] uusiKuvaLista = vdp.annaKuvienTiedostoNimet();
            int uusiId = vdp.annaId();
            int uusiPituus = vdp.annaPituus();
            boolean uusiValinta = vdp.onkoValinta();
            String uusiValinnanNimi = vdp.annaValinnanNimi();
            String uusiValinnanOtsikko = vdp.annaValinnanOtsikko();
            String[] uusiValinnanVaihtoehtoLista = vdp.annaValinnanVaihtoehdot();
            String[] uusiValinnanKohdeLista = vdp.annaValinnanVaihtoehtojenKohdeDialogit();
            String[] uusiValinnanTriggeriLista = vdp.annaTriggerit();
            VuoropuheDialogiPätkä uusiDialogipätkä = new VuoropuheDialogiPätkä(uusiId, uusiNimi, uusiPituus, uusiKuvaLista, uusiTekstiLista, uusiPuhujaLista, uusiValinta, uusiValinnanNimi, uusiValinnanOtsikko, uusiValinnanVaihtoehtoLista, uusiValinnanKohdeLista, uusiValinnanTriggeriLista);
            editorinDialogiKartta.put(uusiNimi, uusiDialogipätkä);
        }
    }

    public static void painaEsc() {
        switch (dialogiEditorinTila) {
            case VAKIO -> {
                suljeDialogiEditori();
            }
            case PÄTKÄN_VALINTA -> {
                DialogiPätkänMuokkaus.suljeDialogiPätkänMuokkaus();
            }
            case PÄTKÄN_MUOKKAUS -> {
                DialogiTekstinMuokkaus.suljeDialogiTekstinMuokkaus();
            }
            case TEKSTIN_MUOKKAUS -> {
                TekstinMuokkausIkkuna.suljeTekstinMuokkausIkkuna();
            }
            case KUVAN_VALINTA -> {
                KuvanValintaIkkuna.suljeKuvanValintaIkkuna();
            }
        }
    }

    public static void lisääMerkki(String näppäin) {
        switch (dialogiEditorinTila) {
            case TEKSTIN_MUOKKAUS -> {
                if (näppäin.length() > 0) {
                    TekstinMuokkausIkkuna.lisääMerkki(näppäin);
                }
            }
            case null, default -> {}
        }
    }

    public static void poistaMerkki() {
        switch (dialogiEditorinTila) {
            case TEKSTIN_MUOKKAUS -> {
                TekstinMuokkausIkkuna.poistaMerkki();
            }
            case null, default -> {}
        }
    }

    public static void tarkistaHover(int hiiriX, int hiiriY) {
        try {
            switch (dialogiEditorinTila) {
                case VAKIO -> {
                    nuoliYlösNappi.hiiriSisällä(hiiriX, hiiriY);
                    nuoliAlasNappi.hiiriSisällä(hiiriX, hiiriY);
                    scrollSäädin.hiiriSisällä(hiiriX, hiiriY);
                    uusiDialogiNappi.hiiriSisällä(hiiriX, hiiriY);
                    okNappi.hiiriSisällä(hiiriX, hiiriY);
                    peruutaNappi.hiiriSisällä(hiiriX, hiiriY);
                    for (Nappi nappi : dialogiValikko.values()) {
                        nappi.hiiriSisällä(hiiriX, hiiriY);
                    }
                }
                case PÄTKÄN_VALINTA -> {
                    DialogiPätkänMuokkaus.tarkistaHover(hiiriX, hiiriY);
                }
                case PÄTKÄN_MUOKKAUS -> {
                    DialogiTekstinMuokkaus.tarkistaHover(hiiriX, hiiriY);
                }
                case TEKSTIN_MUOKKAUS -> {
                    TekstinMuokkausIkkuna.tarkistaHover(hiiriX, hiiriY);
                }
                case KUVAN_VALINTA -> {
                    KuvanValintaIkkuna.tarkistaHover(hiiriX, hiiriY);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tarkistaNapit(int hiiriX, int hiiriY) {
        switch (dialogiEditorinTila) {
            case VAKIO -> {
                if (okNappi.hiiriSisällä(hiiriX, hiiriY)) {
                    suljeDialogiEditori();
                }
                else if (peruutaNappi.hiiriSisällä(hiiriX, hiiriY)) {
                    suljeDialogiEditori();
                }
                else if (nuoliYlösNappi.hiiriSisällä(hiiriX, hiiriY)) {
                    if (scroll > 0) scroll--;
                    scrollSäädin.päivitäArvo((int)(scroll*(float)((scrollSäädin.annaMax()-scrollSäädin.annaMin())/maxScroll)));
                }
                else if (nuoliAlasNappi.hiiriSisällä(hiiriX, hiiriY)) {
                    if (scroll < maxScroll) scroll++;
                    scrollSäädin.päivitäArvo((int)(scroll*(float)((scrollSäädin.annaMax()-scrollSäädin.annaMin())/maxScroll)));
                }
                else if (uusiDialogiNappi.hiiriSisällä(hiiriX, hiiriY)) {
                    String nimi = "dialogi " + editorinDialogiKartta.size();
                    nimi = DialogiIkkunat.syöteIkkuna("Uusi dialogipätkä", "Anna nimi uudelle dialogipätkälle.", nimi);
                    if (nimi != null && nimi != "") {
                        VuoropuheDialogiPätkä vdp = new VuoropuheDialogiPätkä(
                                                                        editorinDialogiKartta.size(),
                                                                        nimi,
                                                                        1,
                                                                        new String[1],
                                                                        new String[]{"teksti 0"},
                                                                        new String[]{"puhuja 0"},
                                                                        false,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        null);
                        editorinDialogiKartta.put(nimi, vdp);
                        päivitäValikko = true;
                    }
                }
                else {
                    for (ObjektiListaNappi n : dialogiValikko.values()) {
                        if (n.hiiriSisällä(hiiriX, hiiriY)) {
                            String dialoginNimi = n.annaNimi();
                            DialogiPätkänMuokkaus.avaaDialogiPätkänMuokkaus(dialoginNimi);
                            break;
                        }
                    }
                }
            }
            case PÄTKÄN_VALINTA -> {
                DialogiPätkänMuokkaus.tarkistaNapit(hiiriX, hiiriY);
            }
            case PÄTKÄN_MUOKKAUS -> {
                DialogiTekstinMuokkaus.tarkistaNapit(hiiriX, hiiriY);
            }
            case TEKSTIN_MUOKKAUS -> {
                TekstinMuokkausIkkuna.tarkistaNapit(hiiriX, hiiriY);
            }
            case KUVAN_VALINTA -> {
                KuvanValintaIkkuna.tarkistaNapit(hiiriX, hiiriY);
            }
        }
    }

    public static void tarkistaHiirenRaahaus(int hiiriX, int hiiriY) {
        if (scrollSäädin.hiiriSisällä(hiiriX, hiiriY)) {
            scrollSäädin.liikutaSäädintä(hiiriX, hiiriY);
            float säätökerroin = (float)(maxScroll+1)/(float)(scrollSäädin.annaMax()-(float)scrollSäädin.annaMin());
            int scrollArvo = scrollSäädin.annaArvo();
            scroll = (int)(scrollArvo*säätökerroin);
        }
    }

    public static void scroll(int scrollY) {
        switch (dialogiEditorinTila) {
            case VAKIO -> {
                if (scrollY > 0) {
                    if (scroll > 0) scroll--;
                }
                else if (scrollY < 0) {
                    if (scroll < maxScroll) scroll++;
                }
                scrollSäädin.päivitäArvo((int)(scroll*(float)((scrollSäädin.annaMax()-scrollSäädin.annaMin())/maxScroll)));
            }
            case PÄTKÄN_VALINTA -> {

            }
            case KUVAN_VALINTA -> {
                KuvanValintaIkkuna.scroll(scrollY);
            }
            case null, default -> {}
        }
    }

    private static void luoDialogiValikko() {
        int dialogiIndeksi = 0;
        dialogiValikko.clear();
        for (VuoropuheDialogiPätkä vdp : editorinDialogiKartta.values()) {
            Teksti teksti = new Teksti(dialogiIndeksi + ". " + vdp.annaTunniste(), Väri.white, 1000, 48);
            dialogiValikko.put(dialogiIndeksi, new ObjektiListaNappi(dialogiIndeksi, vdp.annaTunniste(), 0.75f, 0.05f, -0.1f, 0.1f*dialogiIndeksi, teksti));
            dialogiIndeksi++;
        }
        päivitäValikko = false;
    }

    private static void alustaGrafiikat() {
        if (dialogiOtsikkoTeksti == null) {
            dialogiOtsikkoTeksti = new Teksti("otsikko", Väri.white, 1000, 48);
            otsikkoTeksti = new Teksti("Dialogit", Väri.white, 800, 48);
            otsikko.päivitäSisältö(otsikkoTeksti);
        }
        if (dialogiValikko.size() == 0 || päivitäValikko) {
            luoDialogiValikko();
        }
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        switch (dialogiEditorinTila) {
            case VAKIO -> {
                alustaGrafiikat();
                pohja.renderöi(shader, window);
                otsikko.renderöi(shader, window);

                int dialogiIndeksi = 0;
                for (Nappi nappi : dialogiValikko.values()) {
                    if (dialogiIndeksi - scroll < rivejä && dialogiIndeksi >= scroll) {
                        nappi.muutaOffsetY(0.275f -0.075f*(dialogiIndeksi-scroll));
                        nappi.renderöi(shader, window);
                    }
                    dialogiIndeksi++;
                }
                maxScroll = dialogiIndeksi - rivejä;

                nuoliYlösNappi.renderöi(shader, window);
                nuoliAlasNappi.renderöi(shader, window);
                scrollSäädin.renderöi(shader, window);
                uusiDialogiNappi.renderöi(shader, window);

                okNappi.renderöi(shader, window);
                peruutaNappi.renderöi(shader, window);

                uusiDialogiNappi.renderöiTooltip(shader, window);
            }
            case PÄTKÄN_VALINTA -> {
                DialogiPätkänMuokkaus.renderöi(shader, window);
            }
            case PÄTKÄN_MUOKKAUS -> {
                DialogiTekstinMuokkaus.renderöi(shader, window);
            }
            case TEKSTIN_MUOKKAUS -> {
                DialogiTekstinMuokkaus.renderöi(shader, window);
                TekstinMuokkausIkkuna.renderöi(shader, window);
            }
            case KUVAN_VALINTA -> {
                DialogiTekstinMuokkaus.renderöi(shader, window);
                KuvanValintaIkkuna.renderöi(shader, window);
            }
        }
    }
}