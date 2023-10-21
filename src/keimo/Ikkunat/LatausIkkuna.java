package keimo.Ikkunat;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LatausIkkuna {

    static Frame ikkuna;

    public static void luoLatausIkkuna() {
        
        ImgComp latausKuvake = new ImgComp("tiedostot/kuvat/menu/lataus/latauskuvake.png");
        latausKuvake.setPreferredSize(new Dimension(300, 60));
        Label latausTeksti = new Label("Käynnistetään peliä");

        Panel paneli = new Panel(new BorderLayout());
        paneli.add(latausTeksti, BorderLayout.NORTH);
        paneli.add(latausKuvake, BorderLayout.CENTER);

        ikkuna = new Frame("Keimon seikkailupeli");
        //ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        try {
            ikkuna.setIconImage(ImageIO.read(new File("tiedostot/kuvat/pelaaja_og.png")));
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        ikkuna.setBounds(0, 0, 320, 120);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.add(paneli, BorderLayout.CENTER);
        ikkuna.setLocationRelativeTo(null);
        ikkuna.revalidate();
        ikkuna.repaint();
    }

    public static void palautaLatausIkkuna() {
        ikkuna.setVisible(true);
    }

    public static void suljeLatausIkkuna() {
        ikkuna.setVisible(false);
    }

    static class ImgComp extends Component {  
        BufferedImage ig;  
        public void paint(Graphics p) {  
            p.drawImage(ig, 0, 0, null);  
        }  
        public ImgComp(String filepath) {  
            try {  
                ig = ImageIO.read(new File(filepath));  
            }   
            catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
      /* Sets the size of the image */  
        public Dimension getPreferredSize() {  
            if (ig == null) {  
                return new Dimension(120,200);  
            }   
            else {  
                return new Dimension(ig.getWidth(), ig.getHeight());  
            }  
        }
    }
}
