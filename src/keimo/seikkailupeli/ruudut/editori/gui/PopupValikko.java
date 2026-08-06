package keimo.seikkailupeli.ruudut.editori.gui;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.PeliObjekti;
import keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC.NPC_KenttäKohde;
import keimo.seikkailupeli.objektit.kenttäkohteet.warp.Warp;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu;
import keimo.seikkailupeli.ruudut.editori.muokkausikkunat.WarpMuokkausIkkuna;

import java.util.ArrayList;

public class PopupValikko {

    public static int popupValittuKohde = 0;
    public static int popupKameraX = 0;
    public static int popupKameraY = 0;
    public static int popupHiiriX = 0;
    public static int popupHiiriY = 0;
    private static Ikkuna window1;

    private static Renderöitävä popupValikkoTekstuuri = Assets.annaTekstuuri("editori_popup_pohja");
    private static LabelKomponentti popupPohjaLabel = new LabelKomponentti(0.2f, 0.3f, 0.5f, 0, popupValikkoTekstuuri);
    private static ArrayList<Nappi> popupNapit = new ArrayList<>();
    private static boolean luoValikko = false;
    private static PeliObjekti obj;
    private static int hiirenSijX = 0;
    private static int hiirenSijY = 0;

    public static void avaaPopup() {
        luoValikko = true;
    }

    private static void luoPopupValikko() {
        popupNapit.clear();
        obj = EditoriRuutu.ladattuHuone.annaHuoneenKenttäSisältö()[EditoriRuutu.tileX][EditoriRuutu.tileY];
        if (obj instanceof Warp) {
            popupNapit.add(new Nappi(0.2f, 0.1f, 0, 0, new Teksti("Tiedot", Väri.white, 300, 48)));
            popupNapit.add(new Nappi(0.2f, 0.1f, 0, 0, new Teksti("Muokkaa", Väri.white, 300, 48)));
            popupNapit.add(new Nappi(0.2f, 0.1f, 0, 0, new Teksti("Poista", Väri.white, 300, 48)));
        }
        else if (obj instanceof NPC_KenttäKohde) {
            popupNapit.add(new Nappi(0.2f, 0.1f, 0, 0, new Teksti("Tiedot", Väri.white, 300, 48)));
            popupNapit.add(new Nappi(0.2f, 0.1f, 0, 0, new Teksti("Muokkaa", Väri.white, 300, 48)));
            popupNapit.add(new Nappi(0.2f, 0.1f, 0, 0, new Teksti("Poista", Väri.white, 300, 48)));
        }
        else {
            popupNapit.add(new Nappi(0.2f, 0.1f, 0, 0, new Teksti("Tiedot", Väri.white, 300, 48)));
            popupNapit.add(new Nappi(0.2f, 0.1f, 0, 0, new Teksti("Poista", Väri.white, 300, 48)));
        }
        luoValikko = false;
    }

    public static void tarkistaPopupHover(int hiiriX, int hiiriY) {
        for (Nappi n : popupNapit) {
            n.hiiriSisällä(hiiriX, hiiriY);
        }
    }

    public static void tarkistaPainetutNapit(int hiiriX, int hiiriY) {
        EditoriRuutu.avaaPopup(false);
        for (Nappi n : popupNapit) {
            if (n.hiiriSisällä(hiiriX, hiiriY)) {
                Renderöitävä sisältö = n.annaSisältö();
                if (sisältö instanceof Teksti) {
                    String valinta = ((Teksti)sisältö).annaTeksti();
                    switch (valinta) {
                        case "Tiedot" -> {
                            EditoriRuutu.avaaTietoIkkuna(true);
                        }
                        case "Muokkaa" -> {
                            if (obj instanceof Warp) {
                                WarpMuokkausIkkuna.luoIkkuna((Warp)obj);
                            }
                        }
                        case "Poista" -> {
                            EditoriRuutu.poistaObjekti();
                            EditoriRuutu.estäVahinkoPainallukset = true;
                        }
                    }
                }
            }
        }
    }

    public static void päivitäHiirenSijainti(int hiiriX, int hiiriY) {
        hiirenSijX = hiiriX;
        hiirenSijY = hiiriY;
    }

    private static void päivitäSijainti(Ikkuna window) {
        if (window1 != null) {
            if (window1.getWidth() > 0 && window1.getHeight() > 0) {
                float offsetX = (hiirenSijX - (window1.getWidth()/2f))/(window1.getWidth()/2f) + 0.2f;
                float offsetY = -(hiirenSijY - (window1.getHeight()/2f))/(window1.getHeight()/2f);
                if (offsetX > 0.8) offsetX = 0.8f;
                if (offsetY > 0.7) offsetY = 0.7f;
                popupPohjaLabel.muutaKokoa(0.2f, 0.1f * popupNapit.size(), offsetX, offsetY - 0.1f * popupNapit.size());
                for (int i = 0; i < popupNapit.size(); i++) {
                    Nappi n = popupNapit.get(i);
                    n.muutaKokoa(0.2f, 0.1f, offsetX, offsetY - i * 0.2f - 0.1f);
                }
            }
        }
    }
    
    public static void renderöi(Shader shader, Ikkuna window) {
        window1 = window;
        päivitäSijainti(window);
        if (luoValikko) luoPopupValikko();
        else {
            popupPohjaLabel.renderöi(shader, window);
            for (Nappi n : popupNapit) {
                n.renderöi(shader, window);
            }
        }
    }
}
