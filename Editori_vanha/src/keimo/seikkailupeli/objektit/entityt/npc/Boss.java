package keimo.seikkailupeli.objektit.entityt.npc;

import keimo.Utility.collision.Neliö;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Ase;

import java.util.ArrayList;

public class Boss extends Vihollinen {

    public BossState bossTila = BossState.NORMAALI;
    public enum BossState {
        NORMAALI,
        PYÖRÄHDYS,
        LATAUS,
        HYÖKKÄYS,
        HYPER;
    }
    
    @Override
    public void vahingoita(Ase ase) {
        if (bossTila == BossState.NORMAALI) {
            this.hp -= ase.annaVahinko();
            System.out.println(this.annaNimi() + ", hp: " + this.annaHp());
            if (this.hp <= 0) {
                this.hurtAika = 240;
                this.kukista(ase.annaNimi());
            }
            else {
                this.hurtAika = 10;
            }
        }
    }

    @Override
    public void kukista(String kukistusTapa) {
        super.kukista(kukistusTapa);
    }

    public void valitseVahinko() {
        switch (this.bossTila) {
            default: this.vahinko = 2; break;
            case PYÖRÄHDYS: this.vahinko = 3; break;
            case LATAUS: this.vahinko = 4; break;
            case HYÖKKÄYS: this.vahinko = 4; break;
            case HYPER : this.vahinko = 5; break;
        }
    }

    public Boss(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = 20;
        super.vahinko = 2;
        super.nopeus = 4;
        super.tekeeVahinkoa = true;
        super.ampuu = true;
        super.ammusVahinko = 1;
        super.tiedostonNimi = "tiedostot/kuvat/npc/boss64.png";
        super.leveys = 128;
        super.korkeus = 128;
        super.kilpiTehoaa = false;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox = new Neliö(sijX * tilenKoko, sijY * tilenKoko, 128, 128);
        super.nimi = "Pomo";
        super.ominaisHuuto = "frans_cs";
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
