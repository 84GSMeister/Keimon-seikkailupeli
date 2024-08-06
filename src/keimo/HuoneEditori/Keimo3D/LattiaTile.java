package keimo.HuoneEditori.Keimo3D;

import com.jogamp.opengl.GL2;

public class LattiaTile {

    int textureInt;
    String textureFileName;
    int floorPosX;
    int floorPosY;
    int floorPosZ;
    int kääntö;
    boolean xPeilaus;
    boolean yPeilaus;

    public void piirrä(GL2 gl) {
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textureInt);
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(1f, 1f, 1f);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(floorPosX - 0.5f, floorPosY, floorPosZ - 0.5f);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(floorPosX + 0.5f, floorPosY, floorPosZ - 0.5f);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(floorPosX + 0.5f, floorPosY, floorPosZ + 0.5f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(floorPosX - 0.5f, 0, floorPosZ + 0.5f);
        gl.glEnd();
    }
    
    public LattiaTile(String[] propertyList, int posX, int posY, int posZ) {
        this.floorPosX = posX;
        this.floorPosY = posY;
        this.floorPosZ = posZ;

        for (String s : propertyList) {
            if (s.startsWith("kuva=")) {
                this.textureFileName = "tiedostot/kuvat/maasto/" + s.substring(5);
            }
            else if (s.startsWith("kääntö=")) {
                try {
                    this.kääntö = Integer.parseInt(s.substring(7));
                }
                catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
            }
            else if (s.startsWith("x-peilaus=")) {
                if (s.substring(10).startsWith("kyllä")) {
                    this.xPeilaus = true;
                }
            }
            else if (s.startsWith("y-peilaus=")) {
                if (s.substring(10).startsWith("kyllä")) {
                    this.yPeilaus = true;
                }
            }
        }
    }
}
