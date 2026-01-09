package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.assets.EngineAssets;

import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * Komponentti, jolle voidaan määrittää toiminto klikatessa. 
 */

public class Nappi {

    protected int minX, minY;
    protected int maxX, maxY;
    protected float scaleX, scaleY;
    protected float offsetX, offsetY;
    protected Renderöitävä tekstuuri;
    protected boolean hover;
    protected Ikkuna window;
    protected TooltipTeksti tooltipTeksti;

    public Nappi(float scaleX, float scaleY, float offsetX, float offsetY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Nappi(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.tekstuuri = tekstuuri;
    }

    public Nappi(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri, TooltipTeksti tooltipTeksti) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.tekstuuri = tekstuuri;
        this.tooltipTeksti = tooltipTeksti;
    }

    public Renderöitävä annaSisältö() {
        return tekstuuri;
    }

    public void päivitäSisältö(Renderöitävä tekstuuri) {
        this.tekstuuri = tekstuuri;
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

    public void muutaSisältöä(Renderöitävä tekstuuri) {
        this.tekstuuri = tekstuuri;
    }

    public void renderöi(Shader shader, Ikkuna window) {
        this.window = window;
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, 0, false, false);
    }

    public void renderöiRotaatio(Shader shader, Ikkuna window, int kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
        this.window = window;
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, kääntöAsteet, xPeilaus, yPeilaus);
    }

    public void renderöiTooltip(Shader shader, Ikkuna window) {
        if (hover && tooltipTeksti != null) tooltipTeksti.renderöi(shader, window);
    }

    private void renderöiKomponentti(Shader shader, Renderöitävä tekstuuri, Ikkuna window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ, int kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
        Matrix4f sijaintiMatriisi = new Matrix4f();
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        shader.setUniform("projection", sijaintiMatriisi);
        if (hover) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0.5f));
        else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        tekstuuri.bind(0);
        EngineAssets.getModel(kääntöAsteet, xPeilaus, yPeilaus).render();
    }
}
