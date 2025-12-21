package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.ikkuna.Window;
import keimo.keimoengine.assets.EngineAssets;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * Renderöityy muuten kuten tavallinen stattinen komponentti, mutta venyttää kuvasuhteen aina 4:3 niin,
 * että laajalla resoluutiolla tulee mustat palkit sivuille.
 * Suunniteltu valikoissa käytettäväksi.
 */

public class MenuKomponentti {

    private float scaleX, scaleY, scaleZ;
    private float offsetX, offsetY, offsetZ;
    private float rotX, rotY, rotZ;
    private Renderöitävä tekstuuri;
    private Matrix4f sijaintiMatriisi = new Matrix4f();
    private Matrix4f rotaatioMatriisi = new Matrix4f();

    public MenuKomponentti(float scaleX, float scaleY, float offsetX, float offsetY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public MenuKomponentti(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.tekstuuri = tekstuuri;
    }

    public MenuKomponentti(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri, float rotX, float rotY, float rotZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.tekstuuri = tekstuuri;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    public Renderöitävä annaSisältö() {
        return tekstuuri;
    }

    public void päivitäSisältö(Renderöitävä tekstuuri) {
        this.tekstuuri = tekstuuri;
    }

    public void muutaKokoa(float scaleX, float scaleY, float offsetX, float offsetY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void muutaOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void muutaOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    // public void render(Shader shader, Renderöitävä tekstuuri, Window window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ) {
    //     renderöiKomponentti(shader, tekstuuri, window, skaalaX, skaalaY, 1, offsetX, offsetY, 0);
    // }

    // public void render(Shader shader, Renderöitävä tekstuuri, Window window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ, float rotX, float rotY, float rotZ) {
    //     renderöiKomponenttiRotaatio(shader, tekstuuri, window, skaalaX, skaalaY, 1, offsetX, offsetY, 0, rotX, rotY, rotZ);
    // }

    public void renderöi(Shader shader, Window window) {
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
    }

    private void renderöiKomponentti(Shader shader, Renderöitävä tekstuuri, Window window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ) {
        sijaintiMatriisi.identity();
        sijaintiMatriisi = skaalaaPiirtoalueKuvasuhteenMukaan(sijaintiMatriisi, window);
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        shader.setUniform("projection", sijaintiMatriisi);
        tekstuuri.bind(0);
        EngineAssets.getModel().render();
    }

    // private void renderöiKomponenttiRotaatio(Shader shader, Renderöitävä tekstuuri, Window window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ, float rotX, float rotY, float rotZ) {
    //     sijaintiMatriisi.identity();
    //     sijaintiMatriisi = skaalaaPiirtoalueKuvasuhteenMukaan(sijaintiMatriisi, window);
    //     sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
    //     sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
    //     rotaatioMatriisi.rotate(new AxisAngle4f((float)Math.toRadians(1), rotX, rotY, rotZ));
    //     sijaintiMatriisi.mul(rotaatioMatriisi);
    //     shader.setUniform("projection", sijaintiMatriisi);
    //     tekstuuri.bind(0);
    //     EngineAssets.getModel().render();
    // }

    // Kuva venytetään aina 4:3 piirtoalueeseen valikoissa. Sen isommilla kuvasuhteilla tulee mustat palkit.
    private Matrix4f skaalaaPiirtoalueKuvasuhteenMukaan(Matrix4f sijaintiMatriisi, Window ikkuna) {
        if (ikkuna.getWidth() > 0 && ikkuna.getHeight() > 0) {
            if ((float)ikkuna.getWidth() / (float)ikkuna.getHeight() > 4f / 3f) {
                sijaintiMatriisi.scale(((float)ikkuna.getHeight()/(float)ikkuna.getWidth()) * (4f/3f), 1f, 1f);
            }
        }
        return sijaintiMatriisi;
    }
}
