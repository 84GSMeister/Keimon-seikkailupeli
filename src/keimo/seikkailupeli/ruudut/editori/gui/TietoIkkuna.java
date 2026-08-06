package keimo.seikkailupeli.ruudut.editori.gui;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu;

public class TietoIkkuna {
    
    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("editori_popup_pohja");
    private static LabelKomponentti pohja = new LabelKomponentti(0.5f, 0.5f, 0, 0, pohjaTekstuuri);

    private static Teksti otsikkoTeksti;
    private static Teksti alaotsikkoTeksti;
    private static Teksti tiedotTeksti;
    private static LabelKomponentti otsikko = new LabelKomponentti(0.45f, 0.1f, 0, 0.4f, otsikkoTeksti);
    private static LabelKomponentti alaotsikko = new LabelKomponentti(0.45f, 0.025f, 0, 0.3f, alaotsikkoTeksti);
    private static LabelKomponentti tiedot = new LabelKomponentti(0.45f, 0.4f, 0, -0.2f, tiedotTeksti);

    private static Nappi okNappi = new Nappi(0.15f, 0.05f, 0, -0.4f, Assets.annaTekstuuri("editori_nappi_ok"));

    public static void tarkistaHover(int hiiriX, int hiiriY) {
        okNappi.hiiriSisällä(hiiriX, hiiriY);
    }

    public static void tarkistaNapit(int hiiriX, int hiiriY) {
        if (okNappi.hiiriSisällä(hiiriX, hiiriY)) {
            EditoriRuutu.avaaTietoIkkuna(false);
            EditoriRuutu.estäVahinkoPainallukset = true;
        }
    }

    private static void alustaGrafiikat() {
        if (otsikkoTeksti == null) {
            otsikkoTeksti = new Teksti("Otsikko", Väri.white, 800, 48);
            otsikko.päivitäSisältö(otsikkoTeksti);
            alaotsikkoTeksti = new Teksti("Alaotsikko", Väri.white, 2800, 48);
            alaotsikko.päivitäSisältö(alaotsikkoTeksti);
            tiedotTeksti = new Teksti("Tiedot", Väri.white, 1600, 400);
            tiedot.päivitäSisältö(tiedotTeksti);
        }
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        alustaGrafiikat();
        if (EditoriRuutu.tarkistettavaEsine != null) {
            if (EditoriRuutu.tarkistettavaEsine instanceof KenttäKohde) {
                KenttäKohde k = (KenttäKohde)EditoriRuutu.tarkistettavaEsine;
                otsikkoTeksti.päivitäTeksti("Tiedot: " + k.annaNimi());
                alaotsikkoTeksti.päivitäTeksti("" + k);
                tiedotTeksti.päivitäTeksti(k.annaTiedot());
            }
        }
        else {
            otsikkoTeksti.päivitäTeksti("Tunematon objekti");
            alaotsikkoTeksti.päivitäTeksti("");
            tiedotTeksti.päivitäTeksti("Ei tietoja");
        }
        pohja.renderöi(shader, window);
        otsikko.renderöi(shader, window);
        alaotsikko.renderöi(shader, window);
        tiedot.renderöi(shader, window);
        okNappi.renderöi(shader, window);
    }
}
