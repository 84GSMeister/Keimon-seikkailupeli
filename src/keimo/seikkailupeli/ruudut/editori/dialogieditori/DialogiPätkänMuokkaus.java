package keimo.seikkailupeli.ruudut.editori.dialogieditori;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.ObjektiListaNappi;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.DialogiIkkunat;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.dialogi.VuoropuheDialogiPätkä;
import keimo.seikkailupeli.ruudut.editori.dialogieditori.DialogiEditoriRuutu.Tilat;

import java.util.HashMap;

public class DialogiPätkänMuokkaus {
    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("editori_lisäikkuna_pohja");
    private static LabelKomponentti pohja = new LabelKomponentti(0.9f, 0.75f, 0, -0.15f, pohjaTekstuuri);
    private static Teksti otsikkoTeksti;
    private static LabelKomponentti otsikko = new LabelKomponentti(0.45f, 0.1f, 0, 0.4f, otsikkoTeksti);

    private static Renderöitävä okTekstuuri = Assets.annaTekstuuri("editori_nappi_ok");
    private static Renderöitävä nuoliTekstuuri = Assets.annaTekstuuri("editori_nappi_nuoli");
    private static Renderöitävä poistaDialogiTekstuuri = Assets.annaTekstuuri("editori_dialogi_poista");
    private static Nappi okNappi = new Nappi(0.15f, 0.05f, 0, -0.8f, okTekstuuri);
    private static Nappi nuoliYlösNappi = new Nappi(0.08f, 0.1f, 0.825f, 0.5f, nuoliTekstuuri);
    private static Nappi nuoliAlasNappi = new Nappi(0.08f, -0.1f, 0.825f, -0.8f, nuoliTekstuuri);
    private static Nappi poistaDialogiNappi = new Nappi(0.08f, 0.1f, 0.625f, 0.5f, poistaDialogiTekstuuri, new TooltipTeksti("Poista dialogipätkä"));

    private static Teksti dialogiOtsikkoTeksti;
    private static Teksti dialogiEnsimmäinenRiviTeksti;
    private static int scroll = 0;
    private static int maxScroll = 0;
    private static int rivejä = 14;
    protected static boolean päivitäDialogiTekstit = true;

    protected static VuoropuheDialogiPätkä muokattavaVdp;
    protected static HashMap<Integer, ObjektiListaNappi> dialogiTekstiValikko = new HashMap<>();

    protected static void avaaDialogiPätkänMuokkaus(String dialogiPätkänNimi) {
        DialogiEditoriRuutu.dialogiEditorinTila = Tilat.PÄTKÄN_VALINTA;
        muokattavaVdp = DialogiEditoriRuutu.editorinDialogiKartta.get(dialogiPätkänNimi);
        päivitäDialogiTekstit = true;
    }

    protected static void suljeDialogiPätkänMuokkaus() {
        DialogiEditoriRuutu.dialogiEditorinTila = Tilat.VAKIO;
        muokattavaVdp = null;
    }

    protected static void tarkistaHover(int hiiriX, int hiiriY) {
        nuoliYlösNappi.hiiriSisällä(hiiriX, hiiriY);
        nuoliAlasNappi.hiiriSisällä(hiiriX, hiiriY);
        poistaDialogiNappi.hiiriSisällä(hiiriX, hiiriY);
        okNappi.hiiriSisällä(hiiriX, hiiriY);
        for (Nappi nappi : dialogiTekstiValikko.values()) {
            nappi.hiiriSisällä(hiiriX, hiiriY);
        }
    }

    protected static void tarkistaNapit(int hiiriX, int hiiriY) {
        if (okNappi.hiiriSisällä(hiiriX, hiiriY)) {
            suljeDialogiPätkänMuokkaus();
        }
        else if (nuoliYlösNappi.hiiriSisällä(hiiriX, hiiriY)) {
            if (scroll > 0) scroll--;
        }
        else if (nuoliAlasNappi.hiiriSisällä(hiiriX, hiiriY)) {
            if (scroll < maxScroll) scroll++;
        }
        else if (poistaDialogiNappi.hiiriSisällä(hiiriX, hiiriY)) {
            String poistettava = muokattavaVdp.annaTunniste();
            boolean poista = DialogiIkkunat.viestiIkkuna("Poista dialogipätkä", "Haluatko poistaa dialogipätkän " + poistettava + "?", "yesno", "question", false);
            if (poista) {
                DialogiEditoriRuutu.editorinDialogiKartta.remove(poistettava);
                suljeDialogiPätkänMuokkaus();
                DialogiEditoriRuutu.päivitäValikko = true;
            }
        }
        else {
            try {
                for (int i = 0; i < dialogiTekstiValikko.size(); i++) {
                    Nappi n = dialogiTekstiValikko.get(i);
                    if (n.hiiriSisällä(hiiriX, hiiriY)) {
                        //if (((Teksti)n.annaSisältö()).annaTeksti().startsWith("V:") && i == dialogiTekstiValikko.size()-1) {
                        if (i == dialogiTekstiValikko.size()-1) {
                            DialogiIkkunat.viestiIkkuna("Ominaisuus tulossa", "Valintojen muokkaus tulossa myöhemmin. Jos haluat tehdä valintaikkunoita, syötä ne .kst-tiedostoon suoraan.", "ok", "info", päivitäDialogiTekstit);
                            break;
                        }
                        else {
                            DialogiTekstinMuokkaus.avaaDialogiTekstinMuokkaus(i);
                            break;
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected static void scroll(int scrollY) {
        if (scrollY > 0) {
            if (scroll > 0) scroll--;
        }
        else if (scrollY < 0) {
            if (scroll < maxScroll) scroll++;
        }
    }

    private static void luoDialogiTekstiValikko() {
        dialogiTekstiValikko.clear();
        int dialogiIndeksi = 0;
        for (int i = 0; i < muokattavaVdp.annaPituus(); i++) {
            String s = muokattavaVdp.annaTekstit()[i];
            Teksti teksti = new Teksti(dialogiIndeksi + ". " + s, Väri.white, 1000, 48);
            dialogiTekstiValikko.put(dialogiIndeksi, new ObjektiListaNappi(dialogiIndeksi, s, 0.8f, 0.05f, 0, 0.1f*dialogiIndeksi, teksti));
            dialogiIndeksi++;
        }
        if (muokattavaVdp.onkoValinta()) {
            Teksti teksti = new Teksti("V: " + muokattavaVdp.annaValinnanNimi(), Väri.white, 1000, 48);
            dialogiTekstiValikko.put(dialogiIndeksi, new ObjektiListaNappi(dialogiIndeksi, muokattavaVdp.annaValinnanNimi(), 0.8f, 0.05f, 0, 0.1f*(dialogiIndeksi+1), teksti));
            dialogiIndeksi++;
        }
    }

    private static void alustaGrafiikat() {
        if (dialogiOtsikkoTeksti == null) {
            dialogiOtsikkoTeksti = new Teksti("otsikko", Väri.white, 1000, 48);
            dialogiEnsimmäinenRiviTeksti = new Teksti("dialogi 1", Väri.white, 1000, 48);
            otsikkoTeksti = new Teksti("dialogin nimi", Väri.white, 800, 48);
            otsikko.päivitäSisältö(otsikkoTeksti);
        }
    }

    protected static void renderöi(Shader shader, Ikkuna window) {
        try {
            alustaGrafiikat();
            pohja.renderöi(shader, window);
            if (muokattavaVdp != null) {
                Teksti t = (Teksti)otsikko.annaSisältö();
                t.päivitäTeksti(muokattavaVdp.annaTunniste());
                otsikko.päivitäSisältö(t);
            }
            otsikko.renderöi(shader, window);
            if (päivitäDialogiTekstit) {
                luoDialogiTekstiValikko();
                päivitäDialogiTekstit = false;
            }

            int dialogiIndeksi = 0;
            for (int i = 0; i < dialogiTekstiValikko.size(); i++) {
                if (i < muokattavaVdp.annaTekstit().length) {
                    Nappi nappi = dialogiTekstiValikko.get(i);
                    dialogiEnsimmäinenRiviTeksti.päivitäTeksti(i + ". " + muokattavaVdp.annaTekstit()[i]);
                    nappi.päivitäSisältö(dialogiEnsimmäinenRiviTeksti);
                    nappi.muutaOffsetY(0.275f -0.075f*(i-scroll));
                    nappi.renderöiTekstuuriKääntö(shader, window, 0, false, false);
                    dialogiIndeksi++;
                }
            }
            if (muokattavaVdp.onkoValinta()) {
                Nappi nappi = dialogiTekstiValikko.get(dialogiIndeksi);
                dialogiEnsimmäinenRiviTeksti.päivitäTeksti("V: " + muokattavaVdp.annaValinnanNimi());
                nappi.päivitäSisältö(dialogiEnsimmäinenRiviTeksti);
                nappi.muutaOffsetY(0.275f -0.075f*(dialogiIndeksi-scroll +1));
                nappi.renderöiTekstuuriKääntö(shader, window, 0, false, false);
            }
            maxScroll = dialogiIndeksi - rivejä;

            nuoliYlösNappi.renderöi(shader, window);
            nuoliAlasNappi.renderöi(shader, window);
            poistaDialogiNappi.renderöi(shader, window);

            okNappi.renderöi(shader, window);

            poistaDialogiNappi.renderöiTooltip(shader, window);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
