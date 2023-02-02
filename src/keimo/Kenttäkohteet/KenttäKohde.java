package keimo.Kenttäkohteet;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.*;
import java.awt.image.*;

public abstract class KenttäKohde {
    
    boolean määritettySijainti = false;
    int sijX;
    int sijY;
    boolean lisäOminaisuuksia = false;
    String[] lisäOminaisuudet;

    public boolean onkoMääritettySijainti() {
        return määritettySijainti;
    }

    public int annaSijX() {
        return sijX;
    }

    public int annaSijY() {
        return sijY;
    }

    public boolean onkolisäOminaisuuksia() {
        return lisäOminaisuuksia;
    }

    public String[] annalisäOminaisuudet() {
        return lisäOminaisuudet;
    }

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
    public boolean tavoiteSuoritettu = false;

    boolean vaatiiPäivityksen = true;

    String katsomisTeksti = "vakioteksti";
    public String katso() {
        return katsomisTeksti;
    }

    public String kokeileEsinettä(Esine e) {
        return "Mitään ei tapahtunut!";
    }

    public String annaNimi() {
        return nimi;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        return nimi;
    }

    public Icon annaKuvake() {
        return kuvake;
    }

    String tiedot = "";
    void asetaTiedot() {
        tiedot += "Nimi: " + this.annaNimi() + "\n";
        tiedot += "Satunnainen sijainti: " + (!this.määritettySijainti ? "Kyllä" : "Ei") + "\n";

        if (this instanceof Esine) {
            tiedot += "Tyyppi: Esine" + "\n";

            Esine esine = (Esine)this;
            if (esine.kenttäkäyttö) {
                tiedot += "Sopii käytettäväksi: ";
                for (String s : esine.sopiiKäytettäväksi) {
                    tiedot += s + ", ";
                }
                tiedot += "\n";
            }
            if (esine.yhdistettävä) {
                tiedot += "Sopii yhdistettäväksi: ";
                for (String s : esine.kelvollisetYhdistettävät) {
                    tiedot += s + ", ";
                }
                tiedot += "\n";
            }
        }
        else if (this instanceof Kiintopiste){
            tiedot += "Tyyppi: Kiintopiste" + "\n";
        }

        else if (this instanceof NPC_KenttäKohde) {
            tiedot += "Tyyppi: NPC" + "\n";
        }

        else if (this instanceof Warp) {
            tiedot += "Tyyppi: Warp" + "\n";
        }
    }
    
    public String annaTiedot() {
        return tiedot;
    }

    //void asetaKuvake(ImageIcon kuvake, Suunta suunta) {
    //    this.kuvake = rotateImageIcon(kuvake, suunta);
    //}

    void asetaKuvake(ImageIcon kuvake, Suunta suunta) {
        this.kuvake = kuvake;
    }
}
