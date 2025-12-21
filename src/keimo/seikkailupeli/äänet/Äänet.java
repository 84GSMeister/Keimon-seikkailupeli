package keimo.seikkailupeli.äänet;

import keimo.keimoengine.collision.Piste;
import keimo.keimoengine.äänet.Dekoodaus;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Äänet {
    protected static HashMap<Integer, Clip> ääniClipit = new HashMap<>();
    private static int seuraavaÄäniIndeksi = 0;
    private static int maxÄäntenMäärä = 20;
    private static double defaultVolume = 1;
    private static double defaultPan = 0;
    private static AudioInputStream resampledInputStream;
    private static Random random = new Random();

    public static void toistaSFX(String ääni) {
        toistaSFX(ääni, defaultVolume, defaultPan);
    }

    public static void toistaSFX(String ääni, boolean randomTaajuus) {
        toistaSFX(ääni, defaultVolume, defaultPan, randomTaajuus);
    }

    public static void toistaSFX(String ääni, Piste sijaintiKentällä) {
        double xEtäisyys = Pelaaja.hitbox.getCenterX() - sijaintiKentällä.annaX();
        double yEtäisyys = Pelaaja.hitbox.getCenterY() - sijaintiKentällä.annaY();
        double pan = -xEtäisyys/512;
        if (pan < -1) pan = -1;
        else if (pan > 1) pan = 1;
        double etäisyysKerroinV = Math.min(xEtäisyys, yEtäisyys);
        double volume = ((100 * etäisyysKerroinV) / 1024 + 100)/100;
        if (volume > 0.75) volume = 0.75;
        else if (volume < 0) volume = 0;
        toistaSFX(ääni, volume, pan);
    }

    public static void toistaSFX(String ääni, double volume, double pan) {
        toistaSFX(ääni, volume, pan, false);
    }

    public static void toistaSFX(String ääni, double volume, double pan, boolean randomTaajuus) {
        try {
            if (randomTaajuus) {
                //int puoliaskelMuutos = random.nextInt(-12, 13);
                //float sampleRate = (float)(44100 * Math.pow(2d, (((double)puoliaskelMuutos)/12d)));
                float minTaajuus = (float)(44100 * Math.pow(2d, ((-1/12d))));
                float maxTaajuus = (float)(44100 * Math.pow(2d, ((1/12d))));
                float sampleRate = random.nextFloat(minTaajuus, maxTaajuus);
                toistaResamplattavaÄäni(sampleRate, ääni, false, false);
            }
            else toistaÄäni(ääni, volume, pan);
        }
        catch (Exception e) {
            System.out.println("Äänitiedostoa \"" + ääni + "\" ei löytynyt");
            e.printStackTrace();
        }
    }

    public static void toistaÄäni(String ääni, double volume, double pan) {
        try {
            for (int i = 0; i < ääniClipit.size(); i++) {
                if (!ääniClipit.get(i).isActive()) {
                    if (ääniClipit.get(i).isOpen()) {
                        ääniClipit.get(i).close();
                    }
                }
            }
            boolean kasvataÄäniIndeksiä = false;

            if (PelinAsetukset.äänetPäällä) {
                AudioInputStream audioInputStream = null;
                String tiedostotyyppi = "";
                String tiedostonNimi = "";
                String tiedostonPolku = "";
            
                File ääniTiedosto = Assets.annaÄäni(ääni);
                tiedostonNimi = ääniTiedosto.getName();
                tiedostonPolku = ääniTiedosto.getPath();
                if (tiedostonNimi.length() > 3) {
                    tiedostotyyppi = tiedostonNimi.substring(tiedostonNimi.length()-3, tiedostonNimi.length());
                }
                switch (tiedostotyyppi) {
                    case "wav" -> {
                        audioInputStream = AudioSystem.getAudioInputStream(ääniTiedosto);
                    }
                    case "mp3" -> {
                        audioInputStream = Dekoodaus.decodeMP3(tiedostonPolku);
                    }
                    case "ogg" -> {
                        audioInputStream = Dekoodaus.decodeOgg(tiedostonPolku);
                    }
                    case null, default -> {
                        System.out.println("Ei-tuettu tiedostotyyppi: " + tiedostonNimi);
                        throw new UnsupportedAudioFileException();
                    }
                }

                if (ääniClipit.get(seuraavaÄäniIndeksi) == null) {
                    Clip clip = AudioSystem.getClip();
                    ääniClipit.put(seuraavaÄäniIndeksi, clip);
                    kasvataÄäniIndeksiä = true;
                }
                ääniClipit.get(seuraavaÄäniIndeksi).open(audioInputStream);
                
                FloatControl gainControl = (FloatControl)ääniClipit.get(seuraavaÄäniIndeksi).getControl(FloatControl.Type.MASTER_GAIN);
                float gainFloat = (float)(Math.pow(PelinAsetukset.ääniVolyymi * volume, (1f/9f))*80 -80);
                gainControl.setValue(gainFloat);
                try {
                    FloatControl panControl = (FloatControl)ääniClipit.get(seuraavaÄäniIndeksi).getControl(FloatControl.Type.PAN);
                    float panFloat = (float)pan;
                    panControl.setValue(panFloat);
                }
                catch (IllegalArgumentException e) {
                    System.out.println("Panoroinnin säätö ei onnistunut. Tiedosto saattaa olla monoääni.");
                    e.printStackTrace();
                }
                finally {
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
            }
        }
        catch (Exception e) {
            System.out.println("Ääntä ei voitu toistaa");
            e.printStackTrace();
        }
    }

    public static void toistaResamplattavaÄäni(float sampleRate, String ääni, boolean musa, boolean toistaWoof) {
        try {
            if (ääniClipit.get(seuraavaÄäniIndeksi) != null) {
                ääniClipit.get(seuraavaÄäniIndeksi).close();
            }

            File ääniTiedosto = Assets.annaÄäni(ääni);
            // String tiedostonNimi = ääniTiedosto.getName();
            // String tiedostonPolku = ääniTiedosto.getPath();
            // if (tiedostonNimi.length() > 3) {
            //     tiedostotyyppi = tiedostonNimi.substring(tiedostonNimi.length()-3, tiedostonNimi.length());
            // }
            AudioInputStream sourceStream;
            if (ääniTiedosto.getName().endsWith(".wav")) {
                sourceStream = AudioSystem.getAudioInputStream(ääniTiedosto);
            }
            else if (ääniTiedosto.getName().endsWith(".ogg")) {
                sourceStream = Dekoodaus.decodeOgg(ääniTiedosto.getPath());
            }
            else if (ääniTiedosto.getName().endsWith(".mp3")) {
                sourceStream = Dekoodaus.decodeMP3(ääniTiedosto.getPath());
            }
            else {
                throw new UnsupportedAudioFileException();
            }
            AudioFormat sourceFormat = sourceStream.getFormat();
            AudioFormat targetFormat = getOutFormat(sourceFormat, sampleRate);
            resampledInputStream = new AudioInputStream(sourceStream, targetFormat, AudioSystem.NOT_SPECIFIED);

            if (ääniClipit.get(seuraavaÄäniIndeksi) == null) {
                Clip clip = AudioSystem.getClip();
                ääniClipit.put(seuraavaÄäniIndeksi, clip);
            }
            ääniClipit.get(seuraavaÄäniIndeksi).open(resampledInputStream);
            // if (musa) {
            //     int loopStart = valitsePeliMusanLoopKohta(ääniTiedosto.getName(), 44100);
            //     int loopEnd = ääniClipit.get(seuraavaÄäniIndeksi).getFrameLength()-1;
            //     ääniClipit.get(seuraavaÄäniIndeksi).setLoopPoints(loopStart, loopEnd);
            //     ääniClipit.get(seuraavaÄäniIndeksi).loop(Clip.LOOP_CONTINUOUSLY);
            // }
            FloatControl gainControl = (FloatControl) ääniClipit.get(seuraavaÄäniIndeksi).getControl(FloatControl.Type.MASTER_GAIN);
            float gain = 0;
            if (musa) gain = (float)(Math.pow(PelinAsetukset.musaVolyymi, (1f/9f))*80 -80);
            else gain = (float)(Math.pow(PelinAsetukset.ääniVolyymi, (1f/9f))*80 -80);
            gainControl.setValue(gain);
            ääniClipit.get(seuraavaÄäniIndeksi).start();

            seuraavaÄäniIndeksi++;
            seuraavaÄäniIndeksi %= maxÄäntenMäärä;
        }
        catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private static AudioFormat getOutFormat(AudioFormat inFormat, float sampleRate) {
        Encoding enc = inFormat.getEncoding();
        int ch = inFormat.getChannels();
        float rate = inFormat.getSampleRate();
        boolean isBigEndian = inFormat.isBigEndian();
        return new AudioFormat(enc, sampleRate, 16, ch, ch * 2, rate, isBigEndian);
    }

    public static void suljeToistetutÄänet() {
        for (Clip clip : ääniClipit.values()) {
            if (!clip.isActive() && clip.isOpen()) {
                clip.close();
            }
        }
    }

    public static void asetaSFXVolyymi(double volyymi) {
        if (ääniClipit.get(seuraavaÄäniIndeksi) != null) {
            FloatControl gainControl = (FloatControl) ääniClipit.get(seuraavaÄäniIndeksi).getControl(FloatControl.Type.MASTER_GAIN);
            float gain = (float)(Math.pow(volyymi, (1f/9f))*80 -80);
            gainControl.setValue(gain);
        }
        PelinAsetukset.ääniVolyymi = volyymi;
    }
}
