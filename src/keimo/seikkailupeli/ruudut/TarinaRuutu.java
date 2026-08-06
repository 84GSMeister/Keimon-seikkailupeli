package keimo.seikkailupeli.ruudut;

import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyöteLaitteet;
import keimo.seikkailupeli.Renderöinti;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.tarina.TarinaPätkä;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.äänet.Musat;
import keimo.seikkailupeli.äänet.Äänet;

import java.util.ArrayList;

public class TarinaRuutu {

    private static Renderöitävä kuvaTexture;
    private static Teksti tekstiTexture = new Teksti("Tarinan teksti 1", Väri.WHITE, 1800, 500);
    private static Teksti jatkaNappiTexture = new Teksti("Jatka", Väri.WHITE, 1600, 300, KeimoFontit.fontti_keimo_100, false);
    private static ArrayList<Renderöitävä> tarinanKuvat = new ArrayList<>();
    private static ArrayList<String> tarinanTekstit = new ArrayList<>();
    private static MenuKomponentti kuvaLabel = new MenuKomponentti(1, 0.5f, 0, 0.5f, kuvaTexture);
    private static MenuKomponentti tekstiLabel = new MenuKomponentti(1, 1f/3f, 0, -1f/3f, tekstiTexture);
    private static MenuKomponentti jatkaLabel = new MenuKomponentti(1, 1f/6f, 0, -5f/6f, jatkaNappiTexture, 0, 1, 0);

    private static int klikkaustenMäärä;
    private static int tarinanPituusRuutuina;

    public static void lataaTarinaPätkä(String tarinanTunniste) {
        klikkaustenMäärä = 0;
        tarinanKuvat.clear();
        tarinanTekstit.clear();
        if (Peli.peliTiedosto.annaTarinaKartta() != null && Peli.peliTiedosto.annaTarinaKartta().containsKey(tarinanTunniste)) {
            TarinaPätkä tp = Peli.peliTiedosto.annaTarinaKartta().get(tarinanTunniste);
            tarinanPituusRuutuina = tp.annaPituus();
            for (int i = 0; i < tp.annaPituus(); i++) {
                try {
                    String s = tp.annaTekstit()[i];
                    if (s == null) throw new NullPointerException();
                    tarinanTekstit.add(s);
                }
                catch (NullPointerException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    tarinanTekstit.add("Tarinapätkälle " + "\"" + tarinanTunniste + "\"\n" + "ei löytynyt sivua " + i + "\n\n" + "Onkohan default.kst-tiedosto vioittunut?");
                }
            }
            for (int i = 0; i < tp.annaPituus(); i++) {
                try {
                    String s = tp.annaKuvatiedostot()[i];
                    // if (s.endsWith(".gif")) tarinanKuvat.add(new Animaatio(s));
                    // else tarinanKuvat.add(new Tekstuuri(s));
                    tarinanKuvat.add(Assets.annaTarinaTekstuuri(s));
                }
                catch (NullPointerException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    //tarinanKuvat.add(new Tekstuuri("tiedostot/kuvat/tarina/tarina_placeholder.png"));
                    tarinanKuvat.add(Assets.annaTarinaTekstuuri("vakio"));
                }
            }
        }
        else {
            tarinanTekstit.add("Ei voitu ladata tarinapätkää " + "\"" + tarinanTunniste + "\"" + "\n" + "Onkohan default.kst-tiedosto vioittunut?");
            //tarinanKuvat.add(new Tekstuuri("tiedostot/kuvat/tarina/tarina_placeholder.png"));
            tarinanKuvat.add(Assets.annaTarinaTekstuuri("vakio"));
        }
        if (tarinanTunniste.equals("alku")) Musat.toistaPeliMusa("tarina");
		else Musat.toistaPeliMusa("välitarina");
    }

    public static void jatka() {
        if (klikkaustenMäärä < tarinanPituusRuutuina) {
            klikkaustenMäärä++;
        }
        Äänet.toistaSFX("Valinta");
        Pelaaja.käyttöViive = 50;
    }

    public static void renderöi(Shader shader, Ikkuna window) {
        shader.bind();
        shader.nollaaShaderEfektit();

        tarkistaSiirtymä();

        if (klikkaustenMäärä < tarinanPituusRuutuina) {
            if (tarinanKuvat.size() > klikkaustenMäärä) {
                if (tarinanKuvat.get(klikkaustenMäärä) != null) {
                    kuvaLabel.päivitäSisältö(tarinanKuvat.get(klikkaustenMäärä));
                    kuvaLabel.renderöi(shader, window);
                }
            }

            if (tarinanTekstit.size() > klikkaustenMäärä) {
                if (tarinanTekstit.get(klikkaustenMäärä) != null) {
                    tekstiTexture.päivitäTeksti(tarinanTekstit.get(klikkaustenMäärä), 2);
                    tekstiLabel.renderöi(shader, window);
                }
            }

            if (Peli.viimeisinSyöteLaite == SyöteLaitteet.NÄPPÄIMISTÖ) {
                jatkaNappiTexture.päivitäTeksti("Space: Jatka");
            }
            else if (Peli.viimeisinSyöteLaite == SyöteLaitteet.PELIOHJAIN) {
                jatkaNappiTexture.päivitäTeksti("A: Jatka");
            }
        }

        jatkaLabel.renderöiPyörivä(shader, window);
    }

    private static void tarkistaSiirtymä() {
        if (klikkaustenMäärä >= tarinanPituusRuutuina) {
            if (!Peli.peliKäynnissä) {
                Renderöinti.siirrySeuraavaanRuutuun("valikkoruutu");
            }
            else {
                Renderöinti.siirrySeuraavaanRuutuun("peliruutu");
                Pelaaja.pakotaPelaajanPysäytys();
                if (Peli.huone != null) {
                    Musat.toistaPeliMusa(Peli.huone.annaHuoneenMusa());
                }
                Peli.pause = false;
            }
        }
    }
}
