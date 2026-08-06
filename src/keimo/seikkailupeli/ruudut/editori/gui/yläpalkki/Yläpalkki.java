package keimo.seikkailupeli.ruudut.editori.gui.yläpalkki;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.ikkuna.DialogiIkkunat;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.PeliObjekti;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu;
import keimo.seikkailupeli.ruudut.editori.gui.HuoneenLuontiIkkuna;
import keimo.seikkailupeli.ruudut.editori.gui.MuokkausIkkuna;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Yläpalkki {

    private static Renderöitävä pohjaVasenTekstuuri = Assets.annaTekstuuri("editori_yläpalkki_pohja_vasen");
    private static Renderöitävä pohjaKeskiTekstuuri = Assets.annaTekstuuri("editori_yläpalkki_pohja_keski");
    private static LabelKomponentti pohjaVasenLabel = new LabelKomponentti(0.25f, 0.15f, -0.75f, 0.85f, pohjaVasenTekstuuri);
    private static LabelKomponentti pohjaKeskiLabel = new LabelKomponentti(0.5f, 0.15f, 0, 0.85f, pohjaKeskiTekstuuri);
    private static LabelKomponentti pohjaOikeaLabel = new LabelKomponentti(0.25f, 0.15f, 0.75f, 0.85f, pohjaVasenTekstuuri);
    private static Nappi välilehtiTiedostoNappi = new Nappi(0.122f, 0.045f, -0.872f, 0.94f, Assets.annaTekstuuri("editori_välilehti_tiedosto"), new TooltipTeksti("Tiedosto"));
    private static Nappi välilehtiKenttäNappi = new Nappi(0.122f, 0.045f, -0.872f, 0.85f, Assets.annaTekstuuri("editori_välilehti_kenttä"), new TooltipTeksti("Kenttä"));
    private static Nappi välilehtiNäytäNappi = new Nappi(0.122f, 0.045f, -0.628f, 0.94f, Assets.annaTekstuuri("editori_välilehti_näytä"), new TooltipTeksti("Näytä"));
    private static Nappi välilehtiLisäosatNappi = new Nappi(0.122f, 0.045f, -0.628f, 0.85f, Assets.annaTekstuuri("editori_välilehti_lisäosat"), new TooltipTeksti("Lisäosat"));

    private static Nappi vaihdaHuonettaNappiVasen = new Nappi(-0.05f, 0.05f, 0.8f, 0.8f, Assets.annaTekstuuri("editori_huone_vaihda"), new TooltipTeksti("Edellinen huone"));
    private static Nappi vaihdaHuonettaNappiOikea = new Nappi(0.05f, 0.05f, 0.9f, 0.8f, Assets.annaTekstuuri("editori_huone_vaihda"), new TooltipTeksti("Seuraava huone"));
    private static Nappi uusiHuoneNappi = new Nappi(0.05f, 0.05f, 0.8f, 0.9f, Assets.annaTekstuuri("editori_huone_uusi"), new TooltipTeksti("Luo uusi huone"));
    private static Nappi poistaHuoneNappi = new Nappi(0.05f, 0.05f, 0.9f, 0.9f, Assets.annaTekstuuri("editori_huone_poista"), new TooltipTeksti("Poista huone"));
    private static Nappi huoneenTiedotNappi = new Nappi(0.125f, 0.125f, 0.625f, 0.85f, Assets.annaTekstuuri("editori_popup_pohja"), new TooltipTeksti("Huoneen asetukset"));
    private static Teksti huoneenIdTeksti;
    private static Teksti huoneenNimiTeksti;
    private static Teksti huoneenAlueTeksti;
    private static Teksti huoneenKokoTeksti;

    public static enum Välilehdet {
        TIEDOSTO,
        KENTTÄ,
        NÄYTÄ,
        LISÄOSAT;
    }
    public static Välilehdet nykyinenVälilehti = Välilehdet.KENTTÄ;

    public static void alustaGrafiikat() {
        YläpalkkiTiedosto.alustaGrafiikat();
        YläpalkkiKenttä.alustaGrafiikat();
        YläpalkkiNäytä.alustaGrafiikat();
        YläpalkkiLisäosat.alustaGrafiikat();

        huoneenIdTeksti = new Teksti("ID", Väri.yellow, 1000, 48);
        huoneenNimiTeksti = new Teksti("Huoneen nimi", Väri.yellow, 1500, 48);
        huoneenAlueTeksti = new Teksti("Alue", Väri.yellow, 1200, 48);
        huoneenKokoTeksti = new Teksti("Koko", Väri.yellow, 1000, 48);
    }

    public static void tarkistaYläpalkkiHover(int hiiriX, int hiiriY) {
        välilehtiTiedostoNappi.hiiriSisällä(hiiriX, hiiriY);
        välilehtiKenttäNappi.hiiriSisällä(hiiriX, hiiriY);
        välilehtiNäytäNappi.hiiriSisällä(hiiriX, hiiriY);
        välilehtiLisäosatNappi.hiiriSisällä(hiiriX, hiiriY);
        switch (nykyinenVälilehti) {
            case TIEDOSTO -> {
                YläpalkkiTiedosto.tarkistaHover(hiiriX, hiiriY);
            }
            case KENTTÄ -> {
                YläpalkkiKenttä.tarkistaHover(hiiriX, hiiriY);
            }
            case NÄYTÄ -> {
                YläpalkkiNäytä.tarkistaHover(hiiriX, hiiriY);
            }
            case LISÄOSAT -> {
                YläpalkkiLisäosat.tarkistaHover(hiiriX, hiiriY);
            }
        }
        vaihdaHuonettaNappiVasen.hiiriSisällä(hiiriX, hiiriY);
        vaihdaHuonettaNappiOikea.hiiriSisällä(hiiriX, hiiriY);
        uusiHuoneNappi.hiiriSisällä(hiiriX, hiiriY);
        poistaHuoneNappi.hiiriSisällä(hiiriX, hiiriY);
        huoneenTiedotNappi.hiiriSisällä(hiiriX, hiiriY);
    }

    public static void tarkistaPainetutNapit(int hiiriX, int hiiriY) {
        if (välilehtiTiedostoNappi.hiiriSisällä(hiiriX, hiiriY)) {
            nykyinenVälilehti = Välilehdet.TIEDOSTO;
        }
        else if (välilehtiKenttäNappi.hiiriSisällä(hiiriX, hiiriY)) {
            nykyinenVälilehti = Välilehdet.KENTTÄ;
        }
        else if (välilehtiNäytäNappi.hiiriSisällä(hiiriX, hiiriY)) {
            nykyinenVälilehti = Välilehdet.NÄYTÄ;
        }
        else if (välilehtiLisäosatNappi.hiiriSisällä(hiiriX, hiiriY)) {
            nykyinenVälilehti = Välilehdet.LISÄOSAT;
        }
        switch (nykyinenVälilehti) {
            case TIEDOSTO -> {
                YläpalkkiTiedosto.tarkistaPainetutNapit(hiiriX, hiiriY);
            }
            case KENTTÄ -> {
                YläpalkkiKenttä.tarkistaPainetutNapit(hiiriX, hiiriY);
            }
            case NÄYTÄ -> {
                YläpalkkiNäytä.tarkistaPainetutNapit(hiiriX, hiiriY);
            }
            case LISÄOSAT -> {
                YläpalkkiLisäosat.tarkistaPainetutNapit(hiiriX, hiiriY);
            }
        }

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
            if (DialogiIkkunat.viestiIkkuna("Poista huone", "Haluatko varmasti poistaa huoneen " + EditoriRuutu.ladatunHuoneenId + "?", "yesno", "question", false)) {
                if (EditoriRuutu.ladatunHuoneenId <= 0) {
                    DialogiIkkunat.viestiIkkuna("Poista huone", "Huonetta 0 ei voi poistaa. Huoneen 0 on aina oltava aloitushuone", "ok", "error", false);
                }
                else {
                    EditoriRuutu.editorinHuoneKartta.remove(EditoriRuutu.ladatunHuoneenId);
                    EditoriRuutu.lataaHuone(EditoriRuutu.ladatunHuoneenId -1);
                }
            }
        }
        else if (huoneenTiedotNappi.hiiriSisällä(hiiriX, hiiriY)) {
            if (!vaihdaHuonettaNappiVasen.hiiriSisällä(hiiriX, hiiriY) && !vaihdaHuonettaNappiOikea.hiiriSisällä(hiiriX, hiiriY) && !uusiHuoneNappi.hiiriSisällä(hiiriX, hiiriY) && !poistaHuoneNappi.hiiriSisällä(hiiriX, hiiriY)) {
                MuokkausIkkuna.luoIkkuna(EditoriRuutu.ladatunHuoneenId);
            }
        }
    }

    public static void asetaValittuObjekti(PeliObjekti objekti) {
        YläpalkkiKenttä.asetaValittuObjekti(objekti);
        nykyinenVälilehti = Välilehdet.KENTTÄ;
    }

    public static void renderöi(Shader shader, Ikkuna ikkuna) {
        pohjaVasenLabel.renderöi(shader, ikkuna);
        pohjaKeskiLabel.renderöi(shader, ikkuna);
        pohjaOikeaLabel.renderöi(shader, ikkuna);

        välilehtiTiedostoNappi.renderöi(shader, ikkuna);
        välilehtiKenttäNappi.renderöi(shader, ikkuna);
        välilehtiNäytäNappi.renderöi(shader, ikkuna);
        välilehtiLisäosatNappi.renderöi(shader, ikkuna);

        switch (nykyinenVälilehti) {
            case TIEDOSTO -> {
                YläpalkkiTiedosto.renderöi(shader, ikkuna);
            }
            case KENTTÄ -> {
                YläpalkkiKenttä.renderöi(shader, ikkuna);
            }
            case NÄYTÄ -> {
                YläpalkkiNäytä.renderöi(shader, ikkuna);
            }
            case LISÄOSAT -> {
                YläpalkkiLisäosat.renderöi(shader, ikkuna);
            }
        }
        renderöiHuoneenTiedot(shader, ikkuna);
        renderöiTooltipTekstit(shader, ikkuna);
    }

    private static void renderöiHuoneenTiedot(Shader shader, Ikkuna ikkuna) {
        huoneenTiedotNappi.renderöi(shader, ikkuna);

        float scaleX = 0.2f, scaleY = 0.025f;
        float offsetX = 0.725f, offsetY = 0.94f;
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
        vaihdaHuonettaNappiOikea.renderöi(shader, ikkuna);
        uusiHuoneNappi.renderöi(shader, ikkuna);
        poistaHuoneNappi.renderöi(shader, ikkuna);
    }

    private static void renderöiTooltipTekstit(Shader shader, Ikkuna ikkuna) {
        vaihdaHuonettaNappiVasen.renderöiTooltip(shader, ikkuna);
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
