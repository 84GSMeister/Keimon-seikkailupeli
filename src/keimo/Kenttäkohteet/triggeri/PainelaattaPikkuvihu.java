package keimo.kenttäkohteet.triggeri;

import keimo.entityt.npc.Pikkuvihu;
import keimo.keimoEngine.grafiikat.Tekstuuri;

import javax.swing.ImageIcon;

public class PainelaattaPikkuvihu extends Triggeri {

    private Tekstuuri vakioTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/painelaatta_pikkuvihu.png");
    private Tekstuuri painettuTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/painelaatta_pikkuvihu_painettu.png");

    @Override
    public void triggeröi() {
        super.triggeröi();
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/painelaatta_pikkuvihu_painettu.png");
        super.tekstuuri = painettuTekstuuri;
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
    
    public PainelaattaPikkuvihu(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Painelaatta (pikkuvihu)";
        super.tiedostonNimi = "painelaatta_pikkuvihu.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = vakioTekstuuri;
        super.katsomisTeksti = "Tähän täytyy varmaankin saada vihollinen.";
        super.vaadittuEsine = null;
        super.vaadittuVihollinen = new Pikkuvihu(sijX, sijY, null);
        super.asetaTiedot();
    }
}
