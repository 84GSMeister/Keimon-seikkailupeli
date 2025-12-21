package keimo.seikkailupeli.objektit.kenttäkohteet.esine;

import keimo.TarkistettavatArvot;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.seikkailupeli.objektit.Pelaaja;

public abstract class Juoma extends Esine {
    
    float voltit;
    int känniKuolemattomuus;

    public Juoma(int sijX, int sijY) {
        super(sijX, sijY);
    }

    public float annaVoltit() {
        return voltit;
    }

    public int annaKännikuolemattomuus() {
        return känniKuolemattomuus;
    }

    @Override
    public String käytä() {
        Pelaaja.känninVoimakkuusFloat += voltit;
        Pelaaja.känniKuolemattomuus += känniKuolemattomuus;

        double kuolemanTodennäköisyys = 0.125 * Pelaaja.känninVoimakkuusFloat - 1;
        if (kuolemanTodennäköisyys < 0) {
            kuolemanTodennäköisyys = 0;
        }
        double d = Math.random();
        if (kuolemanTodennäköisyys > d) {
            TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.ALKOHOLIMYRKYTYS;
            Pelaaja.hp = 0;
        }
        return "Juoma käytetty";
    }
}
