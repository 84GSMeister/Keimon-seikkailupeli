package keimo.seikkailupeli.äänet;

import keimo.keimoengine.äänet.PeliääniToistin;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.Assets;

import java.io.File;
import java.lang.Thread.State;

public class Musat {
    protected static String nytSoi;
    private static Object äänisäikeenLukko = new Object();
    private static Thread musasäie;
    private static File musaSäieTiedosto;
    private static double musasäieVolyymi = 0;
    private static double musasäiePan = 0;
    private static float musasäieSampleRate = 44100;
    private static boolean musasäieLoop = false;
    private static int musaSäieLoopKohta = 0;
    private static boolean musaSäieTakaperin = false;

    public static Thread luoMusasäie() {
        musasäie = new Thread() {
            @Override
            public void run() {
                synchronized(äänisäikeenLukko) {
                    try {
                        if (PelinAsetukset.äänetPäällä) {
                            double toistoVolyymi = musasäieVolyymi * PelinAsetukset.ääniVolyymi;
                            PeliääniToistin.toistaPelimusa(musasäieSampleRate, musaSäieTiedosto, toistoVolyymi, musasäiePan, musasäieLoop, musaSäieLoopKohta, musaSäieTakaperin);
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Ääntä ei voitu toistaa");
                        e.printStackTrace();
                    }
                }
            }
        };
        return musasäie;
    }

    private static int valitsePeliMusanLoopKohta(String musa, int sampleRate) {
        int loopKohta = 0;
        double loopKohtaMs = 0;
        switch (musa) {
            case "keimo_overworld.ogg":        loopKohtaMs = 48_000; break;
            case "keimo_puisto.ogg":           loopKohtaMs = 60_000; break;
            case "keimo_sad_tarina.ogg":       loopKohtaMs = 14_769; break;
            case "keimo_taistelu_boss.ogg":    loopKohtaMs = 1_600; break;
            case "keimo_valikko.mp3":          loopKohtaMs = 6_400; break;
            case "keimo_metsä.ogg":            loopKohtaMs = 8_350; break;
            case "keimo_baari.ogg":            loopKohtaMs = 6_857; break;
            case "keimo_koti.ogg":             loopKohtaMs = 7_680; break;
            case "keimo_temppeli.ogg":         loopKohtaMs = 17_455; break;
            case "keimo_kauppa.ogg":           loopKohtaMs = 16_700; break;
            case "keimo_kuu.ogg":              loopKohtaMs = 27_429; break;
            case "keimo_välitarina.ogg":       loopKohtaMs = 29_536; break;
            case "minipeli_pong.ogg":          loopKohtaMs = 6_400; break;
            case "minipeli_kasino.mid":        loopKohtaMs = 0; break;
            case "0_udo_haukkuu_90s.ogg":      loopKohtaMs = 0; break;
            case "1_udo_haukkuu_diiduu.ogg":   loopKohtaMs = 0; break;
            case "2_udo_haukkuu_kylie.ogg":    loopKohtaMs = 0; break;
            case "3_udo_haukkuu_mario2.ogg":   loopKohtaMs = 5_333; break;
            case "4_udo_haukkuu_nyän.ogg":     loopKohtaMs = 28_800; break;
            case "5_udo_haukkuu_smw.ogg":      loopKohtaMs = 0; break;
            case "6_udo_haukkuu_rick.ogg":     loopKohtaMs = 1_316; break;
            case "7_udo_haukkuu_wide.ogg":     loopKohtaMs = 24_000; break;
            case null, default:                loopKohtaMs = 0; break;
        }
        loopKohta = (int)((loopKohtaMs/1000d) * sampleRate);
        return loopKohta;
    }

    private static void asetaArvotSäikeelle(File tiedosto, double volume, double pan, float sampleRate, boolean loop, int loopKohta, boolean takaperin) {
        musaSäieTiedosto = tiedosto;
        musasäieVolyymi = volume;
        musasäiePan = pan;
        musasäieSampleRate = sampleRate;
        musasäieLoop = loop;
        musaSäieLoopKohta = loopKohta;
        musaSäieTakaperin = takaperin;
    }

    /**
     * @param musa musan nimi
     */
    public static void toistaPeliMusa(String musa) {
        File musaTiedosto = Assets.annaMusa(musa);
        if (nytSoi == null || !nytSoi.equals(musa)) {
            nytSoi = musa;
            toistaPeliMusa(musaTiedosto, 1, 0, 44100, true, false);
        }
    }

    /**
     * @param musa musan nimi
     */
    public static void toistaPeliMusa(File musaTiedosto, double volume, double pan, float sampleRate, boolean loop, boolean takaperin) {
        synchronized(äänisäikeenLukko) {
            try {
                int loopKohta = valitsePeliMusanLoopKohta(musaTiedosto.getName(), 44100);
                if (musasäie == null || musasäie.getState() == State.TERMINATED) {
                    musasäie = luoMusasäie();
                }
                if (musasäie != null) {
                    if (musasäie.getState() != State.TERMINATED && musasäie.getState() != State.RUNNABLE) {
                        System.out.println();
                        asetaArvotSäikeelle(musaTiedosto, volume, pan, sampleRate, loop, loopKohta, takaperin);
                        musasäie.start();
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Musiikkia ei voitu toistaa");
                e.printStackTrace();
            }
        }
    }

    public static void suljeMusa() {
        synchronized(äänisäikeenLukko) {
            nytSoi = null;
            PeliääniToistin.suljeMusa();
        }
    }

    public static void asetaMusanVolyymi(double volyymi) {
        synchronized(äänisäikeenLukko) {
            PelinAsetukset.musaVolyymi = volyymi;
            PeliääniToistin.asetaMusanVolyymi(volyymi);
        }
    }
}
