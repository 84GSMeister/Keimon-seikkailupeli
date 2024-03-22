package keimo.NPCt;

import javax.swing.ImageIcon;

import keimo.Kenttäkohteet.Esine;
import keimo.Ruudut.PeliRuutu;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.Utility.SkaalattavaKuvake;

public class Boss extends Vihollinen {

    @Override
    public void vahingoita(Esine esine) {
        this.hp -= esine.annaVahinko();
        System.out.println(this.annaNimi() + ", hp: " + this.annaHp());
        if (this.hp <= 0) {
            this.kukista(esine.annaNimi());
            ÄänentoistamisSäie.toistaSFX("Boss_death");
        }
        else {
            this.hurtAika = 10;
            ÄänentoistamisSäie.toistaSFX(this.annaNimi() + "_damage");
        }
    }

    @Override
    public void valitseKuvake() {
        if (this.hurtAika > 0) {
            this.kuvake = new ImageIcon("tiedostot/kuvat/npc/boss_hurt.png");
        }
        else {
            switch (this.suuntaVasenOikea) {
                case VASEN:
                    this.kuvake = new ImageIcon("tiedostot/kuvat/npc/boss.png");
                break;
                case OIKEA:
                    this.kuvake = new SkaalattavaKuvake("tiedostot/kuvat/npc/boss.png", SkaalattavaKuvake.Peilaus.PEILAA_X);
                break;
            }
        }
    }

    Boss(int sijX, int sijY, String[] ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = 20;
        super.vahinko = 2;
        super.nopeus = 4;
        super.tekeeVahinkoa = true;
        super.ampuu = true;
        super.ammusVahinko = 1;
        super.kuvake = new ImageIcon("tiedostot/kuvat/npc/boss.png");
        super.kilpiTehoaa = false;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox.setLocation(sijX * PeliRuutu.pelaajanKokoPx, sijY * PeliRuutu.pelaajanKokoPx);
        super.nimi = "Pomo";
        super.lisäOminaisuuksia = true;
        super.lisäOminaisuudet = ominaisuusLista;
        super.ominaisHuuto = "frans_cs";
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
