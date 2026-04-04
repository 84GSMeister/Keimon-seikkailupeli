package keimo.seikkailupeli.menu.editori.gui.yläpalkki;

import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.huone.Huone;
import keimo.seikkailupeli.assets.tarina.TarinaDialogiLista;
import keimo.seikkailupeli.menu.editori.EditoriRuutu;
import keimo.seikkailupeli.toiminnot.Dialogit;
import keimo.utility.KSTLoader;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;

import org.lwjgl.PointerBuffer;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class YläpalkkiTiedosto {

    private static Teksti otsikkoTeksti;
    private static StaattinenKomponentti otsikko = new StaattinenKomponentti(0.5f, 0.05f, 0.25f, 0.95f, otsikkoTeksti);
    private static Nappi uusiTiedostoNappi = new Nappi(0.05f, 0.05f, -0.3f, 0.8f, Assets.annaTekstuuri("editori_tiedosto_uusi"), new TooltipTeksti("Uusi kenttä"));
    private static Nappi avaaTiedostoNappi = new Nappi(0.05f, 0.05f, -0.2f, 0.8f, Assets.annaTekstuuri("editori_tiedosto_avaa"), new TooltipTeksti("Avaa tiedosto"));
    private static Nappi tallennaTiedostoNappi = new Nappi(0.05f, 0.05f, -0.1f, 0.8f, Assets.annaTekstuuri("editori_tiedosto_tallenna"), new TooltipTeksti("Tallenna tiedosto"));
    
    protected static void alustaGrafiikat() {
        otsikkoTeksti = new Teksti("Tiedosto", Color.white, 400, 48);
        otsikko.päivitäSisältö(otsikkoTeksti);
    }

    public static void tarkistaHover(int hiiriX, int hiiriY) {
        uusiTiedostoNappi.hiiriSisällä(hiiriX, hiiriY);
        avaaTiedostoNappi.hiiriSisällä(hiiriX, hiiriY);
        tallennaTiedostoNappi.hiiriSisällä(hiiriX, hiiriY);
    }

    public static void tarkistaPainetutNapit(int hiiriX, int hiiriY) {
        if (uusiTiedostoNappi.hiiriSisällä(hiiriX, hiiriY)) {
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

    public static void renderöi(Shader shader, Ikkuna ikkuna) {
        renderöiTyökaluValikko(shader, ikkuna);
        renderöiTooltipTekstit(shader, ikkuna);
    }

    private static void renderöiTyökaluValikko(Shader shader, Ikkuna ikkuna) {
        otsikko.renderöi(shader, ikkuna);
        uusiTiedostoNappi.renderöi(shader, ikkuna);
        avaaTiedostoNappi.renderöi(shader, ikkuna);
        tallennaTiedostoNappi.renderöi(shader, ikkuna);
    }

    private static void renderöiTooltipTekstit(Shader shader, Ikkuna ikkuna) {
        uusiTiedostoNappi.renderöiTooltip(shader, ikkuna);
        avaaTiedostoNappi.renderöiTooltip(shader, ikkuna);
        tallennaTiedostoNappi.renderöiTooltip(shader, ikkuna);
    }
}
