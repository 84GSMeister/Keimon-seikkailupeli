package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.assets.EngineAssets;

import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * Yksinkertainen staattinen komponentti, a.k.a. Label-komponentti
 */

public class StaattinenKomponentti {

    protected float scaleX, scaleY;
    protected float offsetX, offsetY;
    protected Renderöitävä tekstuuri;
    protected boolean hover;
    protected TooltipTeksti popupTeksti;

    public StaattinenKomponentti(float scaleX, float scaleY, float offsetX, float offsetY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public StaattinenKomponentti(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.tekstuuri = tekstuuri;
    }

    public StaattinenKomponentti(float scaleX, float scaleY, float offsetX, float offsetY, Renderöitävä tekstuuri, TooltipTeksti popupTeksti) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.tekstuuri = tekstuuri;
        this.popupTeksti = popupTeksti;
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

    public void renderöi(Shader shader, Ikkuna window) {
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, 0, false, false);
    }

    public void renderöiRotaatio(Shader shader, Ikkuna window, int kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
        renderöiKomponentti(shader, tekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, kääntöAsteet, xPeilaus, yPeilaus);
    }

    public void renderöiPopup(Shader shader, Ikkuna window) {
        if (hover && popupTeksti != null) popupTeksti.renderöi(shader, window);
    }

    private void renderöiKomponentti(Shader shader, Renderöitävä tekstuuri, Ikkuna window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ, int kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
        Matrix4f sijaintiMatriisi = new Matrix4f();
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        shader.setUniform("projection", sijaintiMatriisi);
        shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        tekstuuri.bind(0);
        EngineAssets.getModel(kääntöAsteet, xPeilaus, yPeilaus).render();
    }
}
