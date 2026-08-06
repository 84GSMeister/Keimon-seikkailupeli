package keimo.seikkailupeli.ruudut.editori.tarinaeditori;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.guikomponentit.ObjektiListaNappi;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.tarina.TarinaPätkä;
import keimo.seikkailupeli.ruudut.editori.EditoriRuutu;

import java.util.HashMap;

public class TarinaEditoriIkkuna {
    
    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("editori_lisäikkuna_pohja");
    private static LabelKomponentti pohja = new LabelKomponentti(0.9f, 0.75f, 0, -0.15f, pohjaTekstuuri);
    private static Teksti otsikkoTeksti;
    private static LabelKomponentti otsikko = new LabelKomponentti(0.45f, 0.1f, 0, 0.4f, otsikkoTeksti);

    private static Renderöitävä okTekstuuri = Assets.annaTekstuuri("editori_nappi_ok");
    private static Renderöitävä peruutaTekstuuri = Assets.annaTekstuuri("editori_nappi_peruuta");
    private static Renderöitävä nuoliTekstuuri = Assets.annaTekstuuri("editori_nappi_nuoli");
    private static Nappi okNappi = new Nappi(0.15f, 0.05f, -0.2f, -0.8f, okTekstuuri);
    private static Nappi peruutaNappi = new Nappi(0.15f, 0.05f, 0.2f, -0.8f, peruutaTekstuuri);
    private static Nappi nuoliYlösNappi = new Nappi(0.08f, 0.1f, 0.825f, 0.5f, nuoliTekstuuri);
    private static Nappi nuoliAlasNappi = new Nappi(0.08f, -0.1f, 0.825f, -0.8f, nuoliTekstuuri);

    public static HashMap<String, TarinaPätkä> editorinTarinaKartta = new HashMap<>();
    private static Teksti dialogiOtsikkoTeksti;
    private static int scroll = 0;
    private static int maxScroll = 0;
    private static int rivejä = 14;

    protected static HashMap<Integer, ObjektiListaNappi> tarinaValikko = new HashMap<>();

    public enum Tilat {
        VAKIO,
        PÄTKÄN_VALINTA,
        TEKSTIN_MUOKKAUS,
        KUVAN_VALINTA;
    }
    public static Tilat tarinaEditorinTila = Tilat.VAKIO;

    public static void avaaTarinaEditori() {
        EditoriRuutu.tarinaEditoriAuki = true;
    }

    public static void suljeDialogiEditori() {
        EditoriRuutu.tarinaEditoriAuki = false;
        EditoriRuutu.estäVahinkoPainallukset = true;
    }

    public static void kopioiTarinakarttaEditoriin() {
        editorinTarinaKartta.clear();
        for (TarinaPätkä tp : Peli.peliTiedosto.annaTarinaKartta().values()) {
            String uusiNimi = tp.annaNimi();
            String[] uusiTekstiLista = tp.annaTekstit();
            String[] uusiKuvaLista = tp.annaKuvatiedostot();
            int uusiId = tp.annaId();
            int uusiPituus = tp.annaPituus();
            TarinaPätkä uusiTarinapätkä = new TarinaPätkä(uusiId, uusiNimi, uusiPituus, uusiKuvaLista, uusiTekstiLista);
            editorinTarinaKartta.put(uusiNimi, uusiTarinapätkä);
        }
    }

    public static void painaEsc() {
        switch (tarinaEditorinTila) {
            case VAKIO -> {
                suljeDialogiEditori();
            }
            case PÄTKÄN_VALINTA -> {
                TarinaPätkänValinta.suljeTarinaPätkänMuokkaus();
            }
            case TEKSTIN_MUOKKAUS -> {
                TekstinMuokkausIkkuna.suljeTekstinMuokkausIkkuna();
            }
            case KUVAN_VALINTA -> {
                KuvanValintaIkkuna.suljeKuvanValintaIkkuna();
            }
        }
    }

    public static void lisääMerkki(String näppäin) {
        switch (tarinaEditorinTila) {
            case TEKSTIN_MUOKKAUS -> {
                if (näppäin.length() > 0) {
                    TekstinMuokkausIkkuna.lisääMerkki(näppäin);
                }
            }
            case null, default -> {}
        }
    }

    public static void poistaMerkki() {
        switch (tarinaEditorinTila) {
            case TEKSTIN_MUOKKAUS -> {
                TekstinMuokkausIkkuna.poistaMerkki();
            }
            case null, default -> {}
        }
    }

    public static void tarkistaHover(int hiiriX, int hiiriY) {
        switch (tarinaEditorinTila) {
            case VAKIO -> {
                nuoliYlösNappi.hiiriSisällä(hiiriX, hiiriY);
                nuoliAlasNappi.hiiriSisällä(hiiriX, hiiriY);
                okNappi.hiiriSisällä(hiiriX, hiiriY);
                peruutaNappi.hiiriSisällä(hiiriX, hiiriY);
                for (Nappi nappi : tarinaValikko.values()) {
                    nappi.hiiriSisällä(hiiriX, hiiriY);
                }
            }
            case PÄTKÄN_VALINTA -> {
                TarinaPätkänValinta.tarkistaHover(hiiriX, hiiriY);
            }
            case TEKSTIN_MUOKKAUS -> {
                TekstinMuokkausIkkuna.tarkistaHover(hiiriX, hiiriY);
            }
            case KUVAN_VALINTA -> {
                KuvanValintaIkkuna.tarkistaHover(hiiriX, hiiriY);
            }
        }
    }

    public static void tarkistaNapit(int hiiriX, int hiiriY) {
        switch (tarinaEditorinTila) {
            case VAKIO -> {
                if (okNappi.hiiriSisällä(hiiriX, hiiriY)) {
                    suljeDialogiEditori();
                }
                else if (peruutaNappi.hiiriSisällä(hiiriX, hiiriY)) {
                    suljeDialogiEditori();
                }
                else if (nuoliYlösNappi.hiiriSisällä(hiiriX, hiiriY)) {
                    if (scroll > 0) scroll--;
                }
                else if (nuoliAlasNappi.hiiriSisällä(hiiriX, hiiriY)) {
                    if (scroll < maxScroll) scroll++;
                }
                else {
                    for (ObjektiListaNappi n : tarinaValikko.values()) {
                        if (n.hiiriSisällä(hiiriX, hiiriY)) {
                            String tarinanNimi = n.annaNimi();
                            TarinaPätkänValinta.avaaTarinaPätkänMuokkaus(tarinanNimi);
                            break;
                        }
                    }
                }
            }
            case PÄTKÄN_VALINTA -> {
                TarinaPätkänValinta.tarkistaNapit(hiiriX, hiiriY);
            }
            case TEKSTIN_MUOKKAUS -> {
                TekstinMuokkausIkkuna.tarkistaNapit(hiiriX, hiiriY);
            }
            case KUVAN_VALINTA -> {
                KuvanValintaIkkuna.tarkistaNapit(hiiriX, hiiriY);
            }
        }
    }

    public static void scroll(int scrollY) {
        switch (tarinaEditorinTila) {
            case VAKIO -> {
                if (scrollY > 0) {
                    if (scroll > 0) scroll--;
                }
                else if (scrollY < 0) {
                    if (scroll < maxScroll) scroll++;
                }
            }
            case PÄTKÄN_VALINTA -> {

            }
            case KUVAN_VALINTA -> {
                KuvanValintaIkkuna.scroll(scrollY);
            }
            case null, default -> {}
        }
    }

    public static void luoDialogiValikko() {
        int tarinaIndeksi = 0;
        for (TarinaPätkä tp : editorinTarinaKartta.values()) {
            Teksti teksti = new Teksti(tarinaIndeksi + ". " + tp.annaNimi(), Väri.white, 1000, 48);
            tarinaValikko.put(tarinaIndeksi, new ObjektiListaNappi(tarinaIndeksi, tp.annaNimi(), 0.8f, 0.05f, 0, 0.1f*tarinaIndeksi, teksti));
            tarinaIndeksi++;
        }
    }

    private static void alustaGrafiikat() {
        if (dialogiOtsikkoTeksti == null) {
            dialogiOtsikkoTeksti = new Teksti("otsikko", Väri.white, 1000, 48);
            otsikkoTeksti = new Teksti("Tarinapätkät", Väri.white, 800, 48);
            otsikko.päivitäSisältö(otsikkoTeksti);
            if (tarinaValikko.size() == 0) luoDialogiValikko();
        }
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        switch (tarinaEditorinTila) {
            case VAKIO -> {
                alustaGrafiikat();
                pohja.renderöi(shader, window);
                otsikko.renderöi(shader, window);

                int dialogiIndeksi = 0;
                for (Nappi nappi : tarinaValikko.values()) {
                    if (dialogiIndeksi - scroll < rivejä && dialogiIndeksi >= scroll) {
                        nappi.muutaOffsetY(0.275f -0.075f*(dialogiIndeksi-scroll));
                        nappi.renderöi(shader, window);
                    }
                    dialogiIndeksi++;
                }
                maxScroll = dialogiIndeksi - rivejä;

                nuoliYlösNappi.renderöi(shader, window);
                nuoliAlasNappi.renderöi(shader, window);

                okNappi.renderöi(shader, window);
                peruutaNappi.renderöi(shader, window);
            }
            case PÄTKÄN_VALINTA -> {
                TarinaPätkänValinta.renderöi(shader, window);
            }
            case TEKSTIN_MUOKKAUS -> {
                TarinaPätkänValinta.renderöi(shader, window);
                TekstinMuokkausIkkuna.renderöi(shader, window);
            }
            case KUVAN_VALINTA -> {
                TarinaPätkänValinta.renderöi(shader, window);
                KuvanValintaIkkuna.renderöi(shader, window);
            }
        }
    }
}
