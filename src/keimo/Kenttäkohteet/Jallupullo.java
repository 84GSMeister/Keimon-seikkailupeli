package keimo.Kenttäkohteet;

import keimo.Pelaaja;
import keimo.TarkistettavatArvot;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.Säikeet.ÄänentoistamisSäie;

import java.text.DecimalFormat;
import javax.swing.ImageIcon;

public class Jallupullo extends Esine {
    
    DecimalFormat df = new DecimalFormat("##.##");

    @Override
    public String käytä(){
        super.poista = true;
        Pelaaja.kuparit++;
        Pelaaja.känninVoimakkuusFloat += 4;
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
            case "nominatiivi": return "Jallupullo";
            case "genetiivi": return "Jallupullon";
            case "esiivi": return "Jallupullona";
            case "partitiivi": return "Jallupulloa";
            case "translatiivi": return "Jallupulloksi";
            case "inessiivi": return "Jallupullossa";
            case "elatiivi": return "Jallupullosta";
            case "illatiivi": return "Jallupulloon";
            case "adessiivi": return "Jallupullolla";
            case "ablatiivi": return "Jallupullolta";
            case "allatiivi": return "Jallupullolle";
            default: return "Jallupullo";
        }
    }

    public Jallupullo(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Jallupullo";
        super.katsomisTeksti = "Elämän eliksiiri.";
        super.käyttö = true;
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/jallupullo.png");
        super.hinta = 18;
        super.asetaTiedot();
    }
}
