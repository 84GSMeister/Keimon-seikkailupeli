package keimo.seikkailupeli.menu.editori.gui;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.ikkuna.Window;
import keimo.seikkailupeli.menu.editori.EditoriRuutu;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;

import java.awt.Color;

public class TietoIkkuna {
    
    private static Tekstuuri pohjaTekstuuri = new Tekstuuri("tiedostot/kuvat/editori/popup_valinta_pohja.png");
    private static StaattinenKomponentti pohja = new StaattinenKomponentti(0.5f, 0.5f, 0, 0, pohjaTekstuuri);

    private static Teksti otsikkoTeksti = new Teksti("Otsikko", Color.white, 800, 48);
    private static Teksti alaotsikkoTeksti = new Teksti("Alaotsikko", Color.white, 2800, 48);
    private static Teksti tiedotTeksti = new Teksti("Tiedot", Color.white, 1600, 400);
    private static StaattinenKomponentti otsikko = new StaattinenKomponentti(0.45f, 0.1f, 0, 0.4f, otsikkoTeksti);
    private static StaattinenKomponentti alaotsikko = new StaattinenKomponentti(0.45f, 0.025f, 0, 0.3f, alaotsikkoTeksti);
    private static StaattinenKomponentti tiedot = new StaattinenKomponentti(0.45f, 0.4f, 0, -0.2f, tiedotTeksti);

    private static Nappi okNappi = new Nappi(0.15f, 0.05f, 0, -0.4f, new Tekstuuri("tiedostot/kuvat/editori/ok_nappi.png"));

    public static void tarkistaHover(int hiiriX, int hiiriY) {
        okNappi.hiiriSisällä(hiiriX, hiiriY);
    }

    public static void tarkistaNapit(int hiiriX, int hiiriY) {
        if (okNappi.hiiriSisällä(hiiriX, hiiriY)) {
            EditoriRuutu.avaaTietoIkkuna(false);
            EditoriRuutu.estäVahinkoPainallukset = true;
        }
    }

    public static void renderöi(Shader shader, Window window) {
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
