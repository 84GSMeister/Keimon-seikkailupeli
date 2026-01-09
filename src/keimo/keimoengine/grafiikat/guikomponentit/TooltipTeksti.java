package keimo.keimoengine.grafiikat.guikomponentit;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.Tekstuuri;
import keimo.keimoengine.ikkuna.Ikkuna;

import java.awt.Color;

public class TooltipTeksti {
    protected float scaleX;
    protected float scaleY;
    protected float offsetX;
    protected float offsetY;
    private static Ikkuna window1;
    private String teksti;

    protected Tekstuuri tooltipValikkoTekstuuri = new Tekstuuri("tiedostot/kuvat/gui/komponentit/popup_teksti_pohja.png");

    protected StaattinenKomponentti tooltipPohjaLabel;
    protected Teksti tooltipTeksti;
    protected StaattinenKomponentti tooltipTekstiLabel;

    public TooltipTeksti(String teksti) {
        this.scaleX = 0.2f;
        this.scaleY = 0.05f;
        this.offsetX = 0;
        this.offsetY = 0;
        this.teksti = teksti;
        tooltipTeksti = new Teksti(teksti, Color.white, 600, 48);
        tooltipTekstiLabel = new StaattinenKomponentti(0.2f, 0.1f, offsetX, offsetY, tooltipTeksti);
        tooltipPohjaLabel = new StaattinenKomponentti(0.2f, 0.1f, offsetX, offsetY, tooltipValikkoTekstuuri);
    }

    public TooltipTeksti(String teksti, int leveys, int korkeus) {
        this.scaleX = 0.2f;
        this.scaleY = 0.05f;
        this.offsetX = 0;
        this.offsetY = 0;
        this.teksti = teksti;
        tooltipTeksti = new Teksti(teksti, Color.white, leveys, korkeus);
        tooltipTekstiLabel = new StaattinenKomponentti(0.2f, 0.1f, offsetX, offsetY, tooltipTeksti);
        tooltipPohjaLabel = new StaattinenKomponentti(0.2f, 0.1f, offsetX, offsetY, tooltipValikkoTekstuuri);
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
        tooltipTeksti.päivitäTeksti(teksti);
        tooltipTekstiLabel.renderöi(shader, window);
    }
}
