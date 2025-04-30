package keimo.entityt;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.awt.Rectangle;

public class TestiEntity extends LiikkuvaObjekti {
    
    public TestiEntity(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "TestiEntity";
        super.leveys = 300;
        super.korkeus = 265;
        super.hitbox = new Rectangle(sijX * tilenKoko, sijY * tilenKoko, leveys, korkeus);
        super.sisäHitboxOffset = 8;
        super.sisäHitbox = new Rectangle(sijX * tilenKoko + sisäHitboxOffset, sijY * tilenKoko + sisäHitboxOffset, leveys - 2*sisäHitboxOffset, korkeus -2*sisäHitboxOffset);
        super.ulkoHitboxOffset = 8;
        super.ulkoHitbox = new Rectangle(sijX * tilenKoko - ulkoHitboxOffset, sijY * tilenKoko - ulkoHitboxOffset, leveys + 2*ulkoHitboxOffset, korkeus +2*ulkoHitboxOffset);
        super.kuvaTiedosto = "tiedostot/kuvat/entity/apu_pesukone.png";
        super.tekstuuri = new Tekstuuri(kuvaTiedosto);
        super.voiTyöntää = true;
        super.asetaTiedot();
    }
}
