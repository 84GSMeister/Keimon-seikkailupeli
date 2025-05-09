package keimo.keimoEngine.äänet;

import keimo.PelinAsetukset;
import keimo.keimoEngine.assets.Assets;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Musat {
    protected static Clip musaClip;
    protected static String nytSoi;
    private static Object äänisäikeenLukko = new Object();

    private static int valitsePeliMusanLoopKohta(String musa, int sampleRate) {
        int loopKohta = 0;
        double loopKohtaMs = 0;
        switch (musa) {
            case "overworld":   loopKohtaMs = 48_000; break;
            case "puisto":      loopKohtaMs = 60_000; break;
            case "tarina":      loopKohtaMs = 14_769; break;
            case "boss":        loopKohtaMs = 1_600; break;
            case "valikko":     loopKohtaMs = 6_400; break;
            case "metsä":       loopKohtaMs = 8_350; break;
            case "baari":       loopKohtaMs = 6_857; break;
            case "koti":        loopKohtaMs = 7_680; break;
            case "temppeli":    loopKohtaMs = 17_455; break;
            case "kauppa":      loopKohtaMs = 16_700; break;
            case "kuu":         loopKohtaMs = 27_429; break;
            case null, default: loopKohtaMs = 0; break;
        }
        loopKohta = (int)((loopKohtaMs/1000d) * sampleRate);
        return loopKohta;
    }

    /**
     * Todo: Korjaa pelin pysähtyminen musan feidatessa.
     * Ehkä joku pakotettu latausruutu jolloin säie ehtii viimeistellä toimintansa.
     * @param musa
     */
    public static void toistaPeliMusa(String musa) {
        synchronized(äänisäikeenLukko) {
            try {
                if (nytSoi == null || !nytSoi.equals(musa)) {
                    nytSoi = musa;
                    if (musaClip != null) {
                        musaClip.stop();
                    }
                    else {
                        musaClip = AudioSystem.getClip();
                    }
                    AudioInputStream audioInputStream = null;
                    String tiedostotyyppi = "";
                    String tiedostonNimi = "";
                    String tiedostonPolku = "";
                    if (PelinAsetukset.musiikkiPäällä) {
                        File musaTiedosto = Assets.annaMusa(musa);
                        tiedostonNimi = musaTiedosto.getName();
                        tiedostonPolku = musaTiedosto.getPath();
                        int sampleRate = 48_000;
                        if (tiedostonNimi.length() > 3) {
                            tiedostotyyppi = tiedostonNimi.substring(tiedostonNimi.length()-3, tiedostonNimi.length());
                        }
                        switch (tiedostotyyppi) {
                            case "wav" -> {
                                musaClip.close();    
                                audioInputStream = AudioSystem.getAudioInputStream(musaTiedosto);
                                musaClip.open(audioInputStream);
                                sampleRate = 48_000;
                            }
                            case "mp3" -> {
                                musaClip.close();
                                audioInputStream = Dekoodaus.decodeMP3(tiedostonPolku);
                                musaClip.open(audioInputStream);
                                sampleRate = 44_100;
                            }
                            case "ogg" -> {
                                musaClip.close();
                                audioInputStream = Dekoodaus.decodeOgg(tiedostonPolku);
                                musaClip.open(audioInputStream);
                                sampleRate = 44_100;
                            }
                            case null, default -> {
                                System.out.println("Ei-tuettu tiedostotyyppi: " + tiedostonNimi);
                                throw new UnsupportedAudioFileException();
                            }
                        }
                        FloatControl gainControl = (FloatControl) musaClip.getControl(FloatControl.Type.MASTER_GAIN);
                        float gain = (float)(Math.pow(PelinAsetukset.musaVolyymi, (1f/9f))*80 -80);
                        gainControl.setValue(gain);
                        int loopStart = valitsePeliMusanLoopKohta(musa, sampleRate);
                        int loopEnd = musaClip.getFrameLength()-1;
                        musaClip.setLoopPoints(loopStart, loopEnd);
                        musaClip.loop(Clip.LOOP_CONTINUOUSLY);
                        musaClip.start();
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
            if (musaClip != null) {
                musaClip.stop();
            }
        }
    }

    public static void asetaMusanVolyymi(double volyymi) {
        synchronized(äänisäikeenLukko) {
            if (musaClip != null) {
                FloatControl gainControl = (FloatControl) musaClip.getControl(FloatControl.Type.MASTER_GAIN);
                float gain = (float)(Math.pow(volyymi, (1f/9f))*80 -80);
                gainControl.setValue(gain);
            }
            PelinAsetukset.musaVolyymi = volyymi;
        }
    }
}
