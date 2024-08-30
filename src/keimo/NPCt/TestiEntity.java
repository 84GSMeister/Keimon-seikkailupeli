package keimo.NPCt;

import java.awt.Rectangle;
import javax.swing.ImageIcon;

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
        super.kuvake = new ImageIcon("tiedostot/kuvat/entity/apu_pesukone.png");
        super.voiTyöntää = true;
        super.asetaTiedot();
    }
}
