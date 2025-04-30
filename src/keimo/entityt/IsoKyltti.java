package keimo.entityt;

import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.awt.Rectangle;

public class IsoKyltti extends Entity {
    
    public IsoKyltti(int sijX, int sijY) {
        super(sijX, sijY);
        super.nimi = "IsoKyltti";
        super.leveys = 768;
        super.korkeus = 192;
        super.hitbox = new Rectangle(sijX * tilenKoko +32, sijY * tilenKoko, leveys, korkeus);
        super.kuvaTiedosto = "tiedostot/kuvat/entity/kyltti_kuubileet.png";
        super.tekstuuri = new Tekstuuri(kuvaTiedosto);
        super.asetaTiedot();
    }
}
