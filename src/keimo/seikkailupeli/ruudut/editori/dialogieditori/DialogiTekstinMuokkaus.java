package keimo.seikkailupeli.ruudut.editori.dialogieditori;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.DialogiIkkunat;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.ruudut.editori.dialogieditori.DialogiEditoriRuutu.Tilat;

public class DialogiTekstinMuokkaus {
    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("editori_lisäikkuna_pohja");
    private static LabelKomponentti pohja = new LabelKomponentti(0.9f, 0.75f, 0, -0.15f, pohjaTekstuuri);
    private static Teksti puhujaTeksti;
    private static Nappi puhujaLabel = new Nappi(0.65f, 0.05f, 0.15f, 0.4f, puhujaTeksti, new TooltipTeksti("Muokkaa nimeä"));
    private static Teksti puheTeksti;
    private static Nappi puhetekstiLabel = new Nappi(0.65f, 0.30f, 0.15f, -0.05f, puheTeksti, new TooltipTeksti("Muokkaa tekstiä"));
    private static Renderöitävä puhujanKuvaTekstuuri;
    private static Nappi puhujanKuvaLabel = new Nappi(0.15f, 0.2f, -0.65f, 0.25f, puhujanKuvaTekstuuri, new TooltipTeksti("Valitse kuva"));

    private static Renderöitävä okTekstuuri = Assets.annaTekstuuri("editori_nappi_ok");
    private static Renderöitävä nuoliTekstuuri = Assets.annaTekstuuri("editori_nappi_nuoli");
    private static Renderöitävä uusiSivuTekstuuri = Assets.annaTekstuuri("editori_dialogi_uusi");
    private static Renderöitävä poistaSivuTekstuuri = Assets.annaTekstuuri("editori_dialogi_poista");
    private static Nappi okNappi = new Nappi(0.15f, 0.05f, 0, -0.8f, okTekstuuri);
    private static Nappi nuoliVasemmalleNappi = new Nappi(0.08f, 0.1f, -0.725f, -0.5f, nuoliTekstuuri, new TooltipTeksti("Edellinen sivu"));
    private static Nappi nuoliOikealleNappi = new Nappi(0.08f, 0.1f, 0.725f, -0.5f, nuoliTekstuuri, new TooltipTeksti("Seuraava sivu"));
    private static Nappi uusiNappi = new Nappi(0.08f, 0.1f, -0.225f, -0.5f, uusiSivuTekstuuri, new TooltipTeksti("Lisää sivu"));
    private static Nappi poistaNappi = new Nappi(0.08f, 0.1f, 0.225f, -0.5f, poistaSivuTekstuuri, new TooltipTeksti("Poista sivu"));

    protected static int muokattavaIndeksi = 0;

    protected static void avaaDialogiTekstinMuokkaus(int valittuIndeksi) {
        DialogiEditoriRuutu.dialogiEditorinTila = Tilat.PÄTKÄN_MUOKKAUS;
        muokattavaIndeksi = valittuIndeksi;
    }

    protected static void suljeDialogiTekstinMuokkaus() {
        DialogiEditoriRuutu.dialogiEditorinTila = Tilat.PÄTKÄN_VALINTA;
        DialogiPätkänMuokkaus.päivitäDialogiTekstit = true;
    }

    protected static void tarkistaHover(int hiiriX, int hiiriY) {
        okNappi.hiiriSisällä(hiiriX, hiiriY);
        nuoliVasemmalleNappi.hiiriSisällä(hiiriX, hiiriY);
        nuoliOikealleNappi.hiiriSisällä(hiiriX, hiiriY);
        uusiNappi.hiiriSisällä(hiiriX, hiiriY);
        poistaNappi.hiiriSisällä(hiiriX, hiiriY);
        puhujaLabel.hiiriSisällä(hiiriX, hiiriY);
        puhetekstiLabel.hiiriSisällä(hiiriX, hiiriY);
        puhujanKuvaLabel.hiiriSisällä(hiiriX, hiiriY);
    }

    protected static void tarkistaNapit(int hiiriX, int hiiriY) {
        if (okNappi.hiiriSisällä(hiiriX, hiiriY)) {
            suljeDialogiTekstinMuokkaus();
        }
        else if (nuoliVasemmalleNappi.hiiriSisällä(hiiriX, hiiriY)) {
            if (muokattavaIndeksi > 0) {
                muokattavaIndeksi--;
            }
        }
        else if (nuoliOikealleNappi.hiiriSisällä(hiiriX, hiiriY)) {
            if (muokattavaIndeksi < DialogiPätkänMuokkaus.muokattavaVdp.annaPituus()-1) {
                muokattavaIndeksi++;
            }
        }
        else if (uusiNappi.hiiriSisällä(hiiriX, hiiriY)) {
            DialogiPätkänMuokkaus.muokattavaVdp.lisääSivu();
            muokattavaIndeksi = DialogiPätkänMuokkaus.muokattavaVdp.annaPituus()-1;
        }
        else if (poistaNappi.hiiriSisällä(hiiriX, hiiriY)) {
            if (DialogiIkkunat.viestiIkkuna("Poista sivu", "Haluatko poistaa sivun " + muokattavaIndeksi + "?", "yesno", "question", false)) {
                if (DialogiPätkänMuokkaus.muokattavaVdp.annaPituus() > 1) {
                    DialogiPätkänMuokkaus.muokattavaVdp.poistaSivu(muokattavaIndeksi);
                    if (muokattavaIndeksi > 0) muokattavaIndeksi--;
                }
                else {
                    DialogiIkkunat.viestiIkkuna("Poista sivu", "Dialogissa on oltava vähintään 1 sivu.", "ok", "error", false);
                }
            }
        }
        else if (puhetekstiLabel.hiiriSisällä(hiiriX, hiiriY)) {
            TekstinMuokkausIkkuna.avaaTekstinMuokkausIkkuna(puheTeksti.annaTeksti(), false);
        }
        else if (puhujaLabel.hiiriSisällä(hiiriX, hiiriY)) {
            TekstinMuokkausIkkuna.avaaTekstinMuokkausIkkuna(puhujaTeksti.annaTeksti(), true);
        }
        else if (puhujanKuvaLabel.hiiriSisällä(hiiriX, hiiriY)) {
            KuvanValintaIkkuna.avaaKuvanValintaIkkuna();
        }
    }

    private static void alustaGrafiikat() {
        if (puhujaTeksti == null) {
            puhujaTeksti = new Teksti("puhuja", Väri.white, 1500, 48);
            puhujaLabel.päivitäSisältö(puhujaTeksti);
            puheTeksti = new Teksti("teksti", Väri.white, 1500, 224);
            puhetekstiLabel.päivitäSisältö(puheTeksti);
        }
    }

    protected static void renderöi(Shader shader, Ikkuna window) {
        try {
            alustaGrafiikat();
            pohja.renderöi(shader, window);
            if (DialogiPätkänMuokkaus.muokattavaVdp != null) {
                if (DialogiPätkänMuokkaus.muokattavaVdp.annaPuhujat().length >= muokattavaIndeksi) {
                    puhujaTeksti.päivitäTeksti(DialogiPätkänMuokkaus.muokattavaVdp.annaPuhujat()[muokattavaIndeksi]);
                    puhujaLabel.päivitäSisältö(puhujaTeksti);
                }
                if (DialogiPätkänMuokkaus.muokattavaVdp.annaTekstit().length >= muokattavaIndeksi) {
                    puheTeksti.päivitäTeksti(DialogiPätkänMuokkaus.muokattavaVdp.annaTekstit()[muokattavaIndeksi], 2);
                    puhetekstiLabel.päivitäSisältö(puheTeksti);
                }
                if (DialogiPätkänMuokkaus.muokattavaVdp.annaKuvienTiedostoNimet().length >= muokattavaIndeksi) {
                    puhujanKuvaLabel.päivitäSisältö(Assets.annaDialogiTekstuuri(DialogiPätkänMuokkaus.muokattavaVdp.annaKuvienTiedostoNimet()[muokattavaIndeksi]));
                }
            }
            puhujaLabel.renderöi(shader, window);
            puhetekstiLabel.renderöi(shader, window);
            puhujanKuvaLabel.renderöi(shader, window);

            if (muokattavaIndeksi > 0) nuoliVasemmalleNappi.renderöiTekstuuriKääntö(shader, window, 270, false, false);
            if (muokattavaIndeksi < DialogiPätkänMuokkaus.muokattavaVdp.annaPituus()-1) {
                nuoliOikealleNappi.renderöiTekstuuriKääntö(shader, window, 90, false, false);
            }
            uusiNappi.renderöi(shader, window);
            poistaNappi.renderöi(shader, window);

            okNappi.renderöi(shader, window);

            puhujanKuvaLabel.renderöiTooltip(shader, window);
            puhetekstiLabel.renderöiTooltip(shader, window);
            puhujaLabel.renderöiTooltip(shader, window);
            nuoliVasemmalleNappi.renderöiTooltip(shader, window);
            nuoliOikealleNappi.renderöiTooltip(shader, window);
            uusiNappi.renderöiTooltip(shader, window);
            poistaNappi.renderöiTooltip(shader, window);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
