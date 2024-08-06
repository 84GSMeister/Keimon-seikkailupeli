package keimo.HuoneEditori.Keimo3D;

import keimo.Kenttäkohteet.KenttäKohde;
import keimo.Kenttäkohteet.VisuaalinenObjekti;

public class KenttäObjekti {

    int textureInt;
    String textureFileName;
    int posX;
    int posY;
    int posZ;
    
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
