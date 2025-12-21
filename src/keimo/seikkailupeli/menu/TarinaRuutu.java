package keimo.seikkailupeli.menu;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.fontit.KeimoFontit;
import keimo.keimoengine.grafiikat.*;
import keimo.keimoengine.grafiikat.guikomponentit.Komponentti;
import keimo.keimoengine.grafiikat.guikomponentit.MenuKomponentti;
import keimo.keimoengine.ikkuna.Window;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.TarinaDialogiLista;
import keimo.seikkailupeli.assets.TarinaPätkä;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.äänet.Musat;
import keimo.seikkailupeli.äänet.Äänet;

import java.awt.Color;
import java.util.ArrayList;

public class TarinaRuutu {

    private static Tekstuuri kuvaTexture;
    private static Teksti tekstiTexture = new Teksti("Tarinan teksti 1", Color.WHITE, 1800, 500);
    private static Teksti jatkaNappiTexture = new Teksti("Space: Jatka", Color.WHITE, 1600, 300, KeimoFontit.fontti_keimo_100, false);
    private static ArrayList<Renderöitävä> tarinanKuvat = new ArrayList<>();
    private static ArrayList<String> tarinanTekstit = new ArrayList<>();
    private static MenuKomponentti kuvaLabel = new MenuKomponentti(1, 0.5f, 0, 0.5f, kuvaTexture);
    private static MenuKomponentti tekstiLabel = new MenuKomponentti(1, 1f/3f, 0, -1f/3f, tekstiTexture);

    private static int klikkaustenMäärä;
    private static int tarinanPituusRuutuina;

    public static void lataaTarinaPätkä(String tarinanTunniste) {
        klikkaustenMäärä = 0;
        tarinanKuvat.clear();
        tarinanTekstit.clear();
        if (TarinaDialogiLista.tarinaKartta != null && TarinaDialogiLista.tarinaKartta.containsKey(tarinanTunniste)) {
            TarinaPätkä tp = TarinaDialogiLista.tarinaKartta.get(tarinanTunniste);
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
                    if (s.endsWith(".gif")) tarinanKuvat.add(new Animaatio(s));
                    else tarinanKuvat.add(new Tekstuuri(s));
                }
                catch (NullPointerException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    tarinanKuvat.add(new Tekstuuri("tiedostot/kuvat/tarina/tarina_placeholder.png"));
                }
            }
        }
        else {
            tarinanTekstit.add("Ei voitu ladata tarinapätkää " + "\"" + tarinanTunniste + "\"" + "\n" + "Onkohan default.kst-tiedosto vioittunut?");
            tarinanKuvat.add(new Tekstuuri("tiedostot/kuvat/tarina/tarina_placeholder.png"));
        }
    }

    public static void jatka() {
        klikkaustenMäärä++;
        if (klikkaustenMäärä >= tarinanPituusRuutuina) {
            if (!Peli.peliAloitettu) {
                KeimoEngine.valitseAktiivinenRuutu("valikkoruutu");
            }
            else {
                KeimoEngine.valitseAktiivinenRuutu("peliruutu");
                Pelaaja.pakotaPelaajanPysäytys();
                if (Peli.huone != null) {
                    Musat.toistaPeliMusa(Peli.huone.annaHuoneenMusa());
                }
                Peli.pause = false;
            }
        }
        Äänet.toistaSFX("Valinta");
        Pelaaja.käyttöViive = 50;
    }

    public static void render(Shader shader, Window window) {
        shader.bind();
        shader.nollaaShaderEfektit();

        kuvaLabel.päivitäSisältö(tarinanKuvat.get(klikkaustenMäärä));
        kuvaLabel.renderöi(shader, window);

        tekstiTexture.päivitäTeksti(tarinanTekstit.get(klikkaustenMäärä), 2);
        tekstiLabel.renderöi(shader, window);

        Komponentti.renderöiKomponenttiRotaatio(shader, jatkaNappiTexture, window, 1, 1f/6f, 1, 0, -5f/6f, 0, 0, 1, 0);
    }
}
