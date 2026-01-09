package keimo.keimoengine.ikkuna;

import keimo.keimoengine.io.Input;

import java.util.ArrayList;

import org.joml.Matrix4f;

/**
 * Yleinen Ikkuna-luokka. Mahdollistaa ikkunan toteutuksen eri ikkunointikirjastoilla.
 */

public abstract class Ikkuna {
    protected long window;
    protected int windowedWidth, windowedHeight;
    protected int width, height;
    protected boolean fullscreen, vsync;
    protected Input input;
    protected boolean hasResized;
    protected Matrix4f view;
    protected ArrayList<String> resoluutiot = new ArrayList<>();

    public Ikkuna(String title, boolean fullscreen, int width, int height) {
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
        setSize(width, height);
        setView(width, height);
        hasResized = false;
    }

    public Matrix4f getView() {
		return view;
	}

    public ArrayList<String> annaResoluutiot() {
        return resoluutiot;
    }

    public void setView(int width, int height) {
        view = new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
    }

    public abstract boolean shouldClose();

    public abstract void swapBuffers();

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public abstract void setFullscreen(boolean fullscreen, boolean changeResolution);

    public abstract void setMonitor(int monitor);

    public abstract void setVSync(boolean vsync);

    public abstract void update();

    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public boolean isFullscreen() {return fullscreen;}
    public boolean isVsync() {return vsync;}
    public boolean hasResized() {return hasResized;}
    public long getWindow() {return window;}
    public Input getInput() {return input;}
}
