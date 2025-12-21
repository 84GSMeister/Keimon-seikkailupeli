package keimo.keimoengine.grafiikat.guikomponentit;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import keimo.keimoengine.assets.EngineAssets;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.ikkuna.Window;

public class ObjektiListaNappi extends Nappi {

    private int id;
    private String nimi;
    private Teksti teksti;
    private Renderöitävä kuvake;

    public int annaId() {
        return id;
    }

    public String annaNimi() {
        return nimi;
    }
    
    public ObjektiListaNappi(int id, String nimi, float scaleX, float scaleY, float offsetX, float offsetY, Teksti teksti) {
        super(scaleX, scaleY, offsetX, offsetY, teksti);
        this.id = id;
        this.nimi = nimi;
        this.teksti = teksti;
    }

    public ObjektiListaNappi(int id, String nimi, float scaleX, float scaleY, float offsetX, float offsetY, Teksti teksti, Renderöitävä kuvake) {
        super(scaleX, scaleY, offsetX, offsetY, teksti);
        this.id = id;
        this.nimi = nimi;
        this.teksti = teksti;
        this.kuvake = kuvake;
    }

    @Override
    public void renderöi(Shader shader, Window window) {
        this.window = window;
        this.renderöiKomponentti(shader, teksti, kuvake, window, scaleX, scaleY, 1, offsetX, offsetY, 0, 0, false, false);
    }

    private void renderöiKomponentti(Shader shader, Teksti teksti, Renderöitävä kuvake, Window window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ, int kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
        Matrix4f sijaintiMatriisi = new Matrix4f();
        sijaintiMatriisi.translate(offsetX + 0.06f, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        shader.setUniform("projection", sijaintiMatriisi);
        if (hover) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0.5f));
        else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        teksti.bind(0);
        EngineAssets.getModel(kääntöAsteet, xPeilaus, yPeilaus).render();

        if (kuvake != null) {
            sijaintiMatriisi = new Matrix4f();
            sijaintiMatriisi.translate(offsetX - 0.16f, offsetY, offsetZ);
            sijaintiMatriisi.scale(skaalaX/8f, skaalaY, skaalaZ);
            shader.setUniform("projection", sijaintiMatriisi);
            if (hover) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0.5f));
            else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
            kuvake.bind(0);
            EngineAssets.getModel(kääntöAsteet, xPeilaus, yPeilaus).render();
        }
    }
}
