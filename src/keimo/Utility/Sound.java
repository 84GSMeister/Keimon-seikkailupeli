package keimo.Utility;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The {@code Sound} class plays audio from a wav, ogg, or mp3 file with wav working the best in a new thread
 * <p>
 * Here are some examples of how the {@code Sound} object can be initialized:
 * <blockquote><pre>
 *     Sound soundOne = new Sound("pathToFile/music.wav", true);
 *     Sound soundTwo = new Sound(new File("pathToFile/music.wav"), true);
 *     Sound soundThree = new Sound(ClassName.class.getResource(pathToFile/music.wav), true);
 * </pre></blockquote>
 * <p>
 * The class {@code Sound} includes methods for playing audio, stopping audio, changing the volume, getting the duration if a wav, get whether the
 * audio is stopped, get whether the audio is finished, and changing the input file
 *
 * @author Gigi Bayte 2
 */
public class Sound {
    /**
     * Whether or not the music should be playing in a loop
     */
    private boolean loopable;

    /**
     * The String name of the file
     */
    private String fileName;

    /**
     * The list of instances of this sound playing
     */
    private ArrayList<PlayingSound> playingSounds = new ArrayList<>();

    /**
     * Initializes a newly created {@code Sound} object given a String file name
     *
     * @param fileName Path of the file to be played
     * @param loopable Whether or not the audio should loop
     */
    public Sound(String fileName, boolean loopable) {
        this.fileName = fileName;
        this.loopable = loopable;
    }

    /**
     * Plays the audio from the given source
     */
    public final void play() {
        playingSounds.add(new PlayingSound());
    }

    /**
     * Stops the audio from playing
     */
    public final void stop() {
        for(PlayingSound ps : playingSounds)
            ps.stop();
    }

    /**
     * The AudioFormat to specify the convention to represent the data
     *
     * @param inFormat The format of the audio file
     * @return The necessary format information from the inFormat
     */
    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    /**
     * Removes a {@code PlayingSound} object from the {@code ArrayList} of audio clips playing
     *
     * @param ps The {@code PlayingSound} instance to remove
     */
    private void removeInternalSound(PlayingSound ps) {
        playingSounds.remove(ps);
    }

    /**
     * The {@code PlayingSound} class plays the audio file and allows for multiple files to be played and stopped
     */
    private class PlayingSound {
        private boolean stop = false;

        PlayingSound() {
            Thread playingSound = new Thread(() -> {

                //REMOVED THE DO WHILE LOOP HERE
                try {
                    AudioInputStream in;
                    in = getAudioInputStream(new File(fileName));
                    final AudioFormat outFormat = getOutFormat(in.getFormat());
                    final Info info = new Info(SourceDataLine.class, outFormat);
                    try(final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                        if(line != null) {
                            line.open(outFormat);
                            line.start();
                            AudioInputStream inputMystream = AudioSystem.getAudioInputStream(outFormat, in);
                            stream(outFormat, inputMystream, line);
                            line.drain();
                            line.stop();
                        }
                    }
                }
                catch(UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                    throw new IllegalStateException(e);
                }
                finally {
                    removeInternalSound(this);
                }
            });
            playingSound.start();
        }

        /**
         * Streams the audio to the mixer
         *
         * @param in   Input stream to audio file
         * @param line Where the audio data can be written to
         * @throws IOException Thrown if given file has any problems
         */
        private void stream(AudioFormat outFormat, AudioInputStream in, SourceDataLine line) throws IOException {
            byte[] buffer = new byte[32];
            do {
                for(int n = 0; n != -1 && !stop; n = in.read(buffer, 0, buffer.length)) {
                    byte[] bufferTemp = new byte[buffer.length];
                    for(int i = 0; i < bufferTemp.length; i += 2) {
                        short audioSample = (short) ((short) ((buffer[i + 1] & 0xff) << 8) | (buffer[i] & 0xff));
                        bufferTemp[i] = (byte) audioSample;
                        bufferTemp[i + 1] = (byte) (audioSample >> 8);
                    }
                    buffer = bufferTemp;
                    line.write(buffer, 0, n);
                }
                try {
                    in = getAudioInputStream(new File(fileName));
                }
                catch (UnsupportedAudioFileException uafe) {
                    uafe.printStackTrace();
                }
                in = AudioSystem.getAudioInputStream(outFormat, in);
            } while(loopable && !stop);
        }

        void stop() {
            stop = true;
        }

    }

}
