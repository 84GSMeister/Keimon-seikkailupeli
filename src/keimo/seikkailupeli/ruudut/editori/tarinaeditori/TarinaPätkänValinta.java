package keimo.seikkailupeli.ruudut.editori.tarinaeditori;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.tarina.TarinaPätkä;
import keimo.seikkailupeli.ruudut.editori.tarinaeditori.TarinaEditoriIkkuna.Tilat;

public class TarinaPätkänValinta {
    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("editori_lisäikkuna_pohja");
    private static LabelKomponentti pohja = new LabelKomponentti(0.9f, 0.75f, 0, -0.15f, pohjaTekstuuri);
    private static Renderöitävä tarinanKuvaTekstuuri;
    private static Nappi tarinanKuvaLabel = new Nappi(0.4f, 0.3f, 0f, 0.25f, tarinanKuvaTekstuuri);
    private static Teksti tarinanTeksti;
    private static Nappi tarinanTekstiLabel = new Nappi(0.6f, 0.3f, 0f, -0.4f, tarinanTeksti);
    
    private static Renderöitävä okTekstuuri = Assets.annaTekstuuri("editori_nappi_ok");
    private static Renderöitävä nuoliTekstuuri = Assets.annaTekstuuri("editori_nappi_nuoli");
    private static Nappi okNappi = new Nappi(0.15f, 0.05f, 0, -0.8f, okTekstuuri);
    private static Nappi nuoliVasemmalleNappi = new Nappi(0.08f, 0.1f, -0.725f, -0.5f, nuoliTekstuuri);
    private static Nappi nuoliOikealleNappi = new Nappi(0.08f, -0.1f, 0.725f, -0.5f, nuoliTekstuuri);

    protected static TarinaPätkä muokattavaTarinaPätkä;
    protected static int muokattavaIndeksi = 0;

    public static void avaaTarinaPätkänMuokkaus(String tarinaPätkänNimi) {
        TarinaEditoriIkkuna.tarinaEditorinTila = Tilat.PÄTKÄN_VALINTA;
        muokattavaTarinaPätkä = TarinaEditoriIkkuna.editorinTarinaKartta.get(tarinaPätkänNimi);
        muokattavaIndeksi = 0;
    }

    public static void suljeTarinaPätkänMuokkaus() {
        TarinaEditoriIkkuna.tarinaEditorinTila = Tilat.VAKIO;
        muokattavaTarinaPätkä = null;
    }

    public static void tarkistaHover(int hiiriX, int hiiriY) {
        okNappi.hiiriSisällä(hiiriX, hiiriY);
        nuoliVasemmalleNappi.hiiriSisällä(hiiriX, hiiriY);
        nuoliOikealleNappi.hiiriSisällä(hiiriX, hiiriY);
        tarinanTekstiLabel.hiiriSisällä(hiiriX, hiiriY);
        tarinanKuvaLabel.hiiriSisällä(hiiriX, hiiriY);
    }

    public static void tarkistaNapit(int hiiriX, int hiiriY) {
        if (okNappi.hiiriSisällä(hiiriX, hiiriY)) {
            suljeTarinaPätkänMuokkaus();
        }
        else if (nuoliVasemmalleNappi.hiiriSisällä(hiiriX, hiiriY)) {
            if (muokattavaIndeksi > 0) {
                muokattavaIndeksi--;
            }
        }
        else if (nuoliOikealleNappi.hiiriSisällä(hiiriX, hiiriY)) {
            if (muokattavaIndeksi < muokattavaTarinaPätkä.annaPituus()-1) {
                muokattavaIndeksi++;
            }
        }
        else if (tarinanTekstiLabel.hiiriSisällä(hiiriX, hiiriY)) {
            TekstinMuokkausIkkuna.avaaTekstinMuokkausIkkuna(tarinanTeksti.annaTeksti());
        }
        else if (tarinanKuvaLabel.hiiriSisällä(hiiriX, hiiriY)) {
            KuvanValintaIkkuna.avaaKuvanValintaIkkuna();
        }
    }

    private static void alustaGrafiikat() {
        if (tarinanTeksti == null) {
            tarinanTeksti = new Teksti("tarinan teksti", Väri.white, 1800, 500);
            tarinanTekstiLabel.päivitäSisältö(tarinanTeksti);
        }
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        try {
            alustaGrafiikat();
            pohja.renderöi(shader, window);
            if (muokattavaTarinaPätkä != null) {
                if (muokattavaTarinaPätkä.annaTekstit().length >= muokattavaIndeksi) {
                    tarinanTeksti.päivitäTeksti(muokattavaTarinaPätkä.annaTekstit()[muokattavaIndeksi], 2);
                    tarinanTekstiLabel.päivitäSisältö(tarinanTeksti);
                }
                if (muokattavaTarinaPätkä.annaKuvatiedostot().length >= muokattavaIndeksi) {
                    tarinanKuvaLabel.päivitäSisältö(Assets.annaTarinaTekstuuri(muokattavaTarinaPätkä.annaKuvatiedostot()[muokattavaIndeksi]));
                }
            }
            tarinanTekstiLabel.renderöi(shader, window);
            tarinanKuvaLabel.renderöi(shader, window);

            if (muokattavaIndeksi > 0) nuoliVasemmalleNappi.renderöiTekstuuriKääntö(shader, window, 270, false, false);
            if (muokattavaIndeksi < muokattavaTarinaPätkä.annaPituus()-1) {
                nuoliOikealleNappi.renderöiTekstuuriKääntö(shader, window, 90, false, false);
            }

            okNappi.renderöi(shader, window);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
