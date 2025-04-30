package keimo.keimoEngine.äänet;

import org.lwjgl.openal.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import keimo.Utility.Downloaded.WaveData;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class Ääni {
    private int bufferId;
    private int sourceId;
    private String filePath;
    private boolean isPlaying = false;

    int format = -1;
    ShortBuffer rawAudioBuffer;

    public Ääni(String filepath, boolean loops) {
        this.filePath = filepath;
        
        if (filepath.endsWith(".ogg")) {
            
            stackPush();
            IntBuffer channelsBuffer = stackMallocInt(1);
            stackPush();
            IntBuffer sampleRateBuffer = stackMallocInt(1);

            rawAudioBuffer = stb_vorbis_decode_filename(filepath, channelsBuffer, sampleRateBuffer);
            if (rawAudioBuffer == null) {
                System.out.println("ongelma ladatessa ääntä: " + filepath);
                stackPop();
                stackPop();
                return;
            }

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

            bufferId = alGenBuffers();
            alBufferData(bufferId, format, rawAudioBuffer, sampleRate);

            sourceId = alGenSources();
            alSourcei(sourceId, AL_BUFFER, bufferId);
            alSourcei(sourceId, AL_LOOPING, loops ? 1 : 0);
            alSourcef(sourceId, AL_GAIN, 0.5f);

        }
        else if (filepath.endsWith(".wav")) {
            try {
                WaveData waveData = WaveData.create(new BufferedInputStream(new FileInputStream(new File(filepath))));
                bufferId = alGenBuffers();
                alBufferData(bufferId, waveData.format, waveData.data, waveData.samplerate);
                waveData.dispose();
                sourceId = alGenSources();
                alSourcei(sourceId, AL_BUFFER, bufferId);
                alSourcef(sourceId, AL_GAIN, 0.5f);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("unsupported audio format: " + filepath);
        }

        //free(rawAudioBuffer);
    }

    public void delete() {
        alDeleteSources(sourceId);
        alDeleteBuffers(bufferId);
    }

    public void play() {
        alSourcePlay(sourceId);
        isPlaying = true;
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

    public void resample(int sampleRate) {
        alDeleteBuffers(bufferId);
        alBufferData(bufferId, format, rawAudioBuffer, sampleRate);
        bufferId = alGenBuffers();
    }

    public void setVolume(float gain) {
        if (gain > 1) gain = 1;
        else if (gain < 0) gain = 0;
        alSourcef(sourceId, AL_GAIN, gain);
    }
}
