package keimo.seikkailupeli.objektit.kenttäkohteet.triggeri;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.entityt.npc.*;

import java.util.ArrayList;

public class Painelaatta extends Triggeri {
    
    private Renderöitävä eiVihuaTekstuuri = Assets.annaTekstuuri("painelaatta");
    private Renderöitävä vakioTekstuuriPikkuvihu = Assets.annaTekstuuri("painelaatta_pikkuvihu");
    private Renderöitävä painettuTekstuuriPikkuvihu = Assets.annaTekstuuri("painelaatta_pikkuvihu_painettu");
    private Renderöitävä vakioTekstuuriPahavihu = Assets.annaTekstuuri("painelaatta_pahavihu");
    private Renderöitävä painettuTekstuuriPahavihu = Assets.annaTekstuuri("painelaatta_pahavihu_painettu");

    public Painelaatta(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY);
        super.nimi = "Painelaatta";
        if (ominaisuusLista != null) {
            Vihollinen vihollinen = new Pikkuvihu(sijX, sijY, null);
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("vihollinen=")) {
                    vihollinen = (Vihollinen)Vihollinen.luoEntityTiedoilla(ominaisuus.substring(11), 0, 0, null);
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
        }
        else {
            super.tiedostonNimi = "painelaatta.png";
            super.katsomisTeksti = "Tähän ei ole määritelty vihollista.";
            super.tekstuuri = eiVihuaTekstuuri;
        }

        super.vaadittuEsine = null;
        päivitäLisäOminaisuudet();
        super.asetaTiedot();
    }

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

    private void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet != null) {
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("vihollinen="));
            if (this.annaVaadittuVihollinen() != null) this.lisäOminaisuudet.add("vihollinen=" + this.annaVaadittuVihollinen().annaNimi());
        }
    }
}
