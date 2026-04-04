package keimo.seikkailupeli.objektit.entityt;

import keimo.keimoengine.collision.Neliö;
import keimo.seikkailupeli.assets.Assets;

public class IsoLaatikko extends LiikkuvaObjekti {
    
    public IsoLaatikko(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "IsoLaatikko";
        super.leveys = 128;
        super.korkeus = 128;
        super.hitbox = new Neliö(sijX * tilenKoko, sijY * tilenKoko, leveys, korkeus);
        super.sisäHitboxOffset = 8;
        super.sisäHitbox = new Neliö(sijX * tilenKoko + sisäHitboxOffset, sijY * tilenKoko + sisäHitboxOffset, leveys - 2*sisäHitboxOffset, korkeus -2*sisäHitboxOffset);
        super.ulkoHitboxOffset = 8;
        super.ulkoHitbox = new Neliö(sijX * tilenKoko - ulkoHitboxOffset, sijY * tilenKoko - ulkoHitboxOffset, leveys + 2*ulkoHitboxOffset, korkeus +2*ulkoHitboxOffset);
        super.tiedostonNimi = "tiedostot/kuvat/entity/iso_laatikko.png";
        super.tekstuuri = Assets.annaTekstuuri("laatikko_iso");
        super.voiTyöntää = true;
        super.asetaTiedot();
    }
}
