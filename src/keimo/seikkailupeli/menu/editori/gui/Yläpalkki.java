package keimo.seikkailupeli.menu.editori.gui;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.ValintaLaatikko;
import keimo.keimoengine.ikkuna.Window;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.TarinaDialogiLista;
import keimo.seikkailupeli.kenttä.Huone;
import keimo.seikkailupeli.menu.editori.EditoriRuutu;
import keimo.seikkailupeli.objektit.PeliObjekti;
import keimo.seikkailupeli.objektit.entityt.Entity;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.objektit.maastot.Maasto;
import keimo.seikkailupeli.toiminnot.Dialogit;
import keimo.utility.KSTLoader;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class Yläpalkki {

    private static Tekstuuri yläpalkkiTekstuuri = new Tekstuuri("tiedostot/kuvat/editori/popup_valinta_pohja.png");
    private static Teksti yläpalkkiTekstiHuone = new Teksti("Huone", Color.white, 350, 48);
    private static Teksti yläpalkkiTekstiKenttä = new Teksti("Kenttä", Color.white, 300, 48);
    private static Teksti yläpalkkiTekstiOminaisuudet = new Teksti("Näytä", Color.white, 300, 48);
    private static Teksti yläpalkkiTekstiTyökalut = new Teksti("Tiedosto", Color.white, 300, 48);

    private static Nappi vaihdaHuonettaNappiVasen = new Nappi(-0.05f, 0.05f, -0.675f, 0.9f, new Tekstuuri("tiedostot/kuvat/editori/huone_vaihda_nuoli.png"), new TooltipTeksti("Edellinen huone"));
    private static Nappi vaihdaHuonettaNappiOikea = new Nappi(0.05f, 0.05f, -0.575f, 0.9f, new Tekstuuri("tiedostot/kuvat/editori/huone_vaihda_nuoli.png"), new TooltipTeksti("Seuraava huone"));
    private static Nappi uusiHuoneNappi = new Nappi(0.05f, 0.05f, -0.675f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/huone_uusi.png"), new TooltipTeksti("Luo uusi huone"));
    private static Nappi poistaHuoneNappi = new Nappi(0.05f, 0.05f, -0.575f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/huone_poista.png"), new TooltipTeksti("Poista huone"));
    private static Nappi muokkaaHuoneenTietojaNappi = new Nappi(0.1f, 0.08f, -0.85f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/popup_valinta_pohja.png"), new TooltipTeksti("Huoneen asetukset"));
    private static Teksti huoneenIdTeksti = new Teksti("ID", Color.yellow, 1000, 48);
    private static Teksti huoneenNimiTeksti = new Teksti("Huoneen nimi", Color.yellow, 1500, 48);
    private static Teksti huoneenAlueTeksti = new Teksti("Alue", Color.yellow, 1200, 48);
    private static Teksti huoneenKokoTeksti = new Teksti("Koko", Color.yellow, 1000, 48);

    private static Nappi valittuObjektiNappi = new Nappi(0.1f, 0.05f, -0.375f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/valittu_objekti.png"), new TooltipTeksti("Valitse objekti"));
    private static Teksti valitunEsineenNimiTeksti = new Teksti("Valitse esine", Color.magenta, 1200, 36);
    private static Teksti valitunMaastonKuvaTeksti = new Teksti("", Color.blue, 1200, 36);
    private static Renderöitävä valitunEsineenTekstuuri;
    private static Nappi valitunEsineenTekstuuriNappi = new Nappi(0.025f, 0.05f, -0.240f, 0.8f, valitunEsineenTekstuuri, new TooltipTeksti("Muokkaa objektia"));
    private static Tekstuuri kopioituTekstuuri = new Tekstuuri("tiedostot/kuvat/editori/kopioi_ominaisuudet.png");
    private static StaattinenKomponentti kopioituTekstuuriLabel = new StaattinenKomponentti(0.0125f, 0.025f, -0.220f, 0.775f, kopioituTekstuuri);
    private static Nappi käännäObjektiaNappi = new Nappi(0.025f, 0.05f, -0.175f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/objekti_kääntö.png"), new TooltipTeksti("Käännä objektia"));
    private static Nappi peilaaObjektiXNappi = new Nappi(0.025f, 0.05f, -0.125f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/objekti_peilaus_x.png"), new TooltipTeksti("Peilaa vaakasuunnassa", 750, 48));
    private static Nappi peilaaObjektiYNappi = new Nappi(0.025f, 0.05f, -0.075f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/objekti_peilaus_y.png"), new TooltipTeksti("Peilaa pystysuunnassa", 750, 48));
    private static Teksti objektinKääntöAsteetTeksti = new Teksti("" + EditoriRuutu.kääntöAsteet, Color.orange, 100, 36);
    private static StaattinenKomponentti objektinKääntöAsteetLabel = new StaattinenKomponentti(0.025f, 0.05f, -0.175f, 0.8f, objektinKääntöAsteetTeksti);
    private static Tekstuuri objektinPeilausValittuKuvake = new Tekstuuri("tiedostot/kuvat/editori/valittu.png");
    private static StaattinenKomponentti objektinPeilausXLabel = new StaattinenKomponentti(0.025f, 0.05f, -0.125f, 0.8f, objektinPeilausValittuKuvake);
    private static StaattinenKomponentti objektinPeilausYLabel = new StaattinenKomponentti(0.025f, 0.05f, -0.075f, 0.8f, objektinPeilausValittuKuvake);

    private static ValintaLaatikko näytäTiletNappi = new ValintaLaatikko(true, 0.025f, 0.03f, 0.05f, 0.86f);
    private static ValintaLaatikko näytäObjektitNappi = new ValintaLaatikko(true, 0.025f, 0.03f, 0.05f, 0.8f);
    private static ValintaLaatikko näytäEntitytNappi = new ValintaLaatikko(true, 0.025f, 0.03f, 0.05f, 0.74f);
    private static Teksti näytäTiletTeksti = new Teksti("1: Näytä tilet", Color.yellow, 1000, 48);
    private static Teksti näytäObjektitTeksti = new Teksti("2: Näytä objektit", Color.yellow, 1000, 48);
    private static Teksti näytäEntitytTeksti = new Teksti("3: Näytä entityt", Color.yellow, 1000, 48);
    private static StaattinenKomponentti näytäOsoitinLabel = new StaattinenKomponentti(0.025f, 0.03f, 0.35f, 0.86f, new Tekstuuri("tiedostot/kuvat/editori/osoitin.png"));
    private static ValintaLaatikko näytäOsoitinNappi = new ValintaLaatikko(true, 0.025f, 0.03f, 0.4f, 0.86f, new TooltipTeksti("Näytä osoittimen sijainti", 1000, 48));
    
    private static Nappi uusiTiedostoNappi = new Nappi(0.05f, 0.05f, 0.6f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/tiedosto_uusi.png"), new TooltipTeksti("Uusi kenttä"));
    private static Nappi avaaTiedostoNappi = new Nappi(0.05f, 0.05f, 0.7f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/tiedosto_avaa.png"), new TooltipTeksti("Avaa tiedosto"));
    private static Nappi tallennaTiedostoNappi = new Nappi(0.05f, 0.05f, 0.8f, 0.8f, new Tekstuuri("tiedostot/kuvat/editori/tiedosto_tallenna.png"), new TooltipTeksti("Tallenna tiedosto"));

    public static void tarkistaYläpalkkiHover(int hiiriX, int hiiriY) {
        vaihdaHuonettaNappiVasen.hiiriSisällä(hiiriX, hiiriY);
        vaihdaHuonettaNappiOikea.hiiriSisällä(hiiriX, hiiriY);
        uusiHuoneNappi.hiiriSisällä(hiiriX, hiiriY);
        poistaHuoneNappi.hiiriSisällä(hiiriX, hiiriY);
        muokkaaHuoneenTietojaNappi.hiiriSisällä(hiiriX, hiiriY);
        valittuObjektiNappi.hiiriSisällä(hiiriX, hiiriY);
        valitunEsineenTekstuuriNappi.hiiriSisällä(hiiriX, hiiriY);
        käännäObjektiaNappi.hiiriSisällä(hiiriX, hiiriY);
        peilaaObjektiXNappi.hiiriSisällä(hiiriX, hiiriY);
        peilaaObjektiYNappi.hiiriSisällä(hiiriX, hiiriY);
        näytäTiletNappi.hiiriSisällä(hiiriX, hiiriY);
        näytäObjektitNappi.hiiriSisällä(hiiriX, hiiriY);
        näytäEntitytNappi.hiiriSisällä(hiiriX, hiiriY);
        näytäOsoitinNappi.hiiriSisällä(hiiriX, hiiriY);
        uusiTiedostoNappi.hiiriSisällä(hiiriX, hiiriY);
        avaaTiedostoNappi.hiiriSisällä(hiiriX, hiiriY);
        tallennaTiedostoNappi.hiiriSisällä(hiiriX, hiiriY);
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
        else if (valittuObjektiNappi.hiiriSisällä(hiiriX, hiiriY)) {
            EditoriRuutu.avaaObjektiValikko(true);
            valittuObjektiNappi.poistaValinta();
        }
        else if (valitunEsineenTekstuuriNappi.hiiriSisällä(hiiriX, hiiriY)) {
            EditoriRuutu.avaaMuokkausIkkuna(true);
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
        else if (näytäTiletNappi.hiiriSisällä(hiiriX, hiiriY)) {
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
        else if (uusiTiedostoNappi.hiiriSisällä(hiiriX, hiiriY)) {
            // Tähän joku varmistus. Pitää suunnitella dialogiboksi tätä varten.
            if (tinyfd_messageBox("Uusi kenttä", "Haluatko luoda uuden kentän? Kaikki huoneet poistetaan.", "yesno", "question", true)) {
                EditoriRuutu.editorinHuoneKartta.clear();
                EditoriRuutu.editorinHuoneKartta.put(0, new Huone(0, 10, "uusi huone", "", "uusi alue", null, null, null, "", "", ""));
                EditoriRuutu.lataaHuone(0);
            }
        }
        else if (avaaTiedostoNappi.hiiriSisällä(hiiriX, hiiriY)) {
            try {
                PointerBuffer filters = PointerBuffer.allocateDirect(1);
                filters.put(40);
                String tiedostoPolku = tinyfd_openFileDialog("Avaa tiedosto", "tiedostot/pelitiedostot/", filters, "Keimon seikkailupelin tiedostot (.kst)", false);
                if (tiedostoPolku != null && tiedostoPolku != "") {
                    KSTLoader.lataaAsetuksetKST(tiedostoPolku);
                    EditoriRuutu.editorinHuoneKartta = KSTLoader.lataaKentätKST(tiedostoPolku);
                    TarinaDialogiLista.tarinaKartta = KSTLoader.lataaTarinatKST(tiedostoPolku);
                    Dialogit.PitkätDialogit.vuoropuheDialogiKartta = KSTLoader.lataaDialogitKST(tiedostoPolku);
                    EditoriRuutu.lataaHuone(0);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                tinyfd_messageBox("Virhe", "Tiedoston avaaminen epäonnistui", "ok", "error", false);
            }
        }
        else if (tallennaTiedostoNappi.hiiriSisällä(hiiriX, hiiriY)) {
            try {
                PointerBuffer filters = PointerBuffer.allocateDirect(1);
                filters.put(1);
                File tiedosto = new File(tinyfd_saveFileDialog("Tallenna tiedosto", "tiedostot/pelitiedostot/", filters, "Keimon seikkailupelin tiedostot (.kst)"));
                if (tiedosto != null) {
                    String kokoTiedostoString = KSTLoader.luoMerkkijonotHuonekartasta(EditoriRuutu.editorinHuoneKartta, TarinaDialogiLista.tarinaKartta, Dialogit.PitkätDialogit.vuoropuheDialogiKartta);
                    FileWriter writer = new FileWriter(tiedosto);
                    writer.write(kokoTiedostoString);
                    writer.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                tinyfd_messageBox("Virhe", "Tiedoston tallentaminen epäonnistui", "ok", "error", false);
            }
        }
    }

    public static void asetaValittuObjekti(PeliObjekti objekti) {
        if (objekti != null) {
            valitunEsineenNimiTeksti.päivitäTeksti(objekti.annaNimi());
            if (objekti instanceof KenttäKohde || objekti instanceof Entity) {
                valitunEsineenTekstuuri = objekti.annaTekstuuri();
            }
            else if (objekti instanceof Maasto) {
                valitunMaastonKuvaTeksti.päivitäTeksti(((Maasto)objekti).annaKuvanTiedostoNimi());
                valitunEsineenTekstuuri = EditoriRuutu.tileTextures.get(((Maasto)objekti).annaTekstuurinNimi());
            }
        }
    }

    public static void renderöi(Shader shader, Window window) {
        float scaleY = 0.15f;
        Matrix4f yläpalkinSijainti = new Matrix4f();
        yläpalkinSijainti.translate(0, 1f - scaleY, 0);
        yläpalkinSijainti.scale(1, scaleY, 1);

        shader.bind();
		shader.setUniform("projection", yläpalkinSijainti);
        shader.setUniform("sampler", 0);
        shader.setUniform("subcolor", new Vector4f(1, 1, 1, 0.25f));
        yläpalkkiTekstuuri.bind(0);
        Assets.getModel().render();

        float scaleX = 0.25f;
        shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        for (int i = 0; i < 4; i++) {
            float offsetX = (-1.5f+i) * 2 * scaleX;
            renderöiKomponentti(shader, yläpalkkiTekstuuri, window, scaleX, scaleY, 1, offsetX, 1f - scaleY, 0);
        }

        scaleX = 0.25f;
        float tekstinKeskitysY = scaleY/2f;
        scaleY = 0.05f;
        for (int i = 0; i < 4; i++) {
            float offsetX = (-1.5f+i) * 2 * scaleX + 0.025f;
            Teksti teksti = valitseTeksti(i);
            teksti.bind(0);
            renderöiKomponentti(shader, teksti, window, scaleX, scaleY, 1, offsetX, 1f - tekstinKeskitysY, 0);
        }

        renderöiHuoneenTiedot(shader, window);
        renderöiObjektiValikko(shader, window);
        renderöiNäytettävienOsienTiedot(shader, window);
        renderöiTyökaluValikko(shader, window);
        renderöiPopupTekstit(shader, window);
    }

    private static void renderöiHuoneenTiedot(Shader shader, Window window) {
        muokkaaHuoneenTietojaNappi.renderöi(shader, window);

        float scaleX = 0.2f, scaleY = 0.025f;
        float offsetX = -1f + scaleX + 0.05f, offsetY = 1f - 0.14f;
        huoneenIdTeksti.päivitäTeksti("ID: " + EditoriRuutu.ladatunHuoneenId);
        huoneenIdTeksti.bind(0);
        renderöiKomponentti(shader, huoneenIdTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);

        offsetY = 1f - 0.18f;
        huoneenNimiTeksti.päivitäTeksti("Nimi: " + EditoriRuutu.ladattuHuone.annaNimi());
        huoneenNimiTeksti.bind(0);
        renderöiKomponentti(shader, huoneenNimiTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);

        offsetY = 1f - 0.22f;
        huoneenAlueTeksti.päivitäTeksti("Alue: " + EditoriRuutu.ladattuHuone.annaAlue());
        huoneenAlueTeksti.bind(0);
        renderöiKomponentti(shader, huoneenAlueTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);

        offsetY = 1f - 0.26f;
        huoneenKokoTeksti.päivitäTeksti("Koko: " + EditoriRuutu.ladattuHuone.annaKoko());
        huoneenKokoTeksti.bind(0);
        renderöiKomponentti(shader, huoneenKokoTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);

        vaihdaHuonettaNappiVasen.renderöi(shader, window);
        vaihdaHuonettaNappiOikea.renderöi(shader, window);
        uusiHuoneNappi.renderöi(shader, window);
        poistaHuoneNappi.renderöi(shader, window);
    }

    private static void renderöiObjektiValikko(Shader shader, Window window) {
        valittuObjektiNappi.renderöi(shader, window);

        if (valitunEsineenTekstuuri != null) {
            valitunEsineenTekstuuriNappi.päivitäSisältö(valitunEsineenTekstuuri);
            valitunEsineenTekstuuriNappi.renderöiRotaatio(shader, window, EditoriRuutu.kääntöAsteet, EditoriRuutu.peilausX, EditoriRuutu.peilausY);
            //valitunEsineenTekstuuriNappi.renderöi(shader, window);
            if (EditoriRuutu.kopioitu) kopioituTekstuuriLabel.renderöi(shader, window);
        }

        if (EditoriRuutu.valittuEsine instanceof Maasto) {
            float scaleX = 0.2f, scaleY = 0.0125f;
            float offsetX = -0.25f, offsetY = 1f - 0.180f;
            valitunEsineenNimiTeksti.bind(0);
            renderöiKomponentti(shader, valitunEsineenNimiTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
            
            scaleX = 0.2f; scaleY = 0.0125f;
            offsetX = -0.25f; offsetY = 1f - 0.220f;
            valitunMaastonKuvaTeksti.bind(0);
            renderöiKomponentti(shader, valitunMaastonKuvaTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
        }
        else if (EditoriRuutu.valittuEsine instanceof KenttäKohde) {
            float scaleX = 0.2f, scaleY = 0.025f;
            float offsetX = -0.25f, offsetY = 1f - 0.190f;
            valitunEsineenNimiTeksti.bind(0);
            renderöiKomponentti(shader, valitunEsineenNimiTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
        }
        else {
            float scaleX = 0.2f, scaleY = 0.025f;
            float offsetX = -0.25f, offsetY = 1f - 0.190f;
            valitunEsineenNimiTeksti.bind(0);
            renderöiKomponentti(shader, valitunEsineenNimiTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
        }

        objektinKääntöAsteetTeksti.päivitäTeksti("" + EditoriRuutu.kääntöAsteet);
        käännäObjektiaNappi.renderöi(shader, window);
        objektinKääntöAsteetLabel.renderöi(shader, window);
        peilaaObjektiXNappi.renderöi(shader, window);
        if (EditoriRuutu.peilausX) objektinPeilausXLabel.renderöi(shader, window);
        peilaaObjektiYNappi.renderöi(shader, window);
        if (EditoriRuutu.peilausY) objektinPeilausYLabel.renderöi(shader, window);
    }

    private static void renderöiNäytettävienOsienTiedot(Shader shader, Window window) {
        näytäTiletNappi.renderöi(shader, window);
        float scaleX = 0.2f, scaleY = 0.025f;
        float offsetX = scaleX + 0.1f, offsetY = 0.86f;
        String teksti = "Maasto: " + (EditoriRuutu.maastoNäkyvissä ? "kyllä" : "ei");
        Color väri = EditoriRuutu.maastoNäkyvissä ? Color.green : Color.red;
        näytäTiletTeksti.päivitäTeksti(teksti, 0, 50, väri);
        näytäTiletTeksti.bind(0);
        renderöiKomponentti(shader, näytäTiletTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);

        näytäObjektitNappi.renderöi(shader, window);
        offsetY = 0.8f;
        teksti = "Objektit: " + (EditoriRuutu.kenttäNäkyvissä ? "kyllä" : "ei");
        väri = EditoriRuutu.kenttäNäkyvissä ? Color.green : Color.red;
        näytäObjektitTeksti.päivitäTeksti(teksti, 0, 50, väri);
        näytäObjektitTeksti.bind(0);
        renderöiKomponentti(shader, näytäObjektitTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);

        näytäEntitytNappi.renderöi(shader, window);
        offsetY = 0.74f;
        teksti = "Entityt: " + (EditoriRuutu.entitytNäkyvissä ? "kyllä" : "ei");
        väri = EditoriRuutu.entitytNäkyvissä ? Color.green : Color.red;
        näytäEntitytTeksti.päivitäTeksti(teksti, 0, 50, väri);
        näytäEntitytTeksti.bind(0);
        renderöiKomponentti(shader, näytäEntitytTeksti, window, scaleX, scaleY, 1, offsetX, offsetY, 0);

        näytäOsoitinLabel.renderöi(shader, window);
        näytäOsoitinNappi.renderöi(shader, window);
    }

    private static void renderöiTyökaluValikko(Shader shader, Window window) {
        uusiTiedostoNappi.renderöi(shader, window);
        avaaTiedostoNappi.renderöi(shader, window);
        tallennaTiedostoNappi.renderöi(shader, window);
    }

    private static void renderöiPopupTekstit(Shader shader, Window window) {
        vaihdaHuonettaNappiVasen.renderöiTooltip(shader, window);
        vaihdaHuonettaNappiOikea.renderöiTooltip(shader, window);
        uusiHuoneNappi.renderöiTooltip(shader, window);
        poistaHuoneNappi.renderöiTooltip(shader, window);
        muokkaaHuoneenTietojaNappi.renderöiTooltip(shader, window);
        valittuObjektiNappi.renderöiTooltip(shader, window);
        valitunEsineenTekstuuriNappi.renderöiTooltip(shader, window);
        käännäObjektiaNappi.renderöiTooltip(shader, window);
        peilaaObjektiXNappi.renderöiTooltip(shader, window);
        peilaaObjektiYNappi.renderöiTooltip(shader, window);
        näytäOsoitinNappi.renderöiTooltip(shader, window);
        uusiTiedostoNappi.renderöiTooltip(shader, window);
        avaaTiedostoNappi.renderöiTooltip(shader, window);
        tallennaTiedostoNappi.renderöiTooltip(shader, window);
    }

    private static Teksti valitseTeksti(int valinta) {
        switch(valinta) {
            default: return yläpalkkiTekstiHuone;
            case 1: return yläpalkkiTekstiKenttä;
            case 2: return yläpalkkiTekstiOminaisuudet;
            case 3: return yläpalkkiTekstiTyökalut;
        }
    }

    private static void renderöiKomponentti(Shader shader, Renderöitävä tekstuuri, Window window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ) {
        Matrix4f sijaintiMatriisi = new Matrix4f();
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        shader.setUniform("projection", sijaintiMatriisi);
        shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        tekstuuri.bind(0);
        Assets.getModel().render();
    }
}
