package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.assets.EngineAssets;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Latauspalkki {

    protected float scaleX, scaleY;
    protected float offsetX, offsetY;
    protected float latausProsentti = 0;
    protected Tekstuuri palkkiPunainenTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/komponentit/palkki_punainen.png");
    protected Tekstuuri palkkiVihreäTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/komponentit/palkki_vihreä.png");

    public Latauspalkki(float scaleX, float scaleY, float offsetX, float offsetY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void muutaKokoa(float scaleX, float scaleY, float offsetX, float offsetY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void päivitäLatausProsentti(float prosentti) {
        this.latausProsentti = prosentti;
    }

    public void renderöi(Shader shader, Ikkuna window) {
        renderöiKomponentti(shader, palkkiPunainenTekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
        renderöiKomponentti(shader, palkkiVihreäTekstuuri, window, scaleX * (latausProsentti/100f), scaleY, 1, scaleX * latausProsentti/100f - scaleX + offsetX, offsetY, 0);
    }

    private void renderöiKomponentti(Shader shader, Renderöitävä tekstuuri, Ikkuna window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ) {
        Matrix4f sijaintiMatriisi = new Matrix4f();
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        shader.asetaSijainti(sijaintiMatriisi);
        shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        tekstuuri.bind(0);
        EngineAssets.getModel().render();
    }
    
}
