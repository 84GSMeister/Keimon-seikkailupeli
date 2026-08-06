package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.assets.EngineAssets;
import keimo.keimoengine.assets.GUITekstuurit;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Latauspalkki extends Komponentti {

    protected float latausProsentti = 0;
    protected Renderöitävä palkkiPunainenTekstuuri = GUITekstuurit.annaTekstuuri("palkki_punainen");
    protected Renderöitävä palkkiVihreäTekstuuri = GUITekstuurit.annaTekstuuri("palkki_vihreä");

    public Latauspalkki(float scaleX, float scaleY, float offsetX, float offsetY) {
        super(scaleX, scaleY, offsetX, offsetY);
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
