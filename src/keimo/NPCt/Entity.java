package keimo.NPCt;

import keimo.PääIkkuna;
import keimo.TarkistettavatArvot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class Entity extends JLabel {

    protected String nimi = "";
    public int sijX;
    public int sijY;
    protected int alkuSijX;
    protected int alkuSijY;
    public Rectangle hitbox;
    protected Icon kuvake;

    public String annaNimi() {
        return nimi;
    }

    public Icon annaKuvake() {
        return kuvake;
    }
    
    public SuuntaVasenOikea suuntaVasenOikea = SuuntaVasenOikea.OIKEA;
    public enum SuuntaVasenOikea {
        VASEN,
        OIKEA;
    }

    public SuuntaDiagonaali suuntaDiagonaali = SuuntaDiagonaali.OIKEA;
    public enum SuuntaDiagonaali {
        VASEN,
        OIKEA,
        ALAS,
        YLÖS,
        YLÄVASEN,
        ALAVASEN,
        YLÄOIKEA,
        ALAOIKEA;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (PääIkkuna.reunatNäkyvissä) {
            g.drawRect(0, 0, (int)hitbox.getWidth(), (int)hitbox.getHeight());
            g.setColor(Color.GREEN);
            g.fillRect(0, 0, (int)hitbox.getWidth(), (int)hitbox.getHeight());
            if (this instanceof LiikkuvaObjekti) {
                LiikkuvaObjekti obj = (LiikkuvaObjekti)this;
                g.drawRect(obj.sisäHitboxOffset, obj.sisäHitboxOffset, (int)obj.sisäHitbox.getWidth(), (int)obj.sisäHitbox.getHeight());
                g.setColor(Color.RED);
                g.fillRect(obj.sisäHitboxOffset, obj.sisäHitboxOffset, (int)obj.sisäHitbox.getWidth(), (int)obj.sisäHitbox.getHeight());
            }
        }
    }

    Entity(int sijX, int sijY) {
        this.sijX = sijX;
        this.sijY = sijY;
        this.alkuSijX = sijX;
        this.alkuSijY = sijY;
    }
}
