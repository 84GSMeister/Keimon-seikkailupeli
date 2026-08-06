package keimo.seikkailupeli.assets;

import keimo.seikkailupeli.assets.dialogi.VuoropuheDialogiPätkä;
import keimo.seikkailupeli.assets.huone.Huone;
import keimo.seikkailupeli.assets.tarina.TarinaPätkä;

import java.util.HashMap;

public class PeliTiedosto {
    
    protected HashMap<Integer, Huone> huoneKartta = new HashMap<Integer, Huone>();
    protected HashMap<String, TarinaPätkä> tarinaKartta = new HashMap<>();
    protected HashMap<String, VuoropuheDialogiPätkä> vuoropuheDialogiKartta = new HashMap<>();

    public PeliTiedosto(HashMap<Integer, Huone> huoneKartta, HashMap<String, TarinaPätkä> tarinaKartta, HashMap<String, VuoropuheDialogiPätkä> vuoropuheDialogiKartta) {
        this.huoneKartta = huoneKartta;
        this.tarinaKartta = tarinaKartta;
        this.vuoropuheDialogiKartta = vuoropuheDialogiKartta;
    }

    public HashMap<Integer, Huone> annaHuoneKartta() {
        return huoneKartta;
    }

    public HashMap<String, TarinaPätkä> annaTarinaKartta() {
        return tarinaKartta;
    }

    public HashMap<String, VuoropuheDialogiPätkä> annaDialogiKartta() {
        return vuoropuheDialogiKartta;
    }
}
