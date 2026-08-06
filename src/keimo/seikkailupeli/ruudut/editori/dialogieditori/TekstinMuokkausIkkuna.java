package keimo.seikkailupeli.ruudut.editori.dialogieditori;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.ruudut.editori.dialogieditori.DialogiEditoriRuutu.Tilat;

public class TekstinMuokkausIkkuna {

    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("editori_lisäikkuna_pohja");
    private static LabelKomponentti pohja = new LabelKomponentti(0.6f, 0.6f, 0, -0.15f, pohjaTekstuuri);
    private static Teksti muokkausTeksti;
    private static Nappi muokkausTekstiLabel = new Nappi(0.45f, 0.3f, 0, 0.1f, muokkausTeksti);

    private static Renderöitävä okTekstuuri = Assets.annaTekstuuri("editori_nappi_ok");
    private static Renderöitävä peruutaTekstuuri = Assets.annaTekstuuri("editori_nappi_peruuta");
    private static Nappi okNappi = new Nappi(0.15f, 0.05f, -0.2f, -0.45f, okTekstuuri);
    private static Nappi peruutaNappi = new Nappi(0.15f, 0.05f, 0.2f, -0.45f, peruutaTekstuuri);

    protected static String muokattavaTeksti = "";
    private static boolean muokkaaPuhujaa = false;
    private static int tekstiOsoittimenAjastin = 0;

    protected static void avaaTekstinMuokkausIkkuna(String teksti, boolean puhuja) {
        DialogiEditoriRuutu.dialogiEditorinTila = Tilat.TEKSTIN_MUOKKAUS;
        muokattavaTeksti = new String(teksti);
        muokkaaPuhujaa = puhuja;
    }

    protected static void suljeTekstinMuokkausIkkuna() {
        DialogiEditoriRuutu.dialogiEditorinTila = Tilat.PÄTKÄN_MUOKKAUS;
    }

    protected static void tarkistaHover(int hiiriX, int hiiriY) {
        okNappi.hiiriSisällä(hiiriX, hiiriY);
        peruutaNappi.hiiriSisällä(hiiriX, hiiriY);
    }

    protected static void tarkistaNapit(int hiiriX, int hiiriY) {
        if (okNappi.hiiriSisällä(hiiriX, hiiriY)) {
            suljeTekstinMuokkausIkkuna();
            try {
                if (muokkaaPuhujaa) {
                    DialogiPätkänMuokkaus.muokattavaVdp.annaPuhujat()[DialogiTekstinMuokkaus.muokattavaIndeksi] = muokattavaTeksti;
                }
                else {
                    DialogiPätkänMuokkaus.muokattavaVdp.annaTekstit()[DialogiTekstinMuokkaus.muokattavaIndeksi] = muokattavaTeksti;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (peruutaNappi.hiiriSisällä(hiiriX, hiiriY)) {
            suljeTekstinMuokkausIkkuna();
        }
    }

    protected static void lisääMerkki(String merkki) {
        muokattavaTeksti += merkki;
    }

    protected static void poistaMerkki() {
        if (muokattavaTeksti.length() > 0) {
            muokattavaTeksti = muokattavaTeksti.substring(0, muokattavaTeksti.length()-1);
        }
    }

    private static void alustaGrafiikat() {
        if (muokkausTeksti == null) {
            muokkausTeksti = new Teksti("puhuja", Väri.white, 1500, 224);
            muokkausTekstiLabel.päivitäSisältö(muokkausTeksti);
        }
    }

    protected static void renderöi(Shader shader, Ikkuna window) {
        try {
            alustaGrafiikat();
            pohja.renderöi(shader, window);
            if (DialogiPätkänMuokkaus.muokattavaVdp != null) {
                if (tekstiOsoittimenAjastin / 16 % 2 == 0) {
                    muokkausTeksti.päivitäTeksti(muokattavaTeksti + "_", 2);
                }
                else {
                    muokkausTeksti.päivitäTeksti(muokattavaTeksti + " ", 2);
                }
                muokkausTekstiLabel.päivitäSisältö(muokkausTeksti);
            }
            muokkausTekstiLabel.renderöi(shader, window);

            okNappi.renderöi(shader, window);
            peruutaNappi.renderöi(shader, window);
            tekstiOsoittimenAjastin++;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
