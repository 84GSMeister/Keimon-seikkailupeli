package keimo.entityt;

import keimo.Ruudut.PeliRuutu;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class TyönnettäväLaatikko extends LiikkuvaObjekti {
    
    public TyönnettäväLaatikko(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Laatikko";
        super.leveys = PeliRuutu.pelaajanKokoPx;
        super.korkeus = PeliRuutu.pelaajanKokoPx;
        super.hitbox = new Rectangle(sijX * leveys, sijY * korkeus, leveys, korkeus);
        super.sisäHitboxOffset = 8;
        super.sisäHitbox = new Rectangle(sijX * leveys + sisäHitboxOffset, sijY * korkeus + sisäHitboxOffset, leveys - 2*sisäHitboxOffset, korkeus -2*sisäHitboxOffset);
        super.ulkoHitboxOffset = 8;
        super.ulkoHitbox = new Rectangle(sijX * leveys - ulkoHitboxOffset, sijY * korkeus - ulkoHitboxOffset, leveys + 2*ulkoHitboxOffset, korkeus +2*ulkoHitboxOffset);
        super.kuvaTiedosto = "tiedostot/kuvat/entity/työnnettävä_laatikko.png";
        super.kuvake = new ImageIcon(kuvaTiedosto);
        super.tekstuuri = new Tekstuuri(kuvaTiedosto);
        super.voiTyöntää = true;
        super.asetaTiedot();
    }
}
