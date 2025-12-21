package keimo.seikkailupeli.objektit.entityt;

import keimo.keimoengine.collision.Neliö;
import keimo.keimoengine.grafiikat.Tekstuuri;

public class TestiEntity extends LiikkuvaObjekti {
    
    public TestiEntity(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "TestiEntity";
        super.leveys = 300;
        super.korkeus = 265;
        super.hitbox = new Neliö(sijX * tilenKoko, sijY * tilenKoko, leveys, korkeus);
        super.sisäHitboxOffset = 8;
        super.sisäHitbox = new Neliö(sijX * tilenKoko + sisäHitboxOffset, sijY * tilenKoko + sisäHitboxOffset, leveys - 2*sisäHitboxOffset, korkeus -2*sisäHitboxOffset);
        super.ulkoHitboxOffset = 8;
        super.ulkoHitbox = new Neliö(sijX * tilenKoko - ulkoHitboxOffset, sijY * tilenKoko - ulkoHitboxOffset, leveys + 2*ulkoHitboxOffset, korkeus +2*ulkoHitboxOffset);
        super.tiedostonNimi = "tiedostot/kuvat/entity/apu_pesukone.png";
        super.tekstuuri = new Tekstuuri(tiedostonNimi);
        super.voiTyöntää = true;
        super.asetaTiedot();
    }
}
