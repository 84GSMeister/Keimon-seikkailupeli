package keimo.HuoneEditori.Keimo3D;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Objektit3D implements GLEventListener {

    protected static ArrayList<LattiaTile> lattiaTilet = new ArrayList<>();
    protected static ArrayList<KenttäObjekti> kenttäObjektit = new ArrayList<>();
    TestiPyramidi testiPyramidi = new TestiPyramidi(1, 0, -6);
    TestiKuutio testiKuutio = new TestiKuutio(-1, 2, -3);

    private GLU glu = new GLU();

    public Objektit3D() {
        kenttäObjektit.clear();
        lattiaTilet.clear();
        Kenttä.tallennaVäliaikainenTiedosto();
        Kenttä.lataaGrafiikatKSTstä();
    }

    @Override
    public void display(GLAutoDrawable glautodrawable) {
        GL2 gl = glautodrawable.getGL().getGL2();
        piirräLattia(gl);
        piirräObjektit(gl);
        piirräTestiObjektit(gl);
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
        try {
            for (LattiaTile l : lattiaTilet) {
                File im = new File(l.textureFileName);
                Texture t = TextureIO.newTexture(im, true);
                l.textureInt = t.getTextureObject(gl);
            }
            for (KenttäObjekti k : kenttäObjektit) {
                File im = new File(k.textureFileName);
                Texture t = TextureIO.newTexture(im, true);
                k.textureInt = t.getTextureObject(gl);
            }
            File im = new File(testiKuutio.textureFileName);
            Texture t = TextureIO.newTexture(im, true);
            testiKuutio.textureInt = t.getTextureObject(gl);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        };
    }

    @Override
    public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
        GL2 gl = glautodrawable.getGL().getGL2();
      
        if (height <= 0)
            height = 1;
            
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
            
        glu.gluPerspective(45f, h, 0.25, 40.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private void piirräLattia(GL2 gl) {
        for (LattiaTile l : lattiaTilet) {
            l.piirrä(gl);
        }
    }

    private void piirräObjektit(GL2 gl) {
        for (KenttäObjekti ko : kenttäObjektit) {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, ko.textureInt);
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(1f, 1f, 1f);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(ko.posX - 0.5f, ko.posY, ko.posZ + 0.5f);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(ko.posX + 0.5f, ko.posY, ko.posZ + 0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(ko.posX + 0.5f, ko.posY + 1, ko.posZ + 0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(ko.posX - 0.5f, ko.posY + 1, ko.posZ + 0.5f);

            // Back Face
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(ko.posX - 0.5f, ko.posY, ko.posZ - 0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(ko.posX - 0.5f, ko.posY + 1, ko.posZ - 0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(ko.posX + 0.5f, ko.posY + 1, ko.posZ - 0.5f);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(ko.posX + 0.5f, ko.posY, ko.posZ - 0.5f);

            // Top Face
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(ko.posX - 0.5f, ko.posY + 1, ko.posZ - 0.5f);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(ko.posX - 0.5f, ko.posY + 1, ko.posZ + 0.5f);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(ko.posX + 0.5f, ko.posY + 1, ko.posZ + 0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(ko.posX + 0.5f, ko.posY + 1, ko.posZ - 0.5f);

            // Bottom Face
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(ko.posX - 0.5f, ko.posY, ko.posZ - 0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(ko.posX + 0.5f, ko.posY, ko.posZ - 0.5f);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(ko.posX + 0.5f, ko.posY, ko.posZ + 0.5f);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(ko.posX - 0.5f, ko.posY, ko.posZ + 0.5f);

            // Right face
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(ko.posX + 0.5f, ko.posY, ko.posZ - 0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(ko.posX + 0.5f, ko.posY + 1, ko.posZ - 0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(ko.posX + 0.5f, ko.posY + 1, ko.posZ + 0.5f);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(ko.posX + 0.5f, ko.posY, ko.posZ + 0.5f);

            // Left Face
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(ko.posX - 0.5f, ko.posY, ko.posZ - 0.5f);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(ko.posX - 0.5f, ko.posY, ko.posZ + 0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(ko.posX - 0.5f, ko.posY + 1, ko.posZ + 0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(ko.posX - 0.5f, ko.posY + 1, ko.posZ - 0.5f);
            gl.glEnd();
        }
    }

    private void piirräTestiObjektit(GL2 gl) {
        testiPyramidi.piirrä(gl);
        testiKuutio.piirrä(gl);
    }

    public class TestiKuutio {

        int textureInt;
        String textureFileName = "tiedostot/kuvat/muut/84gs.jpg";
        int posX;
        int posY;
        int posZ;

        public void piirrä(GL2 gl) {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, textureInt);
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(1f, 1f, 1f);

            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(posX - 0.5f, posY, posZ + 0.5f);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(posX + 0.5f, posY, posZ + 0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(posX + 0.5f, posY + 1, posZ + 0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(posX - 0.5f, posY + 1, posZ + 0.5f);

            // Back Face
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(posX - 0.5f, posY, posZ - 0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(posX - 0.5f, posY + 1, posZ - 0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(posX + 0.5f, posY + 1, posZ - 0.5f);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(posX + 0.5f, posY, posZ - 0.5f);

            // Top Face
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(posX - 0.5f, posY + 1, posZ - 0.5f);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(posX - 0.5f, posY + 1, posZ + 0.5f);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(posX + 0.5f, posY + 1, posZ + 0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(posX + 0.5f, posY + 1, posZ - 0.5f);

            // Bottom Face
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(posX - 0.5f, posY, posZ - 0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(posX + 0.5f, posY, posZ - 0.5f);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(posX + 0.5f, posY, posZ + 0.5f);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(posX - 0.5f, posY, posZ + 0.5f);

            // Right face
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(posX + 0.5f, posY, posZ - 0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(posX + 0.5f, posY + 1, posZ - 0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(posX + 0.5f, posY + 1, posZ + 0.5f);
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(posX + 0.5f, posY, posZ + 0.5f);

            // Left Face
            gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(posX - 0.5f, posY, posZ - 0.5f);
            gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(posX - 0.5f, posY, posZ + 0.5f);
            gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(posX - 0.5f, posY + 1, posZ + 0.5f);
            gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(posX - 0.5f, posY + 1, posZ - 0.5f);
            
            gl.glEnd();
        }
        
        public TestiKuutio(int posX, int posY, int posZ) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
        }
    }

    public class TestiPyramidi {

        int posX;
        int posY;
        int posZ;

        public void piirrä(GL2 gl) {
            gl.glBegin(GL2.GL_TRIANGLES);
                        
            //drawing triangle in all dimensions
            // Front
            gl.glColor3f( 1.0f, 0.0f, 0.0f ); // Red
            gl.glVertex3f( 1.0f, 2.0f, 0.0f ); // Top Of Triangle (Front)
                
            gl.glColor3f( 0.0f, 1.0f, 0.0f ); // Green
            gl.glVertex3f( -1.0f, -1.0f, 1.0f ); // Left Of Triangle (Front)
                
            gl.glColor3f( 0.0f, 0.0f, 1.0f ); // Blue
            gl.glVertex3f( 1.0f, -1.0f, 1.0f ); // Right Of Triangle (Front)
                
            // Right
            gl.glColor3f( 1.0f, 0.0f, 0.0f ); // Red
            gl.glVertex3f( 1.0f, 2.0f, 0.0f ); // Top Of Triangle (Right)
                
            gl.glColor3f( 0.0f, 0.0f, 1.0f ); // Blue
            gl.glVertex3f( 1.0f, -1.0f, 1.0f ); // Left Of Triangle (Right)
                
            gl.glColor3f( 0.0f, 1.0f, 0.0f ); // Green
            gl.glVertex3f( 1.0f, -1.0f, -1.0f ); // Right Of Triangle (Right)
                
            // Left
            gl.glColor3f( 1.0f, 0.0f, 0.0f ); // Red
            gl.glVertex3f( 1.0f, 2.0f, 0.0f ); // Top Of Triangle (Back)
                
            gl.glColor3f( 0.0f, 1.0f, 0.0f ); // Green
            gl.glVertex3f( 1.0f, -1.0f, -1.0f ); // Left Of Triangle (Back)
                
            gl.glColor3f( 0.0f, 0.0f, 1.0f ); // Blue
            gl.glVertex3f( -1.0f, -1.0f, -1.0f ); // Right Of Triangle (Back)
                
            //left
            gl.glColor3f( 0.0f, 1.0f, 0.0f ); // Red
            gl.glVertex3f( 1.0f, 2.0f, 0.0f ); // Top Of Triangle (Left)
                
            gl.glColor3f( 0.0f, 0.0f, 1.0f ); // Blue
            gl.glVertex3f( -1.0f, -1.0f, -1.0f ); // Left Of Triangle (Left)
                
            gl.glColor3f( 0.0f, 1.0f, 0.0f ); // Green
            gl.glVertex3f( -1.0f, -1.0f, 1.0f ); // Right Of Triangle (Left)
                
            gl.glEnd(); // Done Drawing 3d triangle (Pyramid)
        }
        
        public TestiPyramidi(int posX, int posY, int posZ) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
        }
    }
}
