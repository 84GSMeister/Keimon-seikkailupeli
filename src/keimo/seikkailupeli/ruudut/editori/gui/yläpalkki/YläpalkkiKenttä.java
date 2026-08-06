package keimo.seikkailupeli.ruudut.editori.gui.yläpalkki;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.PeliObjekti;
import keimo.seikkailupeli.objektit.Suunnallinen;
import keimo.seikkailupeli.objektit.Suunnallinen.Suunta;
import keimo.seikkailupeli.objektit.entityt.Entity;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.objektit.maastot.Maasto;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class YläpalkkiKenttä {

    private static Teksti otsikkoTeksti;
    private static LabelKomponentti otsikko = new LabelKomponentti(0.5f, 0.05f, 0.25f, 0.95f, otsikkoTeksti);
    
    private static Teksti valitunEsineenNimiTeksti;
    private static Teksti valitunMaastonKuvaTeksti;
    private static Renderöitävä kopioituTekstuuri = Assets.annaTekstuuri("editori_kopioi_ominaisuudet");
    private static Teksti objektinKääntöAsteetTeksti;
    private static Renderöitävä objektinPeilausValittuKuvake = Assets.annaTekstuuri("editori_valittu");
    private static Renderöitävä valitunEsineenTekstuuri;

    private static Nappi valitunEsineenTekstuuriNappi = new Nappi(0.05f, 0.1f, -0.35f, 0.85f, valitunEsineenTekstuuri, new TooltipTeksti("Valitse objekti"));
    private static LabelKomponentti kopioituTekstuuriLabel = new LabelKomponentti(0.0125f, 0.025f, -0.300f, 0.775f, kopioituTekstuuri);

    private static Nappi käännäObjektiaNappi = new Nappi(0.025f, 0.05f, 0f, 0.8f, Assets.annaTekstuuri("editori_objekti_kääntö"), new TooltipTeksti("Käännä objektia"));
    private static Nappi peilaaObjektiXNappi = new Nappi(0.025f, 0.05f, 0.1f, 0.8f, Assets.annaTekstuuri("editori_objekti_peilaus_x"), new TooltipTeksti("Peilaa vaakasuunnassa", 750, 48));
    private static Nappi peilaaObjektiYNappi = new Nappi(0.025f, 0.05f, 0.2f, 0.8f, Assets.annaTekstuuri("editori_objekti_peilaus_y"), new TooltipTeksti("Peilaa pystysuunnassa", 750, 48));
    private static LabelKomponentti objektinKääntöAsteetLabel = new LabelKomponentti(0.025f, 0.05f, 0f, 0.8f, objektinKääntöAsteetTeksti);
    private static LabelKomponentti objektinPeilausXLabel = new LabelKomponentti(0.025f, 0.05f, 0.1f, 0.8f, objektinPeilausValittuKuvake);
    private static LabelKomponentti objektinPeilausYLabel = new LabelKomponentti(0.025f, 0.05f, 0.2f, 0.8f, objektinPeilausValittuKuvake);
    
    protected static void alustaGrafiikat() {
        otsikkoTeksti = new Teksti("Kenttä", Väri.white, 400, 48);
        otsikko.päivitäSisältö(otsikkoTeksti);
        valitunEsineenNimiTeksti = new Teksti("Valitse esine", Väri.magenta, 1200, 36);
        valitunMaastonKuvaTeksti = new Teksti("", Väri.blue, 1200, 36);
        objektinKääntöAsteetTeksti = new Teksti("" + EditoriRuutu.kääntöAsteet, Väri.orange, 100, 36);
        objektinKääntöAsteetLabel.päivitäSisältö(objektinKääntöAsteetTeksti);
        objektinPeilausXLabel.päivitäSisältö(objektinPeilausValittuKuvake);
        objektinPeilausYLabel.päivitäSisältö(objektinPeilausValittuKuvake);
    }

    public static void tarkistaHover(int hiiriX, int hiiriY) {
        valitunEsineenTekstuuriNappi.hiiriSisällä(hiiriX, hiiriY);
        käännäObjektiaNappi.hiiriSisällä(hiiriX, hiiriY);
        peilaaObjektiXNappi.hiiriSisällä(hiiriX, hiiriY);
        peilaaObjektiYNappi.hiiriSisällä(hiiriX, hiiriY);
    }

    public static void tarkistaPainetutNapit(int hiiriX, int hiiriY) {
        if (valitunEsineenTekstuuriNappi.hiiriSisällä(hiiriX, hiiriY)) {
            EditoriRuutu.avaaObjektiValikko(true);
            valitunEsineenTekstuuriNappi.poistaValinta();
        }
        else if (käännäObjektiaNappi.hiiriSisällä(hiiriX, hiiriY)) {
            EditoriRuutu.kääntöAsteet += 90;
            EditoriRuutu.kääntöAsteet %= 360;
        }
        else if (peilaaObjektiXNappi.hiiriSisällä(hiiriX, hiiriY)) {
            EditoriRuutu.peilausX = !EditoriRuutu.peilausX;
        }
        else if (peilaaObjektiYNappi.hiiriSisällä(hiiriX, hiiriY)) {
            EditoriRuutu.peilausY = !EditoriRuutu.peilausY;
        }
    }

    public static void asetaValittuObjekti(PeliObjekti objekti) {
        if (objekti != null) {
            if (objekti instanceof KenttäKohde || objekti instanceof Entity) {
                valitunEsineenTekstuuri = objekti.annaTekstuuri();
            }
            else if (objekti instanceof Maasto) {
                valitunEsineenTekstuuri = Assets.annaTileTekstuurit().get(((Maasto)objekti).annaTekstuurinNimi());
            }
        }
    }

    public static void renderöi(Shader shader, Ikkuna ikkuna) {
        renderöiObjektiValikko(shader, ikkuna);
        renderöiTooltipTekstit(shader, ikkuna);
    }

    private static void renderöiObjektiValikko(Shader shader, Ikkuna ikkuna) {
        otsikko.renderöi(shader, ikkuna);

        if (valitunEsineenTekstuuri != null) {
            valitunEsineenTekstuuriNappi.päivitäSisältö(valitunEsineenTekstuuri);
            if (EditoriRuutu.valittuEsine instanceof Suunnallinen) {
                Suunnallinen suunnallinenObjekti = (Suunnallinen)EditoriRuutu.valittuEsine;
                Suunta s = suunnallinenObjekti.annaSuunta();
                int asteet = Suunnallinen.haeAsteet(s);
                valitunEsineenTekstuuriNappi.renderöiTekstuuriKääntö(shader, ikkuna, asteet, false, false);
            }
            else {
                valitunEsineenTekstuuriNappi.renderöiTekstuuriKääntö(shader, ikkuna, EditoriRuutu.kääntöAsteet, EditoriRuutu.peilausX, EditoriRuutu.peilausY);
            }
            if (EditoriRuutu.kopioitu) kopioituTekstuuriLabel.renderöi(shader, ikkuna);
        }

        if (EditoriRuutu.valittuEsine instanceof Maasto) {
            float scaleX = 0.125f, scaleY = 0.0125f;
            float offsetX = -0.15f, offsetY = 1f - 0.180f;
            valitunEsineenNimiTeksti.bind(0);
            if (EditoriRuutu.valittuEsine != null) {
                valitunEsineenNimiTeksti.päivitäTeksti(EditoriRuutu.valittuEsine.annaNimi(), 1, 4);
                if (EditoriRuutu.valittuEsine instanceof Maasto) {
                    valitunMaastonKuvaTeksti.päivitäTeksti(((Maasto)EditoriRuutu.valittuEsine).annaKuvanTiedostoNimi(), 1, 10);
                }
            }
            renderöiKomponentti(shader, valitunEsineenNimiTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);
            
            scaleX = 0.125f; scaleY = 0.0125f;
            offsetX = -0.15f; offsetY = 1f - 0.220f;
            valitunMaastonKuvaTeksti.bind(0);
            renderöiKomponentti(shader, valitunMaastonKuvaTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);
        }
        else if (EditoriRuutu.valittuEsine instanceof KenttäKohde) {
            float scaleX = 0.125f, scaleY = 0.025f;
            float offsetX = -0.15f, offsetY = 1f - 0.190f;
            if (EditoriRuutu.valittuEsine != null) {
                valitunEsineenNimiTeksti.päivitäTeksti(EditoriRuutu.valittuEsine.annaNimi(), 1, 4);
                valitunEsineenNimiTeksti.bind(0);
            }
            renderöiKomponentti(shader, valitunEsineenNimiTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);
        }
        else {
            float scaleX = 0.125f, scaleY = 0.025f;
            float offsetX = -0.15f, offsetY = 1f - 0.190f;
            if (EditoriRuutu.valittuEsine != null) {
                valitunEsineenNimiTeksti.päivitäTeksti(EditoriRuutu.valittuEsine.annaNimi(), 1, 4);
                valitunEsineenNimiTeksti.bind(0);
            }
            renderöiKomponentti(shader, valitunEsineenNimiTeksti, ikkuna, scaleX, scaleY, 1, offsetX, offsetY, 0);
        }

        objektinKääntöAsteetTeksti.päivitäTeksti("" + EditoriRuutu.kääntöAsteet);
        käännäObjektiaNappi.renderöi(shader, ikkuna);
        objektinKääntöAsteetLabel.renderöi(shader, ikkuna);
        peilaaObjektiXNappi.renderöi(shader, ikkuna);
        if (EditoriRuutu.peilausX) objektinPeilausXLabel.renderöi(shader, ikkuna);
        peilaaObjektiYNappi.renderöi(shader, ikkuna);
        if (EditoriRuutu.peilausY) objektinPeilausYLabel.renderöi(shader, ikkuna);
    }

    private static void renderöiTooltipTekstit(Shader shader, Ikkuna ikkuna) {
        valitunEsineenTekstuuriNappi.renderöiTooltip(shader, ikkuna);
        käännäObjektiaNappi.renderöiTooltip(shader, ikkuna);
        peilaaObjektiXNappi.renderöiTooltip(shader, ikkuna);
        peilaaObjektiYNappi.renderöiTooltip(shader, ikkuna);
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
