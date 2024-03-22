package keimo.Kenttäkohteet;

import keimo.Säikeet.ÄänentoistamisSäie;

import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class Portti extends AvattavaEste {

    private boolean päivitäKuvake = true;
    Toolkit t = Toolkit.getDefaultToolkit();

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Portti";
            case "genetiivi": return "Portin";
            case "esiivi": return "Porttina";
            case "partitiivi": return "Porttia";
            case "translatiivi": return "Portiksi";
            case "inessiivi": return "Portissa";
            case "elatiivi": return "Portista";
            case "illatiivi": return "Porttiin";
            case "adessiivi": return "Portilla";
            case "ablatiivi": return "Portilta";
            case "allatiivi": return "Portille";
            default: return "Portti";
        }
    }

    @Override
    protected void avaa(boolean avaus) {
        super.avaa(avaus);
        if (avaus) {
            if (päivitäKuvake) {
                super.kuvake = new ImageIcon(t.createImage("tiedostot/kuvat/kenttäkohteet/portti_auki.gif"));
                ÄänentoistamisSäie.toistaSFX("portti", this.annaSijaintiKentällä());
                päivitäKuvake = false;
            }
        }
        else {
            super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/portti.png");
            päivitäKuvake = true;
        }
    }
    
    Portti(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Portti";
        super.kuvake = new ImageIcon(t.createImage("tiedostot/kuvat/kenttäkohteet/portti.png"));
        super.katsomisTeksti = "portti";
        super.asetaTiedot();
    }
}
