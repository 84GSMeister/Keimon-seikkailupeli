package keimo.kenttäkohteet.kiintopiste;

import keimo.Pelaaja;
import keimo.PääIkkuna;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;
import keimo.kenttäkohteet.esine.Ponuainekset;

import javax.swing.ImageIcon;

public class KauppaRuutu extends Kiintopiste {
    
    @Override
    public String vuorovaikuta(Esine e) {
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
                int tyhjätPaikat = 0;
                for (Esine esine : Pelaaja.esineet) {
                    if (esine == null) {
                        tyhjätPaikat++;
                    }
                }
                if (Pelaaja.ostosKori.size() - tyhjätPaikat > 0) {
                    PääIkkuna.avaaDialogi(null, "Ostokset ei mahdu tavaraluetteloon.", "");
                }
                else {
                    //PääIkkuna.avaaPitkäDialogiRuutu("kauppa_normaali");
                    Pelaaja.raha -= Pelaaja.ostostenHintaYhteensä;
                    Pelaaja.ostostenHintaYhteensä = 0;
                    for (Esine ostos : Pelaaja.ostosKori) {
                        Pelaaja.annaEsine(ostos);
                    }
                    Pelaaja.tyhjennäOstoskori();
                }
            }
            else {
                Pelaaja.tyhjennäOstoskori();
            }
        }
        else if (Pelaaja.ostostenHintaYhteensä <= 0) {
            PääIkkuna.avaaDialogi(this.annaDialogiKuvake(), "Meinasitko ostaa jotain?", "ASS-Market kassa");
        }
        else if (Pelaaja.raha >= Pelaaja.ostostenHintaYhteensä) {
            int tyhjätPaikat = 0;
            for (Esine esine : Pelaaja.esineet) {
                if (esine == null) {
                    tyhjätPaikat++;
                }
            }
            if (Pelaaja.ostosKori.size() - tyhjätPaikat > 0) {
                PääIkkuna.avaaDialogi(null, "Ostokset ei mahdu tavaraluetteloon.", "");
            }
            else {
                PääIkkuna.avaaPitkäDialogiRuutu("kauppa_normaali");
                Pelaaja.raha -= Pelaaja.ostostenHintaYhteensä;
                Pelaaja.ostostenHintaYhteensä = 0;
                for (Esine ostos : Pelaaja.ostosKori) {
                    Pelaaja.annaEsine(ostos);
                }
                Pelaaja.tyhjennäOstoskori();
            }
        }
        else {
            PääIkkuna.avaaPitkäDialogiRuutu("kauppa_eivaraa");
            Pelaaja.tyhjennäOstoskori();
        }
        return "";
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kaupparuutu";
            case "genetiivi":    return "Kaupparuudun";
            case "esiivi":       return "Kaupparuutuna";
            case "partitiivi":   return "Kaupparuutua";
            case "translatiivi": return "Kaupparuuduksi";
            case "inessiivi":    return "Kaupparuudussa";
            case "elatiivi":     return "Kaupparuudusta";
            case "illatiivi":    return "Kaupparuutuun";
            case "adessiivi":    return "Kaupparuudulla";
            case "ablatiivi":    return "Kaupparuudulta";
            case "allatiivi":    return "Kaupparuudulle";
            default:             return "Kaupparuutu";
        }
    }
    
    public KauppaRuutu (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Kaupparuutu";
        super.tiedostonNimi = "kaupparuutu.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
        super.dialogiTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
        super.katsomisTeksti = "Kylien kauppias";
        super.erillisDialogi = true;
        super.ignooraaEsineValintaDialogissa = true;
        super.ohitaDialogiTesktiboksi = true;
        super.asetaTiedot();
    }
}
