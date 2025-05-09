package keimo.keimoEngine.menu;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.HuoneEditori.TarinaEditori.TarinaDialogiLista;
import keimo.HuoneEditori.TarinaEditori.TarinaPätkä;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.grafiikat.objekti3d.Transform3D;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.äänet.Musat;
import keimo.keimoEngine.äänet.Äänet;

import java.awt.Color;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class TarinaRuutu {

    private static Shader tarinaRuutuShader = new Shader("staattinen");
    private static Transform3D transformJatkaNappi = new Transform3D();
    private static Transform3D transformKuva = new Transform3D();
    private static Teksti tekstiTexture = new Teksti("Tarinan teksti 1", Color.WHITE, 600, 200);
    private static Teksti jatkaNappiTexture = new Teksti("Space: Jatka", Color.WHITE, 200, 30);
    public static float fade = 0.5f;
    private static float pyörimisNopeus = 1f;
    static ArrayList<Kuva> tarinanKuvat = new ArrayList<>();
    static ArrayList<String> tarinanTekstit = new ArrayList<>();

    static int klikkaustenMäärä;
    static int tarinanPituusRuutuina;
    static String tarinanTunniste;

    public static void lataaTarinaPätkä(String tarinanTunniste) {
        klikkaustenMäärä = 0;
        tarinanKuvat.clear();
        tarinanTekstit.clear();
        if (TarinaDialogiLista.tarinaKartta.containsKey(tarinanTunniste)) {
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
                    tarinanTekstit.add("Tarinapätkälle " + "\"" + tarinanTunniste + "\"\n" + "ei löytynyt sivua " + i + "\n\n" + "Onkohan kst-tiedosto vioittunut?");
                }
            }
            for (int i = 0; i < tp.annaPituus(); i++) {
                try {
                    String s = tp.annaKuvatiedostot()[i];
                    if (s.endsWith(".gif"))
                    tarinanKuvat.add(new Animaatio(s));
                    else tarinanKuvat.add(new Tekstuuri(s));
                }
                catch (NullPointerException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    tarinanKuvat.add(new Tekstuuri("tiedostot/kuvat/tarina/tarina_placeholder.png"));
                }
            }
        }
        else {
            tarinanTekstit.add("Ei voitu ladata tarinapätkää " + "\"" + tarinanTunniste + "\"" + "\n" + "Onkohan kst-tiedosto vioittunut?");
            tarinanKuvat.add(new Tekstuuri("tiedostot/kuvat/tarina/tarina_placeholder.png"));
        }
    }

    public static void jatka() {
        klikkaustenMäärä++;
        if (klikkaustenMäärä >= tarinanPituusRuutuina -1 && tarinanTunniste == "alku") {
            //jatka.setText("Space: Aloita peli");
        }
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

    public static void render(Window window) {
        tarinaRuutuShader.bind();
        float scaleX = 1;
        if (window.getWidth() > 0 && window.getHeight() > 0) scaleX = window.getWidth()/ (window.getWidth()*2/window.getHeight());
        float scaleYKuva = window.getHeight()/4;
        float scaleYTeksti = window.getHeight()/6;
        float scaleYJatkaNappi = window.getHeight()/12;

        Matrix4f matKuva = new Matrix4f();
        window.getView().scale(1, matKuva);
        matKuva.translate(0, scaleYKuva, 0);
        matKuva.scale(scaleX, scaleYKuva, 0);
        //transformKuva.getRotation().rotateAxis((float)Math.toRadians(pyörimisNopeus), 0.347f, 0.52f, 1);
        matKuva.mul(transformKuva.getTransformation());
        tarinaRuutuShader.setUniform("projection", matKuva);
        tarinanKuvat.get(klikkaustenMäärä).bind(0);
        Assets.getModel().render();

        Matrix4f matTeksti = new Matrix4f();
        window.getView().scale(1, matTeksti);
        matTeksti.translate(0, -scaleYTeksti, 0);
        matTeksti.scale(scaleX, scaleYTeksti, 0);
        tarinaRuutuShader.setUniform("projection", matTeksti);
        tekstiTexture.bind(0);
        Assets.getModel().render();
        tekstiTexture.päivitäTeksti(tarinanTekstit.get(klikkaustenMäärä), false, 36, Color.white, 10, 10);

        Matrix4f matJatkaNappi = new Matrix4f();
        window.getView().scale(1, matJatkaNappi);
        matJatkaNappi.translate(0, -scaleYJatkaNappi*5, 0);
        matJatkaNappi.scale(scaleX, scaleYJatkaNappi, 0);
        transformJatkaNappi.getRotation().rotateAxis((float)Math.toRadians(pyörimisNopeus), 0, 1, 0);
        matJatkaNappi.mul(transformJatkaNappi.getTransformation());
        tarinaRuutuShader.setUniform("addcolor", new Vector4f(0, 0, 0, 1f));
        tarinaRuutuShader.setUniform("projection", matJatkaNappi);
        jatkaNappiTexture.bind(0);
        Assets.getModel().render();
    }
}
