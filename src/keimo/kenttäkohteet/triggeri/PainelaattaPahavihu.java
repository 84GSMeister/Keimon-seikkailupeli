package keimo.kenttäkohteet.triggeri;

import keimo.entityt.npc.Pahavihu;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import javax.swing.ImageIcon;

public class PainelaattaPahavihu extends Triggeri {

    @Override
    public void triggeröi() {
        super.triggeröi();
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/painelaatta_pahavihu_painettu.png");
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Painelaatta";
            case "genetiivi": return "Painelaatan";
            case "esiivi": return "Painelaattana";
            case "partitiivi": return "Painelaattaa";
            case "translatiivi": return "Painelaataksi";
            case "inessiivi": return "Painelaatassa";
            case "elatiivi": return "Painelaatasta";
            case "illatiivi": return "Painelaattaan";
            case "adessiivi": return "Painelaatalla";
            case "ablatiivi": return "Painelaatalta";
            case "allatiivi": return "Painelaalle";
            default: return "Painelaatta";
        }
    }
    
    public PainelaattaPahavihu(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Painelaatta (pahavihu)";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/painelaatta_pahavihu.png");
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Tähän täytyy varmaankin saada vihollinen.";
        super.vaadittuEsine = null;
        super.vaadittuVihollinen = new Pahavihu(sijX, sijY, null);
        super.asetaTiedot();
    }
}
