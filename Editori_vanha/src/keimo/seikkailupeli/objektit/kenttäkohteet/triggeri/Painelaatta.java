package keimo.seikkailupeli.objektit.kenttäkohteet.triggeri;

import keimo.seikkailupeli.objektit.entityt.npc.*;

import java.util.ArrayList;

public class Painelaatta extends Triggeri {

    public Painelaatta(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY);
        super.nimi = "Painelaatta";

        if (ominaisuusLista != null) {
            this.lisäOminaisuudet = new ArrayList<>();
            Vihollinen vihollinen = new Pikkuvihu(sijX, sijY, null);
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("vihollinen=")) {
                    vihollinen = (Vihollinen)Vihollinen.luoEntityTiedoilla(ominaisuus.substring(11), 0, 0, null);
                }
            }
            super.tiedostonNimi = "painelaatta_" + vihollinen.annaNimi() + ".png";
            super.vaadittuVihollinen = vihollinen;
            super.katsomisTeksti = "Tähän täytyy saada " + vihollinen.annaNimi();
            päivitäLisäOminaisuudet();
        }
        else {
            this.lisäOminaisuuksia = false;
            super.tiedostonNimi = "painelaatta.png";
            super.katsomisTeksti = "Tähän ei ole määritelty vihollista.";
        }

        super.vaadittuEsine = null;        
        super.asetaTiedot();
    }

    @Override
    public void triggeröi() {
        super.triggeröi();
    }
    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Painelaatta";
            case "genetiivi":    return "Painelaatan";
            case "essiivi":      return "Painelaattana";
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
        if (this.lisäOminaisuudet != null) {
            this.lisäOminaisuuksia = true;
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("vihollinen="));
            this.lisäOminaisuudet.add("vihollinen=" + this.annaVaadittuVihollinen().annaNimi());
        }
    }
}
