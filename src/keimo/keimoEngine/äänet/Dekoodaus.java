package keimo.keimoEngine.äänet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;

import javazoom.jl.decoder.*;

import static org.lwjgl.stb.STBVorbis.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

public class Dekoodaus {
    
    public static AudioInputStream decodeOgg(String tiedostonNimi) {
        int channels = 0;
        int sampleRate = 0;
        int sampleCount = 0;
        AudioInputStream stream;
        ShortBuffer rawAudioBuffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer channelsBuffer = stack.mallocInt(1);
            IntBuffer sampleRateBuffer = stack.mallocInt(1);
            rawAudioBuffer = stb_vorbis_decode_filename(tiedostonNimi, channelsBuffer, sampleRateBuffer);
            sampleCount = rawAudioBuffer.capacity();
            channels = channelsBuffer.get();
            sampleRate = sampleRateBuffer.get();

            short[] samples = new short[sampleCount];
            for (int i = 0; i < sampleCount; i++) {
                samples[i] = rawAudioBuffer.get(i);
            }
            byte[] sampleBytes = new byte[samples.length * 2];
            for (int i = 0; i < samples.length; i++) {
                sampleBytes[i*2] = (byte)(samples[i] & 0x00FF);
                sampleBytes[i*2+1] = (byte)((samples[i] >> 8) & 0x00FF);
            }

            AudioFormat format = new AudioFormat(Encoding.PCM_SIGNED, sampleRate, 16, channels, channels * 2, sampleRate, false);
            ByteArrayInputStream byteStream = new ByteArrayInputStream(sampleBytes);
            stream = new AudioInputStream(byteStream, format, samples.length);
            LibCStdlib.free(rawAudioBuffer);
        }
        return stream;
    }

    protected static AudioInputStream decodeMP3(String tiedostonNimi) {
        try {
            Bitstream bitStream;
            bitStream = new Bitstream(new FileInputStream(tiedostonNimi));
            boolean finished = false;
            int frame = 0;
            Decoder decoder = new Decoder();
            int bitDepth = 16;
            int channels = 2;
            int sampleRate = 44100;
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            List<Byte> byteList = new ArrayList<>();

            while (!finished) {
                if (bitStream.readFrame() != null) {
                    SampleBuffer buf = (SampleBuffer) decoder.decodeFrame(bitStream.readFrame(), bitStream); //returns the next 2304 samples
                    short[] samples = buf.getBuffer();
                    byte[] sampleBytes = new byte[samples.length * 2];
                    for (int i = 0; i < samples.length; i++) {
                        sampleBytes[i*2] = (byte)(samples[i] & 0x00FF);
                        sampleBytes[i*2+1] = (byte)((samples[i] >> 8) & 0x00FF);
                    }
                    if (frame == 0) {
                        channels = decoder.getOutputChannels();
                        sampleRate = decoder.getOutputFrequency();
                    }
                    else if (frame > 1) {
                        for (int i = 0; i < sampleBytes.length; i++) {
                            byteList.add(sampleBytes[i]);
                        }
                    }
                    outStream.writeBytes(sampleBytes);
                    bitStream.closeFrame();
                    frame++;
                }
                else {
                    finished = true;
                }
            }

            byte[] bytes = new byte[byteList.size()];
            for (int i = 0; i < byteList.size(); i++) {
                bytes[i] = byteList.get(i);
            }

            AudioFormat format = new AudioFormat(Encoding.PCM_SIGNED, sampleRate, bitDepth, channels, 2 * channels, sampleRate, false);
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            AudioInputStream stream = new AudioInputStream(byteStream, format, bytes.length/2);
            return stream;
        }
        catch (Exception e) {
            System.out.println("Decode failed.");
            e.printStackTrace();
            return null;
        }
    }

    static float log2(float n) {
        float result = (float)(Math.log(n)/Math.log(2));
        return result;
    }
}
