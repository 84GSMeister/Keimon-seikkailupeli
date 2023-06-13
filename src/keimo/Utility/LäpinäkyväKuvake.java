package keimo.Utility;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

public class LäpinäkyväKuvake extends ImageIcon {
    
    private float läpinäkyvyys = 0.5f;

    public LäpinäkyväKuvake(String filename, float läpinäkyvyys) {
        super(filename);
        this.läpinäkyvyys = läpinäkyvyys;
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, läpinäkyvyys));
        super.paintIcon(c, g2d, x, y);
    }

}
