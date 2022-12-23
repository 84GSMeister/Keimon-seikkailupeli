import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.image.*;

public abstract class KenttäKohde {
    
    boolean määritettySijainti = false;
    int sijX;
    int sijY;

    protected enum Suunta {
        VASEN,
        OIKEA,
        YLÖS,
        ALAS;
    }

    Suunta suunta;
    
    //protected class KäännettäväKuvake extends JLabel {

        //@Override
        //protected void paintComponent(Graphics g) {
        
        ImageIcon rotateImageIcon(ImageIcon picture, Suunta suunta) {
            int w = picture.getIconWidth();
            int h = picture.getIconHeight();
            int type = BufferedImage.TYPE_INT_RGB;
            BufferedImage image = new BufferedImage(h, w, type);
            Graphics2D gr = image.createGraphics();
            switch (suunta) {
                case YLÖS:
                    gr.rotate(Math.toRadians(270));
                    break;
                case ALAS:
                    gr.rotate(Math.toRadians(180));
                    break;
                case OIKEA:
                    gr.rotate(Math.toRadians(180));
                    break;
                default:
                    break;
            }
            gr.drawImage(picture.getImage(), null, null);
            return picture;
        }

    //super.paintComponent(gr);
    //}
    
    protected String nimi;
    protected Icon kuvake;
    protected boolean tavoiteSuoritettu = false;

    boolean vaatiiPäivityksen = true;

    String katsomisTeksti = "vakioteksti";
    String katso() {
        return katsomisTeksti;
    }

    String kokeileEsinettä(Esine e) {
        return "Mitään ei tapahtunut!";
    }

    String annaNimi() {
        return nimi;
    }

    String annaNimiSijamuodossa(String sijamuoto) {
        return nimi;
    }

    Icon annaKuvake() {
        return kuvake;
    }

    //void asetaKuvake(ImageIcon kuvake, Suunta suunta) {
    //    this.kuvake = rotateImageIcon(kuvake, suunta);
    //}

    void asetaKuvake(ImageIcon kuvake, Suunta suunta) {
        this.kuvake = kuvake;
    }
}
