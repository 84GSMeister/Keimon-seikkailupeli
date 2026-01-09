package keimo.seikkailupeli.menu.editori.gui.yläpalkki;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.guikomponentit.ValintaLaatikko;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.menu.editori.EditoriRuutu;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class YläpalkkiNäytä {

    private static StaattinenKomponentti otsikko = new StaattinenKomponentti(0.5f, 0.05f, 0.25f, 0.95f, new Teksti("Näytä", Color.white, 400, 48));
    private static ValintaLaatikko näytäTiletNappi = new ValintaLaatikko(true, 0.025f, 0.03f, 0.05f, 0.86f);
    private static ValintaLaatikko näytäObjektitNappi = new ValintaLaatikko(true, 0.025f, 0.03f, 0.05f, 0.8f);
    private static ValintaLaatikko näytäEntitytNappi = new ValintaLaatikko(true, 0.025f, 0.03f, 0.05f, 0.74f);
    private static Teksti näytäTiletTeksti = new Teksti("1: Näytä tilet", Color.yellow, 1000, 48);
    private static Teksti näytäObjektitTeksti = new Teksti("2: Näytä objektit", Color.yellow, 1000, 48);
    private static Teksti näytäEntitytTeksti = new Teksti("3: Näytä entityt", Color.yellow, 1000, 48);
    private static StaattinenKomponentti näytäOsoitinLabel = new StaattinenKomponentti(0.025f, 0.03f, 0.35f, 0.86f, new Tekstuuri("tiedostot/kuvat/editori/osoitin.png"));
    private static ValintaLaatikko näytäOsoitinNappi = new ValintaLaatikko(true, 0.025f, 0.03f, 0.4f, 0.86f, new TooltipTeksti("Näytä osoittimen sijainti", 1000, 48));
    
    public static void tarkistaHover(int hiiriX, int hiiriY) {
        näytäTiletNappi.hiiriSisällä(hiiriX, hiiriY);
        näytäObjektitNappi.hiiriSisällä(hiiriX, hiiriY);
        näytäEntitytNappi.hiiriSisällä(hiiriX, hiiriY);
        näytäOsoitinNappi.hiiriSisällä(hiiriX, hiiriY);
    }

    public static void tarkistaPainetutNapit(int hiiriX, int hiiriY) {
        if (näytäTiletNappi.hiiriSisällä(hiiriX, hiiriY)) {
            näytäTiletNappi.valitse();
            EditoriRuutu.maastoNäkyvissä = näytäTiletNappi.valittu();
        }
        else if (näytäObjektitNappi.hiiriSisällä(hiiriX, hiiriY)) {
            näytäObjektitNappi.valitse();
            EditoriRuutu.kenttäNäkyvissä = näytäObjektitNappi.valittu();
        }
        else if (näytäEntitytNappi.hiiriSisällä(hiiriX, hiiriY)) {
            näytäEntitytNappi.valitse();
            EditoriRuutu.entitytNäkyvissä = näytäEntitytNappi.valittu();
        }
        else if (näytäOsoitinNappi.hiiriSisällä(hiiriX, hiiriY)) {
            näytäOsoitinNappi.valitse();
            EditoriRuutu.näytäTileTooltip = näytäOsoitinNappi.valittu();
        }
    }

    public static void renderöi(Shader shader, Ikkuna ikkuna) {
        renderöiNäytettävienOsienTiedot(shader, ikkuna);
        renderöiTooltipTekstit(shader, ikkuna);
    }

    private static void renderöiNäytettävienOsienTiedot(Shader shader, Ikkuna ikkuna) {
        otsikko.renderöi(shader, ikkuna);

        näytäTiletNappi.renderöi(shader, ikkuna);
        float scaleX = 0.2f, scaleY = 0.025f;
        float offsetX = scaleX + 0.1f, offsetY = 0.86f;
        String teksti = "Maasto: " + (EditoriRuutu.maastoNäkyvissä ? "kyllä" : "ei");
        Color väri = EditoriRuutu.maastoNäkyvissä ? Color.green : Color.red;
        näytäTiletTeksti.päivitäTeksti(teksti, 0, 50, väri);
        näytäTiletTeksti.bind(0);
        renderöiKomponentti(shader, näytäTiletTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);

        näytäObjektitNappi.renderöi(shader, ikkuna);
        offsetY = 0.8f;
        teksti = "Objektit: " + (EditoriRuutu.kenttäNäkyvissä ? "kyllä" : "ei");
        väri = EditoriRuutu.kenttäNäkyvissä ? Color.green : Color.red;
        näytäObjektitTeksti.päivitäTeksti(teksti, 0, 50, väri);
        näytäObjektitTeksti.bind(0);
        renderöiKomponentti(shader, näytäObjektitTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);

        näytäEntitytNappi.renderöi(shader, ikkuna);
        offsetY = 0.74f;
        teksti = "Entityt: " + (EditoriRuutu.entitytNäkyvissä ? "kyllä" : "ei");
        väri = EditoriRuutu.entitytNäkyvissä ? Color.green : Color.red;
        näytäEntitytTeksti.päivitäTeksti(teksti, 0, 50, väri);
        näytäEntitytTeksti.bind(0);
        renderöiKomponentti(shader, näytäEntitytTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);

        näytäOsoitinLabel.renderöi(shader, ikkuna);
        näytäOsoitinNappi.renderöi(shader, ikkuna);
    }

    private static void renderöiTooltipTekstit(Shader shader, Ikkuna ikkuna) {
        näytäOsoitinNappi.renderöiTooltip(shader, ikkuna);
    }

    private static void renderöiKomponentti(Shader shader, Renderöitävä tekstuuri, Ikkuna window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ) {
        Matrix4f sijaintiMatriisi = new Matrix4f();
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        shader.setUniform("projection", sijaintiMatriisi);
        shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        tekstuuri.bind(0);
        Assets.getModel().render();
    }
}
