package keimo.keimoengine.assets;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Tekstuuri;

import java.util.HashMap;

public class GUITekstuurit {
    
    private static HashMap<String, Renderöitävä> tekstuurit = new HashMap<>();

    public static void lataaTekstuurit() {
        tekstuurit.put("virhe", new Tekstuuri("tiedostot/kuvat/muut/virhetekstuuri.png"));
        tekstuurit.put("tooltip_pohja", new Tekstuuri("tiedostot/kuvat/gui/komponentit/tooltip_teksti_pohja.png"));
        tekstuurit.put("lataus_punainen", new Tekstuuri("tiedostot/kuvat/gui/komponentit/palkki_punainen.png"));
        tekstuurit.put("lataus_vihreä", new Tekstuuri("tiedostot/kuvat/gui/komponentit/palkki_vihreä.png"));
    }

    public static Renderöitävä annaTekstuuri(String nimi) {
        if (tekstuurit.containsKey(nimi)) {
            return tekstuurit.get(nimi);
        }
        else {
            assert false : "Tekstuuria ei löytynyt: " + nimi;
            return tekstuurit.get("vakio");
        }
    }
}
