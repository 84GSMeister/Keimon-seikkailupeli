package keimo.entityt;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.awt.Rectangle;

public class TyönnettäväLaatikko extends LiikkuvaObjekti {
    
    public TyönnettäväLaatikko(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Laatikko";
        super.leveys = 64;
        super.korkeus = 64;
        super.hitbox = new Rectangle(sijX * leveys, sijY * korkeus, leveys, korkeus);
        super.sisäHitboxOffset = 8;
        super.sisäHitbox = new Rectangle(sijX * leveys + sisäHitboxOffset, sijY * korkeus + sisäHitboxOffset, leveys - 2*sisäHitboxOffset, korkeus -2*sisäHitboxOffset);
        super.ulkoHitboxOffset = 8;
        super.ulkoHitbox = new Rectangle(sijX * leveys - ulkoHitboxOffset, sijY * korkeus - ulkoHitboxOffset, leveys + 2*ulkoHitboxOffset, korkeus +2*ulkoHitboxOffset);
        super.kuvaTiedosto = "tiedostot/kuvat/entity/työnnettävä_laatikko.png";
        super.tekstuuri = new Tekstuuri(kuvaTiedosto);
        super.voiTyöntää = true;
        super.asetaTiedot();
    }
}
