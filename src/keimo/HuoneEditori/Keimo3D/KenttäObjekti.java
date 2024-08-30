package keimo.HuoneEditori.Keimo3D;

import com.jogamp.opengl.GL2;

import keimo.Kenttäkohteet.KenttäKohde;
import keimo.Kenttäkohteet.VisuaalinenObjekti;

public class KenttäObjekti {

    int textureInt;
    String textureFileName;
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
    
    public KenttäObjekti(String objectName, String[] propertyList, int posX, int posY, int posZ) {
        KenttäKohde k = KenttäKohde.luoObjektiTiedoilla(objectName, true, posX, posZ, propertyList);
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        if (k instanceof VisuaalinenObjekti) {
            this.textureFileName = "tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/" + k.annaKuvatiedostonNimi();
        }
        else {
            this.textureFileName = "tiedostot/kuvat/kenttäkohteet/" + k.annaKuvatiedostonNimi();
        }
    }
}
