package keimo.keimoengine.ikkuna;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWJoystickCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowRefreshCallback;
import org.lwjgl.glfw.GLFWVidMode.Buffer;


/**
 * Ikkunan toteutus GLFW-ikkunointikirjastolla. Muita ikkunointijärjestelmiä ei toistaiseksi tueta.
 */

public class GLFW_Ikkuna extends Ikkuna {
    private HashMap<Short, Long> näytöt = new HashMap<>();
    private short valittuNäyttö = 0;
    private boolean käytäAinaPäänäyttöä = false;

    public static void setCallbacks() {
        glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
            }
        });
    }

    private void setLocalCallbacks() {
        glfwSetWindowRefreshCallback(window, new GLFWWindowRefreshCallback() {
            @Override
            public void invoke(long window) {
                hasResized = true;
            }
        });

        glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long argWindow, int argWidth, int argHeight) {
                windowedWidth = width;
                windowedHeight = height;
                width = argWidth;
                height = argHeight;
                hasResized = true;
            }
        });

        glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                //annaNäppäimistöSyöte().setCursorPos(xpos, ypos);
                annaSyöte().setCursorPos(xpos, ypos);
            }
        });

        glfwSetScrollCallback(window, new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                //annaNäppäimistöSyöte().setScroll(xoffset, yoffset);
                annaSyöte().setScroll(xoffset, yoffset);
            }
        });

        glfwSetJoystickCallback(new GLFWJoystickCallback() {
            @Override
            public void invoke(int jid, int event) {
                //annaOhjainSyöte().asetaOhjaimenTila(jid, event);
                annaSyöte().asetaOhjaimenTila(jid, event);
            }
        });
    }

    public GLFW_Ikkuna(String title, boolean fullscreen, int width, int height) {
        super(title, fullscreen, width, height);
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;

        glfwInitHint(GLFW_JOYSTICK_HAT_BUTTONS, GLFW_FALSE);

        // Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

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
        asetaNäytöt();
        window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window!");
        }
        if (!fullscreen) {
            GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vid.width()-width)/2, (vid.height()-height)/2);
        }
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        setLocalCallbacks();
        hasResized = false;
    }

    @Override
    public ArrayList<String> annaResoluutiot() {
        return resoluutiot;
    }

    public HashMap<Short, Long> annaNäytöt() {
        return näytöt;
    }

    private void asetaNäytöt() {
        PointerBuffer monitorsBuffer = glfwGetMonitors();
        for (int i = 0; i < monitorsBuffer.capacity(); i++)  {
            long monitorHandle = monitorsBuffer.get();
            näytöt.put((short)i, monitorHandle);
        }
    }

    @Override
    public void setMonitor(int monitor) {
        valittuNäyttö = (short)monitor;
    }

    @Override
    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    @Override
    public void muutaKokoa(int leveys, int korkeus) {
        super.muutaKokoa(leveys, korkeus);
        glViewport(0, 0, getWidth(), getHeight());
        hasResized = false;
    }

    @Override
    public void swapBuffers() {
        glfwSwapBuffers(window); // swap the color buffers
    }

    @Override
    public void setFullscreen(boolean fullscreen, boolean changeResolution) {
        this.fullscreen = fullscreen;
        GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
        long primaryMonitor = glfwGetPrimaryMonitor();
        long selectedMonitor = näytöt.get(valittuNäyttö);
        if (fullscreen) {
            if (käytäAinaPäänäyttöä) {
                if (changeResolution) {
                    glfwSetWindowMonitor(window, primaryMonitor, 0, 0, this.width, this.height, vid.refreshRate());
                }
                else glfwSetWindowMonitor(window, primaryMonitor, 0, 0, vid.width(), vid.height(), vid.refreshRate());
            }
            else {
                if (changeResolution) {
                    glfwSetWindowMonitor(window, selectedMonitor, 0, 0, this.width, this.height, vid.refreshRate());
                }
                else glfwSetWindowMonitor(window, selectedMonitor, 0, 0, vid.width(), vid.height(), vid.refreshRate());
            }
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
        if (syöte != null) syöte.update();
        glfwPollEvents();
    }

    public void pollEvents() {
        glfwPollEvents();
    }
}

