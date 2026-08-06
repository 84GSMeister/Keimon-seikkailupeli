package keimo.seikkailupeli.ruudut.editori.gui.yläpalkki;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.ikkuna.DialogiIkkunat;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.huone.Huone;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu.EditorinTilat;
import keimo.seikkailupeli.ruudut.editori.dialogieditori.DialogiEditoriRuutu;
import keimo.seikkailupeli.ruudut.editori.gui.LatausIkkuna;
import keimo.seikkailupeli.ruudut.editori.tarinaeditori.TarinaEditoriIkkuna;
import keimo.utility.KSTLoader;

import java.io.File;
import java.io.FileWriter;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

public class YläpalkkiTiedosto {

    private static Teksti otsikkoTeksti;
    private static LabelKomponentti otsikko = new LabelKomponentti(0.5f, 0.05f, 0.25f, 0.95f, otsikkoTeksti);
    private static Nappi uusiTiedostoNappi = new Nappi(0.05f, 0.05f, -0.3f, 0.8f, Assets.annaTekstuuri("editori_tiedosto_uusi"), new TooltipTeksti("Uusi kenttä"));
    private static Nappi avaaTiedostoNappi = new Nappi(0.05f, 0.05f, -0.2f, 0.8f, Assets.annaTekstuuri("editori_tiedosto_avaa"), new TooltipTeksti("Avaa tiedosto"));
    private static Nappi tallennaTiedostoNappi = new Nappi(0.05f, 0.05f, -0.1f, 0.8f, Assets.annaTekstuuri("editori_tiedosto_tallenna"), new TooltipTeksti("Tallenna tiedosto"));
    
    protected static void alustaGrafiikat() {
        otsikkoTeksti = new Teksti("Tiedosto", Väri.white, 400, 48);
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
            if (DialogiIkkunat.viestiIkkuna("Uusi kenttä", "Haluatko luoda uuden kentän? Kaikki huoneet poistetaan.", "yesno", "question", true)) {
                EditoriRuutu.editorinHuoneKartta.clear();
                EditoriRuutu.editorinHuoneKartta.put(0, new Huone(0, 10, "uusi huone", "", "uusi alue", null, null, null, "", "", ""));
                EditoriRuutu.lataaHuone(0);
            }
        }
        else if (avaaTiedostoNappi.hiiriSisällä(hiiriX, hiiriY)) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                PointerBuffer filters = stack.mallocPointer(1);
                filters.put(stack.UTF8("*.kst"));
                filters.flip();
                String tiedostoPolku = DialogiIkkunat.tiedostoSelainAvaa("Avaa tiedosto", "tiedostot/pelitiedostot/", filters, "Keimon seikkailupelin tiedostot (.kst)", false);
                if (tiedostoPolku != null && tiedostoPolku != "") {
                    EditoriRuutu.aktiivinenKomponentti = EditorinTilat.LATAUS;
                    LatausIkkuna.päivitäTila("Ladataan tiedostoa...");
                    KSTLoader.lataaAsetuksetKST(tiedostoPolku);
                    EditoriRuutu.editorinHuoneKartta = KSTLoader.lataaKentätKST(tiedostoPolku);
                    TarinaEditoriIkkuna.editorinTarinaKartta = KSTLoader.lataaTarinatKST(tiedostoPolku);
                    DialogiEditoriRuutu.editorinDialogiKartta = KSTLoader.lataaDialogitKST(tiedostoPolku);
                    EditoriRuutu.lataaHuone(0);
                    EditoriRuutu.aktiivinenKomponentti = EditorinTilat.MAAILMA;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                DialogiIkkunat.viestiIkkuna("Virhe", "Tiedoston avaaminen epäonnistui", "ok", "error", false);
                EditoriRuutu.aktiivinenKomponentti = EditorinTilat.MAAILMA;
            }
        }
        else if (tallennaTiedostoNappi.hiiriSisällä(hiiriX, hiiriY)) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                PointerBuffer filters = stack.mallocPointer(1);
                filters.put(stack.UTF8("*.kst"));
                filters.flip();
                String tiedostoPolku = DialogiIkkunat.tiedostoSelainTallenna("Tallenna tiedosto", "tiedostot/pelitiedostot/", filters, "Keimon seikkailupelin tiedostot (.kst)");
                if (tiedostoPolku != null) {
                    EditoriRuutu.aktiivinenKomponentti = EditorinTilat.LATAUS;
                    LatausIkkuna.päivitäTila("Tallennetaan tiedostoa...");
                    File tiedosto = new File(tiedostoPolku);
                    if (tiedosto != null) {
                        String kokoTiedostoString = KSTLoader.luoMerkkijonotHuonekartasta(EditoriRuutu.editorinHuoneKartta, TarinaEditoriIkkuna.editorinTarinaKartta, DialogiEditoriRuutu.editorinDialogiKartta);
                        FileWriter writer = new FileWriter(tiedosto);
                        writer.write(kokoTiedostoString);
                        writer.close();
                        EditoriRuutu.aktiivinenKomponentti = EditorinTilat.MAAILMA;
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                DialogiIkkunat.viestiIkkuna("Virhe", "Tiedoston tallentaminen epäonnistui", "ok", "error", false);
                EditoriRuutu.aktiivinenKomponentti = EditorinTilat.MAAILMA;
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
