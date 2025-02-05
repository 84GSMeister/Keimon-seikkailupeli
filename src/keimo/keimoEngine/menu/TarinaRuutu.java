package keimo.keimoEngine.menu;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.HuoneEditori.TarinaEditori.TarinaDialogiLista;
import keimo.HuoneEditori.TarinaEditori.TarinaPätkä;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.grafiikat.objekti3d.Transform3D;
import keimo.keimoEngine.ikkuna.Kamera;
import keimo.keimoEngine.ikkuna.Window;

import java.awt.Color;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TarinaRuutu {

    private static Shader tarinaRuutuShader = new Shader("staattinen");
    private static Shader tarinaRuutuShader3d = new Shader("shader3dtest");
    private static Kamera kamera = new Kamera(640, 480);
    private static Transform3D transformJatkaNappi = new Transform3D();
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
        TarinaPätkä tp = TarinaDialogiLista.tarinaKartta.get(tarinanTunniste);
        tarinanPituusRuutuina = tp.annaPituus();
        for (String s : tp.annaTekstit()) {
            tarinanTekstit.add(s);
        }
        for (String s : tp.annaKuvatiedostot()) {
            if (s.endsWith(".gif"))
            tarinanKuvat.add(new Animaatio(15, s));
            else tarinanKuvat.add(new Tekstuuri(s));
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
                    ÄänentoistamisSäie.toistaPeliMusa(Peli.huone.annaHuoneenMusa());
                }
                Peli.pause = false;
            }
        }
        ÄänentoistamisSäie.toistaSFX("Valinta");
    }

    public static void render(Window window) {
        tarinaRuutuShader.bind();
        float scaleX = window.getWidth()/ (window.getWidth()*2/window.getHeight());
        float scaleYKuva = window.getHeight()/4;
        float scaleYTeksti = window.getHeight()/6;
        float scaleYJatkaNappi = window.getHeight()/12;

        Matrix4f matKuva = new Matrix4f();
        window.getView().scale(1, matKuva);
        matKuva.translate(0, scaleYKuva, 0);
        matKuva.scale(scaleX, scaleYKuva, 0);
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
        tekstiTexture.päivitäTeksti(tarinanTekstit.get(klikkaustenMäärä));

        tarinaRuutuShader3d.bind();
        transformJatkaNappi.getRotation().rotateAxis((float)Math.toRadians(pyörimisNopeus), 0, 1, 0);
        kamera.setPosition(new Vector3f(0, 0, 0));
        kamera.setPerspective((float)Math.toRadians(70), 1, 0.001f, 1000f);
        tarinaRuutuShader3d.setCamera(kamera);
        tarinaRuutuShader3d.setTransform(transformJatkaNappi);

        Matrix4f matJatkaNappi = new Matrix4f();
        window.getView().scale(1, matJatkaNappi);
        matJatkaNappi.translate(0, -scaleYJatkaNappi*5, 0);
        matJatkaNappi.scale(scaleX, scaleYJatkaNappi, 0);
        tarinaRuutuShader3d.setUniform("addcolor", new Vector4f(0, 0, 0, 1f));
        tarinaRuutuShader3d.setUniform("projection", matJatkaNappi);
        jatkaNappiTexture.bind(0);
        Assets.getModel().render();
    }
}
