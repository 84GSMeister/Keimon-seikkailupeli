package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.assets.EngineAssets;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.ikkuna.Window;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;

public class Komponentti {

    private static Matrix4f sijaintiMatriisi = new Matrix4f();
    private static Matrix4f rotaatioMatriisi = new Matrix4f();
    
    public static void renderöiKomponentti(Shader shader, Renderöitävä tekstuuri, Window window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ) {
        sijaintiMatriisi.identity();
        sijaintiMatriisi = skaalaaPiirtoalueKuvasuhteenMukaan(sijaintiMatriisi, window);
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        shader.setUniform("projection", sijaintiMatriisi);
        tekstuuri.bind(0);
        EngineAssets.getModel().render();
    }

    public static void renderöiKomponenttiRotaatio(Shader shader, Renderöitävä tekstuuri, Window window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ, float rotX, float rotY, float rotZ) {
        sijaintiMatriisi.identity();
        sijaintiMatriisi = skaalaaPiirtoalueKuvasuhteenMukaan(sijaintiMatriisi, window);
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        rotaatioMatriisi.rotate(new AxisAngle4f((float)Math.toRadians(1), rotX, rotY, rotZ));
        sijaintiMatriisi.mul(rotaatioMatriisi);
        shader.setUniform("projection", sijaintiMatriisi);
        tekstuuri.bind(0);
        EngineAssets.getModel().render();
    }

    // Kuva venytetään aina 4:3 piirtoalueeseen valikoissa. Sen isommilla kuvasuhteilla tulee mustat palkit.
    private static Matrix4f skaalaaPiirtoalueKuvasuhteenMukaan(Matrix4f sijaintiMatriisi, Window ikkuna) {
        if (ikkuna.getWidth() > 0 && ikkuna.getHeight() > 0) {
            if ((float)ikkuna.getWidth() / (float)ikkuna.getHeight() > 4f / 3f) {
                sijaintiMatriisi.scale(((float)ikkuna.getHeight()/(float)ikkuna.getWidth()) * (4f/3f), 1f, 1f);
            }
        }
        return sijaintiMatriisi;
    }
}
