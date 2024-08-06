package keimo.NPCt;

import java.awt.Rectangle;
import javax.swing.ImageIcon;

import keimo.Ruudut.PeliRuutu;

public class TyönnettäväLaatikko extends LiikkuvaObjekti {
    
    public TyönnettäväLaatikko(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Työnnettävä laatikko";
        super.hitbox = new Rectangle(sijX * PeliRuutu.pelaajanKokoPx, sijY * PeliRuutu.pelaajanKokoPx, PeliRuutu.pelaajanKokoPx, PeliRuutu.pelaajanKokoPx);
        super.sisäHitboxOffset = 8;
        super.sisäHitbox = new Rectangle(sijX * PeliRuutu.pelaajanKokoPx + sisäHitboxOffset, sijY * PeliRuutu.pelaajanKokoPx + sisäHitboxOffset, PeliRuutu.pelaajanKokoPx - 2*sisäHitboxOffset, PeliRuutu.pelaajanKokoPx -2*sisäHitboxOffset);
        super.ulkoHitboxOffset = 8;
        super.ulkoHitbox = new Rectangle(sijX * PeliRuutu.pelaajanKokoPx - ulkoHitboxOffset, sijY * PeliRuutu.pelaajanKokoPx - ulkoHitboxOffset, PeliRuutu.pelaajanKokoPx + 2*ulkoHitboxOffset, PeliRuutu.pelaajanKokoPx +2*ulkoHitboxOffset);
        super.kuvake = new ImageIcon("tiedostot/kuvat/entity/työnnettävä_laatikko.png");
    }
}
