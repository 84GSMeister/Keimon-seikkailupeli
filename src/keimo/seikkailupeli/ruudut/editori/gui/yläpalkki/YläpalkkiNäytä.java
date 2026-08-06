package keimo.seikkailupeli.ruudut.editori.gui.yläpalkki;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.guikomponentit.ValintaLaatikko;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu;

public class YläpalkkiNäytä {

    private static Teksti otsikkoTeksti;
    private static LabelKomponentti otsikko = new LabelKomponentti(0.5f, 0.05f, 0.25f, 0.95f, otsikkoTeksti);

    private static LabelKomponentti näytäMaastoLabel = new LabelKomponentti(0.025f, 0.03f, -0.25f, 0.86f, Assets.annaTekstuuri("editori_näytä_maasto"));
    private static ValintaLaatikko näytäMaastoNappi = new ValintaLaatikko(true, 0.025f, 0.03f, -0.2f, 0.86f, new TooltipTeksti("Näytä maasto", 500, 48));
    private static LabelKomponentti näytäObjektitLabel = new LabelKomponentti(0.025f, 0.03f, -0.25f, 0.8f, Assets.annaTekstuuri("editori_näytä_objektit"));
    private static ValintaLaatikko näytäObjektitNappi = new ValintaLaatikko(true, 0.025f, 0.03f, -0.2f, 0.8f, new TooltipTeksti("Näytä objektit", 500, 48));
    private static LabelKomponentti näytäEntitytLabel = new LabelKomponentti(0.025f, 0.03f, -0.25f, 0.74f, Assets.annaTekstuuri("editori_näytä_entityt"));
    private static ValintaLaatikko näytäEntitytNappi = new ValintaLaatikko(true, 0.025f, 0.03f, -0.2f, 0.74f, new TooltipTeksti("Näytä entityt", 500, 48));

    private static LabelKomponentti näytäTaustaLabel = new LabelKomponentti(0.025f, 0.03f, -0.1f, 0.86f, Assets.annaTekstuuri("editori_näytä_tausta"));
    private static ValintaLaatikko näytäTaustaNappi = new ValintaLaatikko(true, 0.025f, 0.03f, -0.05f, 0.86f, new TooltipTeksti("Näytä tausta", 500, 48));
    private static LabelKomponentti näytäOsoitinLabel = new LabelKomponentti(0.025f, 0.03f, -0.1f, 0.8f, Assets.annaTekstuuri("editori_näytä_osoitin"));
    private static ValintaLaatikko näytäOsoitinNappi = new ValintaLaatikko(true, 0.025f, 0.03f, -0.05f, 0.8f, new TooltipTeksti("Näytä osoittimen sijainti", 900, 48));
    private static LabelKomponentti näytäDebugLabel = new LabelKomponentti(0.025f, 0.03f, -0.1f, 0.74f, Assets.annaTekstuuri("editori_näytä_debug"));
    private static ValintaLaatikko näytäDebugNappi = new ValintaLaatikko(false, 0.025f, 0.03f, -0.05f, 0.74f, new TooltipTeksti("Näytä Debug-tiedot (F3)", 900, 48));
    
    protected static void alustaGrafiikat() {
        otsikkoTeksti = new Teksti("Näytä", Väri.white, 400, 48);
        otsikko.päivitäSisältö(otsikkoTeksti);
    }

    public static void tarkistaHover(int hiiriX, int hiiriY) {
        näytäMaastoNappi.hiiriSisällä(hiiriX, hiiriY);
        näytäObjektitNappi.hiiriSisällä(hiiriX, hiiriY);
        näytäEntitytNappi.hiiriSisällä(hiiriX, hiiriY);
        näytäTaustaNappi.hiiriSisällä(hiiriX, hiiriY);
        näytäOsoitinNappi.hiiriSisällä(hiiriX, hiiriY);
        näytäDebugNappi.hiiriSisällä(hiiriX, hiiriY);
    }

    public static void tarkistaPainetutNapit(int hiiriX, int hiiriY) {
        if (näytäMaastoNappi.hiiriSisällä(hiiriX, hiiriY)) {
            näytäMaastoNappi.valitse();
            EditoriRuutu.maastoNäkyvissä = näytäMaastoNappi.valittu();
        }
        else if (näytäObjektitNappi.hiiriSisällä(hiiriX, hiiriY)) {
            näytäObjektitNappi.valitse();
            EditoriRuutu.kenttäNäkyvissä = näytäObjektitNappi.valittu();
        }
        else if (näytäEntitytNappi.hiiriSisällä(hiiriX, hiiriY)) {
            näytäEntitytNappi.valitse();
            EditoriRuutu.entitytNäkyvissä = näytäEntitytNappi.valittu();
        }
        else if (näytäTaustaNappi.hiiriSisällä(hiiriX, hiiriY)) {
            näytäTaustaNappi.valitse();
            EditoriRuutu.taustaNäkyvissä = näytäTaustaNappi.valittu();
        }
        else if (näytäOsoitinNappi.hiiriSisällä(hiiriX, hiiriY)) {
            näytäOsoitinNappi.valitse();
            EditoriRuutu.näytäTileTooltip = näytäOsoitinNappi.valittu();
        }
        else if (näytäDebugNappi.hiiriSisällä(hiiriX, hiiriY)) {
            näytäDebug();
        }
    }

    public static void näytäDebug() {
        näytäDebugNappi.valitse();
        EditoriRuutu.debugTiedotNäkyvissä = näytäDebugNappi.valittu();
    }

    public static void renderöi(Shader shader, Ikkuna ikkuna) {
        renderöiNäytettävienOsienTiedot(shader, ikkuna);
        renderöiTooltipTekstit(shader, ikkuna);
    }

    private static void renderöiNäytettävienOsienTiedot(Shader shader, Ikkuna ikkuna) {
        otsikko.renderöi(shader, ikkuna);

        näytäMaastoLabel.renderöi(shader, ikkuna);
        näytäMaastoNappi.renderöi(shader, ikkuna);
        näytäObjektitLabel.renderöi(shader, ikkuna);
        näytäObjektitNappi.renderöi(shader, ikkuna);
        näytäEntitytLabel.renderöi(shader, ikkuna);
        näytäEntitytNappi.renderöi(shader, ikkuna);

        näytäTaustaLabel.renderöi(shader, ikkuna);
        näytäTaustaNappi.renderöi(shader, ikkuna);
        näytäOsoitinLabel.renderöi(shader, ikkuna);
        näytäOsoitinNappi.renderöi(shader, ikkuna);
        näytäDebugLabel.renderöi(shader, ikkuna);
        näytäDebugNappi.renderöi(shader, ikkuna);
    }

    private static void renderöiTooltipTekstit(Shader shader, Ikkuna ikkuna) {
        näytäMaastoNappi.renderöiTooltip(shader, ikkuna);
        näytäObjektitNappi.renderöiTooltip(shader, ikkuna);
        näytäEntitytNappi.renderöiTooltip(shader, ikkuna);
        näytäTaustaNappi.renderöiTooltip(shader, ikkuna);
        näytäOsoitinNappi.renderöiTooltip(shader, ikkuna);
        näytäDebugNappi.renderöiTooltip(shader, ikkuna);
    }
}
