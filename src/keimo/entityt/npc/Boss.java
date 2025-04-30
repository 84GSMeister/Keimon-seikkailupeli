package keimo.entityt.npc;

import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.grafiikat.*;
import keimo.kenttäkohteet.esine.Ase;

import java.awt.Rectangle;

public class Boss extends Vihollinen {

    private Kuva idleTekstuuri = new Animaatio(15, "tiedostot/kuvat/npc/boss/boss_idle.gif");
    private Kuva spinTekstuuri = new Animaatio(15, "tiedostot/kuvat/npc/boss/boss_spin.gif");
    private Kuva attackTekstuuri = new Animaatio(15, "tiedostot/kuvat/npc/boss/boss_aggr.gif");
    private Kuva kuollutTekstuuri = new Tekstuuri("tiedostot/kuvat/npc/boss/boss_kuollut.png");

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
                ÄänentoistamisSäie.toistaSFX("Boss_death");
            }
            else {
                this.hurtAika = 10;
                ÄänentoistamisSäie.toistaSFX(this.annaNimi() + "_damage");
            }
        }
    }

    @Override
    public void kukista(String kukistusTapa) {
        super.kukista(kukistusTapa);
        this.tekstuuri = kuollutTekstuuri;
    }

    public void valitseTekstuuri() {
        switch (this.bossTila) {
            default: this.tekstuuri = idleTekstuuri; break;
            case PYÖRÄHDYS: this.tekstuuri = spinTekstuuri; break;
            case LATAUS: this.tekstuuri = attackTekstuuri; break;
            case HYÖKKÄYS: this.tekstuuri = attackTekstuuri; break;
        }
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

    public Boss(int sijX, int sijY, String[] ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = 20;
        super.vahinko = 2;
        super.nopeus = 4;
        super.tekeeVahinkoa = true;
        super.ampuu = true;
        super.ammusVahinko = 1;
        super.kuvaTiedosto = "tiedostot/kuvat/npc/boss64.png";
        super.tekstuuri = idleTekstuuri;
        super.leveys = 128;
        super.korkeus = 128;
        super.kilpiTehoaa = false;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox = new Rectangle(sijX * tilenKoko, sijY * tilenKoko, 128, 128);
        super.nimi = "Pomo";
        super.lisäOminaisuuksia = true;
        super.lisäOminaisuudet = ominaisuusLista;
        super.ominaisHuuto = "frans_cs";
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
