package keimo.keimoEngine.menu;

import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.grafiikat.objekti3d.Model3D;
import keimo.keimoEngine.grafiikat.objekti3d.Transform3D;
import keimo.keimoEngine.ikkuna.Window;

import java.awt.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class KehittäjäRuutu {

    private static Shader tekstiShader = new Shader("shader");
    private static Shader otsikkoShader = new Shader("shader");
    private static Shader teksti3dShader = new Shader("shader");
    private static int tekstienMäärä = 10;
    private static Tekstuuri otsikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/menu/main_kehittäjät.png");
    private static Transform3D transformOtsikko = new Transform3D();
    private static Transform3D transformTekstit = new Transform3D();

    private static Teksti tieto1Teksti = new Teksti("Tuottaja: ", Color.white, 240, 16);
    private static Teksti tieto2Teksti = new Teksti("Pelisuunnittelu: ", Color.white, 240, 16);
    private static Teksti tieto3Teksti = new Teksti("Ohjelmointi: ", Color.white, 240, 16);
    private static Teksti tieto4Teksti = new Teksti("Grafiikat: ", Color.white, 240, 16);
    private static Teksti tieto5Teksti = new Teksti("Animaatiot: ", Color.white, 240, 16);
    private static Teksti tieto6Teksti = new Teksti("3D-mallinnus: ", Color.white, 240, 16);
    private static Teksti tieto7Teksti = new Teksti("Äänisuunnittelu: ", Color.white, 240, 16);
    private static Teksti tieto8Teksti = new Teksti("Musat: ", Color.white, 240, 16);
    private static Teksti tieto9Teksti = new Teksti("Kenttäsuunnittelu: ", Color.white, 240, 16);
    private static Teksti tieto10Teksti = new Teksti("Tarina: ", Color.white, 240, 16);

    private static Teksti kehittäjä1Teksti = new Teksti("Joonatan", Color.white, 200, 16);
    private static Teksti kehittäjä2Teksti = new Teksti("Joonatan", Color.white, 200, 16);
    private static Teksti kehittäjä3Teksti = new Teksti("Joonatan", Color.white, 200, 16);
    private static Teksti kehittäjä4Teksti = new Teksti("Unto", Color.white, 200, 16);
    private static Teksti kehittäjä5Teksti = new Teksti("Unto", Color.white, 200, 16);
    private static Teksti kehittäjä6Teksti = new Teksti("Joonatan", Color.white, 200, 16);
    private static Teksti kehittäjä7Teksti = new Teksti("Joonatan", Color.white, 200, 16);
    private static Teksti kehittäjä8Teksti = new Teksti("Joonatan", Color.white, 200, 16);
    private static Teksti kehittäjä9Teksti = new Teksti("Kristian", Color.white, 200, 16);
    private static Teksti kehittäjä10Teksti = new Teksti("Kristian", Color.white, 200, 16);

    static int testX;
    static float liikeNopeus = 1;
    static float pyörimisNopeusOtsikko = 1.5f;
    static float pyörimisNopeus3DTeksti = 2f;

    public static void takaisin() {
        ÄänentoistamisSäie.toistaSFX("Valinta");
        KeimoEngine.valitseAktiivinenRuutu("valikkoruutu");
    }

    public static void render(Window window) {
        renderöiTekstit(window);
        renderöi3DTeksti(window);
    }

    private static void renderöiTekstit(Window window) {
        float scaleXOtsikko = 1;
        if (window.getWidth() > 0 && window.getHeight() > 0) scaleXOtsikko = window.getWidth()/ (window.getWidth()*2/window.getHeight());
        float scaleYOtsikko = window.getHeight()/16;
        float scaleXTekstit = window.getWidth()/4;
        float scaleYTekstit = window.getHeight()/30;
        float keskitysX = window.getWidth()/4;
        float keskitysY = window.getHeight()/8;
        float offsetX = window.getWidth()/8;
        float offsetYValinta = window.getHeight()/16;

        otsikkoShader.bind();
        testX += liikeNopeus;
        transformOtsikko.setPosition(new Vector3f((float)Math.sin(Math.toRadians(testX)), 0, 0));
        transformOtsikko.getRotation().rotateAxis((float)Math.toRadians(pyörimisNopeusOtsikko), 0, 1, 0);
        transformTekstit.getRotation().rotateAxis((float)Math.toRadians(pyörimisNopeusOtsikko), 1, 0, 0);
        otsikkoShader.setUniform("addcolor", new Vector4f(0, 0, 0, 0));
        otsikkoShader.setUniform("subcolor", new Vector4f(0, 0, 0, 0));

        Matrix4f matOtsikko = new Matrix4f();
        window.getView().scale(1, matOtsikko);
        matOtsikko.translate(0, scaleYOtsikko*6f -keskitysY, 0);
        matOtsikko.scale(scaleXOtsikko, scaleYOtsikko, 0);
        matOtsikko.mul(transformOtsikko.getTransformation());
        otsikkoShader.setUniform("projection", matOtsikko);
        otsikkoTekstuuri.bind(0);
        Assets.getModel().render();
        väriEfekti(otsikkoShader);

        tekstiShader.bind();
        for (int i = 0; i < tekstienMäärä; i++) {
            Matrix4f matOsoitin = new Matrix4f();
            window.getView().scale(1, matOsoitin);
            matOsoitin.translate(-keskitysX + offsetX, scaleYOtsikko*4f -keskitysY -i*offsetYValinta, 0);
            matOsoitin.scale(scaleXTekstit, scaleYTekstit, 0);
            matOsoitin.mul(transformTekstit.getTransformation());
            tekstiShader.setUniform("projection", matOsoitin);
            switch (i) {
                case 0: tieto1Teksti.bind(0); break;
                case 1: tieto2Teksti.bind(0); break;
                case 2: tieto3Teksti.bind(0); break;
                case 3: tieto4Teksti.bind(0); break;
                case 4: tieto5Teksti.bind(0); break;
                case 5: tieto6Teksti.bind(0); break;
                case 6: tieto7Teksti.bind(0); break;
                case 7: tieto8Teksti.bind(0); break;
                case 8: tieto9Teksti.bind(0); break;
                case 9: tieto10Teksti.bind(0); break;
            }
            Assets.getModel().render();
        }

        for (int i = 0; i < tekstienMäärä; i++) {
            Matrix4f matValinta = new Matrix4f();
            window.getView().scale(1, matValinta);
            matValinta.translate(keskitysX + offsetX, scaleYOtsikko*4f -keskitysY -i*offsetYValinta, 0);
            matValinta.scale(scaleXTekstit, scaleYTekstit, 0);
            matValinta.mul(transformTekstit.getTransformation());
            tekstiShader.setUniform("projection", matValinta);
            switch (i) {
                case 0: kehittäjä1Teksti.bind(0); break;
                case 1: kehittäjä2Teksti.bind(0); break;
                case 2: kehittäjä3Teksti.bind(0); break;
                case 3: kehittäjä4Teksti.bind(0); break;
                case 4: kehittäjä5Teksti.bind(0); break;
                case 5: kehittäjä6Teksti.bind(0); break;
                case 6: kehittäjä7Teksti.bind(0); break;
                case 7: kehittäjä8Teksti.bind(0); break;
                case 8: kehittäjä9Teksti.bind(0); break;
                case 9: kehittäjä10Teksti.bind(0); break;
            }
            Assets.getModel().render();
        }
    }

    private static void renderöi3DTeksti(Window window) {
        teksti3dShader.bind();
        väriEfekti2(teksti3dShader);
        Model3D keimoTekstiModel = Assets.getModel3D("KeimoTeksti");
        Transform3D transform = keimoTekstiModel.getTransform();
        transform.setPosition(new Vector3f(0, -6.5f, -5));
        transform.getRotation().rotateAxis((float)Math.toRadians(pyörimisNopeus3DTeksti), 0, 0, 1);
        Matrix4f mat3DMalli = new Matrix4f();
        mat3DMalli.mul(transform.getTransformation());
        teksti3dShader.setUniform("projection", mat3DMalli);
        keimoTekstiModel.draw();
    }

    static float punainen = 0f, vihreä = 0.5f, sininen = 1f;
    static boolean lisääPun = true, lisääVihr = true, lisääSin = false;
    protected static void väriEfekti(Shader shader) {
        if (lisääPun) punainen += 0.01f;
        else punainen -= 0.01f;
        if (lisääVihr) vihreä += 0.01f;
        else vihreä -= 0.01f;
        if (lisääSin) sininen += 0.01f;
        else sininen -= 0.01f;
        
        if (punainen >= 1f) lisääPun = false;
        else if (punainen <= 0f) lisääPun = true;
        if (vihreä >= 1f) lisääVihr = false;
        else if (vihreä <= 0f) lisääVihr = true;
        if (sininen >= 1f) lisääSin = false;
        else if (sininen <= 0f) lisääSin = true;
        
        shader.setUniform("himmennys", new Vector4f(punainen, vihreä, sininen, 0f));
    }

    protected static void väriEfekti2(Shader shader) {
        if (lisääPun) punainen += 0.01f;
        else punainen -= 0.01f;
        if (lisääVihr) vihreä += 0.01f;
        else vihreä -= 0.01f;
        if (lisääSin) sininen += 0.01f;
        else sininen -= 0.01f;
        
        if (punainen >= 1f) lisääPun = false;
        else if (punainen <= 0f) lisääPun = true;
        if (vihreä >= 1f) lisääVihr = false;
        else if (vihreä <= 0f) lisääVihr = true;
        if (sininen >= 1f) lisääSin = false;
        else if (sininen <= 0f) lisääSin = true;
        
        shader.setUniform("addcolor", new Vector4f(punainen, vihreä, sininen, 0f));
    }
}
