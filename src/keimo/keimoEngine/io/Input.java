package keimo.keimoEngine.io;

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.*;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private long window;
    private boolean[] keys;
    private int[] keyPresses;

    private static ArrayList<MidiDevice> midiLaitteet = new ArrayList<MidiDevice>();
    private static Sequencer sequencer;

    public Input(long window) {
        this.window = window;
        this.keys = new boolean[GLFW_KEY_LAST];
        for (int i = 0; i < GLFW_KEY_LAST; i++) {
            keys[i] = false;
        }
        this.keyPresses = new int[GLFW_KEY_LAST];
        for (int i = 0; i < GLFW_KEY_LAST; i++) {
            keyPresses[i] = 0;
        }
        avaaMidiLaitteet();
    }

    public boolean isKeyDown(int key) {
        return glfwGetKey(window, key) == 1;
    }

    public boolean isMouseButtonDown(int button) {
        return glfwGetMouseButton(window, button) == 1;
    }

    public boolean isKeyPressed(int key) {
        return (isKeyDown(key) && !keys[key]);
    }

    public boolean isKeyReleased(int key) {
        return (!isKeyDown(key) && keys[key]);
    }

    public boolean isKeyHeld(int key) {
        if (isKeyDown(key) && !keys[key]) {
            return true;
        }
        else if (keyPresses[key] > 20) {
            return keyPresses[key] % 2 == 0;

        }
        else return false;
    }

    public void update() {
        for (int i = 32; i < GLFW_KEY_LAST; i++) {
            try {
                keys[i] = isKeyDown(i);
                if (isKeyDown(i)) keyPresses[i]++;
                else keyPresses[i] = 0;
            }
            catch (Exception e) {
                System.out.println("invalid key " + i);
            }
        }
    }

    public ArrayList<MidiDevice> annaMidiLaitteet() {
        return midiLaitteet;
    }

    public Sequencer getMidiSequencer() {
        return sequencer;
    }

    private void avaaMidiLaitteet() {
        try{
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            MidiDevice midiLaite;
            MidiDevice.Info[] midiLaiteInfot = MidiSystem.getMidiDeviceInfo();
            for (int i = 0; i < midiLaiteInfot.length; i++) {
                try {
                    midiLaite = MidiSystem.getMidiDevice(midiLaiteInfot[i]);
                    List<Transmitter> transmitters = midiLaite.getTransmitters();
                    for(int j = 0; j < transmitters.size(); j++) {
                        transmitters.get(j).setReceiver(new MidiInputReceiver(midiLaite.getDeviceInfo().toString(), this));
                    }

                    Transmitter trans = midiLaite.getTransmitter();
                    trans.setReceiver(new MidiInputReceiver(midiLaite.getDeviceInfo().toString(), this));
                    midiLaite.open();
                }
                catch (MidiUnavailableException e) {
                    //e.printStackTrace();
                }
            }
        }
        catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
}
