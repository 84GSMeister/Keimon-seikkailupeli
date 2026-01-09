package keimo.seikkailupeli.menu.editori.gui.yläpalkki;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.objektit.PeliObjekti;

public class Yläpalkki {

    private static Tekstuuri pohjaTekstuuri = new Tekstuuri("tiedostot/kuvat/editori/popup_valinta_pohja.png");
    private static StaattinenKomponentti pohjaVasenLabel = new StaattinenKomponentti(0.25f, 0.15f, -0.75f, 0.85f, pohjaTekstuuri);
    private static StaattinenKomponentti pohjaOikeaLabel = new StaattinenKomponentti(0.75f, 0.15f, 0.25f, 0.85f, pohjaTekstuuri);

    private static Nappi välilehtiTiedostoNappi = new Nappi(0.12f, 0.04f, -0.875f, 0.95f, new Tekstuuri("tiedostot/kuvat/editori/välilehti_tiedosto.png"), new TooltipTeksti("Tiedosto"));
    private static Nappi välilehtiHuoneNappi = new Nappi(0.12f, 0.04f, -0.875f, 0.85f, new Tekstuuri("tiedostot/kuvat/editori/välilehti_huone.png"), new TooltipTeksti("Huone"));
    private static Nappi välilehtiKenttäNappi = new Nappi(0.12f, 0.04f, -0.875f, 0.75f, new Tekstuuri("tiedostot/kuvat/editori/välilehti_kenttä.png"), new TooltipTeksti("Kenttä"));
    private static Nappi välilehtiNäytäNappi = new Nappi(0.12f, 0.04f, -0.625f, 0.95f, new Tekstuuri("tiedostot/kuvat/editori/välilehti_näytä.png"), new TooltipTeksti("Näytä"));

    public static enum Välilehdet {
        TIEDOSTO,
        HUONE,
        KENTTÄ,
        NÄYTÄ;
    }
    public static Välilehdet nykyinenVälilehti = Välilehdet.HUONE;

    public static void tarkistaYläpalkkiHover(int hiiriX, int hiiriY) {
        välilehtiTiedostoNappi.hiiriSisällä(hiiriX, hiiriY);
        välilehtiHuoneNappi.hiiriSisällä(hiiriX, hiiriY);
        välilehtiKenttäNappi.hiiriSisällä(hiiriX, hiiriY);
        välilehtiNäytäNappi.hiiriSisällä(hiiriX, hiiriY);
        switch (nykyinenVälilehti) {
            case TIEDOSTO -> {
                YläpalkkiTiedosto.tarkistaHover(hiiriX, hiiriY);
            }
            case HUONE -> {
                YläpalkkiHuone.tarkistaHover(hiiriX, hiiriY);
            }
            case KENTTÄ -> {
                YläpalkkiKenttä.tarkistaHover(hiiriX, hiiriY);
            }
            case NÄYTÄ -> {
                YläpalkkiNäytä.tarkistaHover(hiiriX, hiiriY);
            }
        }
    }

    public static void tarkistaPainetutNapit(int hiiriX, int hiiriY) {
        if (välilehtiTiedostoNappi.hiiriSisällä(hiiriX, hiiriY)) {
            nykyinenVälilehti = Välilehdet.TIEDOSTO;
        }
        else if (välilehtiHuoneNappi.hiiriSisällä(hiiriX, hiiriY)) {
            nykyinenVälilehti = Välilehdet.HUONE;
        }
        else if (välilehtiKenttäNappi.hiiriSisällä(hiiriX, hiiriY)) {
            nykyinenVälilehti = Välilehdet.KENTTÄ;
        }
        else if (välilehtiNäytäNappi.hiiriSisällä(hiiriX, hiiriY)) {
            nykyinenVälilehti = Välilehdet.NÄYTÄ;
        }
        switch (nykyinenVälilehti) {
            case TIEDOSTO -> {
                YläpalkkiTiedosto.tarkistaPainetutNapit(hiiriX, hiiriY);
            }
            case HUONE -> {
                YläpalkkiHuone.tarkistaPainetutNapit(hiiriX, hiiriY);
            }
            case KENTTÄ -> {
                YläpalkkiKenttä.tarkistaPainetutNapit(hiiriX, hiiriY);
            }
            case NÄYTÄ -> {
                YläpalkkiNäytä.tarkistaPainetutNapit(hiiriX, hiiriY);
            }
        }
    }

    public static void asetaValittuObjekti(PeliObjekti objekti) {
        YläpalkkiKenttä.asetaValittuObjekti(objekti);
        nykyinenVälilehti = Välilehdet.KENTTÄ;
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        pohjaVasenLabel.renderöi(shader, window);
        pohjaOikeaLabel.renderöi(shader, window);

        välilehtiTiedostoNappi.renderöi(shader, window);
        välilehtiHuoneNappi.renderöi(shader, window);
        välilehtiKenttäNappi.renderöi(shader, window);
        välilehtiNäytäNappi.renderöi(shader, window);

        switch (nykyinenVälilehti) {
            case TIEDOSTO -> {
                YläpalkkiTiedosto.renderöi(shader, window);
            }
            case HUONE -> {
                YläpalkkiHuone.renderöi(shader, window);
            }
            case KENTTÄ -> {
                YläpalkkiKenttä.renderöi(shader, window);
            }
            case NÄYTÄ -> {
                YläpalkkiNäytä.renderöi(shader, window);
            }
        }
    }
}
