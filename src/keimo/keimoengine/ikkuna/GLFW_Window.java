package keimo.keimoengine.ikkuna;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWVidMode.Buffer;

public class GLFW_Window extends Window {
    private GLFWWindowSizeCallback windowSizeCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWScrollCallback scrollCallback;

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

        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                getInput().setCursorPos(xpos, ypos);
            }
        };
        glfwSetCursorPosCallback(window, cursorPosCallback);

        scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                getInput().setScroll(xoffset, yoffset);
            }
        };
        glfwSetScrollCallback(window, scrollCallback);
    }

    public GLFW_Window(String title, boolean fullscreen, int width, int height) {
        super(title, fullscreen, width, height);
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 1);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        Buffer videoModeBuffer = glfwGetVideoModes(glfwGetPrimaryMonitor());
        videoModeBuffer.forEach(e -> {
            String resoluutio = e.width() + "x" + e.height();
            if (!resoluutiot.contains(resoluutio)) resoluutiot.add(resoluutio);
        });

        setSize(width, height);
        //setView(width, height);
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
        hasResized = false;
    }

    public static ArrayList<String> annaResoluutiot() {
        return resoluutiot;
    }

    public void cleanUp() {
        windowSizeCallback.close();
    }

    @Override
    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    @Override
    public void swapBuffers() {
        glfwSwapBuffers(window); // swap the color buffers
    }

    @Override
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

    @Override
    public void setVSync(boolean vsync) {
        this.vsync = vsync;
        if (vsync) glfwSwapInterval(1);
        else glfwSwapInterval(0); 
    }

    @Override
    public void update() {
        input.update();
        hasResized = false;
        glfwPollEvents();
    }
}

