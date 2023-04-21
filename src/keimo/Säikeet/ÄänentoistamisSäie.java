package keimo.Säikeet;

import keimo.*;

import java.io.File;
import jaco.mp3.player.MP3Player;
import java.util.HashMap;

public class ÄänentoistamisSäie extends Thread{
    
    public static MP3Player musiikkiSoitin;
    static HashMap<Integer, File> musiikkiVaihtoehdot = new HashMap<Integer, File>();
    static int musaValinta = 0;

    void luoMusaKartta() {
        musiikkiVaihtoehdot.put(0, new File("tiedostot/musat/udo_haukkuu_mario2_loop.mp3"));
        musiikkiVaihtoehdot.put(1, new File("tiedostot/musat/udo_haukkuu_90s.mp3"));
        musiikkiVaihtoehdot.put(2, new File("tiedostot/musat/udo_haukkuu_nyän.mp3"));
        musiikkiVaihtoehdot.put(3, new File("tiedostot/musat/udo_haukkuu_smw.mp3"));
        musiikkiVaihtoehdot.put(4, new File("tiedostot/musat/udo_haukkuu_rick.mp3"));
        musiikkiVaihtoehdot.put(5, new File("tiedostot/musat/udo_haukkuu_diiduu.mp3"));
        musiikkiVaihtoehdot.put(6, new File("tiedostot/musat/udo_haukkuu_wide_loop.mp3"));
    }

    void toistaMusiikki() {
        if (musiikkiSoitin != null) {
            if (!musiikkiSoitin.isStopped()) {
                musiikkiSoitin.stop();
            }
            try {
                musaValinta = PelinAsetukset.musiikkiValinta;
                musiikkiSoitin = new MP3Player(musiikkiVaihtoehdot.get(musaValinta));
                musiikkiSoitin.setRepeat(true);
                if (PelinAsetukset.musiikkiPäällä) {
                    musiikkiSoitin.play();
                }
            }
            catch (NullPointerException e) {
    
            }
        }
        else {
            try {
                musaValinta = PelinAsetukset.musiikkiValinta;
                musiikkiSoitin = new MP3Player(musiikkiVaihtoehdot.get(musaValinta));
                musiikkiSoitin.setRepeat(true);
                if (PelinAsetukset.musiikkiPäällä) {
                    musiikkiSoitin.play();
                }
            }
            catch (NullPointerException e) {
    
            }
        }
    }
    public static void toistaSFX(String ääni) {

        MP3Player ääniToistin;

        switch (ääni) {

            case "pelaaja_damage":
                ääniToistin = new MP3Player(new File("tiedostot/äänet/pelaaja_damage.mp3"));
                ääniToistin.play();
                break;
            case "pikkuvihu_damage":
                ääniToistin = new MP3Player(new File("tiedostot/äänet/pikkuvihu_damage.mp3"));
                ääniToistin.play();
                break;

            case "tölkki":
                ääniToistin = new MP3Player(new File("tiedostot/äänet/tölkki.mp3"));
                ääniToistin.play();
            default:
                break;
        }
    }

    public static void toistaTappoÄäni(String esineenNimi) {

        MP3Player ääniToistin;

        switch (esineenNimi) {

            case "Vesiämpäri":
                ääniToistin = new MP3Player(new File("tiedostot/äänet/vihollinen_ämpäröinti.mp3"));
                ääniToistin.play();
                break;
            case "Pesäpallomaila":
                ääniToistin = new MP3Player(new File("tiedostot/äänet/vihollinen_mukilointi.mp3"));
                ääniToistin.play();
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        luoMusaKartta();
        toistaMusiikki();
    }
}
