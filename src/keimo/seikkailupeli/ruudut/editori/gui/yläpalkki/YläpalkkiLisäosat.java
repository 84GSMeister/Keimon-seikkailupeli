package keimo.seikkailupeli.ruudut.editori.gui.yläpalkki;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.ruudut.editori.dialogieditori.DialogiEditoriRuutu;
import keimo.seikkailupeli.ruudut.editori.tarinaeditori.TarinaEditoriIkkuna;

public class YläpalkkiLisäosat {

    private static Teksti otsikkoTeksti;
    private static LabelKomponentti otsikko = new LabelKomponentti(0.5f, 0.05f, 0.25f, 0.95f, otsikkoTeksti);
    private static Nappi dialogiEditoriNappi = new Nappi(0.05f, 0.05f, -0.3f, 0.8f, Assets.annaTekstuuri("editori_lisäosat_dialogieditori"), new TooltipTeksti("Dialogieditori", 640, 48));
    private static Nappi tarinaEditoriNappi = new Nappi(0.05f, 0.05f, -0.2f, 0.8f, Assets.annaTekstuuri("editori_lisäosat_tarinaeditori"), new TooltipTeksti("Tarinaeditori", 640, 48));
    private static Nappi tavoiteEditoriNappi = new Nappi(0.05f, 0.05f, -0.1f, 0.8f, Assets.annaTekstuuri("editori_lisäosat_tavoite-editori"), new TooltipTeksti("Tulossa myöhemmin!", 640, 48));
    
    protected static void alustaGrafiikat() {
        otsikkoTeksti = new Teksti("Lisäosat", Väri.white, 400, 48);
        otsikko.päivitäSisältö(otsikkoTeksti);
    }

    public static void tarkistaHover(int hiiriX, int hiiriY) {
        dialogiEditoriNappi.hiiriSisällä(hiiriX, hiiriY);
        tarinaEditoriNappi.hiiriSisällä(hiiriX, hiiriY);
        tavoiteEditoriNappi.hiiriSisällä(hiiriX, hiiriY);
    }

    public static void tarkistaPainetutNapit(int hiiriX, int hiiriY) {
        if (dialogiEditoriNappi.hiiriSisällä(hiiriX, hiiriY)) {
            DialogiEditoriRuutu.avaaDialogiEditori();
        }
        else if (tarinaEditoriNappi.hiiriSisällä(hiiriX, hiiriY)) {
            TarinaEditoriIkkuna.avaaTarinaEditori();
        }
    }

    public static void renderöi(Shader shader, Ikkuna ikkuna) {
        renderöiTyökaluValikko(shader, ikkuna);
        renderöiTooltipTekstit(shader, ikkuna);
    }

    private static void renderöiTyökaluValikko(Shader shader, Ikkuna ikkuna) {
        otsikko.renderöi(shader, ikkuna);
        dialogiEditoriNappi.renderöi(shader, ikkuna);
        tarinaEditoriNappi.renderöi(shader, ikkuna);
        tavoiteEditoriNappi.renderöi(shader, ikkuna);
    }

    private static void renderöiTooltipTekstit(Shader shader, Ikkuna ikkuna) {
        dialogiEditoriNappi.renderöiTooltip(shader, ikkuna);
        tarinaEditoriNappi.renderöiTooltip(shader, ikkuna);
        tavoiteEditoriNappi.renderöiTooltip(shader, ikkuna);
    }
    
}
