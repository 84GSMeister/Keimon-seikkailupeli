package keimo;
import java.io.File;
import jaco.mp3.player.MP3Player;
import java.util.HashMap;

public class ÄänentoistamisSäie extends Thread{
    
    static MP3Player musiikkiSoitin;
    static HashMap<Integer, File> musiikkiVaihtoehdot = new HashMap<Integer, File>();
    static int musaValinta = 0;

    void luoMusaKartta() {
        musiikkiVaihtoehdot.put(0, new File("tiedostot/musat/udo_haukkuu_mario2.mp3"));
        musiikkiVaihtoehdot.put(1, new File("tiedostot/musat/udo_haukkuu_90s.mp3"));
        musiikkiVaihtoehdot.put(2, new File("tiedostot/musat/udo_haukkuu_nyän.mp3"));
        musiikkiVaihtoehdot.put(3, new File("tiedostot/musat/udo_haukkuu_smw.mp3"));
        musiikkiVaihtoehdot.put(4, new File("tiedostot/musat/udo_haukkuu_rick.mp3"));
        musiikkiVaihtoehdot.put(5, new File("tiedostot/musat/udo_haukkuu_diiduu.mp3"));
        musiikkiVaihtoehdot.put(6, new File("tiedostot/musat/udo_haukkuu_wide.mp3"));
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

    static void toistaSFX(String ääni) {

        MP3Player ääniToistin;

        switch (ääni) {

            case "damage":
                ääniToistin = new MP3Player(new File("tiedostot/äänet/damage.mp3"));
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
