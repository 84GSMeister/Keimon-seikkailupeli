package keimo.seikkailupeli.ruudut.editori.tarinaeditori;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.Nappi;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.ruudut.editori.tarinaeditori.TarinaEditoriIkkuna.Tilat;

import java.util.HashMap;
import java.util.Set;

public class KuvanValintaIkkuna {
    
    private static Renderöitävä pohjaTekstuuri = Assets.annaTekstuuri("editori_lisäikkuna_pohja");
    private static LabelKomponentti pohja = new LabelKomponentti(0.6f, 0.6f, 0, -0.15f, pohjaTekstuuri);
    private static HashMap<Nappi, String> kuvaVaihtoehdot = new HashMap<>();

    private static Renderöitävä peruutaTekstuuri = Assets.annaTekstuuri("editori_nappi_peruuta");
    private static Renderöitävä nuoliTekstuuri = Assets.annaTekstuuri("editori_nappi_nuoli");
    private static Nappi peruutaNappi = new Nappi(0.15f, 0.05f, 0f, -0.65f, peruutaTekstuuri);
    private static Nappi nuoliYlösNappi = new Nappi(0.08f, 0.1f, 0.525f, 0.35f, nuoliTekstuuri);
    private static Nappi nuoliAlasNappi = new Nappi(0.08f, -0.1f, 0.525f, -0.65f, nuoliTekstuuri);

    private static int sarakkeet = 3;
    private static int rivit = 3;
    private static int scroll = 0;
    private static int maxScroll = Assets.annaTarinaTekstuurit().size()/sarakkeet - rivit;

    protected static void avaaKuvanValintaIkkuna() {
        TarinaEditoriIkkuna.tarinaEditorinTila = Tilat.KUVAN_VALINTA;
        kuvaVaihtoehdot.clear();
        Set<String> kuvaTiedostot = Assets.annaTarinaTekstuurit().keySet();
        for (String s : kuvaTiedostot) {
            kuvaVaihtoehdot.put(new Nappi(1/6f, 1/8f, 0, 0, Assets.annaTarinaTekstuuri(s)), s);
        }
    }

    protected static void suljeKuvanValintaIkkuna() {
        TarinaEditoriIkkuna.tarinaEditorinTila = Tilat.PÄTKÄN_VALINTA;
    }

    protected static void tarkistaHover(int hiiriX, int hiiriY) {
        peruutaNappi.hiiriSisällä(hiiriX, hiiriY);
        nuoliYlösNappi.hiiriSisällä(hiiriX, hiiriY);
        nuoliAlasNappi.hiiriSisällä(hiiriX, hiiriY);
        for (Nappi n : kuvaVaihtoehdot.keySet()) {
            n.hiiriSisällä(hiiriX, hiiriY);
        }
    }

    protected static void tarkistaNapit(int hiiriX, int hiiriY) {
        if (peruutaNappi.hiiriSisällä(hiiriX, hiiriY)) {
            suljeKuvanValintaIkkuna();
        }
        else if (nuoliYlösNappi.hiiriSisällä(hiiriX, hiiriY)) {
            if (scroll > 0) scroll--;
        }
        else if (nuoliAlasNappi.hiiriSisällä(hiiriX, hiiriY)) {
            if (scroll < maxScroll) scroll++;
        }
        else {
            try {
                for (Nappi n : kuvaVaihtoehdot.keySet()) {
                    if (n.hiiriSisällä(hiiriX, hiiriY)) {
                        TarinaPätkänValinta.muokattavaTarinaPätkä.annaKuvatiedostot()[TarinaPätkänValinta.muokattavaIndeksi] = kuvaVaihtoehdot.get(n);
                        suljeKuvanValintaIkkuna();
                        break;
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected static void scroll(int scrollY) {
        if (scrollY > 0) {
            if (scroll > 0) scroll--;
        }
        else if (scrollY < 0) {
            if (scroll < maxScroll) scroll++;
        }
    }

    protected static void renderöi(Shader shader, Ikkuna window) {
        try {
            pohja.renderöi(shader, window);

            int indeksi = 0;
            for (Nappi n : kuvaVaihtoehdot.keySet()) {
                if (!(indeksi/sarakkeet < scroll) && !((indeksi/sarakkeet) > (rivit + scroll))) {
                    float offsetX = (indeksi % sarakkeet) * (1/3f) - (1/3f);
                    float offsetY = scroll * 0.2f - (indeksi/sarakkeet) * 0.2f + 0.2f;
                    n.muutaOffsetX(offsetX);
                    n.muutaOffsetY(offsetY);
                    n.renderöi(shader, window);
                }
                indeksi++;
            }

            peruutaNappi.renderöi(shader, window);
            nuoliYlösNappi.renderöi(shader, window);
            nuoliAlasNappi.renderöi(shader, window);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
