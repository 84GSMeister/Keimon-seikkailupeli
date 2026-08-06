package keimo.seikkailupeli.ruudut.editori.gui;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;

public class LatausIkkuna {
    
    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("editori_popup_pohja");
    private static LabelKomponentti pohja = new LabelKomponentti(0.5f, 0.5f, 0, 0, pohjaTekstuuri);

    private static String tilaViesti = "";
    private static Teksti otsikkoTeksti;
    private static LabelKomponentti otsikko = new LabelKomponentti(0.45f, 0.1f, 0, 0f, otsikkoTeksti);

    private static void alustaGrafiikat() {
        if (otsikkoTeksti == null) {
            otsikkoTeksti = new Teksti("Ladataan", Väri.white, 800, 48);
            otsikko.päivitäSisältö(otsikkoTeksti);
        }
    }

    public static void päivitäTila(String viesti) {
        tilaViesti = viesti;
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        alustaGrafiikat();
        otsikkoTeksti.päivitäTeksti(tilaViesti, 1);
        pohja.renderöi(shader, window);
        otsikko.renderöi(shader, window);
    }
}
