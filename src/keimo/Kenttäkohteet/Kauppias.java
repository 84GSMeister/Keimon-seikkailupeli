package keimo.Kenttäkohteet;

import keimo.Pelaaja;
import keimo.PääIkkuna;

import javax.swing.ImageIcon;

public final class Kauppias extends NPC_KenttäKohde {

    @Override
    public void näytäDialogi(Esine e) {
        boolean ponuAineksetOstettu = false;
        for (Esine pelaajanEsine : Pelaaja.ostosKori) {
            if (pelaajanEsine instanceof Ponuainekset) {
                ponuAineksetOstettu = true;
                break;
            }
        }
        if (ponuAineksetOstettu) {
            PääIkkuna.avaaPitkäDialogiRuutu("kyläkauppa");
            if (Pelaaja.raha > Pelaaja.ostostenHintaYhteensä) {
                Pelaaja.raha -= Pelaaja.ostostenHintaYhteensä;
                Pelaaja.ostostenHintaYhteensä = 0;
                for (Esine ostos : Pelaaja.ostosKori) {
                    Pelaaja.annaEsine(ostos);
                }
                Pelaaja.tyhjennäOstoskori();
            }
            else {
                Pelaaja.tyhjennäOstoskori();
            }
        }
        else if (Pelaaja.ostostenHintaYhteensä <= 0) {
            PääIkkuna.avaaDialogi(this.annaDialogiKuvake(), "Meinasitko ostaa jotain?", "ASS-Market kassa");
        }
        else if (Pelaaja.raha > Pelaaja.ostostenHintaYhteensä) {
            PääIkkuna.avaaPitkäDialogiRuutu("kauppa_normaali");
            Pelaaja.raha -= Pelaaja.ostostenHintaYhteensä;
            Pelaaja.ostostenHintaYhteensä = 0;
            for (Esine ostos : Pelaaja.ostosKori) {
                Pelaaja.annaEsine(ostos);
            }
            Pelaaja.tyhjennäOstoskori();
        }
        else {
            PääIkkuna.avaaPitkäDialogiRuutu("kauppa_eivaraa");
            Pelaaja.tyhjennäOstoskori();
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Kauppias";
            case "genetiivi": return "Kauppiaan";
            case "esiivi": return "Kauppiaana";
            case "partitiivi": return "Kauppiasta";
            case "translatiivi": return "Kauppiaaksi";
            case "inessiivi": return "Kauppiaassa";
            case "elatiivi": return "Kauppiaasta";
            case "illatiivi": return "Kauppiaaseen";
            case "adessiivi": return "Kauppiaalla";
            case "ablatiivi": return "Kauppiaalta";
            case "allatiivi": return "Kauppiaalle";
            default: return "Kauppias";
        }
    }
    
    public Kauppias (boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kauppias";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppias.png");
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
        super.tiedostonNimi = "kauppias.png";
        super.katsomisTeksti = "Kylien kauppias";
        super.asetaTiedot();
    }
}
