package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.assets.GUITekstuurit;
import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;

public class TooltipTeksti {

    protected float scaleX;
    protected float scaleY;
    protected float offsetX;
    protected float offsetY;
    private static Ikkuna window1;
    private String teksti;
    private int leveys;
    private int korkeus;

    protected Renderöitävä tooltipValikkoTekstuuri = GUITekstuurit.annaTekstuuri("tooltip_pohja");
    protected LabelKomponentti tooltipPohjaLabel;
    protected Teksti tooltipTeksti;
    protected LabelKomponentti tooltipTekstiLabel;

    public TooltipTeksti(String teksti) {
        this.scaleX = 0.2f;
        this.scaleY = 0.05f;
        this.offsetX = 0;
        this.offsetY = 0;
        this.teksti = teksti;
        this.leveys = 600;
        this.korkeus = 48;
        tooltipTekstiLabel = new LabelKomponentti(0.2f, 0.1f, offsetX, offsetY, tooltipTeksti);
        tooltipPohjaLabel = new LabelKomponentti(0.2f, 0.1f, offsetX, offsetY, tooltipValikkoTekstuuri);
    }

    public TooltipTeksti(String teksti, int leveys, int korkeus) {
        this.scaleX = 0.2f;
        this.scaleY = 0.05f;
        this.offsetX = 0;
        this.offsetY = 0;
        this.teksti = teksti;
        this.leveys = leveys;
        this.korkeus = korkeus;
        tooltipTekstiLabel = new LabelKomponentti(0.2f, 0.1f, offsetX, offsetY, tooltipTeksti);
        tooltipPohjaLabel = new LabelKomponentti(0.2f, 0.1f, offsetX, offsetY, tooltipValikkoTekstuuri);
    }

    public void päivitäTeksti(String uusiTeksti) {
        if (tooltipTeksti != null) {
            tooltipTeksti.päivitäTeksti(uusiTeksti);
            this.teksti = uusiTeksti;
        }
    }

    public void päivitäSijainti(int hiiriX, int hiiriY) {
        if (window1 != null) {
            if (window1.getWidth() > 0 && window1.getHeight() > 0) {
                float newOffsetX = (hiiriX - (window1.getWidth()/2f))/(window1.getWidth()/2f) + 0.25f;
                float newOffsetY = -(hiiriY - (window1.getHeight()/2f))/(window1.getHeight()/2f) - 0.1f;
                if (newOffsetX > 0.8) newOffsetX = 0.8f;
                if (newOffsetY < -0.95) newOffsetY = -0.95f;
                tooltipPohjaLabel.muutaKokoa(scaleX, scaleY, newOffsetX, newOffsetY);
                tooltipTekstiLabel.muutaKokoa(scaleX, scaleY, newOffsetX, newOffsetY);
            }
        }
    }
    
    public void renderöi(Shader shader, Ikkuna window) {
        window1 = window;
        tooltipPohjaLabel.renderöi(shader, window);
        if (tooltipTeksti == null) {
            tooltipTeksti = new Teksti(teksti, Väri.white, leveys, korkeus);
            tooltipTekstiLabel.päivitäSisältö(tooltipTeksti);
        }
        tooltipTeksti.päivitäTeksti(teksti);
        tooltipTekstiLabel.renderöi(shader, window);
    }
}
