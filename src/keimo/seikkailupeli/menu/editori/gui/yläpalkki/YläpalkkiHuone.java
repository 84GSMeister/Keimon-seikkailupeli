package keimo.seikkailupeli.menu.editori.gui.yläpalkki;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
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

    private static StaattinenKomponentti otsikko = new StaattinenKomponentti(0.5f, 0.05f, 0.25f, 0.95f, new Teksti("Huone", Color.white, 400, 48));
    private static Nappi vaihdaHuonettaNappiVasen = new Nappi(-0.05f, 0.05f, -0.3f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/huone_vaihda_nuoli.png"), new TooltipTeksti("Edellinen huone"));
    private static Nappi vaihdaHuonettaNappiOikea = new Nappi(0.05f, 0.05f, 0f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/huone_vaihda_nuoli.png"), new TooltipTeksti("Seuraava huone"));
    private static Nappi uusiHuoneNappi = new Nappi(0.05f, 0.05f, 0.1f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/huone_uusi.png"), new TooltipTeksti("Luo uusi huone"));
    private static Nappi poistaHuoneNappi = new Nappi(0.05f, 0.05f, 0.2f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/huone_poista.png"), new TooltipTeksti("Poista huone"));
    private static Nappi muokkaaHuoneenTietojaNappi = new Nappi(0.1f, 0.05f, -0.15f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/popup_valinta_pohja.png"), new TooltipTeksti("Huoneen asetukset"));
    private static Teksti huoneenIdTeksti = new Teksti("ID", Color.yellow, 1000, 48);
    private static Teksti huoneenNimiTeksti = new Teksti("Huoneen nimi", Color.yellow, 1500, 48);
    private static Teksti huoneenAlueTeksti = new Teksti("Alue", Color.yellow, 1200, 48);
    private static Teksti huoneenKokoTeksti = new Teksti("Koko", Color.yellow, 1000, 48);
    
    public static void tarkistaHover(int hiiriX, int hiiriY) {
        vaihdaHuonettaNappiVasen.hiiriSisällä(hiiriX, hiiriY);
        vaihdaHuonettaNappiOikea.hiiriSisällä(hiiriX, hiiriY);
        uusiHuoneNappi.hiiriSisällä(hiiriX, hiiriY);
        poistaHuoneNappi.hiiriSisällä(hiiriX, hiiriY);
        muokkaaHuoneenTietojaNappi.hiiriSisällä(hiiriX, hiiriY);
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
            //EditoriRuutu.avaaHuoneenLuontiIkkuna(true);
            uusiHuoneNappi.poistaValinta();
            for (int i = 0; i < EditoriRuutu.editorinHuoneKartta.size()+1; i++) {
                if (!EditoriRuutu.editorinHuoneKartta.containsKey(i)) {
                    HuoneenLuontiIkkuna.luoIkkuna(i);
                    break;
                }
            }
        }
        else if (poistaHuoneNappi.hiiriSisällä(hiiriX, hiiriY)) {
            //EditoriRuutu.avaaHuoneenLuontiIkkuna(true);
            poistaHuoneNappi.poistaValinta();
            if (tinyfd_messageBox("Poista huone", "Haluatko varmasti poistaa huoneen " + EditoriRuutu.ladatunHuoneenId + "?", "yesno", "question", false)) {
                if (EditoriRuutu.ladatunHuoneenId <= 0) {
                    tinyfd_messageBox("Poista huone", "Huonetta 0 ei voi poistaa.", "ok", "error", false);
                }
                else {
                    EditoriRuutu.editorinHuoneKartta.remove(EditoriRuutu.ladatunHuoneenId);
                    EditoriRuutu.lataaHuone(EditoriRuutu.ladatunHuoneenId -1);
                }
            }
        }
        else if (muokkaaHuoneenTietojaNappi.hiiriSisällä(hiiriX, hiiriY)) {
            //EditoriRuutu.avaaMuokkausIkkuna(true);
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

        muokkaaHuoneenTietojaNappi.renderöi(shader, ikkuna);

        float scaleX = 0.2f, scaleY = 0.025f;
        float offsetX = 1f - scaleX - 0.05f, offsetY = 1f - 0.14f;
        huoneenIdTeksti.päivitäTeksti("ID: " + EditoriRuutu.ladatunHuoneenId);
        huoneenIdTeksti.bind(0);
        renderöiKomponentti(shader, huoneenIdTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);

        offsetY = 1f - 0.18f;
        huoneenNimiTeksti.päivitäTeksti("Nimi: " + EditoriRuutu.ladattuHuone.annaNimi());
        huoneenNimiTeksti.bind(0);
        renderöiKomponentti(shader, huoneenNimiTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);

        offsetY = 1f - 0.22f;
        huoneenAlueTeksti.päivitäTeksti("Alue: " + EditoriRuutu.ladattuHuone.annaAlue());
        huoneenAlueTeksti.bind(0);
        renderöiKomponentti(shader, huoneenAlueTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);

        offsetY = 1f - 0.26f;
        huoneenKokoTeksti.päivitäTeksti("Koko: " + EditoriRuutu.ladattuHuone.annaKoko());
        huoneenKokoTeksti.bind(0);
        renderöiKomponentti(shader, huoneenKokoTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);

        vaihdaHuonettaNappiVasen.renderöi(shader, ikkuna);
        vaihdaHuonettaNappiOikea.renderöi(shader, ikkuna);
        uusiHuoneNappi.renderöi(shader, ikkuna);
        poistaHuoneNappi.renderöi(shader, ikkuna);
    }

    private static void renderöiTooltipTekstit(Shader shader, Ikkuna ikkuna) {
        vaihdaHuonettaNappiVasen.renderöiTooltip(shader, ikkuna);
        vaihdaHuonettaNappiOikea.renderöiTooltip(shader, ikkuna);
        uusiHuoneNappi.renderöiTooltip(shader, ikkuna);
        poistaHuoneNappi.renderöiTooltip(shader, ikkuna);
        muokkaaHuoneenTietojaNappi.renderöiTooltip(shader, ikkuna);
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
