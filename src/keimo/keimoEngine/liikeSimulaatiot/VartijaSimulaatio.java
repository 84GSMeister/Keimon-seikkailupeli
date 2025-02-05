package keimo.keimoEngine.liikeSimulaatiot;

import keimo.Pelaaja;
import keimo.Utility.Käännettävä.Suunta;
import keimo.entityt.npc.Vartija;

public class VartijaSimulaatio {
    
    public static void simuloiVartija(Vartija vartija) {
        if (Pelaaja.ostostenHintaYhteensä > 0 && (Pelaaja.sijX >= vartija.x1 && Pelaaja.sijX < vartija.x2 && Pelaaja.sijY >= vartija.y1 && Pelaaja.sijY < vartija.y2)) {
            vartija.liikkuu = true;
            vartija.tekeeVahinkoa = true;
        }
        else {
            vartija.liikkuu = false;
            vartija.tekeeVahinkoa = false;
        }
        if (vartija.liikkuu) {
            int etäisyysX = (int)(Pelaaja.hitbox.getCenterX() - vartija.hitbox.getCenterX());
            int etäisyysY = (int)(Pelaaja.hitbox.getCenterY() - vartija.hitbox.getCenterY());
            int etäisyysXits = (int)Math.abs(etäisyysX);
            int etäisyysYits = (int)Math.abs(etäisyysY);
            if (etäisyysXits > etäisyysYits) {
                if (etäisyysX < 0) {
                    vartija.kokeileLiikkumista(Suunta.VASEN);
                }
                else {
                    vartija.kokeileLiikkumista(Suunta.OIKEA);
                }
            }
            else {
                if (etäisyysY < 0) {
                    vartija.kokeileLiikkumista(Suunta.YLÖS);
                }
                else {
                    vartija.kokeileLiikkumista(Suunta.ALAS);
                }
            }
        }
    }
}
