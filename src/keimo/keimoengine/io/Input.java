package keimo.keimoengine.io;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.sound.midi.*;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Input {
    private long window;
    private boolean[] keys;
    private int[] keyPresses;
    private boolean[] mouseButtons;
    private boolean[] joystickButtons;
    private boolean[] joystickHats;
    private boolean[] joystickAxes;
    private int[] joystickAxisPresses;
    public double[] cursorPosX;
    public double[] cursorPosY;
    private double cursorX;
    private double cursorY;
    private double scrollX;
    private double scrollY;
    protected boolean updateScroll;
    private int holdWaitFrames = 20;
    private int ohjainNumero;
    private int ohjainEvent;
    private float analogDeadzone = 0.25f;
    private float analogPainettuArvo = 0.5f;
    private float triggeriPainettuArvo = 0.8f;

    protected static final int NÄPPÄIN_A = GLFW_JOYSTICK_1;
    protected static final int NÄPPÄIN_B = GLFW_JOYSTICK_2;
    protected static final int NÄPPÄIN_X = GLFW_JOYSTICK_3;
    protected static final int NÄPPÄIN_Y = GLFW_JOYSTICK_4;
    protected static final int NÄPPÄIN_L = GLFW_JOYSTICK_5;
    protected static final int NÄPPÄIN_R = GLFW_JOYSTICK_6;
    protected static final int NÄPPÄIN_START = GLFW_JOYSTICK_8;
    protected static final int NÄPPÄIN_SELECT = GLFW_JOYSTICK_7;
    protected static final int ANALOG_L_CLICK = GLFW_JOYSTICK_9;
    protected static final int ANALOG_R_CLICK = GLFW_JOYSTICK_10;
    protected static final int DPAD_NEUTRAALI = 0;
    protected static final int DPAD_YLÖS = 1;
    protected static final int DPAD_OIKEA = 2;
    protected static final int DPAD_ALAS = 4;
    protected static final int DPAD_VASEN = 8;
    protected static final int DPAD_YLÄVASEN = GLFW_HAT_LEFT | GLFW_HAT_UP;
    protected static final int DPAD_YLÄOIKEA = GLFW_HAT_RIGHT | GLFW_HAT_UP;
    protected static final int DPAD_ALAVASEN = GLFW_HAT_LEFT | GLFW_HAT_DOWN;
    protected static final int DPAD_ALAOIKEA = GLFW_HAT_RIGHT | GLFW_HAT_DOWN;
    protected static final int ANALOG_L_VASEN = 0;
    protected static final int ANALOG_L_OIKEA = 1;
    protected static final int ANALOG_L_YLÖS = 2;
    protected static final int ANALOG_L_ALAS = 3;
    protected static final int ANALOG_R_VASEN = 4;
    protected static final int ANALOG_R_OIKEA = 5;
    protected static final int ANALOG_R_YLÖS = 6;
    protected static final int ANALOG_R_ALAS = 7;
    protected static final int TRIGGERI_VASEN = 8;
    protected static final int TRIGGERI_OIKEA = 9;

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
        this.joystickButtons = new boolean[GLFW_JOYSTICK_LAST];
        for (int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
            joystickButtons[i] = false;
        }
        this.joystickHats = new boolean[16];
        for (int i = 0; i < 16; i++) {
            this.joystickHats[i] = false;
        }
        this.joystickAxes = new boolean[10];
        for (int i = 0; i < 10; i++) {
            this.joystickAxes[i] = false;
        }
        this.joystickAxisPresses = new int[10];
        for (int i = 0; i < 10; i++) {
            joystickAxisPresses[i] = 0;
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
        else if (keyPresses[key] > holdWaitFrames) {
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

    public int getJoystickId() {
        return ohjainNumero;
    }

    public int getJoystickEvent() {
        return ohjainEvent;
    }

    public void asetaOhjaimenTila(int jid, int event) {
        this.ohjainNumero = jid;
        this.ohjainEvent = event;
    }

    public ByteBuffer getJoystickButtons(int jid, int event) {
        return glfwGetJoystickButtons(jid);
    }

    public boolean isJoystickButtonDown(int button) {
        boolean pressed = false;
        ByteBuffer pressedButtons = glfwGetJoystickButtons(ohjainNumero);
        if (pressedButtons != null) {
            for (int i = 0; i < pressedButtons.capacity(); i++) {
                byte pressedButton = pressedButtons.get();
                if (button == i && pressedButton == GLFW_PRESS) {
                    pressed = true;
                }
            }
        }
        return pressed;
    }

    public boolean isJoystickButtonPressed(int button) {
        return (isJoystickButtonDown(button) && !joystickButtons[button]);
    }

    public boolean isJoystickButtonReleased(int button) {
        return (!isJoystickButtonDown(button) && joystickButtons[button]);
    }

    public boolean isJoystickHatDown(int hatButton) {
        boolean pressed = false;
        ByteBuffer pressedHats = glfwGetJoystickHats(ohjainNumero);
        if (pressedHats != null) {
            for (int i = 0; i < pressedHats.capacity(); i++) {
                byte pressedHat = pressedHats.get();
                if ((hatButton == pressedHat) && pressedHat != 0) {
                    pressed = true;
                }
            }
            return pressed;
        }
        else return false;
    }

    public boolean isJoystickHatPressed(int hatButton) {
        return (isJoystickHatDown(hatButton) && !joystickHats[hatButton]);
    }

    public boolean isJoystickHatReleased(int hatButton) {
        return (!isJoystickHatDown(hatButton) && joystickHats[hatButton]);
    }

    public boolean isJoystickAnalogDown(int axis) {
        boolean pressed = false;
        FloatBuffer axes = glfwGetJoystickAxes(ohjainNumero);
        if (axes != null) {
            for (int i = 0; i < axes.capacity(); i++) {
                float pressedAxis = axes.get();
                switch (axis) {
                    case ANALOG_L_VASEN -> {
                        if (i == 0 && pressedAxis < -analogPainettuArvo) {
                            pressed = true;
                        }
                        else if (i == 0 && pressedAxis > -analogDeadzone) {
                            pressed = false;
                        }
                    }
                    case ANALOG_L_OIKEA -> {
                        if (i == 0 && pressedAxis > analogPainettuArvo) {
                            pressed = true;
                        }
                        else if (i == 0 && pressedAxis < analogDeadzone) {
                            pressed = false;
                        }
                    }
                    case ANALOG_L_YLÖS -> {
                        if (i == 1 && pressedAxis < -analogPainettuArvo) {
                            pressed = true;
                        }
                        else if (i == 0 && pressedAxis > -analogDeadzone) {
                            pressed = false;
                        }
                    }
                    case ANALOG_L_ALAS -> {
                        if (i == 1 && pressedAxis > analogPainettuArvo) {
                            pressed = true;
                        }
                        else if (i == 0 && pressedAxis < analogDeadzone) {
                            pressed = false;
                        }
                    }
                    case ANALOG_R_VASEN -> {
                        if (i == 2 && pressedAxis < -analogPainettuArvo) {
                            pressed = true;
                        }
                    }
                    case ANALOG_R_OIKEA -> {
                        if (i == 2 && pressedAxis > analogPainettuArvo) {
                            pressed = true;
                        }
                    }
                    case ANALOG_R_YLÖS -> {
                        if (i == 3 && pressedAxis < -analogPainettuArvo) {
                            pressed = true;
                        }
                    }
                    case ANALOG_R_ALAS -> {
                        if (i == 3 && pressedAxis > analogPainettuArvo) {
                        pressed = true;
                        }
                    }
                    case TRIGGERI_VASEN -> {
                        if (i == 4 && pressedAxis > triggeriPainettuArvo) {
                            pressed = true;
                        }
                    }
                    case TRIGGERI_OIKEA -> {
                        if (i == 5 && pressedAxis > triggeriPainettuArvo) {
                            pressed = true;
                        }
                    }
                }
            }
        }
        return pressed;
    }

    public boolean isJoystickAnalogPressed(int axis) {
        return (isJoystickAnalogDown(axis) && !joystickAxes[axis]);
    }

    public boolean isJoystickAnalogReleased(int axis) {
        return (!isJoystickAnalogDown(axis) && joystickAxes[axis]);
    }

    public boolean isJoystickAnalogHeld(int axis) {
        if (isJoystickAnalogDown(axis) && !joystickAxes[axis]) {
            return true;
        }
        else if (joystickAxisPresses[axis] > holdWaitFrames) {
            return joystickAxisPresses[axis] % 2 == 0;
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
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
            try {
                mouseButtons[i] = isMouseButtonDown(i);
            }
            catch (Exception e) {
                System.out.println("invalid mouse button " + i);
            }
        }
        for (int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
            try {
                joystickButtons[i] = isJoystickButtonDown(i);
            }
            catch (Exception e) {
                System.out.println("invalid joystick button " + i);
            }
        }
        for (int i = 0; i < 16; i++) {
            try {
                joystickHats[i] = isJoystickHatDown(i);
            }
            catch (Exception e) {
                System.out.println("invalid joystick hat button " + i);
            }
        }
        FloatBuffer axisValues = glfwGetJoystickAxes(ohjainNumero);
        if (axisValues != null) {
            try {
                joystickAxes[ANALOG_L_VASEN] = axisValues.get(0) < -analogPainettuArvo;
                if (joystickAxes[ANALOG_L_VASEN]) joystickAxisPresses[ANALOG_L_VASEN]++;
                else joystickAxisPresses[ANALOG_L_VASEN] = 0;
                joystickAxes[ANALOG_L_OIKEA] = axisValues.get(0) > analogPainettuArvo;
                if (joystickAxes[ANALOG_L_OIKEA]) joystickAxisPresses[ANALOG_L_OIKEA]++;
                else joystickAxisPresses[ANALOG_L_OIKEA] = 0;
                joystickAxes[ANALOG_L_YLÖS] = axisValues.get(1) < -analogPainettuArvo;
                if (joystickAxes[ANALOG_L_YLÖS]) joystickAxisPresses[ANALOG_L_YLÖS]++;
                else joystickAxisPresses[ANALOG_L_YLÖS] = 0;
                joystickAxes[ANALOG_L_ALAS] = axisValues.get(1) > analogPainettuArvo;
                if (joystickAxes[ANALOG_L_ALAS]) joystickAxisPresses[ANALOG_L_ALAS]++;
                else joystickAxisPresses[ANALOG_L_ALAS] = 0;
                joystickAxes[ANALOG_R_VASEN] = axisValues.get(2) < -analogPainettuArvo;
                if (joystickAxes[ANALOG_R_VASEN]) joystickAxisPresses[ANALOG_R_VASEN]++;
                else joystickAxisPresses[ANALOG_R_VASEN] = 0;
                joystickAxes[ANALOG_R_OIKEA] = axisValues.get(2) > analogPainettuArvo;
                if (joystickAxes[ANALOG_R_OIKEA]) joystickAxisPresses[ANALOG_R_OIKEA]++;
                else joystickAxisPresses[ANALOG_R_OIKEA] = 0;
                joystickAxes[ANALOG_R_YLÖS] = axisValues.get(3) < -analogPainettuArvo;
                if (joystickAxes[ANALOG_R_YLÖS]) joystickAxisPresses[ANALOG_R_YLÖS]++;
                else joystickAxisPresses[ANALOG_R_YLÖS] = 0;
                joystickAxes[ANALOG_R_ALAS] = axisValues.get(3) > analogPainettuArvo;
                if (joystickAxes[ANALOG_R_ALAS]) joystickAxisPresses[ANALOG_R_ALAS]++;
                else joystickAxisPresses[ANALOG_R_ALAS] = 0;
                joystickAxes[TRIGGERI_VASEN] = axisValues.get(4) > triggeriPainettuArvo;
                if (joystickAxes[TRIGGERI_VASEN]) joystickAxisPresses[TRIGGERI_VASEN]++;
                else joystickAxisPresses[TRIGGERI_VASEN] = 0;
                joystickAxes[TRIGGERI_OIKEA] = axisValues.get(5) > triggeriPainettuArvo;
                if (joystickAxes[TRIGGERI_OIKEA]) joystickAxisPresses[TRIGGERI_OIKEA]++;
                else joystickAxisPresses[TRIGGERI_OIKEA] = 0;
            }
            catch (Exception e) {
                System.out.println("invalid joystick axis ");
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
