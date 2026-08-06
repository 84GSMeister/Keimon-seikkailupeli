package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.assets.EngineAssets;
import keimo.keimoengine.assets.GUITekstuurit;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * 
 * Liukusäädin a.k.a. Slider-komponentti
 */

public class Liukusäädin extends Nappi {

    private int min = 0;
    private int max = 100;
    private float range = max - min;
    private int arvo = 0;
    private boolean pystysuuntainen = false;
    private Renderöitävä pohjaTekstuuri = GUITekstuurit.annaTekstuuri("slider_pohja");
    private Renderöitävä nuppiTekstuuri = GUITekstuurit.annaTekstuuri("slider_nuppi");

    public Liukusäädin(float scaleX, float scaleY, float offsetX, float offsetY, boolean pystysuuntainen) {
        super(scaleX, scaleY, offsetX, offsetY);
        this.pystysuuntainen = pystysuuntainen;
    }

    public Liukusäädin(float scaleX, float scaleY, float offsetX, float offsetY, int arvo, boolean pystysuuntainen) {
        this(scaleX, scaleY, offsetX, offsetY, pystysuuntainen);
        this.arvo = arvo;
    }

    public void liikutaSäädintä(int hiiriX, int hiiriY) {
        if (window != null) {
            if (pystysuuntainen) {
                float hiiriAlue = maxY - minY;
                arvo = (int)((hiiriY-minY) * (range/hiiriAlue));
            }
            else {
                float hiiriAlue = maxX - minX;
                arvo = (int)((hiiriX-minX) * (range/hiiriAlue));
            }
        }
    }

    public void päivitäArvo(int arvo) {
        if (arvo >= min && arvo <= max) {
            this.arvo = arvo;
        }
    }

    public int annaArvo() {return arvo;}
    public int annaMin() {return min;}
    public int annaMax() {return max;}

    @Override
    public void renderöiTooltip(Shader shader, Ikkuna window) {
        if (tooltipTeksti == null) {
            tooltipTeksti = new TooltipTeksti("" + arvo);
        }
        if (hover && tooltipTeksti != null) {
            tooltipTeksti.päivitäTeksti("" + arvo);
            tooltipTeksti.renderöi(shader, window);
        }
    }

    @Override
    public void renderöi(Shader shader, Ikkuna window) {
        this.window = window;
        if (pystysuuntainen) {
            renderöiKomponentti(shader, pohjaTekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, 0, 0, 0, 90, false, false, false);
            float sliderSijY = -offsetY +((-range/2 + arvo)/range)*1.75f*scaleY;
            renderöiKomponentti(shader, nuppiTekstuuri, window, scaleX, scaleY/4f, 1, offsetX, -sliderSijY, 0, 0, 0, 0, 0, false, false, true);
        }
        else {
            renderöiKomponentti(shader, pohjaTekstuuri, window, scaleX, scaleY, 1, offsetX, offsetY, 0, 0, 0, 0, 0, false, false, false);
            float sliderSijX = offsetX +((-range/2 + arvo)/range)*1.75f*scaleX;
            renderöiKomponentti(shader, nuppiTekstuuri, window, scaleX/4f, scaleY, 1, sliderSijX, offsetY, 0, 0, 0, 0, 0, false, false, true);
        }
    }
    
    private void renderöiKomponentti(Shader shader, Renderöitävä tekstuuri, Ikkuna window, float skaalaX, float skaalaY, float skaalaZ, float offsetX, float offsetY, float offsetZ, float rotX, float rotY, float rotZ, int kääntöAsteet, boolean xPeilaus, boolean yPeilaus, boolean liikkuva) {
        Matrix4f sijaintiMatriisi = new Matrix4f();
        sijaintiMatriisi.translate(offsetX, offsetY, offsetZ);
        sijaintiMatriisi.scale(skaalaX, skaalaY, skaalaZ);
        Matrix4f rotaatioMatriisi = new Matrix4f();
        rotaatioMatriisi.rotate(new AxisAngle4f((float)Math.toRadians(rotX), 1, 0, 0));
        rotaatioMatriisi.rotate(new AxisAngle4f((float)Math.toRadians(rotY), 0, 1, 0));
        rotaatioMatriisi.rotate(new AxisAngle4f((float)Math.toRadians(rotZ), 0, 0, 1));
        sijaintiMatriisi.mul(rotaatioMatriisi);
        shader.asetaSijainti(sijaintiMatriisi);
        if (hover & liikkuva) shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0.5f));
        else shader.setUniform("subcolor", new Vector4f(0, 0, 0, 0f));
        tekstuuri.bind(0);
        EngineAssets.getModel(kääntöAsteet, xPeilaus, yPeilaus).render();
    }
}
