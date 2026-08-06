package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.assets.EngineAssets;

import org.joml.AxisAngle4f;
import org.joml.Vector4f;

/**
 * Komponentti, jolle voidaan määrittää toiminto klikatessa. 
 */

public class Nappi extends Komponentti {

    protected int minX, minY;
    protected int maxX, maxY;
    protected boolean hover;
    protected Ikkuna window;
    protected TooltipTeksti tooltipTeksti;

    public Nappi(float scaleX, float scaleY, float offsetX, float offsetY) {
        super(scaleX, scaleY, offsetX, offsetY);
    }

    public Nappi(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri) {
        super(scaleX, scaleY, offsetX, offsetY, tekstuuri);
    }

    public Nappi(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri, TooltipTeksti tooltipTeksti) {
        this(scaleX, scaleY, offsetX, offsetY, tekstuuri);
        this.tooltipTeksti = tooltipTeksti;
    }
    
    public boolean hiiriSisällä(int hiiriX, int hiiriY) {
        if (window != null) {
            hover = false;
            int napinSijX = (int)(window.getWidth()/2 + (window.getWidth()/2)*offsetX);
            float scaleXits = Math.abs(scaleX);
            minX = napinSijX - (int)(window.getWidth()/2*scaleXits);
            maxX = napinSijX + (int)(window.getWidth()/2*scaleXits);

            int napinSijY = (int)(window.getHeight()/2 - (window.getHeight()/2)*offsetY);
            float scaleYits = Math.abs(scaleY);
            minY = napinSijY - (int)(window.getHeight()/2*scaleYits);
            maxY = napinSijY + (int)(window.getHeight()/2*scaleYits);

            if (hiiriX >= minX && hiiriX <= maxX) {
                if (hiiriY >= minY && hiiriY <= maxY) {
                    hover = true;
                }
            }
            if (hover && tooltipTeksti != null) tooltipTeksti.päivitäSijainti(hiiriX, hiiriY);
            return hover;
        }
        else return false;
    }

    public void poistaValinta() {
        hover = false;
    }

    public void renderöi(Shader shader, Ikkuna window) {
        this.window = window;
        rotaatioMatriisi.identity();
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, false, 0, 0, 0, 0, false, false);
    }

    public void renderöiTekstuuriKääntö(Shader shader, Ikkuna window, int kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
        this.window = window;
        rotaatioMatriisi.identity();
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, false, 0, 0, 0, kääntöAsteet, xPeilaus, yPeilaus);
    }

    public void renderöiPyörivä(Shader shader, Ikkuna window, float rotX, float rotY, float rotZ) {
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, true, rotX, rotY, rotZ, 0, false, false);
    }

    public void renderöiRotaatio(Shader shader, Ikkuna window, float rotX, float rotY, float rotZ) {
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, false, rotX, rotY, rotZ, 0, false, false);
    }

    public void renderöiTooltip(Shader shader, Ikkuna window) {
        if (hover && tooltipTeksti != null) tooltipTeksti.renderöi(shader, window);
    }

    private void renderöiKomponentti(Shader shader, Renderöitävä tekstuuri, Ikkuna window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ, boolean pyörivä, float rotX, float rotY, float rotZ, int kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
        sijaintiMatriisi.identity();
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        if (!pyörivä) rotaatioMatriisi.identity();
        rotaatioMatriisi.rotate(new AxisAngle4f((float)Math.toRadians(rotX), 1, 0, 0));
        rotaatioMatriisi.rotate(new AxisAngle4f((float)Math.toRadians(rotY), 0, 1, 0));
        rotaatioMatriisi.rotate(new AxisAngle4f((float)Math.toRadians(rotZ), 0, 0, 1));
        sijaintiMatriisi.mul(rotaatioMatriisi);
        shader.asetaSijainti(sijaintiMatriisi);
        if (hover) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0.5f));
        else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        tekstuuri.bind(0);
        EngineAssets.getModel(kääntöAsteet, xPeilaus, yPeilaus).render();
    }
}
