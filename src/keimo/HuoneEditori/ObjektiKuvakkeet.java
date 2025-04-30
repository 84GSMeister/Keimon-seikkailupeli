package keimo.HuoneEditori;

import keimo.Huone;
import keimo.Peli;

import java.util.HashMap;

import javax.swing.ImageIcon;

public class ObjektiKuvakkeet {

    public static HashMap<String, ObjektiKuvake> objektiKuvakkeet = new HashMap<>();
    public static HashMap<String, ObjektiKuvake> tileKuvakkeet = new HashMap<>();
    public static HashMap<String, ObjektiKuvake> entityKuvakkeet = new HashMap<>();

    public static void luoObjektiKuvakkeet() {
        for (Huone huone : Peli.huoneKartta.values()) {
            for (int y = 0; y < huone.annaKoko(); y++) {
                for (int x = 0; x < huone.annaKoko(); x++) {
                    if (huone.annaHuoneenKenttäSisältö()[x][y] != null) {
                        String tiedostonNimi = huone.annaHuoneenKenttäSisältö()[x][y].annaKuvanTiedostoNimi();
                        if (tiedostonNimi != null) {
                            if (!objektiKuvakkeet.containsKey(tiedostonNimi)) {
                                objektiKuvakkeet.put(tiedostonNimi, new ObjektiKuvake("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi));
                            }
                        }
                    }
                    if (huone.annaHuoneenMaastoSisältö()[x][y] != null) {
                        String tiedostonNimi = huone.annaHuoneenMaastoSisältö()[x][y].annaKuvanTiedostoNimi();
                        if (tiedostonNimi != null) {
                            if (!tileKuvakkeet.containsKey(tiedostonNimi)) {
                                //String[] tileKuvakkeet = {"kuva=" + tiedostonNimi};
                                //tiedostonNimi = tiedostonNimi.substring(0, tiedostonNimi.length()-4);
                                tileKuvakkeet.put(tiedostonNimi, new ObjektiKuvake("tiedostot/kuvat/maasto/" + tiedostonNimi));
                            }
                        }
                    }
                    if (huone.annaHuoneenNPCSisältö()[x][y] != null) {
                        String tiedostonNimi = huone.annaHuoneenNPCSisältö()[x][y].annaKuvanTiedostoNimi();
                        if (tiedostonNimi != null) {
                            if (!entityKuvakkeet.containsKey(tiedostonNimi)) {
                                entityKuvakkeet.put(tiedostonNimi, new ObjektiKuvake("tiedostot/kuvat/entity/" + tiedostonNimi));
                            }
                        }
                    }
                }
            }
        }
    }

    public static class ObjektiKuvake extends ImageIcon {
        public ObjektiKuvake(String tiedostonNimi) {
            super(tiedostonNimi);
        }
    }
}
