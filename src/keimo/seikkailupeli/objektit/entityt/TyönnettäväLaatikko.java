package keimo.seikkailupeli.objektit.entityt;

import keimo.keimoengine.collision.Neliö;
import keimo.keimoengine.grafiikat.Tekstuuri;

public class TyönnettäväLaatikko extends LiikkuvaObjekti {
    
    public TyönnettäväLaatikko(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "Laatikko";
        super.leveys = 64;
        super.korkeus = 64;
        super.hitbox = new Neliö(sijX * leveys, sijY * korkeus, leveys, korkeus);
        super.sisäHitboxOffset = 8;
        super.sisäHitbox = new Neliö(sijX * leveys + sisäHitboxOffset, sijY * korkeus + sisäHitboxOffset, leveys - 2*sisäHitboxOffset, korkeus -2*sisäHitboxOffset);
        super.ulkoHitboxOffset = 8;
        super.ulkoHitbox = new Neliö(sijX * leveys - ulkoHitboxOffset, sijY * korkeus - ulkoHitboxOffset, leveys + 2*ulkoHitboxOffset, korkeus +2*ulkoHitboxOffset);
        super.tiedostonNimi = "tiedostot/kuvat/entity/työnnettävä_laatikko.png";
        super.tekstuuri = new Tekstuuri(tiedostonNimi);
        super.voiTyöntää = true;
        super.asetaTiedot();
    }
}
