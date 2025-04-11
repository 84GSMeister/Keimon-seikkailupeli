package keimo.keimoEngine.ikkuna;

import keimo.keimoEngine.io.Input;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWVidMode.Buffer;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.*;


public class Window {
    private long window;
    private int windowedWidth, windowedHeight;
    private int width, height;
    private boolean fullscreen, vsync;
    private Input input;
    private boolean hasResized;
    private GLFWWindowSizeCallback windowSizeCallback;
    private Matrix4f view;
    private static ArrayList<String> resoluutiot = new ArrayList<>();

    public static void setCallbacks() {
        glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
            }
        });
    }

    private void setLocalCallbacks() {
        windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long argWindow, int argWidth, int argHeight) {
                windowedWidth = width;
                windowedHeight = height;
                width = argWidth;
                height = argHeight;
                hasResized = true;
            }
        };
        glfwSetWindowSizeCallback(window, windowSizeCallback);
    }

    public Window(String title, boolean fullscreen, int width, int height) {
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 1);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        //glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        //glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        Buffer videoModeBuffer = glfwGetVideoModes(glfwGetPrimaryMonitor());
        videoModeBuffer.forEach(e -> {
            String resoluutio = e.width() + "x" + e.height();
            if (!resoluutiot.contains(resoluutio)) resoluutiot.add(resoluutio);
        });

        setSize(width, height);
        setView(width, height);
        window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window!");
        }
        if (!fullscreen) {
            GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vid.width()-width)/2, (vid.height()-height)/2);
        }
        glfwShowWindow(window);
        glfwMakeContextCurrent(window); // Make the OpenGL context current
        setLocalCallbacks();
        input = new Input(window);
        hasResized = false;
    }

    public Matrix4f getView() {
		return view;
	}

    public static ArrayList<String> annaResoluutiot() {
        return resoluutiot;
    }

    public void setView(int width, int height) {
        view = new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
    }

    public void cleanUp() {
        windowSizeCallback.close();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers() {
        glfwSwapBuffers(window); // swap the color buffers
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setFullscreen(boolean fullscreen, boolean changeResolution) {
        this.fullscreen = fullscreen;
        System.out.println(windowedWidth + " " + windowedHeight);
        GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (fullscreen) {
                if (changeResolution) {
                    glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, this.width, this.height, vid.refreshRate());
                }
                else glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, vid.width(), vid.height(), vid.refreshRate());
            }
            else {
                if (changeResolution) {
                    glfwSetWindowMonitor(window, 0, this.windowedWidth/2, this.windowedHeight/2, 800, 600, vid.refreshRate());
                }
                else {
                    if (this.windowedWidth != 0 && this.windowedHeight != 0) {
                        if (vid.width() != 0 && vid.height() != 0) glfwSetWindowMonitor(window, 0, (vid.width()-this.windowedWidth)/2, (vid.height()-this.windowedHeight)/2, this.windowedWidth, this.windowedHeight, vid.refreshRate());
                        else glfwSetWindowMonitor(window, 0, this.windowedWidth/2, this.windowedHeight/2, 800, 600, vid.refreshRate());
                    }
                    else {
                        if (vid.width() != 0 && vid.height() != 0) glfwSetWindowMonitor(window, 0, (vid.width()-this.windowedWidth)/2, (vid.height()-this.windowedHeight)/2, 800, 600, vid.refreshRate());
                        else glfwSetWindowMonitor(window, 0, 0, 0, 800, 600, vid.refreshRate());
                    }
                }
            }
    }

    public void setVSync(boolean vsync) {
        this.vsync = vsync;
        if (vsync) glfwSwapInterval(1);
        else glfwSwapInterval(0); 
    }

    public void update() {
        input.update();
        hasResized = false;
        glfwPollEvents();
    }

    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public boolean isFullscreen() {return fullscreen;}
    public boolean isVsync() {return vsync;}
    public boolean hasResized() {return hasResized;}
    public long getWindow() {return window;}
    public Input getInput() {return input;}
}
