package keimo.seikkailupeli.menu.editori.gui;

import keimo.keimoengine.grafiikat.Shader;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.guikomponentit.TooltipTeksti;
import keimo.keimoengine.ikkuna.Ikkuna;

import java.awt.Color;

public class TileTooltip extends TooltipTeksti {
    private Ikkuna window1;
    private String teksti = "X, Y";

    public TileTooltip(String teksti) {
        super(teksti);
        this.scaleX = 0.1f;
        this.scaleY = 0.05f;
        this.tooltipTeksti = new Teksti(teksti, Color.white, 500, 48);
        this.tooltipTekstiLabel = new StaattinenKomponentti(this.scaleX, this.scaleY, this.offsetX, this.offsetY, tooltipTeksti);
    }

    @Override
    public void päivitäSijainti(int hiiriX, int hiiriY) {
        if (window1 != null) {
            if (window1.getWidth() > 0 && window1.getHeight() > 0) {
                float offsetX = (hiiriX - (window1.getWidth()/2f))/(window1.getWidth()/2f) + 0.15f;
                float offsetY = -(hiiriY - (window1.getHeight()/2f))/(window1.getHeight()/2f) - 0.10f;
                if (offsetX > 0.95) offsetX = 0.95f;
                if (offsetY < -0.95) offsetY = -0.95f;
                tooltipTekstiLabel.muutaKokoa(this.scaleX, this.scaleY, offsetX, offsetY);
            }
        }
    }
    
    @Override
    public void renderöi(Shader shader, Ikkuna window) {
        window1 = window;
        this.tooltipTeksti.päivitäTeksti(teksti);
        this.tooltipTekstiLabel.renderöi(shader, window);
    }

    public void päivitäTeksti(String uusiTeksti) {
        this.teksti = uusiTeksti;
    }

    public void päivitäVäri(Color väri) {
        this.tooltipTeksti.päivitäTeksti(teksti, 0, 30, väri);
    }
}
