package keimo.kenttäkohteet.esine;

import keimo.Pelaaja;
import keimo.TarkistettavatArvot;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.text.DecimalFormat;
import javax.swing.ImageIcon;

public class Jallupullo extends Esine {
    
    DecimalFormat df = new DecimalFormat("##.##");

    @Override
    public String käytä(){
        super.poista = true;
        Pelaaja.kuparit++;
        Pelaaja.känninVoimakkuusFloat += 4;
        Pelaaja.känniKuolemattomuus += 4000;
        ÄänentoistamisSäie.toistaSFX("pullo");

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
        // Jos joskus ajattelet, että työsi on turhaa, mieti sitä kaveria, joka kirjoittaa näitä.
        switch (sijamuoto) {
            case "nominatiivi":  return "Jallupullo";
            case "genetiivi":    return "Jallupullon";
            case "esiivi":       return "Jallupullona";
            case "partitiivi":   return "Jallupulloa";
            case "translatiivi": return "Jallupulloksi";
            case "inessiivi":    return "Jallupullossa";
            case "elatiivi":     return "Jallupullosta";
            case "illatiivi":    return "Jallupulloon";
            case "adessiivi":    return "Jallupullolla";
            case "ablatiivi":    return "Jallupullolta";
            case "allatiivi":    return "Jallupullolle";
            default:             return "Jallupullo";
        }
    }

    public Jallupullo(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Jallupullo";
        super.tiedostonNimi = "jallupullo.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Elämän eliksiiri.";
        super.käyttö = true;
        super.hinta = 18;
        super.asetaTiedot();
    }
}
