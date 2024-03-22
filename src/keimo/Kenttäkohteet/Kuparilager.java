package keimo.Kenttäkohteet;

import keimo.Pelaaja;
import keimo.TarkistettavatArvot;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.TarkistettavatArvot.PelinLopetukset;

import javax.swing.ImageIcon;
import java.text.DecimalFormat;

public final class Kuparilager extends Esine {

    DecimalFormat df = new DecimalFormat("##.##");

    @Override
    public String käytä(){
        super.poista = true;
        Pelaaja.kuparit++;
        Pelaaja.känninVoimakkuusFloat += 0.4;
        ÄänentoistamisSäie.toistaSFX("tölkki");

        double kuolemanTodennäköisyys = 0.125 * Pelaaja.känninVoimakkuusFloat - 1;
        if (kuolemanTodennäköisyys < 0) {
            kuolemanTodennäköisyys = 0;
        }
        double d = Math.random();
        if (kuolemanTodennäköisyys > d) {
            TarkistettavatArvot.pelinLoppuSyy = PelinLopetukset.ALKOHOLIMYRKYTYS;
            Pelaaja.hp = 0;
        }
        System.out.println("Kuoleman todennäköisyys: " + kuolemanTodennäköisyys);
        return "Rahaa tulossa tölkeistä: " + df.format(0.15f * Pelaaja.kuparit) + "€";
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Kuparilager";
            case "genetiivi": return "Kuparilagerin";
            case "esiivi": return "Kuparilagerina";
            case "partitiivi": return "Kuparilageria";
            case "translatiivi": return "Kuparilageriksi";
            case "inessiivi": return "Kuparilagerissa";
            case "elatiivi": return "Kuparilagerista";
            case "illatiivi": return "Kuparilageriin";
            case "adessiivi": return "Kuparilagerilla";
            case "ablatiivi": return "Kuparilagerilta";
            case "allatiivi": return "Kuparilagerille";
            default: return "Kuparilager";
        }
    }

    public Kuparilager(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kuparilager";
        super.katsomisTeksti = "Uskollinen kupari";
        super.käyttö = true;
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kuparilager.png");
        super.hinta = 1.05;
        super.asetaTiedot();
    }
}
