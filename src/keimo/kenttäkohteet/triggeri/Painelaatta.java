package keimo.kenttäkohteet.triggeri;

import keimo.entityt.npc.NPC;
import keimo.entityt.npc.Pahavihu;
import keimo.entityt.npc.Pikkuvihu;
import keimo.entityt.npc.Vihollinen;
import keimo.keimoEngine.grafiikat.Tekstuuri;

public class Painelaatta extends Triggeri {
    
    private Tekstuuri vakioTekstuuriPikkuvihu = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/painelaatta_Pikkuvihu.png");
    private Tekstuuri painettuTekstuuriPikkuvihu = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/painelaatta_Pikkuvihu_painettu.png");
    private Tekstuuri vakioTekstuuriPahavihu = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/painelaatta_Pahavihu.png");
    private Tekstuuri painettuTekstuuriPahavihu = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/painelaatta_Pahavihu_painettu.png");

    @Override
    public void triggeröi() {
        super.triggeröi();
        if (vaadittuVihollinen instanceof Pikkuvihu) {
            super.tekstuuri = painettuTekstuuriPikkuvihu;
        }
        else if (vaadittuVihollinen instanceof Pahavihu) {
            super.tekstuuri = painettuTekstuuriPahavihu;
        }
    }
    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Painelaatta";
            case "genetiivi":    return "Painelaatan";
            case "esiivi":       return "Painelaattana";
            case "partitiivi":   return "Painelaattaa";
            case "translatiivi": return "Painelaataksi";
            case "inessiivi":    return "Painelaatassa";
            case "elatiivi":     return "Painelaatasta";
            case "illatiivi":    return "Painelaattaan";
            case "adessiivi":    return "Painelaatalla";
            case "ablatiivi":    return "Painelaatalta";
            case "allatiivi":    return "Painelaalle";
            default:             return "Painelaatta";
        }
    }

    public void päivitäLisäOminaisuudet() {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[1];
        this.lisäOminaisuudet[0] = "vihollinen=" + this.annaVaadittuVihollinen().annaNimi();
    }
    
    public Painelaatta(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Painelaatta";

        if (ominaisuusLista != null) {
            Vihollinen vihollinen = new Pikkuvihu(sijX, sijY, null);
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("vihollinen=")) {
                    vihollinen = (Vihollinen)NPC.luoEntityTiedoilla(ominaisuus.substring(11), false, 0, 0, null);
                }
            }
            super.tiedostonNimi = "painelaatta_" + vihollinen.annaNimi() + ".png";
            super.vaadittuVihollinen = vihollinen;
            super.katsomisTeksti = "Tähän täytyy saada " + vihollinen.annaNimi();
            if (super.vaadittuVihollinen instanceof Pikkuvihu) {
                super.tekstuuri = vakioTekstuuriPikkuvihu;
            }
            else if (super.vaadittuVihollinen instanceof Pahavihu) {
                super.tekstuuri = vakioTekstuuriPahavihu;
            }
            päivitäLisäOminaisuudet();
        }
        else {
            this.lisäOminaisuuksia = false;
            super.tiedostonNimi = "painelaatta.png";
            super.katsomisTeksti = "Tähän täytyy varmaankin saada vihollinen.";
        }

        super.vaadittuEsine = null;        
        super.asetaTiedot();
    }
}
