package keimo.seikkailupeli.menu.editori.gui.yläpalkki;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.menu.editori.EditoriRuutu;
import keimo.seikkailupeli.menu.editori.gui.HuoneenLuontiIkkuna;
import keimo.seikkailupeli.menu.editori.gui.MuokkausIkkuna;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class YläpalkkiHuone {

    private static Teksti otsikkoTeksti;
    private static StaattinenKomponentti otsikko = new StaattinenKomponentti(0.5f, 0.05f, 0.25f, 0.95f, otsikkoTeksti);
    private static Nappi vaihdaHuonettaNappiVasen = new Nappi(-0.05f, 0.05f, -0.3f, 0.8f, Assets.annaTekstuuri("editori_huone_vaihda"), new TooltipTeksti("Edellinen huone"));
    private static Nappi vaihdaHuonettaNappiOikea = new Nappi(0.05f, 0.05f, 0f, 0.8f, Assets.annaTekstuuri("editori_huone_vaihda"), new TooltipTeksti("Seuraava huone"));
    private static Nappi uusiHuoneNappi = new Nappi(0.05f, 0.05f, 0.1f, 0.8f, Assets.annaTekstuuri("editori_huone_uusi"), new TooltipTeksti("Luo uusi huone"));
    private static Nappi poistaHuoneNappi = new Nappi(0.05f, 0.05f, 0.2f, 0.8f, Assets.annaTekstuuri("editori_huone_poista"), new TooltipTeksti("Poista huone"));
    private static Nappi muokkaaHuoneenTietojaNappi = new Nappi(0.1f, 0.05f, -0.15f, 0.8f, Assets.annaTekstuuri("editori_popup_pohja"), new TooltipTeksti("Huoneen asetukset"));
    private static Nappi huoneenTiedotNappi = new Nappi(0.15f, 0.125f, 0.5f, 0.85f, Assets.annaTekstuuri("editori_popup_pohja"), new TooltipTeksti("Huoneen asetukset"));
    private static Teksti huoneenIdTeksti;
    private static Teksti huoneenNimiTeksti;
    private static Teksti huoneenAlueTeksti;
    private static Teksti huoneenKokoTeksti;

    protected static void alustaGrafiikat() {
        otsikkoTeksti = new Teksti("Huone", Color.white, 400, 48);
        otsikko.päivitäSisältö(otsikkoTeksti);
        huoneenIdTeksti = new Teksti("ID", Color.yellow, 1000, 48);
        huoneenNimiTeksti = new Teksti("Huoneen nimi", Color.yellow, 1500, 48);
        huoneenAlueTeksti = new Teksti("Alue", Color.yellow, 1200, 48);
        huoneenKokoTeksti = new Teksti("Koko", Color.yellow, 1000, 48);
    }
    
    public static void tarkistaHover(int hiiriX, int hiiriY) {
        vaihdaHuonettaNappiVasen.hiiriSisällä(hiiriX, hiiriY);
        muokkaaHuoneenTietojaNappi.hiiriSisällä(hiiriX, hiiriY);
        vaihdaHuonettaNappiOikea.hiiriSisällä(hiiriX, hiiriY);
        uusiHuoneNappi.hiiriSisällä(hiiriX, hiiriY);
        poistaHuoneNappi.hiiriSisällä(hiiriX, hiiriY);
        huoneenTiedotNappi.hiiriSisällä(hiiriX, hiiriY);
    }

    public static void tarkistaPainetutNapit(int hiiriX, int hiiriY) {
        if (vaihdaHuonettaNappiVasen.hiiriSisällä(hiiriX, hiiriY)) {
            if (EditoriRuutu.ladatunHuoneenId > 0) {
                EditoriRuutu.lataaHuone(EditoriRuutu.ladatunHuoneenId-1, true);
            }
        }
        else if (vaihdaHuonettaNappiOikea.hiiriSisällä(hiiriX, hiiriY)) {
            if (EditoriRuutu.ladatunHuoneenId < EditoriRuutu.editorinHuoneKartta.size()-1) {
                EditoriRuutu.lataaHuone(EditoriRuutu.ladatunHuoneenId+1);
            }
        }
        else if (uusiHuoneNappi.hiiriSisällä(hiiriX, hiiriY)) {
            uusiHuoneNappi.poistaValinta();
            for (int i = 0; i < EditoriRuutu.editorinHuoneKartta.size()+1; i++) {
                if (!EditoriRuutu.editorinHuoneKartta.containsKey(i)) {
                    HuoneenLuontiIkkuna.luoIkkuna(i);
                    break;
                }
            }
        }
        else if (poistaHuoneNappi.hiiriSisällä(hiiriX, hiiriY)) {
            poistaHuoneNappi.poistaValinta();
            if (tinyfd_messageBox("Poista huone", "Haluatko varmasti poistaa huoneen " + EditoriRuutu.ladatunHuoneenId + "?", "yesno", "question", false)) {
                if (EditoriRuutu.ladatunHuoneenId <= 0) {
                    tinyfd_messageBox("Poista huone", "Huonetta 0 ei voi poistaa. Huoneen 0 on aina oltava aloitushuone", "ok", "error", false);
                }
                else {
                    EditoriRuutu.editorinHuoneKartta.remove(EditoriRuutu.ladatunHuoneenId);
                    EditoriRuutu.lataaHuone(EditoriRuutu.ladatunHuoneenId -1);
                }
            }
        }
        else if (muokkaaHuoneenTietojaNappi.hiiriSisällä(hiiriX, hiiriY) || huoneenTiedotNappi.hiiriSisällä(hiiriX, hiiriY)) {
            muokkaaHuoneenTietojaNappi.poistaValinta();
            MuokkausIkkuna.luoIkkuna(EditoriRuutu.ladatunHuoneenId);
        }
    }

    public static void renderöi(Shader shader, Ikkuna ikkuna) {
        renderöiHuoneenTiedot(shader, ikkuna);
        renderöiTooltipTekstit(shader, ikkuna);
    }

    private static void renderöiHuoneenTiedot(Shader shader, Ikkuna ikkuna) {
        otsikko.renderöi(shader, ikkuna);

        huoneenTiedotNappi.renderöi(shader, ikkuna);

        float scaleX = 0.2f, scaleY = 0.025f;
        float offsetX = 0.575f, offsetY = 0.94f;
        huoneenIdTeksti.päivitäTeksti("ID: " + EditoriRuutu.ladatunHuoneenId);
        huoneenIdTeksti.bind(0);
        renderöiKomponentti(shader, huoneenIdTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);

        offsetY = 0.88f;
        huoneenNimiTeksti.päivitäTeksti("Nimi: " + EditoriRuutu.ladattuHuone.annaNimi());
        huoneenNimiTeksti.bind(0);
        renderöiKomponentti(shader, huoneenNimiTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);

        offsetY = 0.82f;
        huoneenAlueTeksti.päivitäTeksti("Alue: " + EditoriRuutu.ladattuHuone.annaAlue());
        huoneenAlueTeksti.bind(0);
        renderöiKomponentti(shader, huoneenAlueTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);

        offsetY = 0.76f;
        huoneenKokoTeksti.päivitäTeksti("Koko: " + EditoriRuutu.ladattuHuone.annaKoko());
        huoneenKokoTeksti.bind(0);
        renderöiKomponentti(shader, huoneenKokoTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);

        vaihdaHuonettaNappiVasen.renderöi(shader, ikkuna);
        muokkaaHuoneenTietojaNappi.renderöi(shader, ikkuna);
        vaihdaHuonettaNappiOikea.renderöi(shader, ikkuna);
        uusiHuoneNappi.renderöi(shader, ikkuna);
        poistaHuoneNappi.renderöi(shader, ikkuna);
    }

    private static void renderöiTooltipTekstit(Shader shader, Ikkuna ikkuna) {
        vaihdaHuonettaNappiVasen.renderöiTooltip(shader, ikkuna);
        muokkaaHuoneenTietojaNappi.renderöiTooltip(shader, ikkuna);
        vaihdaHuonettaNappiOikea.renderöiTooltip(shader, ikkuna);
        uusiHuoneNappi.renderöiTooltip(shader, ikkuna);
        poistaHuoneNappi.renderöiTooltip(shader, ikkuna);
        huoneenTiedotNappi.renderöiTooltip(shader, ikkuna);
    }

    private static void renderöiKomponentti(Shader shader, Renderöitävä tekstuuri, Ikkuna window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ) {
        Matrix4f sijaintiMatriisi = new Matrix4f();
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        shader.asetaSijainti(sijaintiMatriisi);
        shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        tekstuuri.bind(0);
        Assets.getModel().render();
    }
}
