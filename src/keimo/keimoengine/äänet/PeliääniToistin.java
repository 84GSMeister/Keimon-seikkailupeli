package keimo.keimoengine.äänet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;

public class PeliääniToistin {

    private static int maxÄäntenMäärä = 32;
    static HashMap<Integer, Clip> ääniClipit = new HashMap<>();
    static int seuraavaÄäniIndeksi = 0;
    static Clip musaClip;
    static Line line;

    public static void toistaPelimusa(float sampleRate, File ääniTiedosto, double volume, double pan, boolean loop, int loopKohta, boolean takaperin) {
        try {
            float realSampleRate = sampleRate;
            if (musaClip != null && !musaClip.isActive()) {
                musaClip.close();
            }
            if (sampleRate > 176_400) {
                realSampleRate = 176_400;
            }

            AudioInputStream sourceStream = Dekoodaus.haeÄäniStream(ääniTiedosto, sampleRate, takaperin);
            AudioInputStream resampledInputStream = muutaTaajuutta(sourceStream, realSampleRate);

            if (musaClip == null) musaClip = AudioSystem.getClip();
            else musaClip.close();
            musaClip.open(resampledInputStream);

            if (loop) {
                int loopStart = 0;
                int loopEnd = musaClip.getFrameLength()-1;
                if (takaperin) {
                    if (sampleRate <= 176_400) {
                        loopEnd = musaClip.getFrameLength()-1 - loopKohta;
                    }
                    else {
                        loopEnd = musaClip.getFrameLength()-1 - (int)(loopKohta * (176_400/sampleRate));
                    }
                }
                else {
                    if (sampleRate <= 176_400) loopStart = loopKohta;
                    else loopStart = (int)(loopKohta * (176_400/sampleRate));
                }
                musaClip.setLoopPoints(loopStart, loopEnd);
                musaClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            if (musaClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) musaClip.getControl(FloatControl.Type.MASTER_GAIN);
                float gain = 0;
                gain = (float)(Math.pow(volume, (1f/9f))*80 -80);
                gainControl.setValue(gain);
            }
            if (musaClip.isControlSupported(FloatControl.Type.PAN)) {
                FloatControl panControl = (FloatControl)musaClip.getControl(FloatControl.Type.PAN);
                float panFloat = (float)pan;
                panControl.setValue(panFloat);
            }
            musaClip.start();
        }
        catch (IllegalArgumentException e) {
            System.out.println("Ei-tuettu säätö");
            e.printStackTrace();
        }
        catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            System.out.println("Ääntä ei voitu toistaa: " + ääniTiedosto.getName());
            e.printStackTrace();
        }
    }

    public static void toistaResamplattavaÄäni(float sampleRate, File ääniTiedosto, double volume, double pan, boolean loop, boolean takaperin) {
        try {
            float realSampleRate = sampleRate;
            for (int i = 0; i < ääniClipit.size(); i++) {
                if (ääniClipit.get(i) != null && !ääniClipit.get(i).isActive()) {
                    if (ääniClipit.get(i).isOpen()) {
                        ääniClipit.get(i).close();
                    }
                }
            }
            // Jos samplerate on yli 176_400 (4*44100), toistetaan todellisuudessa 176_400 Hz, mutta leikataan sampleja dekoodausvaiheessa
            if (sampleRate > 176_400) {
                realSampleRate = 176_400;
            }

            AudioInputStream sourceStream = Dekoodaus.haeÄäniStream(ääniTiedosto, sampleRate, takaperin);
            AudioInputStream resampledInputStream = muutaTaajuutta(sourceStream, realSampleRate);

            boolean kasvataÄäniIndeksiä = false;
            if (ääniClipit.get(seuraavaÄäniIndeksi) == null) {
                Clip clip = AudioSystem.getClip();
                ääniClipit.put(seuraavaÄäniIndeksi, clip);
                kasvataÄäniIndeksiä = true;
            }
            ääniClipit.get(seuraavaÄäniIndeksi).open(resampledInputStream);

            // Jos peliääniä tarvii loopata, keksi tähän joku uusi ratkaisu

            // if (loop) {
            //     int loopStart = 0;
            //     int loopEnd = ääniClipit.get(seuraavaÄäniIndeksi).getFrameLength()-1;
            //     if (takaperin) {
            //         if (sampleRate <= 176_400) {
            //             loopEnd = ääniClipit.get(seuraavaÄäniIndeksi).getFrameLength()-1 - valitsePeliMusanLoopKohta(ääniTiedosto.getName(), 44100);
            //         }
            //         else {
            //             loopEnd = ääniClipit.get(seuraavaÄäniIndeksi).getFrameLength()-1 - valitsePeliMusanLoopKohta(ääniTiedosto.getName(), (int)(44100 * (176_400/sampleRate)));
            //         }
            //     }
            //     else {
            //         if (sampleRate <= 176_400) loopStart = valitsePeliMusanLoopKohta(ääniTiedosto.getName(), 44100);
            //         else loopStart = valitsePeliMusanLoopKohta(ääniTiedosto.getName(), (int)(44100 * (176_400/sampleRate)));
            //     }
            //     ääniClipit.get(seuraavaÄäniIndeksi).setLoopPoints(loopStart, loopEnd);
            //     ääniClipit.get(seuraavaÄäniIndeksi).loop(Clip.LOOP_CONTINUOUSLY);
            // }
            if (ääniClipit.get(seuraavaÄäniIndeksi).isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) ääniClipit.get(seuraavaÄäniIndeksi).getControl(FloatControl.Type.MASTER_GAIN);
                float gain = 0;
                gain = (float)(Math.pow(volume, (1f/9f))*80 -80);
                gainControl.setValue(gain);
            }
            if (ääniClipit.get(seuraavaÄäniIndeksi).isControlSupported(FloatControl.Type.PAN)) {
                FloatControl panControl = (FloatControl)ääniClipit.get(seuraavaÄäniIndeksi).getControl(FloatControl.Type.PAN);
                float panFloat = (float)pan;
                panControl.setValue(panFloat);
            }
            ääniClipit.get(seuraavaÄäniIndeksi).start();
            if (kasvataÄäniIndeksiä) {
                seuraavaÄäniIndeksi++;
                seuraavaÄäniIndeksi %= maxÄäntenMäärä;
            }
            else {
                for (int i = 0; i < ääniClipit.size(); i++) {
                    if (!ääniClipit.get(i).isOpen()) {
                        seuraavaÄäniIndeksi = i;
                        break;
                    }
                    else if (i == maxÄäntenMäärä-1) {
                        ääniClipit.get(i).close();
                    }
                }
            }

        }
        catch (IllegalArgumentException e) {
            System.out.println("Ei-tuettu säätö");
            e.printStackTrace();
        }
        catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            System.out.println("Ääntä ei voitu toistaa: " + ääniTiedosto.getName());
            e.printStackTrace();
        }
    }

    public static void suljeÄänet() {
        for (int i = 0; i < ääniClipit.size(); i++) {
            if (ääniClipit.get(i) != null) {
                ääniClipit.get(i).close();
            }
        }
    }

    public static void suljeMusa() {
        if (musaClip != null) {
            musaClip.stop();
        }
    }

    public static void asetaMusanVolyymi(double volyymi) {
        if (musaClip != null) {
            FloatControl gainControl = (FloatControl) musaClip.getControl(FloatControl.Type.MASTER_GAIN);
            float gain = (float)(Math.pow(volyymi, (1f/9f))*80 -80);
            gainControl.setValue(gain);
        }
    }

    public static AudioInputStream muutaTaajuutta(AudioInputStream sourceStream, float sampleRate) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat targetFormat = getOutFormat(sourceFormat, sampleRate);
        return new AudioInputStream(sourceStream, targetFormat, AudioSystem.NOT_SPECIFIED);
    }

    private static AudioFormat getOutFormat(AudioFormat inFormat, float sampleRate) {
        Encoding enc = inFormat.getEncoding();
        int ch = inFormat.getChannels();
        boolean isBigEndian = inFormat.isBigEndian();
        return new AudioFormat(enc, sampleRate, 16, ch, ch * 2, sampleRate, isBigEndian);
    }
}
