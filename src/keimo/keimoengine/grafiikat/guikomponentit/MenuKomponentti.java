package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.assets.EngineAssets;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;

/**
 * Renderöityy muuten kuten tavallinen staattinen komponentti, mutta venyttää kuvasuhteen aina 4:3 niin,
 * että laajalla resoluutiolla tulee mustat palkit sivuille.
 * Suunniteltu valikoissa käytettäväksi.
 */

public class MenuKomponentti extends Komponentti {

    public MenuKomponentti(float scaleX, float scaleY, float offsetX, float offsetY) {
        super(scaleX, scaleY, offsetX, offsetY);
    }

    public MenuKomponentti(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri) {
        super(scaleX, scaleY, offsetX, offsetY, tekstuuri);
    }

    public MenuKomponentti(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri, float rotX, float rotY, float rotZ) {
        this(scaleX, scaleY, offsetX, offsetY, tekstuuri);
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    public void renderöi(Shader shader, Ikkuna window) {
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, 0, 0, 0);
    }

    public void renderöiPyörivä(Shader shader, Ikkuna window) {
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, rotX, rotY, rotZ);
    }

    private void renderöiKomponentti(Shader shader, Renderöitävä tekstuuri, Ikkuna window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ, float rotX, float rotY, float rotZ) {
        sijaintiMatriisi.identity();
        sijaintiMatriisi = skaalaaPiirtoalueKuvasuhteenMukaan(sijaintiMatriisi, window);
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        rotaatioMatriisi.rotate(new AxisAngle4f((float)Math.toRadians(rotX), 1, 0, 0));
        rotaatioMatriisi.rotate(new AxisAngle4f((float)Math.toRadians(rotY), 0, 1, 0));
        rotaatioMatriisi.rotate(new AxisAngle4f((float)Math.toRadians(rotZ), 0, 0, 1));
        sijaintiMatriisi.mul(rotaatioMatriisi);
        shader.asetaSijainti(sijaintiMatriisi);
        tekstuuri.bind(0);
        EngineAssets.getModel().render();
    }

    // Kuva venytetään aina 4:3 piirtoalueeseen valikoissa. Sen isommilla kuvasuhteilla tulee mustat palkit.
    private Matrix4f skaalaaPiirtoalueKuvasuhteenMukaan(Matrix4f sijaintiMatriisi, Ikkuna ikkuna) {
        if (ikkuna.getWidth() > 0 && ikkuna.getHeight() > 0) {
            if ((float)ikkuna.getWidth() / (float)ikkuna.getHeight() > 4f / 3f) {
                sijaintiMatriisi.scale(((float)ikkuna.getHeight()/(float)ikkuna.getWidth()) * (4f/3f), 1f, 1f);
            }
        }
        return sijaintiMatriisi;
    }
}
