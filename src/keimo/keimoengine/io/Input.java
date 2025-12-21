package keimo.keimoengine.io;

import java.util.ArrayList;
import javax.sound.midi.*;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Input {
    private long window;
    private boolean[] keys;
    private int[] keyPresses;
    private boolean[] mouseButtons;
    public double[] cursorPosX;
    public double[] cursorPosY;
    private double cursorX;
    private double cursorY;
    private double scrollX;
    private double scrollY;
    protected boolean updateScroll;

    private static ArrayList<MidiDevice> midiLaitteet = new ArrayList<MidiDevice>();
    protected static Sequencer sequencer;

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
        this.mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
            mouseButtons[i] = false;
        }
    }

    public boolean isKeyDown(int key) {
        return glfwGetKey(window, key) == 1;
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

    public boolean isMouseButtonDown(int button) {
        return glfwGetMouseButton(window, button) == 1;
    }

    public boolean isMouseButtonPressed(int button) {
        return (isMouseButtonDown(button) && !mouseButtons[button]);
    }

    public boolean isMouseButtonReleased(int button) {
        return (!isMouseButtonDown(button) && mouseButtons[button]);
    }

    public double getCursorPosX() {
        return cursorX;
    }

    public double getCursorPosY() {
        return cursorY;
    }

    public void setCursorPos(double x, double y) {
        this.cursorX = x;
        this.cursorY = y;
    }

    public double getScrollX() {
        return scrollX;
    }

    public double getScrollY() {
        return scrollY;
    }

    public void setScroll(double x, double y) {
        this.scrollX = x;
        this.scrollY = y;
        updateScroll = true;
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
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
            try {
                mouseButtons[i] = isMouseButtonDown(i);
            }
            catch (Exception e) {
                System.out.println("invalid mouse button " + i);
            }
        }
    }

    public abstract void tarkistaSyöte();

    public ArrayList<MidiDevice> annaMidiLaitteet() {
        return midiLaitteet;
    }

    public Sequencer getMidiSequencer() {
        return sequencer;
    }
}
