package keimo.keimoengine.äänet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.*;

import static org.lwjgl.stb.STBVorbis.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

public class Dekoodaus {

    public static AudioInputStream haeÄäniStream(File ääniTiedosto, float sampleRate, boolean takaperin) {
        AudioInputStream sourceStream = null;
        try {
            if (ääniTiedosto.getName().endsWith(".wav")) {
                sourceStream = decodeWav(ääniTiedosto, sampleRate, takaperin);
            }
            else if (ääniTiedosto.getName().endsWith(".ogg")) {
                sourceStream = decodeOgg(ääniTiedosto.getPath(), sampleRate, takaperin);
            }
            else if (ääniTiedosto.getName().endsWith(".mp3")) {
                sourceStream = decodeMP3(ääniTiedosto.getPath(), sampleRate, takaperin);
            }
            else {
                throw new UnsupportedAudioFileException();
            }
        }
        catch (UnsupportedAudioFileException e) {
            System.out.println("Ei-tuettu äänitiedosto: " + ääniTiedosto.getName());
            e.printStackTrace();
        }
        return sourceStream;
    }

    public static AudioInputStream decodeWav(File ääniTiedosto, float newSampleRate, boolean takaperin ) {
        // Tähän ei tarvita mitään ihmeellistä koska Java Sound API tukee natiivisti Wave-tiedostoja.
        // Jos ääntä ei tarvitse manipuloida, voidaan palauttaa AudioInputStream suoraan.
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(ääniTiedosto);
            // Bittivirran manipulointia
            // Jos halutaan toistaa takaperin, käännetään array
            // Jos halutaan toistaa suuremmalla taajuudella kuin Java Sound API tukee, leikataan "nerokkaasti" osa sampleista pois.
            if (takaperin || newSampleRate > 176_400) {
                byte[] bytes = stream.readAllBytes();
                short[] samples = new short[bytes.length/2];
                for (int i = 0; i < samples.length-1; i++) {
                    short s = (short)(bytes[i*2] + bytes[i*2+1]*0xFF);
                    samples[i] = s;
                }

                short[] samplesCut = new short[1];
                short[] samplesReversed = new short[1];
                if (newSampleRate > 176_400) {
                    samplesCut = new short[(int)(samples.length * (176_400/newSampleRate) +1)];
                    for (int i = 0; i < samples.length; i++) {
                        samplesCut[(int)(i * (176_400/newSampleRate))] = samples[i];
                    }
                    if (takaperin) {
                        samplesReversed = new short[samplesCut.length];
                        for (int i = 0; i < samplesReversed.length; i++) {
                            samplesReversed[samplesCut.length-1 -i] = samplesCut[i];
                        }
                    }
                    else {
                        samplesReversed = samplesCut;
                    }
                }
                else {
                    samplesReversed = new short[samples.length];
                    for (int i = 0; i < samplesReversed.length; i++) {
                        samplesReversed[samples.length-1 -i] = samples[i];
                    }
                }

                byte[] sampleBytes = new byte[samplesReversed.length * 2];
                for (int i = 0; i < samplesReversed.length-1; i++) {
                    sampleBytes[i*2] = (byte)(samplesReversed[i] & 0x00FF);
                    sampleBytes[i*2+1] = (byte)((samplesReversed[i] >> 8) & 0x00FF);
                }
                AudioFormat format = stream.getFormat();
                ByteArrayInputStream byteStream = new ByteArrayInputStream(sampleBytes);
                stream = new AudioInputStream(byteStream, format, samples.length);
            }
            return stream;
        }
        catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AudioInputStream decodeOgg(String tiedostonNimi) {
        return decodeOgg(tiedostonNimi, 44100, false);
    }
    
    public static AudioInputStream decodeOgg(String tiedostonNimi, float newSampleRate, boolean takaperin) {
        int channels = 0;
        int sampleRate = 0;
        int sampleCount = 0;
        ShortBuffer rawAudioBuffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer channelsBuffer = stack.mallocInt(1);
            IntBuffer sampleRateBuffer = stack.mallocInt(1);
            // Tämä on varsinainen dekoodausfunktio, joka muuntaa OGG:n raa'aksi dataksi. STB Vorbis -kirjastosta.
            rawAudioBuffer = stb_vorbis_decode_filename(tiedostonNimi, channelsBuffer, sampleRateBuffer);
            sampleCount = rawAudioBuffer.capacity();
            channels = channelsBuffer.get();

            short[] samples;
            // Jos samplerate on yli 176_400 (4*44100), rupea leikkaamaan sampleja
            if (newSampleRate > 176_400) {
                sampleRate = 176_400;
                samples = new short[(int)(sampleCount * (176_400/newSampleRate) +1)];
                for (int i = 0; i < sampleCount; i++) {
                    samples[(int)(i * (176_400/newSampleRate))] = rawAudioBuffer.get(i);
                }
            }
            else { 
                sampleRate = sampleRateBuffer.get();
                samples = new short[sampleCount];
                for (int i = 0; i < sampleCount; i++) {
                    samples[i] = rawAudioBuffer.get(i);
                }
            }
            LibCStdlib.free(rawAudioBuffer);

            // Muutetaan short-array byte-arrayksi.
            byte[] sampleBytes = new byte[samples.length * 2];
            if (takaperin) {
                // Tehdään käänteinen array jos halutaan toistaa takaperin.
                short[] samplesReversed = new short[samples.length];
                for (int i = 0; i < samplesReversed.length; i++) {
                    samplesReversed[samples.length-1 -i] = samples[i];
                }

                for (int i = 0; i < samplesReversed.length-1; i++) {
                    sampleBytes[i*2] = (byte)(samplesReversed[i] & 0x00FF);
                    sampleBytes[i*2+1] = (byte)((samplesReversed[i] >> 8) & 0x00FF);
                }
            }
            else {
                for (int i = 0; i < samples.length; i++) {
                    sampleBytes[i*2] = (byte)(samples[i] & 0x00FF);
                    sampleBytes[i*2+1] = (byte)((samples[i] >> 8) & 0x00FF);
                }
            }

            // Muunnetaan raaka data Java sound API:n ymmärtämään muotoon.
            AudioFormat format = new AudioFormat(Encoding.PCM_SIGNED, sampleRate, 16, channels, channels * 2, sampleRate, false);
            ByteArrayInputStream byteStream = new ByteArrayInputStream(sampleBytes);
            AudioInputStream stream = new AudioInputStream(byteStream, format, samples.length);
            return stream;
        }
        catch (Exception e) {
            System.out.println("Ogg decode failed.");
            e.printStackTrace();
            return null;
        }
    }

    public static AudioInputStream decodeMP3(String tiedostonNimi) {
        return decodeMP3(tiedostonNimi, 44100, false);
    }

    public static AudioInputStream decodeMP3(String tiedostonNimi, float newSampleRate, boolean takaperin) {
        try {
            Bitstream bitStream;
            bitStream = new Bitstream(new FileInputStream(tiedostonNimi));
            boolean finished = false;
            int frame = 0;
            Decoder decoder = new Decoder();
            int bitDepth = 16;
            int channels = 2;
            int sampleRate = 44100;
            List<Short> shortList = new ArrayList<>();

            while (!finished) {
                if (bitStream.readFrame() != null) {
                    // Tämä on varsinainen dekoodausfunktio, joka muuntaa MP3:n raa'aksi dataksi. JLayer JavaMP3 -kirjastosta.
                    SampleBuffer buf = (SampleBuffer) decoder.decodeFrame(bitStream.readFrame(), bitStream); //returns the next 2304 samples
                    short[] samplesInBuffer;
                    // Jos samplerate on yli 176_400 (4*44100), rupea leikkaamaan sampleja
                    if (newSampleRate > 176_400) {
                        sampleRate = 176_400;
                        samplesInBuffer = new short[(int)(buf.getBuffer().length * (176_400/newSampleRate) +1)];
                        for (int i = 0; i < buf.getBuffer().length; i++) {
                            samplesInBuffer[(int)(i * (176_400/newSampleRate))] = buf.getBuffer()[i];
                        }
                    }
                    else {
                        samplesInBuffer = buf.getBuffer();
                    }

                    if (frame == 0) {
                        channels = decoder.getOutputChannels();
                        sampleRate = decoder.getOutputFrequency();
                    }
                    else if (frame > 1) {
                        for (int i = 0; i < samplesInBuffer.length; i++) {
                            shortList.add(samplesInBuffer[i]);
                        }
                    }
                    bitStream.closeFrame();
                    frame++;
                }
                else {
                    finished = true;
                }
            }

            short[] samples = new short[shortList.size()];
            for (int i = 0; i < shortList.size(); i++) {
                samples[i] = shortList.get(i);
            }

            // Muutetaan short-array byte-arrayksi.
            byte[] sampleBytes = new byte[samples.length * 2];
            if (takaperin) {
                // Tehdään käänteinen array jos halutaan toistaa takaperin.
                short[] samplesReversed = new short[samples.length];
                for (int i = 0; i < samplesReversed.length; i++) {
                    samplesReversed[samples.length-1 -i] = samples[i];
                }
                for (int i = 0; i < samplesReversed.length-1; i++) {
                    sampleBytes[i*2] = (byte)(samplesReversed[i] & 0x00FF);
                    sampleBytes[i*2+1] = (byte)((samplesReversed[i] >> 8) & 0x00FF);
                }
            }
            else {
                for (int i = 0; i < samples.length; i++) {
                    sampleBytes[i*2] = (byte)(samples[i] & 0x00FF);
                    sampleBytes[i*2+1] = (byte)((samples[i] >> 8) & 0x00FF);
                }
            }

            // Muunnetaan raaka data Java sound API:n ymmärtämään muotoon.
            AudioFormat format = new AudioFormat(Encoding.PCM_SIGNED, sampleRate, bitDepth, channels, 2 * channels, sampleRate, false);
            ByteArrayInputStream byteStream = new ByteArrayInputStream(sampleBytes);
            AudioInputStream stream = new AudioInputStream(byteStream, format, sampleBytes.length/2);
            return stream;
        }
        catch (Exception e) {
            System.out.println("MP3 Decode failed.");
            e.printStackTrace();
            return null;
        }
    }

    static float log2(float n) {
        float result = (float)(Math.log(n)/Math.log(2));
        return result;
    }
}
