package keimo.Kenttäkohteet;

import keimo.NPCt.Pikkuvihu;

import javax.swing.ImageIcon;

public class PainelaattaPikkuvihu extends Triggeri {

    @Override
    public void triggeröi() {
        super.triggeröi();
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/painelaatta_pikkuvihu_painettu.png");
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
    
    PainelaattaPikkuvihu(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Painelaatta (pikkuvihu)";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/painelaatta_pikkuvihu.png");
        super.katsomisTeksti = "Tähän täytyy varmaankin saada vihollinen.";
        super.vaadittuEsine = null;
        super.vaadittuVihollinen = new Pikkuvihu(sijX, sijY, null);
        super.asetaTiedot();
    }
}
