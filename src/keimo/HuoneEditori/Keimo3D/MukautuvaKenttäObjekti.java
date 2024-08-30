package keimo.HuoneEditori.Keimo3D;

import com.jogamp.opengl.GL2;

public class MukautuvaKenttäObjekti extends OBJMalli {

    public ObjektiTyyppi objektiTyyppi;
    public boolean käänteinenSuunta;
    public int liikettäJäljellä;
    public float liikkeenSuuruus;
    public float rotaatioX;
    public float rotaatioY;
    public float rotaatioZ;

    enum ObjektiTyyppi {
        STAATTINEN,
        LIIKKUVA_X_EDESTAKAISIN,
        LIIKKUVA_Y_EDESTAKAISIN,
        LIIKKUVA_Z_EDESTAKAISIN,
        PYÖRIVÄ_Y;
    }

    @Override
    public void piirrä(GL2 gl) {
        gl.glRotatef(rotaatioY, 0, 1, 0);
        super.piirrä(gl);
    }
    
    public MukautuvaKenttäObjekti(String objNimi, float skaala, Point3D origin, boolean käännäYZ, ObjektiTyyppi tyyppi, float liikkeenSuuruus) {
        super(objNimi, skaala, origin, käännäYZ);
        this.objektiTyyppi = tyyppi;
        this.liikkeenSuuruus = liikkeenSuuruus;
    }    
}
