package keimo.Utility;

import javax.swing.ImageIcon;
import java.awt.*;

/**
 * Legacya <br><br>
 * Tää pitäis poistaa ja korvata kaikki instanssit KäännettäväKuvake-objekteilla.
 */
public class SkaalattavaKuvake extends ImageIcon {
    
    Peilaus peilaus = Peilaus.PEILAA_X;
    float läpinäkyvyys = 1;
    public enum Peilaus {
        NORMAALI,
        PEILAA_X,
        PEILAA_Y;
    }

    /**
     * Legacya <br><br>
     * Tää pitäis poistaa ja korvata kaikki instanssit KäännettäväKuvake-objekteilla.
     */
    public SkaalattavaKuvake(String filename, Peilaus peilaus) {
        super(filename);
        this.peilaus = peilaus;
    }

    /**
     * Legacya <br><br>
     * Tää pitäis poistaa ja korvata kaikki instanssit KäännettäväKuvake-objekteilla.
     */
    public SkaalattavaKuvake(String filename, float läpinäkyvyys) {
        this.läpinäkyvyys = läpinäkyvyys;
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D)g.create();
        
        switch (peilaus) {
            case PEILAA_X:
                g2.translate(getIconWidth(), 0);
                g2.scale(-1, 1);
            break;
            case PEILAA_Y:
                g2.translate(0, getIconHeight());
                g2.scale(1, -1);
            break;
            default:
            break;
        }
        if (läpinäkyvyys < 1) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, läpinäkyvyys));
        }
        super.paintIcon(c, g2, x, y);
    }

}
