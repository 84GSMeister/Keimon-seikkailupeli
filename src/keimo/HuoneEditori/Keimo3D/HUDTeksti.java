package keimo.HuoneEditori.Keimo3D;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.Font;

public class HUDTeksti implements GLEventListener {

    static TextRenderer renderer;

    @Override
    public void display(GLAutoDrawable glautodrawable) {
        renderer.beginRendering(glautodrawable.getSurfaceWidth(), glautodrawable.getSurfaceHeight());
        renderer.setColor(1.0f, 0.2f, 0.2f, 0.8f);
        renderer.draw("FPS: " + Keimo3D.fps + " (" + Keimo3D.targetFPS + ")", 0, 240);
        renderer.draw("X: " + Keimo3D.xSij, 0, 220);
        renderer.draw("Y: " + Keimo3D.ySij, 0, 200);
        renderer.draw("Z: " + Keimo3D.zSij, 0, 180);
        renderer.draw("H-nopeus: " + Keimo3D.hSpeed, 0, 160);
        renderer.draw("V-nopeus: " + Keimo3D.vSpeed, 0, 140);
        renderer.draw("Kameran kohde X: " + Keimo3D.xKohde, 0, 120);
        renderer.draw("Kameran kohde Y: " + Keimo3D.yKohde, 0, 100);
        renderer.draw("Kameran kohde Z: " + Keimo3D.zKohde, 0, 80);
        renderer.draw("Y-kääntö (Yaw): " + Keimo3D.yaw, 0, 60);
        renderer.draw("X-kääntö (Pitch): " + Keimo3D.pitch, 0, 40);
        renderer.draw("Z-kääntö (Roll): " + Keimo3D.roll, 0, 20);
        renderer.endRendering();
    }

    @Override
    public void dispose(GLAutoDrawable glautodrawable) {
        
    }

    @Override
    public void init(GLAutoDrawable glautodrawable) {
        final GL2 gl = glautodrawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 14));
    }

    @Override
    public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {

    }
    
}
