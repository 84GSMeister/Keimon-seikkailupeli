package keimo.keimoEngine.äänet;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteBuffer;

import org.lwjgl.openal.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import keimo.Utility.Downloaded.WaveData;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class Musa {
    private int bufferIdIntro;
    private int bufferIdLoop;
    private int sourceId;
    private String filePath;
    private boolean isPlaying = false;
    private int loopKohta;

    int format = -1;
    ShortBuffer fullRawBuffer;
    ShortBuffer bufferIntro;
    ShortBuffer bufferLoop;

    public Musa(String filepath, boolean loops, int loopKohta) {
        this.filePath = filepath;
        
        if (filepath.endsWith(".ogg")) {
            stackPush();
            IntBuffer channelsBuffer = stackMallocInt(1);
            stackPush();
            IntBuffer sampleRateBuffer = stackMallocInt(1);

            fullRawBuffer = stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);
            
            if (fullRawBuffer == null) {
                System.out.println("ongelma ladatessa ääntä: " + filepath);
                stackPop();
                stackPop();
                return;
            }

            this.loopKohta = 2 * valitsePeliMusanLoopKohta(loopKohta);
            bufferIntro = fullRawBuffer.slice(0, this.loopKohta);
            bufferLoop = fullRawBuffer.slice(this.loopKohta, fullRawBuffer.remaining() - this.loopKohta);

            int channels = channelsBuffer.get();
            int sampleRate = sampleRateBuffer.get();

            stackPop();
            stackPop();
        
            if (channels == 1) {
                format = AL_FORMAT_MONO16;
            }
            else if (channels == 2) {
                format = AL_FORMAT_STEREO16;
            }

            bufferIdIntro = alGenBuffers();
            alBufferData(bufferIdIntro, format, bufferIntro, sampleRate);

            bufferIdLoop = alGenBuffers();
            alBufferData(bufferIdLoop, format, bufferLoop, sampleRate);

            sourceId = alGenSources();
            //alSourcei(sourceId, AL_BUFFER, bufferIdIntro);
            alSourceQueueBuffers(sourceId, bufferIdIntro);
            alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
            alSourcef(sourceId, AL_GAIN, 0.5f);

            //alSourcei(sourceId, AL_BUFFER, bufferIdLoop);
            for (int i = 0; i < 100_000; i++) {
                alSourceQueueBuffers(sourceId, bufferIdLoop);
            }
            //alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
            //alSourcef(sourceId, AL_GAIN, 0.3f);

            //alSourceUnqueueBuffers(sourceId);

            //free(rawAudioBuffer);
        }
        else if (filepath.endsWith(".wav")) {
            try {
                WaveData waveData = WaveData.create(new BufferedInputStream(new FileInputStream(new File(filepath))));

                this.loopKohta = 0 * valitsePeliMusanLoopKohta(loopKohta);
                ByteBuffer waveDataIntro = waveData.data.slice(0, this.loopKohta);
                ByteBuffer waveDataLoop = waveData.data.slice(this.loopKohta,  waveData.data.remaining() - this.loopKohta);
                bufferIdIntro = alGenBuffers();
                alBufferData(bufferIdIntro, waveData.format, waveDataIntro, waveData.samplerate);

                bufferIdLoop = alGenBuffers();
                alBufferData(bufferIdLoop, waveData.format, waveDataLoop, waveData.samplerate);
                waveData.dispose();
                
                sourceId = alGenSources();
                //alSourcei(sourceId, AL_BUFFER, bufferIdIntro);
                alSourceQueueBuffers(sourceId, bufferIdIntro);
                alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
                alSourcef(sourceId, AL_GAIN, 0.5f);

                //alSourcei(sourceId, AL_BUFFER, bufferIdLoop);
                for (int i = 0; i < 100_000; i++) {
                    alSourceQueueBuffers(sourceId, bufferIdLoop);
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("unsupported audio format: " + filepath);
        }
    }

    public void delete() {
        alDeleteSources(sourceId);
        alDeleteBuffers(sourceId);
    }

    public void queueLoop() {

    }

    public void play() {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
            alSourcei(sourceId, AL_POSITION, 0);
        }

        if (!isPlaying) {
            alSourcePlay(sourceId);
            isPlaying = true;
        }   
    }

    public void stop() {
        alSourceStop(sourceId);
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isPlaying() {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
        }
        return isPlaying;
    }

    public void setVolume(float gain) {
        if (gain > 1) gain = 1;
        else if (gain < 0) gain = 0;
        alSourcef(sourceId, AL_GAIN, gain);
    }

    private int valitsePeliMusanLoopKohta(int loopKohtaMs) {
        int loopKohta = 0;
        double sampleRate = 44_100;
        loopKohta = (int)((loopKohtaMs/1000d) * sampleRate);
        return loopKohta;
    }
}
