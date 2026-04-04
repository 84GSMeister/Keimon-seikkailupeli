package keimo.seikkailupeli.menu.editori.gui.yläpalkki;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.PeliObjekti;

public class Yläpalkki {

    private static Renderöitävä pohjaVasenTekstuuri = Assets.annaTekstuuri("editori_yläpalkki_pohja_vasen");
    private static Renderöitävä pohjaOikeaTekstuuri = Assets.annaTekstuuri("editori_yläpalkki_pohja_oikea");
    private static StaattinenKomponentti pohjaVasenLabel = new StaattinenKomponentti(0.25f, 0.15f, -0.75f, 0.85f, pohjaVasenTekstuuri);
    private static StaattinenKomponentti pohjaOikeaLabel = new StaattinenKomponentti(0.75f, 0.15f, 0.25f, 0.85f, pohjaOikeaTekstuuri);

    private static Nappi välilehtiTiedostoNappi = new Nappi(0.122f, 0.045f, -0.872f, 0.94f, Assets.annaTekstuuri("editori_välilehti_tiedosto"), new TooltipTeksti("Tiedosto"));
    private static Nappi välilehtiHuoneNappi = new Nappi(0.122f, 0.045f, -0.872f, 0.85f, Assets.annaTekstuuri("editori_välilehti_huone"), new TooltipTeksti("Huone"));
    private static Nappi välilehtiKenttäNappi = new Nappi(0.122f, 0.045f, -0.872f, 0.76f, Assets.annaTekstuuri("editori_välilehti_kenttä"), new TooltipTeksti("Kenttä"));
    private static Nappi välilehtiNäytäNappi = new Nappi(0.122f, 0.045f, -0.628f, 0.94f, Assets.annaTekstuuri("editori_välilehti_näytä"), new TooltipTeksti("Näytä"));
    private static Nappi välilehtiLisäosatNappi = new Nappi(0.122f, 0.045f, -0.628f, 0.85f, Assets.annaTekstuuri("editori_välilehti_lisäosat"), new TooltipTeksti("Lisäosat"));

    public static enum Välilehdet {
        TIEDOSTO,
        HUONE,
        KENTTÄ,
        NÄYTÄ,
        LISÄOSAT;
    }
    public static Välilehdet nykyinenVälilehti = Välilehdet.HUONE;

    public static void alustaGrafiikat() {
        YläpalkkiTiedosto.alustaGrafiikat();
        YläpalkkiHuone.alustaGrafiikat();
        YläpalkkiKenttä.alustaGrafiikat();
        YläpalkkiNäytä.alustaGrafiikat();
        YläpalkkiLisäosat.alustaGrafiikat();
    }

    public static void tarkistaYläpalkkiHover(int hiiriX, int hiiriY) {
        välilehtiTiedostoNappi.hiiriSisällä(hiiriX, hiiriY);
        välilehtiHuoneNappi.hiiriSisällä(hiiriX, hiiriY);
        välilehtiKenttäNappi.hiiriSisällä(hiiriX, hiiriY);
        välilehtiNäytäNappi.hiiriSisällä(hiiriX, hiiriY);
        välilehtiLisäosatNappi.hiiriSisällä(hiiriX, hiiriY);
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
            case LISÄOSAT -> {
                YläpalkkiLisäosat.tarkistaHover(hiiriX, hiiriY);
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
        else if (välilehtiLisäosatNappi.hiiriSisällä(hiiriX, hiiriY)) {
            nykyinenVälilehti = Välilehdet.LISÄOSAT;
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
            case LISÄOSAT -> {
                YläpalkkiLisäosat.tarkistaPainetutNapit(hiiriX, hiiriY);
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
        välilehtiLisäosatNappi.renderöi(shader, window);

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
            case LISÄOSAT -> {
                YläpalkkiLisäosat.renderöi(shader, window);
            }
        }
    }
}
