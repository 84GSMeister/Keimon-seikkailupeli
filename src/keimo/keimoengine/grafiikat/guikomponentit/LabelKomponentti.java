package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.assets.EngineAssets;

import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * Yksinkertainen staattinen komponentti, a.k.a. Label-komponentti
 */

public class LabelKomponentti extends Komponentti {

    protected boolean hover;
    protected TooltipTeksti popupTeksti;

    public LabelKomponentti(float scaleX, float scaleY, float offsetX, float offsetY) {
        super(scaleX, scaleY, offsetX, offsetY);
    }

    public LabelKomponentti(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri) {
        super(scaleX, scaleY, offsetX, offsetY, tekstuuri);
    }

    public LabelKomponentti(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri, TooltipTeksti popupTeksti) {
        this(scaleX, scaleY, offsetX, offsetY, tekstuuri);
        this.popupTeksti = popupTeksti;
    }

    public void renderöi(Shader shader, Ikkuna window) {
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, 0, false, false);
    }

    public void renderöiTekstuuriKääntö(Shader shader, Ikkuna window, int kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, kääntöAsteet, xPeilaus, yPeilaus);
    }

    public void renderöiPopup(Shader shader, Ikkuna window) {
        if (hover && popupTeksti != null) popupTeksti.renderöi(shader, window);
    }

    private void renderöiKomponentti(Shader shader, Renderöitävä tekstuuri, Ikkuna window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ, int kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
        Matrix4f sijaintiMatriisi = new Matrix4f();
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        shader.asetaSijainti(sijaintiMatriisi);
        shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        tekstuuri.bind(0);
        EngineAssets.getModel(kääntöAsteet, xPeilaus, yPeilaus).render();
    }
}
