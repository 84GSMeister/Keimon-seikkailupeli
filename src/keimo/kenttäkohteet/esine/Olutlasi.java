package keimo.kenttäkohteet.esine;

import keimo.Pelaaja;
import keimo.TarkistettavatArvot;
import keimo.TarkistettavatArvot.PelinLopetukset;
import keimo.Säikeet.ÄänentoistamisSäie;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import java.text.DecimalFormat;
import javax.swing.ImageIcon;

public class Olutlasi extends Esine {
    DecimalFormat df = new DecimalFormat("##.##");

    @Override
    public String käytä(){
        super.poista = true;
        Pelaaja.känninVoimakkuusFloat += 0.6;
        Pelaaja.känniKuolemattomuus += 600;
        ÄänentoistamisSäie.toistaSFX("Juoman_kaato");

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
        return katso();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Olutlasi";
            case "genetiivi": return "Olutlasin";
            case "esiivi": return "Olutlasina";
            case "partitiivi": return "Olutlasia";
            case "translatiivi": return "Olutlasiksi";
            case "inessiivi": return "Olutlasissa";
            case "elatiivi": return "Olutlasista";
            case "illatiivi": return "Olutlasiin";
            case "adessiivi": return "Olutlasilla";
            case "ablatiivi": return "Olutlasilta";
            case "allatiivi": return "Olutlasille";
            default: return "Olutlasi";
        }
    }

    public Olutlasi(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Olutlasi";
        super.tiedostonNimi = "olutlasi.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Laadukasta Keimo-baarin hanaolutta";
        super.käyttö = true;
        super.hinta = 4.95;
        super.asetaTiedot();
    }
}
