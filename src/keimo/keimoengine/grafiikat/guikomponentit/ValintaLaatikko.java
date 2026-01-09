package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.keimoengine.assets.EngineAssets;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ValintaLaatikko extends Nappi {
    
    private boolean valittu;
    private Tekstuuri valittuTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/komponentit/checkbox_valittu.png");
    private Tekstuuri eiValittuTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/komponentit/checkbox_eivalittu.png");

    public ValintaLaatikko(boolean valittu, float scaleX, float scaleY, float offsetX, float offsetY) {
        super(scaleX, scaleY, offsetX, offsetY);
        this.valittu = valittu;
    }

    public ValintaLaatikko(boolean valittu, float scaleX, float scaleY, float offsetX, float offsetY, TooltipTeksti tooltipTeksti) {
        super(scaleX, scaleY, offsetX, offsetY, null, tooltipTeksti);
        this.valittu = valittu;
    }

    public boolean valittu() {
        return valittu;
    }

    public void valitse() {
        this.valittu = !this.valittu;
    }

    @Override
    public void renderöi(Shader shader, Ikkuna window) {
        super.window = window;
        renderöiKomponentti(shader, window, scaleX, scaleY, 1, offsetX, offsetY, 0);
    }

    private void renderöiKomponentti(Shader shader, Ikkuna window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ) {
        Matrix4f sijaintiMatriisi = new Matrix4f();
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        shader.setUniform("projection", sijaintiMatriisi);
        if (hover) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0.5f));
        else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        if (valittu) valittuTekstuuri.bind(0);
        else eiValittuTekstuuri.bind(0);
        EngineAssets.getModel().render();
    }
}
