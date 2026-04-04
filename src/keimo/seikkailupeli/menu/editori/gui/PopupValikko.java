package keimo.seikkailupeli.menu.editori.gui;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.menu.editori.EditoriRuutu;
import keimo.seikkailupeli.menu.editori.muokkausikkunat.WarpMuokkausIkkuna;
import keimo.seikkailupeli.objektit.PeliObjekti;
import keimo.seikkailupeli.objektit.kenttäkohteet.warp.Warp;

import java.awt.Color;

public class PopupValikko {

    public static int popupValittuKohde = 0;
    public static int popupKameraX = 0;
    public static int popupKameraY = 0;
    public static int popupHiiriX = 0;
    public static int popupHiiriY = 0;
    private static Ikkuna window1;

    private static Renderöitävä popupValikkoTekstuuri = Assets.annaTekstuuri("editori_popup_pohja");
    private static StaattinenKomponentti popupPohjaLabel = new StaattinenKomponentti(0.2f, 0.3f, 0.5f, 0, popupValikkoTekstuuri);
    private static Nappi popupTiedotNappi = new Nappi(0.2f, 0.1f, 0.5f, 0.2f, null);
    private static Nappi popupMuokkaaNappi = new Nappi(0.2f, 0.1f, 0.5f, 0, null);
    private static Nappi popupPoistaNappi = new Nappi(0.2f, 0.1f, 0.5f, -0.2f, null);

    public static void alustaGrafiikat() {
        popupTiedotNappi.päivitäSisältö(new Teksti("Tiedot", Color.white, 300, 48));
        popupMuokkaaNappi.päivitäSisältö(new Teksti("Muokkaa", Color.white, 300, 48));
        popupPoistaNappi.päivitäSisältö(new Teksti("Poista", Color.white, 300, 48));
    }

    public static void tarkistaPopupHover(int hiiriX, int hiiriY) {
        popupTiedotNappi.hiiriSisällä(hiiriX, hiiriY);
        popupMuokkaaNappi.hiiriSisällä(hiiriX, hiiriY);
        popupPoistaNappi.hiiriSisällä(hiiriX, hiiriY);
    }

    public static void tarkistaPainetutNapit(int hiiriX, int hiiriY) {
        EditoriRuutu.avaaPopup(false);
        if (popupTiedotNappi.hiiriSisällä(hiiriX, hiiriY)) {
            EditoriRuutu.avaaTietoIkkuna(true);
        }
        else if (popupMuokkaaNappi.hiiriSisällä(hiiriX, hiiriY)) {
            PeliObjekti obj = EditoriRuutu.ladattuHuone.annaHuoneenKenttäSisältö()[EditoriRuutu.tileX][EditoriRuutu.tileY];
            if (obj instanceof Warp) {
                WarpMuokkausIkkuna.luoIkkuna((Warp)obj);
            }
            else {
                EditoriRuutu.avaaMuokkausIkkuna(true);
            }
        }
        else if (popupPoistaNappi.hiiriSisällä(hiiriX, hiiriY)) {
            EditoriRuutu.poistaObjekti();
            EditoriRuutu.estäVahinkoPainallukset = true;
        }
    }

    public static void päivitäSijainti(int hiiriX, int hiiriY) {
        if (window1 != null) {
            if (window1.getWidth() > 0 && window1.getHeight() > 0) {
                float offsetX = (hiiriX - (window1.getWidth()/2f))/(window1.getWidth()/2f) + 0.2f;
                float offsetY = -(hiiriY - (window1.getHeight()/2f))/(window1.getHeight()/2f) - 0.25f;
                if (offsetX > 0.8) offsetX = 0.8f;
                if (offsetY > 0.7) offsetY = 0.7f;
                popupPohjaLabel.muutaKokoa(0.2f, 0.3f, offsetX, offsetY);
                popupTiedotNappi.muutaKokoa(0.2f, 0.1f, offsetX, offsetY + 0.2f);
                popupMuokkaaNappi.muutaKokoa(0.2f, 0.1f, offsetX, offsetY);
                popupPoistaNappi.muutaKokoa(0.2f, 0.1f, offsetX, offsetY - 0.2f);
            }
        }
    }
    
    public static void renderöi(Shader shader, Ikkuna window) {
        window1 = window;
        popupPohjaLabel.renderöi(shader, window);
        popupTiedotNappi.renderöi(shader, window);
        popupMuokkaaNappi.renderöi(shader, window);
        popupPoistaNappi.renderöi(shader, window);
    }
}
