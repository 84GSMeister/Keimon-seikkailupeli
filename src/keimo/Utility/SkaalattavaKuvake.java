package keimo.Utility;

import javax.swing.ImageIcon;
import java.awt.*;

public class SkaalattavaKuvake extends ImageIcon {

    Peilaus peilaus = Peilaus.PEILAA_X;
    public enum Peilaus {
        PEILAA_X,
        PEILAA_Y;
    }

    public SkaalattavaKuvake(String filename, Peilaus peilaus) {
        super(filename);
        this.peilaus = peilaus;
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
        }
        super.paintIcon(c, g2, x, y);
    }

}
